public abstract class Model {
  protected int id;
  protected String name;
  protected String description;

  public Model(int id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public int getID() {
    return this.id;
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

  public abstract boolean save();

  @Override
  public String toString() {
    return this.name;
  }

}
