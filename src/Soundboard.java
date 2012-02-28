import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

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

  public Soundboard() {
    this.mainFrame = new JFrame();
    this.mainFrame.setTitle("Soundboard");
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    build();
  }

  private void build() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(20, 20));

    JPanel buttonGrid = new Grid();
    mainPanel.add(buttonGrid, BorderLayout.CENTER);    

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

  public static void main(String[] args) {
    Soundboard.getInstance().show();
  }
}
