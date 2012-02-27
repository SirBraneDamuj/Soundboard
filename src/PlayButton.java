import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

public class PlayButton extends JButton {
  private int row;
  private int column;
  private String filename;
  private MP3Player p;

  public PlayButton(String filename, int row, int column) {
    super("Play");
    this.row = row;
    this.column = column;
    this.filename = "Sounds/" + filename;
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try {
          p = new MP3Player(getFilename());
          p.play();
          Soundboard.getInstance().nowPlaying(PlayButton.this);
        } catch(FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void stop() {
    p.stop();
  }

  public String getFilename() {
    return filename;
  }
}
