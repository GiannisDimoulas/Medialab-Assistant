package MediaLab.Assistant;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import Models.Status;
import Services.*;

public class NewObjectPopUpController {
	@FXML
	private Label desc;
	private Stage stage;
	
	public void setStage(Stage stage, String message) {
		this.stage = stage;
		desc.setText(message);
	}
	
	public void cancel() {
		stage.close();
	}
}
