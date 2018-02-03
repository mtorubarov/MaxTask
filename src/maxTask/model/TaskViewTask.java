package maxTask.model;

public class TaskViewTask implements java.io.Serializable {
	public int id;
	public String name;
	public int[] dates;
	
	
	public int dayOfWeeks;
	public int numberDaysRepeats;
	public int numberWeeksRepeats;
	public int numberMonthsRepeats;
	public	int[] otherDays;
	public int dayEnds;
	

	public TaskViewTask(String name, int[] dates, int id, int dayOfWeeks, int numberDaysRepeats, int numberWeeksRepeats,  int numberMonthsRepeats, int[] otherDays, int dayEnds) {
		this.name=name;
		this.dates=dates;
		this.id=id;
		
		this.dayOfWeeks=dayOfWeeks;
		this.numberDaysRepeats=numberDaysRepeats;
		this.numberWeeksRepeats=numberWeeksRepeats;
		this.numberMonthsRepeats=numberMonthsRepeats;
		this.otherDays=otherDays;
		this.dayEnds=dayEnds;
	}
	
	public String toString(){
		String temp = "id: "+id+"| name: "+name;
		//for()
		return temp;
	}
}

