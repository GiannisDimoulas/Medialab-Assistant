package Services;

import java.time.LocalDate;
import java.util.*;

import Models.*;

public class TaskList {
	private ArrayList<Task> tasks = new ArrayList<>();
	
	// Singleton design pattern
	public static TaskList instance = new TaskList(new ArrayList<>());
	
	// The constructor
	private TaskList (ArrayList<Task> tasks) {
		this.tasks = tasks;
	}
	
	
	/*
	 * The getters
	 */
	
	public ArrayList<Task> getTasks() {
		return this.tasks;
	}
	
	public Task get(String task) {
		int idx = this.find(task);
		if (idx != -1) {
			return this.tasks.get(idx);
		} else {
			return null;
		}
	}
	
	public ArrayList<Task> getStatusList(Status status) {
		ArrayList<Task> statusList = new ArrayList<>();
		for (Task task: this.tasks) {
			if (task.getStatus().equals(status)) {
				statusList.add(task);
			}
		}
		
		return statusList;
	}
	
	public ArrayList<Task> getUpcomingList() {
		ArrayList<Task> upcomingList = new ArrayList<>();
		LocalDate week = LocalDate.now().plusWeeks(1);
		for (Task task: this.tasks) {
			if (task.getDeadline().isBefore(week) && !task.getDeadline().isBefore(LocalDate.now())) {
				upcomingList.add(task);
			}
		}
		
		return upcomingList;
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
	
	public void reset(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void add(Task task) {
		if (this.find(task.getTitle()) == -1) {
			this.tasks.add(task);
		}
	}
	
	public void remove(Task task) {
		int idx = this.find(task.getTitle());
		if (idx != -1) {
			this.tasks.remove(idx);
			if (task.getCategory() != null) {
				task.getCategory().removeTask(task);
			}
			task.getPriority().removeTask(task);
			
			for (Notification notification: task.getNotifications()) {
				NotificationHistory.instance.getNotifications().remove(notification);
			}
		}
	}
}
