package MediaLab.Assistant;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import Models.*;
import Services.*;
import Utils.*;

public class PriorityCardController {
	// FXML Components
    @FXML
    private AnchorPane card, firstHalf, secondHalf;
    @FXML
    private HBox padding, descPadding, info, titlePadding;
    @FXML
    private VBox labels;
    @FXML
    private ImageView icon, options;
    @FXML
    private Label title, attributes, description;
    @FXML
    private ComboBox<String> taskList;
    @FXML
    private Spinner<Integer> level;
    
    
    // Other attributes
    private DashboardController dashboardController;
    private Stage stage;
    private Priority priority;
    
    public void assignPriority(Priority priority) {
    	this.priority = priority;
    	initialize();
    }
    
    public Priority getPriority() {
    	return priority;
    }
    
    public void setParentController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public Parent getRoot() {
        return card;
    }

    public void initialize() {
    	// Bind halves
    	firstHalf.minHeightProperty().bind(card.heightProperty().divide(2));
    	firstHalf.maxHeightProperty().bind(card.heightProperty().divide(2));
    	firstHalf.minWidthProperty().bind(card.widthProperty());
    	firstHalf.maxHeightProperty().bind(card.widthProperty());
    	secondHalf.minHeightProperty().bind(card.heightProperty().divide(2));
    	secondHalf.maxHeightProperty().bind(card.heightProperty().divide(2));
    	secondHalf.minWidthProperty().bind(card.widthProperty());
    	secondHalf.maxHeightProperty().bind(card.widthProperty());
    	
    	// Bind padding & spacing
    	padding.paddingProperty().bind(firstHalf.heightProperty().divide(8).map(padding -> new Insets(padding.doubleValue(), padding.doubleValue()/2, padding.doubleValue(), padding.doubleValue())));
    	descPadding.paddingProperty().bind(firstHalf.heightProperty().divide(8).map(padding -> new Insets(padding.doubleValue(), padding.doubleValue(), padding.doubleValue(), padding.doubleValue())));
    	info.minWidthProperty().bind(firstHalf.widthProperty().multiply(0.94));
    	info.maxWidthProperty().bind(firstHalf.widthProperty().multiply(0.94));
    	info.spacingProperty().bind(firstHalf.heightProperty().divide(8));
    	
    	// Info Adjusting
    	labels.minWidthProperty().bind(info.widthProperty().multiply(0.9));
    	labels.maxWidthProperty().bind(info.widthProperty().multiply(0.9));
    	titlePadding.paddingProperty().bind(firstHalf.heightProperty().divide(6).map(padding -> new Insets(0, 0, 0, padding.doubleValue())));
    	if (priority != null) {
    		title.setText(this.priority.getTitle());
    	}
    	title.fontProperty().bind(secondHalf.widthProperty().multiply(0.023).asObject().map(Font::font));
    	
    	taskList.prefWidthProperty().bind(labels.widthProperty().multiply(0.33));
    	if (priority != null) {
	    	for (Task task: this.priority.getTasks()) {
	    		taskList.getItems().add(task.getTitle());
	    	}
    	}
    	
    	// Bind options
    	options.fitHeightProperty().bind(firstHalf.heightProperty().divide(2.3));
    	options.fitWidthProperty().bind(options.fitHeightProperty());
    	
    	// Description adjusting
    	if (priority != null) {
    		//System.out.println(task.getDescription());
    		if (priority.getDescription() == null) {
    			description.setText("There is no description provided for this priority level. You can, always, add one by modifying it.");
    		} else {
    			description.setText(truncateString());
    		}
    	}
    	description.fontProperty().bind(secondHalf.widthProperty().multiply(0.023).divide(1.5).asObject().map(Font::font));
    	
    	level.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, PriorityList.instance.getPriorities().size()-1, 0));
    	if (priority != null) {
    		level.getValueFactory().setValue(PriorityList.instance.getLevel(priority));
    	}
    	
    	level.prefWidthProperty().bind(firstHalf.widthProperty().multiply(0.07));
        level.prefHeightProperty().bind(firstHalf.heightProperty().multiply(0.6));
        
        TextField textField = level.getEditor();
        textField.styleProperty().bind(
            Bindings.concat("-fx-font-size: ", level.widthProperty().divide(4).asString(), "px;")
        );
        
        level.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                DashboardController.handlePriorityChange(dashboardController, this, oldValue, newValue);
            }
        });
    }
    
    public void showPopupMenu() {
    	ContextMenu contextMenu = new ContextMenu();
    	
    	MenuItem delete = new MenuItem("Delete");
    	ImageView bin = new ImageView();
    	bin.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "bin.png").toExternalForm()));
    	bin.setFitWidth(20);
    	bin.setFitHeight(20);
    	delete.setGraphic(bin);
    	delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		if (priority.equals(Priority.DEFAULT)) {
            			showDefaultPopUp();
            		} else {
            			showPopup();
            		}					
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
    	
    	SeparatorMenuItem separator = new SeparatorMenuItem();
        
    	MenuItem modify = new MenuItem("Modify");
        ImageView edit = new ImageView();
        edit.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "edit.png").toExternalForm()));
        edit.setFitWidth(20);
        edit.setFitHeight(20);
    	modify.setGraphic(edit);
    	modify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		modifyPriority();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
        
        contextMenu.getItems().addAll(delete, separator, modify);
        
        double buttonRightX = options.localToScreen(options.getBoundsInLocal()).getMaxX();
        double buttonBottomY = options.localToScreen(options.getBoundsInLocal()).getMaxY();
        
        contextMenu.show(options, buttonRightX, buttonBottomY);
        Platform.runLater(() -> {
            contextMenu.setX(buttonRightX - contextMenu.getWidth()); // Align right edge
        });
    }
    	
    public void bind(DoubleBinding width) {
    	card.minWidthProperty().bind(width);
    	card.maxWidthProperty().bind(width);
    	card.minHeightProperty().bind(card.widthProperty().multiply(0.15));
    	card.maxHeightProperty().bind(card.widthProperty().multiply(0.15));
    }
    
    
    public void showPopup() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PriorityDeletionPopUp.fxml"));
        Parent root = loader.load();

        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.setResizable(false);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.getIcons().add(new Image(getClass().getResourceAsStream(Path.IMAGES.getPath() + "alert.png")));
        popupStage.setTitle("CAUTION");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(Path.DELETION_POPUP.getPath()).toExternalForm());
        popupStage.setScene(scene);

        PriorityDeletionPopUpController popupController = loader.getController();
        popupController.setPopUp(popupStage, dashboardController, priority);

        popupStage.showAndWait();
    }
    
    public void showDefaultPopUp() throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("NewObjectPopUp.fxml"));
        Parent root = loader.load();

        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.setResizable(false);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.getIcons().add(new Image(getClass().getResourceAsStream(Path.IMAGES.getPath() + "alert.png")));
        popupStage.setTitle("CAUTION");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(Path.DELETION_POPUP.getPath()).toExternalForm());
        popupStage.setScene(scene);

        NewObjectPopUpController popupController = loader.getController();
        popupController.setStage(popupStage, "The Default priority level cannot be deleted.");

        popupStage.showAndWait();
    }
    
    private String truncateString() {
        String fullText = priority.getDescription();
        int maxLines = 2;
        int maxCharsPerLine = 140;
        StringBuilder formattedText = new StringBuilder();

        int lineCount = 0; // Track number of lines

        for (int i = 0; i < fullText.length(); i += maxCharsPerLine) {
            if (lineCount >= maxLines) {
                formattedText.append("..."); // Append ellipsis when exceeding max lines
                break;
            }
            
            if (formattedText.length() > 0) formattedText.append("\n"); // Add newline between lines
            formattedText.append(fullText, i, Math.min(i + maxCharsPerLine, fullText.length()));

            lineCount++; // Increment line count
        }

        return formattedText.toString();
    }
    
    public void goToTask() {
    	modifyTask(TaskList.instance.get(taskList.getValue()), (Stage) taskList.getScene().getWindow());
    }
    
    private void modifyTask(Task task, Stage oldStage) {
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyTask.fxml"));
	        Parent root = loader.load();
	        ModifyTaskController modifyTaskController = loader.getController();
	        modifyTaskController.setTask(task);
	
	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource(Path.MODIFY_TASK.getPath()).toExternalForm());
	
	        // Create a new stage
	        Stage stage = new Stage();
	        stage.setTitle("MediaLab Assistant");
	        stage.getIcons().add(new Image(getClass().getResourceAsStream(Path.IMAGES.getPath() + "ntua_logo.png")));
	        stage.setMaximized(true);
	        stage.setScene(scene);
	
	        // Show new stage first, then close old one
	        stage.show();
	        oldStage.close();
	    } catch (Exception e) {
	        e.printStackTrace(); // Print the full error message to the console
	    }
	}
    
    public void modifyPriority() {
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPriority.fxml"));
	        Parent root = loader.load();
	        ModifyPriorityController modifyController = loader.getController();
	        modifyController.setPriority(this.priority);

	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource(Path.MODIFY_TASK.getPath()).toExternalForm());

	        // Get current stage safely
	        Stage oldStage = (Stage) options.getScene().getWindow();

	        // Create a new stage
	        Stage stage = new Stage();
	        stage.setTitle("MediaLab Assistant");
	        stage.getIcons().add(new Image(getClass().getResourceAsStream(Path.IMAGES.getPath() + "ntua_logo.png")));
	        stage.setMaximized(true);
	        stage.setScene(scene);

	        // Show new stage first, then close old one
	        stage.show();
	        oldStage.close();
	    } catch (Exception e) {
	        e.printStackTrace(); // Print the full error message to the console
	    }
	}
}
