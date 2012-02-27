import java.io.*;

public class DAO {
  public static String[] getSoundList() {
    File dir = new File("Sounds");
    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".mp3") || name.endsWith(".wav");
      }
    };
    String[] contents = dir.list(filter);
    return contents;
  }
}
