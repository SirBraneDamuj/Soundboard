import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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

  public Soundboard() {
    this.mainFrame = new JFrame();
    this.mainFrame.setTitle("Soundboard");
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.fileChooser = new JFileChooser();
    this.fileChooser.setFileFilter(new MP3FileFilter());
    try {
      this.buttonGrid = new Grid();
    } catch(SQLException e) {
      JOptionPane.showMessageDialog(mainFrame,
          "There was an unknown error while creating the grid. Try clearing the database and try again.",
          "DB error",
          JOptionPane.ERROR_MESSAGE);
    }

    build();
  }

  private void build() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(20, 20));

    JScrollPane buttonPane = new JScrollPane(buttonGrid);
    buttonPane.setPreferredSize(new Dimension(850, 400));
    buttonPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    mainPanel.add(buttonPane, BorderLayout.CENTER);    

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
    bottomButtons.setLayout(new BoxLayout(bottomButtons, BoxLayout.X_AXIS));
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
    mainPanel.add(bottomButtons, BorderLayout.PAGE_END);

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
      NewSoundDialog d = new NewSoundDialog(f, mainFrame);
      d.showDialog();
      if(!d.getOk()) {
        return;
      }
      Sound newSound = null;
      try {
        newSound = new Sound(d.getSoundName(), d.getSoundDescription(), DAO.copyFile(f));
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
      buttonGrid.add(newSound);
      mainFrame.pack();
      mainFrame.validate();
    }
  }

  public static void main(String[] args) throws Exception {
    DAO dao = DAO.getInstance();
    if(!dao.tableExists("sounds")) {
      dao.createTable("sounds", "name,description null,filename");
    }
    File soundsDir = new File("Sounds");
    if(soundsDir.exists() && soundsDir.isDirectory()) {
      Soundboard.getInstance().show();
    }
    else {
      System.out.println("Error: Missing Sounds directory. Please make sure there is a directory named Sounds at the same level as this jar.");
    }
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
