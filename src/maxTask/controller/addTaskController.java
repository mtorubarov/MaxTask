package maxTask.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import maxTask.application.MaxTasks;
import maxTask.model.TaskView;
import maxTask.util.ManageFrontend;


//TODO: other days doesnt yet work
public class addTaskController {
	//to know what we are looking at:
	@FXML Label currentDateLabel;
	
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
	
    @FXML TextField repeatDayTextField;
    @FXML TextField repeatWeekTextField;
    @FXML TextField repeatMonthTextField;
     
    TaskView taskView;
	
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
		LocalDate todayDate = LocalDate.now();
		LocalDate newCurrentDate=LocalDate.ofYearDay(todayDate.getYear(), MaxTasks.currentDate);
		currentDateLabel.setText(""+newCurrentDate);
		
		//set the cells of the other dates date picker to be what is in the otherDates list
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (otherDates.contains(item))
                        {
                            //setTooltip(new Tooltip("Beware the Ides of March!"));
                            setStyle("-fx-background-color: #ff4444;");
                        } else {
                            setTooltip(null);
                            setStyle(null);
                        }
                    }
                };
            }
        };
        otherDaysDatePicker.setDayCellFactory(dayCellFactory);
		
        //adds the date to our overall list
		otherDaysDatePicker.setOnAction(new EventHandler<ActionEvent>() {
		     public void handle(ActionEvent t) {
		    	 System.out.println("HEY");
		         LocalDate date = otherDaysDatePicker.getValue();
		        
		         if(date!=null){						//because set the value to null... in the end when everything closes, make sure not to add the null
			         if(otherDates.contains(date)){
			        	 otherDates.remove(date);
			         }else{
			        	 otherDates.add(date);
			         }
		         }
		         
		         otherDaysDatePicker.setValue(null);
		     }
		 });
		
		//sets the date picker's on mouse clicked on close calendar
		otherDaysDatePicker.setOnMouseClicked(new EventHandler<Event>() {
	        @Override
	        public void handle(Event event) {
	        	if(hiddenCalendar==false){
		        	if(otherDaysDatePicker.isShowing()){
		        		System.out.println("This is showing??");
						buttonClicked = true;			//if showing calendar, clicked button, we clicked button
						otherDaysDatePicker.hide();		//
						buttonClicked = false;			//
						hiddenCalendar=true;
					}
	        	}else{
	        		hiddenCalendar=false;
	        	}
	        }
	    });
		
		//makes it so that when we hide the other date picker which happens automatically, we don't if the button isn't clicked
		otherDaysDatePicker.setOnHidden(event->{
			if(!buttonClicked){					//if didn't click button, show
				otherDaysDatePicker.show();
			}
		});
		
		//deserialize taskView
		taskView = TaskView.FetchTasks(taskView);
	}
	
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
	
	//This method will handle the adding the task or cancelling
	public void handle(ActionEvent e) throws Exception{
		Button b = (Button) e.getSource() ; //The button press which led to the calling of this method
		if(b == cancelButton){
			buttonClicked=true;						//hide the other days date picker to not produce any errors
			otherDaysDatePicker.hide();	
			
			//just switch back to main page
			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Main Page", p);
		}
		//add task
		else if(b==addTaskButton){
			buttonClicked=true;				//hide the other days date picker to not produce any errors
			otherDaysDatePicker.hide();
			
			/*
			String name = nameTextField.getText();
			int preferredTime=0;
			if(!timeTextField.getText().equals("")){
				preferredTime =Integer.parseInt(timeTextField.getText());		//Assume that integer
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
		
			
			LocalDate dayEndsLocalDate=dayEndsDatePicker.getValue();
			int dayEnds;
			if(dayEndsLocalDate!=null){
				dayEnds=dayEndsLocalDate.getDayOfYear();
			}else{
				dayEnds=365;
			}
			*/
			
			String name = getName();
			int preferredTime = getPreferredTime();
			int firstDay = MaxTasks.currentDate;
			int dayOfWeeks = getDaysOfWeek();
			int numberDaysRepeats = getNumberDaysRepeats();
			int numberWeeksRepeats = getNumberWeeksRepeats();
			int numberMonthsRepeats = getNumberMonthsRepeats();
			int[] otherDays = getOtherDays();
			int dayEnds = getDayEnds();
			
			taskView.addTask(name, preferredTime,firstDay, dayOfWeeks, numberDaysRepeats, numberWeeksRepeats, numberMonthsRepeats, otherDays, dayEnds);
			TaskView.SerializeTask(taskView);

			System.out.println("We pressed button to add task and will now switch");
			Parent p = FXMLLoader.load(getClass().getResource("/maxTask/view/MainPage.fxml"));
			ManageFrontend.DisplayScreen("MainPage.fxml", e , "Added a Task", p);
		}
		
		
	}
}
