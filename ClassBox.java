
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.Group;

public class ClassBox {

	 private double startX, startY;
	 private static double width;
	 private static double height;
	 
	 //static boolean isLineBeingDrawn = false;
	 
	 //Rectangle area where user can click & drag to move this Class Box.
	 private static Rectangle dragArea;
	 
	 //Rectangle parts of ClassBox
	 private static Rectangle rTop, rMid, rBot;
	 
	 //TextArea parts of ClassBox
	 private static TextArea tTop, tMid, tBot;
	 
	 //Rectangle area where user can click & drag to resize this Class Box.
	 private static Rectangle resizeArea;
	 
	 private static Rectangle deleteArea;
	 
	 private static boolean clicked = false;
	
	/** ClassBox Constructor. Initializes all parts including dragArea and resizeArea & draws class box. */
	public ClassBox() {

		this.startX = 300;
		this.startY = 300;
		this.width = 130;
		this.height = 130;
		
		updatedragArea();
		updateResizeArea();
		updateDeleteArea();
		updateBoxes(startX, startY, width, height);
		updateTextAreas(startX, startY, width, height);
		makeResizable();
		makeDraggable();
		makeDeletable();
		
	}
	
	
	/** Constructor with passed in parameters to specify location and shape.
	 * @param startX X value of top left of this ClassBox.
	 * @param startY Y value of top left of this ClassBox.
	 * @param width Width of this ClassBox.
	 * @param height Height of this ClassBox.
	 */
	public ClassBox(double startX, double startY, double width, double height){
		
		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;
		
		this.dragArea = new Rectangle(startX, startY, width, height);
		dragArea.setStrokeWidth(2);
		//dragArea.setStroke(Color.RED);
		dragArea.setFill(Color.TRANSPARENT);
		
		updateBoxes(this.startX, this.startY, this.width, height);
		updateTextAreas(this.startX, this.startY, this.width, height);
		
		updatedragArea();
		updateResizeArea();
		updateDeleteArea();
		makeResizable();
		makeDraggable();
		makeDeletable();
	}
	
	//Method to update Rectangles within ClassBox.
	//Called whenever the model needs to be updated, like when it's dragged or resized. 
	//Also gets called when ClassBox is created. 
	/** Updates the boxes within this ClassBox to necessary location and size. 
	 * @param startX X value to make new X value of top left coordinate of this ClassBox.
	 * @param startY Y value to make new Y value of top-left coordinate of this ClassBox.
	 * @param width Width value to make new width of this ClassBox.
	 * @param height Height value to make new height of this ClassBox.
	 */
	public void updateBoxes(double startX, double startY, double width, double height) {
		
		// Rectangle(startX, startY, width, height)GUI
		
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
		rTop.setHeight(ythird);
		
		if (rMid == null) {
			rMid = new Rectangle(this.startX, this.startY + ythird, this.width, ythird);
			rMid.setFill(Color.WHITE);
			rMid.setStroke(Color.BLACK);
			rMid.setStrokeWidth(2);
		}
		rMid.setX(this.startX);
		rMid.setY(this.startY + ythird);
		rMid.setWidth(this.width);
		rMid.setHeight(ythird);

		if (rBot == null) {
			rBot = new Rectangle(this.startX, this.startY + 2 * ythird, this.width, ythird);
			rBot.setFill(Color.WHITE);
			rBot.setStroke(Color.BLACK);
			rBot.setStrokeWidth(2);
		}
		rBot.setX(this.startX);
		rBot.setY(this.startY + 2 * ythird);
		rBot.setWidth(this.width);
		rBot.setHeight(ythird);
	}
	
	//Method to update TextAreas within ClassBox.
	//Called whenever the model needs to be updated, like when it's dragged or resized. 
	//Also gets called when ClassBox is created. 
	/** Updates the TextAreas within this ClassBox to necessary location and size. 
	 * @param startX X value to make new X value of top left coodinate of this ClassBox.
	 * @param startY Y value to make new Y value of top-left coordinate of this ClassBox.
	 * @param width Width value to make new width of this ClassBox.
	 * @param height Height value to make new height of this ClassBox.
	 */
	public void updateTextAreas(double startX, double startY, double width,
			double height) {
		
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
		tTop.setPrefHeight(ythird - 2);
		tTop.setPrefWidth(width - 2);
		
		if (tMid == null) {
			tMid = new TextArea(); 
		}
		tMid.setLayoutX(startX + 1);
		tMid.setLayoutY(startY + ythird + 1);
		tMid.setPrefHeight(ythird - 2);
		tMid.setPrefWidth(width - 2);

		if (tBot == null) {
			tBot = new TextArea(); 
		}
		tBot.setLayoutX(startX + 1);
		tBot.setLayoutY(startY + 2 * ythird + 1);
		tBot.setPrefHeight(ythird - 2);
		tBot.setPrefWidth(width - 2);
	}
	
	
	//Draws the ClassBox inside the group. 
	//This is the round-about way that I'm drawing the ClassBox, but it works.
	/** Draws this ClassBox on the screen.
	 * @param g The Group in which this ClassBox is placed. 
	 */
	public void drawMe(Group g){
		g.getChildren().addAll(dragArea, resizeArea, deleteArea, rTop,rMid,rBot,tTop,tMid,tBot);
		//g.getChildren().addAll(rTop, rMid, rBot, tTop, tMid, tBot);
		//dragArea.setVisible(false);
		//resizeArea.setVisible(false);
		UML.setUserClicked(false);
	}
	
