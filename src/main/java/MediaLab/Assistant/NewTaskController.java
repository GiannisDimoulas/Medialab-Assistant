package MediaLab.Assistant;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import Services.*;
import Utils.Path;
import Models.*;

public class NewTaskController {

	// FXML Components
	@FXML
	private VBox leftVBox, sizedBox, leftButtons, titleVBox, layout, restOptions, pickNotificationsVBox, perimeterVBox;
	@FXML
	private HBox topHBox, firstHBox, secondHBox, optionsHBox, lowerOptions;
	@FXML
	private AnchorPane searchBar, newTaskAnchor, notifAnchor, perimeter, pickNotifications;
	@FXML
	private ImageView logoImage, searchIcon, notifButton, newNotif;
	@FXML
	private Label logoLabel, pageTitleLabel, localdateLabel, notificationLabel;
	@FXML
	private Button dashboardButton, taskButton, categoryButton, priorityButton, save;
	@FXML
	private TextField searchField, titleField;
	@FXML
	private TextArea textArea;
	@FXML
	private ComboBox<String> pickCategory, pickPriority, pickStatus;
	@FXML
	private DatePicker pickDeadline;
	@FXML
	private RadioButton OneDayBefore, OneWeekBefore, OneMonthBefore, DueDay;
	
	public void initialize() {
		// Left menu adjusting
		leftVBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
            	// Left menu dimensions adjusting
            	leftVBox.prefWidthProperty().bind(newScene.widthProperty().multiply(0.25));
            	leftVBox.paddingProperty().bind(newScene.heightProperty().multiply(0.05).map(topPadding -> new Insets(topPadding.doubleValue(), 0, 0, 0)));
            	
            	// Logo image adjusting
            	logoImage.fitWidthProperty().bind(leftVBox.widthProperty().multiply(0.4));
            	
            	// Logo label adjusting
            	logoLabel.setText("MediaLab\nAssistant");
            	logoLabel.fontProperty().bind(leftVBox.widthProperty().multiply(0.1).asObject().map(Font::font));
            	
            	// Middle padding adjusting
            	sizedBox.prefHeightProperty().bind(newScene.heightProperty().multiply(0.1));
            	
            	// Buttons of left menu adjusting
            	leftButtons.prefWidthProperty().bind(leftVBox.widthProperty().multiply(0.8));
                leftButtons.maxWidthProperty().bind(leftVBox.widthProperty().multiply(0.8));
                leftButtons.spacingProperty().bind(newScene.widthProperty().multiply(0.03));
                initializeLeftButton(dashboardButton, newScene);
                initializeLeftButton(taskButton, newScene);
                initializeLeftButton(categoryButton, newScene);
                initializeLeftButton(priorityButton, newScene);
            }
        });
		
		// Top Part adjusting
		topHBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
            	// Top part dimensions adjusting
            	topHBox.minWidthProperty().bind(newScene.widthProperty().multiply(0.75));
            	topHBox.maxWidthProperty().bind(newScene.widthProperty().multiply(0.75));
            	topHBox.minHeightProperty().bind(newScene.heightProperty().multiply(0.35));
            	topHBox.maxHeightProperty().bind(newScene.heightProperty().multiply(0.35));
            	
            	// First half adjusting
            	firstHBox.prefHeightProperty().bind(topHBox.heightProperty().divide(3));
            	firstHBox.minWidthProperty().bind(topHBox.widthProperty());
            	firstHBox.maxWidthProperty().bind(topHBox.widthProperty());
            	firstHBox.paddingProperty().bind(newScene.heightProperty().multiply(0.03).map(
            		    padding -> new Insets(padding.doubleValue(), padding.doubleValue(), padding.doubleValue(), padding.doubleValue())
            	)); 
            	
            	// Title adjusting
            	titleVBox.prefWidthProperty().bind(topHBox.widthProperty().divide(4));
            	pageTitleLabel.fontProperty().bind(logoLabel.fontProperty());
            	localdateLabel.fontProperty().bind(logoLabel.fontProperty().map(font -> Font.font(font.getFamily(), font.getSize() / 2)));
            	localdateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            	
            	// Options adjusting
            	optionsHBox.prefWidthProperty().bind(topHBox.widthProperty().multiply(0.75));
            	searchBar.minHeightProperty().bind(optionsHBox.heightProperty().divide(2.5));
            	searchBar.maxHeightProperty().bind(optionsHBox.heightProperty().divide(2.5));
            	searchBar.minWidthProperty().bind(optionsHBox.widthProperty().multiply(0.5));
            	searchBar.maxWidthProperty().bind(optionsHBox.widthProperty().multiply(0.5));
            	searchIcon.fitHeightProperty().bind(searchBar.heightProperty().divide(2));
            	searchIcon.fitWidthProperty().bind(searchIcon.fitHeightProperty());
            	searchBar.heightProperty().addListener((observable, oldValue, newValue) -> {
            	    double anchor = newValue.doubleValue() / 4;
            	    AnchorPane.setTopAnchor(searchIcon, anchor);
            	    AnchorPane.setLeftAnchor(searchIcon, anchor);
            	    searchField.setPadding(new Insets(0, 0, 0, anchor * 4));
            	});
            	
            	notifAnchor.minHeightProperty().bind(searchBar.heightProperty());
            	notifAnchor.maxHeightProperty().bind(searchBar.heightProperty());
            	notifAnchor.minWidthProperty().bind(notifAnchor.heightProperty());
            	notifAnchor.maxWidthProperty().bind(notifAnchor.heightProperty());
            	notifButton.fitHeightProperty().bind(notifAnchor.heightProperty());
            	notifButton.fitWidthProperty().bind(notifAnchor.widthProperty());
            	newNotif.fitWidthProperty().bind(notifAnchor.widthProperty().multiply(0.3));
            	newNotif.fitHeightProperty().bind(newNotif.fitWidthProperty());
            	if (NotificationHistory.instance.getUnreadNotifications().size() > 0) {
            		newNotif.setVisible(true);
            	} else {
            		newNotif.setVisible(false);
            	}
            	optionsHBox.spacingProperty().bind(searchBar.widthProperty().divide(20));
            	 
            	// Second half adjusting
            	secondHBox.minHeightProperty().bind(topHBox.heightProperty().multiply(0.15));
            	secondHBox.maxHeightProperty().bind(topHBox.heightProperty().multiply(0.15));
            	secondHBox.minWidthProperty().bind(topHBox.widthProperty());
            	secondHBox.maxWidthProperty().bind(topHBox.widthProperty());
            	secondHBox.paddingProperty().bind(newScene.heightProperty().multiply(0.03).map(
            		    padding -> new Insets(0, padding.doubleValue(), 0, padding.doubleValue())
            	));
            	secondHBox.spacingProperty().bind(secondHBox.widthProperty().divide(10));
            }
        });
		
		newTaskAnchor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
            	newTaskAnchor.minWidthProperty().bind(newScene.widthProperty().multiply(0.75));
            	newTaskAnchor.maxWidthProperty().bind(newScene.widthProperty().multiply(0.75));
            	newTaskAnchor.minHeightProperty().bind(newScene.heightProperty().multiply(0.83));
            	newTaskAnchor.maxHeightProperty().bind(newScene.heightProperty().multiply(0.83));
            	perimeterVBox.paddingProperty().bind(newTaskAnchor.heightProperty().divide(10).map(padding -> new Insets(padding.doubleValue()*1.5, padding.doubleValue(), padding.doubleValue(), padding.doubleValue())));
            	perimeter.prefHeightProperty().bind(perimeterVBox.heightProperty());
            	perimeter.prefWidthProperty().bind(perimeterVBox.widthProperty());
            	
            	newTaskAnchor.widthProperty().addListener((obs1, oldVal, newVal) -> {
            		newTaskAnchor.setLeftAnchor(perimeter, newVal.doubleValue()/8);
            		newTaskAnchor.setRightAnchor(perimeter, newVal.doubleValue()/8);
                });

            	newTaskAnchor.heightProperty().addListener((obs1, oldVal, newVal) -> {
            		newTaskAnchor.setTopAnchor(perimeter, newVal.doubleValue()/5);
            		newTaskAnchor.setBottomAnchor(perimeter, newVal.doubleValue()/8);
                });
            	
            	layout.spacingProperty().bind(layout.heightProperty().divide(33));
            	titleField.minWidthProperty().bind(layout.widthProperty());
            	titleField.maxWidthProperty().bind(layout.widthProperty());
            	titleField.minHeightProperty().bind(layout.heightProperty().divide(10));
            	titleField.maxHeightProperty().bind(layout.heightProperty().divide(10));         	
            	titleField.fontProperty().bind(
            		Bindings.createObjectBinding(() -> 
            		    Font.font(titleField.getHeight() / 3),
            		    titleField.heightProperty()
            		)
            	);
            	
            	lowerOptions.minWidthProperty().bind(layout.widthProperty());
            	lowerOptions.maxWidthProperty().bind(layout.widthProperty());
            	lowerOptions.prefHeightProperty().bind(layout.heightProperty().multiply(0.9));
            	lowerOptions.spacingProperty().bind(layout.spacingProperty());
            	
            	textArea.minWidthProperty().bind(lowerOptions.widthProperty().multiply(0.6));
            	textArea.maxWidthProperty().bind(lowerOptions.widthProperty().multiply(0.6));
            	textArea.minHeightProperty().bind(lowerOptions.heightProperty());
            	textArea.maxHeightProperty().bind(lowerOptions.heightProperty());
            	textArea.fontProperty().bind(titleField.fontProperty());
            	
            	restOptions.prefWidthProperty().bind(lowerOptions.widthProperty().multiply(0.4));
            	restOptions.minHeightProperty().bind(lowerOptions.heightProperty());
            	restOptions.maxHeightProperty().bind(lowerOptions.heightProperty());
            	restOptions.spacingProperty().bind(layout.spacingProperty());
            	
            	pickCategory.minWidthProperty().bind(restOptions.widthProperty());
            	pickCategory.maxWidthProperty().bind(restOptions.widthProperty());
            	pickCategory.minHeightProperty().bind(titleField.heightProperty());
            	pickCategory.maxHeightProperty().bind(titleField.heightProperty());
            	for (Category category: CategoryList.instance.getCategories()) {
            		pickCategory.getItems().add(category.getTitle());
            	}
            	pickCategory.getItems().add("None");
            	pickCategory.setButtonCell(new ListCell(){

                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); 
                        if(empty || item==null){
                            setStyle("-fx-text-fill: #7d878e");
                        } else {
                            setStyle("-fx-text-fill: #c9d4da");
                            setText(item.toString());
                        }
                    }
                });
            	
            	pickPriority.minWidthProperty().bind(restOptions.widthProperty());
            	pickPriority.maxWidthProperty().bind(restOptions.widthProperty());
            	pickPriority.minHeightProperty().bind(titleField.heightProperty());
            	pickPriority.maxHeightProperty().bind(titleField.heightProperty());
            	for (Priority priority: PriorityList.instance.getSortedList()) {
            		pickPriority.getItems().add(PriorityList.instance.getLevel(priority) + ": " + priority.getTitle());
            	}
            	pickPriority.setButtonCell(new ListCell(){

                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); 
                        if(empty || item==null){
                            setStyle("-fx-text-fill: #7d878e");
                        } else {
                            setStyle("-fx-text-fill: #c9d4da");
                            setText(item.toString());
                        }
                    }
                });
            	
            	pickStatus.minWidthProperty().bind(restOptions.widthProperty());
            	pickStatus.maxWidthProperty().bind(restOptions.widthProperty());
            	pickStatus.minHeightProperty().bind(titleField.heightProperty());
            	pickStatus.maxHeightProperty().bind(titleField.heightProperty());
            	for (Status status: Status.values()) {
            		if (!status.equals(Status.DELAYED)) {
            			pickStatus.getItems().add(status.getTitle());
            		}
            	}
            	pickStatus.setButtonCell(new ListCell(){

                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); 
                        if(empty || item==null){
                            setStyle("-fx-text-fill: #7d878e");
                        } else {
                            setStyle("-fx-text-fill: #c9d4da");
                            setText(item.toString());
                        }
                    }
                });
            	
            	pickDeadline.minWidthProperty().bind(restOptions.widthProperty());
            	pickDeadline.maxWidthProperty().bind(restOptions.widthProperty());
            	pickDeadline.minHeightProperty().bind(titleField.heightProperty());
            	pickDeadline.maxHeightProperty().bind(titleField.heightProperty());
            	
            	pickNotifications.minWidthProperty().bind(restOptions.widthProperty());
            	pickNotifications.maxWidthProperty().bind(restOptions.widthProperty());
            	pickNotificationsVBox.paddingProperty().bind(pickNotifications.widthProperty().divide(40).map(padding -> new Insets(padding.doubleValue(), padding.doubleValue(), padding.doubleValue(), padding.doubleValue())));
            	notificationLabel.fontProperty().bind(titleField.fontProperty());
            	
            	save.minWidthProperty().bind(restOptions.widthProperty());
            	save.maxWidthProperty().bind(restOptions.widthProperty());
            	save.minHeightProperty().bind(titleField.heightProperty());
            	save.maxHeightProperty().bind(titleField.heightProperty());
            	save.fontProperty().bind(titleField.fontProperty());
            }
		});
    }
	
	public void dashboardPressed() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
	        Parent root = loader.load();

	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource(Path.DASHBOARD.getPath()).toExternalForm());

	        // Get current stage safely
	        Stage oldStage = (Stage) taskButton.getScene().getWindow();

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
	
	public void newCategoryPressed() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewCategory.fxml"));
	        Parent root = loader.load();

	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource(Path.NEW_CATEGORY.getPath()).toExternalForm());

	        // Get current stage safely
	        Stage oldStage = (Stage) categoryButton.getScene().getWindow();

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
	
	public void newPriorityPressed() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewPriority.fxml"));
	        Parent root = loader.load();

	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource(Path.NEW_PRIORITY.getPath()).toExternalForm());

	        // Get current stage safely
	        Stage oldStage = (Stage) priorityButton.getScene().getWindow();

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
	
	public void save() throws IOException {
		if (titleField.getText().trim().isEmpty()) {
			displayNewObjectPopUp("You cannot create new a task without a title.");
		} else if (TaskList.instance.get(titleField.getText().trim()) != null) {
			displayNewObjectPopUp("A task with this title already exists.");
		} else if (pickDeadline.getValue() == null) {
			displayNewObjectPopUp("You cannot create new a task without a deadline.");
		} else if (pickDeadline.getValue().isBefore(LocalDate.now())) {
			displayNewObjectPopUp("The deadline you have chosen, has already passed.");
		} else if (OneDayBefore.isSelected() && pickDeadline.getValue().isBefore(LocalDate.now().plusDays(1))) {
			displayNewObjectPopUp("You do not have 2 days to finish the task.");
		} else if (OneWeekBefore.isSelected() && pickDeadline.getValue().isBefore(LocalDate.now().plusWeeks(1))) {
			displayNewObjectPopUp("You do not have 1 week to finish the task.");
		} else if (OneMonthBefore.isSelected() && pickDeadline.getValue().isBefore(LocalDate.now().plusMonths(1))) {
			displayNewObjectPopUp("You do not have 1 month to finish the task.");
		} else {
			String title = titleField.getText().trim();
			String description;
			if (textArea.getText().trim().isEmpty()) {
				description = null;
			} else {
				description = textArea.getText();
			}
			LocalDate deadline = pickDeadline.getValue();
			String category;
			if (pickCategory.getSelectionModel().isEmpty() || pickCategory.getValue().equals("None")) {
				category = null;
			} else {
				category = pickCategory.getValue();
			}
			String priority;
			if (pickPriority.getValue() != null) {
				priority = pickPriority.getValue().substring(pickPriority.getValue().indexOf(": ") + 2);
			} else {
				priority = null;
			}
			Status status;
			if (pickStatus.getSelectionModel().isEmpty()) {
				status = null;
			} else {
				status = Status.get(pickStatus.getValue());
			}
			ArrayList<Notification> notifications = new ArrayList<>();
			if (OneDayBefore.isSelected()) {
				notifications.add(new Notification(NotificationType.ONE_DAY_BEFORE));
			}
			if (OneWeekBefore.isSelected()) {
				notifications.add(new Notification(NotificationType.ONE_WEEK_BEFORE));
			}
			if (OneMonthBefore.isSelected()) {
				notifications.add(new Notification(NotificationType.ONE_MONTH_BEFORE));
			}
			if (DueDay.isSelected()) {
				notifications.add(new Notification(NotificationType.DUE_DAY));
			}
			
			if (status.equals(Status.COMPLETED) && notifications.size() != 0) {
				displayNewObjectPopUp("You cannot add notifications for a completed task.");
			} else {		
				Task.newTask(title, description, deadline, category, priority, status, notifications);
				dashboardPressed();
			}
		}
	}
	
	private void displayNewObjectPopUp(String message) throws IOException {
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
        popupController.setStage(popupStage, message);

        popupStage.showAndWait();
	}
	
	private void initializeLeftButton(Button button, Scene newScene) {
		button.prefWidthProperty().bind(leftButtons.widthProperty());
		button.prefHeightProperty().bind(newScene.heightProperty().multiply(0.06));
		button.setMaxWidth(Double.MAX_VALUE);
		button.setContentDisplay(ContentDisplay.LEFT);
		button.setAlignment(Pos.CENTER_LEFT);
		button.graphicTextGapProperty().bind(button.widthProperty().multiply(0.05));
		button.paddingProperty().bind(leftButtons.prefWidthProperty().divide(8).map(leftPadding -> new Insets(0, 0, 0, leftPadding.doubleValue())));	
		button.fontProperty().bind(leftButtons.widthProperty().multiply(0.08).asObject().map(Font::font));
		if (button.getGraphic() instanceof ImageView) {
	        ImageView imageView = (ImageView) button.getGraphic();
	        imageView.fitWidthProperty().bind(button.widthProperty().multiply(0.08));
	        imageView.fitHeightProperty().bind(imageView.fitWidthProperty());
	    }
	}
	
	public void unfocusSearchBar() {
		if(searchField.isFocused()) {
			searchField.getParent().requestFocus();
		}
	}
	
	public void seeNotifications() {
		newNotif.setVisible(false);
		showNotificationMenu();
		NotificationHistory.instance.seeNotifications();
	}
	
	private void showNotificationMenu() {
		ContextMenu contextMenu = new ContextMenu();
		
		if (NotificationHistory.instance.getUnreadNotifications().size() > 0) {
			ImageView icon = new ImageView();
	        icon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "circle.png").toExternalForm()));
		    icon.setFitWidth(8);
		    icon.setFitHeight(8);
		    
		    Label label = new Label("New Notifications");
		    
		    HBox hbox = new HBox(10, icon, label);
		    hbox.setStyle("-fx-alignment: top-left;");
		    
			CustomMenuItem newNotifications = new CustomMenuItem(hbox, false);
			newNotifications.setId("newNotifLabel");
			
			SeparatorMenuItem separator = new SeparatorMenuItem();
			
			contextMenu.getItems().addAll(newNotifications, separator);
			
			for (Notification notification: NotificationHistory.instance.getUnreadNotifications()) {
				ImageView deleteIcon = new ImageView();
				deleteIcon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "bin.png").toExternalForm()));
				deleteIcon.setFitWidth(16);
				deleteIcon.setFitHeight(16);	
				Button delete = new Button();
				delete.setGraphic(deleteIcon);
				delete.setId("deleteNotif");
			    
			    Label message = new Label(notification.getNotificationMessage());
			    message.setPrefWidth(400);
			    
			    HBox newNotifItem = new HBox(10, message, delete);
			    newNotifItem.setStyle("-fx-alignment: center-left;");
			    
			    CustomMenuItem notif = new CustomMenuItem(newNotifItem);
				notif.setId("newNotifItem");
				notif.setHideOnClick(false);
				
				delete.setOnMouseClicked(event -> {
			        // Remove the notification from history
			        NotificationHistory.instance.removeNotification(notification);
			        
			        // Remove the MenuItem from contextMenu
			        contextMenu.getItems().remove(notif);

			        // Stop the event from propagating to the menu item
			        event.consume();
			    });
				
				notif.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	PauseTransition delay = new PauseTransition(Duration.millis(100)); // Adjust delay time as needed
		                delay.setOnFinished(e -> {
		                    if (NotificationHistory.instance.get(notification) != null) {
		                    	contextMenu.hide();
		                    	modifyTask(notification.getTask(), (Stage) notifButton.getScene().getWindow());
		                    }
		                });
		                delay.play();
		            }
		        });
				
				contextMenu.getItems().add(notif);
			}
		}
		
		if (NotificationHistory.instance.getReadNotifications().size() > 0) {
			CustomMenuItem oldNotifications = new CustomMenuItem(new Label("Older Notifications"), false);
			oldNotifications.setId("oldNotifLabel");
			
			SeparatorMenuItem oldSeparator = new SeparatorMenuItem();
			
			contextMenu.getItems().addAll(oldNotifications, oldSeparator);
			
			for (Notification notification: NotificationHistory.instance.getReadNotifications()) {
				ImageView deleteIcon = new ImageView();
				deleteIcon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "bin.png").toExternalForm()));
				deleteIcon.setFitWidth(16);
				deleteIcon.setFitHeight(16);	
				Button delete = new Button();
				delete.setGraphic(deleteIcon);
				delete.setId("deleteNotif");
			    
			    Label message = new Label(notification.getNotificationMessage());
			    message.setPrefWidth(400);
			    
			    HBox oldNotifItem = new HBox(10, message, delete);
			    oldNotifItem.setStyle("-fx-alignment: center-left;");
			    
				CustomMenuItem notif = new CustomMenuItem(oldNotifItem);
				notif.setId("oldNotifItem");
				notif.setHideOnClick(false);
				
				delete.setOnMouseClicked(event -> {
			        // Remove the notification from history
			        NotificationHistory.instance.removeNotification(notification);
			        
			        // Remove the MenuItem from contextMenu
			        contextMenu.getItems().remove(notif);

			        // Stop the event from propagating to the menu item
			        event.consume();
			    });
				
				notif.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	PauseTransition delay = new PauseTransition(Duration.millis(100)); // Adjust delay time as needed
		                delay.setOnFinished(e -> {
		                    if (NotificationHistory.instance.get(notification) != null) {
		                    	contextMenu.hide();
		                        modifyTask(notification.getTask(), (Stage) notifButton.getScene().getWindow());
		                    }
		                });
		                delay.play();
		            }
		        });
				
				contextMenu.getItems().add(notif);
			}
		}
		
		if (NotificationHistory.instance.getNotifications().size() == 0) {
			ImageView icon = new ImageView();
	        icon.setImage(new Image(getClass().getResource(Path.IMAGES.getPath() + "bad.png").toExternalForm()));
		    icon.setFitWidth(150);
		    icon.setFitHeight(150);
		    
		    Label label = new Label("There are no notifications found.");
		    
		    VBox vbox = new VBox(10, icon, label);
		    vbox.setStyle("-fx-alignment: top-center;");
		    
			CustomMenuItem noNotifications = new CustomMenuItem(vbox, false);
			noNotifications.setId("newNotifLabel");
			
			contextMenu.getItems().add(noNotifications);
		}
		
		double buttonRightX = notifAnchor.localToScreen(notifAnchor.getBoundsInLocal()).getMaxX();
        double buttonBottomY = notifAnchor.localToScreen(notifAnchor.getBoundsInLocal()).getMaxY();
        
        contextMenu.setStyle("-fx-background-color: #151b23");
        contextMenu.show(notifAnchor, buttonRightX, buttonBottomY);
        Platform.runLater(() -> {
            contextMenu.setX(buttonRightX - contextMenu.getWidth()); // Align right edge
        });
	}
	
	public void showAutofill() {
		HashMap<String, Task> suggStrings = new HashMap<>();
		for (Task task: TaskList.instance.getTasks()) {
			suggStrings.put(
				task.getTitle() +
				((task.getCategory() == null) ? "" : task.getCategory().getTitle()) +
				task.getPriority().getTitle(),
				task
			);
		}
		
		ObservableList<String> suggestions = FXCollections.observableArrayList();
		suggestions.addAll(suggStrings.keySet());

	    ContextMenu contextMenu = new ContextMenu();

	    searchField.textProperty().addListener((obs, oldText, newText) -> {
	        if (newText.isEmpty()) {
	            contextMenu.hide();
	            return;
	        }

	        // Filter suggestions based on input (case-insensitive)
	        contextMenu.getItems().clear(); // Clear the previous items

	        suggestions.stream()
	            .filter(item -> item.toLowerCase().contains(newText.toLowerCase())) // Case-insensitive search
	            .forEach(filteredItem -> {
	            	// Get Task from filtered Item
	            	Task task = suggStrings.get(filteredItem);
	            	
	                // Create a VBox to hold the task title and category/priority titles
	                VBox vbox = new VBox();
	                vbox.minWidthProperty().bind(searchField.widthProperty().multiply(0.85));
	                vbox.maxWidthProperty().bind(searchField.widthProperty().multiply(0.85));

	                // Task title in large font
	                Text taskTitle = new Text(task.getTitle()); // Task title
	                taskTitle.setStyle("-fx-font-size: 18px; -fx-fill: #c9d4da;");

	                // Add task title to VBox
	                vbox.getChildren().add(taskTitle);

	                // Get category and priority titles
	                Text secondaryInfo = new Text(
	                	((task.getCategory() == null) ? "" : (task.getCategory().getTitle()) + " | ") +
	                	task.getPriority().getTitle() + " | " +
	    	            "Due: " + task.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
	                );
	            
	                secondaryInfo.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-fill: #c9d4da;");
	                vbox.getChildren().add(secondaryInfo);

	                // Create a MenuItem that holds the VBox
	                MenuItem menuItem = new MenuItem();
	                menuItem.setGraphic(vbox);
	                
	                menuItem.setOnAction(e -> {
	                    searchField.setText(task.getTitle()); // Set the text of search field
	                    contextMenu.hide();
	                    unfocusSearchBar();
	                    modifyTask(task, (Stage) searchField.getScene().getWindow());
	                });

	                contextMenu.getItems().add(menuItem);
	            });

	        // Show context menu if there are matching suggestions
	        if (!contextMenu.getItems().isEmpty()) {
	            contextMenu.show(searchField, searchField.localToScreen(0, searchField.getHeight()).getX(),
	                    searchField.localToScreen(0, searchField.getHeight()).getY());
	        } else {
	            contextMenu.hide();
	        }
	    });
	}
	
	public void modifyTask(Task task, Stage oldStage) {
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
}
