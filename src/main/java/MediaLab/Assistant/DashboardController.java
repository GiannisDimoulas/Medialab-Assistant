package MediaLab.Assistant;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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

public class DashboardController {

	// FXML Components
	@FXML
	private VBox leftVBox, sizedBox, leftButtons, titleVBox, totalVBox, completedVBox, delayedVBox,
		inAWeekVBox, tasksList, categoriesList, prioritiesList;
	@FXML
	private HBox topHBox, firstHBox, secondHBox, optionsHBox;
	@FXML
	private AnchorPane searchBar, tabPaneAnchor, scrollAnchor, notifAnchor,
		catScrollAnchor, priScrollAnchor;
	@FXML
	private ImageView logoImage, searchIcon, notifButton, newNotif;
	@FXML
	private Label logoLabel, pageTitleLabel, localdateLabel;
	@FXML
	private Button dashboardButton, taskButton, categoryButton, priorityButton;
	@FXML
	private TextField searchField;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab Tasks, Categories, Priorities;
	
	private static ArrayList<PriorityCardController> cardsList = new ArrayList<>();
	
	
	private static boolean isPopupShown = false;
	
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
            	secondHBox.prefHeightProperty().bind(topHBox.heightProperty().multiply(0.75));
            	secondHBox.minWidthProperty().bind(topHBox.widthProperty());
            	secondHBox.maxWidthProperty().bind(topHBox.widthProperty());
            	secondHBox.paddingProperty().bind(newScene.heightProperty().multiply(0.03).map(
            		    padding -> new Insets(0, padding.doubleValue(), 0, padding.doubleValue())
            	));
            	secondHBox.spacingProperty().bind(secondHBox.widthProperty().divide(10));
            	
