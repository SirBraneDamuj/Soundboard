package player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Soundboard {

	private File soundDirectory;
	private Shell shell;
	private ArrayList<Sound> sounds;
	
	class MP3Filter implements FilenameFilter
	{
		public boolean accept(File dir, String name)
		{
			return name.endsWith(".mp3");
		}
	}
	
	public Soundboard(Display display)
	{
		this.shell = new Shell(display);
		this.shell.setText("Soundboard");
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		this.shell.setLayout(layout);
		this.soundDirectory = new File("Sounds/");
		String[] files = soundDirectory.list(new MP3Filter());
		if(files == null || files.length <= 0)
		{
			System.out.println("Error: Sounds directory not found.");
			System.exit(0);
		}
		this.sounds = new ArrayList<Sound>();
		for(String name : files)
		{
			try{
				sounds.add(new Sound(soundDirectory.getName() + "/" + name, shell, display));
			} catch (FileNotFoundException e) {System.out.println(name + " was not found.");}
		}
	}
	
	public void open()
	{
		this.shell.pack();
		this.shell.open();
	}
	public boolean isDisposed()
	{
		return this.shell.isDisposed();
	}
	
}
