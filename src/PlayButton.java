import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

public class PlayButton extends JButton {
  private Sound sound;
  private MP3Player p;

  public PlayButton(Sound sound) {
    super("Play");
    this.sound = sound;
    try {
      this.p = new MP3Player(getSound());
    } catch(FileNotFoundException e) {
      e.printStackTrace();
    }
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        p.play();
        Soundboard.getInstance().nowPlaying(PlayButton.this);
      }
    });
  }

  public void stop() {
    p.stop();
  }

  public Sound getSound() {
    return sound;
  }
}
