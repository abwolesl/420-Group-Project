/**
 * @author SWETR
 * @Version 3
 * @since April 2018
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	
	public static VBox infoVBox;
	
	/** userClicked describes whether or not an object has been selected to be drawn. Resets to false after every drawn object. */
	static boolean userClicked = false;
		
	/** screenWidth describes the width of the screen that the program is currently being ran in. Used to calculate sizes for the windows. */
	double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
	/** screenHeight describes the height of the screen that the program is currently being ran in. Used to calculate sizes for the windows. */
	double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
	
	TextArea newTextField = null;
	
	GridPane grid;
	
	public static Pane pane;
	
	public static Stage UMLStage;
	
	static ArrayList<ClassBox> cBoxArray = new ArrayList<ClassBox>();
	ArrayList<Relationship> relArray = new ArrayList<Relationship>();
	
	public static boolean hideClassBox;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/** start Initializes the window of the UML editor and calls {@link createUMLOptions}. 
	 * @param UMLStage The root node of the JavaFX application. Provides 
	 */
	@Override
	public void start(Stage UnusedStage) {
		Group group = new Group();
		UMLStage = new Stage();
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
		
		pane = new Pane();
		
		drawingBox = new VBox();
		// hexadecimal for light gray
		drawingBox.setStyle("-fx-background-color: #D3D3D3;");
		drawingBox.prefWidthProperty().bind(UMLStage.widthProperty().multiply(0.915));
		drawingBox.prefHeightProperty().bind(UMLStage.heightProperty().multiply(0.923));
		//drawingBox.prefWidth(10000);
		//drawingBox.prefHeight(10000);

		drawingBox.setTranslateY(screenHeight*.04);
		drawingBox.setTranslateX(screenWidth*.08);
		drawingBox.autosize();
		
		grid = createGrid();
		grid.setGridLinesVisible(true);
		grid.setStyle("-fx-background-color: #D3D3D3;");
		grid.setOpacity(.5);
		updateGrid();
		
		// creates vertical box to make formatting easier
		VBox optionsVBox = new VBox(10);

		// maybe look up if there is an align left function. Would be more
		// understandable
		optionsVBox.setTranslateY(drawingBox.getTranslateY()); // shift vbox down slightly
		optionsVBox.setTranslateX(UMLStage.getWidth()*.0005); // shift vbox over to the left so under top row of buttons
		optionsVBox.setMaxSize(30, 100);
		optionsVBox.setPadding(new Insets(0));
		
		infoVBox = new VBox(10);
		infoVBox.setTranslateY(drawingBox.getTranslateY() + 300); // shift vbox down slightly
		infoVBox.setTranslateX(UMLStage.getWidth()*.0005); // shift vbox over to the left so under top row of buttons
		optionsVBox.setMaxSize(30, 100);
		
		HBox buttonsHBox = new HBox(10);
		buttonsHBox.setTranslateY(screenHeight*.001);
		buttonsHBox.setTranslateX(drawingBox.getTranslateX());

		createUMLButtons(optionsVBox, pane, group);
		createTopButtons(buttonsHBox, UMLStage);
		createInfoPane(infoVBox, UMLScene, group);
	
		pane.getChildren().addAll(buttonsHBox, optionsVBox, drawingBox, infoVBox);
		
		/**
		 * We attempted to make the drawing area scrollable so
		 * the user could draw larger UML diagrams. However, we
		 * ran out of time for this iteration, but still felt it
		 * was valuable to include what we were able to accomplish.
		 * This version does not use the scroll bar, but it could 
		 * if you would like to test it. Below, in the line that reads
		 * group.getChildren().add(pane); change it to 
		 * group.getChildren().add(s1);
		 * Then, in ClassBox.java, TextBox.java, and Relationship.java,
		 * there are lines that read:
		 * Stage UMLStage = UML.getStage();
		 * UMLStage.setResizable(true);
		 * UMLStage.setHeight(UMLStage.getHeight() + 100);
		 * Those three lines are in the checkBoundsY method of each class.
		 * Uncomment those and the scroll should display. When you drag any
		 * of the models to the bottom of the screen, the screen should expand.
		 */
		 ScrollPane s1 = new ScrollPane();
		 s1.setMinWidth(UMLStage.getWidth());
		 s1.setMinHeight(UMLStage.getHeight() * 0.97);
		 s1.setMaxSize(UMLStage.getWidth(), UMLStage.getHeight());
		 
		 s1.setPannable(true);
		 
		 s1.setContent(pane);
		 // Only allow vertical scrolling
		 s1.setHbarPolicy(ScrollBarPolicy.NEVER);
		 s1.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		 
		 // to test how we far we were able to get with implementing a
		 // scroll bar to the, change the pane in add's parameters
		 // to be the scrollbar (s1)
		 group.getChildren().add(pane);
	}
	
	private void updateGrid () {
		grid.setPrefWidth(drawingBox.getWidth());
		grid.setPrefHeight(screenHeight + 100);
	}
	
	private void createInfoPane(VBox infoVBox, Scene UMLScene, Group group) {

		infoVBox.setMinWidth(110);

		Label startXLabel = new Label("Start X");
		Label startYLabel = new Label("Start Y");
		Label endXLabel = new Label("End X");
		Label endYLabel = new Label("End Y");

		TextField startX = new TextField();
		TextField startY = new TextField();
		TextField endX = new TextField();
		TextField endY = new TextField();
		
		startX.setPrefWidth(110);
		startY.setPrefWidth(110);
		endX.setPrefWidth(110);
		endY.setPrefWidth(110);

		infoVBox.getChildren().addAll(startXLabel, startX, startYLabel, startY, endXLabel, endX, endYLabel, endY);
	}

	//Sets each textfield of the info pane to coords of a line passed in from Relationship.java
	public static void setInfoPane(double lineStartX, double lineStartY, double lineEndX, double lineEndY) {
		TextField lsxNode = (TextField) infoVBox.getChildren().get(1);
		lsxNode.setText(Double.toString(lineStartX));
		
		TextField lsyNode = (TextField) infoVBox.getChildren().get(3);
		lsyNode.setText(Double.toString(lineStartY));
		
		TextField lexNode = (TextField) infoVBox.getChildren().get(5);
		lexNode.setText(Double.toString(lineEndX));
		
		TextField leyNode = (TextField) infoVBox.getChildren().get(7);
		leyNode.setText(Double.toString(lineEndY));
	}
	
	// Creates the left-hand side buttons that encompass the options that users have to pick from to create.
	//optionsVBox is the container in which all of the buttons are stored.
	//UMLScene is the container for the Group.
	//group contains all of the JavaFX elements such as Buttons and shapes. 
	private void createUMLButtons(VBox optionsVBox, Pane pane, Group group) {
		
		Button aggregation = new Button("Aggregation");
		Button composition = new Button("Composition");
		Button generalization = new Button("Generalization");
		Button dependency = new Button("Dependency");
		
		aggregation.setPrefWidth(110);
		composition.setPrefWidth(110);
		generalization.setPrefWidth(110);
		dependency.setPrefWidth(110);
		
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
				Relationship newRelationship = new Relationship(pane, option);
			});
		}

		// class box
		Button classBox = new Button();
		classBox.setText("Class Box");
		classBox.setPrefWidth(110);
		
		// maybe should generalize this more
		optionsVBox.setMinWidth(110);

		// Handle event
		classBox.setOnAction((event) -> {
			new ClassBox().drawMe(pane);
		});
		
		// add text or note
		Button addText = new Button("Add Text");
		addText.setPrefWidth(110);

		// Handle event
		addText.setOnAction((event) -> {
			setUserClicked(true);
			new TextBox().drawMe(pane);
		});
		
		Button clearAll = new Button("Clear All");
		clearAll.setPrefWidth(110);
		
		// Handle event
		clearAll.setOnAction((event) -> {
			setUserClicked(true);
			createClearWarning();
		});
		
		Button showGridButton = new Button("Show Grid");
		showGridButton.setPrefWidth(110);
		
		Button removeGridButton = new Button("Remove Grid");
		removeGridButton.setPrefWidth(110);
		
		// Handle event
		showGridButton.setOnAction((event) -> {
			setUserClicked(true);
			// show grid
			drawingBox.getChildren().add(grid);
			optionsVBox.getChildren().remove(showGridButton);
			optionsVBox.getChildren().add(removeGridButton);
		});
		
		removeGridButton.setOnAction((event) -> {
			setUserClicked(true);
			// remove grid
			drawingBox.getChildren().remove(grid);
			optionsVBox.getChildren().remove(removeGridButton);
			optionsVBox.getChildren().add(showGridButton);
		});
		
		optionsVBox.getChildren().addAll(classBox, aggregation, composition, generalization, dependency, addText, clearAll, showGridButton);
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
		openExistingUMLButton.setText("Open");

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
						UMLScene.getStylesheets().add("stylesheet.css");
						UMLStage.setScene(UMLScene);
						UMLStage.show();

						createUMLOptions(UMLStage, UMLScene, group);
						
						readFile(file,pane);
					} catch (IOException e) {
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
		buttonHBox.setAlignment(Pos.BOTTOM_CENTER);
		buttonHBox.setPadding(new Insets(5));

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
	
	private void createClearWarning() {
		Stage exitWarningStage = new Stage();
		exitWarningStage.setTitle("Clear All Warning!");
		exitWarningStage.getIcons().add(new Image("swetr_icon.png"));
		StackPane exitRoot = new StackPane();

		exitWarningStage.setScene(new Scene(exitRoot, 400, 300)); // dimensions can be changed
		exitWarningStage.show();

		Text warningMessage = new Text();
		// should eventually change this styling with CSS
		warningMessage.setText(
				"\t \t \t WARNING! \n \n You are about to clear the UML diagram! \n Are you sure you want to clear all?");

		HBox buttonHBox = new HBox();
		buttonHBox.setSpacing(20);
		// shifts hbox to bottom of message screen
		buttonHBox.setAlignment(Pos.BOTTOM_CENTER);
		buttonHBox.setPadding(new Insets(5));

		Button cancelButton = new Button();
		cancelButton.setText("Cancel");

		Button yesButton = new Button();
		yesButton.setText("Yes");

		// close exit warning screen, not application
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				exitWarningStage.close();
			}
		});

		// close exit warning screen AND application
		yesButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				pane.getChildren().removeIf(n -> n instanceof Rectangle);
				pane.getChildren().removeIf(n -> n instanceof TextArea);
				pane.getChildren().removeIf(n -> n instanceof Line);
				pane.getChildren().removeIf(n -> n instanceof Polygon);
				pane.getChildren().removeIf(n -> n instanceof Polyline);
				exitWarningStage.close();
			}
		});

		// add buttons to hbox
		buttonHBox.getChildren().addAll(cancelButton, yesButton);
		// add hbox and message to the warning screen
		exitRoot.getChildren().addAll(warningMessage, buttonHBox);
	}
	
	/**
	 * @return 
	 */
    private StackPane createCell(BooleanProperty cellSwitch) {

        StackPane cell = new StackPane();
        return cell;
    }

	/**
	 * @return 
	 * Citation: Code to create grid was modified from a stack overflow post
	 * URL: https://stackoverflow.com/questions/37619867/how-to-make-gridpanes-lines-visible
	 */
    private GridPane createGrid() {
    	
        int numCols = 40 ;
        int numRows = 30 ;

        BooleanProperty[][] switches = new BooleanProperty[numCols][numRows];
        for (int x = 0 ; x < numCols ; x++) {
            for (int y = 0 ; y < numRows ; y++) {
                switches[x][y] = new SimpleBooleanProperty();
            }
        }

        GridPane grid = new GridPane();

        for (int x = 0 ; x < numCols ; x++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }

        for (int y = 0 ; y < numRows ; y++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rc);
        }

        for (int x = 0 ; x < numCols ; x++) {
            for (int y = 0 ; y < numRows ; y++) {
                grid.add(createCell(switches[x][y]), x, y);
            }
        }
        
        return grid;
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
	
	public static Pane getPane() {
		return pane;
	}
	
	public static Stage getStage () {
		return UMLStage;
	}
	
	//Opens save dialog and ???
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

		//Reads in save file and breaks into objects
		private static void readFile(File file, Pane pane) throws IOException {
			FileReader in = new FileReader(file);
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("~~~~");
				for (int i = 0; i < parts.length; i++) {
					readString(parts[i], pane);
				}
			}
			br.close();
		}

		//Takes object string and parses details
		//Creates object 
		private static void readString(String action, Pane pane) {

			String[] parts = action.split("/");

			

			 if (parts[0].equals("Relationship")) {

				String relType = parts[1];
				double startX = Double.parseDouble(parts[2]);
				double startY = Double.parseDouble(parts[3]);
				double endX = Double.parseDouble(parts[4]);
				double endY = Double.parseDouble(parts[5]);

				new Relationship(pane, relType, startX, startY, endX, endY);

			}
			 else if (parts[0].equals("CLASSBOX")) {

				double startX = Double.parseDouble(parts[1]);
				double startY = Double.parseDouble(parts[2]);
				double width = Double.parseDouble(parts[3]);
				double height = Double.parseDouble(parts[4]);
				String tTop = "", tMid = "", tBot = "";
				if (!parts[5].equals("PLACEHOLDER")) {
					tTop = parts[5];
				}
				if (!parts[6].equals("PLACEHOLDER")) {
					tMid = parts[6];
				}
				if (!parts[7].equals("PLACEHOLDER")) {
					tBot = parts[7];
				}
				ClassBox cBox = new ClassBox(startX, startY, width, height, tTop, tMid, tBot);
				cBox.drawMe(pane);
			 }
		}
}