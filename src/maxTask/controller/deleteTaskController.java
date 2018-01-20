package maxTask.controller;

import java.io.IOException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import maxTask.model.TaskView;
import maxTask.util.ManageFrontend;

public class deleteTaskController {
	@FXML Button okButton;
	@FXML Button cancelButton;
	@FXML RadioButton ThisRadioButton;
	@FXML RadioButton ThisAndFollowingRadioButton;
	@FXML RadioButton AllRadioButton;
	
	
	TaskView taskView;
	
	
	
	/**
	 * Initializes the front end UI. This method is automatically called
	 * after the fxml file has been loaded.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@FXML private void initialize() throws ClassNotFoundException, IOException{
		taskView = TaskView.FetchTasks(taskView);
		System.out.println("We selected current date: "+MainPageController.currentDate);

		System.out.println("We selected current selected: "+MainPageController.currentSelected);
	}
	
	
	//TODO: remember that the preferred time has to be an integer
	
	//This method will handle the creating, renaming, opening and deleting albums features 
	public void handle(ActionEvent e) throws Exception{
		Button b = (Button) e.getSource() ; //The button press which led to the calling of this method 
		//exit: just serialize and nothing else. (basically save button)
		if(b == cancelButton){
			//just switch back to main page
			//Task.SerializeTask(tasks);
			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Added a Task", p);
		}else if(b==okButton){
			taskView.deleteTaskFromAll(MainPageController.currentSelected);
			TaskView.SerializeTask(taskView);
			
			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Added a Task", p);
		}
	}
}
