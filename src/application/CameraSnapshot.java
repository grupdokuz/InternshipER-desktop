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
		String fileName = "snapshot-"+LocalDateTime.now()+".png";
		try {
			
			ImageIO.write(webcam.getImage(), "PNG", new File("snapshots\\"+fileName));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		FaceDetection.path = "snapshots\\"+fileName;
		face = FaceDetection.detectFaces();
		
		// if there are more than 1 person, flagged		
		if(face>1) {
				new File("snapshots\\"+fileName).renameTo(new File("camera\\flagged\\"+count+"-scanned-"+fileName));
				//ImageIO.write(webcam.getImage(), "PNG", new File("img\\flagged\\"+count+"-scanned-"+fileName));
		}
		else {
				new File("snapshots\\"+fileName).renameTo(new File("camera\\"+count+"-scanned-"+fileName));
				//ImageIO.write(webcam.getImage(), "PNG", new File("img\\"+count+"-scanned-"+fileName));
		}				
		webcam.close();
		count++;
	}
	
	

}
