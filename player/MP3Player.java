package player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javazoom.jl.player.Player;

public class MP3Player {

	private InputStream is;
	private Player player;
	
	public MP3Player(String fileName) throws FileNotFoundException
	{
		is = new FileInputStream(fileName);
	}
	
	public void play()
	{
		try {
			player = new Player(is);
			PlayerThread pThread = new PlayerThread();
			pThread.start();
		} catch(Exception e) {e.printStackTrace();}
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
