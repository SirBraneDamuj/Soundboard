import java.io.*;
import java.util.Vector;
import java.util.Arrays;
import java.nio.channels.FileChannel;
import java.sql.*;

public class DAO {
  private static DAO instance;
  public static DAO getInstance() {
    if(instance == null) {
      try {
        instance = new DAO();
      } catch (ClassNotFoundException e) {
        System.out.println("Couldn't find sqlite JDBC...aborting");
        System.exit(1);
      }
    }
    return instance;
  }

  private Connection conn;

  private DAO() throws ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    try {
      this.conn = DriverManager.getConnection("jdbc:sqlite:soundboard.db");
    } catch(SQLException e) {
      System.out.println("Unable to initialize sqlite JDBC...aborting");
      System.exit(1);
    }
  }

  //columns should be a comma-separated string of column names
  //e.g.: "name,description,color"
  public boolean createTable(String tableName, String columns) {
    try {
      Statement stat = conn.createStatement();
      stat.executeUpdate("drop table if exists " + tableName + ";");
      stat.executeUpdate("create table " + tableName + " (" + columns + ")");
      stat.close();
    } catch(SQLException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  //values should be in the order of their columns in the table
  //strings need escaped quotes in them
  public boolean insertValues(String tableName, String... values) {
    String preparedStatement = "insert into " + tableName + " values (null,"; //auto increment
    for(int i=0;i<values.length;i++) {
      if(i == values.length-1) {
        preparedStatement += values[i] + ");";
      }
      else {
        preparedStatement += values[i] + ", ";
      }
    }
    try {
      Statement stat = conn.createStatement();
      stat.executeUpdate(preparedStatement);
      stat.close();
    } catch(SQLException e) {
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean saveSound(Sound s) {
    try {
      PreparedStatement prep = conn.prepareStatement("replace into sounds (id, name, description, filename) values (?, ?, ?, ?);");
      prep.setInt(1, s.getID());
      prep.setString(2, s.getName());
      prep.setString(3, s.getDescription());
      prep.setString(4, s.getFile().getAbsolutePath());
      prep.execute();
      return true;
    } catch(SQLException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  public boolean saveList(List l) {
    try {
      PreparedStatement prep = conn.prepareStatement("replace into lists (id, name, description) values (?, ?, ?);");
      prep.setInt(1, l.getID());
      prep.setString(2, l.getName());
      prep.setString(3, l.getDescription());
      prep.execute();

      prep = conn.prepareStatement("replace into soundlist (soundid, listid) values (?, ?);");
      for(Sound s : l.getSoundList()) {
        prep.setInt(1, s.getID());
        prep.setInt(2, l.getID());
        prep.addBatch();
      }
      prep.executeBatch();
      return true;
    } catch(SQLException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      return false;
    }
  }

  public int getLargestIDFor(String tableName) {
    try {
      PreparedStatement prep = conn.prepareStatement("select * from SQLITE_SEQUENCE where name=?;");
      prep.setString(1, tableName);
      ResultSet rs = prep.executeQuery();
      if(rs.next()) {
        int retval = rs.getInt("seq");
        rs.close();
        return retval;
      }
    } catch(SQLException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return -1;
  }

  public boolean tableExists(String tableName) {
    try {
      Statement stat = conn.createStatement();
      ResultSet rs = stat.executeQuery("select name from sqlite_master where type=\"table\" and name=\"" + tableName + "\";");
      boolean retval =  rs.getString("name") != null && rs.getString("name").equals(tableName);
      rs.close();
      return retval;
    } catch(SQLException e) {
      return false;
    }
  }

  public Vector<Sound> getSoundsInDB() {
    try {
      Vector<Sound> retval = new Vector<Sound>();
      Statement stat = conn.createStatement();
      ResultSet rs = stat.executeQuery("select * from sounds;");
      while(rs.next()) {
        Sound s = new Sound(rs.getInt("id"), rs.getString("name"), rs.getString("description"), new File(rs.getString("filename")));
        retval.add(s);
      }
      rs.close();
      return retval;
    } catch(SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Sound getSoundForID(int id) throws SQLException {
    Statement stat = conn.createStatement();
    ResultSet rs = stat.executeQuery("select * from sounds where id=" + id + ";");
    if(rs.next()) {
      Sound newSound = new Sound(rs.getInt("id"), rs.getString("name"), rs.getString("description"), new File(rs.getString("filename")));
      rs.close();
      return newSound;
    }
    else {
      rs.close();
      return null;
    }
  }

  public Vector<List> getListsInDB() {
    Vector<List> retval = new Vector<List>();
    try { 
      Statement stat = conn.createStatement();
      ResultSet rs = stat.executeQuery("select * from lists;");
      while(rs.next()) {
        List l = new List(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
        retval.add(l);
      }
      rs.close();
      for(List l : retval) {
        ResultSet sounds = stat.executeQuery("select * from soundlist where listid="+l.getID()+";");
        while(sounds.next()) {
          l.addSound(getSoundForID(sounds.getInt("soundid")));
        }
        sounds.close();
      }
    } catch(SQLException e) {
      e.printStackTrace();
    }
    return retval;
  }

  //returns null if the destination already exists
  public static File copyFile(File sourceFile) throws IOException {
    File destFile = new File("Sounds/" + sourceFile.getName());
    if(!destFile.exists()) {
      destFile.createNewFile();
    }
    else {
      return destFile;
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
    return destFile;
  }
}
