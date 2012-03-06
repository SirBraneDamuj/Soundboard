import java.io.*;
import java.util.Vector;
import java.util.Arrays;
import java.nio.channels.FileChannel;

public class DAO {
  public static Vector<String> getSoundList() {
    File dir = new File("Sounds");
    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith("mp3");
      }
    };
    String[] contents = dir.list(filter);
    return contents == null ? new Vector<String>() : new Vector<String>(Arrays.asList(contents));
  }

  public static void copyFile(File sourceFile) throws IOException {
    File destFile = new File("Sounds/" + sourceFile.getName());
    if(!destFile.exists()) {
      destFile.createNewFile();
    }

    FileChannel source = null;
    FileChannel destination = null;

    try {
      source = new FileInputStream(sourceFile).getChannel();
      destination = new FileOutputStream(destFile).getChannel();
      destination.transferFrom(source, 0, source.size());
    }
    finally {
      if(source != null) {
        source.close();
      }
      if(destination != null) {
        destination.close();
      }
    }
  }
}
