package application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CameraSnapshot {
	
	public static int count=0;
	public static void takePicture() {
                
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  System.out.println("Timer: "+ timer.toString());
				  takePictureHelper();				  
			  }
			}, 5000,5000);	
		
	}
	public static void takePictureHelper() {
		
		int face;
		Webcam webcam = Webcam.getDefault();

		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}

		webcam.open();
		String fileName = "C://InternshipER//camera//temp//img.png";
		try {
			
			ImageIO.write(webcam.getImage(), "PNG", new File(fileName));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CameraSnapshot.class.getName()).log(Level.SEVERE, null, ex);
                    }
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		face = FaceDetection.detectFaces();
								
		webcam.close();
		count++;
	}
	
	

}
