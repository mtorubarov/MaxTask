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
	//for serialization
	private static final long serialVersionUID = 1L;
	public static final String storeDir = "data";		//directory for storing serializations
	
	
	//just has list of days
	public TaskViewDate[] allDates;			//each date has a list of tasks (id, name, times)
	
	//hash table of all tasks (hash is id)
	public Hashtable<Integer,TaskViewTask> allTasks;	//each TaskViewTask has an id, name, other dates 
	
	//for figuring out id's of tasks
	private int lastID;
	
	//------------------------------CONSTRUCTOR----------------------------------------------------------
	public TaskView(){
		allDates = new TaskViewDate[366];
		for(int i =0; i<366; i++){
			allDates[i]=new TaskViewDate();
		}
		allTasks = new Hashtable<Integer, TaskViewTask>();
	}

	//------------------------------DEPRECATED DATE METHODS----------------------------------------------
	/*
	public static int getDateNumber(int year, int month, int day){
		LocalDate thisDate= LocalDate.of(year, month, day);
		return thisDate.getDayOfYear();
	}
	
	//returns 0th index: year, 1th index: month, 2nd index: day
	public static LocalDate getTodayDate(){
		LocalDate today = LocalDate.now(); 
		int year = today.getYear(); 
		int month = today.getMonthValue(); 
		int day = today.getDayOfMonth();
		int[] result = new int[3];
		result[0]=year;
		result[1]=month;
		result[2]=day;
		
		return today;
	}*/
	
	//------------------------------ADDING TASKS METHODS---------------------------------------------------
	
	//method to get new id (use it only once per new task)
	public int getNewID(){
		lastID++;
		System.out.println("We got new id: "+lastID);
		return lastID;
	}

	//figure out based on a number of parameters, all days that this event happens. Then add this event to each date
	//remember , we go to <= day ends
	public void addTask(String name, int preferredTime, int firstDay, int dayOfWeeks, int numberDaysRepeats, int numberWeeksRepeats,  int numberMonthsRepeats, int[] otherDays, int dayEnds){
		//ALREADY HAVE name, preferredTime
		
		//GET NEW ID FOR THE TASK
		int newId=getNewID();
		
		//FIGURE OUT ALL THE DAYS THAT THIS IS SUPPOSED TO HAPPEN IN
		List<Integer> days = new ArrayList<>();
		int currentYear=LocalDate.now().getYear();
		
		//ADD THE FIRST DAY
		if(firstDay<0 && firstDay>366){
			System.out.println("bad input: day number is out of whack");
			return;
		}
		days.add(firstDay);
		
		//FIGURE OUT DAY OF WEEKS
		int originalDayOfWeeks = dayOfWeeks;//for later storage since we will change dayOfWeeks variable
		if(dayOfWeeks!=0){			
			List<Integer> additions = new ArrayList<>();
			
			if(dayOfWeeks/64==1){
				//monday
				additions.add(1);
				dayOfWeeks=dayOfWeeks-64;
				System.out.println("monday included!");
			}
			if(dayOfWeeks/32==1){
				//tuesday
				additions.add(2);
				dayOfWeeks=dayOfWeeks-32;
				System.out.println("tuesday included!");
			}
			if(dayOfWeeks/16==1){
				//wednesday
				additions.add(3);
				dayOfWeeks=dayOfWeeks-16;
				System.out.println("wednesday included!");
			}
			if(dayOfWeeks/8==1){
				//thursday
				additions.add(4);
				dayOfWeeks=dayOfWeeks-8;
				System.out.println("thursday included!");
			}
			if(dayOfWeeks/4==1){
				//friday
				additions.add(5);
				dayOfWeeks=dayOfWeeks-4;
				System.out.println("friday included!");
			}
			if(dayOfWeeks/2==1){
				//saturday
				additions.add(6);
				dayOfWeeks=dayOfWeeks-2;
				System.out.println("saturday included!");
			}
			if(dayOfWeeks/1==1){
				//sunday
				additions.add(7);
				dayOfWeeks=dayOfWeeks-1;
				System.out.println("sunday included!");
			}
			
			
			int day = firstDay+1;
			while(day<=dayEnds){
				LocalDate date = LocalDate.ofYearDay(currentYear, day);
				DayOfWeek dateDayOfWeek = date.getDayOfWeek();
				
				if(additions.contains(dateDayOfWeek.getValue())){
					days.add(day);
				}
				day++;
			}
		}
		
		//FIGURE OUT THE DAYS, WEEKS, MONTHS THAT REPEAT
		if(numberDaysRepeats!=0){
			int day = firstDay+numberDaysRepeats;
			while(day<=dayEnds){
				days.add(day);
				day=day+numberDaysRepeats;
			}
		}
		if(numberWeeksRepeats!=0){
			int day = firstDay+numberWeeksRepeats*7;
			while(day<=dayEnds){
				days.add(day);
				day=day+numberWeeksRepeats*7;
			}
		}
		if(numberMonthsRepeats!=0){
			int numberRepeat=0;
			int day = firstDay+1;
			int wantMonthDate = LocalDate.ofYearDay(currentYear, firstDay).getDayOfMonth();
			while(day<=dayEnds){
				int currentMonthDate=LocalDate.ofYearDay(currentYear, day).getDayOfMonth();
				if(wantMonthDate==currentMonthDate){
					numberRepeat++;
					if(numberRepeat==numberMonthsRepeats){
						days.add(day);
						numberRepeat=0;
					}
				}
				
				day++;
			} 
		}
		
		for(int i =0 ; i<otherDays.length;i++){
			days.add(otherDays[i]);
		}
		
		//we have figured out all the days that this event has to happen. add each of those dates through another addTask method
		addTask(days, name, preferredTime, newId,  originalDayOfWeeks,  numberDaysRepeats,  numberWeeksRepeats,   numberMonthsRepeats, otherDays, dayEnds);
	
	}
	
	//method to add a new task for one date, given 
	//days are between 0 and 366, name is the name of the task, preferred time		//the rest are global variables
	public void addTask(List<Integer> days, String name, int preferredTime, int id, int dayOfWeeks, int numberDaysRepeats, int numberWeeksRepeats,  int numberMonthsRepeats, int[] otherDays, int dayEnds){
		System.out.println("We are doing second add with dayOfWeeks="+dayOfWeeks);
		int[] dates = new int[366];
		
		//update all dates
		for(int day:days){
			Task newTask = new Task(name, preferredTime, id);
			allDates[day].tasks.add(newTask);
			dates[day]=1;
		}
		
		//update all tasks
		TaskViewTask taskViewTask = new TaskViewTask(name, dates, id,  dayOfWeeks,  numberDaysRepeats,  numberWeeksRepeats,   numberMonthsRepeats, otherDays, dayEnds);
		allTasks.put(id, taskViewTask);
		
	}

	//------------------------------DELETING TASKS METHODS------------------------------------------------

	//deleted task with a given id
	public void deleteTaskFromAll(int id){
		//first we find it in allTasks
		//lets print allTasks
		//printAllTasks();
		
		System.out.println("We are deleting all tasks of id: "+id);
		
		//we will with the id, get all the dates from the allTasks, then delete the allTasks entry
		//int[] dates = allTasks.get(id).dates;
		for(int i = 0; i<allDates.length; i++){
			List<Task> tasks = allDates[i].tasks;
			List<Task> tasksToBeRemoved = new ArrayList<Task>();
			for(int t=0; t<tasks.size();t++){
				Task task = tasks.get(t);
				if(task.getID()==id){
					System.out.println("MATCH: "+t);
					tasksToBeRemoved.add(task);
				}
			}
			for(Task task :tasksToBeRemoved){
				allDates[i].tasks.remove(task);	
			}
		}
		
		allTasks.remove(id);
	}
	
	//deleted task with a given id
	public void deleteTaskFromFollowing(int id, int startingDate){
		System.out.println("We are deleting all following tasks of id: "+id);
		
		//we will with the id, get all the dates from the allTasks, then delete the allTasks entry
		
		for(int i = startingDate; i<allDates.length; i++){
			List<Task> tasks = allDates[i].tasks;
			List<Task> tasksToBeRemoved = new ArrayList<Task>();
			for(int t=0; t<tasks.size();t++){
				Task task = tasks.get(t);
				if(task.getID()==id){
					System.out.println("MATCH: "+t);
					tasksToBeRemoved.add(task);
				}
			}
			for(Task task :tasksToBeRemoved){
				allDates[i].tasks.remove(task);	
				allTasks.get(id).dates[i]=0;
			}
		}
	}
	
	//deleted task with a given id
	public void deleteTaskFromThis(int id, int date){
		System.out.println("We are deleting all following tasks of id: "+id);

		List<Task> tasks = allDates[date].tasks;
		List<Task> tasksToBeRemoved = new ArrayList<Task>();
		for(int t=0; t<tasks.size();t++){
			Task task = tasks.get(t);
			if(task.getID()==id){
				System.out.println("MATCH: "+t);
				tasksToBeRemoved.add(task);
			}
		}
		for(Task task :tasksToBeRemoved){
			allDates[date].tasks.remove(task);	
			allTasks.get(id).dates[date]=0;
		}
		
	}

	
	//------------------------------HELPER METHODS--------------------------------------------------------
	
	//get the tasks for a specific day
	public List<Task> getTasks(int day){
		return allDates[day].tasks;
	}
		
	public void printAllTasks(){
		Set<Integer> keys = allTasks.keySet();
        for(int key: keys){
            System.out.println("Value of "+key+" is: "+allTasks.get(key));
        }
	
	}
	
	//------------------------------SERIALIZATION METHODS--------------------------------------------------
	
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
