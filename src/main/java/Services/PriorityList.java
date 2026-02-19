package Services;

import java.util.*;

import Models.*;

public class PriorityList {
	private HashMap<Priority, Integer> priorities = new HashMap<>();
	
	// Singleton design pattern
	public static PriorityList instance = new PriorityList(new HashMap<>());
	
	// The constructor
	private PriorityList(HashMap<Priority, Integer> priorities) {
		this.priorities = priorities;
	}
	
	
	/*
	 * The getters
	 */
	
	public HashMap<Priority, Integer> getPriorities() {
		return this.priorities;
	}
	
	public Priority get(String priority) {
		return find(priority);
	}
	
	public int getLevel(Priority priority) {
		return priorities.get(priority).intValue();
	}
	
	public ArrayList<Priority> getSortedList() {
		List<Map.Entry<Priority, Integer>> entryList = new ArrayList<>(priorities.entrySet());
		entryList.sort(Map.Entry.<Priority, Integer>comparingByValue(Comparator.reverseOrder()));
		ArrayList<Priority> sortedPriorities = new ArrayList<>();
		for (Map.Entry<Priority, Integer> entry: entryList) {
	        sortedPriorities.add(entry.getKey());
	    }
		
		return sortedPriorities;
	}
	
	private Priority find(String priority) {
		for (Map.Entry<Priority, Integer> entry: this.priorities.entrySet()) {
			if (entry.getKey().getTitle().equals(priority)) {
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	/*
	 * The setters
	 */
	
	public void reset(HashMap<Priority, Integer> priorities) {
		this.priorities = priorities;
	}
	
	public void add(Priority priority, int idx) {
		if (find(priority.getTitle()) == null) {
			for (Map.Entry<Priority, Integer> entry : priorities.entrySet()) {
				Priority key = entry.getKey();
				Integer value = entry.getValue();
				
				if (value >= idx) {
					priorities.put(key, value + 1);
				}
			}
			priorities.put(priority, idx);
		}
	}
	
	public void add(Priority priority) {
		this.add(priority, 0);
	}
	
	public void remove(Priority priority) {
		if (priority != Priority.DEFAULT) {
			Integer idx = priorities.get(priority);
			priorities.remove(priority);
			for (Task task: priority.getTasks()) {
				task.setPriority(Priority.DEFAULT);
			}
			
			for (Map.Entry<Priority, Integer> entry : priorities.entrySet()) {
				Priority key = entry.getKey();
				Integer value = entry.getValue();
				
				if (value > idx) {
					priorities.put(key, value - 1);
				}
			}
		} else {
			System.err.println("Invalid Action: priority level \"Default\" cannot be deleted");
		}
	}
	
	public void decreaseLevel(Priority priority, int level) {
		if (level < this.priorities.size()) {
			Integer oldLevel = priorities.get(priority);
			for (Map.Entry<Priority, Integer> entry : priorities.entrySet()) {
				Priority key = entry.getKey();
				Integer value = entry.getValue();
				
				if (value < oldLevel && value >= level && !key.equals(priority)) {
					priorities.put(key, value + 1);
				}
			}
			priorities.put(priority, level);
		}
	}
	
	public void increaseLevel(Priority priority, int level) {
		if (level < this.priorities.size()) {
			Integer oldLevel = priorities.get(priority);
			for (Map.Entry<Priority, Integer> entry : priorities.entrySet()) {
				Priority key = entry.getKey();
				Integer value = entry.getValue();
				
				if (value > oldLevel && value <= level && !key.equals(priority)) {
					priorities.put(key, value - 1);
				}
			}
		}
		priorities.put(priority, level);
	}
}