	//Getter X value of Top left coordinate
	/**Returns this ClassBox's startX field. 
	 * @return This ClassBox's startX field.
	 */
	public double getStartX() {
		return this.startX;
	}
	
	//Getter Y value of Top left coordinate
	/**Returns this ClassBox's startX field.
	 * @return This ClassBox's startY field.
	 */
	public double getStartY() {
		return this.startY;
	}
	
	//Helper function for makeDraggable
	private void setStartX(double x) {
		this.startX = x;
		updateBoxes(x, startY, width, height);
		updateTextAreas(x,startY, width, height);
	}
	
	//Helper function for makeDraggable
	private void setStartY(double y) {
		this.startY = y;
		updateBoxes(startX, y, width, height);
		updateTextAreas(startX, y, width, height);
	}
	
	//Helper function for makeResizable
	//Does the box logic that happens a lot to ensure minimum size, correct orientation.
	//Maybe integrate with setEndY
	private void setEndX(double x) {
		width = x - startX;
		
		//logic to "reverse" box to proper orientation if necessary
		if (width < -1) {
			this.width = -width;
			this.startX = startX - width;
		}
		if (width < 130) {
			this.width = 130;
		}
		
		updateBoxes(startX, startY, width, height);
		updateTextAreas(startX, startY, width, height);
	}
	
