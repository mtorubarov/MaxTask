package maxTask.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Task implements java.io.Serializable{
	int id;
	private String name;
	private int preferredTime;
	private int spentTime;
	
	/**
	 * Default serialVersionUID  
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * directory to store the app's data 
	 */
	public static final String storeDir = "data";
	
	//2 arg constructor
	public Task(String name, int preferredTime, int id){
		this.id=id;
		this.name=name;
		this.preferredTime=preferredTime;
		spentTime=0;
	}
	
	//1 arg constructor
	public Task(String name, int id){
		this(name,0, id);
	}
	
	//time changes
	public void addSpentTime(int time){
		spentTime=spentTime+time;
		if(spentTime>preferredTime){
			spentTime=preferredTime;
		}
	}
	public void subtractSpentTime(int time){
		spentTime=spentTime-time;
		if(spentTime<0){
			spentTime=0;
		}
	}
	
	
	//getterz and setters
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public int getPreferredTime(){
		return preferredTime;
	}
	
	public void setPreferredTime(int time){
		preferredTime=time;
	}
	
	public int getSpentTime(){
		return spentTime;
	}
	
	public void setSpentTime(int time){
		spentTime=time;
	}
	
	//serialization
	/**
	 * This method must be called before quitting the application from any where so that when the app is 
	 * run again the data from the previous session was saved and is seen under admin when we Deseraialize 
	 * admin in next session   
	 * @param Admin instance "o" 
	 */
	public static void SerializeTask (List<Task> o) throws IOException{
		System.out.println("We are serializing tasks");
		//For "Tasks" we will have a file under "data" directory named "task" 
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + "tasks"));
		oos.writeObject(o);
		oos.close();
	}
	
	/**de-serializes the admin
	 * @return admin
	 * @throws IOException input/output exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static List<Task> FetchTasks(List<Task> tasks)throws IOException, ClassNotFoundException {
		System.out.println("We are fetching tasks");
		ObjectInputStream ois ;
		//this is the file from where we will read the stored data
		try{
			ois = new ObjectInputStream(new FileInputStream("data/tasks"));
		}catch(FileNotFoundException e ){
			File f = new File("data/tasks") ;
			f.createNewFile() ; 
			Task.SerializeTask(tasks);
			ois = new ObjectInputStream(new FileInputStream("data/tasks"));
		}
		List<Task> newTasks = (List<Task>)ois.readObject();
		ois.close();
		if(newTasks==null){
			newTasks=new ArrayList<Task>();
		}
		return newTasks; 
	}
	
}
