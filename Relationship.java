
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
public class Relationship extends Line {
	
	//X & Y coordinates of where user started and ended drag gesture.
	private double startingPointX;
private static double startingPointY;
private static double currentEndingPointX;
private static double currentEndingPointY;

	//newLine is the guideline that displays while drawing a relationship.
	private static Line newLine = null;
	//isLineBeingDrawn describes if an option for drawing a relationship is active (button clicked but not drawn)
	private static boolean isLineBeingDrawn = false;
	
	//String representation of the type of this relationship.
	private static String relType = "";
	
	//Describes X & Y coordinates of the start of this relationship and X & Y coordinates of the end of this relationship.
	private double startXValue;
	private double startYValue;
	private double endXValue;
	private double endYValue;
	
	private Line dragLine;
	
	private Line line;
	private Rectangle rHead;
	private Polygon pHead;
	private Polyline plHead;

	
	 
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
	private void drawLine(Scene UMLScene, Group group, String option) {

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
					this.dragLine = new Line(startingPointX, startingPointY, currentEndingPointX, currentEndingPointY);
					this.dragLine.setStroke(Color.CYAN);
					this.dragLine.setStrokeWidth(10);
					makeDraggable();
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
					//ClassBox.isLineBeingDrawn = false;
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
	private void drawAggregationOrComposition(Group group, double startX, double startY, double endX, double endY, String color) {
		
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
		
		
		line = new Line(startXValue, startYValue, endXValue, endYValue);
		line.setStrokeWidth(2);

		rHead = new Rectangle(endX - 5, endY - 5, 10, 10);
		if (color == "White") {
			rHead.setFill(Color.WHITE);
		} else { // color is black
			rHead.setFill(Color.BLACK);
		}
		rHead.setStroke(Color.BLACK);
		rHead.setStrokeWidth(2);
		rHead.setRotate(Math.toDegrees(Math.atan(height / width)) + 45);
		
		double moveX = (Math.cos(Math.atan(-slope))*5*Math.sqrt(2));
		double moveY = (Math.abs(Math.sin(Math.atan(-slope))*5*Math.sqrt(2)));
		
		if (width > 0){
			rHead.setLayoutX(-moveX);
		}
		else if (width < 0){
			rHead.setLayoutX(moveX);
		}
		
		if (height > 0){
			rHead.setLayoutY(-moveY);
		}
		else if (height < 0){
			rHead.setLayoutY(moveY);
		}
		makeDraggable();
		
		group.getChildren().addAll(dragLine, line, rHead);
	}

	
	/** Draws generalization with input parameters to determine position and length.
	 * @param group Group is container to draw/add this Relationship to. 
	 * @param startX Beginning point X value to draw this Relationship.
	 * @param startY Beginning point Y value to draw this Relationship. 
	 * @param startY End point X value to draw this Relationship.
	 * @param startY End point Y value to draw this Relationship.
	 */
	private void drawGeneralization(Group group, double startX, double startY, double endX, double endY) {
		
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
		pHead = new Polygon(triPoints);
		pHead.setFill(Color.WHITE);
		pHead.setStroke(Color.BLACK);
		pHead.setStrokeWidth(2);
		
		//Shift triangle back onto line based on slope..lots of logic. Otherwise triangle would extend past cursor where user released mouse. 
		//Y is abs because angle is calculated backwards if drawn up vs down. 
		double moveX = (Math.cos(Math.atan(-slope))*10);
		double moveY = (Math.abs(Math.sin(Math.atan(-slope))*10));
		
		if (width > 0){
			pHead.setLayoutX(-moveX);
		}
		else if (width < 0){
			pHead.setLayoutX(moveX);
		}
		
		if (height > 0){
			pHead.setLayoutY(-moveY);
		}
		else if (height < 0){
			pHead.setLayoutY(moveY);
		}
		
		//Draw stem of arrow. 
		line = new Line(startX, startY,endX , endY);
		line.setStrokeWidth(2);
		
		group.getChildren().addAll(dragLine, line, pHead);
	}
	
	/** Draws dependency with input parameters to determine position and length.
	 * @param group Group is container to draw/add this Relationship to. 
	 * @param startX Beginning point X value to draw this Relationship.
	 * @param startY Beginning point Y value to draw this Relationship. 
	 * @param startY End point X value to draw this Relationship.
	 * @param startY End point Y value to draw this Relationship.
	 */
	private void drawDependency(Group group, double startX, double startY, double endX, double endY) {
		
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
		plHead = new Polyline(arrowPoints);
		plHead.setStroke(Color.BLACK);
		plHead.setStrokeWidth(2);
		
		double moveX = (Math.cos(Math.atan(-slope))*10);
		double moveY = (Math.abs(Math.sin(Math.atan(-slope))*10));
		
		if (width > 0){
			plHead.setLayoutX(-moveX);
		}
		else if (width < 0){
			plHead.setLayoutX(moveX);
		}
		
		if (height > 0){
			plHead.setLayoutY(-moveY);
		}
		else if (height < 0){
			plHead.setLayoutY(moveY);
		}
		
		line = new Line(startX, startY, endX, endY);
		line.setStrokeWidth(2);
		line.getStrokeDashArray().addAll(2.0, 5.0);
		makeDraggable();
		
		group.getChildren().addAll(dragLine, line, plHead);
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
	public double getStartXValue() {
		return startXValue;
	}
	
	/** Returns value of beginning point of this Relationship.
	 * @return startYValue
	 */
	public double getStartYValue() {
		return startYValue;
	}
	
	/** Returns value of end point of this Relationship.
	 * @return endXValue
	 */
	public double getEndXValue() {
		return endXValue;
	}
	
	/** Returns value of end point of this Relationship.
	 * @return endYValue
	 */
	public double getEndYValue() {
		return endYValue;
	}
	
	
	
	private void makeDraggable() {
		this.dragLine.setOnMouseClicked(eventClicked -> {
			startingPointX = eventClicked.getSceneX();
			startingPointY = eventClicked.getSceneY();
		});
		
		this.dragLine.setOnMouseDragged(eventDragged -> {
			double deltaX = eventDragged.getSceneX() - startingPointX;
			double deltaY = eventDragged.getSceneY() - startingPointY;
			double dragX = eventDragged.getX(); 
			double dragY = eventDragged.getY();
			updateRel(dragX, dragY, dragX + (endXValue - startXValue), dragY + (endYValue - startYValue), deltaX, deltaY);
			eventDragged.consume();
		});
	}
	
	
	private void updateRel(double startX, double startY, double endX, double endY, double deltaX, double deltaY)
	{
		
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
		double l4x, l4y;
		
		//Parallel angles*length
		l4x = Math.cos(angle) * 10;
		l4y = Math.sin(angle) * 10;
		
		//If drawn facing left, need to subtract instead of add to get coordinate. 
		if (width <= 0) 
		{
			l4x = -l4x;
			l4y = -l4y;
		}
		
		line.setStartX(checkBoundsX(startX));
		line.setStartY(startY);
		line.setEndX(checkBoundsX(endX));
		line.setEndY(endY);
		//Rectangle Head
		if (rHead != null)	
		{
		rHead.setX(checkBoundsX(endX-5));
		rHead.setY(endY-5);
		}
		//Generalization (triangle)
		if (pHead != null)
		{	
		System.out.println(pHead.getLayoutX() + "    " + pHead.getTranslateX());
		pHead.setLayoutX(checkBoundsX(deltaX - l4x));
		pHead.setLayoutY(deltaY - l4y);
		}
		//Dependency (arrow head)
		if (plHead != null)
		{
		System.out.println("PlHead");
		plHead.setLayoutX(checkBoundsX(deltaX - l4x));
		System.out.println(plHead.getLayoutX());
		plHead.setLayoutY(deltaY - l4y);
		}
		
		this.dragLine.setStartX(checkBoundsX(startX));
		this.dragLine.setStartY(startY);
		this.dragLine.setEndX(checkBoundsX(endX));
		this.dragLine.setEndY(endY);
	}

	private double checkBoundsX(double x) {
		if (x < UML.drawingBox.getBoundsInParent().getMinX() + 7.5) { // left side of gray area
			x = UML.drawingBox.getBoundsInParent().getMinX() + 7.5;
		}
		else {
			if (x > UML.drawingBox.getBoundsInParent().getMaxX() - 7.5) { // right side of gray area
				x = UML.drawingBox.getBoundsInParent().getMaxX() - 7.5;
			}
		}

		return x;
	}
	
	
}