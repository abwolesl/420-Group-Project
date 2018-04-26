
/**
 * @author SWETR
 * @Version 2.2
 * @since April 2018
 */

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

public class UML extends Application {

	/**
	 * startingPointX describes the last stored value pertaining to the X coordinate
	 * of the start of a mouse drag gesture
	 */
	double startingPointX;
	/**
	 * startingPointY describes the last stored value pertaining to the Y coordinate
	 * of the start of a mouse drag gesture
	 */
	double startingPointY;
	/**
	 * currentEndingPointX describes the last stored value pertaining to the X
	 * coordinate of the end of a mouse drag gesture
	 */
	double currentEndingPointX;
	/**
	 * currentEndingPointY describes the last stored value pertaining to the Y
	 * coordinate of the end of a mouse drag gesture
	 */
	double currentEndingPointY;

	/**
	 * isTextFieldBeingDrawn represents whether or not a textField is the current
	 * option that the user wishes to draw
	 */
	boolean isTextFieldBeingDrawn = false;

	/**
	 * drawingBox is the drawing area for the UML editor. All user created objects
	 * are placed inside of this area.
	 */
	public static VBox drawingBox;

	/**
	 * userClicked describes whether or not an object has been selected to be drawn.
	 * Resets to false after every drawn object.
	 */
	static boolean userClicked = false;

	/**
	 * screenWidth describes the width of the screen that the program is currently
	 * being ran in. Used to calculate sizes for the windows.
	 */
	double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
	/**
	 * screenHeight describes the height of the screen that the program is currently
	 * being ran in. Used to calculate sizes for the windows.
	 */
	double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

	TextArea newTextField = null;

	ArrayList<ClassBox> cBoxArray = new ArrayList<ClassBox>();
	ArrayList<Relationship> relArray = new ArrayList<Relationship>();

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * start Initializes the window of the UML editor and calls
	 * {@link createUMLOptions}.
	 * 
	 * @param UMLStage
	 *            The root node of the JavaFX application. Provides
	 */
	@Override
	public void start(Stage UMLStage) {
		Group group = new Group();
		UMLStage.setTitle("SWETR UML Diagram Application");
		Scene UMLScene = new Scene(group, screenWidth * .99, screenHeight * .95); // dimensions can be changed

		UMLStage.setScene(UMLScene);
		UMLStage.show();

		createUMLOptions(UMLStage, UMLScene, group);
	}

	// createUML options creates all of the objects in an initial window.
	// Including the top row of buttons, the left-hand side column of buttons, and
	// the drawingBox.
	// UMLStage is the primary stage on which the Scene, UMLScene is displayed.
	// UMLScene is the container for the Group.
	// group contains all of the JavaFX elements such as Buttons and shapes.
	private void createUMLOptions(Stage UMLStage, Scene UMLScene, Group group) {
		// creates vertical box to make formatting easier
		VBox optionsVBox = new VBox(10);

		// maybe look up if there is an align left function. Would be more
		// understandable
		optionsVBox.setTranslateY(UMLStage.getHeight() * .05); // shift vbox down slightly
		optionsVBox.setTranslateX(UMLStage.getWidth() * .005); // shift vbox over to the left so under top row of
																// buttons
		optionsVBox.setMaxSize(30, 100);

		HBox buttonsHBox = new HBox(10);
		buttonsHBox.setPadding(new Insets(10)); // sets padding between nodes (so buttons)
		buttonsHBox.setTranslateY(screenHeight * .001);
		buttonsHBox.setTranslateX(screenWidth * .10);

		createUMLButtons(optionsVBox, UMLScene, group);
		createTopButtons(buttonsHBox, UMLStage);

		drawingBox = new VBox();
		// hexadecimal for light gray
		drawingBox.setStyle("-fx-background-color: #D3D3D3;");
		drawingBox.prefWidthProperty().bind(UMLStage.widthProperty().multiply(0.88));
		drawingBox.prefHeightProperty().bind(UMLStage.heightProperty().multiply(0.85));

		drawingBox.setTranslateY(screenHeight * .10);
		drawingBox.setTranslateX(screenWidth * .10);
		drawingBox.autosize();

		// buttonsHBox.getChildren().addAll(drawingBox);
		group.getChildren().addAll(buttonsHBox, optionsVBox, drawingBox);
	}

