package maxTask.model;

import java.util.ArrayList;
import java.util.List;

public class TaskViewDate implements java.io.Serializable{
	public List<Task> tasks;
	
	
	public TaskViewDate(){
		tasks=new ArrayList<Task>();
	}
}
