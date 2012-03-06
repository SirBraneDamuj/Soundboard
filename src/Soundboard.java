import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;

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
    this.buttonGrid = new Grid();

    build();
  }

  private void build() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(20, 20));

    JScrollPane buttonPane = new JScrollPane(buttonGrid);
    buttonPane.setPreferredSize(new Dimension(400, 400));
    mainPanel.add(buttonPane, BorderLayout.CENTER);    

    JButton stop = new JButton("STOP");
    stop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nowPlaying.stop();
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
    if(nowPlaying != null) {
      nowPlaying.stop();
    }
    nowPlaying = p;
  }

  public void addSongs() {
    if(fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();
      System.out.println(f.getPath());
      NewSoundDialog d = new NewSoundDialog(f, mainFrame);
      d.showDialog();
      try {
        buttonGrid.add(f);
      } catch(IOException e) {
        e.printStackTrace();
      }
      mainFrame.pack();
      mainFrame.validate();
    }
  }

  public static void main(String[] args) {
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