	//Helper function for makeResizable
	//Does the box logic that happens a lot to ensure minimum size, correct orientation.
	//Maybe integrate with setEndX
	private void setEndY(double y) {
		height = y - startY;
		
		//logic to "reverse" box to proper orientation if necessary
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
	
	//Creates mouse listener for dragArea (Red outline around ClassBox)
	private void makeDraggable(){
		//if (lineBeingDrawn) {
			this.dragArea.setOnMouseDragged(eventDragged -> {
				dragArea.setStroke(Color.RED);
				resizeArea.setFill(Color.GREEN);
				this.setStartX(checkBoundsX(eventDragged.getSceneX(),dragArea));
				this.setStartY(checkBoundsY(eventDragged.getSceneY(),dragArea));
				updatedragArea();
				updateResizeArea();
				eventDragged.consume();
			});
		//}
	}
	
	//Creates mouse listener for resizeArea (Green square at bottom right)
	private void makeResizable(){
		//if (lineBeingDrawn) {
			this.resizeArea.setOnMouseDragged(eventDragged -> {
				dragArea.setStroke(Color.RED);
				resizeArea.setFill(Color.GREEN);
				this.setEndX(checkBoundsX(eventDragged.getSceneX(),resizeArea));
				this.setEndY(checkBoundsY(eventDragged.getSceneY(),resizeArea));
				updatedragArea();
				updateResizeArea();
				eventDragged.consume();
			});
		//}
	}
	
	private void makeDeletable() {
		this.deleteArea.setOnMouseDragged(event -> {
			removeClassBox(UML.getGroup());
			event.consume();
		});
	}
	
	//Updates resizeArea to match ClassBox location.
	private void updateResizeArea() {
		if(resizeArea == null) {
			this.resizeArea = new Rectangle(startX + width, startY + height, 7.5, 7.5);
			resizeArea.setFill(Color.GREEN);
			resizeArea.setOpacity(50);
		}
		resizeArea.setX(startX + width);
		resizeArea.setY(startY + height);
	}
	
	private void updateDeleteArea() {
		if(deleteArea == null) {
			this.deleteArea = new Rectangle(startX - 7, startY - 7, 7.5, 7.5);
			deleteArea.setFill(Color.RED);
			deleteArea.setOpacity(50);
		}
		deleteArea.setX(startX - 7);
		deleteArea.setY(startY - 7);
	}
	
	//Updates dragArea to match ClassBox location.
	private void updatedragArea(){
		if (this.dragArea == null)
		{
			this.dragArea =  new Rectangle(startX-7.5, startY-7.5, width+15, height+15);
			dragArea.setStroke(Color.RED);
			dragArea.setFill(Color.TRANSPARENT);
			dragToggle();
		}
		dragArea.setX(this.startX - 7.5);
		dragArea.setY(this.startY - 7.5);
		dragArea.setHeight(this.height + 15);
		dragArea.setWidth(this.width + 15);	
	}
	
	//Bounds checking for X parameter for dragging and Resizing.
	//Logic differentiates between dragging and resizing, since you can drag from anywhere, but only resize from bottom right.
	private double checkBoundsX(double x, Rectangle r) {
		if (x < UML.drawingBox.getBoundsInParent().getMinX() + 7.5) { // left side of gray area
			x = UML.drawingBox.getBoundsInParent().getMinX() + 7.5;
		}
		if (r.equals(resizeArea)){
			if (x > UML.drawingBox.getBoundsInParent().getMaxX() - 7.5) {
				x = UML.drawingBox.getBoundsInParent().getMaxX() - 7.5;
			}
		}
		else {
			if (x + this.width > UML.drawingBox.getBoundsInParent().getMaxX() - 7.5) { // right side of gray area
				x = UML.drawingBox.getBoundsInParent().getMaxX() - 7.5 - this.width;
			}
		}
		
		return x;
	}
	
	//Bounds checking for Y parameter for dragging and Resizing.
	//Logic differentiates between dragging and resizing, since you can drag from anywhere, but only resize from bottom right.
	private double checkBoundsY(double y, Rectangle r) {
		if (y < UML.drawingBox.getBoundsInParent().getMinY() + 7.5) { // top of gray area
			y = UML.drawingBox.getBoundsInParent().getMinY() + 7.5;
		}
		
		if (r.equals(resizeArea)){
			if (y > UML.drawingBox.getBoundsInParent().getMaxY() - 7.5) {
				y = UML.drawingBox.getBoundsInParent().getMaxY() - 7.5;
			}
		}
		else {
			if (y + this.height > UML.drawingBox.getBoundsInParent().getMaxY() - 7.5) { // right side of gray area
				y = UML.drawingBox.getBoundsInParent().getMaxY() - 7.5 - this.height;				}
		}
		
		return y;
	}
	
	//Toggles visibility of dragArea and resizeArea by setting their colors.
	//Setting .isVisible(false) doesn't allow setOnMouseEntered to activate, I think the object isn't there anymore.
	private void dragToggle() {
		
		//if (isLineBeingDrawn == false) {
			
			//System.out.println(isLineBeingDrawn);
		
			this.dragArea.setOnMouseExited(eventExited -> {
				resizeArea.setFill(Color.TRANSPARENT);
				dragArea.setStroke(Color.TRANSPARENT);
				deleteArea.setStroke(Color.TRANSPARENT);
				
				//Moving from dragArea to resizeArea to keep both visible
				this.resizeArea.setOnMouseEntered(eventEntered -> {
					resizeArea.setFill(Color.GREEN);
					dragArea.setStroke(Color.RED);
					deleteArea.setStroke(Color.RED);
				});
			});
			
			this.dragArea.setOnMouseEntered(eventEntered -> {
				resizeArea.setFill(Color.GREEN);
				dragArea.setStroke(Color.RED);
				deleteArea.setStroke(Color.RED);
			});
		//}
		
		
	}
	
	/** Returns height of this ClassBox.
	 * @return height This ClassBox's height.
	 */
	public static double getHeight() {
		return height;
	}
	
	/** Returns width of this ClassBox.
	 * @return width This ClassBox's height.
	 */
	public static double getWidth() {
		return width;
	}
	
	
	/** Returns text as string currently located in specified TextArea.
	 * @param t TextArea to get text from.
	 * @return String Text value of Textbox.
	 */
	public static String getText(TextArea t) {
		return t.getText();
	}
	
	public static void removeClassBox(Group group) {
		group.getChildren().removeAll(dragArea, resizeArea, rTop, rMid, rBot, tTop, tMid, tBot);
	}
	
	/*
	public static void setIsLineBeingDrawn(Boolean b) {
		isLineBeingDrawn = b;
	}
	*/
	public static void listenForDeletion(Group group) {
		
		dragArea.setOnMouseClicked(event -> {
			clicked = true;
		});
		resizeArea.setOnMouseClicked(event -> {
			clicked = true;
		});
		rTop.setOnMouseClicked(event -> {
			clicked = true;
		});
		rMid.setOnMouseClicked(event -> {
			clicked = true;
		});
		rBot.setOnMouseClicked(event -> {
			clicked = true;
		});
		tTop.setOnMouseClicked(event -> {
			clicked = true;
		});
		tMid.setOnMouseClicked(event -> {
			clicked = true;
		});
		tBot.setOnMouseClicked(event -> {
			clicked = true;
		});
		
		if (clicked) {
			removeClassBox(group);
		}
		
	}
}