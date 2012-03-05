import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class NewSoundDialog extends JDialog {
  private JTextField name;
  private JTextArea description;
  private File f;

  public NewSoundDialog(File f, Frame owner) {
    super(owner, "New sound", true);
    this.f = f;
    this.name = new JTextField(30);
    this.name.setText(f.getName());
    this.description = new JTextArea(2, 30);
    build();
  }

  private void build() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    mainPanel.add(Box.createVerticalGlue());

    JPanel namePanel = new JPanel();
    namePanel.setAlignmentY(Box.LEFT_ALIGNMENT);
    namePanel.add(new JLabel("Name: "));
    namePanel.add(name);
    mainPanel.add(namePanel);

    mainPanel.add(Box.createVerticalGlue());

    JPanel descPanel = new JPanel();
    JLabel desc = new JLabel("Description: ");
    descPanel.add(desc);
    descPanel.add(description);
    mainPanel.add(descPanel);

    mainPanel.add(Box.createVerticalGlue());

    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
    buttonsPanel.add(Box.createHorizontalGlue());
    JButton ok = new JButton("OK");
    buttonsPanel.add(ok);
    buttonsPanel.add(Box.createHorizontalGlue());
    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setVisible(false);
      }
    });
    buttonsPanel.add(cancel);
    buttonsPanel.add(Box.createHorizontalGlue());
    mainPanel.add(buttonsPanel);

    mainPanel.add(Box.createVerticalGlue());

    this.add(mainPanel);
  }

  public void showDialog() {
    pack();
    setVisible(true);
  }
}
