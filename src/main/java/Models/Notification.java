package Models;

import java.time.*;
import java.time.temporal.*;

import Services.TaskList;

public class Notification {
	private Task task;
	private NotificationType type;
	private LocalDate notificationDate;
	
	// The constructor
	public Notification(Task task, NotificationType type, LocalDate notificationDate) {
		this.task = task;
		this.type = type;
		this.notificationDate = ((notificationDate == null) ? this.calculateNotificationDate() : notificationDate);
	}
	
	public Notification(String taskTitle, NotificationType type, LocalDate notificationDate) {
		this.task = TaskList.instance.get(taskTitle);
		this.type = type;
		this.notificationDate = ((notificationDate == null) ? this.calculateNotificationDate() : notificationDate);
	}
	
	public Notification(NotificationType type, LocalDate notificationDate) {
		this.task = null;
		this.type = type;
		this.notificationDate = ((notificationDate == null) ? this.calculateNotificationDate() : notificationDate);
	}
	
	public Notification(NotificationType type) {
		this.type = type;
	}
	
	/*
	 * The getters
	 */
	
	public Task getTask() {
		return task;
	}
	
	public NotificationType getType() {
		return type;
	}
	
	public LocalDate getNotificationDate() {
		return notificationDate;
	}
	
	public String getNotificationMessage() {
		String message;
		switch (this.type) {
		case ONE_DAY_BEFORE:
			message = "Your task, " + this.task.getTitle() + ", is due tomorrow.";
			break;
		case ONE_WEEK_BEFORE:
			message = "Your task, " + this.task.getTitle() + ", expires in a week from now.";
			break;
		case ONE_MONTH_BEFORE:
			message = "Your task, " + this.task.getTitle() + ", expires next month.";
			break;
		case DUE_DAY:
			message = "Your task, " + this.task.getTitle() + ", expires today.";
			break;
		case CUSTOM:
			message = "There are " + ChronoUnit.DAYS.between(this.notificationDate, this.task.getDeadline()) + " days remaining for you task, " + this.task.getTitle() + ".";
			break;
		default:
			message = "Invalid Notification Type";
			break;
		}
		return message;
	}
	
	private LocalDate calculateNotificationDate() {
		switch (this.type) {
		case ONE_DAY_BEFORE:
			return this.task.getDeadline().minusDays(1);
		case ONE_WEEK_BEFORE:
			return this.task.getDeadline().minusWeeks(1);
		case ONE_MONTH_BEFORE:
			return this.task.getDeadline().minusMonths(1);
		case DUE_DAY:
			return this.task.getDeadline();
		case CUSTOM:
			System.err.println("Invalid Action: Cannot create a custom notification without a notification date input.");
		default:
			System.err.println("Invalid Notification Type");
			return this.task.getDeadline();
		}
	}
	
	
	/*
	 * The setters
	 */
	
	public void assignToTask (Task task) {
		this.task = task;
		if (this.notificationDate == null) {
			this.notificationDate = calculateNotificationDate();
		}
	}
}
