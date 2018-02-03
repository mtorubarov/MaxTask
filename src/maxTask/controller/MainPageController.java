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
import maxTask.application.MaxTasks;
import maxTask.model.Task;
import maxTask.model.TaskView;
import maxTask.util.ManageFrontend;

/**
 * @author Maxim Torubarov 
 */
public class MainPageController {
	
	//------------------------------FXML COMPONENTS----------------------------------------------------------
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
	
	//actually save button
	@FXML Button exitButton;
	
	//shows how much accomplished
    @FXML Label totalPreferredTimeLabel;
    @FXML Label totalSpentTimeLabel;
	@FXML GridPane timeGraphicPane;
    
	//button that switches  the view to new window, after we serialize this one, to window that allows us to have checklist of things
	@FXML Button switchViews;
	
	//------------------------------OTHER FIELDS----------------------------------------------------------
	
	//task view that displays all the tasks
	public static TaskView taskView;
	
	//list of tasks for taskView
    ObservableList<GridPane> tasks_observable = FXCollections.observableArrayList();
    
	
	
	//------------------------------MAIN METHODS----------------------------------------------------------
	/**
	 * Initializes the front end UI. This method is automatically called
	 * after the fxml file has been loaded.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@FXML private void initialize() throws ClassNotFoundException, IOException{
		//deserialize task view
		taskView = TaskView.FetchTasks(taskView);
		
		//set the task list view object to the tasks
		taskListView.setItems(tasks_observable);
		
		//update the date and timer to current settings 
		updateDate();
		updateTimer();
	}
	
	//method that sets the observable list of tasks
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
		if(b== goBackDateButton){				//changes the date backwards
			setCurrentDateBack();
		}else if(b==goForwardDateButton){		//changes the date forwards
			setCurrentDateForward();
		}
		else if(b == exitButton){				//just serializes. basically save button
			TaskView.SerializeTask(taskView);	
		}
		else if(b==addTaskButton){				//serializes, switches to addTask menu
			TaskView.SerializeTask(taskView);
			switchToAddTask(e);
		}
		else if(b==resetButton){				//resets all the times for that day
			resetTimes();
		}
		else if(b==deleteTaskButton){			//serializes, switches to deleteTask menu
			TaskView.SerializeTask(taskView);
			deleteTask(e);
		}
		else if(b==editTaskButton){				//serializes, switches to TODO: editTask menu 
			TaskView.SerializeTask(taskView);
			//printTasks();
			taskView.printAllTasks();
			editTask(e); //used to be retime and renameTsk
		}
		else if(b==addTimeButton){				//adds time to currently selected task
			addTime(false);
		}
		else if(b==removeTimeButton){			//removes time from currently selected task
			removeTime();
		}
		else if(b==startTimerButton){			//starts timer
			startTimer();
		}
		else if(b==endTimerButton){				//ends timer
			endTimer();
		}
		else if(b==switchViews){				//TODO:switch views to the checklist
			//TODO:switchToChecklist(e);
		}else if(b==addTimerButton){			//add timer time to currently selected task
			addTime(true);
		}
		
	}
	
	
	////------------------------------DATES HELPER METHODS----------------------------------------------------------
	//update date to be the currentDate stored in MaxTasks
	public void updateDate(){		
		//display the current date
		LocalDate todayDate = LocalDate.now();
		LocalDate newCurrentDate=LocalDate.ofYearDay(todayDate.getYear(), MaxTasks.currentDate);
		displayDate(newCurrentDate);
	
		//update the list of tasks for the new date
		updateList();
	}
	
	//display the date in the parameters
	public void displayDate(LocalDate date){
		//CurrentDateLabel.setText(todayDate[1] + "/"+ todayDate[2]+"/" + todayDate[0]+"("+dayOfYear+")");
		CurrentDateLabel.setText(date.getDayOfWeek()+"|"+date.getMonth() + " "+ date.getDayOfMonth()+"/" + date.getYear()+"("+MaxTasks.currentDate+")");
	}
	
	public void setCurrentDateBack(){
		MaxTasks.currentDate--;
		if(MaxTasks.currentDate<0){
			MaxTasks.currentDate=0;
		}
		updateDate();
	}
	
	public void setCurrentDateForward(){
		MaxTasks.currentDate++;
		//TODO: handle leap years here
		if(MaxTasks.currentDate>366){
			MaxTasks.currentDate=0;
		}
		updateDate();
	}

	
	
	//------------------------------OTHER METHODS----------------------------------------------------------
	public void printTasks(){
		System.out.println("TASKS---------------");
		int i =0;
		List<Task> tasks = taskView.getTasks(MaxTasks.currentDate);
		for(Task t: tasks){
			System.out.println(i+". name: "+t.getName()+"| preferred time: "+t.getPreferredTime()+"| spent time: "+t.getSpentTime());
			i++;
		}
	}
	
	public void updateList(){
		if(taskView==null){
			System.out.println("taskView is null");
		}
		List<Task> tasks = taskView.getTasks(MaxTasks.currentDate);
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
	
	
	//------------------------------BUTTON METHODS----------------------------------------------------------
	public void resetTimes(){
		List<Task> tasks = taskView.getTasks(MaxTasks.currentDate);
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
			List<Task> tasks = taskView.getTasks(MaxTasks.currentDate);
			temp=tasks.get(index);
			MaxTasks.currentSelected=temp.getID();
			switchToDeleteTask(e);
			//tasks.remove(index);
		}
		//updateList();
		//printTasks();
	}

	public void editTask(ActionEvent e) throws Exception{
		System.out.println("we are editting task");
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			List<Task> tasks = taskView.getTasks(MaxTasks.currentDate);
			temp=tasks.get(index);
			MaxTasks.currentSelected=temp.getID();
			switchToEditTask(e);
		}
	}

	public void addTime(boolean fromTimer){
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			List<Task> tasks = taskView.getTasks(MaxTasks.currentDate);
			temp=tasks.get(index);
			int timeInt=0;
			if(fromTimer){
				timeInt=MaxTasks.timerNumMinutes;
			}else{
				String timeString=timeTextField.getText();
				if(!timeString.equals("")){
					timeInt = Integer.parseInt(timeString);
				}
			}
			temp.addSpentTime(timeInt);
		}
		updateList();
	}
	
	public void removeTime(){
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			List<Task> tasks = taskView.getTasks(MaxTasks.currentDate);
			temp=tasks.get(index);
			String timeString=timeTextField.getText();
			if(!timeString.equals("")){
				int timeInt = Integer.parseInt(timeString);
				temp.subtractSpentTime(timeInt);
			}
		}
		updateList();
	}
	
	
	
	//------------------------------TIMER----------------------------------------------------------
	public void updateTimer(){
		if(MaxTasks.startTime!=null){
			startTimeLabel.setText(MaxTasks.startTime.toString());
		}
		if(MaxTasks.timerNumMinutes!=0){
			totalTimerLabel.setText(""+MaxTasks.timerNumMinutes+" minutes");
		}
	}
	
	public void startTimer(){
		
		MaxTasks.startTime=LocalTime.now();
		System.out.println("start time: "+MaxTasks.startTime.toString());
		startTimeLabel.setText(MaxTasks.startTime.toString());
		totalTimerLabel.setText("");
		MaxTasks.timerNumMinutes=0;
	}
	
	public void endTimer(){
		if(MaxTasks.startTime!=null){
			LocalTime endTime=LocalTime.now();
			System.out.println("end time: "+endTime.toString());
			long numMinutes =MaxTasks.startTime.until(endTime, ChronoUnit.MINUTES);
			System.out.println("minutes: " + numMinutes);
			totalTimerLabel.setText(""+numMinutes+" minutes");
			MaxTasks.startTime=null;
			startTimeLabel.setText("");
			MaxTasks.timerNumMinutes= (int)numMinutes;
		}
	}

	
	//------------------------------SWITCH VIEWS----------------------------------------------------------
	public void switchToChecklist(ActionEvent e) throws IOException{
		System.out.println("We pressed button to switch");
		Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
		ManageFrontend.DisplayScreen("ChecklistPage.fxml", e , "Checklist Page", p);
	}
	
	//switch to addtask view to add tasks
	public void switchToAddTask(ActionEvent e) throws IOException{
		Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/AddTask.fxml"));
		ManageFrontend.DisplayScreen("AddTask.fxml", e , "Adding a Task", p);
	}
	
	//switch to deletetask view to delete tasks
	public void switchToDeleteTask(ActionEvent e) throws IOException{
		Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/DeleteSelectedTask.fxml"));
		ManageFrontend.DisplayScreen("DeleteSelectedTask.fxml", e , "Deleting a Task", p);
	}
	
	public void switchToEditTask(ActionEvent e) throws IOException{
		Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/EditSelectedTask.fxml"));
		ManageFrontend.DisplayScreen("EditSelectedTask.fxml", e , "Editing a Task", p);
	}
}
