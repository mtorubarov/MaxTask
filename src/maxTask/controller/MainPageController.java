package maxTask.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Timer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import maxTask.model.Task;
import maxTask.model.TaskView;
import maxTask.util.ManageFrontend;

//TODO: update the lists after we switch dates (also make sure that add task for the current day)

/**
 * @author Maxim Torubarov 
 */
public class MainPageController {
	//task view
	@FXML ListView<GridPane> taskListView;
	
	//date control
	@FXML Label CurrentDateLabel;
	@FXML Button goBackDateButton;
	@FXML Button goForwardDateButton;
	
	//reset
	@FXML Button resetButton;
	
	//change tasks
	@FXML Button addTaskButton;
	@FXML Button deleteTaskButton;
	@FXML Button editTaskButton;
	
	//time control
	@FXML Button addTimeButton;
	@FXML Button removeTimeButton;
	@FXML TextField timeTextField;
	
	//timer
	@FXML Button startTimerButton;
	@FXML Button endTimerButton;
	@FXML Label startTimeLabel;
	@FXML Label totalTimerLabel;
	@FXML Button addTimerButton;
	int timerNumMinutes;
	
	//actually save button
	@FXML Button exitButton;
	
	public static TaskView taskView;
	
    ObservableList<GridPane> tasks_observable = FXCollections.observableArrayList();
    
    
    //figure out the formula of how much accomplished
    @FXML Label totalPreferredTimeLabel;
    @FXML Label totalSpentTimeLabel;
	@FXML GridPane timeGraphicPane;
    
	//button that switches  the view to new window, after we serialize this one, to window that allows us to have checklist of things

	@FXML Button switchViews;
	
	//date we are currently looking at
	//used for adding tasks controller and edit task controllers
	static int currentDate = 0;
	static int currentSelected = 0;
	
