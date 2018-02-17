package application;
	
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,500,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		execute();
		launch(args);
	}
	
	//Programýn trace start aldýðý durumda baþlatýlacak olan method
	public static void execute() {
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		//executorService.submit(FaceDetection::detectFaces);
		executorService.submit(ScreenShot::screenShotStart);
		executorService.submit(CameraSnapshot::takePicture);
		executorService.submit(NetworkSniffer::listenNetwork);
		
	}
	
}
