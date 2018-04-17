/**
 * @author SWETR
 * @Version 2.2
 * @since April 2018
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;


public class UML extends Application {
	
	/** startingPointX describes the last stored value pertaining to the X coordinate of the start of a mouse drag gesture */
	double startingPointX;
	/** startingPointY describes the last stored value pertaining to the Y coordinate of the start of a mouse drag gesture */
	double startingPointY;
	/** currentEndingPointX describes the last stored value pertaining to the X coordinate of the end of a mouse drag gesture */
	double currentEndingPointX;
	/** currentEndingPointY describes the last stored value pertaining to the Y coordinate of the end of a mouse drag gesture */
	double currentEndingPointY;

	/** isTextFieldBeingDrawn represents whether or not a textField is the current option that the user wishes to draw */
	boolean isTextFieldBeingDrawn = false;
	
	/** drawingBox is the drawing area for the UML editor. All user created objects are placed inside of this area. */
	public static VBox drawingBox;
	
	/** userClicked describes whether or not an object has been selected to be drawn. Resets to false after every drawn object. */
	static boolean userClicked = false;
		
	/** screenWidth describes the width of the screen that the program is currently being ran in. Used to calculate sizes for the windows. */
	double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
	/** screenHeight describes the height of the screen that the program is currently being ran in. Used to calculate sizes for the windows. */
	double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
	
	TextArea newTextField = null;
	
	static Group group;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/** start Initializes the window of the UML editor and calls {@link createUMLOptions}. 
	 * @param UMLStage The root node of the JavaFX application. Provides 
	 */
	@Override
	public void start(Stage UMLStage) {
		group = new Group();
		UMLStage.setTitle("SWETR UML Diagram Application");
		// image courtesy of google
		// image source: https://thenounproject.com/term/sweater/
		UMLStage.getIcons().add(new Image("swetr_icon.png"));
		
		Scene UMLScene = new Scene(group, screenWidth*.99, screenHeight*.95); // dimensions can be changed
		
		UMLScene.getStylesheets().add("stylesheet.css");

		UMLStage.setScene(UMLScene);
		UMLStage.show();

		createUMLOptions(UMLStage, UMLScene, group);
	}

	
	//createUML options creates all of the objects in an initial window. 
	//Including the top row of buttons, the left-hand side column of buttons, and the drawingBox.
	//UMLStage is the primary stage on which the Scene, UMLScene is displayed.
	//UMLScene is the container for the Group.
	//group contains all of the JavaFX elements such as Buttons and shapes. 
	private void createUMLOptions(Stage UMLStage, Scene UMLScene, Group group) {
		// creates vertical box to make formatting easier
		VBox optionsVBox = new VBox(10);

		// maybe look up if there is an align left function. Would be more
		// understandable
		optionsVBox.setTranslateY(UMLStage.getHeight()*.05); // shift vbox down slightly
		optionsVBox.setTranslateX(UMLStage.getWidth()*.005); // shift vbox over to the left so under top row of buttons
		optionsVBox.setMaxSize(30, 100);
		
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
	
	// Creates the left-hand side buttons that encompass the options that users have to pick from to create.
	//optionsVBox is the container in which all of the buttons are stored.
	//UMLScene is the container for the Group.
	//group contains all of the JavaFX elements such as Buttons and shapes. 
	private void createUMLButtons(VBox optionsVBox, Scene UMLScene, Group group) {
		
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
				Relationship newRelationship = new Relationship(UMLScene, group, option);
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
			new TextBox().drawMe(group);
		});
		
		optionsVBox.getChildren().addAll(classBox, aggregation, composition, generalization, dependency, addText);
	}

	// Creates new, save, exit, help buttons.
	//HBox buttonsHBox is the container for the buttons
	//UMLStage is passed along to createExitWarnings for the exit button 
	//    so that the exit button may properly close the whole application.
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

	// Help screen, informing user how to interact with the application.
	private void createHelpStage() {
		Stage helpStage = new Stage();
		helpStage.setTitle("SWETR's UML Diagram Creation Application");
		helpStage.getIcons().add(new Image("swetr_icon.png"));

		Text welcomeToSWETR = new Text();
		welcomeToSWETR.setText("Welcome to SWETR's UML Diagram Creation Application \n\n");
		welcomeToSWETR.setFont(Font.font(null, FontWeight.BOLD, 30));
		welcomeToSWETR.setTranslateX(0);
		welcomeToSWETR.setTranslateY(-170);

		Text introText = new Text();
		introText.setText("To create a UML diagram, begin by simply clicking on one of the options buttons on the left side.\n\n"
				+ "Class Box\n"
				+ "To draw a class box, select the \"Class Box\" button. A class box will appear on the drawing scene.\n\n"
				+ "Text Box\n"
				+ "To draw a text box, select the \"Add Text\" button. A text box will appear on the drawing scene.\n\n"
				+ "Relationship\n"
				+ "To draw a relationship between two class boxes, select the appropriate button (aggregation, composition, dependency, or composition). "
				+ "Click anywhere on the drawing scene to beging drawing the relationship. While keeping the mouse pressed, continue to drag until "
				+ "desired length is reached. Then release the mouse. \n \n"
				+ "Resize and Move Objects in the Drawing Area\n"
				+ "When you hover your mouse over a class box, text box, or relationship a red box will surround that object with a small "
				+ "green rectange in the lower right corner. To move the object around the drawing scene, click within the red box and drag "
				+ "the to the desired location. To resize the object, click on the small green rectangle and drag to the desired size.");
		introText.setWrappingWidth(600);

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
		root.getChildren().addAll(welcomeToSWETR, introText, continueButton);
		helpStage.setScene(new Scene(root, 900, 600)); // dimensions can be changed
		helpStage.show();
	}

	// creates new diagram template
	private void createNewDiagram() {
		Stage UMLStage = new Stage();
		UMLStage.setTitle("New UML Diagram");
		UMLStage.getIcons().add(new Image("swetr_icon.png"));
		Group group = new Group();
		Scene UMLScene = new Scene(group, screenWidth*.99, screenHeight*.95);
		UMLScene.getStylesheets().add("stylesheet.css");
		UMLStage.setScene(UMLScene); // dimensions can be changed
		UMLStage.show();

		createUMLOptions(UMLStage, UMLScene, group);
	}

	// Creates and presents new window to user to confirm that they want to exit program.
	private void createExitWarning(Stage UMLStage) {
		Stage exitWarningStage = new Stage();
		exitWarningStage.setTitle("Exit Warning!");
		exitWarningStage.getIcons().add(new Image("swetr_icon.png"));
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

	/**Sets the value, {@link userClicked} to passed in boolean value.
	 * @param b Boolean value to set <code>userClicked</code> to.
	 */
	public static void setUserClicked (boolean b) {
		userClicked = b;
	}
	
	
	/**Returns value associated with {@link userClicked}
	 * @return Value of userClicked boolean.
	 */
	public static boolean getUserClicked () {
		return userClicked;
	}
	
	public static Group getGroup() {
		return group;
	}
}