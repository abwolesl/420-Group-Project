
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.Group;

public class TextBox {

	private double startX, startY;
	private static double width;
	private static double height;

	// Rectangle area where user can click & drag to move this Class Box.
	private static Rectangle dragArea;

	// TextArea parts of ClassBox
	private static TextArea textArea;

	// Rectangle area where user can click & drag to resize this Class Box.
	private static Rectangle resizeArea;

	/**
	 * ClassBox Constructor. Initializes all parts including dragArea and resizeArea
	 * & draws class box.
	 */
	public TextBox() {

		this.startX = 300;
		this.startY = 200;
		this.width = 130;
		this.height = 100;

		updatedragArea();
		updateResizeArea();
		updateBoxes(startX, startY, width, height);
		makeResizable();
		makeDraggable();
	}

	/**
	 * Constructor with passed in parameters to specify location and shape.
	 * 
	 * @param startX
	 *            X value of top left of this ClassBox.
	 * @param startY
	 *            Y value of top left of this ClassBox.
	 * @param width
	 *            Width of this ClassBox.
	 * @param height
	 *            Height of this ClassBox.
	 */
	public TextBox(double startX, double startY, double width, double height) {

		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;

		this.dragArea = new Rectangle(startX, startY, width, height);
		dragArea.setStrokeWidth(2);
		// dragArea.setStroke(Color.RED);
		dragArea.setFill(Color.TRANSPARENT);

		updateBoxes(this.startX, this.startY, this.width, height);
		updatedragArea();
		updateResizeArea();
		makeResizable();
		makeDraggable();
	}

	// Method to update Rectangles within ClassBox.
	// Called whenever the model needs to be updated, like when it's dragged or
	// resized.
	// Also gets called when ClassBox is created.
	/**
	 * Updates the boxes within this ClassBox to necessary location and size.
	 * 
	 * @param startX
	 *            X value to make new X value of top left coordinate of this
	 *            ClassBox.
	 * @param startY
	 *            Y value to make new Y value of top-left coordinate of this
	 *            ClassBox.
	 * @param width
	 *            Width value to make new width of this ClassBox.
	 * @param height
	 *            Height value to make new height of this ClassBox.
	 */
	public void updateBoxes(double startX, double startY, double width, double height) {

		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;

		if (width < -1) {
			this.width = -width;
			this.startX = startX - width;
		}
		if (height < -1) {
			this.height = -height;
			this.startY = startY - height;
		}

		// Set Min height + width
		if (height < 100) {
			this.height = 100;
		}
		if (width < 130) {
			this.width = 130;
		}

		if (textArea == null) {
			textArea = new TextArea();
			// textArea.setMinSize(100, 50);
			textArea.setWrapText(true);
			textArea.setPrefSize(width, height);
			textArea.setTranslateX(startX);
			textArea.setTranslateY(startY);
		}
	}

	// Draws the ClassBox inside the group.
	// This is the round-about way that I'm drawing the ClassBox, but it works.
	/**
	 * Draws this ClassBox on the screen.
	 * 
	 * @param g
	 *            The Group in which this ClassBox is placed.
	 */
	public void drawMe(Group g) {
		g.getChildren().addAll(dragArea, resizeArea, textArea);
		// g.getChildren().addAll(rTop, rMid, rBot, tTop, tMid, tBot);
		// dragArea.setVisible(false);
		// resizeArea.setVisible(false);
		UML.setUserClicked(false);
	}

	// Getter X value of Top left coordinate
	/**
	 * Returns this ClassBox's startX field.
	 * 
	 * @return This ClassBox's startX field.
	 */
	public double getStartX() {
		return this.startX;
	}

	// Getter Y value of Top left coordinate
	/**
	 * Returns this ClassBox's startX field.
	 * 
	 * @return This ClassBox's startY field.
	 */
	public double getStartY() {
		return this.startY;
	}

	// Helper function for makeDraggable
	private void setStartX(double x) {
		this.startX = x;
		updateBoxes(x, startY, width, height);
	}

	// Helper function for makeDraggable
	private void setStartY(double y) {
		this.startY = y;
		updateBoxes(startX, y, width, height);
	}

	// Helper function for makeResizable
	// Does the box logic that happens a lot to ensure minimum size, correct
	// orientation.
	// Maybe integrate with setEndY
	private void setEndX(double x) {
		width = x - startX;

		// logic to "reverse" box to proper orientation if necessary
		if (width < -1) {
			this.width = -width;
			this.startX = startX - width;
		}
		if (width < 130) {
			this.width = 130;
		}

		updateBoxes(startX, startY, width, height);
	}

	// Helper function for makeResizable
	// Does the box logic that happens a lot to ensure minimum size, correct
	// orientation.
	// Maybe integrate with setEndX
	private void setEndY(double y) {
		height = y - startY;

		// logic to "reverse" box to proper orientation if necessary
		if (height < -1) {
			height = -height;
			startY = startY - height;
		}
		if (height < 120) {
			height = 130;
		}

		updateBoxes(startX, startY, width, height);
	}

