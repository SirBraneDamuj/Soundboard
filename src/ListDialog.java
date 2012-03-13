import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ListDialog extends ModelDialog {
  public ListDialog(String name, Frame owner) {
    super("List", name, owner);
    build();
  }

  protected void build() {
    addButtons();
  }
}

