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

  public Soundboard() {
    this.mainFrame = new JFrame();
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    build();
  }

  private void build() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    JPanel buttonGrid = new Grid();
    mainPanel.add(buttonGrid, BorderLayout.CENTER);    
    mainFrame.setContentPane(mainPanel);
  }

  public void show() {
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }

  public static void main(String[] args) {
    Soundboard.getInstance().show();
  }
}
