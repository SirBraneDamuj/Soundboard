import java.io.File;
import java.io.IOException;

public class Sound extends Model {
  private int id;
  private String name;
  private String description;
  private int listID;
  private File file;

  public Sound(int id, int listID, String name, String description, File file) {
    super(id, name, description);
    this.listID = listID;
    this.file = file;
  }

  public int getListID() {
    return this.listID;
  }

  public void setListID(int listID) {
    this.listID = listID;
  }

  public File getFile() {
    return file;
  }

  public void setFile() {
    this.file = file;
  }
  
  public boolean save() {
    return DAO.getInstance().saveSound(this);
  }
}
