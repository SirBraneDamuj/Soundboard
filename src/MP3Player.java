import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javazoom.jl.player.Player;

public class MP3Player {

  private Sound sound;
	private InputStream is;
  private Player player;
	
	public MP3Player(Sound sound)
	{
    this.sound = sound;
	}
	
	public void play()
	{
    stop();
    try {
      this.is = new FileInputStream(sound.getFile());
      this.player = new Player(is);
    } catch(Exception e) {e.printStackTrace();}
    PlayerThread pThread = new PlayerThread();
    pThread.start();
	}

  public void stop() {
    if(player != null) {
      player.close();
    }
  }

  public boolean isComplete() {
    return player.isComplete();
  }
	
	class PlayerThread extends Thread 
	{		
		public void run()
		{
			try {
				player.play();
        System.out.println("XYZ");
        player.close();
			}
			catch(Exception e) {e.printStackTrace();}
		}
	}
}
