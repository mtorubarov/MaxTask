package maxTask.model;

public class TaskViewTask implements java.io.Serializable {
	int id;
	String name;
	int[] dates;
	

	public TaskViewTask(String name, int[] dates, int id) {
		this.name=name;
		this.dates=dates;
		this.id=id;
	}
	
	public String toString(){
		String temp = "id: "+id+"| name: "+name;
		//for()
		return temp;
	}
}

