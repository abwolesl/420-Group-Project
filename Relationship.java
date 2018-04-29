
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

public class Relationship {

	// X & Y coordinates of where user started and ended drag gesture.
	private double startingPointX;
	private static double startingPointY;
	private static double currentEndingPointX;
	private static double currentEndingPointY;

	// newLine is the guideline that displays while drawing a relationship.
	private static Line newLine = null;
	// isLineBeingDrawn describes if an option for drawing a relationship is active
	// (button clicked but not drawn)
	private static boolean isLineBeingDrawn = false;
	public static boolean hideClassBox;

	// String representation of the type of this relationship.
	private String relType = "";

	// Describes X & Y coordinates of the start of this relationship and X & Y
	// coordinates of the end of this relationship.
	private double startXValue;
	private double startYValue;
	private double endXValue;
	private double endYValue;

	//Parts of a line
	private Line line; //Stem
	private Rectangle rHead; //Aggregation + Composition (rectangle)
	private Polygon pHead; //Generalization (triangle)
	private Polyline plHead; //Dependency (arrowhead)
	private Circle pivot; //Blue circle underneath head.

	/**
	 * Relationship constructor.
	 * 
	 * @param UMLScene
	 *            Scene where user drags to draw this relationship.
	 * @param group
	 *            Group where this relationship is placed.
	 * @param relationshipType
	 *            Type of this relationship.
	 */
	public Relationship(Scene UMLScene, Group group, String relationshipType) {

		relType = relationshipType;
		drawLine(UMLScene, group, relationshipType);

	}

