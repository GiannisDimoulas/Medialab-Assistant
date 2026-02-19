package Services;

import java.time.*;
import java.util.*;

import Models.*;

public class NotificationHistory {
	private HashMap<Notification, Boolean> notifications;
	
	// Singleton design pattern
	public static NotificationHistory instance = new NotificationHistory(new HashMap<>());
	
	// The constructor
	private NotificationHistory(HashMap<Notification, Boolean> notifications) {
		this.notifications = notifications;		
	}
	
	
	/*
	 * The getters
	 */
	
	public HashMap<Notification, Boolean> getNotifications() {
		return this.notifications;
	}
	
	public ArrayList<Notification> getUnreadNotifications() {
		ArrayList<Notification> unreadNotifications = new ArrayList<>();
		for (Map.Entry<Notification, Boolean> entry: this.notifications.entrySet()) {
			if (entry.getValue().equals(false)) {
				unreadNotifications.add(entry.getKey());
			}
		}
		
		unreadNotifications.sort((n1, n2) -> n2.getNotificationDate().compareTo(n1.getNotificationDate()));
		return unreadNotifications;
	}
	
	public ArrayList<Notification> getReadNotifications() {
		ArrayList<Notification> readNotifications = new ArrayList<>();
		for (Map.Entry<Notification, Boolean> entry: this.notifications.entrySet()) {
			if (entry.getValue().equals(true)) {
				readNotifications.add(entry.getKey());
			}
		}
		
		readNotifications.sort((n1, n2) -> n2.getNotificationDate().compareTo(n1.getNotificationDate()));
		return readNotifications;
	}
	
	public Notification searchNotification(Notification notification) {
		Notification history = find(notification.getTask().getTitle(), notification.getType());
		if (history == null) {
			return notification;
		} else {
			return history;
		}
	}
	
	public Notification get(Notification notification) {
		return find(notification.getTask().getTitle(), notification.getType());
	}
	
	private Notification find(String taskTitle, NotificationType type) {
		for (Map.Entry<Notification, Boolean> entry: this.notifications.entrySet()) {
			if (entry.getKey().getTask().getTitle().equals(taskTitle) && entry.getKey().getType().equals(type)) {
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	/*
	 * The setters
	 */
	
	public void addReadNotification(Notification notification) {
		if (this.find(notification.getTask().getTitle(), notification.getType()) == null) {
			this.notifications.put(notification.getTask().getNotification(notification.getType()), true);
		}
	}
	
	public void searchForNewNotifications() {
		for (Task task: TaskList.instance.getTasks()) {
			for (Notification notification: task.getNotifications()) {
				if ((notification.getNotificationDate().isBefore(LocalDate.now()) || notification.getNotificationDate().isEqual(LocalDate.now())) && this.find(notification.getTask().getTitle(), notification.getType()) == null) {
					this.notifications.put(notification, false);
				}
			}
		}
	}
	
	public void seeNotifications() {
		for (Map.Entry<Notification, Boolean> entry: this.notifications.entrySet()) {
			if (entry.getValue().equals(false)) {
				this.notifications.put(entry.getKey(), true);
			}
		}
	}
	
	public void removeNotification(Notification notification) {
		Notification not = this.find(notification.getTask().getTitle(), notification.getType());
		if (not != null) {
			this.notifications.remove(not);
			not.getTask().removeNotification(not);
		}
	}
}
