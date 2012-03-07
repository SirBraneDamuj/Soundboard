import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javazoom.jl.player.advanced.*;

public class MP3Player {

  private Sound sound;
  private AdvancedPlayer player;
	
	public MP3Player(Sound sound)
	{
    this.sound = sound;
	}
	
	public void play(final PlayButton p)
	{
    try {
      this.player = new AdvancedPlayer(new FileInputStream(p.getSound().getFile()));
      this.player.setPlayBackListener(new PlaybackListener() {
       public void playbackStarted(PlaybackEvent evt) {
         p.setEnabled(false);
         p.setFinished(false);
       }
       public void playbackFinished(PlaybackEvent evt) {
         p.setEnabled(true);
         p.setFinished(true);
       }
      });
    } catch(Exception e) {e.printStackTrace();}
    PlayerThread pThread = new PlayerThread();
    pThread.start();
	}

  public void stop() {
    if(player != null) {
      player.stop();
    }
  }

	class PlayerThread extends Thread 
	{		
		public void run()
		{
			try {
				player.play();
			}
			catch(Exception e) {e.printStackTrace();}
		}
	}
}
