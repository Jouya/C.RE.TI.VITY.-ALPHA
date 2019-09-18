import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Random;
import java.util.concurrent.TimeUnit; 

public class SoundToVisual extends JFrame {
	
	protected boolean running;
    ByteArrayOutputStream out;
    MyPanel draw_field = new MyPanel();
    // state indicators 
    boolean reached_veryloud = false;
    boolean stage1 = false;
    boolean reached_veryloud2 = false;
    boolean goodluck = false;
    boolean badluck = false;
	
	public SoundToVisual()
	{
		
		// creating the UI
		super("C.RE.TI.VITY. ALPHA");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container content = getContentPane();
        Dimension size = new  Dimension(500,500);
        content.setPreferredSize(size);
        
        content.add(draw_field, BorderLayout.CENTER);
        draw_field.setBackground(Color.WHITE);
		
		final JButton capture = new JButton("Record");
		final JButton stop = new JButton("Stop");
		capture.setEnabled(true);
		stop.setEnabled(true);
		
		
		// adding an action to a button
		ActionListener captureListener = 
	            new ActionListener() 
		{
	      public void actionPerformed(ActionEvent e) 
	      {
	          capture.setEnabled(false);
	          stop.setEnabled(true);
	          captureAudio();
	      }
		};
		capture.addActionListener(captureListener);
		content.add(capture, BorderLayout.NORTH);
		
		ActionListener stopListener = 
	            new ActionListener() 
		{
	       public void actionPerformed(ActionEvent e) 
	       {
	            capture.setEnabled(true);
	            stop.setEnabled(false);
	            running = false;
	        }
	    };
	    stop.addActionListener(stopListener);
	    content.add(stop, BorderLayout.SOUTH);
		
	}
	
	// this function captures the audio
	private void captureAudio() {
        try {
          final AudioFormat format = getFormat();
          DataLine.Info info = new DataLine.Info(
            TargetDataLine.class, format);
          final TargetDataLine line = (TargetDataLine)
            AudioSystem.getLine(info);
          line.open(format);
          line.start();
          Runnable runner = new Runnable() {
            int bufferSize = (int)format.getSampleRate() 
              * format.getFrameSize();
            byte buffer[] = new byte[bufferSize];
            
            public void run() {
              out = new ByteArrayOutputStream();
              running = true;
              try {
                while (running) {
                  int count = 
                    line.read(buffer, 0, buffer.length);
                  	Markov(volume(buffer,buffer.length));
                  if (count > 0) {
                    out.write(buffer, 0, count);
                  }
                }
                out.close();
              } catch (IOException e) {
                System.err.println("I/O problems: " + e);
                System.exit(-1);
              } catch (InterruptedException e) {
				e.printStackTrace();
			}
            }
          };
          Thread captureThread = new Thread(runner);
          captureThread.start();
        } catch (LineUnavailableException e) {
          System.err.println("Line unavailable: " + e);
          System.exit(-2);
        }
      }
	
	 // set the format of the audio captured (dependent on the type of microphone)
	 private AudioFormat getFormat() {
	        float sampleRate = 18000;
	        int sampleSizeInBits = 8;
	        int channels = 1;
	        boolean signed = false;
	        boolean bigEndian = true;
	        return new AudioFormat(sampleRate, 
	          sampleSizeInBits, channels, signed, bigEndian);
	      }
	 
