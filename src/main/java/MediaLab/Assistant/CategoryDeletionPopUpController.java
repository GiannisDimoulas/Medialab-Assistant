package MediaLab.Assistant;

import Models.*;
import Services.*;
import javafx.stage.Stage;

public class CategoryDeletionPopUpController {
	private Stage stage;
	private Category category;
	private DashboardController dashboardController;
	
	private void setStage(Stage stage) {
		this.stage = stage;
	}
	
	private void setParentController(DashboardController dashboardController) {
		this.dashboardController = dashboardController;
	}
	
	private void assignCategory(Category category) {
    	this.category = category;
    }
	
	public void setPopUp(Stage stage, DashboardController dashboardController, Category category) {
		setStage(stage);
		setParentController(dashboardController);
		assignCategory(category);
	}
    
    public Category getCategory() {
    	return category;
    }
	
	public void delete() {
		CategoryList.instance.remove(category);
		stage.close();
		dashboardController.refreshUI();
	}
	
	public void cancel() {
		stage.close();
	}
}
