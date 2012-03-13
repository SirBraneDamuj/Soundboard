import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SoundPanel extends JPanel {
  private Sound sound;
  private PlayButton play;
  public SoundPanel(Sound sound) {
    super();
    this.sound = sound;
    this.play = new PlayButton(sound);
    build();
  }

  private void build() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(140, 60));
    add(play);
    add(new JLabel("<html>" + sound.getName() + "</html>")); //for some reason this enables word wrap
    setToolTipText(sound.getDescription());
    addMouseListener(new PopClickListener());
  }
}
