import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Grid extends JPanel {
  private int numRows;
  private int numColumns;
  private Vector<Sound> soundList;
  private JPanel[][] cells;
  private int currentRow;
  private int currentColumn;

  public Grid(Vector<Sound> sounds) {
    super();
    this.soundList = sounds;
    this.numRows = 10;
    this.numColumns = 5;
    this.setLayout(new GridLayout(numRows, numColumns, 10, 10));
    this.cells = new JPanel[numRows][numColumns];
    buildButtons();
  }

  public void buildButtons() {
    int count = 0;
    currentRow = 0;
    currentColumn = -1;
    for(int i=0;i<numRows;i++) {
      for(int j=0;j<numColumns;j++) {
        cells[i][j] = new JPanel();
        if(count < soundList.size()) {
          cells[i][j].add(new SoundPanel(soundList.get(count)));
          currentRow = i;
          currentColumn = j;
        }
        add(cells[i][j]);
        count++;
      }
    }
  }

  public void add(Sound newSound) {
    addNewButton(newSound);
  }

  private void addNewButton(Sound s) {
    currentColumn++;
    if(currentColumn >= numColumns) {
      currentColumn = 0;
      currentRow++;
    }
    SoundPanel p = new SoundPanel(s);
    cells[currentRow][currentColumn].add(p);
  }
}
