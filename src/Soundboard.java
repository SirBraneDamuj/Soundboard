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
  private Grid buttonGrid;
  private JList soundList;
  private DefaultListModel listModel;
  private List mainList;
  private JPanel gridCards;
  private Vector<List> lists;

  public Soundboard() {
    this.mainFrame = new JFrame();
    this.mainFrame.setTitle("Zanzibar");
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.fileChooser = new JFileChooser();
    this.fileChooser.setFileFilter(new MP3FileFilter());
    this.lists = DAO.getInstance().getListsInDB();
    this.listModel = new DefaultListModel();
    this.soundList = new JList(listModel);
    this.mainList = new List();
    this.gridCards = new JPanel(new CardLayout());
    this.buttonGrid = mainList.getGrid();
    build();
  }

  private void build() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(20, 20));

    JPanel rightSide = new JPanel();
    gridCards.add(mainList.getGrid(), Integer.toString(mainList.getID()));
    soundList.setSelectedIndex(0);
    rightSide.setLayout(new BorderLayout());
    JScrollPane buttonPane = new JScrollPane(gridCards);
    buttonPane.setPreferredSize(new Dimension(850, 400));
    buttonPane.setMinimumSize(new Dimension(850, 400));
    buttonPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    rightSide.add(buttonPane, BorderLayout.CENTER);
    rightSide.add(new JLabel("Sounds"), BorderLayout.PAGE_START);

    JPanel leftSide = new JPanel();
    leftSide.setLayout(new BorderLayout());
    soundList.setMinimumSize(new Dimension(150, 400));
    soundList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if(!e.getValueIsAdjusting()) {
          changeList(soundList.getSelectedIndex());
        }
      }
    });
    lists.add(mainList);
    listModel.addElement(mainList);
    for(List l : lists) {
      if(l != mainList) {
        gridCards.add(l.getGrid(), Integer.toString(l.getID()));
        listModel.addElement(l);
      }
    }
    JScrollPane listPane = new JScrollPane(soundList);
    leftSide.add(listPane, BorderLayout.CENTER);
    leftSide.add(new JLabel("Lists"), BorderLayout.PAGE_START);

    JPanel bottomLeftButtons = new JPanel();
    JButton newList = new JButton("New");
    newList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addList();
      }
    });
    JButton editList = new JButton("Edit");
    bottomLeftButtons.add(newList);
    bottomLeftButtons.add(editList);
    leftSide.add(bottomLeftButtons, BorderLayout.PAGE_END);

    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSide, rightSide);
    
    mainPanel.add(split, BorderLayout.CENTER);

    JButton stop = new JButton("STOP");
    stop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(nowPlaying != null && !nowPlaying.isFinished()) {
          nowPlaying.stop();
          nowPlaying = null;
        }
      }
    });
    mainPanel.add(stop, BorderLayout.LINE_END);

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
    JButton editButton = new JButton("Edit");
    editButton.setAlignmentY(Component.CENTER_ALIGNMENT);
    bottomButtons.add(editButton);
    bottomButtons.add(Box.createHorizontalGlue());
    rightSide.add(bottomButtons, BorderLayout.PAGE_END);

    mainFrame.setContentPane(mainPanel);
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
      SoundDialog d = new SoundDialog(f.getName(), lists, mainFrame);
      d.showDialog();
      if(!d.getOk()) {
        return;
      }
      Sound newSound = null;
      try {
        newSound = new Sound(DAO.getInstance().getLargestIDFor("sounds")+1, d.getName(), d.getDescription(), DAO.copyFile(f));
      } catch(IOException e) {
        JOptionPane.showMessageDialog(mainFrame,
            "There was an unknown error while copying the sound file",
            "Error copying sound",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      if(!newSound.save()) {
        JOptionPane.showMessageDialog(mainFrame,
            "Unable to save the sound to the DB",
            "DB error",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      List l = d.getSelectedList();
      if(l != null) {
        l.addSound(newSound);
        l.save();
      }
      mainList.addSound(newSound);
      mainFrame.pack();
      mainFrame.validate();
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
    listModel.addElement(newList);
    lists.add(newList);
    gridCards.add(newList.getGrid(), Integer.toString(newList.getID()));
  }

  public void changeList(int index) {
    CardLayout cl = (CardLayout)(gridCards.getLayout());
    List selectedList = (List)(listModel.elementAt(index));
    cl.show(gridCards, Integer.toString(selectedList.getID()));
    mainFrame.pack();
    mainFrame.validate();
  }

  public void editSound(SoundPanel p) {
    
  }

  public void deleteSound(SoundPanel p) {
    
  }

  public static void main(String[] args) throws Exception {
    DAO dao = DAO.getInstance();
    if(!dao.tableExists("sounds")) {
      dao.createTable("sounds", "id integer primary key autoincrement,name,description null,filename");
    }
    if(!dao.tableExists("lists")) {
      dao.createTable("lists", "id integer primary key autoincrement,name,description null");
    }
    if(!dao.tableExists("soundlist")) {
      dao.createTable("soundlist", "soundid integer,listid integer, primary key (soundid, listid), foreign key (soundid) references sounds(id), foreign key(listid) references lists(id)");
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
