package application;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


public  class FaceDetection {
	static String path = "";
	static int detectFaces() {
	    System.out.println("\nRunning DetectFaceDemo");

	    // Create a face detector from the cascade file in the resources
	    // directory.
	
	    CascadeClassifier faceDetector = new CascadeClassifier("C:\\Users\\Administrator\\eclipse-workspace\\facedetect\\bin\\lbpcascade_frontalface.xml");
	    Mat image = Highgui.imread(path);

	    // Detect faces in the image.
	    // MatOfRect is a special container class for Rect.
	    MatOfRect faceDetections = new MatOfRect();
	    faceDetector.detectMultiScale(image, faceDetections);
	    
	    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

	    // Draw a bounding box around each face.
	    for (Rect rect : faceDetections.toArray()) {
	        Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
	    }

	    // Save the visualized detection.
	    String filename = "faceDetection.png";
	    System.out.println(String.format("Writing %s", filename));
	    Highgui.imwrite(filename, image);
	    return faceDetections.toArray().length;
	  }
}
