import java.io.*;
import java.util.Vector;
import java.util.Arrays;
import java.nio.channels.FileChannel;

public class DAO {
  public static Vector<Sound> getSoundList() {
    File dir = new File("Sounds");
    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith("mp3");
      }
    };
    File[] contents = dir.listFiles(filter);
    Vector<Sound> retval = new Vector<Sound>();
    if(contents != null) {
      for(File f : contents) {
        retval.add(new Sound(f.getName(), "", f));
      }
    }
    return retval;
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
