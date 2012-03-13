import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

public class Soundboard {
  private static Soundboard instance;
  public static Soundboard getInstance() {
    if(instance == null) {
      instance = new Soundboard();
    }
    return instance;
  }

  private JFrame mainFrame;
  private PlayButton nowPlaying;
  private JFileChooser fileChooser;
  private Vector<ListViewController> listViewControllers;
  private JList listOfLists;
  private DefaultListModel listModel;
  private JPanel gridCards;
  private CardLayout cardLayout;
  private DAO dao;

  public Soundboard() {
    this.mainFrame = new JFrame();
    this.mainFrame.setTitle("Zanzibar");
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.fileChooser = new JFileChooser();
    this.fileChooser.setFileFilter(new MP3FileFilter());
    this.cardLayout = new CardLayout();
    this.gridCards = new JPanel(cardLayout);
    this.listViewControllers = new Vector<ListViewController>();
    this.listModel = new DefaultListModel();
    this.listOfLists = new JList(listModel);
    this.dao = DAO.getInstance();
    initializeViewControllers();
    initializeComponents();
  }

  private void initializeViewControllers() {
    Vector<Sound> sounds = dao.getSoundsInDB();
    ListViewController mainList = new ListViewController(new List(), sounds);
    listViewControllers.add(mainList);
    listModel.addElement(mainList);
    for(List l : dao.getListsInDB()) {
      Vector<Sound> listSounds = new Vector<Sound>();
      for(Sound s : sounds) {
        if(s.getListID() == l.getID()) {
          listSounds.add(s);
        }
      }
      ListViewController lvc = new ListViewController(l, listSounds);
      listViewControllers.add(lvc);
      listModel.addElement(lvc);
    }
  }

