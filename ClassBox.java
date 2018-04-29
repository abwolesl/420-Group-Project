
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.Group;

public class ClassBox {

	private double startX, startY;
	private double width;
	private double height;

	// Rectangle area where user can click & drag to move this Class Box.
	private Rectangle dragArea;

	// Rectangle parts of ClassBox
	private Rectangle rTop, rMid, rBot;

	// TextArea parts of ClassBox
	private TextArea tTop, tMid, tBot;

	// Rectangle area where user can click & drag to resize this Class Box.
	private Rectangle resizeArea;

	// Rectangle area where user can click to remove the Class Box.
	private Rectangle deleteArea;

	/**
	 * ClassBox Constructor. Initializes all parts including dragArea, resizeArea,
	 * and deleteArea & draws class box.
	 */
	public ClassBox() {

		startX = 300;
		startY = 300;
		width = 130;
		height = 130;

		updateDeleteArea();
		updateDragArea();
		updateResizeArea();
		updateBoxes(startX, startY, width, height);
		updateTextAreas(startX, startY, width, height);
		makeDeletable();
		makeResizable();
		makeDraggable();
		showAura();
		// resizeInternals();

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
	public ClassBox(double startX, double startY, double width, double height, String Top, String Mid, String Bot) {

		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;

		// this.dragArea = new Rectangle(startX, startY, width, height);
		// dragArea.setStrokeWidth(2);
		// dragArea.setStroke(Color.RED);
		// dragArea.setFill(Color.TRANSPARENT);
		updateDeleteArea();
		updateDragArea();
		updateBoxes(this.startX, this.startY, this.width, height);
		updateTextAreas(this.startX, this.startY, this.width, height);
		tTop.setText(Top);
		tMid.setText(Mid);
		tBot.setText(Bot);
		makeDeletable();

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
		if (height < 120) {
			this.height = 130;
		}
		if (width < 130) {
			this.width = 130;
		}
		// Each section of box is a third.
		double ythird = this.height / 3.0;

		if (rTop == null) {
			rTop = new Rectangle(this.startX, this.startY, this.width, ythird);
			rTop.setFill(Color.WHITE);
			rTop.setStroke(Color.BLACK);
			rTop.setStrokeWidth(2);
		}
		rTop.setX(this.startX);
		rTop.setY(this.startY);
		rTop.setWidth(this.width);
		rTop.setHeight(40);

		if (rMid == null) {
			rMid = new Rectangle(this.startX, this.startY + ythird, this.width, ythird);
			rMid.setFill(Color.WHITE);
			rMid.setStroke(Color.BLACK);
			rMid.setStrokeWidth(2);
		}
		rMid.setX(this.startX);
		rMid.setY(this.startY + 40);
		rMid.setWidth(this.width);
		rMid.setHeight((this.height - 40) / 2);

		if (rBot == null) {
			rBot = new Rectangle(this.startX, this.startY + 2 * ythird, this.width, ythird);
			rBot.setFill(Color.WHITE);
			rBot.setStroke(Color.BLACK);
			rBot.setStrokeWidth(2);
		}
		rBot.setX(this.startX);
		rBot.setY(rMid.getY() + rMid.getHeight());
		rBot.setWidth(this.width);
		rBot.setHeight(rMid.getHeight());
	}

	// Method to update TextAreas within ClassBox.
	// Called whenever the model needs to be updated, like when it's dragged or
	// resized.
	// Also gets called when ClassBox is created.
	/**
	 * Updates the TextAreas within this ClassBox to necessary location and size.
	 * 
	 * @param startX
	 *            X value to make new X value of top left coodinate of this
	 *            ClassBox.
	 * @param startY
	 *            Y value to make new Y value of top-left coordinate of this
	 *            ClassBox.
	 * @param width
	 *            Width value to make new width of this ClassBox.
	 * @param height
	 *            Height value to make new height of this ClassBox.
	 */
	public void updateTextAreas(double startX, double startY, double width, double height) {

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
			height = 130;
		}
		if (width < 130) {
			width = 130;
		}
		// Each section of box is a third.
		double ythird = height / 3.0;
		if (tTop == null) {
			tTop = new TextArea();
		}
		tTop.setLayoutX(startX + 1);
		tTop.setLayoutY(startY + 1);
		tTop.setPrefHeight(40 - 2);
		tTop.setPrefWidth(width - 2);

		if (tMid == null) {
			tMid = new TextArea();
		}
		tMid.setLayoutX(startX + 1);
		tMid.setLayoutY(rMid.getY() + 1);
		tMid.setPrefHeight(rMid.getHeight() - 2);
		tMid.setPrefWidth(width - 2);

		if (tBot == null) {
			tBot = new TextArea();
		}
		tBot.setLayoutX(startX + 1);
		tBot.setLayoutY(rBot.getY() + 1);
		tBot.setPrefHeight(rBot.getHeight() - 2);
		tBot.setPrefWidth(width - 2);
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
		g.getChildren().addAll(dragArea, resizeArea, deleteArea, rTop, rMid, rBot, tTop, tMid, tBot);
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
	 * @return This ClassBox's startY field.
	 */
	public double getStartY() {
		return startY;
	}

	// Helper function for makeDraggable
	private void setStartX(double x) {
		startX = x;
		updateBoxes(x, startY, width, height);
		updateTextAreas(x, startY, width, height);
	}

	// Helper function for makeDraggable
	private void setStartY(double y) {
		startY = y;
		updateBoxes(startX, y, width, height);
		updateTextAreas(startX, y, width, height);
	}

	// Helper function for makeResizable
	// Does the box logic that happens a lot to ensure minimum size, correct
	// orientation.
	// Maybe integrate with setEndY
	private void setEndX(double x) {
		width = x - startX;

		// logic to "reverse" box to proper orientation if necessary
		if (width < -1) {
			width = -width;
			startX = startX - width;
		}
		if (width < 130) {
			width = 130;
		}

		updateBoxes(startX, startY, width, height);
		updateTextAreas(startX, startY, width, height);
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
		updateTextAreas(startX, startY, width, height);
	}

	// Creates mouse listener for dragArea (Red outline around ClassBox)
	private void makeDraggable() {
		this.dragArea.setOnMouseDragged(eventDragged -> {
			dragArea.setStroke(Color.RED);
			resizeArea.setFill(Color.GREEN);
			deleteArea.setFill(Color.RED);
			this.setStartX(checkBoundsX(eventDragged.getSceneX(), dragArea));
			this.setStartY(checkBoundsY(eventDragged.getSceneY(), dragArea));
			updateDragArea();
			updateResizeArea();
			updateDeleteArea();
			eventDragged.consume();
		});
	}

	// Creates mouse listener for resizeArea (Green square at bottom right corner of
	// drag area)
	private void makeResizable() {
		this.resizeArea.setOnMouseDragged(eventDragged -> {
			dragArea.setStroke(Color.RED);
			resizeArea.setFill(Color.GREEN);
			deleteArea.setFill(Color.RED);
			this.setEndX(checkBoundsX(eventDragged.getSceneX(), resizeArea));
			this.setEndY(checkBoundsY(eventDragged.getSceneY(), resizeArea));
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
	}

	// Creates mouse listener for deleteArea (Red square at top left corner of drag
	// area)
	private void makeDeletable() {
		this.deleteArea.setOnMouseClicked(event -> {
			removeClassBox();
			deleteArea.setVisible(false);
			event.consume();
		});

		deleteArea.setOnMouseDragged(eventDone -> {
			System.out.print("HELLO");
			resizeArea.setFill(Color.TRANSPARENT);
			deleteArea.setFill(Color.TRANSPARENT);
			dragArea.setStroke(Color.TRANSPARENT);
		});
	}

	// Updates resizeArea to match ClassBox location.
	private void updateResizeArea() {
		if (resizeArea == null) {
			resizeArea = new Rectangle(startX + width, startY + height, 7.5, 7.5);
			resizeArea.setFill(Color.TRANSPARENT);
			resizeArea.setOpacity(50);
		} else {
			resizeArea.setFill(Color.GREEN);
		}

		resizeArea.setX(startX + width);
		resizeArea.setY(startY + height);
	}

	// Updates deleteArea to match ClassBox location.
	private void updateDeleteArea() {
		if (this.deleteArea == null) {
			this.deleteArea = new Rectangle(startX - 7, startY - 7, 7.5, 7.5);
			this.deleteArea.setFill(Color.TRANSPARENT);
			this.deleteArea.setOpacity(50);
		} else {
			deleteArea.setFill(Color.RED);
		}

		deleteArea.setX(startX - 7);
		deleteArea.setY(startY - 7);
	}

	// Updates dragArea to match ClassBox location.
	private void updateDragArea() {
		if (dragArea == null) {
			dragArea = new Rectangle(startX - 7.5, startY - 7.5, width + 15, height + 15);
			dragArea.setStroke(Color.TRANSPARENT);
			dragArea.setFill(Color.TRANSPARENT);
			dragToggle();
		} else {
			dragArea.setStroke(Color.RED);
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

		dragArea.setOnMouseDragReleased(eventDone -> {
			// System.out.println("HELLO");
			resizeArea.setFill(Color.TRANSPARENT);
			deleteArea.setFill(Color.TRANSPARENT);
			dragArea.setStroke(Color.TRANSPARENT);
		});

		dragArea.setOnMouseReleased(eventDone -> {
			//System.out.println("HELLO");
			resizeArea.setFill(Color.TRANSPARENT);
			deleteArea.setFill(Color.TRANSPARENT);
			dragArea.setStroke(Color.TRANSPARENT);
		});

		dragArea.setOnMouseExited(eventExited -> {
			//System.out.println("hello");
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

		// Moving from dragArea to resizeArea to keep both visible
		deleteArea.setOnMouseEntered(eventEntered -> {
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

	public String whereAmI() {
		String tText = "PLACEHOLDER", mText = "PLACEHOLDER", bText = "PLACEHOLDER";
		if (!tTop.getText().equals("")) {
			tText = tTop.getText();
		}
		if (!tMid.getText().equals("")) {
			mText = tMid.getText();
		}
		if (!tBot.getText().equals("")) {
			bText = tBot.getText();
		}
		return ("CLASSBOX/" + startX + "/" + startY + "/" + width + "/" + height + "/" + tText + "/" + mText + "/"
				+ bText + "~~~~");
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

	private void removeClassBox() {
		Group group = UML.getGroup();
		group.getChildren().removeAll(dragArea, resizeArea, rTop, rMid, rBot, tTop, tMid, tBot);
	}

	public void hideAura() {
		this.dragArea.setVisible(false);
		this.resizeArea.setVisible(false);
		this.deleteArea.setVisible(false);
	}

	public void showAura() {
		this.dragArea.setVisible(true);
		this.resizeArea.setVisible(true);
		this.deleteArea.setVisible(true);
		makeDraggable();
	}

	/*
	 * private void resizeInternals() { rBot.setOnMouseDragged(event -> {
	 * rBot.setY(event.getSceneY()); tBot.setLayoutY(event.getSceneY() + 1);
	 * rMid.setHeight(event.getSceneY() - rMid.getY());
	 * tMid.setPrefHeight(event.getSceneY() - rMid.getY() - 2);
	 * dragArea.setHeight(event.getSceneY() - dragArea.getY() + rBot.getHeight() +
	 * 7.5); resizeArea.setY(event.getSceneY() + rBot.getHeight()); event.consume();
	 * }); }
	 */
}