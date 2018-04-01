import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Relationship {
	
	private static double startingPointX, startingPointY, currentEndingPointX, currentEndingPointY;

	private static Line newLine = null;
	private static boolean isLineBeingDrawn = false;
	
	// empty ctor
	public Relationship () {
		
	}
	
	// idea is to create a relationship object
	public Relationship (Scene UMLScene, Group group, String relationshipType) {
		
		drawLine(UMLScene, group, relationshipType);
	}
	
	static void drawLine(Scene UMLScene, Group group, String option) {

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
							drawWD(UMLScene,group,startingPointX,startingPointY,currentEndingPointX,currentEndingPointY);
							newLine.setVisible(false);
							break;
						case "Composition":
							drawBD(UMLScene,group,startingPointX,startingPointY,currentEndingPointX,currentEndingPointY);
							newLine.setVisible(false);
							break;
						case "Generalization":
							drawTriangle(UMLScene,group,startingPointX,startingPointY,currentEndingPointX,currentEndingPointY);
							newLine.setVisible(false);
							break;
						case "Dependency":
							drawDependency(UMLScene, group, startingPointX, startingPointY, currentEndingPointX, currentEndingPointY);
							newLine.setVisible(false);
							break;
					}
					newLine = null;
					isLineBeingDrawn = false;
				}
				UML.setUserClicked(false);
			}
		});
	}
	
	private static void drawWD(Scene fxScene, Group g, double startX, double startY, double endX, double endY) {
		
		// Used to combine shapes
		Path aggregation = new Path();

		double height = endY - startY;
		double width = endX - startX;
		double slope = height/width;
		
		//Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);
		
		
		Line l1 = new Line(startX, startY, endX, endY);
		l1.setStrokeWidth(2);

		double moveX = (Math.cos(Math.atan(-slope))*5*Math.sqrt(2));
		double moveY = (Math.abs(Math.sin(Math.atan(-slope))*5*Math.sqrt(2)));
		
		
		
		Rectangle r1 = new Rectangle(endX - 5, endY - 5, 10, 10);
		r1.setFill(Color.WHITE);
		r1.setStroke(Color.BLACK);
		r1.setStrokeWidth(2);
		// System.out.println(Math.toDegrees(Math.atan(height/width)));
		// System.out.println(Math.toDegrees(Math.atan(height/width)));
		r1.setRotate(Math.toDegrees(Math.atan(height / width)) + 45);
		
		if (width > 0){
			r1.setLayoutX(-moveX);
		}
		else if (width < 0){
			r1.setLayoutX(moveX);
		}
		
		if (height > 0){
			r1.setLayoutY(-moveY);
		}
		else if (height < 0){
			r1.setLayoutY(moveY);
		}
		
		aggregation = (Path) Shape.union(r1, l1);
		// somehow it's losing the color of the rectangle...

		// combines line and arrow head so they are one shape
		g.getChildren().addAll(aggregation);
	}
	
	private static void drawBD(Scene fxScene, Group g, double startX, double startY, double endX, double endY) {

		Path composition = new Path();
		
		double height = endY - startY;
		double width = endX - startX;

		double slope = height/width;
		
		//Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);
		
		
		Line l1 = new Line(startX, startY, endX, endY);
		l1.setStrokeWidth(2);

		Rectangle r1 = new Rectangle(endX - 5, endY - 5, 10, 10);
		r1.setFill(Color.BLACK);
		r1.setStroke(Color.BLACK);
		r1.setStrokeWidth(2);
		//System.out.println(Math.toDegrees(Math.atan(height / width)));
		//System.out.println(Math.toDegrees(Math.atan(height / width)));
		r1.setRotate(Math.toDegrees(Math.atan(height / width)) + 45);
		// Rectangle r2 = new Rectangle(endX-10,endY-10,10,10);
		// r2.setFill(Color.WHITE);
		// r2.setStroke(Color.BLACK);
		// r2.setStrokeWidth(2);
		// r2.setRotate(Math.toDegrees(Math.atan(height/width)));
		/*
		 * Rectangle r3 = new Rectangle(startX,startY+2*height, width, height);
		 * r3.setFill(Color.WHITE); r3.setStroke(Color.BLACK);
		 * r3.setStrokeWidth(2);
		 */
		
		double moveX = (Math.cos(Math.atan(-slope))*5*Math.sqrt(2));
		double moveY = (Math.abs(Math.sin(Math.atan(-slope))*5*Math.sqrt(2)));
		
		if (width > 0){
			r1.setLayoutX(-moveX);
		}
		else if (width < 0){
			r1.setLayoutX(moveX);
		}
		
		if (height > 0){
			r1.setLayoutY(-moveY);
		}
		else if (height < 0){
			r1.setLayoutY(moveY);
		}
		
		composition = (Path) Shape.union(r1, l1);
		
		g.getChildren().addAll(composition);
	}
	
	private static void drawTriangle(Scene fxScene, Group g, double startX, double startY, double endX, double endY) {
		
		Path generalization = new Path();
		
		double height = endY-startY;
		double width = endX-startX;
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
		Line l1 = new Line(startX, startY,endX , endY);
		l1.setStrokeWidth(2);
		generalization = (Path) Shape.union(l1, triangle);
		g.getChildren().addAll(generalization);
	}
	
	private static void drawDependency(Scene fxScene, Group g, double startX, double startY, double endX, double endY) {
		
		Path dependency = new Path();
		
		double height = endY-startY;
		double width = endX-startX;
		double slope = height/width;
		
		//Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);
		
		double angle = Math.atan(slope);
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
		Polyline arrowHead = new Polyline(arrowPoints);
		arrowHead.setStroke(Color.BLACK);
		arrowHead.setStrokeWidth(2);
		
		double moveX = (Math.cos(Math.atan(-slope))*10);
		double moveY = (Math.abs(Math.sin(Math.atan(-slope))*10));
		
		if (width > 0){
			arrowHead.setLayoutX(-moveX);
		}
		else if (width < 0){
			arrowHead.setLayoutX(moveX);
		}
		
		if (height > 0){
			arrowHead.setLayoutY(-moveY);
		}
		else if (height < 0){
			arrowHead.setLayoutY(moveY);
		}
		
		Line l1 = new Line(startX, startY, endX, endY);
		l1.setStrokeWidth(2);
		l1.getStrokeDashArray().addAll(2.0, 5.0);
		
		dependency = (Path) Shape.union(l1, arrowHead);
		
		g.getChildren().addAll(dependency);
	}
}