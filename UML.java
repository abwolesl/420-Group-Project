// swetr iteration 2
// UML.java handles all the GUI interactions (button clicks, etc.)

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

public class UML extends Application {

	double startingPointX, startingPointY, currentEndingPointX, currentEndingPointY;

	TextArea newTextField = null;
	boolean isTextFieldBeingDrawn = false;
	public static VBox drawingBox;
	
	static boolean userClicked = false;
		
	public static void main(String[] args) {
		launch(args);
	}
	
	Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	double screenWidth = primaryScreenBounds.getWidth();
	double screenHeight = primaryScreenBounds.getHeight();

	@Override
	public void start(Stage UMLStage) {
		Group group = new Group();
		UMLStage.setTitle("SWETR UML Diagram Application");
		Scene UMLScene = new Scene(group, screenWidth*.99, screenHeight*.95); // dimensions can be changed

		UMLStage.setScene(UMLScene);
		UMLStage.show();

		createUMLOptions(UMLStage, UMLScene, group);
	}

	private void createUMLOptions(Stage UMLStage, Scene UMLScene, Group group) {
		// creates vertical box to make formatting easier
		VBox optionsVBox = new VBox(10);

		// maybe look up if there is an align left function. Would be more
		// understandable
		optionsVBox.setTranslateY(UMLStage.getHeight()*.05); // shift vbox down slightly
		optionsVBox.setTranslateX(UMLStage.getWidth()*.005); // shift vbox over to the left so under top row of buttons
		optionsVBox.setMaxSize(30, 100);
		//optionsVBox.setStyle("-fx-border-color: black");
		HBox buttonsHBox = new HBox(10);
		buttonsHBox.setPadding(new Insets(10)); // sets padding between nodes (so buttons)
		buttonsHBox.setTranslateY(screenHeight*.001);
		buttonsHBox.setTranslateX(screenWidth*.10);

		createUMLButtons(optionsVBox, UMLScene, group);
		createTopButtons(buttonsHBox, UMLStage);

		drawingBox = new VBox();
		// hexadecimal for light gray
		drawingBox.setStyle("-fx-background-color: #D3D3D3;");
		drawingBox.prefWidthProperty().bind(UMLStage.widthProperty().multiply(0.88));
		drawingBox.prefHeightProperty().bind(UMLStage.heightProperty().multiply(0.85));

		drawingBox.setTranslateY(screenHeight*.10);
		drawingBox.setTranslateX(screenWidth*.10);
		drawingBox.autosize();

		//buttonsHBox.getChildren().addAll(drawingBox);
		group.getChildren().addAll(buttonsHBox,optionsVBox,drawingBox);
	}
	
	// These buttons allow user to draw and edit a UML diagram
	private void createUMLButtons(VBox optionsVBox, Scene drawingScene, Group group) {
		
		Button aggregation = new Button("Aggregation");
		Button composition = new Button("Composition");
		Button generalization = new Button("Generalization");
		Button dependency = new Button("Dependency");
		
		Button[] buttons = new Button[4];
		buttons[0] = aggregation;
		buttons[1] = composition;
		buttons[2] = generalization;
		buttons[3] = dependency;
		
		// draw the line depending on the button press
		for(Button b : buttons) {
			b.setOnAction((event) -> {
				setUserClicked(true);
				String option = b.getText();
				Relationship newRelationship = new Relationship(drawingScene, group, option);
				//newRelationship.setScene(drawingScene, group);
				// Relation.getRelationship() returns the relationship
				// then draw it doing
				// group.getChildren().add(relationship);
			});
		}

		// class box
		Button classBox = new Button();
		classBox.setText("Class Box");
		
		// maybe should generalize this more
		optionsVBox.setMinWidth(110);

		// Handle event
		classBox.setOnAction((event) -> {
			new ClassBox().drawMe(group);
		});
		
		// add text or note
		Button addText = new Button("Add Text");

		// Handle event
		addText.setOnAction((event) -> {
			setUserClicked(true);
			drawTextField(drawingScene, group);
		});
		/*
		// press this button before clicking on node so prepared for mouse clicks
		Button edit = new Button("Edit");
		
		// Handle event
		edit.setOnAction((event) -> {
			drawingScene.setOnMousePressed((MouseEvent drawingEvent) -> {
				ArrayList<Node> nodes = new ArrayList<Node>();
				nodes = getAllNodes(group);
				for (Node node : nodes ) {
					node.setOnMouseClicked((eventClicked) -> {
						System.out.println(node);
						// now when you click on something to edit it, it changes to red
						// helpful for user to know what they selected to edit
						//((Shape) node).setStroke(Color.RED);
					});
					
					node.setOnMouseDragged((eventDragged) -> {
						node.setTranslateX(eventDragged.getSceneX());
						node.setTranslateY(eventDragged.getSceneY());
						// once they're done editing, change back to black
						//((Shape) node).setStroke(Color.BLACK);
					});
				}
			});
		});
		
		Button delete = new Button("Delete");
		
		// Handle event
		// need to fix the repeated click thing
		// also needs restrictions on what can and cannot be deleted
		// restrictions will prob be the same as edit
		delete.setOnAction((event) -> {
			
			ArrayList<Node> nodes = new ArrayList<Node>();
			nodes = getAllNodes(group);
			for (Node node : nodes ) {
				node.setOnMouseClicked((eventClicked) -> {
					group.getChildren().remove(node);
				});
			}
		});
		*/
		optionsVBox.getChildren().addAll(classBox, aggregation, composition, generalization, dependency, addText);
	}
	
	// get all nodes of parent (in our case, group)
	// JavaFX is hierarchical, so able to get all children (nodes)
	// of the group
	public static ArrayList<Node> getAllNodes(Parent root) {
	    ArrayList<Node> nodes = new ArrayList<Node>();
	    addAllDescendents(root, nodes);
	    return nodes;
	}

