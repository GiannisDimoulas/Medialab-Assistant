package Utils;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;
import javax.json.*;

import Models.*;
import Services.NotificationHistory;

public class Decoder {
	
	private static void decodeCategoriesFromJson() {
		try {
			File f = new File(Path.CATEGORIES.getPath());
			URL url = f.toURI().toURL();
			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			JsonArray obj = rdr.readArray();
			for (JsonValue value : obj) {
				JsonObject jsonObject = value.asJsonObject();
	            String title = jsonObject.getString("title");
	            String description = (jsonObject.getString("description").equals("null"))
	            		? null
	            		: jsonObject.getString("description");
	            
	            Category.newCategory(title, description);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			handleFileNotFoundException(Path.CATEGORIES);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void decodePrioritiesFromJson() {
		try {
			File f = new File(Path.PRIORITIES.getPath());
			URL url = f.toURI().toURL();
			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			JsonArray obj = rdr.readArray();
			for (JsonValue value : obj) {
				JsonObject jsonObject = value.asJsonObject();
				Integer index = jsonObject.getInt("index");
	            String title = jsonObject.getString("title");
	            String description = (jsonObject.getString("description").equals("null"))
	            		? null
	            		: jsonObject.getString("description");
	            
	            Priority.newPriority(title, description, index);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			handleFileNotFoundException(Path.PRIORITIES);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void decodeNotificationsFromJson() {
		try {
			File f = new File(Path.NOTIFICATIONS.getPath());
			URL url = f.toURI().toURL();
			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			JsonArray obj = rdr.readArray();
			for (JsonValue value : obj) {
				JsonObject jsonObject = value.asJsonObject();
				String taskTitle = jsonObject.getString("task");
				NotificationType type = NotificationType.valueOf(jsonObject.getString("type"));
	            LocalDate notificationDate = LocalDate.parse(jsonObject.getString("notificationDate"));
	            
	            NotificationHistory.instance.addReadNotification(new Notification(taskTitle, type, notificationDate));
			}
			
			NotificationHistory.instance.searchForNewNotifications();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			handleFileNotFoundException(Path.PRIORITIES);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void decodeTasksFromJson() {	
		try {
			File f = new File(Path.TASKS.getPath());
			URL url = f.toURI().toURL();
			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			JsonArray obj = rdr.readArray();
			for (JsonValue value : obj) {
				JsonObject jsonObject = value.asJsonObject();
	            String title = jsonObject.getString("title");
	            String description = (jsonObject.getString("description").equals("null"))
	            		? null
	            		: jsonObject.getString("description");
	            LocalDate deadline = LocalDate.parse(jsonObject.getString("deadline"));
	            String category = (jsonObject.getString("category").equals("null"))
	            		? null
	            		: jsonObject.getString("category");
	            String priority = jsonObject.getString("priority");
	            Status status = Status.valueOf(jsonObject.getString("status"));
	            
	            ArrayList<Notification> nots = new ArrayList<>();
	            JsonArray notificationsArray = jsonObject.getJsonArray("notifications");
	            for (JsonObject notificationObj : notificationsArray.getValuesAs(JsonObject.class)) {
	            	NotificationType type = NotificationType.valueOf(notificationObj.getString("type"));
	            	LocalDate notificationDate = LocalDate.parse(notificationObj.getString("notificationDate"));
	            	
	            	nots.add(new Notification(type, notificationDate));
	            }
	            
	            Task.newTask(title, description, deadline, category, priority, status, nots);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			handleFileNotFoundException(Path.TASKS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void decodeDataFromJson() {
		decodeCategoriesFromJson();
		decodePrioritiesFromJson();
		decodeTasksFromJson();
		decodeNotificationsFromJson();
	}
	
	private static void handleFileNotFoundException(Path path) {
		File newFile = new File(path.getPath());
		newFile.getParentFile().mkdirs();
            
		Encoder.encodeEmptyListToJson(path);
	}
}
