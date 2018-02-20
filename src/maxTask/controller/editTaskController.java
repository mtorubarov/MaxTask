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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import maxTask.application.MaxTasks;
import maxTask.model.Task;
import maxTask.model.TaskView;
import maxTask.model.TaskViewDate;
import maxTask.model.TaskViewTask;
import maxTask.util.ManageFrontend;


//TODO: other days doesnt yet work
public class editTaskController {
	//to know what we are looking at:
	@FXML Label currentDateLabel;
	
	
	@FXML Button thisTaskButton;
	@FXML Button thisAndFollowingTasksButton;
	@FXML Button allTasksButton;
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
	
     
     String oldName;
     int oldPreferredTime;
     boolean oldMondayCheck;
     boolean oldTuesdayCheck;
     boolean oldWednesdayCheck;
     boolean oldThursdayCheck;
     boolean oldFridayCheck;
     boolean oldSaturdayCheck;
     boolean oldSundayCheck;
     int oldRepeatDay;
     int oldRepeatWeek;
     int oldRepeatMonth;
     
     int[] oldOtherDays;	//TODO: finish other days
     int oldDayEnds;
     
     
     TaskViewTask task;
     int currentDate;
     
     //for date picker
     public boolean buttonClicked = false;
     public boolean hiddenCalendar = true;
     public List<LocalDate> otherDates = new ArrayList<LocalDate>();
     
     
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
		currentDate=MaxTasks.currentDate;
		
		
		LocalDate todayDate = LocalDate.now();
		LocalDate newCurrentDate=LocalDate.ofYearDay(todayDate.getYear(), MaxTasks.currentDate);
		currentDateLabel.setText(""+newCurrentDate);
		
		
		//first find the currently selected task
		task = taskView.allTasks.get(MaxTasks.currentSelected);
		//TaskViewDate currentDateTasks = taskView.allDates[MaxTasks.currentDate];
		Task currentDateTask = taskView.getTaskFromDate(MaxTasks.currentDate,MaxTasks.currentSelected);	//could be null if it doesn't exist
		
		//we first set all the guys to their proper values
		oldName=task.name;
		nameTextField.setText(task.name);
		//timeTextField.setText();
		
		//FIGURE OUT DAY OF WEEKS
		int dayOfWeeks=task.dayOfWeeks;
		System.out.println("EDITING: dayOfWeeks="+dayOfWeeks);
		if(dayOfWeeks!=0){
			if(dayOfWeeks/64==1){
				mondayCheck.setSelected(true);
				oldMondayCheck=true;
				dayOfWeeks=dayOfWeeks-64;
			}
			if(dayOfWeeks/32==1){
				tuesdayCheck.setSelected(true);
				oldTuesdayCheck=true;
				dayOfWeeks=dayOfWeeks-32;
			}
			if(dayOfWeeks/16==1){
				wednesdayCheck.setSelected(true);
				oldWednesdayCheck=true;
				dayOfWeeks=dayOfWeeks-16;
			}
			if(dayOfWeeks/8==1){
				thursdayCheck.setSelected(true);
				oldThursdayCheck=true;
				dayOfWeeks=dayOfWeeks-8;
			}
			if(dayOfWeeks/4==1){
				fridayCheck.setSelected(true);
				oldFridayCheck=true;
				dayOfWeeks=dayOfWeeks-4;
			}
			if(dayOfWeeks/2==1){
				saturdayCheck.setSelected(true);
				oldSaturdayCheck=true;
				dayOfWeeks=dayOfWeeks-2;
			}
			if(dayOfWeeks/1==1){
				sundayCheck.setSelected(true);
				oldSundayCheck=true;
			}
		}
		
		//TODO: set otherDaysDatePicker
		
		dayEndsDatePicker.setValue(LocalDate.ofYearDay(LocalDate.now().getYear(), task.dayEnds));
		oldDayEnds=task.dayEnds;
		
		
		repeatDayTextField.setText(""+task.numberDaysRepeats);
		oldRepeatDay=task.numberDaysRepeats;
		repeatWeekTextField.setText(""+task.numberWeeksRepeats);
		oldRepeatWeek=task.numberWeeksRepeats;
		repeatMonthTextField.setText(""+task.numberMonthsRepeats);
		oldRepeatMonth = task.numberMonthsRepeats;
		
		//display always the current
		timeTextField.setText(""+currentDateTask.getPreferredTime());
		oldPreferredTime=currentDateTask.getPreferredTime();
		
		
		
		//TODO: can edit just this one, all or all following including this one
		
		
		
		
		
		
		//preferred time may be different between different tasks.. so if change that, change either for all, for one, etc.
		//for everything else change for everything else
		//make sure that the spent time matches preferred time
		
