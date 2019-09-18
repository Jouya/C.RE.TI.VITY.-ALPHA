import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JPanel;

class MyPanel extends JPanel
{
	  public void DrawRect(Graphics g, int height,int width,int locationx, int locationy)
	  {
	    g.setColor(Color.YELLOW);
	    g.fillRect(height, width, locationx, locationy);
	    
	  }
	  
	  public void DrawOval(Graphics g, int height,int width,int locationx, int locationy, Color color)
	  {
		  g.setColor(color);
		  g.fillOval(height, width, locationx, locationy);
	  }
	  
	  public void SadFace(Graphics g) 
	    { 
	  
	        // Oval for face outline 
		    g.setColor(Color.YELLOW);
	        g.fillOval(110, 70, 250, 250); 
	  
	        // Ovals for eyes 
	        // with black color filled 
	        g.setColor(Color.BLACK); 
	        g.fillOval(200, 120, 25, 25); 
	        g.fillOval(250, 120, 25, 25); 
	  
	        // Arc for the smile 
	        g.drawArc(190, 250, 100, 120, 45, 100); 
	    } 
	  
	  public void Smiley(Graphics g) 
	    { 
	  
	        // Oval for face outline 
		    g.setColor(Color.YELLOW);
	        g.fillOval(110, 70, 250, 250); 
	  
	        // Ovals for eyes 
	        // with black color filled 
	        g.setColor(Color.BLACK); 
	        g.fillOval(200, 120, 25, 25); 
	        g.fillOval(250, 120, 25, 25); 
	  
	        // Arc for the smile 
	        g.drawArc(130, 150, 200, 120, 220, 100); 
	    } 
	}
