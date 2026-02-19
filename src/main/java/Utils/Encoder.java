package Utils;

import java.io.*;
import java.util.*;
import javax.json.*;

import Models.*;
import Services.*;

public class Encoder {
	
	private static void encodeCategoriesToJson() {
		JsonArrayBuilder arr = Json.createArrayBuilder();
		
		for (Category category: CategoryList.instance.getCategories()) {
			JsonObject obj = Json.createObjectBuilder()
					.add("title", category.getTitle())
					.add("description", (category.getDescription() == null) ? "null" : category.getDescription())
					.build();
			arr.add(obj);
		}
		
		writeIntoJsonFile(arr, Path.CATEGORIES);
	}
	
	private static void encodePrioritiesToJson() {
		JsonArrayBuilder arr = Json.createArrayBuilder();
		
		for (Map.Entry<Priority, Integer> entry : PriorityList.instance.getPriorities().entrySet()) {
			Priority key = entry.getKey();
			Integer value = entry.getValue();
			
			JsonObject obj = Json.createObjectBuilder()
					.add("index", value.intValue())
					.add("title", key.getTitle())
					.add("description", (key.getDescription() == null) ? "null" : key.getDescription())
					.build();
			arr.add(obj);
		}
		
		writeIntoJsonFile(arr, Path.PRIORITIES);
	}
	
	private static void encodeTasksToJson() {
		JsonArrayBuilder arr = Json.createArrayBuilder();
		
		for (Task task: TaskList.instance.getTasks()) {
			JsonArrayBuilder nots = Json.createArrayBuilder();
			for (Notification notification: task.getNotifications()) {
				JsonObject obj = Json.createObjectBuilder()
						.add("type", notification.getType().toString())
						.add("notificationDate", notification.getNotificationDate().toString())
						.build();
				nots.add(obj);
			}
			
			JsonObject obj = Json.createObjectBuilder()
					.add("title", task.getTitle())
					.add("description", (task.getDescription() == null) ? "null" : task.getDescription())
					.add("deadline", task.getDeadline().toString())
					.add("category", (task.getCategory() == null) ? "null" : task.getCategory().getTitle())
					.add("priority", (task.getPriority() == null) ? "null" : task.getPriority().getTitle())
					.add("status", task.getStatus().toString())
					.add("notifications", nots)
					.build();
			arr.add(obj);
		}
		
		writeIntoJsonFile(arr, Path.TASKS);
	}
	
	private static void encodeNotificationsToJson() {
		JsonArrayBuilder arr = Json.createArrayBuilder();
		
		for (Notification notification: NotificationHistory.instance.getReadNotifications()) {
			JsonObject obj = Json.createObjectBuilder()
					.add("task", notification.getTask().getTitle())
					.add("type", notification.getType().toString())
					.add("notificationDate", notification.getNotificationDate().toString())
					.build();
			arr.add(obj);
		}
		
		writeIntoJsonFile(arr, Path.NOTIFICATIONS);
	}
	
	public static void encodeDataToJson() {
		encodeCategoriesToJson();
		encodePrioritiesToJson();
		encodeTasksToJson();
		encodeNotificationsToJson();
	}
	
	public static void encodeEmptyListToJson(Path path) {
		writeIntoJsonFile(Json.createArrayBuilder(), path);
	}
	
	private static void writeIntoJsonFile(JsonArrayBuilder arr, Path path) {
		try (
				FileOutputStream fos = new FileOutputStream(path.getPath());
				JsonWriter jsonWriter = Json.createWriter(fos);
			) {
			jsonWriter.writeArray(arr.build());
		} catch (FileNotFoundException e) {
			File newFile = new File(path.getPath());
			newFile.getParentFile().mkdirs();
			
			writeIntoJsonFile(arr, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
