package maxTask.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

//TODO: make serializable as well as other classes
public class TaskView implements java.io.Serializable{
	
	/**
	 * Default serialVersionUID  
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * directory to store the app's data 
	 */
	public static final String storeDir = "data";
	
	
	
	//just has list of days
	public TaskViewDate[] allDates;
		//each date has a list of tasks (id, name, times)
	
	//hash table of all tasks
	public Hashtable<Integer,TaskViewTask> allTasks;	//integer is the id
		//each TaskViewTask has an id, name, other dates 
	
	private static int lastID;
	
	//currently selected task
	private static Task currentTask;
	
	
	public TaskView(){
		allDates = new TaskViewDate[366];
		for(int i =0; i<366; i++){
			allDates[i]=new TaskViewDate();
		}
		allTasks = new Hashtable<Integer, TaskViewTask>();
		lastID=0;
	}

	//method to get new id (use it only once per new task)
	public static int getNewID(){
		System.out.println("We are getting new id");
		lastID++;
		System.out.println("We got new id: "+lastID);
		return lastID;
	}
	
	//date stuffs:
	
	public static int getDateNumber(int year, int month, int day){
		LocalDate thisDate= LocalDate.of(year, month, day);
		return thisDate.getDayOfYear();
	}
	
	//returns 0th index: year, 1th index: month, 2nd index: day
	public static int[] getTodayDate(){
		LocalDate today = LocalDate.now(); 
		int year = today.getYear(); 
		int month = today.getMonthValue(); 
		int day = today.getDayOfMonth();
		int[] result = new int[3];
		result[0]=year;
		result[1]=month;
		result[2]=day;
		return result;
	}
	
	public List<Task> getTasks(int day){
		return allDates[day].tasks;
	}
	
	//figure out based on 
		//today's date
		//days of weeks that repeats
		//repeats every how many weeks
		//other days
	//all the number of days that this event happens
	//requires one date at least
	public void addTask(String name, int preferredTime, int firstDay, int dayOfWeeks, int numberDaysRepeats, int numberWeeksRepeats,  int numberMonthsRepeats, int otherDays, int dayEnds){
		List<Integer> days = new ArrayList<>();
		int newId=getNewID();
		int currentYear=LocalDate.now().getYear();
		
		
		if(firstDay<0 && firstDay>366){
			System.out.println("bad input: day number is out of whack");
			return;
		}
		days.add(firstDay);
		
		//TODO: day of weeks and all other things
		if(dayOfWeeks!=0){
			//TODO: instead of first day of week do first day
			int tempDay=0;
			//figure out the day of the week of the first day of the current year
			LocalDate firstDayOfYear = LocalDate.ofYearDay(currentYear, 0);
			DayOfWeek firstDayOfWeek = firstDayOfYear.getDayOfWeek();
			List<Integer> additions = new ArrayList<>();
			//check monday
			//day of weeks will be an integer so: 0111 1111 which indicates all 7 days will be repeated 
			//if()
		}
		
		addTask(days, name, preferredTime, newId);
		
	}
	
	
	public void printAllTasks(){
		
		Set<Integer> keys = allTasks.keySet();
        for(int key: keys){
            System.out.println("Value of "+key+" is: "+allTasks.get(key));
        }
	
	}
	
	//deleted task with a given id
	public void deleteTaskFromAll(int id){
		//first we find it in allTasks
		//lets print allTasks
		//printAllTasks();
		
		System.out.println("We are deleting all tasks of id: "+id);
		
		//we will with the id, get all the dates from the allTasks, then delete the allTasks entry
		int[] dates = allTasks.get(id).dates;
		for(int i = 0; i<dates.length; i++){
			System.out.println("For date: "+i);
			List<Task> tasks = allDates[i].tasks;
			List<Task> tasksToBeRemoved = new ArrayList<Task>();
			for(int t=0; t<tasks.size();t++){
				System.out.println("For task: "+t);
				Task task = tasks.get(t);
				if(task.getID()==id){
					System.out.println("MATCH: "+t);
					tasksToBeRemoved.add(task);
				}
			}
			for(Task task :tasksToBeRemoved){
				System.out.println("Looking through tasks: "+task.getID());
				allDates[i].tasks.remove(task);
				
			}
			System.out.println("The size: "+ allDates[i].tasks.size());
			
		}
		
		
		//for each of the dates, we will go through the tasks and see if any are with that id
		
	}
	
	
	
	//method to add a new task for one date, given 
	//days are between 0 and 366, name is the name of the task, preferred time
	public void addTask(List<Integer> days, String name, int preferredTime, int id){
		int[] dates = new int[366];
		
		for(int day:days){
			System.out.println("day: "+day);
			Task newTask = new Task(name, preferredTime, id);
			allDates[day].tasks.add(newTask);
			dates[day]=1;
			
		}
		
		TaskViewTask taskViewTask = new TaskViewTask(name, dates, id);
		allTasks.put(id, taskViewTask);
		
	}

	
	
	//method to get date class provided date
	//TaskViewDate getDate()
	
	//method to remove a selected task
	//removeTask()
	
	

	//serialization
	
		/**
		 * This method must be called before quitting the application from any where so that when the app is 
		 * run again the data from the previous session was saved and is seen under admin when we Deseraialize 
		 * admin in next session   
		 * @param Admin instance "o" 
		 */
		public static void SerializeTask (TaskView o) throws IOException{
			System.out.println("We are serializing tasks");
			//For "Tasks" we will have a file under "data" directory named "task" 
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + "taskView3"));
			oos.writeObject(o);
			oos.close();
		}
		
		/**de-serializes the admin
		 * @return admin
		 * @throws IOException input/output exception
		 * @throws ClassNotFoundException class not found exception
		 */
		public static TaskView FetchTasks(TaskView tasks)throws IOException, ClassNotFoundException {
			System.out.println("We are fetching tasks");
			ObjectInputStream ois ;
			//this is the file from where we will read the stored data
			try{
				ois = new ObjectInputStream(new FileInputStream("data/taskView3"));
			}catch(FileNotFoundException e ){
				System.out.println("We did not find the serialization file");
				File f = new File("data/taskView3") ;
				f.createNewFile() ; 
				TaskView.SerializeTask(tasks);
				ois = new ObjectInputStream(new FileInputStream("data/taskView3"));
			}
			TaskView newTasks = new TaskView();
			try{
				newTasks = (TaskView)ois.readObject();
			}catch(ClassNotFoundException | InvalidClassException e){
				System.out.println("We couldn't deserialize file");
				/*File f = new File("data/taskView") ;
				f.createNewFile() ; 
				TaskView.SerializeTask(tasks);
				ois = new ObjectInputStream(new FileInputStream("data/taskView"));
				newTasks= (TaskView)ois.readObject();*/
			}
			ois.close();
			if(newTasks==null){
				newTasks=new TaskView();
			}
			return newTasks; 
		}
}
