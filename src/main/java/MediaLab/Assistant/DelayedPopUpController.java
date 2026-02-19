package MediaLab.Assistant;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import Models.Status;
import Services.*;

public class DelayedPopUpController {
	@FXML
	private Label desc;
	private Stage stage;
	
	public void setStage(Stage stage) {
		this.stage = stage;
		initialize();
	}
	
	public void cancel() {
		stage.close();
	}
	
	public void initialize() {
		desc.setText("Let's get back on track: " + TaskList.instance.getStatusList(Status.DELAYED).size() + " tasks are overdue.");
	}
}
