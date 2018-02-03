package maxTask.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import maxTask.application.MaxTasks;
import maxTask.model.TaskView;
import maxTask.util.ManageFrontend;

public class deleteTaskController {
	@FXML Button cancelButton;
	@FXML Button ThisButton;
	@FXML Button ThisAndFollowingButton;
	@FXML Button AllButton;
	
	TaskView taskView;
	
	
	/**
	 * Initializes the front end UI. This method is automatically called
	 * after the fxml file has been loaded.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@FXML private void initialize() throws ClassNotFoundException, IOException{
		taskView = TaskView.FetchTasks(taskView);
	}
	
	//This method will handle all the buttons
	public void handle(ActionEvent e) throws Exception{
		Button b = (Button) e.getSource() ; //The button press which led to the calling of this method 
		if(b == cancelButton){
			//just switch back to main page
			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Canceled Deleting Task", p);
		}else if(b==AllButton){
			taskView.deleteTaskFromAll(MaxTasks.currentSelected);
			TaskView.SerializeTask(taskView);
			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Deleted a Task", p);
		}else if(b==ThisAndFollowingButton){
			taskView.deleteTaskFromFollowing(MaxTasks.currentSelected, MaxTasks.currentDate);
			TaskView.SerializeTask(taskView);
			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Deleted a Task", p);
		}else if(b==ThisButton){
			taskView.deleteTaskFromThis(MaxTasks.currentSelected, MaxTasks.currentDate);
			TaskView.SerializeTask(taskView);
			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Deleted a Task", p);
		}
	}
}