  private void initializeComponents() {
    //init main panel with borderlayout
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(20, 20));
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, initLeftSide(), initRightSide());
    mainPanel.add(split, BorderLayout.CENTER);
    mainPanel.add(initStopButton(), BorderLayout.LINE_END);

    mainFrame.setContentPane(mainPanel);
  }

  private JPanel initLeftSide() {
    JPanel leftSide = new JPanel();
    leftSide.setLayout(new BorderLayout());

    //init list
    listOfLists.setMinimumSize(new Dimension(150, 400));
    listOfLists.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if(!e.getValueIsAdjusting()) {
          changeList(listOfLists.getSelectedIndex());
        }
      }
    });
    listOfLists.setSelectedIndex(0);

    //init list scroll pane
    JScrollPane listPane = new JScrollPane(listOfLists);
    leftSide.add(listPane, BorderLayout.CENTER);

    //init left side label
    leftSide.add(new JLabel("Lists"), BorderLayout.PAGE_START);

    //init new/edit buttons
    JPanel bottomButtons = new JPanel();
    JButton newList = new JButton("New");
    newList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addList();
      }
    });
    JButton editList = new JButton("Edit");
    bottomButtons.add(newList);
    bottomButtons.add(editList);
    leftSide.add(bottomButtons, BorderLayout.PAGE_END);
    return leftSide;
  }

  private JPanel initRightSide() {
    JPanel rightSide = new JPanel();
    rightSide.setLayout(new BorderLayout());

    //init cards for card layout
    for(ListViewController l : listViewControllers) {
      gridCards.add(l.getGrid(), Integer.toString(l.getList().getID()));
    }

    //init grid scrollpane
    JScrollPane buttonPane = new JScrollPane(gridCards);
    buttonPane.setPreferredSize(new Dimension(850, 400));
    buttonPane.setMinimumSize(new Dimension(850, 400));
    buttonPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    rightSide.add(buttonPane, BorderLayout.CENTER);

    //init right side label
    rightSide.add(new JLabel("Sounds"), BorderLayout.PAGE_START);

    //init new button
    JPanel bottomButtons = new JPanel();
    bottomButtons.add(Box.createHorizontalGlue());
    JButton newButton = new JButton("New");
    newButton.setAlignmentY(Component.CENTER_ALIGNMENT);
    newButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addSongs();
      }
    });
    bottomButtons.add(newButton);
    bottomButtons.add(Box.createHorizontalGlue());
    rightSide.add(bottomButtons, BorderLayout.PAGE_END);
    return rightSide;
  }

  public JButton initStopButton() {
    JButton stop = new JButton("STOP");
    stop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(nowPlaying != null && !nowPlaying.isFinished()) {
          nowPlaying.stop();
          nowPlaying = null;
        }
      }
    });
    return stop;
  }

  public void show() {
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }

  public void nowPlaying(PlayButton p) {
    if(nowPlaying != null && nowPlaying != p && !nowPlaying.isFinished()) {
      nowPlaying.stop();
    }
    nowPlaying = p;
  }

  public void addSongs() {
    if(fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();
      SoundDialog d = new SoundDialog(f.getName(), listViewControllers, mainFrame);
      d.showDialog();
      if(!d.getOk()) {
        return;
      }
      Sound newSound = null;
      ListViewController lvc = listViewControllers.get(d.getSelectedIndex());
      try {
        newSound = new Sound(DAO.getInstance().getLargestIDFor("sounds")+1, lvc.getList().getID(), d.getName(), d.getDescription(), DAO.copyFile(f));
      } catch(IOException e) {
        JOptionPane.showMessageDialog(mainFrame,
            "There was an unknown error while copying the sound file",
            "Error copying sound",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      listViewControllers.get(0).addSound(newSound);
      if(newSound.getListID() != -1) {
        lvc.addSound(newSound);
      }
      if(!newSound.save()) {
        JOptionPane.showMessageDialog(mainFrame,
            "Unable to save the sound to the DB",
            "DB error",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
    }
  }

  public void addList() {
    ListDialog d = new ListDialog("New List", mainFrame);
    d.showDialog();
    if(!d.getOk()) {
      return;
    }
    List newList = new List(DAO.getInstance().getLargestIDFor("lists")+1, d.getName(), d.getDescription());
    if(!newList.save()) {
      JOptionPane.showMessageDialog(mainFrame,
          "Unable to save the list to the DB",
          "DB error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    ListViewController lvc = new ListViewController(newList, new Vector<Sound>());
    listViewControllers.add(lvc);
    listModel.addElement(lvc);
    gridCards.add(lvc.getGrid(), Integer.toString(lvc.getList().getID()));
  }

  public void changeList(int index) {
    int selectedList = listOfLists.getSelectedIndex();
    cardLayout.show(gridCards, Integer.toString(listViewControllers.get(selectedList).getList().getID()));
    mainFrame.pack();
    mainFrame.validate();
  }

  public void editSound(SoundPanel p) {
    Sound s = p.getSound();
    System.out.println(s.getListID());
    SoundDialog d = new SoundDialog(s, listViewControllers, mainFrame);
    d.showDialog();
    if(d.getOk()) {
      s.setName(d.getName());
      s.setDescription(d.getDescription());
      ListViewController lvc = listViewControllers.get(d.getSelectedIndex());
      if(lvc.getList().getID() == s.getListID()) { //list did not change
        lvc.soundDidEdit(s);
      }
      else if(lvc.getList().getID() == -1) { //list was set to "all sounds", remove from current list
        removeSoundFromList(s);
        s.setListID(-1);
      }
      else {
        if(s.getListID() != -1) {
          removeSoundFromList(s);
        }
        s.setListID(lvc.getList().getID());
        lvc.addSound(s);
      }
      listViewControllers.get(0).soundDidEdit(s);
      s.save();
      SwingUtilities.updateComponentTreeUI(mainFrame); //<3
    }
  }

  public void deleteSound(SoundPanel p) {
    
  }
  
  public void removeSoundFromList(Sound s) {
    for(int i=0;i<listViewControllers.size();i++) {
      if(listViewControllers.get(i).getList().getID() == s.getListID()) {
        listViewControllers.get(i).soundWasRemoved(s);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    DAO dao = DAO.getInstance();
    if(!dao.tableExists("sounds")) {
      dao.createTable("sounds", "id integer primary key autoincrement, listid integer, name,description null,filename");
    }
    if(!dao.tableExists("lists")) {
      dao.createTable("lists", "id integer primary key autoincrement,name,description null");
    }
    File soundsDir = new File("Sounds");
    if(!soundsDir.exists()) {
      soundsDir.mkdir();
    }
    Soundboard.getInstance().show();
  }
}

class MP3FileFilter extends FileFilter {
  public boolean accept(File f) {
    return f.getName().endsWith(".mp3") || f.isDirectory();
  }

  public String getDescription() {
    return "MP3 Files";
  }
}

class PopClickListener extends MouseAdapter {
  public void mousePressed(MouseEvent e) {
    if(e.isPopupTrigger()) {
      doPop(e);
    }
  }

  public void mouseReleased(MouseEvent e) {
    if(e.isPopupTrigger()) {
      doPop(e);
    }
  }

  private void doPop(MouseEvent e) {
    SoundMenu menu = new SoundMenu(e);
    menu.show(e.getComponent(), e.getX(), e.getY());
  }
}

