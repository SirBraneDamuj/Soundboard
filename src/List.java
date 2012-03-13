import java.util.Vector;

public class List {
  private int id;
  private String name;
  private String description;
  private Vector<Sound> sounds;
  private Grid grid;

  public List() {
    this.id = -1;
    this.name = "All sounds";
    this.description = "All sounds in the system";
    this.sounds = DAO.getInstance().getSoundsInDB();
    this.grid = new Grid(this.sounds);
  }

  public List(int id, String name, String description) {
    this(name, description);
    this.id = id;
  }

  public List(String name, String description) {
    this.name = name;
    this.description = description;
    this.sounds = new Vector<Sound>();
    this.grid = new Grid(sounds);
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

  public boolean save() {
    return DAO.getInstance().saveList(this);
  }

  @Override
  public String toString() {
    return this.name;
  }

  public void addSound(Sound s) {
    if(getID() >= 0) {
      System.out.println("XYZ");
    }
    sounds.add(s);
    grid.add(s);
  }

  public int getID() {
    return this.id;
  }

  public Vector<Sound> getSoundList() {
    return sounds;
  }

  public Grid getGrid() {
    return this.grid;
  }

  private String quotacise(String thing) {
    if(thing == null || thing.equals("")) {
      return "null";
    }
    return "\"" + thing + "\"";
  }
}

