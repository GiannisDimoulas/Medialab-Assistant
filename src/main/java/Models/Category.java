package Models;

import java.util.*;

import Services.*;

public class Category {
	private String title;
	private String description; //= "No description available for this category.";
	private ArrayList<Task> tasks = new ArrayList<>();
	
	
	/*
	 * The constructors
	 */
	
	private Category (String title) {
		this.title = title;
	}
	
	private Category (String title, String description) {
		this.title = title;
		this.description = description;
	}
	
	/**
	 *  Creates a new Category Object and adds it into the instance of the categories' list.
	 *  
	 * @param title the new category's title.
	 * @param description the new category's description. If no description needs to be added, this parameter must be null.
	 * @return The Category Object.
	 */
	public static Category newCategory (String title, String description) {
		Category category = new Category(title, description);
		CategoryList.instance.add(category);
		
		return category;
	}
	
	
	/*
	 *  The getters
	 */
	
	/**
	 * Returns this Category object's title attribute.
	 * 
	 * @return this category's title.
	 */
	public String getTitle() {
		return title;
	}
	
	
	/**
	 * Returns this Category object's description attribute.
	 * 
	 * @return this category's description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns an ArrayList of the Task objects belonging to this category.
	 * 
	 * @return the ArrayLits of the Task objects belonging to this category.
	 */
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
	
	/**
	 * Sets this Category object's title.
	 * 
	 * @param title The String to be set as title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Sets this Category object's description.
	 * 
	 * @param description The String to be set as description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Adds a Task object under this Category.
	 * 
	 * @param task The new Task object to be associated with this Category.
	 */
	public void addTask(Task task) {
		if (this.find(task.getTitle()) == -1) {
			this.tasks.add(task);
		}
	}
	
	/**
	 * Removes a Task object from being under this Category.
	 * 
	 * @param task The Task object to cease being associated with this Category.
	 */
	public void removeTask(Task task) {
		int idx = this.find(task.getTitle());
		if (idx != -1) {
			this.tasks.remove(idx);
		}
	}
}
