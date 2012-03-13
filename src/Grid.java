import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Grid extends JPanel {
  private JPanel[][] cells;
  private int currentRow;
  private int currentColumn;
  public static final int numRows = 10;
  public static final int numColumns = 5;

  public Grid() {
    super();
    this.setLayout(new GridLayout(numRows, numColumns, 10, 10));
    this.cells = new JPanel[numRows][numColumns];
    initializeGrid();
  }

  public void initializeGrid() {
    for(int i=0;i<numRows;i++) {
      for(int j=0;j<numColumns;j++) {
        cells[i][j] = new JPanel();
        add(cells[i][j]);
      }
    }
  }

  public void setPanelAtLocation(SoundPanel sp, int x, int y) {
    cells[x][y].removeAll();
    cells[x][y].add(sp);
  }

  public void removePanelAtLocation(int x, int y) {
    cells[x][y].removeAll();
  }
}
