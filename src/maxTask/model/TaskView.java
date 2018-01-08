package maxTask.model;

import java.time.LocalDate;
import java.util.Hashtable;

public class TaskView implements java.io.Serializable{
	//just has list of days
	TaskViewDate[] allDates;
		//each date has a list of tasks (id, name, times)
	
	//hash table of all tasks
	Hashtable<Integer,TaskViewTask> allTasks;	//integer is the id
		//each TaskViewTask has an id, name, other dates 
	
	private static int lastID;
	
	public TaskView(){
		allDates = new TaskViewDate[366];
		allTasks = new Hashtable<Integer, TaskViewTask>();
		lastID=0;
	}

	//method to get new id (use it only once per new task)
	public static int getNewID(){
		lastID=lastID++;
		return lastID;
	}
	
	//date stuffs:
	
	//TODO: finish this method
	public static int getDateNumber(int year, int month, int day){
		LocalDate thisDate= LocalDate.of(year, month, day);
		return thisDate.getDayOfYear();
	}
	
	//0th index: year, 1th index: month, 2nd index: day
	public static int[] getTodayDate(){
		LocalDate today = LocalDate.now(); 
		int year = today.getYear(); 
		int month = today.getMonthValue(); 
		int day = today.getDayOfMonth();
		System.out.println("month: "+month);
		System.out.println("day: "+day);
		int[] result = new int[3];
		result[0]=year;
		result[1]=month;
		result[2]=day;
		return result;
	}
	
	//figure out based on 
		//today's date
		//days of weeks that repeats
		//repeats every how many weeks
		//other days
	//all the number of days that this event happens
	
	
	
	
	
	
	
	
	
	
	//method to add a new task for one date, given 
	//days are between 0 and 366, name is the name of the task, preferred time
	//addTask(int[] day, String name, preferredTime){
		//add it to allDates
		//for all days, for each day add everything
		//add it to allTasks
		//
		
	//}
	
	
	//method to get date class provided date
	//TaskViewDate getDate()
	
	//method to remove a selected task
	//removeTask()
	
	

}
