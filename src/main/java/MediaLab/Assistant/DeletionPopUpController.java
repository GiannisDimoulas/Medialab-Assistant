package MediaLab.Assistant;

import Models.*;
import Services.*;
import javafx.stage.Stage;

public class DeletionPopUpController {
	private Stage stage;
	private Task task;
	private DashboardController dashboardController;
	
	private void setStage(Stage stage) {
		this.stage = stage;
	}
	
	private void setParentController(DashboardController dashboardController) {
		this.dashboardController = dashboardController;
	}
	
	private void assignTask(Task task) {
    	this.task = task;
    }
	
	public void setPopUp(Stage stage, DashboardController dashboardController, Task task) {
		setStage(stage);
		setParentController(dashboardController);
		assignTask(task);
	}
    
    public Task getTask() {
    	return task;
    }
	
	public void delete() {
		TaskList.instance.remove(task);
		stage.close();
		dashboardController.refreshUI();
	}
	
	public void cancel() {
		stage.close();
	}
}
