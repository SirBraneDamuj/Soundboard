import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

import javazoom.jl.player.advanced.*;

public class PlayButton extends JButton {
  private Sound sound;
  private MP3Player p;

  public PlayButton(Sound sound) {
    super("Play");
    this.sound = sound;
    this.p = new MP3Player(getSound());
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        p.play(PlayButton.this);
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
