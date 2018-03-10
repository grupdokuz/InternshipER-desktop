package application;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;


public class ScreenShot extends TimerTask {
	int i = 1;

	public void run() {

		try {

			captureScreen();
			i++;
			if (i == 10) {
				cancel();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void screenShotStart(){
		Timer timer = new Timer();
		timer.schedule(new ScreenShot(), 0, 5000);
        
	}

	public void captureScreen() throws Exception {
		try {
			
			Robot robot = new Robot();
			String format = "jpg";
			String file = "C://InternshipER//screenshots//screenshot_"+i + "." + format;

			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
			
			ImageIO.write(screenFullImage, format, new File(file));

			System.out.println("A full screenshot saved!");
		} catch (AWTException | IOException ex) {
			System.err.println(ex);
		}
	}
}
