
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TextBox {

	private double startX, startY;
	private double width;
	private double height;

	// Rectangle area where user can click & drag to move this Text Box.
	private Rectangle dragArea;

	// TextArea
	private TextArea textBox;

	// Rectangle area where user can click & drag to resize this Text Box.
	private Rectangle resizeArea;

	// Rectangle area where user can click to remove the Text Box.
	private Rectangle deleteArea;

	private boolean why;
	
	/**
	 * TextBox Constructor. Initializes all parts including dragArea, resizeArea,
	 * and deleteArea
	 * 
	 */
	public TextBox() {

		startX = 300;
		startY = 300;
		width = 50;
		height = 50;

		updateDragArea();
		updateResizeArea();
		updateDeleteArea();
		updateBox(startX, startY, width, height);
		makeResizable();
		makeDraggable();
		makeDeletable();
	}

	/**
	 * Constructor with passed in parameters to specify location and shape.
	 * 
	 * @param startX
	 *            X value of top left of this TextBox.
	 * @param startY
	 *            Y value of top left of this TextBox.
	 * @param width
	 *            Width of this TextBox.
	 * @param height
	 *            Height of this TextBox.
	 * @param text
	 *            Initial text of this TextBox
	 */
	public TextBox(double startX, double startY, double width, double height, String text) {
		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;

		updateBox(this.startX, this.startY, this.width, this.height);

		updateDragArea();
		updateResizeArea();
		updateDeleteArea();
		makeResizable();
		makeDraggable();
		makeDeletable();

		this.textBox.setText(text);
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
	public void updateBox(double startX, double startY, double width, double height) {

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

		// Setting min and max dimensions is just a design choice so user can't draw
		// a text box to take up the entire drawing scene.
		// This could be changed later.

		// Set min height and width
		if (height < 50) {
			this.height = 50;
		}
		if (width < 50) {
			this.width = 50;
		}

		// Set max height and width
		if (height > 170) {
			this.height = 170;
		}
		if (width > 200) {
			this.width = 200;
		}

		if (textBox == null) {
			textBox = new TextArea();
		}
		textBox.setTranslateX(this.startX);
		textBox.setTranslateY(this.startY);
		textBox.setMaxWidth(this.width);
		textBox.setMaxHeight(this.height);
		textBox.setFont(Font.font("Verdana", 12));
	}

	// Draws the ClassBox inside the group.
	// This is the round-about way that I'm drawing the ClassBox, but it works.
	/**
	 * Draws this ClassBox on the screen.
	 * 
	 * @param g
	 *            The Group in which this ClassBox is placed.
	 */
	public void drawMe(Pane pane) {
		pane.getChildren().addAll(dragArea, resizeArea, deleteArea, textBox);
		UML.setUserClicked(false);
	}

	// Getter X value of Top left coordinate
	/**
	 * Returns this ClassBox's startX field.
	 * 
	 * @return This ClassBox's startX field.
	 */
	public double getStartX() {
		return startX;
	}

	// Getter Y value of Top left coordinate
	/**
	 * Returns this ClassBox's startX field.
	 * 
	 * @return This ClassBox's startX field.
	 */
	public double getStartY() {
		return startY;
	}

	/**
	 * Helper function for makeDraggable
	 * 
	 * @param x
	 *            Starting X coordinate of where the text box will be dragged
	 */
	private void setStartX(double x) {
		startX = x;
		updateBox(x, startY, width, height);
	}

	/**
	 * Helper function for makeDraggable
	 * 
	 * @param y
	 *            Starting Y coordinate of where the text box will be dragged
	 */
	private void setStartY(double y) {
		startY = y;
		updateBox(startX, y, width, height);
	}

	/**
	 * Helper function for makeResizable. Does the box logic that happens a lot to
	 * ensure minimum size, correct orientation.
	 * 
	 * @param x
	 *            The horizontal bounds of the resize area
	 */
	private void setEndX(double x) {
		width = x - startX;

		// logic to "reverse" box to proper orientation if necessary
		if (width < -1) {
			width = -width;
			startX = startX - width;
		}
		if (width < 50) {
			width = 50;
		}

		updateBox(startX, startY, width, height);
	}

	/**
	 * Helper function for makeResizable. Does the box logic that happens a lot to
	 * ensure minimum size, correct orientation.
	 * 
	 * @param y
	 *            The vertical bounds of the resize area
	 */
	private void setEndY(double y) {
		height = y - startY;

		// logic to "reverse" box to proper orientation if necessary
		if (height < -1) {
			height = -height;
			startY = startY - height;
		}
		if (height < 50) {
			height = 50;
		}

		updateBox(startX, startY, width, height);
	}

	/**
	 * Creates mouse listener for dragArea (Red outline around TextBox)
	 */
	private void makeDraggable() {
		dragArea.setOnMouseDragged(eventDragged -> {
			dragArea.setStroke(Color.RED);
			resizeArea.setFill(Color.GREEN);
			deleteArea.setFill(Color.RED);
			setStartX(checkBoundsX(eventDragged.getSceneX(), dragArea));
			setStartY(checkBoundsY(eventDragged.getSceneY(), dragArea));
			updateDragArea();
			updateResizeArea();
			updateDeleteArea();
			eventDragged.consume();
		});
	}

	/**
	 * Creates mouse listener for resizeArea (Green square at bottom right corner of
	 * drag area)
	 */
	private void makeResizable() {
		resizeArea.setOnMouseDragged(eventDragged -> {
			dragArea.setStroke(Color.RED);
			resizeArea.setFill(Color.GREEN);
			deleteArea.setFill(Color.RED);
			setEndX(checkBoundsX(eventDragged.getSceneX(), resizeArea));
			setEndY(checkBoundsY(eventDragged.getSceneY(), resizeArea));
			updateDragArea();
			updateResizeArea();
			updateDeleteArea();

			eventDragged.consume();
		});
		
		resizeArea.setOnMouseEntered(eventEntered -> {
			resizeArea.setFill(Color.GREEN);
			dragArea.setStroke(Color.RED);
			deleteArea.setFill(Color.RED);
		});

		// Visually hides (not to system) when not "active"
		resizeArea.setOnMouseExited(eventExited -> {
			resizeArea.setFill(Color.TRANSPARENT);
			deleteArea.setFill(Color.TRANSPARENT);
			dragArea.setStroke(Color.TRANSPARENT);
		});
	}

	/**
	 * Creates mouse listener for deleteArea (Red square at top left corner of drag
	 * area)
	 */
	private void makeDeletable() {
		this.deleteArea.setOnMouseClicked(event -> {
			removeClassBox();
			deleteArea.setVisible(false);
			why = true;
			event.consume();
		
		});

		// Moving from dragArea to resizeArea to keep both visible
		deleteArea.setOnMouseEntered(eventEntered -> {
			resizeArea.setFill(Color.GREEN);
			deleteArea.setFill(Color.RED);
			dragArea.setStroke(Color.RED);
		});

		// Visually hides (not to system) when not "active"
		deleteArea.setOnMouseExited(eventExited -> {
			resizeArea.setFill(Color.TRANSPARENT);
			deleteArea.setFill(Color.TRANSPARENT);
			dragArea.setStroke(Color.TRANSPARENT);
		});
	}

	/**
	 * Updates resizeArea to match ClassBox location.
	 */
	private void updateResizeArea() {
		if (resizeArea == null) {
			resizeArea = new Rectangle(startX + width, startY + height, 7.5, 7.5);
			resizeArea.setFill(Color.TRANSPARENT);
			resizeArea.setOpacity(50);
		}

		resizeArea.setX(startX + (dragArea.getWidth() - 15));
		resizeArea.setY(startY + (dragArea.getHeight() - 15));
	}

	/**
	 * Updates deleteArea to match ClassBox location.
	 */
	private void updateDeleteArea() {
		if (deleteArea == null) {
			deleteArea = new Rectangle(startX - 7, startY - 7, 7.5, 7.5);
			deleteArea.setFill(Color.TRANSPARENT);
			deleteArea.setOpacity(50);
		}
		deleteArea.setX(startX - 7);
		deleteArea.setY(startY - 7);
	}

	/**
	 * Updates dragArea to match ClassBox location.
	 */
	private void updateDragArea() {
		if (dragArea == null) {
			dragArea = new Rectangle(startX - 7.5, startY - 7.5, width + 15, height + 15);
			dragArea.setStroke(Color.TRANSPARENT);
			dragArea.setFill(Color.TRANSPARENT);
			dragToggle();
		}
		dragArea.setX(this.startX - 7.5);
		dragArea.setY(this.startY - 7.5);

		dragArea.setHeight(this.height + 15);
		dragArea.setWidth(this.width + 15);
	}

	/**
	 * Bounds checking for X parameter for dragging and Resizing. Logic
	 * differentiates between dragging and resizing, since you can drag from
	 * anywhere, but only resize from bottom right.
	 * 
	 * @param x
	 *            The X coordinate of the rectangle
	 * @param r
	 *            The rectangle to be checked
	 * @return The X coordinate of the rectangle
	 */
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

	/**
	 * Bounds checking for Y parameter for dragging and Resizing. Logic
	 * differentiates between dragging and resizing, since you can drag from
	 * anywhere, but only resize from bottom right.
	 * 
	 * @param y
	 *            The Y coordinate of the rectangle
	 * @param r
	 *            The rectangle to be checked
	 * @return The Y coordinate of the rectangle
	 */
	private double checkBoundsY(double y, Rectangle r) {
		if (y < UML.drawingBox.getBoundsInParent().getMinY() + 7.5) { // top of gray area
			y = UML.drawingBox.getBoundsInParent().getMinY() + 7.5;
		}

		if (r.equals(resizeArea)) {
			if (y > UML.drawingBox.getBoundsInParent().getMaxY() - 7.5) {
				y = UML.drawingBox.getBoundsInParent().getMaxY() - 7.5;
			}
		} else {
			if (y + this.height > UML.drawingBox.getBoundsInParent().getMaxY() - 7.5) { // bottom of gray area
				y = UML.drawingBox.getBoundsInParent().getMaxY() - 7.5 - this.height;
				/*
				 * // Because user is trying to draw at bottom, should allow the drawing scene
				 * to grow Stage UMLStage = UML.getStage(); UMLStage.setResizable(true);
				 * UMLStage.setHeight(UMLStage.getHeight() + 100);
				 */
			}
		}

		return y;
	}

	/**
	 * Toggles visibility of dragArea and resizeArea by setting their colors.
	 * Setting .isVisible(false) doesn't allow setOnMouseEntered to activate, I
	 * think the object isn't there anymore.
	 */
	private void dragToggle() {
		// Fixed weird bug where if you released while moving fast, aura would persist
		dragArea.setOnMouseDragReleased(eventDone -> {
			resizeArea.setFill(Color.TRANSPARENT);
			deleteArea.setFill(Color.TRANSPARENT);
			dragArea.setStroke(Color.TRANSPARENT);
		});
		// Fixed weird bug where if you released while moving fast, aura would persist
		// ^^ not sure if both are necessary
		dragArea.setOnMouseReleased(eventDone -> {
			resizeArea.setFill(Color.TRANSPARENT);
			deleteArea.setFill(Color.TRANSPARENT);
			dragArea.setStroke(Color.TRANSPARENT);
		});
		// Visually hides (not to system) when not "active"
		dragArea.setOnMouseExited(eventExited -> {
			resizeArea.setFill(Color.TRANSPARENT);
			deleteArea.setFill(Color.TRANSPARENT);
			dragArea.setStroke(Color.TRANSPARENT);
		});

		// Moving from dragArea to resizeArea to keep everything visible
		dragArea.setOnMouseEntered(eventEntered -> {
			resizeArea.setFill(Color.GREEN);
			deleteArea.setFill(Color.RED);
			dragArea.setStroke(Color.RED);
		});

	}

	/**
	 * Returns height of this ClassBox.
	 * 
	 * @return height This ClassBox's height.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Returns width of this ClassBox.
	 * 
	 * @return width This ClassBox's height.
	 */
	public double getWidth() {
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

	/**
	 * Removes all the children from the object
	 */
	public void removeClassBox() {
		Pane pane = UML.getPane();
		pane.getChildren().removeAll(dragArea, resizeArea, textBox);
	}

	/**
	 * Returns String representation of TextBox "/" == Field delimiter 
	 * "~~~~" == Object delimiter for save file
	 * 
	 * @return Info about the location of the object
	 */
	public String whereAmI() {
		String text = "PLACEHOLDER";
		if (!textBox.getText().equals("")) {
			text = textBox.getText();
		}
		return ("TEXTBOX/" + startX + "/" + startY + "/" + width + "/" + height + "/" + text + "~~~~");
	}
	
	/**
	 * Return whether or not ClassBox is "deleted"
	 * @return boolean true if deleted
	 */
	public boolean isDeleted() {
		return why == true;
	}
}