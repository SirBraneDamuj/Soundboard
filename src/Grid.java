import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

public class Grid extends JPanel {
  private int numRows;
  private int numColumns;
  private Vector<String> soundList;

  public Grid() {
    super();
    this.soundList = DAO.getSoundList();
    this.numColumns = 6;
    this.numRows = soundList.size()/numColumns + 1; //extra row for the remainder
    this.setLayout(new GridLayout(numRows, numColumns, 10, 10));
    buildButtons();
  }

  public void buildButtons() {
    int row = 0;
    int col = 0;
    for(int i=0;i<soundList.size();i++) {
      PlayButton b = new PlayButton(soundList.get(i), row, col);
      this.add(b);
      col++;
      if(col > numColumns) {
        col = 0;
        row++;
      }
    }
  }

  private void addNewButton(String name) {
    this.numRows = soundList.size()/numColumns + 1; //extra row for the remainder
    removeAll();
    this.setLayout(new GridLayout(numRows, numColumns, 10, 10));
    buildButtons();
  }

  public void add(File newSound) throws IOException {
    if(soundList.contains(newSound.getName())) {
      return;
    }
    else {
      DAO.copyFile(newSound);
    }
    soundList.add(newSound.getName());
    addNewButton(newSound.getName());
  }
}
