// Alayna Woleslagle
// swetr iteration 1


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.BorderStrokeStyle;
//import javafx.scene.layout.BorderWidths;
//import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javafx.scene.shape.Polygon;
//import javafx.scene.shape.Polyline;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Path;

public class UML extends Application {

	double startingPointX, startingPointY, currentEndingPointX, currentEndingPointY;

	Rectangle newBox = null;
	boolean isBoxBeingDrawn = false;

	Line newLine = null;
	boolean isLineBeingDrawn = false;

	Circle newDot = null;
	boolean isDotBeingDrawn = false;

	TextArea newTextField = null;
	boolean isTextFieldBeingDrawn = false;
	
	Path newClassBox = null;
	boolean isClassBoxBeingDrawn = false;
	
	double startX = 0;
	double startY = 0;
	double endX = 0;
	double endY = 0;
	double width = 0;
	double height = 0;
	
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

		createLineOptions(optionsVBox, UMLScene, group);
		createBoxOptions(optionsVBox, UMLScene, group);
		createArrowOptions(optionsVBox, UMLScene, group);
		addText(optionsVBox, UMLScene, group);
		createButtons(buttonsHBox, UMLStage);

		VBox drawingBox = new VBox();
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

	private void createLineOptions(VBox optionsVBox, Scene drawingScene, Group group) {
		Text lineChoiceMessage = new Text();
		lineChoiceMessage.setText("Choose Line Type");

		// images in comboBox are behaving oddly, maybe we can add them later, but for
		// now I'll just use text options in the comboBox
		/*
		 * ImageView line = new ImageView("line.png"); ImageView boldLine = new
		 * ImageView("bold-line.png"); ImageView dottedLine = new
		 * ImageView("dotted-line.png");
		 * 
		 * 
		 * ComboBox<ImageView> lineOptions = new ComboBox<ImageView>();
		 * lineOptions.getItems().addAll(line, boldLine, dottedLine);
		 */

		ComboBox<String> lineOptions = new ComboBox<String>();
		lineOptions.getItems().addAll("Solid Line", "Dotted Line", "Bold Line");

		optionsVBox.getChildren().addAll(lineChoiceMessage, lineOptions);

		// Handle ComboBox event.
		lineOptions.setOnAction((event) -> {
			//System.out.println(
					//"ComboBox Action selected: " + lineOptions.getSelectionModel().getSelectedItem().toString());
			drawLine(drawingScene, group);

			
			
		});
	}

	private void createBoxOptions(VBox optionsVBox, Scene drawingScene, Group group) {
		Text boxChoiceMessage = new Text();
		boxChoiceMessage.setText("Choose Box Type");

		// ComboBox<ImageView> boxOptions = new ComboBox<ImageView>();
		ComboBox<String> boxOptions = new ComboBox<String>();

		boxOptions.getItems().addAll("Single Box", "Double Box", "Triple Box", "Class Box");

		optionsVBox.getChildren().addAll(boxChoiceMessage, boxOptions);

		// Handle ComboBox event.
		boxOptions.setOnAction((event) -> {
			//System.out.println(
					//"ComboBox Action selected: " + boxOptions.getSelectionModel().getSelectedItem().toString());
			drawClassBox(drawingScene, group);
		});

		// maybe should have a setBoxOption to hold what the user chooses
		// and a get box option that returns their choice
	}

	private void createArrowOptions(VBox optionsVBox, Scene drawingScene, Group group) {
		Text arrowChoiceMessage = new Text();
		arrowChoiceMessage.setText("Choose Arrow Type");

		ComboBox<String> arrowOptions = new ComboBox<String>();
		arrowOptions.getItems().addAll("Normal", "Diamond", "Solid Diamond");

		optionsVBox.getChildren().addAll(arrowChoiceMessage, arrowOptions);

		// Handle ComboBox event.
		arrowOptions.setOnAction((event) -> {
			System.out.println(
					"ComboBox Action selected: " + arrowOptions.getSelectionModel().getSelectedItem().toString());
			// for now, just going to represent an arrow with a dot on the screen. I think
			// we need to design our own arrows later
			drawDot(drawingScene, group);
		});
	}

