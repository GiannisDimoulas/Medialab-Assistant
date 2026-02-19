package Models;

import java.util.*;

import Services.*;

public class Priority {
	private String title;
	private String description; //= "No description available for this priority level.";
	private ArrayList<Task> tasks = new ArrayList<>();
	
	// A constant priority level
	public static final Priority DEFAULT = Priority.newPriority("Default", "The standard priority level representing a neutral tier of importance. It is automatically assigned to any new task if no other level is spesified.");
	
	/*
	 * The constructors
	 */
	
	private Priority (String title) {
		this.title = title;
	}
	
	private Priority (String title, String description) {
		this.title = title;
		this.description = description;
	}
	
	public static Priority newPriority (String title, String description) {
		Priority priority = new Priority (title, description);
		PriorityList.instance.add(priority);
		
		return priority;
	}
	
	public static Priority newPriority (String title, String description, int index) {
		Priority priority = new Priority (title, description);
		PriorityList.instance.add(priority, index);
		
		return priority;
	}
	
	
	/*
	 *  The getters
	 */
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ArrayList<Task> getTasks() {
		return tasks;
	}
	
	private int find(String task) {
		int idx;
		for (idx = 0; idx < this.tasks.size(); ++idx) {
			if (this.tasks.get(idx).getTitle().equals(task)) {
				return idx;
			}
		}
		
		return -1;
	}
	
	
	/*
	 * The setters
	 */
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addTask(Task task) {
		if (this.find(task.getTitle()) == -1) {
			this.tasks.add(task);
		}
	}
	
	public void removeTask(Task task) {
		int idx = this.find(task.getTitle());
		if (idx != -1) {
			this.tasks.remove(idx);
		}
	}
}
