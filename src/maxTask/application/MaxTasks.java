package maxTask.application;
	
import java.time.LocalDate;
import java.time.LocalTime;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


/**
 * @author Maxim Torubarov 
 * Main class for runnig the application  
 */
public class MaxTasks extends Application {
	
	
	//variables over the whole application for task view
	
	//date we are currently looking at
	public static int currentDate = 0;
	//task that is currently selected
	public static int currentSelected = 0;
	//timer
	public static LocalTime startTime;
	public static int timerNumMinutes;
	
	
	
	public static Stage primaryStage ; 
		
	@Override
	public void start(Stage primaryStage) {
		//make current date today's current date to start off
		int dayOfYear = (LocalDate.now()).getDayOfYear();
		System.out.println("We are setting the current date: "+dayOfYear);
		MaxTasks.currentDate=dayOfYear;
		
		//Do everything else...
	    MaxTasks.primaryStage = primaryStage;
		try{
			Pane root = (Pane) FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Every day Timed Tasks");
			primaryStage.setResizable(false);
			primaryStage.show();	
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args String
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
}