	private void addText(VBox optionsVBox, Scene drawingScene, Group group) {
		CheckBox addTextCheckBox = new CheckBox("Add Text");
		addTextCheckBox.setIndeterminate(false);

		optionsVBox.getChildren().add(addTextCheckBox);

		// Handle ComboBox event.
		addTextCheckBox.setOnAction((event) -> {
			if (addTextCheckBox.isSelected() == true) {
				drawTextField(drawingScene, group);
				addTextCheckBox.setOnMousePressed((MouseEvent event2) -> {
					addTextCheckBox.setSelected(false);
				}
				);
			} else {
				addTextCheckBox.setSelected(false);
				addTextCheckBox.setIndeterminate(false);
				// not sure why able to draw text fields after unchecking box...
			}
		});
	}

	private void createButtons(HBox buttonsHBox, Stage UMLStage) {
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
	/*
	private void drawBox(Scene UMLScene, Group group) {

		// mouse pressed, user is about to draw a new box
		UMLScene.setOnMousePressed((MouseEvent event) -> {

			if (isBoxBeingDrawn == false) {
				// get x and y coordinates of the mouse press
				startingPointX = event.getSceneX();
				startingPointY = event.getSceneY();

				if (startingPointX > 160 && startingPointX < 1390 && startingPointY > 61 && startingPointY < 690) {
					newBox = new Rectangle();

					newBox.setFill(Color.LIGHTGRAY); // makes entire box white
					newBox.setStroke(Color.BLACK); // gives box black outline

					group.getChildren().add(newBox);

					isBoxBeingDrawn = true;
				}
			}
		});

		// while user is dragging box, dimensions change
		UMLScene.setOnMouseDragged((MouseEvent event) -> {
			if (isBoxBeingDrawn == true) {
				double currentEndingPointX = event.getSceneX();
				double currentEndingPointY = event.getSceneY();

				// makes it so if user drags box outside of gray area, it won't go past sides of
				// gray window
				if (currentEndingPointX < 160) { // left side of gray area
					currentEndingPointX = 160;
				}
				if (currentEndingPointX > 1390) { // right side of gray area
					currentEndingPointX = 1390;
				}
				if (currentEndingPointY < 61) { // top of gray area
					currentEndingPointY = 61;
				}
				if (currentEndingPointY > 690) { // bottom of gray area
					currentEndingPointY = 690;
				}

				newBox.setX(startingPointX);
				newBox.setY(startingPointY);
				newBox.setWidth(currentEndingPointX - startingPointX);
				newBox.setHeight(currentEndingPointY - startingPointY);

				// these make it so user can still draw rectangle if width and height are
				// negative. Just makes those positive
				if (newBox.getWidth() < 0) {
					newBox.setWidth(-newBox.getWidth());
					newBox.setX(newBox.getX() - newBox.getWidth());
				}

				if (newBox.getHeight() < 0) {
					newBox.setHeight(-newBox.getHeight());
					newBox.setY(newBox.getY() - newBox.getHeight());
				}
			}
		});

		// user finished drawing box, reset variables
		UMLScene.setOnMouseReleased((MouseEvent event) -> {
			if (isBoxBeingDrawn == true) {
				newBox = null;
				isBoxBeingDrawn = false;
			}
		});
	}
	*/
	
	
	private void drawLine(Scene UMLScene, Group group) {

		// mouse pressed, user is about to draw a new line
		UMLScene.setOnMousePressed((MouseEvent event) -> {

			if (isLineBeingDrawn == false) {
				// get x and y coordinates of the mouse press
				startingPointX = event.getSceneX();
				startingPointY = event.getSceneY();

				if (startingPointX > UMLScene.getWidth()*.11 && startingPointX < UMLScene.getWidth()*.97 && startingPointY > UMLScene.getHeight()*.11 && startingPointY < UMLScene.getHeight()*.94) {
					newLine = new Line();

					newLine.setFill(Color.BLACK);

					group.getChildren().add(newLine);

					isLineBeingDrawn = true;
				}
			}
		});

		// while user is dragging line, dimensions change
		UMLScene.setOnMouseDragged((MouseEvent event) -> {
			if (isLineBeingDrawn == true) {
				currentEndingPointX = event.getSceneX();
				currentEndingPointY = event.getSceneY();

				// makes it so if user drags line outside of gray area, it won't go past sides
				// of gray window
				if (currentEndingPointX < UMLScene.getWidth()*.11) { // left side of gray area
					currentEndingPointX = UMLScene.getWidth()*.11;
				}
				if (currentEndingPointX > UMLScene.getWidth()*.97) { // right side of gray area
					currentEndingPointX = UMLScene.getWidth()*.97;
				}
				if (currentEndingPointY < UMLScene.getHeight()*.11) { // top of gray area
					currentEndingPointY = UMLScene.getHeight()*.11;
				}
				if (currentEndingPointY > UMLScene.getHeight()*.94) { // bottom of gray area
					currentEndingPointY = UMLScene.getHeight()*.94;
				}
				
				newLine.setStartX(startingPointX);
				newLine.setStartY(startingPointY);

				newLine.setEndX(currentEndingPointX);
				newLine.setEndY(currentEndingPointY);
			}
		});

		// user finished drawing line, reset variables
		UMLScene.setOnMouseReleased((MouseEvent event) -> {
			if (isLineBeingDrawn == true) {
				newLine = null;
				isLineBeingDrawn = false;
			}
		});
	}
	
	private void drawDot(Scene UMLScene, Group group) {

		UMLScene.setOnMousePressed((MouseEvent event) -> {
			if (isDotBeingDrawn == false) {

				newDot = new Circle();

				startingPointX = event.getSceneX();
				startingPointY = event.getSceneY();

				if (startingPointX > UMLScene.getWidth()*.11 && startingPointX < UMLScene.getWidth()*.97 && startingPointY > UMLScene.getHeight()*.11 && startingPointY < UMLScene.getHeight()*.94) {
					newDot.setCenterX(startingPointX);
					newDot.setCenterY(startingPointY);
					newDot.setRadius(5);

					newDot.setFill(Color.BLACK);

					group.getChildren().add(newDot);

					isDotBeingDrawn = true;
				}
			}
		});

		UMLScene.setOnMouseReleased((MouseEvent event) -> {
			if (isDotBeingDrawn == true) {
				newDot = null;
				isDotBeingDrawn = false;
			}
		});
	}
	
	private void drawTextField(Scene UMLScene, Group group) {

		UMLScene.setOnMousePressed((MouseEvent event) -> {
			if (isTextFieldBeingDrawn == false) {

				newTextField = new TextArea();
				newTextField.isResizable();
				newTextField.setMaxSize(300, 300);
				newTextField.setMinSize(20, 20);
				newTextField.setWrapText(true);
				newTextField.setPrefSize(50, 50);
				startingPointX = event.getSceneX();
				startingPointY = event.getSceneY();

				if (startingPointX > UMLScene.getWidth()*.11 && startingPointX < UMLScene.getWidth()*.97 && startingPointY > UMLScene.getHeight()*.11 && startingPointY < UMLScene.getHeight()*.94) {

					newTextField.setTranslateX(startingPointX);
					newTextField.setTranslateY(startingPointY);

					// Changes background color (light gray) and border (black)
					newTextField.setStyle("-fx-background-color: #D3D3D3; -fx-border-color: #000000;");

					group.getChildren().add(newTextField);

					isTextFieldBeingDrawn = true;
				}
			}
		});

		UMLScene.setOnMouseReleased((MouseEvent event) -> {
			if (isTextFieldBeingDrawn == true) {
				newTextField = null;
				isTextFieldBeingDrawn = false;
			}
		});
	}
	
	private void drawClassBox(Scene UMLScene, Group group)
	{
		UMLScene.setOnMousePressed((MouseEvent event) -> {
			
			if (isBoxBeingDrawn == false) {
				// get x and y coordinates of the mouse press
				startingPointX = event.getSceneX();
				startingPointY = event.getSceneY();
				if (startingPointX > UMLScene.getWidth()*.11 && startingPointX < UMLScene.getWidth()*.97 && startingPointY > UMLScene.getHeight()*.11 && startingPointY < UMLScene.getHeight()*.94) 
				{
					newBox = new Rectangle();
					newBox.setFill(Color.LIGHTGRAY);
					newBox.setStroke(Color.BLACK);
					group.getChildren().add(newBox);
					isBoxBeingDrawn = true;
				}
			}
		});
			
		// while user is dragging box, dimensions change
				UMLScene.setOnMouseDragged((MouseEvent event) -> {
					if (isBoxBeingDrawn == true) {
						currentEndingPointX = event.getSceneX();
						currentEndingPointY = event.getSceneY();
						
						if (currentEndingPointX < UMLScene.getWidth()*.11) { // left side of gray area
							currentEndingPointX = UMLScene.getWidth()*.11;
						}
						if (currentEndingPointX > UMLScene.getWidth()*.97) { // right side of gray area
							currentEndingPointX = UMLScene.getWidth()*.97;
						}
						if (currentEndingPointY < UMLScene.getHeight()*.11) { // top of gray area
							currentEndingPointY = UMLScene.getHeight()*.11;
						}
						if (currentEndingPointY > UMLScene.getHeight()*.94) { // bottom of gray area
							currentEndingPointY = UMLScene.getHeight()*.94;
						}
						
						width = currentEndingPointX-startingPointX;
						height = currentEndingPointY-startingPointY;
						
						//newBox = visual aid box 
						newBox.setX(startingPointX);
						newBox.setY(startingPointY);
						newBox.setWidth(width);
						newBox.setHeight(height);

						// these make it so user can still draw rectangle if width and height are
						// negative. Just makes those positive
						if (newBox.getWidth() < 0) {
							newBox.setWidth(-newBox.getWidth());
							newBox.setX(newBox.getX() - newBox.getWidth());
						}

						if (newBox.getHeight() < 0) {
							newBox.setHeight(-newBox.getHeight());
							newBox.setY(newBox.getY() - newBox.getHeight());
						}
					
					}
				});
				
				// user finished drawing box, Draw class box and reset variables
				UMLScene.setOnMouseReleased((MouseEvent event) -> {
					if (isBoxBeingDrawn == true) {
						
						Rectangle[] recs = createBoxes(UMLScene,group,startingPointX,startingPointY,width,height);
						TextArea[] tas = createTextAreas(UMLScene, group, startingPointX, startingPointY, width, height);
						group.getChildren().addAll(recs);
						group.getChildren().addAll(tas);
						
						newBox = null;
						isBoxBeingDrawn = false;
					}
				});
				
		
		
		
	}
	
	private TextArea[] createTextAreas(Scene fxScene, Group g, double startX, double startY, double width, double height)
	{
		//Same logic as createBoxes... consolidate? 
		if (width < -1){
			width = -width;
			startX = startX - width;
		}
		if (height < -1){
			height = -height;
			startY = startY - height;
		}

		if (height < 120){
			height = 120;
		}
		
		if (width < 130){
			width = 130;
		}
		
		double ythird = height/3.0;
		
		TextArea ta1 = new TextArea("Default Text ta1");
		ta1.setLayoutX(startX+1);
		ta1.setLayoutY(startY+1);
		ta1.setPrefHeight(ythird-2);
		ta1.setPrefWidth(width-2);
		
		TextArea ta2 = new TextArea("Default Text ta2");
		ta2.setLayoutX(startX+1);
		ta2.setLayoutY(startY+ythird+1);
		ta2.setPrefHeight(ythird-2);
		ta2.setPrefWidth(width-2);
		
		TextArea ta3 = new TextArea("Default Text ta3");
		ta3.setLayoutX(startX+1);
		ta3.setLayoutY(startY+2*ythird+1);
		ta3.setPrefHeight(ythird-2);
		ta3.setPrefWidth(width-2);

		TextArea[] tas = new TextArea[3];
		tas[0] = ta1;
		tas[1] = ta2;
		tas[2] = ta3;
		return tas;

	}
	
	
	private Rectangle[] createBoxes(Scene fxScene, Group g,double startX, double startY, double width, double height)
	{
		
		//If drawn up and/or left, reverse start + end to compensate. 
		if (width < -1){
			width = -width;
			startX = startX - width;
		}
		if (height < -1){
			height = -height;
			startY = startY - height;
		}

		//Set Min height + width
		if (height < 120){
			height = 120;
		}
		
		if (width < 130){
			width = 130;
		}
		
		//Each section of box is a third. 
		double ythird = height/3;
		
		//Repeating statements...consolidate? 
		//Rectangle(startX, startY, width, height)
		Rectangle r1 = new Rectangle(startX, startY, width, ythird);
		r1.setX(startX);
		r1.setY(startY);
		r1.setFill(Color.WHITE);
		r1.setStroke(Color.BLACK);
		r1.setStrokeWidth(2);
		
		Rectangle r2 = new Rectangle(startX, startY+ythird, width, ythird);
		r2.setX(startX);
		r2.setY(startY+ythird);
		r2.setFill(Color.WHITE);
		r2.setStroke(Color.BLACK);
		r2.setStrokeWidth(2);
		
		Rectangle r3 = new Rectangle(startX,startY+2*ythird, width, ythird);
		r3.setX(startX);
		r3.setY(startY+2*ythird);
		r3.setFill(Color.WHITE);
		r3.setStroke(Color.BLACK);
		r3.setStrokeWidth(2);
		
		Rectangle[] recs = new Rectangle[3];
		recs[0] = r1;
		recs[1] = r2;
		recs[2] = r3;
		
		return recs;
	}
	
}
	
	