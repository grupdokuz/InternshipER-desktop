package application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class CameraSnapshot {
	

	public static void takePicture() {
		

		int face;
		Webcam webcam = Webcam.getDefault();

		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}

		webcam.open();
		face = FaceDetection.detectFaces();

		// if there are more than 1 person, flagged
		
		if(face>1) {
			
			try {
				ImageIO.write(webcam.getImage(), "PNG", new File("img\\flagged\\"+"snapshot-"+LocalDateTime.now()+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else {
			try {
				
				ImageIO.write(webcam.getImage(), "PNG", new File("img\\"+"snapshot-"+LocalDateTime.now()+".png"));
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		
		webcam.close();
	}
	
	

}
