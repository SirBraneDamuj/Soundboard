import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class NewDialog extends JDialog {
  private JTextField name;
  private JTextArea description;
  private boolean ok;

  public NewDialog(String name, Frame owner) {
    super(owner, "New", true);
    this.name = new JTextField(30);
    this.name.setText(name);
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
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setOk(true);
        setVisible(false);
      }
    });
    buttonsPanel.add(ok);
    buttonsPanel.add(Box.createHorizontalGlue());
    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setOk(false);
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

  public String getName() {
    return name.getText();
  }

  public String getDescription() {
    return description.getText();
  }
  
  public boolean getOk() {
    return ok;
  }

  private void setOk(boolean b) {
    ok = b;
  }
}
