import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    buildGrid();
  }

  private void buildGrid() {
    
  }

  public void show() {
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }

  public static void main(String[] args) {
    Soundboard.getInstance().show();
  }
}