		//Some other problems to think about:
			//we don't want to erase the old task times...
			//so all the nonzero times, should stay the same
		
	}
	
	//NOTE: all these methods also appear in addtaskcontroller
	
	String getName(){
		return nameTextField.getText();
	}
	
	int getPreferredTime(){
		int preferredTime=0;
		if(!timeTextField.getText().equals("")){
			preferredTime =Integer.parseInt(timeTextField.getText());		//Assume that integer
		}
		return preferredTime;
	}
	
	int getDaysOfWeek(){
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
		return dayOfWeeks;
	}
	
	int getNumberDaysRepeats(){
		int numberDaysRepeats = 0;
		if(!repeatDayTextField.getText().equals("")){
			numberDaysRepeats=Integer.parseInt(repeatDayTextField.getText());
		}
		return numberDaysRepeats;
	}
	
	int getNumberWeeksRepeats(){
		int numberWeeksRepeats = 0;
		if(!repeatWeekTextField.getText().equals("")){
			numberWeeksRepeats=Integer.parseInt(repeatWeekTextField.getText());
		}
		return numberWeeksRepeats;
	}
	
	int getNumberMonthsRepeats(){
		int numberMonthsRepeats = 0;
		if(!repeatMonthTextField.getText().equals("")){
			numberMonthsRepeats=Integer.parseInt(repeatMonthTextField.getText());
		}
		return numberMonthsRepeats;
	}
	
	
	int[] getOtherDays(){
		if(otherDates == null){
			return null;
		}
		int[] otherDays= new int[otherDates.size()];
		int index = 0;
		for(LocalDate d: otherDates){
			if(otherDays==null){
				System.out.println("otherDays is null!!");
			}
			if(d!=null){
				System.out.println("d");
				otherDays[index] = d.getDayOfYear();
				index++;
			}
		}
		return otherDays;
	}
	
	int getDayEnds(){
		LocalDate dayEndsLocalDate=dayEndsDatePicker.getValue();
		int dayEnds;
		if(dayEndsLocalDate!=null){
			dayEnds=dayEndsLocalDate.getDayOfYear();
		}else{
			dayEnds=365;
		}
		return dayEnds;
	}
	
	//TODO: date picker
	
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
		//edit all tasks
		else if(b==allTasksButton||b==thisTaskButton||b==thisAndFollowingTasksButton){
			buttonClicked=true;				//hide the other days date picker to not produce any errors
			otherDaysDatePicker.hide();
			
			String name = getName();
			int preferredTime = getPreferredTime();
			int firstDay = MaxTasks.currentDate;
			int dayOfWeeks = getDaysOfWeek();
			int numberDaysRepeats = getNumberDaysRepeats();
			int numberWeeksRepeats = getNumberWeeksRepeats();
			int numberMonthsRepeats = getNumberMonthsRepeats();
			int[] otherDays = getOtherDays();
			int dayEnds = getDayEnds();
			
			int id = task.id;
			
			if(b==allTasksButton){
				taskView.editTaskFromAll(task, name, preferredTime, id, dayOfWeeks, numberDaysRepeats, numberWeeksRepeats, numberMonthsRepeats, otherDays, dayEnds);
			}else if(b==thisTaskButton){
				taskView.editTaskFromThis(task, firstDay, name, preferredTime, id, dayOfWeeks, numberDaysRepeats, numberWeeksRepeats, numberMonthsRepeats, otherDays, dayEnds);
			}else if(b==thisAndFollowingTasksButton){
				taskView.editTaskFromFollowing(task, firstDay, name, preferredTime, id, dayOfWeeks, numberDaysRepeats, numberWeeksRepeats, numberMonthsRepeats, otherDays, dayEnds);
			}
				
			
			
			/*
			//NAME
			String name = nameTextField.getText();
			if(name!=oldName){
				//change the name for the taskViewTask and then for all the Task objects in each taskViewDate
				task.name = name;
			}
			
			
			//PREFERRED TIME
			if(!timeTextField.getText().equals("")){
				int preferredTime =Integer.parseInt(timeTextField.getText());
				if(preferredTime!=oldPreferredTime){
					//could put the following in method
					
					int[] dates = task.dates;
					for (int i =0; i< dates.length;i++){
						if(dates[i]==1){
							System.out.println("date: "+i+"| id: "+task.id);
							Task currentTask = taskView.getTaskFromDate(i, task.id);
							if(currentTask!=null){
								currentTask.setPreferredTime(preferredTime);
								if(currentTask.getSpentTime()>currentTask.getPreferredTime()){
									currentTask.setSpentTime(currentTask.getPreferredTime());
								}
							}	
						}
						
					}
				}*/
			//}
			
			//if new day of week checked, we must go through the text's days.. if they don't match up, delete.. if not there, make new one.....
			
			
			
			
			
			//
			
			/*
			//public void addTask(String name, int preferredTime, int firstDay, int dayOfWeeks, int numberDaysRepeats, int numberWeeksRepeats,  int numberMonthsRepeats, int otherDays, int dayEnds){
			//String name = nameTextField.getText();
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
			*/
			TaskView.SerializeTask(taskView);

			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Edited a Task", p);
		}
		
		
		
	}
}
