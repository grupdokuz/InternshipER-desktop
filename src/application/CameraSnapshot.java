package application;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class CameraSnapshot {
	
	
	public static void takePicture() {
		
		
		Webcam webcam = Webcam.getDefault();

		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}

		webcam.open();
		try {
			ImageIO.write(webcam.getImage(), "PNG", new File("C:\\Users\\ugur\\Desktop\\img.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		webcam.close();
	}
	
	

}
