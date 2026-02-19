package Utils;

public enum Path {
	
	// JSON Paths
	CATEGORIES ("medialab/categories.json"),
	PRIORITIES ("medialab/priorities.json"),
	TASKS ("medialab/tasks.json"),
	NOTIFICATIONS ("medialab/notifications.json"),
	
	// Images Folder Path
	IMAGES ("/Images/"),
	
	// Styles Paths
	DASHBOARD ("/Styles/Dashboard.css"),
	TASK_CARD ("/Styles/TaskCard.css"),
	DELETION_POPUP ("/Styles/DeletionPopUp.css"),
	NEW_TASK ("/Styles/NewTask.css"),
	MODIFY_TASK ("/Styles/ModifyTask.css"),
	CATEGORY_CARD ("/Styles/CategoryCard.css"),
	NEW_CATEGORY ("/Styles/NewCategory.css"),
	PRIORITY_CARD ("/Styles/PriorityCard.css"),
	NEW_PRIORITY ("/Styles/NewPriority.css");
	
	private final String path;
	
	Path (String path) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}
}
