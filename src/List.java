import java.util.Vector;
import java.sql.SQLException;

public class List extends Model {
  private int id;
  private String name;
  private String description;

  public List() {
    super(-1, "All Sounds", "All sounds in the system");
  }

  public List(int id, String name, String description) {
    super(id, name, description);
  }

  public boolean save() {
    return id == -1 ? false : DAO.getInstance().saveList(this);
  }
}

