package Services;

import java.util.*;

import Models.*;

public class CategoryList {
	private ArrayList<Category> categories = new ArrayList<>();
	
	// Singleton design pattern
	public static CategoryList instance = new CategoryList(new ArrayList<>());	
	
	// The constructor
	private CategoryList (ArrayList<Category> categories) {
		this.categories = categories;
	}

	
	/*
	 * The getters
	 */
	
	public ArrayList<Category> getCategories() {
		return this.categories;
	}
	
	public Category get(String category) {
		int idx = this.find(category);
		if (idx != -1) {
			return this.categories.get(idx);
		} else {
			return null;
		}
	}
	
	private int find(String category) {
		int idx;
		for (idx = 0; idx < this.categories.size(); ++idx) {
			if (this.categories.get(idx).getTitle().equals(category)) {
				return idx;
			}
		}
		
		return -1;
	}
	
	
	/*
	 * The setters
	 */
	
	public void reset(ArrayList<Category> categories) {
		this.categories = categories;
	}
	
	public void add(Category category) {
		if (this.find(category.getTitle()) == -1) {
			this.categories.add(category);
		}
	}
	
	public void remove(Category category) {
		int idx = this.find(category.getTitle());
		if (idx != -1) {
			this.categories.remove(idx);
			for (Task task: category.getTasks()) {
				task.setCategory(null);
				TaskList.instance.remove(task);
			}
		}
	}
}
