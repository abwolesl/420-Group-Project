
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

public class Relationship extends Line {
	
	//X & Y coordinates of where user started and ended drag gesture.
	private static double startingPointX, startingPointY, currentEndingPointX, currentEndingPointY;

	//newLine is the guideline that displays while drawing a relationship.
	private static Line newLine = null;
	//isLineBeingDrawn describes if an option for drawing a relationship is active (button clicked but not drawn)
	private static boolean isLineBeingDrawn = false;
	
	//String representation of the type of this relationship.
	private static String relType = "";
	
	//Describes X & Y coordinates of the start of this relationship and X & Y coordinates of the end of this relationship.
	private static double startXValue;
	private static double startYValue;
	private static double endXValue;
	private static double endYValue;

	
	/** Relationship constructor.
	 * @param UMLScene Scene where user drags to draw this relationship.
	 * @param group Group where this relationship is placed. 
	 * @param relationshipType Type of this relationship.
	 */
	public Relationship (Scene UMLScene, Group group, String relationshipType) {
		
		relType = relationshipType;
		drawLine(UMLScene, group, relationshipType);
	}
	
	//draws guideline from user click until user releases mouse. Then draws this Relationship's type. 
	private static void drawLine(Scene UMLScene, Group group, String option) {

		// mouse pressed, user is about to draw a new line
		UMLScene.setOnMousePressed((MouseEvent event) -> {
			if (UML.getUserClicked()) {
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
			}
		});

		// while user is dragging line, dimensions change
		UMLScene.setOnMouseDragged((MouseEvent event) -> {
			if (UML.getUserClicked()) {
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
			}
		});

		// user finished drawing line, reset variables
		UMLScene.setOnMouseReleased((MouseEvent event) -> {
			if (UML.getUserClicked()) {
				if (isLineBeingDrawn == true) {
					
					switch (option){
						case "Aggregation":
							drawAggregationOrComposition(group, startingPointX,startingPointY,currentEndingPointX,currentEndingPointY,"White");
							newLine.setVisible(false);
							break;
						case "Composition":
							drawAggregationOrComposition(group, startingPointX,startingPointY,currentEndingPointX,currentEndingPointY,"Black");
							newLine.setVisible(false);
							break;
						case "Generalization":
							drawGeneralization(group, startingPointX,startingPointY,currentEndingPointX,currentEndingPointY);
							newLine.setVisible(false);
							break;
						case "Dependency":
							drawDependency(group, startingPointX, startingPointY, currentEndingPointX, currentEndingPointY);
							newLine.setVisible(false);
							break;
					}
					//newLine = null;
					isLineBeingDrawn = false;
				}
				UML.setUserClicked(false);
			}
		});
	}
	
	// Draw aggregation or composition line
	// Logic for both lines is identical, only difference is the fill of the arrowhead (diamond)
	// This color is passed as a parameter
	/** Draws aggregation or composition based on selection. 
	 * @param group Group is container to draw/add this Relationship to. 
	 * @param startX Beginning point X value to draw this Relationship.
	 * @param startY Beginning point Y value to draw this Relationship. 
	 * @param startY End point X value to draw this Relationship.
	 * @param startY End point Y value to draw this Relationship.
	 * @param color Determines aggregation(black) or composition(white)
	 */
	static void drawAggregationOrComposition(Group group, double startX, double startY, double endX, double endY, String color) {
		
		startXValue = startX;
		startYValue = startY;
		endXValue = endX;
		endYValue = endY;
		
		double height = endY - startYValue;
		double width = endX - startXValue;

		double slope = height/width;
		
		//Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);
		
		
		Line line = new Line(startXValue, startYValue, endXValue, endYValue);
		line.setStrokeWidth(2);

		Rectangle diamond = new Rectangle(endX - 5, endY - 5, 10, 10);
		if (color == "White") {
			diamond.setFill(Color.WHITE);
		} else { // color is black
			diamond.setFill(Color.BLACK);
		}
		diamond.setStroke(Color.BLACK);
		diamond.setStrokeWidth(2);
		diamond.setRotate(Math.toDegrees(Math.atan(height / width)) + 45);
		
		double moveX = (Math.cos(Math.atan(-slope))*5*Math.sqrt(2));
		double moveY = (Math.abs(Math.sin(Math.atan(-slope))*5*Math.sqrt(2)));
		
		if (width > 0){
			diamond.setLayoutX(-moveX);
		}
		else if (width < 0){
			diamond.setLayoutX(moveX);
		}
		
		if (height > 0){
			diamond.setLayoutY(-moveY);
		}
		else if (height < 0){
			diamond.setLayoutY(moveY);
		}
		
		group.getChildren().addAll(line, diamond);
	}

	
	/** Draws generalization with input parameters to determine position and length.
	 * @param group Group is container to draw/add this Relationship to. 
	 * @param startX Beginning point X value to draw this Relationship.
	 * @param startY Beginning point Y value to draw this Relationship. 
	 * @param startY End point X value to draw this Relationship.
	 * @param startY End point Y value to draw this Relationship.
	 */
	static void drawGeneralization(Group group, double startX, double startY, double endX, double endY) {
		
		startXValue = startX;
		startYValue = startY;
		endXValue = endX;
		endYValue = endY;
		
		double height = endYValue - startYValue;
		double width = endXValue - startXValue;
		double slope = height/width;
		
		//Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);
		
		double angle = Math.atan(slope);
		double l2x, l2y ,l4x, l4y;
		
		//Perpendicular angles*length
		l2x = Math.cos(angle + Math.PI/2) * 7.5;
		l2y = Math.sin(angle + Math.PI/2) * 7.5;
					
		//Parallel angles*length
		l4x = Math.cos(angle) * 10;
		l4y = Math.sin(angle) * 10;
		
		//If drawn facing left, need to subtract instead of add to get coordinate. 
		if (width <= 0) 
		{
			l4x = -l4x;
			l4y = -l4y;
		}
		
		//Make Triangle w/ points + set attributes
		double[] triPoints = new double[6];
		triPoints[0] = (l2x)+endX;
		triPoints[1] = (l2y)+endY;
		triPoints[2] = -(l2x)+endX;
		triPoints[3] = -(l2y)+endY;
		triPoints[4] = (l4x)+endX;
		triPoints[5] = (l4y)+endY;
		Polygon triangle = new Polygon(triPoints);
		triangle.setFill(Color.WHITE);
		triangle.setStroke(Color.BLACK);
		triangle.setStrokeWidth(2);
		
		//Shift triangle back onto line based on slope..lots of logic. Otherwise triangle would extend past cursor where user released mouse. 
		//Y is abs because angle is calculated backwards if drawn up vs down. 
		double moveX = (Math.cos(Math.atan(-slope))*10);
		double moveY = (Math.abs(Math.sin(Math.atan(-slope))*10));
		
		if (width > 0){
			triangle.setLayoutX(-moveX);
		}
		else if (width < 0){
			triangle.setLayoutX(moveX);
		}
		
		if (height > 0){
			triangle.setLayoutY(-moveY);
		}
		else if (height < 0){
			triangle.setLayoutY(moveY);
		}
		
		//Draw stem of arrow. 
		Line line = new Line(startX, startY,endX , endY);
		line.setStrokeWidth(2);
		
		group.getChildren().addAll(line, triangle);
	}
	
