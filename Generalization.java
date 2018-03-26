import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import java.util.stream.Stream;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class Generalization {
	private double startX, startY, endX, endY;
	private Line line;
	private Polygon head;
	
	public Generalization (double startX, double startY, double endX, double endY)  
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		
		this.setLine(new Line(startX, startY, endX, endY));
		this.setHead(new Polygon(Stream.of(calculateTrianglePoints(startX, startY, endX, endY)).mapToDouble(Double::doubleValue).toArray()));
		
	}
	
	public Generalization ()  
	{
		this.startX = 200;
		this.startY = 200;
		this.endX = 400;
		this.endY = 400;
		
		this.setLine(new Line(startX, startY, endX, endY));
		this.setHead(new Polygon(Stream.of(calculateTrianglePoints(startX, startY, endX, endY)).mapToDouble(Double::doubleValue).toArray()));
		
	}
	
	private Double[] calculateTrianglePoints(double startX, double startY, double endX, double endY)
	{
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
		Double[] triPoints = new Double[6];
		triPoints[0] = (l2x)+endX;
		triPoints[1] = (l2y)+endY;
		triPoints[2] = -(l2x)+endX;
		triPoints[3] = -(l2y)+endY;
		triPoints[4] = (l4x)+endX;
		triPoints[5] = (l4y)+endY;
		
		return triPoints;
	}

	public Polygon getHead() {
		return head;
	}

	public void setHead(Polygon head) {
		this.head = head;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}
	
	public double getStartX()
	{
		return startX;
	}
	
	public void setStartX(double X)
	{
		this.startX = X;
		this.head.getPoints().setAll(calculateTrianglePoints(startX, startY, endX, endY));
		this.line.setStartX(X);
	}
	
	public double getStartY()
	{
		return endX;
	}
	
	public void setStartY(double Y)
	{
		this.startY = Y;
		this.head.getPoints().setAll(calculateTrianglePoints(startX, startY, endX, endY));
		this.line.setStartY(Y);
	}
	
	public double getEndX()
	{
		return endX;
	}
	
	public void setEndX(double X)
	{
		this.endX = X;

		this.head.getPoints().setAll(calculateTrianglePoints(startX, startY, endX, endY));
		//this.head = new Polygon(calculateTrianglePoints(startX, startY, endX, endY));
		this.line.setEndX(X);
	}
	
	public double getEndY()
	{
		return endY;
	}
	
	public void setEndY(double Y)
	{
		this.endY = Y;

		this.head.getPoints().setAll(calculateTrianglePoints(startX, startY, endX, endY));
		//this.head = new Polygon(calculateTrianglePoints(startX, startY, endX, endY));
		this.line.setEndY(Y);
	}
	
	public void drawMe(Group g)
	{
		g.getChildren().addAll(line,head);
	}

}