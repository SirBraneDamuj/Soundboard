import java.io.File;
public class Zanzibar {
  public static void main(String[] args) throws Exception {
    DAO dao = DAO.getInstance();
    if(!dao.tableExists("sounds")) {
      dao.createTable("sounds", "id integer primary key autoincrement, listid integer, name,description null,filename");
    }
    if(!dao.tableExists("lists")) {
      dao.createTable("lists", "id integer primary key autoincrement,name,description null");
    }
    File soundsDir = new File("Sounds");
    if(!soundsDir.exists()) {
      soundsDir.mkdir();
    }
    Soundboard.getInstance().show();
  }
}
