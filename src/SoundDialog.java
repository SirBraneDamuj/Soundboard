import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class SoundDialog extends ModelDialog {
  private JComboBox listComboBox;

  public SoundDialog(String name, Vector<List> lists, Frame owner) {
    super("Sound", name, owner);
    this.listComboBox = new JComboBox(lists);
    build();
  }

  private void build() {
    DefaultComboBoxModel model = (DefaultComboBoxModel)(listComboBox.getModel());
    listComboBox.setSelectedIndex(0);
    mainPanel.add(listComboBox);
    addButtons();
  }

  public List getSelectedList() {
    if(listComboBox.getSelectedIndex() == 0) {
      return null;
    }
    else {
      return (List)(listComboBox.getSelectedItem());
    }
  }
}
