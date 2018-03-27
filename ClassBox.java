import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ClassBox {

	private static Rectangle newBox = null;
	private static boolean isBoxBeingDrawn = false;

	private static double startX = 0;
	private static double startY = 0;
	private static double endX = 0;
	private static double endY = 0;
	private static double width = 0;
	private static double height = 0;
	
	// arraylist of arrays. The arrays within the arraylist contain unique info about each class box (x and y coords)
	private static ArrayList<double[]> classBoxes = new ArrayList<double[]>();
	
	public static void drawClassBox(Scene UMLScene, Group group) {
		
		UMLScene.setOnMousePressed((MouseEvent event) -> {
			if (UML.getUserClicked()) {
				if (isBoxBeingDrawn == false) {
					// get x and y coordinates of the mouse press
					startX = event.getSceneX();
					startY = event.getSceneY();
					if (startX > UMLScene.getWidth() * .11 && startX < UMLScene.getWidth() * .97
							&& startY > UMLScene.getHeight() * .11 && startY < UMLScene.getHeight() * .94) {
						newBox = new Rectangle();
						newBox.setFill(Color.LIGHTGRAY);
						newBox.setStroke(Color.BLACK);
						group.getChildren().add(newBox);
						isBoxBeingDrawn = true;
					}
				}
			}
		});

		// while user is dragging box, dimensions change
		UMLScene.setOnMouseDragged((MouseEvent event) -> {
			if (UML.getUserClicked()) {
				if (isBoxBeingDrawn == true) {
					endX = event.getSceneX();
					endY = event.getSceneY();
	
					if (endX < UMLScene.getWidth() * .11) { // left side of gray area
						endX = UMLScene.getWidth() * .11;
					}
					if (endX > UMLScene.getWidth() * .97) { // right side of gray area
						endX = UMLScene.getWidth() * .97;
					}
					if (endY < UMLScene.getHeight() * .11) { // top of gray area
						endY = UMLScene.getHeight() * .11;
					}
					if (endY > UMLScene.getHeight() * .94) { // bottom of gray area
						endY = UMLScene.getHeight() * .94;
					}
	
					width = endX - startX;
					height = endY - startY;
	
					newBox.setX(startX);
					newBox.setY(startY);
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
			}
		});
		
		// user finished drawing box, reset variables
		UMLScene.setOnMouseReleased((MouseEvent event) -> {
			if(UML.getUserClicked()) {
				if (isBoxBeingDrawn == true) {
					Rectangle[] recs = createBoxes(UMLScene, group, startX, startY, width, height);
					Rectangle r1 = recs[0];
					Rectangle r2 = recs[1];
					Rectangle r3 = recs[2];
					/// r1.setVisible(true);
					// r2.setVisible(true);
					// r3.setVisible(true);
					group.getChildren().addAll(r1, r2, r3);
					TextArea[] tas = createTextAreas(UMLScene, group, startX, startY, width, height);
					TextArea ta1 = tas[0];
					TextArea ta2 = tas[1];
					TextArea ta3 = tas[2];
					// ta1.setVisible(true);
					// ta2.setVisible(true);
					// ta3.setVisible(true);
					group.getChildren().addAll(ta1, ta2, ta3);
					// System.out.println("rectangle height = " + r1.getHeight());
					// System.out.println("rectnagle width = " + r1.getWidth());
					// System.out.println("ta maxheight = " + ta1.getMaxHeight() + " ta height =" +
					// ta1.getHeight());
					// System.out.println("ta maxwidth = " + ta1.getMaxWidth() + " ta width = " +
					// ta1.getWidth() );
	
					// newBox = null;
					isBoxBeingDrawn = false;
				}
				UML.setUserClicked(false);
			}
			
			// add class box to arraylist of all class boxes
			double[] newClassBox = new double[4];
			newClassBox[0] = startX;
			newClassBox[1] = startY;
			newClassBox[2] = endX;
			newClassBox[3] = endY;
			classBoxes.add(newClassBox);
		});
	}

	private static Rectangle[] createBoxes(Scene fxScene, Group g, double startX, double startY, double width,
			double height) {
		// If drawn up and/or left, reverse start + end to compensate.
		if (width < -1) {
			width = -width;
			startX = startX - width;
		}
		if (height < -1) {
			height = -height;
			startY = startY - height;
		}

		// Set Min height + width
		if (height < 120) {
			height = 120;
		}
		if (width < 130) {
			width = 130;
		}
		// Each section of box is a third.
		double ythird = height / 3;

		// Rectangle(startX, startY, width, height)
		Rectangle r1 = new Rectangle(startX, startY, width, ythird);
		r1.setX(startX);
		r1.setY(startY);
		r1.setFill(Color.WHITE);
		r1.setStroke(Color.BLACK);
		r1.setStrokeWidth(2);

		Rectangle r2 = new Rectangle(startX, startY + ythird, width, ythird);
		r2.setX(startX);
		r2.setY(startY + ythird);
		r2.setFill(Color.WHITE);
		r2.setStroke(Color.BLACK);
		r2.setStrokeWidth(2);

		Rectangle r3 = new Rectangle(startX, startY + 2 * ythird, width, ythird);
		r3.setX(startX);
		r3.setY(startY + 2 * ythird);
		r3.setFill(Color.WHITE);
		r3.setStroke(Color.BLACK);
		r3.setStrokeWidth(2);

		Rectangle[] recs = new Rectangle[3];
		/// r1.setVisible(false);
		/// r2.setVisible(false);
		/// r3.setVisible(false);

		// System.out.println("r1 Width = " + r1.getWidth());
		// System.out.println("r1 Height = " + r1.getHeight());
		recs[0] = r1;
		recs[1] = r2;
		recs[2] = r3;

		return recs;
	}

	private static TextArea[] createTextAreas(Scene fxScene, Group g, double startX, double startY, double width,
			double height) {
		// Same logic as createBoxes
		if (width < -1) {
			width = -width;
			startX = startX - width;
		}
		if (height < -1) {
			height = -height;
			startY = startY - height;
		}

		if (height < 120) {
			height = 120;
		}
		if (width < 130) {
			width = 130;
		}

		double ythird = height / 3.0;

		TextArea ta1 = new TextArea("Default Text ta1");
		ta1.setLayoutX(startX + 1);
		ta1.setLayoutY(startY + 1);
		ta1.setPrefHeight(ythird - 2);
		ta1.setPrefWidth(width - 2);
		// ta1.setMinHeight(37);
		// ta1.setMinWidth(120);

		TextArea ta2 = new TextArea("Default Text ta2");
		ta2.setLayoutX(startX + 1);
		ta2.setLayoutY(startY + ythird + 1);
		ta2.setPrefHeight(ythird - 2);
		ta2.setPrefWidth(width - 2);
		// ta2.setMinHeight(37);
		// ta2.setMinWidth(120);

		TextArea ta3 = new TextArea("Default Text ta3");
		ta3.setLayoutX(startX + 1);
		ta3.setLayoutY(startY + 2 * ythird + 1);
		ta3.setPrefHeight(ythird - 2);
		ta3.setPrefWidth(width - 2);
		// ta3.setMinHeight(37);
		// ta3.setMinWidth(120);

		// ta1.setVisible(false);
		// ta2.setVisible(false);
		// ta3.setVisible(false);

		// System.out.println(ta1.getMaxWidth());

		// System.out.println("ta height =" + ta1.getMaxHeight());
		// System.out.println("ta width = " + ta1.getMaxWidth());

		TextArea[] tas = new TextArea[3];
		tas[0] = ta1;
		tas[1] = ta2;
		tas[2] = ta3;
		return tas;
	}
	
	// need getters
	
	public static double getStartX () {
		return startX;
	}
	
	public static double getStartY () {
		return startY;
	}
	
	public static double getEndX () {
		return endX;
	}
	
	public static double getEndY () {
		return endY;
	}
	
	// move the class boxes
	// returns element index that matches the box to be moved
	// returns -1 if not found
	// this works, but you really have to click like exactly on the box's line for this to work
	// should adjust so if you click + or - like 5 pixels from the x or the y then it should recognize
	public static int checkCoordinates (double x, double y) {
		for (int i = 0; i < classBoxes.size(); ++i) {
			double[] element = new double[4];
			element = classBoxes.get(i);
			//System.out.println(element[0]);
			// startX or endX
			if (x == element[0] || x == element[0] + 1 || x == element[0] + 2 || x == element[2] || x == element[2] + 1 || x == element[2] + 2) {
				return i;
			}  // could be combined with above, I separated it for testing and so it was easier to read
			else if (y == element[1] || y == element[1] + 1 || y == element[1] + 2 || y == element[3] || y == element[3] + 1 || y == element[3] + 2) {
				return i;
			}
		}
		
		return -1;
	}
}
