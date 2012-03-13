import java.io.File;
import java.io.IOException;

public class Sound {
  private int id;
  private String name;
  private String description;
  private File file;

  public Sound(int id, String name, String description, File file) {
    this(name, description, file);
    this.id = id;
  }

  public Sound(String name, String description, File file) {
    this.name = name;
    this.description = description;
    this.file = file;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  private String quotacise(String thing) {
    if(thing == null || thing.equals("")) {
      return "null";
    }
    return "\"" + thing + "\"";
  }

  public int getID() {
    return id;
  }
}