	private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
	    for (Node node : parent.getChildrenUnmodifiable()) {
	        nodes.add(node);
	        if (node instanceof Parent) {
	            addAllDescendents((Parent)node, nodes);
	        }
	    }
	}

	// creates new, save, exit, help buttons
	private void createTopButtons(HBox buttonsHBox, Stage UMLStage) {
		Button newButton = new Button();
		newButton.setText("New");

		Button saveButton = new Button();
		saveButton.setText("Save");

		Button openExistingUMLButton = new Button();
		openExistingUMLButton.setText("Open Existing UML Diagram");

		Button exitButton = new Button();
		exitButton.setText("Exit");

		Button helpButton = new Button();
		helpButton.setText("Help");

		buttonsHBox.getChildren().addAll(newButton, saveButton, openExistingUMLButton, exitButton, helpButton);

		// New Button brings up new page
		newButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				createNewDiagram();
			}
		});

		// Save Button
		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// implement how to save diagram
			}
		});

		// Open Existing UML Button opens diagram saved on user's computer
		openExistingUMLButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// implement how to open existing diagram
			}
		});

		// Exit Button
		exitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				createExitWarning(UMLStage);
			}
		});

		// Help Button brings up help page
		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				createHelpStage();
			}
		});
	}

	// Help screen, informing user how to interact with the application
	private void createHelpStage() {
		Stage helpStage = new Stage();
		helpStage.setTitle("SWETR's UML Diagram Creation Application");

		Text welcomeToSWETR = new Text();
		welcomeToSWETR.setText("Welcome to SWETR's UML Diagram Creation Application \n");
		welcomeToSWETR.setFont(Font.font(null, FontWeight.BOLD, 30));
		welcomeToSWETR.setTranslateX(0);
		welcomeToSWETR.setTranslateY(-130);

		Text welcomeText = new Text();
		welcomeText.setText(
				"To create a UML diagram, simply select from one of the \n many options on the lefthand side of the creation screen then begin to draw. ETC...");

		Button continueButton = new Button();
		continueButton.setText("Exit");
		continueButton.setTranslateX(0); // set button to center on x axis
		continueButton.setTranslateY(200); // move to be at bottom of welcome screen

		continueButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				helpStage.close();
			}
		});

		StackPane root = new StackPane();
		root.getChildren().addAll(welcomeToSWETR, welcomeText, continueButton);
		helpStage.setScene(new Scene(root, 900, 600)); // dimensions can be changed
		helpStage.show();
	}

	// creates new diagram template
	private void createNewDiagram() {
		Stage UMLStage = new Stage();
		UMLStage.setTitle("New UML Diagram");
		Group group = new Group();
		Scene UMLScene = new Scene(group, 1400, 700);
		UMLStage.setScene(UMLScene); // dimensions can be changed
		UMLStage.show();

		createUMLOptions(UMLStage, UMLScene, group);
	}

	private void createExitWarning(Stage UMLStage) {
		Stage exitWarningStage = new Stage();
		StackPane exitRoot = new StackPane();

		exitWarningStage.setScene(new Scene(exitRoot, 400, 300)); // dimensions can be changed
		exitWarningStage.show();

		Text warningMessage = new Text();
		// should eventually change this styling with CSS
		warningMessage.setText(
				"\t \t \t WARNING! \n \n Be sure to save your work before exiting. \n Any unsaved work will be deleted.");

		HBox buttonHBox = new HBox();
		buttonHBox.setSpacing(20);
		// shifts hbox to bottom of message screen
		buttonHBox.setTranslateX(70);
		buttonHBox.setTranslateY(260);

		Button cancelButton = new Button();
		cancelButton.setText("Cancel");

		Button exitButton = new Button();
		exitButton.setText("Exit and Close Application");

		// close exit warning screen, not application
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				exitWarningStage.close();
			}
		});

		// close exit warning screen AND application
		exitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				exitWarningStage.close();
				UMLStage.close();
			}
		});

		// add buttons to hbox
		buttonHBox.getChildren().addAll(cancelButton, exitButton);
		// add hbox and message to the warning screen
		exitRoot.getChildren().addAll(warningMessage, buttonHBox);

		// maybe add save button to message
	}
	
	private void drawTextField(Scene UMLScene, Group group) {

		UMLScene.setOnMousePressed((MouseEvent event) -> {
			if (getUserClicked()) {
				if (isTextFieldBeingDrawn == false) {
	
					newTextField = new TextArea();
					newTextField.isResizable();
					newTextField.setMinSize(150, 100);
					newTextField.setWrapText(true);
					newTextField.setPrefSize(50, 50);
					startingPointX = event.getSceneX();
					startingPointY = event.getSceneY();
	
					if (startingPointX > 160 && startingPointX < 1390 && startingPointY > 61 && startingPointY < 690) {
	
						newTextField.setTranslateX(startingPointX);
						newTextField.setTranslateY(startingPointY);
	
						// Changes background color (light gray) and border (black)
						newTextField.setStyle("-fx-background-color: #D3D3D3; -fx-border-color: #000000;");
	
						group.getChildren().add(newTextField);
	
						isTextFieldBeingDrawn = true;
					}
				}
			}
		});

		UMLScene.setOnMouseReleased((MouseEvent event) -> {
			if (isTextFieldBeingDrawn == true) {
				newTextField = null;
				isTextFieldBeingDrawn = false;
				setUserClicked(false);
			}
		});
	}
	
	public static void setUserClicked (boolean b) {
		userClicked = b;
	}
	
	public static boolean getUserClicked () {
		return userClicked;
	}
}