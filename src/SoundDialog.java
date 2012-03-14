import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class SoundDialog extends ModelDialog {
  private JComboBox listComboBox;

  public SoundDialog(String name, Vector<ListViewController> lvcs, Frame owner) {
    super("New Sound", name, owner);
    this.listComboBox = new JComboBox(lvcs);
    build();
  }

  public SoundDialog(Sound s, Vector<ListViewController> lvcs, Frame owner) {
    super("Edit Sound", s.getName(), owner);
    this.listComboBox = new JComboBox(lvcs);
    build();
    this.description.setText(s.getDescription());
    for(int i=0;i<lvcs.size();i++) {
      if(lvcs.get(i).getList().getID() == s.getListID()) {
        listComboBox.setSelectedIndex(i);
      }
    }
  }

  protected void build() {
    DefaultComboBoxModel model = (DefaultComboBoxModel)(listComboBox.getModel());
    listComboBox.setSelectedIndex(0);
    mainPanel.add(listComboBox);
    addButtons();
  }

  public int getSelectedIndex() {
    return listComboBox.getSelectedIndex();
  }
}
