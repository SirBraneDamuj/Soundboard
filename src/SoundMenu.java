import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SoundMenu extends JPopupMenu {
  private JMenuItem editItem;
  private JMenuItem deleteItem;
  private MouseEvent event;

  public SoundMenu(MouseEvent e) {
    this.editItem = new JMenuItem("Edit");
    this.deleteItem = new JMenuItem("Delete");
    this.event = e;
    editItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Soundboard.getInstance().editSound((SoundPanel)(getEvent().getComponent()));
      }
    });
    deleteItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Soundboard.getInstance().deleteSound((SoundPanel)(getEvent().getComponent()));
      }
    });
    add(editItem);
    add(deleteItem);
  }

  public MouseEvent getEvent() {
    return this.event;
  }
}
