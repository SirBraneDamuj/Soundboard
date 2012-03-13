import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SoundPanel extends JPanel {
  private Sound sound;
  private PlayButton play;
  public SoundPanel(Sound sound) {
    super();
    this.sound = sound;
    setToolTipText(sound.getDescription());
    initialize();
  }

  private void initialize() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(140, 60));
    add(new PlayButton(sound));
    add(new JLabel("<html>" + sound.getName() + "</html>")); //for some reason this enables word wrap
    addMouseListener(new PopClickListener());
  }

  public Sound getSound() {
    return sound;
  }
}
