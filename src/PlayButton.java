import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

import javazoom.jl.player.advanced.*;

public class PlayButton extends JButton {
  private Sound sound;
  private MP3Player p;
  private boolean finished;

  public PlayButton(Sound sound) {
    super("Play");
    this.sound = sound;
    this.p = new MP3Player(getSound());
    this.finished = true;
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        Soundboard.getInstance().nowPlaying(PlayButton.this);
        p.play(PlayButton.this);
      }
    });
    setToolTipText(sound.getDescription());
  }

  public void stop() {
    p.stop();
  }

  public Sound getSound() {
    return sound;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean b) {
    finished = b;
  }
}
