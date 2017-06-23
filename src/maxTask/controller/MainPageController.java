package maxTask.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Timer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import maxTask.model.Task;



/**
 * @author Maxim Torubarov 
 */
public class MainPageController {
	//task view
	@FXML ListView<GridPane> taskListView;
	
	//reset
	@FXML Button resetButton;
	
	//change tasks
	@FXML Button addTaskButton;
	@FXML TextField addTaskTextField;
	@FXML Button deleteTaskButton;
	@FXML Button renameTaskButton;
	@FXML TextField renameTaskTextField;
	@FXML Button retimeTaskButton;
	@FXML TextField retimeTaskTextField;
	
	//time control
	@FXML Button addTimeButton;
	@FXML Button removeTimeButton;
	@FXML TextField timeTextField;
	
	//timer
	@FXML Button startTimerButton;
	@FXML Button endTimerButton;
	@FXML Label startTimeLabel;
	@FXML Label totalTimerLabel;
	
	//actually save button
	@FXML Button exitButton;
	
	List<Task> tasks;
	
    ObservableList<GridPane> tasks_observable = FXCollections.observableArrayList();
    
    @FXML Label totalPreferredTimeLabel;
	
	/**
	 * Initializes the front end UI. This method is automatically called
	 * after the fxml file has been loaded.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@FXML private void initialize() throws ClassNotFoundException, IOException{
		tasks=Task.FetchTasks(tasks);
		taskListView.setItems(tasks_observable);
		//for each of the Photo instances of current album, make its corresponding observable list
		updateList();
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
		//exit: just sserialize and nothing else. (basically save button)
		if(b == exitButton){
			Task.SerializeTask(tasks);
		}
		//add task
		else if(b==addTaskButton){
			addTask();
		}
		//reset tasks
		else if(b==resetButton){
			resetTimes();
		}
		//delete selected task
		else if(b==deleteTaskButton){
			deleteTask();
		}
		//rename selected task
		else if(b==renameTaskButton){
			renameTask();
		}
		//retime selected task
		else if(b==retimeTaskButton){
			retimeTask();
		}
		//add time
		else if(b==addTimeButton){
			addTime();
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
		
	}
	
	//helper methods
	public void printTasks(){
		System.out.println("TASKS---------------");
		int i =0;
		for(Task t: tasks){
			System.out.println(i+". name: "+t.getName()+"| preferred time: "+t.getPreferredTime()+"| spent time: "+t.getSpentTime());
			i++;
		}
	}
	
	public void updateList(){
		int totalTime =0;
		
		tasks_observable.clear();
		for(Task t: tasks){
			GridPane pane = getPaneForTask(t);
			tasks_observable.add(pane);
			totalTime+=t.getPreferredTime();
		}
		taskListView.refresh();
		
		//update total
		totalPreferredTimeLabel.setText(totalTime+"");
	}
	
	
	//button methods
	public void resetTimes(){
		for(Task t: tasks){
			t.setSpentTime(0);
		}
		updateList();
		printTasks();
	}

	public void deleteTask(){
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			temp=tasks.get(index);
			tasks.remove(index);
		}
		updateList();
		printTasks();
		
	}

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
	}
	
	public void addTime(){
		//get selected task
		int index=taskListView.getSelectionModel().getSelectedIndex();
		Task temp;
		if(index<0){
			System.out.println("Nothing selected");
		}else{
			temp=tasks.get(index);
			String timeString=timeTextField.getText();
			int timeInt = Integer.parseInt(timeString);
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
			temp=tasks.get(index);
			String timeString=timeTextField.getText();
			int timeInt = Integer.parseInt(timeString);
			temp.subtractSpentTime(timeInt);
		}
		updateList();
		printTasks();
	}
	
	public void addTask(){
		String name=addTaskTextField.getText();
		Task temp=new Task(name);
		tasks.add(temp);
		updateList();
		printTasks();
	}
	
	
	//TIMER
	LocalTime startTime;
	
	public void startTimer(){
		startTime=LocalTime.now();
		System.out.println("start time: "+startTime.toString());
		startTimeLabel.setText(startTime.toString());
		totalTimerLabel.setText("");
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
		}
	}
		
}