	/**
	 * Initializes the front end UI. This method is automatically called
	 * after the fxml file has been loaded.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@FXML private void initialize() throws ClassNotFoundException, IOException{
		//set the current date to today's date
		int[] todayDate = TaskView.getTodayDate();
		int dayOfYear = TaskView.getDateNumber(todayDate[0], todayDate[1], todayDate[2]);
		currentDate=dayOfYear;
		CurrentDateLabel.setText(todayDate[1] + "/"+ todayDate[2]+"/" + todayDate[0]+"("+dayOfYear+")");
		
		taskView = TaskView.FetchTasks(taskView);
		//taskView= new TaskView();
		
		//set the tasks for today's date
		int[] todayDay=TaskView.getTodayDate();
		//tasks=taskView.getTasks(TaskView.getDateNumber(todayDay[0],todayDay[1],todayDay[2]));
		
		taskListView.setItems(tasks_observable);
		updateList();
		
		/*
		tasks=Task.FetchTasks(tasks);
		taskListView.setItems(tasks_observable);
		//for each of the Photo instances of current album, make its corresponding observable list
		updateList();
		*/
	}
	
	private GridPane getPaneForTask(Task t){
		//add to photo_grids
		GridPane pane=new GridPane();
		pane.setHgap(10);
		pane.setPadding(new Insets(10,10,10,10));
		Label name = new Label(t.getName());
		Label prefferedTime = new Label(""+t.getPreferredTime());
		Label spentTime = new Label(""+t.getSpentTime());
		pane.add(name, 0, 0);
		pane.add(prefferedTime, 2, 0);
		pane.add(spentTime, 1, 0);
		
		//make bar
		Rectangle outer = new Rectangle(100,10);
		Rectangle inner;
		if(t.getPreferredTime()==0){
			inner = new Rectangle(0,10);
		}else{
			inner = new Rectangle(((double)t.getSpentTime()/(double)t.getPreferredTime())*100,10);
		}
		inner.setFill(Color.RED);
		
		pane.add(outer, 3, 0);
		pane.add(inner, 3, 0);

		
        return pane;
	}
		
	//This method will handle the creating, renaming, opening and deleting albums features 
	public void handle(ActionEvent e) throws Exception{
		Button b = (Button) e.getSource() ; //The button press which led to the calling of this method 
		if(b== goBackDateButton){
			setCurrentDateBack();
		}else if(b==goForwardDateButton){
			setCurrentDateForward();
		}
		//exit: just sserialize and nothing else. (basically save button)
		else if(b == exitButton){
			TaskView.SerializeTask(taskView);
		}
		//add task
		else if(b==addTaskButton){
			TaskView.SerializeTask(taskView);
			switchToAddTask(e);
		}
		//reset tasks
		else if(b==resetButton){
			resetTimes();
		}
		//delete selected task
		else if(b==deleteTaskButton){
			TaskView.SerializeTask(taskView);
			deleteTask(e);
		}
		//edit selected task
		else if(b==editTaskButton){
			TaskView.SerializeTask(taskView);
			//editTask(); used to be retime and renameTsk
		}
		//add time
		else if(b==addTimeButton){
			addTime(false);
		}
		//remove time
		else if(b==removeTimeButton){
			removeTime();
		}
		//start timer
		else if(b==startTimerButton){
			startTimer();
		}
		//end timer
		else if(b==endTimerButton){
			endTimer();
		}
		//switch view to the checklist
		else if(b==switchViews){
			//TODO:switchToChecklist(e);
		}else if(b==addTimerButton){
			addTime(true);
		}
		
	}
	
	//helper methods
	
	//dates helper mehtods:
	public void updateDate(){
		int[] todayDate = TaskView.getTodayDate();
		//figure out the month, year, etc of this currentDate
		LocalDate newCurrentDate=LocalDate.ofYearDay(todayDate[2], currentDate);
		CurrentDateLabel.setText(newCurrentDate.getMonth().getValue() + "/"+ newCurrentDate.getDayOfMonth()+"/" + newCurrentDate.getYear()+"("+currentDate+")");
		//set the tasks for today's date
		
		//tasks=taskView.getTasks(currentDate);
		
		
		taskListView.setItems(tasks_observable);
		updateList();
	}
	
	public void setCurrentDateBack(){
		//TODO: make separate method to display
		currentDate--;
		if(currentDate<0){
			currentDate=0;
		}
		updateDate();
	}
	
	
	
	public void setCurrentDateForward(){
		currentDate++;
		//TODO: handle leap years here
		if(currentDate>366){
			currentDate=0;
		}
		updateDate();
	}
	
	
	
	
	
	
	public void printTasks(){
		System.out.println("TASKS---------------");
		int i =0;
		List<Task> tasks = taskView.getTasks(currentDate);
		for(Task t: tasks){
			System.out.println(i+". name: "+t.getName()+"| preferred time: "+t.getPreferredTime()+"| spent time: "+t.getSpentTime());
			i++;
		}
	}
	
	public void updateList(){
		List<Task> tasks = taskView.getTasks(currentDate);
		if(tasks==null){
			return;
		}
		
		int totalPreferredTime =0;
		int totalSpentTime=0;
		
		tasks_observable.clear();
		for(Task t: tasks){
			GridPane pane = getPaneForTask(t);
			tasks_observable.add(pane);
			totalPreferredTime+=t.getPreferredTime();
			totalSpentTime+=t.getSpentTime();
		}
		taskListView.refresh();
		
		//update total
		totalPreferredTimeLabel.setText(totalPreferredTime+"");
		totalSpentTimeLabel.setText(totalSpentTime+"");
		
		
		//delete old stuff
		//System.out.println("the height is: "+timeGraphicPane.getChildren().size());
		if (timeGraphicPane.getChildren().size()>2){
			timeGraphicPane.getChildren().remove(2);
			timeGraphicPane.getChildren().remove(1);
			timeGraphicPane.getChildren().remove(0);
		}
		
		//make bar
		Rectangle outer = new Rectangle(400,10);
		Rectangle inner;
		if(totalPreferredTime==0){
			inner = new Rectangle(0,10);
		}else{
			inner = new Rectangle(((double)totalSpentTime/(double)totalPreferredTime)*400,10);
		}
		inner.setFill(Color.RED);
		
		timeGraphicPane.add(outer, 0, 0);
		timeGraphicPane.add(inner, 0, 0);
		
		
		//add a goal?
		
		
		
		Label goalLabel = new Label(""+ (int)(((double)totalSpentTime/(double)totalPreferredTime)*100) + "%");
		goalLabel.setPrefWidth(10000);
		timeGraphicPane.add(goalLabel, 0, 1);
		
	}
	
	
	
	//button methods
	public void resetTimes(){
		List<Task> tasks = taskView.getTasks(currentDate);
		for(Task t: tasks){
			t.setSpentTime(0);
		}
		updateList();
		printTasks();
	}

	public void deleteTask(ActionEvent e) throws Exception{
		System.out.println("we are deleting task");
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		//taskView.printAllTasks();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			List<Task> tasks = taskView.getTasks(currentDate);
			temp=tasks.get(index);
			currentSelected=temp.getID();
			switchToDeleteTask(e);
			//tasks.remove(index);
		}
		//updateList();
		//printTasks();
		
	}

	/*
	public void renameTask(){
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			temp=tasks.get(index);
			String name=renameTaskTextField.getText();
			temp.setName(name);
		}
		updateList();
		printTasks();
	}

	public void retimeTask(){
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			temp=tasks.get(index);
			String timeString=retimeTaskTextField.getText();
			int timeInt = Integer.parseInt(timeString);
			temp.setPreferredTime(timeInt);
		}
		updateList();
		printTasks();
	}*/
	
	public void addTime(boolean fromTimer){
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			List<Task> tasks = taskView.getTasks(currentDate);
			temp=tasks.get(index);
			int timeInt=0;
			if(fromTimer){
				timeInt=timerNumMinutes;
			}else{
				String timeString=timeTextField.getText();
				if(!timeString.equals("")){
					timeInt = Integer.parseInt(timeString);
				}
			}
			temp.addSpentTime(timeInt);
		}
		updateList();
		printTasks();
	}
	
	public void removeTime(){
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			List<Task> tasks = taskView.getTasks(currentDate);
			temp=tasks.get(index);
			String timeString=timeTextField.getText();
			if(!timeString.equals("")){
				int timeInt = Integer.parseInt(timeString);
				temp.subtractSpentTime(timeInt);
			}
		}
		updateList();
		printTasks();
	}
	/*
	public void addTask(){
		String name=addTaskTextField.getText();
		Task temp=new Task(name);
		tasks.add(temp);
		updateList();
		printTasks();
	}*/
	
	
	//TIMER
	LocalTime startTime;
	
	public void startTimer(){
		startTime=LocalTime.now();
		System.out.println("start time: "+startTime.toString());
		startTimeLabel.setText(startTime.toString());
		totalTimerLabel.setText("");
		timerNumMinutes=0;
	}
	
	public void endTimer(){
		if(startTime!=null){
			LocalTime endTime=LocalTime.now();
			System.out.println("end time: "+endTime.toString());
			long numMinutes =startTime.until(endTime, ChronoUnit.MINUTES);
			System.out.println("minutes: " + numMinutes);
			totalTimerLabel.setText(""+numMinutes+" minutes");
			startTime=null;
			startTimeLabel.setText("");
			timerNumMinutes= (int)numMinutes;
		}
	}

	
	//switch to checklist view
	public void switchToChecklist(ActionEvent e) throws IOException{
		System.out.println("We pressed button to switch");
		Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
		ManageFrontend.DisplayScreen("ChecklistPage.fxml", e , "Checklist Page", p);
	}
	
	//switch to addtask view to add tasks
	public void switchToAddTask(ActionEvent e) throws IOException{
		System.out.println("We pressed button to add task");
		Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/AddTask.fxml"));
		ManageFrontend.DisplayScreen("AddTask.fxml", e , "Adding a Task", p);
	}
	
	//switch to deletetask view to delete tasks
	public void switchToDeleteTask(ActionEvent e) throws IOException{
		System.out.println("We pressed button to delete task");
		Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/DeleteSelectedTask.fxml"));
		ManageFrontend.DisplayScreen("DeleteSelectedTask.fxml", e , "Deleting a Task", p);
	}
}