	// draws guideline from user click until user releases mouse. Then draws this
	// Relationship's type.
	private void drawLine(Scene UMLScene, Group group, String option) {

		// mouse pressed, user is about to draw a new line
		UMLScene.setOnMousePressed((MouseEvent event) -> {
			if (UML.getUserClicked()) {
				if (isLineBeingDrawn == false) {
					UML.hideClassBox = true;
					// get x and y coordinates of the mouse press
					startingPointX = event.getSceneX();
					startingPointY = event.getSceneY();

					if (startingPointX < UML.drawingBox.getBoundsInParent().getMaxX()
							&& startingPointX > UML.drawingBox.getBoundsInParent().getMinX()
							&& startingPointY < UML.drawingBox.getBoundsInParent().getMaxY()
							&& startingPointY > UML.drawingBox.getBoundsInParent().getMinY()) {

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
					double buffer = 7.5;
					currentEndingPointX = event.getSceneX();
					currentEndingPointY = event.getSceneY();

					// makes it so if user drags line outside of gray area, it won't go past sides
					// of gray window
					if (currentEndingPointX < UML.drawingBox.getBoundsInParent().getMinX() + buffer) { // left side of
																										// gray area
						currentEndingPointX = UML.drawingBox.getBoundsInParent().getMinX() + buffer;
					}
					if (currentEndingPointX > UML.drawingBox.getBoundsInParent().getMaxX() - buffer) { // right side of
																										// gray area
						currentEndingPointX = UML.drawingBox.getBoundsInParent().getMaxX() - buffer;
					}
					if (currentEndingPointY < UML.drawingBox.getBoundsInParent().getMinY() + buffer) { // top of gray
																										// area
						currentEndingPointY = UML.drawingBox.getBoundsInParent().getMinY() + buffer;
					}
					if (currentEndingPointY > UML.drawingBox.getBoundsInParent().getMaxY() - buffer) { // bottom of gray
																										// area
						currentEndingPointY = UML.drawingBox.getBoundsInParent().getMaxY() - buffer;
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

					// this.dragLine = new Line(startingPointX, startingPointY, currentEndingPointX,
					// currentEndingPointY);
					// this.dragLine.setStroke(Color.CYAN);
					// this.dragLine.setStrokeWidth(10);
					makeNewCircle(startingPointX, startingPointY, currentEndingPointX, currentEndingPointY);
					group.getChildren().add(pivot);

					switch (option) {
					case "Aggregation":
						drawAggregationOrComposition(group, startingPointX, startingPointY, currentEndingPointX,
								currentEndingPointY, "White");
						newLine.setVisible(false);
						break;
					case "Composition":
						drawAggregationOrComposition(group, startingPointX, startingPointY, currentEndingPointX,
								currentEndingPointY, "Black");
						newLine.setVisible(false);
						break;
					case "Generalization":
						drawGeneralization(group, startingPointX, startingPointY, currentEndingPointX,
								currentEndingPointY);
						newLine.setVisible(false);
						break;
					case "Dependency":
						drawDependency(group, startingPointX, startingPointY, currentEndingPointX, currentEndingPointY);
						newLine.setVisible(false);
						break;
					}

					makeRotatable();
					UML.hideClassBox = false;
					for (ClassBox cb : UML.cBoxArray) {
						cb.showAura();
					}
					// newLine = null;
					isLineBeingDrawn = false;
					// ClassBox.isLineBeingDrawn = false;
				}

				UML.setUserClicked(false);
			}
		});

	}

	public Relationship(Group group, String relationshipType, double startX, double startY, double endX, double endY) {

		makeNewCircle(startX, startY, endX, endY);
		makeRotatable();
		group.getChildren().add(pivot);
		this.relType = relationshipType;

		switch (relationshipType) {
		case "Aggregation":
			drawAggregationOrComposition(group, startX, startY, endX, endY, "White");
			break;
		case "Composition":
			drawAggregationOrComposition(group, startX, startY, endX, endY, "Black");
			break;
		case "Generalization":
			drawGeneralization(group, startX, startY, endX, endY);

			break;
		case "Dependency":
			drawDependency(group, startX, startY, endX, endY);

			break;
		}

		line.setOnMouseEntered(event -> {
			line.setStroke(Color.INDIANRED);
		});

		line.setOnMouseExited(event -> {
			line.setStroke(Color.BLACK);
		});

	}

	// Draw aggregation or composition line
	// Logic for both lines is identical, only difference is the fill of the
	// arrowhead (diamond)
	// This color is passed as a parameter
	/**
	 * Draws aggregation or composition based on selection.
	 * 
	 * @param group
	 *            Group is container to draw/add this Relationship to.
	 * @param startX
	 *            Beginning point X value to draw this Relationship.
	 * @param startY
	 *            Beginning point Y value to draw this Relationship.
	 * @param startY
	 *            End point X value to draw this Relationship.
	 * @param startY
	 *            End point Y value to draw this Relationship.
	 * @param color
	 *            Determines aggregation(black) or composition(white)
	 */
	void drawAggregationOrComposition(Group group, double startX, double startY, double endX, double endY,
			String color) {

		startXValue = startX;
		startYValue = startY;
		endXValue = endX;
		endYValue = endY;

		double height = endY - startYValue;
		double width = endX - startXValue;

		double slope = height / width;

		// Diving by 0 is bad
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

		double moveX = (Math.cos(Math.atan(-slope)) * 5 * Math.sqrt(2));
		double moveY = (Math.abs(Math.sin(Math.atan(-slope)) * 5 * Math.sqrt(2)));

		if (width > 0) {
			rHead.setLayoutX(-moveX);
		} else if (width < 0) {
			rHead.setLayoutX(moveX);
		}

		if (height > 0) {
			rHead.setLayoutY(-moveY);
		} else if (height < 0) {
			rHead.setLayoutY(moveY);
		}
		// Looks nice to show / hide blue bubble
		rHead.setOnMouseExited(event -> {
			pivot.setFill(Color.TRANSPARENT);
		});

		rHead.setOnMouseEntered(event -> {
			pivot.setFill(Color.LIGHTCYAN);
		});

		makeDraggable();
		group.getChildren().addAll(line, rHead);

		line.setOnMouseEntered(event -> {
			line.setStroke(Color.INDIANRED);
		});

		line.setOnMouseExited(event -> {
			line.setStroke(Color.BLACK);
		});

	}

	/**
	 * Draws generalization with input parameters to determine position and length.
	 * 
	 * @param group
	 *            Group is container to draw/add this Relationship to.
	 * @param startX
	 *            Beginning point X value to draw this Relationship.
	 * @param startY
	 *            Beginning point Y value to draw this Relationship.
	 * @param startY
	 *            End point X value to draw this Relationship.
	 * @param startY
	 *            End point Y value to draw this Relationship.
	 */
	void drawGeneralization(Group group, double startX, double startY, double endX, double endY) {

		startXValue = startX;
		startYValue = startY;
		endXValue = endX;
		endYValue = endY;

		double height = endYValue - startYValue;
		double width = endXValue - startXValue;
		double slope = height / width;

		// Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);

		double angle = Math.atan(slope);
		double l2x, l2y, l4x, l4y;

		// Perpendicular angles*length
		l2x = Math.cos(angle + Math.PI / 2) * 7.5;
		l2y = Math.sin(angle + Math.PI / 2) * 7.5;

		// Parallel angles*length
		l4x = Math.cos(angle) * 10;
		l4y = Math.sin(angle) * 10;

		// If drawn facing left, need to subtract instead of add to get coordinate.
		if (width <= 0) {
			l4x = -l4x;
			l4y = -l4y;
		}

		// Make Triangle w/ points + set attributes
		double[] triPoints = new double[6];
		triPoints[0] = (l2x) + endX;
		triPoints[1] = (l2y) + endY;
		triPoints[2] = -(l2x) + endX;
		triPoints[3] = -(l2y) + endY;
		triPoints[4] = (l4x) + endX;
		triPoints[5] = (l4y) + endY;
		pHead = new Polygon(triPoints);
		pHead.setFill(Color.WHITE);
		pHead.setStroke(Color.BLACK);
		pHead.setStrokeWidth(2);

		// Shift triangle back onto line based on slope..lots of logic. Otherwise
		// triangle would extend past cursor where user released mouse.
		// Y is abs because angle is calculated backwards if drawn up vs down.
		double moveX = (Math.cos(Math.atan(-slope)) * 10);
		double moveY = (Math.abs(Math.sin(Math.atan(-slope)) * 10));

		if (width > 0) {
			pHead.setLayoutX(-moveX);
		} else if (width < 0) {
			pHead.setLayoutX(moveX);
		}

		if (height > 0) {
			pHead.setLayoutY(-moveY);
		} else if (height < 0) {
			pHead.setLayoutY(moveY);
		}

		// Draw stem of arrow.
		line = new Line(startX, startY, endX, endY);
		line.setStrokeWidth(2);

		// Looks nice to show / hide blue bubble
		pHead.setOnMouseExited(event -> {
			pivot.setFill(Color.TRANSPARENT);
		});

		pHead.setOnMouseEntered(event -> {
			pivot.setFill(Color.LIGHTCYAN);
		});

		makeDraggable();
		group.getChildren().addAll(line, pHead);

		line.setOnMouseEntered(event -> {
			line.setStroke(Color.INDIANRED);
		});

		line.setOnMouseExited(event -> {
			line.setStroke(Color.BLACK);
		});

	}

	/**
	 * Draws dependency with input parameters to determine position and length.
	 * 
	 * @param group
	 *            Group is container to draw/add this Relationship to.
	 * @param startX
	 *            Beginning point X value to draw this Relationship.
	 * @param startY
	 *            Beginning point Y value to draw this Relationship.
	 * @param startY
	 *            End point X value to draw this Relationship.
	 * @param startY
	 *            End point Y value to draw this Relationship.
	 */
	void drawDependency(Group group, double startX, double startY, double endX, double endY) {

		startXValue = startX;
		startYValue = startY;
		endXValue = endX;
		endYValue = endY;

		double height = endYValue - startYValue;
		double width = endXValue - startXValue;
		double slope = height / width;

		// Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);

		double angle = Math.atan(slope);
		// what are these?
		double l2x, l2y, l4x, l4y;

		// Perpendicular angles*length
		l2x = Math.cos(angle + Math.PI / 2) * 7.5;
		l2y = Math.sin(angle + Math.PI / 2) * 7.5;

		// Parallel angles*length
		l4x = Math.cos(angle) * 10;
		l4y = Math.sin(angle) * 10;

		// If drawn facing left, need to subtract instead of add to get coordinate.
		if (width <= 0) {
			l4x = -l4x;
			l4y = -l4y;
		}

		double[] arrowPoints = new double[6];
		arrowPoints[0] = (l2x) + endX;
		arrowPoints[1] = (l2y) + endY;
		arrowPoints[2] = (l4x) + endX;
		arrowPoints[3] = (l4y) + endY;
		arrowPoints[4] = -(l2x) + endX;
		arrowPoints[5] = -(l2y) + endY;

		// Polygon triangle = new Polygon(triPoints);
		plHead = new Polyline(arrowPoints);
		plHead.setStroke(Color.BLACK);
		plHead.setStrokeWidth(2);

		double moveX = (Math.cos(Math.atan(-slope)) * 10);
		double moveY = (Math.abs(Math.sin(Math.atan(-slope)) * 10));

		if (width > 0) {
			plHead.setLayoutX(-moveX);
		} else if (width < 0) {
			plHead.setLayoutX(moveX);
		}

		if (height > 0) {
			plHead.setLayoutY(-moveY);
		} else if (height < 0) {
			plHead.setLayoutY(moveY);
		}

		line = new Line(startX, startY, endX, endY);
		line.setStrokeWidth(2);
		line.getStrokeDashArray().addAll(2.0, 5.0);

		// Looks nice to show / hide blue bubble
		plHead.setOnMouseExited(event -> {
			pivot.setFill(Color.TRANSPARENT);
		});

		plHead.setOnMouseEntered(event -> {
			pivot.setFill(Color.LIGHTCYAN);
		});

		makeDraggable();
		group.getChildren().addAll(line, plHead);

		line.setOnMouseEntered(event -> {
			line.setStroke(Color.INDIANRED);
		});

		line.setOnMouseExited(event -> {
			line.setStroke(Color.BLACK);
		});

	}

	// Getters and setters

	/**
	 * Returns type of this Relationship as a String.
	 * 
	 * @return relType of this Relationship
	 */
	public String getRelType() {
		return relType;
	}

	/**
	 * Returns X value of beginning point of this Relationship.
	 * 
	 * @return startXValue
	 */
	public double getStartXValue() {
		return startXValue;
	}

	/**
	 * Returns value of beginning point of this Relationship.
	 * 
	 * @return startYValue
	 */
	public double getStartYValue() {
		return startYValue;
	}

	/**
	 * Returns value of end point of this Relationship.
	 * 
	 * @return endXValue
	 */
	public double getEndXValue() {
		return endXValue;
	}

	/**
	 * Returns value of end point of this Relationship.
	 * 
	 * @return endYValue
	 */
	public double getEndYValue() {
		return endYValue;
	}

	// Adds Listeners to line to enable dragging feature.
	private void makeDraggable() {
		// Offset from initial click needed to properly update heads. not 100% sure why.
		this.line.setOnMouseClicked(eventClicked -> {
			startingPointX = eventClicked.getSceneX();
			startingPointY = eventClicked.getSceneY();

		});
		// While dragging update line + head
		this.line.setOnMouseDragged(eventDragged -> {
			double dragX = checkBoundsX(eventDragged.getX());
			double dragY = checkBoundsY(eventDragged.getY());
			updateRel(dragX, dragY, dragX + (endXValue - startXValue), dragY + (endYValue - startYValue));
			makeNewCircle(dragX, dragY, dragX + (endXValue - startXValue), dragY + (endYValue - startYValue));
			eventDragged.consume();
			startingPointX = line.getStartX();
			startingPointY = line.getStartY();

			line.setStroke(Color.INDIANRED);
		});

	}

	// Updates line + calls appropriate method for head.
	public void updateRel(double startX, double startY, double endX, double endY) {

		double height = endYValue - startYValue;
		double width = endXValue - startXValue;
		double slope = height / width;

		// Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);

		double angle = Math.atan(slope);
		// what are these?
		double l4x, l4y;

		// Parallel angles*length
		l4x = Math.cos(angle) * 10;
		l4y = Math.sin(angle) * 10;

		// If drawn facing left, need to subtract instead of add to get coordinate.
		if (width <= 0) {
			l4x = -l4x;
			l4y = -l4y;
		}
		line.setStartX(startX);
		line.setStartY(startY);
		line.setEndX(endX);
		line.setEndY(endY);

		// Rectangle Head
		if (rHead != null) {
			rHead.setX(endX - 5);
			rHead.setY(endY - 5);
			updateRect(startX, startY, endX, endY);
		}
		// Generalization (triangle)
		if (pHead != null) {
			updateTriangle(startX, startY, endX, endY);
		}
		// Dependency (arrow head)
		if (plHead != null) {
			updateArrow(startX, startY, endX, endY);
		}

		this.startXValue = line.getStartX();
		this.startYValue = line.getStartY();
		this.endXValue = line.getEndX();
		this.endYValue = line.getEndY();

	}

	// Check against *pushing* line out of drawingBox left/right
	private double checkBoundsX(double x) {
		double buffer = 7.5;

		if (endXValue > startXValue) // Right Facing Line
		{
			if (x + (endXValue - startXValue) > UML.drawingBox.getBoundsInParent().getMaxX() - buffer) { // right side
				x = UML.drawingBox.getBoundsInParent().getMaxX() - (endXValue - startXValue) - buffer;
			} else {
				if (x < UML.drawingBox.getBoundsInParent().getMinX() + buffer) { // left side
					x = UML.drawingBox.getBoundsInParent().getMinX() + buffer;
				}
			}
		} else { // Left Facing Line
			if (x + (endXValue - startXValue) <= UML.drawingBox.getBoundsInParent().getMinX() + buffer) { // left side
				x = UML.drawingBox.getBoundsInParent().getMinX() + (startXValue - endXValue) + buffer;
			} else {
				if (x > UML.drawingBox.getBoundsInParent().getMaxX() - buffer) { // right side
					x = UML.drawingBox.getBoundsInParent().getMaxX() - buffer;
				}
			}
		}

		return x;
	}

	// Check against *pushing* line out of drawingBox up/down
	private double checkBoundsY(double y) {
		double buffer = 7.5;
		if (endYValue > startYValue) // Down Facing Line
		{
			if (y + (endYValue - startYValue) > UML.drawingBox.getBoundsInParent().getMaxY() - buffer) { // Bottom
				y = UML.drawingBox.getBoundsInParent().getMaxY() - (endYValue - startYValue) - buffer;
			} else {
				if (y < UML.drawingBox.getBoundsInParent().getMinY() + buffer) { // left side
					y = UML.drawingBox.getBoundsInParent().getMinY() + buffer;
				}
			}
		} else { // Top Facing Line
			if (y + (endYValue - startYValue) <= UML.drawingBox.getBoundsInParent().getMinY() + buffer) { // Top
				y = UML.drawingBox.getBoundsInParent().getMinY() + (startYValue - endYValue) + buffer;
			} else {
				if (y > UML.drawingBox.getBoundsInParent().getMaxY() - buffer) { // right side
					y = UML.drawingBox.getBoundsInParent().getMaxY() - buffer;
				}
			}
		}

		return y;
	}

	// Updates triangle for Generalization
	private void updateTriangle(double startX, double startY, double endX, double endY) {
		double height = endY - startY;
		double width = endX - startX;
		double slope = height / width;

		// Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);

		double angle = Math.atan(slope);
		double l2x, l2y, l4x, l4y;

		// Perpendicular angles*length
		l2x = Math.cos(angle + Math.PI / 2) * 7.5;
		l2y = Math.sin(angle + Math.PI / 2) * 7.5;

		// Parallel angles*length
		l4x = Math.cos(angle) * 10;
		l4y = Math.sin(angle) * 10;

		// If drawn facing left, need to subtract instead of add to get coordinate.
		if (width <= 0) {
			l4x = -l4x;
			l4y = -l4y;
		}

		// Make Triangle w/ points + set attributes
		Double[] triPoints = new Double[6];
		triPoints[0] = (l2x) + endX;
		triPoints[1] = (l2y) + endY;
		triPoints[2] = -(l2x) + endX;
		triPoints[3] = -(l2y) + endY;
		triPoints[4] = (l4x) + endX;
		triPoints[5] = (l4y) + endY;

		pHead.getPoints().setAll(triPoints);

		double moveX = (Math.cos(Math.atan(-slope)) * 10);
		double moveY = (Math.abs(Math.sin(Math.atan(-slope)) * 10));

		if (width > 0) {
			pHead.setLayoutX(-moveX);
		} else if (width < 0) {
			pHead.setLayoutX(moveX);
		}

		if (height > 0) {
			pHead.setLayoutY(-moveY);
		} else if (height < 0) {
			pHead.setLayoutY(moveY);
		}

	}

	// Updates arrow head for dependency
	private void updateArrow(double startX, double startY, double endX, double endY) {
		double height = endY - startY;
		double width = endX - startX;
		double slope = height / width;

		// Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);

		double angle = Math.atan(slope);
		double l2x, l2y, l4x, l4y;

		// Perpendicular angles*length
		l2x = Math.cos(angle + Math.PI / 2) * 7.5;
		l2y = Math.sin(angle + Math.PI / 2) * 7.5;

		// Parallel angles*length
		l4x = Math.cos(angle) * 10;
		l4y = Math.sin(angle) * 10;

		// If drawn facing left, need to subtract instead of add to get coordinate.
		if (width <= 0) {
			l4x = -l4x;
			l4y = -l4y;
		}

		// Make Triangle w/ points + set attributes
		Double[] arrowPoints = new Double[6];
		arrowPoints[0] = (l2x) + endX;
		arrowPoints[1] = (l2y) + endY;
		arrowPoints[2] = (l4x) + endX;
		arrowPoints[3] = (l4y) + endY;
		arrowPoints[4] = -(l2x) + endX;
		arrowPoints[5] = -(l2y) + endY;

		plHead.getPoints().setAll(arrowPoints);

		double moveX = (Math.cos(Math.atan(-slope)) * 10);
		double moveY = (Math.abs(Math.sin(Math.atan(-slope)) * 10));

		if (width > 0) {
			plHead.setLayoutX(-moveX);
		} else if (width < 0) {
			plHead.setLayoutX(moveX);
		}
		if (height > 0) {
			plHead.setLayoutY(-moveY);
		} else if (height < 0) {
			plHead.setLayoutY(moveY);
		}
	}

	// Updates rectangle arrowhead for Aggregation/Composition
	private void updateRect(double startX, double startY, double endX, double endY) {

		double height = endY - startYValue;
		double width = endX - startXValue;

		double slope = height / width;

		// Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);

		rHead.setRotate(Math.toDegrees(Math.atan(height / width)) + 45);

		double moveX = (Math.cos(Math.atan(-slope)) * 5 * Math.sqrt(2));
		double moveY = (Math.abs(Math.sin(Math.atan(-slope)) * 5 * Math.sqrt(2)));

		if (width > 0) {
			rHead.setLayoutX(-moveX);
		} else if (width < 0) {
			rHead.setLayoutX(moveX);
		}

		if (height > 0) {
			rHead.setLayoutY(-moveY);
		} else if (height < 0) {
			rHead.setLayoutY(moveY);
		}
	}

	// Really should be called UpdateCircle..
	// Creates new pivot circle if needed, otherwise just updates.
	private void makeNewCircle(double startX, double startY, double endX, double endY) {
		double height = endY - startY;
		double width = endX - startX;
		double slope = height / width;

		// Diving by 0 is bad
		slope = (slope == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE : slope);
		slope = (slope == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : slope);

		double angle = Math.atan(slope);
		double l2x, l2y, l4x, l4y;

		// Perpendicular angles*length
		l2x = Math.cos(angle + Math.PI / 2) * 7.5;
		l2y = Math.sin(angle + Math.PI / 2) * 7.5;

		// Parallel angles*length
		l4x = Math.cos(angle) * 10;
		l4y = Math.sin(angle) * 10;

		// If drawn facing left, need to subtract instead of add to get coordinate.
		if (width <= 0) {
			l4x = -l4x;
			l4y = -l4y;
		}
		if (this.pivot == null) {
			this.pivot = new Circle();
			this.pivot.setRadius(10);
			this.pivot.setFill(Color.LIGHTCYAN);
		}
		this.pivot.setCenterX(endX - l4x);
		this.pivot.setCenterY(endY - l4y);

	}

	// Creates Listener for blue circle (pivot)
	private void makeRotatable() {
		pivot.setOnMouseDragged(event -> {
			pivot.setFill(Color.LIGHTCYAN);
			// Buffer to the edge of drawingBox, since arrow heads stick out sometimes.
			double buffer = 7.5;
			double endX = event.getSceneX();
			double endY = event.getSceneY();

			// Bounds checking (can't use methods because don't have to worry about length
			// , just dragging one side.
			if (event.getSceneX() < UML.drawingBox.getBoundsInParent().getMinX() + buffer) {
				endX = UML.drawingBox.getBoundsInParent().getMinX() + buffer;
			} else if (event.getSceneX() > UML.drawingBox.getBoundsInParent().getMaxX() - buffer) {
				endX = UML.drawingBox.getBoundsInParent().getMaxX() - buffer;
			}
			if (event.getSceneY() < UML.drawingBox.getBoundsInParent().getMinY() + buffer) {
				endY = UML.drawingBox.getBoundsInParent().getMinY() + buffer;
			} else if (event.getSceneY() > UML.drawingBox.getBoundsInParent().getMaxY() - buffer) {
				endY = UML.drawingBox.getBoundsInParent().getMaxY() - buffer;
			}

			updateRel(startXValue, startYValue, endX, endY);

			this.startXValue = line.getStartX();
			this.startYValue = line.getStartY();
			this.endXValue = line.getEndX();
			this.endYValue = line.getEndY();

			if (pHead != null) {
				updateTriangle(line.getStartX(), line.getStartY(), endXValue, endYValue);
			}
			if (plHead != null) {
				updateArrow(this.startXValue, this.startYValue, line.getEndX(), line.getEndY());
			}
			// Update Circle
			makeNewCircle(startXValue, startYValue, endXValue, endYValue);

		});
		// Visually hide blue circle when mouse isn't on it.
		pivot.setOnMouseExited(event -> {
			pivot.setFill(Color.TRANSPARENT);
		});
		// Keep blue circle steadily visible while dragging.
		pivot.setOnMouseEntered(event -> {
			pivot.setFill(Color.LIGHTCYAN);
		});

	}

	// Returns String representation of Relationship
	// "/" == Field delimiter
	// "~~~~" == Object delimiter for save file
	public String whereAmI() {
		return ("Relationship/" + relType + "/" + startXValue + "/" + startYValue + "/" + endXValue + "/" + endYValue
				+ "~~~~");
	}

}