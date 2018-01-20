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
	private int id;
	private String name;
	private int preferredTime;
	private int spentTime;
	
	
	
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
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id=id;
	}

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
	
	
	
}
