import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Grid extends JPanel {
  private int numRows;
  private int numColumns;
  private String[] soundList;

  public Grid() {
    super();
    this.soundList = DAO.getSoundList();
    this.numColumns = 6;
    this.numRows = soundList.length/numColumns + 1; //extra row for the remainder
    this.setLayout(new GridLayout(numRows, numColumns));
    buildButtons();
  }

  public void buildButtons() {
    int row = 0;
    int col = 0;
    for(int i=0;i<soundList.length;i++) {
      PlayButton b = new PlayButton(soundList[i], row, col);
      this.add(b);
      col++;
      if(col > numColumns) {
        col = 0;
        row++;
      }
    }
  }
}
