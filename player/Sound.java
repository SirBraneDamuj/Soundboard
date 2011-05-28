package player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Sound {

	private String soundFileName;
	private String infoFileName;
	private Composite container;
	private Button playButton;
	private Button infoButton;
	private Label nameLabel;
	
	public Sound(String fileName, Shell shell, Display display) throws FileNotFoundException
	{
		this.soundFileName = fileName;
		this.infoFileName = fileName.split("\\.")[0]+".txt";
		container = new Composite(shell, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		this.nameLabel = new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		this.playButton = new Button(container, SWT.PUSH);
		this.infoButton = new Button(container, SWT.PUSH);
		
		configureButtons(display);
		container.pack();
	}
	
	private void configureButtons(final Display display)
	{
		playButton.setText("Play");
		infoButton.setText("Info");
		nameLabel.setText(soundFileName.split("\\.")[0].split("/")[1]);
		playButton.addSelectionListener(new SelectionListener() {

		      public void widgetSelected(SelectionEvent event) {
		    	  try {
			    	  MP3Player player = new MP3Player(soundFileName);
			    	  player.play();
		    	  } catch(FileNotFoundException e) {
		    		  System.out.println(soundFileName + " was not found.");
		    	  }
		      }

		      public void widgetDefaultSelected(SelectionEvent event) {
		    	  widgetSelected(event);
		      }
		});
		infoButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				String text = "";
		    	  try {
			    	  BufferedReader in = new BufferedReader(new FileReader(infoFileName));
			    	  String buf = in.readLine();
			    	  while(buf != null)
			    	  {
			    		  text += buf;
			    		  buf = in.readLine();
			    	  }		    	  
		    	  } catch(FileNotFoundException e) {
		    		  text = "No info file found for this sound.";
		    	  }
		    	  catch (IOException e) {
		    		  System.out.println("An error occurred while reading " + infoFileName);
		    	  }
		    	  Shell shell = new Shell(display);
		    	  (new Label(shell, SWT.NONE)).setText(text);
		    	  shell.setText(soundFileName);
		    	  shell.setLayout(new GridLayout());
		    	  shell.pack();
		    	  shell.open();
		    	  while (!shell.isDisposed ()) {
		  			if (!display.readAndDispatch ()) display.sleep ();
		    	  }
		      }

		      public void widgetDefaultSelected(SelectionEvent event) {
		    	  widgetSelected(event);
		      }
		});
		GridData data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		playButton.setLayoutData(data);
	}
	
}
