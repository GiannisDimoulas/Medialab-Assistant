package Models;

public enum Status {
	OPEN ("Open", "The task is pending."),
	IN_PROGRESS ("In Progress", "The task is currently being worked on."),
	POSTPONED ("Postponed", "The task has been delayed but will be resumed later."),
	COMPLETED ("Completed", "The task has succesfully been completed."),
	DELAYED ("Delayed", "The task is overdue.");
	
	private final String title, description;
	
	Status (String title, String description) {
		this.title = title;
		this.description = description;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static Status get(String title) {
		for (Status status : Status.values()) {
			if (status.getTitle().equals(title)) {
				return status;
			}
		}
		
		return null;
	}
}