	// Creates mouse listener for dragArea (Red outline around ClassBox)
	private void makeDraggable() {
		this.dragArea.setOnMouseDragged(eventDragged -> {
			dragArea.setStroke(Color.RED);
			resizeArea.setFill(Color.GREEN);
			this.setStartX(checkBoundsX(eventDragged.getSceneX(), dragArea));
			this.setStartY(checkBoundsY(eventDragged.getSceneY(), dragArea));
			updatedragArea();
			updateResizeArea();
			eventDragged.consume();
		});
	}

	// Creates mouse listener for resizeArea (Green square at bottom right)
	private void makeResizable() {
		this.resizeArea.setOnMouseDragged(eventDragged -> {
			dragArea.setStroke(Color.RED);
			resizeArea.setFill(Color.GREEN);
			this.setEndX(checkBoundsX(eventDragged.getSceneX(), resizeArea));
			this.setEndY(checkBoundsY(eventDragged.getSceneY(), resizeArea));
			updatedragArea();
			updateResizeArea();
			eventDragged.consume();
		});
	}

	// Updates resizeArea to match ClassBox location.
	private void updateResizeArea() {
		if (resizeArea == null) {
			this.resizeArea = new Rectangle(startX + width, startY + height, 7.5, 7.5);
			resizeArea.setFill(Color.GREEN);
			resizeArea.setOpacity(50);
		}
		resizeArea.setX(startX + width);
		resizeArea.setY(startY + height);
	}

	// Updates dragArea to match ClassBox location.
	private void updatedragArea() {
		if (this.dragArea == null) {
			this.dragArea = new Rectangle(startX - 7.5, startY - 7.5, width + 15, height + 15);
			dragArea.setStroke(Color.RED);
			dragArea.setFill(Color.TRANSPARENT);
			dragToggle();
		}
		dragArea.setX(this.startX - 7.5);
		dragArea.setY(this.startY - 7.5);
		dragArea.setHeight(this.height + 15);
		dragArea.setWidth(this.width + 15);
	}

	// Bounds checking for X parameter for dragging and Resizing.
	// Logic differentiates between dragging and resizing, since you can drag from
	// anywhere, but only resize from bottom right.
	private double checkBoundsX(double x, Rectangle r) {
		if (x < UML.drawingBox.getBoundsInParent().getMinX() + 7.5) { // left side of gray area
			x = UML.drawingBox.getBoundsInParent().getMinX() + 7.5;
		}
		if (r.equals(resizeArea)) {
			if (x > UML.drawingBox.getBoundsInParent().getMaxX() - 7.5) {
				x = UML.drawingBox.getBoundsInParent().getMaxX() - 7.5;
			}
		} else {
			if (x + this.width > UML.drawingBox.getBoundsInParent().getMaxX() - 7.5) { // right side of gray area
				x = UML.drawingBox.getBoundsInParent().getMaxX() - 7.5 - this.width;
			}
		}

		return x;
	}

	// Bounds checking for Y parameter for dragging and Resizing.
	// Logic differentiates between dragging and resizing, since you can drag from
	// anywhere, but only resize from bottom right.
	private double checkBoundsY(double y, Rectangle r) {
		if (y < UML.drawingBox.getBoundsInParent().getMinY() + 7.5) { // top of gray area
			y = UML.drawingBox.getBoundsInParent().getMinY() + 7.5;
		}

		if (r.equals(resizeArea)) {
			if (y > UML.drawingBox.getBoundsInParent().getMaxY() - 7.5) {
				y = UML.drawingBox.getBoundsInParent().getMaxY() - 7.5;
			}
		} else {
			if (y + this.height > UML.drawingBox.getBoundsInParent().getMaxY() - 7.5) { // right side of gray area
				y = UML.drawingBox.getBoundsInParent().getMaxY() - 7.5 - this.height;
			}
		}

		return y;
	}

	// Toggles visibility of dragArea and resizeArea by setting their colors.
	// Setting .isVisible(false) doesn't allow setOnMouseEntered to activate, I
	// think the object isn't there anymore.
	private void dragToggle() {
		this.dragArea.setOnMouseExited(eventExited -> {
			resizeArea.setFill(Color.TRANSPARENT);
			dragArea.setStroke(Color.TRANSPARENT);

			// Moving from dragArea to resizeArea to keep both visible
			this.resizeArea.setOnMouseEntered(eventEntered -> {
				resizeArea.setFill(Color.GREEN);
				dragArea.setStroke(Color.RED);
			});
		});

		this.dragArea.setOnMouseEntered(eventEntered -> {
			resizeArea.setFill(Color.GREEN);
			dragArea.setStroke(Color.RED);
		});
	}

	/**
	 * Returns height of this ClassBox.
	 * 
	 * @return height This ClassBox's height.
	 */
	public static double getHeight() {
		return height;
	}

	/**
	 * Returns width of this ClassBox.
	 * 
	 * @return width This ClassBox's height.
	 */
	public static double getWidth() {
		return width;
	}

	/**
	 * Returns text as string currently located in specified TextArea.
	 * 
	 * @param t
	 *            TextArea to get text from.
	 * @return String Text value of Textbox.
	 */
	public static String getText(TextArea t) {
		return t.getText();
	}
}