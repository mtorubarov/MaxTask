package maxTask.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import maxTask.application.MaxTasks;
import maxTask.model.TaskView;
import maxTask.model.TaskViewTask;
import maxTask.util.ManageFrontend;


//TODO: other days doesnt yet work
public class editTaskController {
	@FXML Button addTaskButton;
	@FXML Button cancelButton;
	
	@FXML TextField nameTextField;
	@FXML TextField timeTextField;
	@FXML CheckBox mondayCheck;
	@FXML CheckBox tuesdayCheck;
	@FXML CheckBox wednesdayCheck;
	@FXML CheckBox thursdayCheck;
	@FXML CheckBox fridayCheck;
	@FXML CheckBox saturdayCheck;
	@FXML CheckBox sundayCheck;
	
	@FXML DatePicker otherDaysDatePicker;
	@FXML DatePicker dayEndsDatePicker;
	
	//@FXML DropdownButton repeatTypeDropdown;
	//@FXML MenuItem dayMenuItem;
	//@FXML MenuItem weekMenuItem;
	//@FXML MenuItem monthMenuItem;
	//@FXML DatePicker dayEndsDatePicker;
     @FXML TextField repeatDayTextField;
     @FXML TextField repeatWeekTextField;
     @FXML TextField repeatMonthTextField;
     
     TaskView taskView;
	
	/**
	 * Initializes the front end UI. This method is automatically called
	 * after the fxml file has been loaded.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@FXML private void initialize() throws ClassNotFoundException, IOException{
		taskView = TaskView.FetchTasks(taskView);
		
		System.out.println("We selected current date: "+MaxTasks.currentDate);
		System.out.println("We selected current selected: "+MaxTasks.currentSelected);
		
		//first find the currently selected task
		TaskViewTask task = taskView.allTasks.get(MaxTasks.currentSelected);
		
		//we first set all the guys to their proper values
		nameTextField.setText(task.name);
		//timeTextField.setText();
		
		//FIGURE OUT DAY OF WEEKS
		int dayOfWeeks=task.dayOfWeeks;
		System.out.println("EDITING: dayOfWeeks="+dayOfWeeks);
		if(dayOfWeeks!=0){
			if(dayOfWeeks/64==1){
				mondayCheck.setSelected(true);
				dayOfWeeks=dayOfWeeks-64;
			}
			if(dayOfWeeks/32==1){
				tuesdayCheck.setSelected(true);
				dayOfWeeks=dayOfWeeks-32;
			}
			if(dayOfWeeks/16==1){
				wednesdayCheck.setSelected(true);
				dayOfWeeks=dayOfWeeks-16;
			}
			if(dayOfWeeks/8==1){
				thursdayCheck.setSelected(true);
				dayOfWeeks=dayOfWeeks-8;
			}
			if(dayOfWeeks/4==1){
				fridayCheck.setSelected(true);
				dayOfWeeks=dayOfWeeks-4;
			}
			if(dayOfWeeks/2==1){
				saturdayCheck.setSelected(true);
				dayOfWeeks=dayOfWeeks-2;
			}
			if(dayOfWeeks/1==1){
				sundayCheck.setSelected(true);
			}
		}
		
		//TODO: set otherDaysDatePicker
		
		dayEndsDatePicker.setValue(LocalDate.ofYearDay(LocalDate.now().getYear(), task.dayEnds));
		
		repeatDayTextField.setText(""+task.numberDaysRepeats);
		repeatWeekTextField.setText(""+task.numberWeeksRepeats);
		repeatMonthTextField.setText(""+task.numberMonthsRepeats);
		
		
		//preferred time may be different between different tasks.. so if change that, change either for all, for one, etc.
		//for everything else change for everything else
		//make sure that the spent time matches preferred time
		
		//Some other problems to think about:
			//we don't want to erase the old task times...
			//so all the nonzero times, should stay the same
		
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
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Main Page", p);
		}
		//add task
		else if(b==addTaskButton){
			//public void addTask(String name, int preferredTime, int firstDay, int dayOfWeeks, int numberDaysRepeats, int numberWeeksRepeats,  int numberMonthsRepeats, int otherDays, int dayEnds){
			String name = nameTextField.getText();
			int preferredTime=0;
			if(!timeTextField.getText().equals("")){
				preferredTime =Integer.parseInt(timeTextField.getText());
			}
			
			int firstDay = MaxTasks.currentDate;
			int dayOfWeeks=0;
			if(mondayCheck.isSelected()){
				dayOfWeeks=dayOfWeeks+(int)Math.pow(2, 6);
			}
			if(tuesdayCheck.isSelected()){
				dayOfWeeks=dayOfWeeks+(int)Math.pow(2, 5);
			}
			if(wednesdayCheck.isSelected()){
				dayOfWeeks=dayOfWeeks+(int)Math.pow(2, 4);
			}
			if(thursdayCheck.isSelected()){
				dayOfWeeks=dayOfWeeks+(int)Math.pow(2, 3);
			}
			if(fridayCheck.isSelected()){
				dayOfWeeks=dayOfWeeks+(int)Math.pow(2, 2);
			}
			if(saturdayCheck.isSelected()){
				dayOfWeeks=dayOfWeeks+(int)Math.pow(2, 1);
			}
			if(sundayCheck.isSelected()){
				dayOfWeeks=dayOfWeeks+(int)Math.pow(2, 0);
			}
			int numberDaysRepeats = 0;
			if(!repeatDayTextField.getText().equals("")){
				numberDaysRepeats=Integer.parseInt(repeatDayTextField.getText());
			}
			
			int numberWeeksRepeats = 0;
			if(!repeatWeekTextField.getText().equals("")){
				numberWeeksRepeats=Integer.parseInt(repeatWeekTextField.getText());
			}
			
			int numberMonthsRepeats = 0;
			if(!repeatMonthTextField.getText().equals("")){
				numberMonthsRepeats=Integer.parseInt(repeatMonthTextField.getText());
			}
			
			int[] otherDays={0};
			
			LocalDate dayEndsLocalDate=dayEndsDatePicker.getValue();
			int dayEnds;
			if(dayEndsLocalDate!=null){
				dayEnds=dayEndsLocalDate.getDayOfYear();//TaskView.getDateNumber(dayEndsLocalDate.getYear(), dayEndsLocalDate.getMonthValue(), dayEndsLocalDate.getDayOfMonth());
			}else{
				dayEnds=365;
			}
			taskView.addTask(name, preferredTime,firstDay, dayOfWeeks, numberDaysRepeats, numberWeeksRepeats, numberMonthsRepeats, otherDays, dayEnds);
			TaskView.SerializeTask(taskView);

			System.out.println("We pressed button to add task and will now switch");
			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Added a Task", p);
			//addTask();
		}
		
		
	}
}
