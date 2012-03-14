import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ListDialog extends ModelDialog {
  public ListDialog(String name, Frame owner) {
    super("New List", name, owner);
    build();
  }

  public ListDialog(ListViewController lvc, Frame owner) {
    super("Edit List", lvc.getList().getName(), owner);
    this.description.setText(lvc.getList().getDescription());
    build();
  }

  protected void build() {
    addButtons();
  }
}