            	// FABs adjusting
            	initializeFAB(totalVBox, TaskList.instance.getTasks().size());
            	initializeFAB(completedVBox, TaskList.instance.getStatusList(Status.COMPLETED).size());
            	initializeFAB(delayedVBox, TaskList.instance.getStatusList(Status.DELAYED).size());
            	initializeFAB(inAWeekVBox, TaskList.instance.getUpcomingList().size());
            }
        });
		
		tabPaneAnchor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
            	tabPaneAnchor.minWidthProperty().bind(newScene.widthProperty().multiply(0.75));
            	tabPaneAnchor.maxWidthProperty().bind(newScene.widthProperty().multiply(0.75));
            	tabPaneAnchor.minHeightProperty().bind(newScene.heightProperty().multiply(0.65));
            	tabPaneAnchor.maxHeightProperty().bind(newScene.heightProperty().multiply(0.65));
            	scrollAnchor.minWidthProperty().bind(tabPane.widthProperty().multiply(0.98));
            	scrollAnchor.maxWidthProperty().bind(tabPane.widthProperty().multiply(0.98));
            	tasksList.paddingProperty().bind(scrollAnchor.widthProperty().divide(50).map(padding -> new Insets(padding.doubleValue(), padding.doubleValue(), padding.doubleValue(), padding.doubleValue())));
            	tasksList.spacingProperty().bind(scrollAnchor.widthProperty().divide(50));
            	
            	catScrollAnchor.minWidthProperty().bind(tabPane.widthProperty().multiply(0.98));
            	catScrollAnchor.maxWidthProperty().bind(tabPane.widthProperty().multiply(0.98));
            	categoriesList.paddingProperty().bind(catScrollAnchor.widthProperty().divide(50).map(padding -> new Insets(padding.doubleValue(), padding.doubleValue(), padding.doubleValue(), padding.doubleValue())));
            	categoriesList.spacingProperty().bind(catScrollAnchor.widthProperty().divide(50)); 
            	
            	priScrollAnchor.minWidthProperty().bind(tabPane.widthProperty().multiply(0.98));
            	priScrollAnchor.maxWidthProperty().bind(tabPane.widthProperty().multiply(0.98));
            	prioritiesList.paddingProperty().bind(priScrollAnchor.widthProperty().divide(50).map(padding -> new Insets(padding.doubleValue(), padding.doubleValue(), padding.doubleValue(), padding.doubleValue())));
            	prioritiesList.spacingProperty().bind(priScrollAnchor.widthProperty().divide(50)); 

            	rebuildTabPane(TaskList.instance.getTasks(), CategoryList.instance.getCategories(), PriorityList.instance.getSortedList());
            }
		});
		
		if (!isPopupShown && TaskList.instance.getStatusList(Status.DELAYED).size() > 0) {
			Platform.runLater(() -> {
		        try {
		        	isPopupShown = true;
		            showDelayedPopup();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    });
		}
    }
	
	public void newTaskPressed() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewTask.fxml"));
	        Parent root = loader.load();

	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource(Path.NEW_TASK.getPath()).toExternalForm());

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

	
	public void inTotalFABPressed() {
		rebuildTabPane(TaskList.instance.getTasks(), CategoryList.instance.getCategories(), PriorityList.instance.getSortedList());
	    tabPane.getSelectionModel().select(Tasks);
	}
	
	public void completedFABPressed() {
		rebuildTabPane(TaskList.instance.getStatusList(Status.COMPLETED), CategoryList.instance.getCategories(), PriorityList.instance.getSortedList());
	    tabPane.getSelectionModel().select(Tasks);
	}
	
	public void delayedFABPressed() {
		rebuildTabPane(TaskList.instance.getStatusList(Status.DELAYED), CategoryList.instance.getCategories(), PriorityList.instance.getSortedList());
	    tabPane.getSelectionModel().select(Tasks);
	}
	
	public void inAWeekFABPressed() {
		rebuildTabPane(TaskList.instance.getUpcomingList(), CategoryList.instance.getCategories(), PriorityList.instance.getSortedList());
	    tabPane.getSelectionModel().select(Tasks);
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
	
	private void initializeFAB(VBox vbox, int number) {
		vbox.minWidthProperty().bind(secondHBox.widthProperty().divide(10));
		vbox.maxWidthProperty().bind(secondHBox.widthProperty().divide(10));
		vbox.minHeightProperty().bind(secondHBox.heightProperty().multiply(0.85));
		vbox.maxHeightProperty().bind(secondHBox.heightProperty().multiply(0.85));
		vbox.paddingProperty().bind(vbox.widthProperty().divide(8).map(leftPadding -> new Insets(0, 0, 0, leftPadding.doubleValue())));
		
		ImageView icon = (ImageView) vbox.getChildren().get(0);
		VBox sizedBox = (VBox) vbox.getChildren().get(1);
		Label title = (Label) vbox.getChildren().get(2);
		Label tasks = (Label) vbox.getChildren().get(3);
		
		icon.fitHeightProperty().bind(vbox.heightProperty().divide(4));
		icon.fitWidthProperty().bind(icon.fitHeightProperty());
		
		title.fontProperty().bind(vbox.widthProperty().multiply(0.15).asObject().map(Font::font));
		
		tasks.setText(number + " tasks.");
		tasks.fontProperty().bind(vbox.widthProperty().multiply(0.12).asObject().map(Font::font));
		
		sizedBox.minHeightProperty().bind(vbox.heightProperty().divide(8));
		sizedBox.maxHeightProperty().bind(vbox.heightProperty().divide(8));
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
	
	public void rebuildTabPane(ArrayList<Task> Tasks, ArrayList<Category> Categories, ArrayList<Priority> Priorities) {
		displayTasks(Tasks);
		displayCategories(Categories);
		displayPriorities(Priorities);
	}
	
	private void displayTasks(ArrayList<Task> Tasks) {
		//Stage currentStage = (Stage) tasksList.getScene().getWindow();
		tasksList.getChildren().clear();
		
		if (Tasks.size() == 0) {
			Label noTaskLabel = new Label("No task found.");
			tasksList.setAlignment(Pos.TOP_CENTER);
			noTaskLabel.setStyle("-fx-text-fill: #151b23;");
			noTaskLabel.fontProperty().bind(tasksList.widthProperty().multiply(0.015).asObject().map(Font::font));
			tasksList.getChildren().add(noTaskLabel);
		} else {
			for (Task task: Tasks) {
				try {
	        		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("TaskCard.fxml"));
	                Parent card = fxmlLoader.load();
	        		card.getStylesheets().add(getClass().getResource(Path.TASK_CARD.getPath()).toExternalForm());
	        		TaskCardController cardController = fxmlLoader.getController();
	        		cardController.assignTask(task);
	        		cardController.setParentController(this);
	        		//cardController.setStage(currentStage);
	        		
	        		cardController.bind(tasksList.widthProperty().multiply(1).subtract(scrollAnchor.widthProperty().divide(25)));
	        		
					tasksList.getChildren().add(card);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void displayCategories(ArrayList<Category> Categories) {
		//Stage currentStage = (Stage) tasksList.getScene().getWindow();
		categoriesList.getChildren().clear();
		
		if (Categories.size() == 0) {
			Label noTaskLabel = new Label("No category found.");
			categoriesList.setAlignment(Pos.TOP_CENTER);
			noTaskLabel.setStyle("-fx-text-fill: #151b23;");
			noTaskLabel.fontProperty().bind(tasksList.widthProperty().multiply(0.015).asObject().map(Font::font));
			categoriesList.getChildren().add(noTaskLabel);
		} else {
			HBox hbox = null; 
			int count = 0; // Counter to track how many cards are added to the current HBox

			for (Category task : Categories) {
			    if (count % 2 == 0) { // Create a new HBox for every two cards
			        hbox = new HBox();
			        hbox.spacingProperty().bind(categoriesList.spacingProperty()); // Optional: Adjust spacing between cards
			    }
			    
			    try {
			        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("CategoryCard.fxml"));
			        Parent card = fxmlLoader.load();
			        card.getStylesheets().add(getClass().getResource(Path.CATEGORY_CARD.getPath()).toExternalForm());
			        
			        CategoryCardController cardController = fxmlLoader.getController();
			        cardController.assignCategory(task);
			        cardController.setParentController(this);
			        
			        cardController.bind(categoriesList.widthProperty().multiply(0.5).subtract(catScrollAnchor.widthProperty().divide(25)));

			        hbox.getChildren().add(card); // Add the card to the current HBox

			        if (count % 2 == 1 || task == Categories.get(Categories.size() - 1)) {
			            categoriesList.getChildren().add(hbox); // Add HBox after two cards or if it's the last element
			        }

			        count++;
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}

		}
	}
	
	private void displayPriorities(ArrayList<Priority> Priorities) {
		prioritiesList.getChildren().clear();
		cardsList.clear();
		
		if (Priorities.size() == 0) {
			Label noTaskLabel = new Label("No priority found.");
			prioritiesList.setAlignment(Pos.TOP_CENTER);
			noTaskLabel.setStyle("-fx-text-fill: #151b23;");
			noTaskLabel.fontProperty().bind(prioritiesList.widthProperty().multiply(0.015).asObject().map(Font::font));
			prioritiesList.getChildren().add(noTaskLabel);
		} else {
			for (Priority priority: Priorities) {
				try {
	        		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("PriorityCard.fxml"));
	                Parent card = fxmlLoader.load();
	        		card.getStylesheets().add(getClass().getResource(Path.PRIORITY_CARD.getPath()).toExternalForm());
	        		PriorityCardController cardController = fxmlLoader.getController();
	        		cardController.assignPriority(priority);
	        		cardController.setParentController(this);
	        		
	        		cardController.bind(tasksList.widthProperty().multiply(1).subtract(scrollAnchor.widthProperty().divide(25)));
	        		
					prioritiesList.getChildren().add(card);
					cardsList.add(cardController);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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

	
	public void refreshUI() {
	    Scene scene = leftVBox.getScene();  // Get the current scene
	    if (scene != null) {
	        leftVBox.prefWidthProperty().bind(scene.widthProperty().multiply(0.25));
	        leftVBox.paddingProperty().bind(scene.heightProperty().multiply(0.05).map(topPadding -> new Insets(topPadding.doubleValue(), 0, 0, 0)));
	        
	        logoImage.fitWidthProperty().bind(leftVBox.widthProperty().multiply(0.4));
	        logoLabel.fontProperty().bind(leftVBox.widthProperty().multiply(0.1).asObject().map(Font::font));
	        
	        sizedBox.prefHeightProperty().bind(scene.heightProperty().multiply(0.1));

	        // Reapply button settings
	        leftButtons.prefWidthProperty().bind(leftVBox.widthProperty().multiply(0.8));
	        leftButtons.maxWidthProperty().bind(leftVBox.widthProperty().multiply(0.8));
	        leftButtons.spacingProperty().bind(scene.widthProperty().multiply(0.03));

	        initializeLeftButton(dashboardButton, scene);
            initializeLeftButton(taskButton, scene);
            initializeLeftButton(categoryButton, scene);
            initializeLeftButton(priorityButton, scene);
	        
	        // New notifications
	        if (NotificationHistory.instance.getUnreadNotifications().size() > 0) {
        		newNotif.setVisible(true);
        	} else {
        		newNotif.setVisible(false);
        	}
	        
	        // Reapply FABs
	        initializeFAB(totalVBox, TaskList.instance.getTasks().size());
	        initializeFAB(completedVBox, TaskList.instance.getStatusList(Status.COMPLETED).size());
	        initializeFAB(delayedVBox, TaskList.instance.getStatusList(Status.DELAYED).size());
	        initializeFAB(inAWeekVBox, TaskList.instance.getUpcomingList().size());
	        
	        rebuildTabPane(TaskList.instance.getTasks(), CategoryList.instance.getCategories(), PriorityList.instance.getSortedList());
	    }
	}
	
	public void showDelayedPopup() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DelayedPopUp.fxml"));
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

        DelayedPopUpController popupController = loader.getController();
        popupController.setStage(popupStage);

        popupStage.showAndWait();
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
	
	public static void handlePriorityChange(DashboardController parent, PriorityCardController changedCard, int oldValue, int newValue) {
		PriorityCardController conflictingCard = null;
		for (PriorityCardController controller: cardsList) {
			if (PriorityList.instance.getLevel(controller.getPriority()) == newValue) {
				conflictingCard = controller;
			}
		}

		if (conflictingCard != null) {
            // Swap the priority levels
        	if (oldValue < newValue) {
            	PriorityList.instance.increaseLevel(changedCard.getPriority(), newValue);
            } else {
            	PriorityList.instance.decreaseLevel(changedCard.getPriority(), newValue);
            }
        	
        	animateCardSwap(parent, changedCard, conflictingCard);
        }
	}
	
	private static void animateCardSwap(DashboardController parent, PriorityCardController card1, PriorityCardController card2) {
	    // Get their current positions
	    double card1Y = card1.getRoot().getLayoutY();
	    double card2Y = card2.getRoot().getLayoutY();

	    // Create animations
	    TranslateTransition moveCard1 = new TranslateTransition(Duration.millis(300), card1.getRoot());
	    moveCard1.setByY(card2Y - card1Y);

	    TranslateTransition moveCard2 = new TranslateTransition(Duration.millis(300), card2.getRoot());
	    moveCard2.setByY(card1Y - card2Y);

	    // Play animations together
	    ParallelTransition parallelTransition = new ParallelTransition(moveCard1, moveCard2);
	    parallelTransition.setOnFinished(event -> {
	        // Reset positions to avoid permanent offset
	        card1.getRoot().setTranslateY(0);
	        card2.getRoot().setTranslateY(0);

	        // Swap cards in the list
	        int index1 = cardsList.indexOf(card1);
	        int index2 = cardsList.indexOf(card2);
	        if (index1 == -1 || index2 == -1) {
	            return;
	        }
	        Collections.swap(cardsList, index1, index2);

	        // Refresh the UI layout if necessary
	        parent.rebuildTabPane(TaskList.instance.getTasks(), CategoryList.instance.getCategories(), PriorityList.instance.getSortedList());
	    });

	    parallelTransition.play();
	}
}
