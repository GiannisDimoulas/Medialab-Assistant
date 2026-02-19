package MediaLab.Assistant;

import Models.*;
import Services.*;
import javafx.stage.Stage;

public class PriorityDeletionPopUpController {
	private Stage stage;
	private Priority priority;
	private DashboardController dashboardController;
	
	private void setStage(Stage stage) {
		this.stage = stage;
	}
	
	private void setParentController(DashboardController dashboardController) {
		this.dashboardController = dashboardController;
	}
	
	private void assignPriority(Priority priority) {
    	this.priority = priority;
    }
	
	public void setPopUp(Stage stage, DashboardController dashboardController, Priority priority) {
		setStage(stage);
		setParentController(dashboardController);
		assignPriority(priority);
	}
    
    public Priority getPriority() {
    	return priority;
    }
	
	public void delete() {
		PriorityList.instance.remove(priority);
		stage.close();
		dashboardController.refreshUI();
	}
	
	public void cancel() {
		stage.close();
	}
}
