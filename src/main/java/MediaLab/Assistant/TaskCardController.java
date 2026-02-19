package MediaLab.Assistant;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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

public class TaskCardController {
	// FXML Components
    @FXML
    private AnchorPane card, firstHalf, secondHalf;
    @FXML
    private HBox padding, descPadding, info;
    @FXML
    private VBox labels;
    @FXML
    private ImageView icon, options;
    @FXML
    private Label title, attributes, description;
    
    // Other attributes
    private DashboardController dashboardController;
    private Stage stage;
    private Task task;
    
    public void assignTask(Task task) {
    	this.task = task;
    	initialize();
    }
    
    public Task getTask() {
    	return task;
    }
    
    public void setParentController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
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
    	
    	// Icon adjusting
    	chooseIcon();
    	icon.fitHeightProperty().bind(firstHalf.heightProperty().divide(1.5));
    	icon.fitWidthProperty().bind(icon.fitHeightProperty());
    	
    	// Info Adjusting
    	labels.minWidthProperty().bind(info.widthProperty().multiply(0.9));
    	labels.maxWidthProperty().bind(info.widthProperty().multiply(0.9));
    	if (task != null) {
    		title.setText(this.task.getTitle());
    	}
    	title.fontProperty().bind(firstHalf.widthProperty().multiply(0.025).asObject().map(Font::font));
    	if (task != null) {
    		attributes.setText(this.task.getStatus().getTitle() + " | " 
    				+ ((this.task.getCategory() == null) ? "" : (this.task.getCategory().getTitle() + " | "))
    				+ this.task.getPriority().getTitle() + " | "
    				+ "Due: " + this.task.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    		);
    	}
    	attributes.fontProperty().bind(firstHalf.widthProperty().multiply(0.023).divide(1.8).asObject().map(Font::font));
    	
    	// Bind options
    	options.fitHeightProperty().bind(firstHalf.heightProperty().divide(2.3));
    	options.fitWidthProperty().bind(icon.fitHeightProperty());
    	
    	// Description adjusting
    	if (task != null) {
    		//System.out.println(task.getDescription());
    		if (task.getDescription() == null) {
    			description.setText("There is no description provided for this task. You can, always, add one by modifying your task.");
    		} else {
    			description.setText(truncateString());
    		}
    	}
    	description.fontProperty().bind(secondHalf.widthProperty().multiply(0.023).divide(1.5).asObject().map(Font::font));
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
					showPopup();
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
            		modifyTask();
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
    
    private void chooseIcon() {
    	if (this.task!= null) {
	    	switch (this.task.getStatus()) {
	    	case OPEN: {
	    		icon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "target.png").toExternalForm()));
	    		break;
	    	}
	    	case IN_PROGRESS: {
	    		icon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "gear.png").toExternalForm()));
	    		break;
	    	}
	    	case POSTPONED: {
	    		icon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "sleep.png").toExternalForm()));
	    		break;
	    	}
	    	case DELAYED: {
	    		icon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "time.png").toExternalForm()));
	    		break;
	    	}
	    	case COMPLETED: {
	    		icon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "checked.png").toExternalForm()));
	    		break;
	    	}
	    	default:
	    		icon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "task.png").toExternalForm()));
	    	}
    	}
    }
    
    public void showPopup() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeletionPopUp.fxml"));
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

        DeletionPopUpController popupController = loader.getController();
        popupController.setPopUp(popupStage, dashboardController, task);

        popupStage.showAndWait();
    }
    
    private String truncateString() {
        String fullText = task.getDescription();
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
    
    public void modifyTask() {
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyTask.fxml"));
	        Parent root = loader.load();
	        ModifyTaskController modifyTaskController = loader.getController();
	        modifyTaskController.setTask(this.task);

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