	/** Draws dependency with input parameters to determine position and length.
	 * @param group Group is container to draw/add this Relationship to. 
	 * @param startX Beginning point X value to draw this Relationship.
	 * @param startY Beginning point Y value to draw this Relationship. 
	 * @param startY End point X value to draw this Relationship.
	 * @param startY End point Y value to draw this Relationship.
	 */
	static void drawDependency(Group group, double startX, double startY, double endX, double endY) {
		
		startXValue = startX;
		startYValue = startY;
		endXValue = endX;
		endYValue = endY;
		
		double height = endYValue - startYValue;
		double width = endXValue - startXValue;
		double slope = height/width;
		
		//Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);
		
		double angle = Math.atan(slope);
		// what are these?
		double l2x, l2y ,l4x, l4y;
		
		//Perpendicular angles*length
		l2x = Math.cos(angle + Math.PI/2) * 7.5;
		l2y = Math.sin(angle + Math.PI/2) *7.5;
					
		//Parallel angles*length
		l4x = Math.cos(angle) * 10;
		l4y = Math.sin(angle) * 10;
		
		//If drawn facing left, need to subtract instead of add to get coordinate. 
		if (width <= 0) 
		{
			l4x = -l4x;
			l4y = -l4y;
		}
		
		
		double[] arrowPoints = new double[6];
		arrowPoints[0] = (l2x)+endX;
		arrowPoints[1] = (l2y)+endY;
		arrowPoints[2] = (l4x)+endX;
		arrowPoints[3] = (l4y)+endY;
		arrowPoints[4] = -(l2x)+endX;
		arrowPoints[5] = -(l2y)+endY;
		
		
		//Polygon triangle = new Polygon(triPoints);
		Polyline arrowhead = new Polyline(arrowPoints);
		arrowhead.setStroke(Color.BLACK);
		arrowhead.setStrokeWidth(2);
		
		double moveX = (Math.cos(Math.atan(-slope))*10);
		double moveY = (Math.abs(Math.sin(Math.atan(-slope))*10));
		
		if (width > 0){
			arrowhead.setLayoutX(-moveX);
		}
		else if (width < 0){
			arrowhead.setLayoutX(moveX);
		}
		
		if (height > 0){
			arrowhead.setLayoutY(-moveY);
		}
		else if (height < 0){
			arrowhead.setLayoutY(moveY);
		}
		
		Line line = new Line(startX, startY, endX, endY);
		line.setStrokeWidth(2);
		line.getStrokeDashArray().addAll(2.0, 5.0);
		
		group.getChildren().addAll(line, arrowhead);
	}
	
	
	// Getters and setters 

	/** Returns type of this Relationship as a String.
	 * @return relType of this Relationship
	 */
	public String getRelType() {
		return relType;
	}
	
	/** Returns X value of beginning point of this Relationship.
	 * @return startXValue
	 */
	public static double getStartXValue() {
		return startXValue;
	}
	
	/** Returns value of beginning point of this Relationship.
	 * @return startYValue
	 */
	public static double getStartYValue() {
		return startYValue;
	}
	
	/** Returns value of end point of this Relationship.
	 * @return endXValue
	 */
	public static double getEndXValue() {
		return endXValue;
	}
	
	/** Returns value of end point of this Relationship.
	 * @return endYValue
	 */
	public static double getEndYValue() {
		return endYValue;
	}
}