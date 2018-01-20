package maxTask.application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


/**
 * @author Maxim Torubarov 
 * @author Mihir Patel
 * Main class for runnig the application  
 */
public class MaxTasks extends Application {
	
	public static Stage primaryStage ; 
		
	@Override
	public void start(Stage primaryStage) {
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





