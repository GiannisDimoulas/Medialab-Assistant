package Models;

import java.util.*;
import java.time.*;

import Services.*;

public class Task {
	private String title;
	private String description;
	private LocalDate deadline;
	private Category category;
	private Priority priority;
	private Status status;
	private ArrayList<Notification> notifications;
	
	
	/*
	 *  The constructors
	 */
	
	private Task(String title, String description, LocalDate deadline, Category category, Priority priority, Status status) {
		this.title = title;
		this.description = description;
		this.deadline = deadline;
		this.category = category;
		this.priority = ((priority == null) ? Priority.DEFAULT : priority);
		this.status = ((status == null) ? Status.OPEN : status);
		this.notifications = new ArrayList<>();
	}
	
	public static Task newTask(String title, String description, LocalDate deadline, Category category, Priority priority, Status status, ArrayList<Notification> notifications) {
		if (deadline.isBefore(LocalDate.now()) && !status.equals(Status.COMPLETED)) {
			status = Status.DELAYED;
		}
		Task task = new Task(title, description, deadline, category, priority, status);
		if (category != null) {
			category.addTask(task);
		}
		task.priority.addTask(task);
		for (Notification notification: notifications) {
			notification.assignToTask(task);
			task.addNotification(notification);
		}
		
		TaskList.instance.add(task);		
		return task;
	}
	
	public static Task newTask(String title, String description, LocalDate deadline, String categoryTitle, String priorityTitle, Status status, ArrayList<Notification> notifications) {
		Category category = CategoryList.instance.get(categoryTitle);
		Priority priority = PriorityList.instance.get(priorityTitle);
		if (deadline.isBefore(LocalDate.now()) && !status.equals(Status.COMPLETED)) {
			status = Status.DELAYED;
		}
		Task task = new Task(title, description, deadline, category, priority, status);
		if (category != null) {
			category.addTask(task);
		}
		task.priority.addTask(task);
		for (Notification notification: notifications) {
			notification.assignToTask(task);
			task.addNotification(notification);
		}
		
		TaskList.instance.add(task);	
		return task;
	}
	
	/*
	 * The getters
	 */
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public LocalDate getDeadline() {
		return deadline;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public ArrayList<Notification> getNotifications() {
		return notifications;
	}
	
	public Notification getNotification(NotificationType notificationType) {
		for (Notification notification: this.notifications) {
			if (notification.getType().equals(notificationType)) {
				return notification;
			}
		}
		
		return null;
	}
	
	/*
	 *  The setters
	 */
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	public void setCategory(Category category) {
		this.category = category;
		if (category != null) {
			category.addTask(this);
		}
	}
	
	public void changeCategory(Category category) {
		if (this.category != null) {
			this.category.removeTask(this);
		}
		this.category = category;
		if (category != null) {
			category.addTask(this);
		}
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
		priority.addTask(this);
	}
	
	public void changePriority(Priority priority) {
		this.priority.removeTask(this);
		this.priority = priority;
		priority.addTask(this);
	}
	
	public void setStatus(Status status) {
		this.status = status;
		if (this.status.equals(Status.COMPLETED)) {
			List<Notification> toRemove = new ArrayList<>();
			for (Notification notification: this.notifications) {
			    Notification not = NotificationHistory.instance.get(notification);
			    if (not != null) {
			        toRemove.add(not);
			    }
			}
			toRemove.forEach(NotificationHistory.instance::removeNotification);
			this.notifications.clear();
		}
	}
	
	public void chooseNotifications(ArrayList<Notification> notifications) {
		this.notifications = notifications;
		for (Notification notification: notifications) {
			notification.assignToTask(this);
			NotificationHistory.instance.removeNotification(notification);
			this.addNotification(notification);
		}
	}
	
	public void addNotification(Notification notification) {
		if (!this.notifications.contains(notification)) {
			this.notifications.add(NotificationHistory.instance.searchNotification(notification));
		}
	}
	
	public void removeNotification(Notification notification) {
		this.notifications.remove(notification);
	}
}