	// Creates the left-hand side buttons that encompass the options that users have
	// to pick from to create.
	// optionsVBox is the container in which all of the buttons are stored.
	// UMLScene is the container for the Group.
	// group contains all of the JavaFX elements such as Buttons and shapes.
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
		for (Button b : buttons) {
			b.setOnAction((event) -> {
				setUserClicked(true);
				String option = b.getText();
				Relationship newRelationship = new Relationship(UMLScene, group, option);
				relArray.add(newRelationship);

				// newRelationship.setScene(UMLScene, group);
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
			ClassBox cBox = new ClassBox();
			cBoxArray.add(cBox);
			cBox.drawMe(group);
		});

		// add text or note
		Button addText = new Button("Add Text");

		// Handle event
		addText.setOnAction((event) -> {
			setUserClicked(true);
			drawTextField(UMLScene, group);
		});
		/*
		 * // press this button before clicking on node so prepared for mouse clicks
		 * Button edit = new Button("Edit");
		 * 
		 * // Handle event edit.setOnAction((event) -> {
		 * UMLScene.setOnMousePressed((MouseEvent drawingEvent) -> { ArrayList<Node>
		 * nodes = new ArrayList<Node>(); nodes = getAllNodes(group); for (Node node :
		 * nodes ) { node.setOnMouseClicked((eventClicked) -> {
		 * System.out.println(node); // now when you click on something to edit it, it
		 * changes to red // helpful for user to know what they selected to edit
		 * //((Shape) node).setStroke(Color.RED); });
		 * 
		 * node.setOnMouseDragged((eventDragged) -> {
		 * node.setTranslateX(eventDragged.getSceneX());
		 * node.setTranslateY(eventDragged.getSceneY()); // once they're done editing,
		 * change back to black //((Shape) node).setStroke(Color.BLACK); }); } }); });
		 * 
		 * Button delete = new Button("Delete");
		 * 
		 * // Handle event // need to fix the repeated click thing // also needs
		 * restrictions on what can and cannot be deleted // restrictions will prob be
		 * the same as edit delete.setOnAction((event) -> {
		 * 
		 * ArrayList<Node> nodes = new ArrayList<Node>(); nodes = getAllNodes(group);
		 * for (Node node : nodes ) { node.setOnMouseClicked((eventClicked) -> {
		 * group.getChildren().remove(node); }); } });
		 */
		optionsVBox.getChildren().addAll(classBox, aggregation, composition, generalization, dependency, addText);
	}

	// get all nodes of parent (in our case, group)
	// JavaFX is hierarchical, so able to get all children (nodes)
	// of the group
	private static ArrayList<Node> getAllNodes(Parent root) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		addAllDescendents(root, nodes);
		return nodes;
	}

	// Effectively adds all notes of a root node to nodes ArrayList.
	// Adds all children of specific parent to nodes ArrayList.
	// Recursively calls self in case that a child is also a parent.
	// parent is the root node.
	// nodes is the ArrayList of nodes that houses all child nodes.
	private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
		for (Node node : parent.getChildrenUnmodifiable()) {
			nodes.add(node);
			if (node instanceof Parent) {
				addAllDescendents((Parent) node, nodes);
			}
		}
	}

	// Creates new, save, exit, help buttons.
	// HBox buttonsHBox is the container for the buttons
	// UMLStage is passed along to createExitWarnings for the exit button
	// so that the exit button may properly close the whole application.
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
				FileChooser fileChooser = new FileChooser();

				// Set extension filter
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(UMLStage);

				if (file != null) {
					String addToFile = "";

					for (int i = 0; i < cBoxArray.size(); i++) {
						addToFile += cBoxArray.get(i).whereAmI();
					}

					for (int i = 0; i < relArray.size(); i++) {
						addToFile += relArray.get(i).whereAmI();
					}

					SaveFile(addToFile, file);
				}
			}
		});

		// Open Existing UML Button opens diagram saved on user's computer
		openExistingUMLButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Text");

				// Set extension filter
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showOpenDialog(UMLStage);
				if (file != null) {
					try {

						Stage UMLStage = new Stage();
						UMLStage.setTitle("New UML Diagram");
						Group group = new Group();
						Scene UMLScene = new Scene(group, 1400, 700);
						UMLStage.setScene(UMLScene); // dimensions can be changed
						UMLStage.show();

						createUMLOptions(UMLStage, UMLScene, group);

						readFile(file,group);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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

	// Creates and presents new window to user to confirm that they want to exit
	// program.
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

	// Creates and draws a TextArea for the user.
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
				// newTextField = null;
				isTextFieldBeingDrawn = false;
				setUserClicked(false);
			}
		});
	}

	/**
	 * Sets the value, {@link userClicked} to passed in boolean value.
	 * 
	 * @param b
	 *            Boolean value to set <code>userClicked</code> to.
	 */
	public static void setUserClicked(boolean b) {
		userClicked = b;
	}

	/**
	 * Returns value associated with {@link userClicked}
	 * 
	 * @return Value of userClicked boolean.
	 */
	public static boolean getUserClicked() {
		return userClicked;
	}

	private void SaveFile(String content, File file) {
		try {
			FileWriter fileWriter = null;

			fileWriter = new FileWriter(file);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException ex) {
			Logger.getLogger(UML.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private static void readFile(File file,Group group) throws IOException {
		FileReader in = new FileReader(file);
		BufferedReader br = new BufferedReader(in);
		String line;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("~~~~");

			for (int i = 0; i < parts.length; i++) {
				readString(parts[i],group);
			}
		}

	}

	private static void readString(String action,Group group) {

		String[] parts = action.split("/");
		System.out.println(parts[0]);
		
		if (parts[0].equals("CLASSBOX")) {

			double startX = Double.parseDouble(parts[1]);
			double startY = Double.parseDouble(parts[2]);
			double width = Double.parseDouble(parts[3]);
			double height = Double.parseDouble(parts[4]);
			String tTop = parts[5];
			String tMid = parts[6];
			String tBot = parts[7];
			ClassBox cBox = new ClassBox(startX, startY, width, height, tTop, tMid, tBot);
			cBox.drawMe(group);

		}else if (parts[0].equals("Relationship")) {
			
			String relType = parts[1];
			double startX = Double.parseDouble(parts[2]); 
			double startY =Double.parseDouble(parts[3]);
			double endX = Double.parseDouble(parts[4]);
			double endY = Double.parseDouble(parts[5]);
			
			Relationship rel = new Relationship(group, relType, startX, startY, endX,endY);
			group.getChildren().add(rel);

	}

}
}