	 // calculates the "volume/loudness" of the audio
	 // the more quite = the more negative the value is
	 // more noise = more positive value (less negative)
	 private int volume(byte buffer[], int size)
     {
   	  int sum = 0;
   	  for (int i = 0; i < size; i++)
   	  {
   		  sum += buffer[i];
   	  }
   	  	return sum/size;
     }
	 
	 
	 // The "creative", Markov inspired, function. Depending on how loud the user gets, different things will be displayed!
	 private void Markov(int sum) throws InterruptedException
	 {
		
		 Random rand = new Random();
		 int height = rand.nextInt(200);
		 int width = rand.nextInt(300);
		 int locationx = rand.nextInt(500);
		 int locationy = rand.nextInt(500);
		 int luck = rand.nextInt(2);
		 float r = rand.nextFloat();
		 float g = rand.nextFloat();
		 float b = rand.nextFloat();

		 // will get stuck at these 2 states at the end until the program restarted 
		 if (goodluck)
		 {
			 draw_field.removeAll();
			 draw_field.setBackground(Color.WHITE);
			 draw_field.Smiley(draw_field.getGraphics());
			 JLabel label = new JLabel("GOOD LUCK :) ");
			 label.setFont(new Font("Serif", Font.PLAIN, 30));
			 draw_field.add(label);
			 draw_field.validate(); 
		 }
		 else if (badluck)
		 {
			 draw_field.removeAll();
			 draw_field.setBackground(Color.WHITE);
			 draw_field.SadFace(draw_field.getGraphics());
			 JLabel label = new JLabel("BAD LUCK :(  ");
			 label.setFont(new Font("Serif", Font.PLAIN, 30));
			 draw_field.add(label);
			 draw_field.validate();
		 }
		 // 50/50 that you will get lucky or not 
		 else if (stage1 && sum > -5)
		 {
			 if(luck == 1)
			 {
				 draw_field.removeAll();
				 draw_field.setBackground(Color.WHITE);
				 draw_field.Smiley(draw_field.getGraphics());
				 JLabel label = new JLabel("GOOD LUCK :) ");
				 label.setFont(new Font("Serif", Font.PLAIN, 30));
				 draw_field.add(label);
				 draw_field.validate();
				 goodluck = true;
				 
				 
				 
			 }
			 else
			 {
				 draw_field.removeAll();
				 draw_field.setBackground(Color.WHITE);
				 draw_field.SadFace(draw_field.getGraphics());
				 JLabel label = new JLabel("BAD LUCK :(  ");
				 label.setFont(new Font("Serif", Font.PLAIN, 30));
				 draw_field.add(label);
				 draw_field.validate();
				 badluck = true;
			 }

			 
		 }
		 
		 else if (reached_veryloud && sum < -40)
		 {
			 draw_field.removeAll();
			 draw_field.setBackground(Color.WHITE);
			 JLabel label = new JLabel("Thank you for quieting down.");
			 label.setFont(new Font("Serif", Font.PLAIN, 30));
			 draw_field.add(label);
			 draw_field.validate();
			 draw_field.repaint();
			 TimeUnit.MILLISECONDS.sleep(1500);
			 label.setText("I dare you to get loud one more time!");
			 label.setFont(new Font("Serif", Font.PLAIN, 30));
			 draw_field.add(label);
			 draw_field.validate();
			 draw_field.repaint();
			 TimeUnit.MILLISECONDS.sleep(1500);
			 stage1 = true;
			 reached_veryloud = false;
			 
			 			 
		 }
		 // VERY LOUD
		 else if (sum > -3)
		 {
			 draw_field.removeAll();
			 draw_field.setBackground(Color.WHITE);
			 JLabel label = new JLabel("YOU ARE VERY LOUD");
			 label.setFont(new Font("Serif", Font.PLAIN, 30));
			 draw_field.add(label);
			 draw_field.validate();
			 draw_field.repaint();
			 reached_veryloud = true;
			 
		 }
		 // Relatively loud
		 else if (sum > -20)
		 {
			 {
				 draw_field.removeAll();
			 }
			 Color color = new Color(r,b,g);
			 draw_field.DrawOval(draw_field.getGraphics(), height, width, locationx, locationy, color);
			 //draw_field.DrawRect(draw_field.getGraphics(), height, width, locationx, locationy);
			 draw_field.setBackground(Color.GREEN);
			 
		
		 }
		 // Picking up something
		 else if ( (sum > -40))
		 {
			 draw_field.removeAll();
			 draw_field.setBackground(Color.YELLOW);
		 }
		 // Quiet
		 else
		 {
			 draw_field.removeAll();
			 draw_field.setBackground(Color.RED);
		 }
		 
		 // Stopped 
		 if (!running)
		 {
			 draw_field.removeAll();
			 draw_field.setBackground(Color.WHITE);
		 }
	 }
	
	
	public static void main(String args[]) {
        JFrame frame = new SoundToVisual();
        frame.pack();
        frame.show();
     
        
      
      }
	
}
