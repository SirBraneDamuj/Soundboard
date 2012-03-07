import java.io.File;
import java.io.IOException;

public class Sound {
  private String name;
  private String description;
  private File file;

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
    return DAO.getInstance().insertValues("sounds", quotacise(name), quotacise(description), quotacise(file.getAbsolutePath()));
  }

  private String quotacise(String thing) {
    if(thing == null || thing.equals("")) {
      return "null";
    }
    return "\"" + thing + "\"";
  }
}
