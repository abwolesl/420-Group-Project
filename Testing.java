
// JUnit tests for SWETR iteration 2

import org.junit.Test;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import static org.junit.Assert.*;

public class Testing {

	// Below ensures JUnit testing works with JavaFX
	// Allows time for UML scene to set up properly
	// Source:
	// http://stackoverflow.com/questions/18429422/basic-junit-test-for-javafx-8
	public void startUML() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				new JFXPanel();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						new UML().start(new Stage());
					}
				});
			}
		});
		thread.start();
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Test default constructor (no params)
	@Test
	public void testEmptyCtor() {
		// Have to start UML diagram once.
		startUML();

		ClassBox empty = new ClassBox();
		assertTrue("startX default location is 300", empty.getStartX() == 300);
		assertTrue("startY default location is 300", empty.getStartY() == 300);
		assertTrue("default height is 130", empty.getHeight() == 130);
		assertTrue("default width is 130", empty.getWidth() == 130);
		assertTrue("Top TextArea is empty", empty.getText(empty.getTTop()).equals(""));
		assertTrue("Middle TextArea is empty", empty.getText(empty.getTMid()).equals(""));
		assertTrue("Bottom TextArea is empty", empty.getText(empty.getTBot()).equals(""));
	}

	// Test constructor with parameters
	@Test
	public void testFullCtor() {
		ClassBox full = new ClassBox(500, 550, 250, 300, "Top Text", "Middle Text", "Bottom Text");
		assertTrue("startX matches startX param", full.getStartX() == 500);
		assertTrue("startY matches startY param", full.getStartY() == 550);
		assertTrue("height matches height param", full.getHeight() == 300);
		assertTrue("width matches width param", full.getWidth() == 250);

		assertTrue("Top Text matches tTop param", full.getText(full.getTTop()).equals("Top Text"));
		assertTrue("Top Text matches tMid param", full.getText(full.getTMid()).equals("Middle Text"));
		assertTrue("Top Text matches tBot param", full.getText(full.getTBot()).equals("Bottom Text"));
	}

	// Simulate drawn bottom right to top left
	@Test
	public void testBackwardsCtor() {
		ClassBox back = new ClassBox(350, 350, -250, -300, "Top Text", "Middle Text", "Bottom Text");
		assertTrue("startX matches startX param", back.getStartX() == 100);
		assertTrue("startY matches startY param", back.getStartY() == 50);
		assertTrue("height matches height param", back.getHeight() == 300);
		assertTrue("width matches width param", back.getWidth() == 250);
	}

	@Test
	public void testClassBoxIncreaseSize() {
		ClassBox increase = new ClassBox();
		// Boxes Update
		increase.updateBoxes(300, 300, 200, 400);
		increase.updateBoxes(300, 300, 400, 500);
		assertTrue("Height matches updated height", increase.getHeight() == 500);
		assertTrue("Width matches updated width", increase.getWidth() == 400);
		// TextArea Update
		increase.updateTextAreas(300, 300, 200, 400);
		increase.updateTextAreas(300, 300, 400, 500);
		assertTrue("Height matches updated height", increase.getHeight() == 500);
		assertTrue("Width matches updated width", increase.getWidth() == 400);

	}

	@Test
	public void testClassBoxDecreaseSize() {
		ClassBox decrease = new ClassBox();
		// Boxes Update
		decrease.updateBoxes(300, 300, 500, 500);
		decrease.updateBoxes(300, 300, 250, 250);
		assertTrue("Height matches updated height", decrease.getHeight() == 250);
		assertTrue("Width matches updated width", decrease.getWidth() == 250);
		// TextArea Update
		decrease.updateBoxes(300, 300, 500, 500);
		decrease.updateBoxes(300, 300, 250, 250);
		assertTrue("Height matches updated height", decrease.getHeight() == 250);
		assertTrue("Width matches updated width", decrease.getWidth() == 250);
	}

	@Test
	public void testClassBoxMinSize() {
		ClassBox min = new ClassBox();
		// Boxes Update
		min.updateBoxes(300, 300, 50, 50);
		assertTrue("Height matches min height(130)", min.getHeight() == 130);
		assertTrue("Width matches min width(130)", min.getWidth() == 130);
		// TextArea Update
		min.updateTextAreas(300, 300, 50, 50);
		assertTrue("Height matches min height(130)", min.getHeight() == 130);
		assertTrue("Width matches min width(130)", min.getWidth() == 130);
	}

	@Test
	public void testClassBoxMove() {
		ClassBox move = new ClassBox();
		// Decrease X & Y
		// Update Boxes
		move.updateBoxes(80.20, 9.20, 130, 130);
		assertTrue("ClassBox X matches X Param", move.getStartX() == 80.20);
		assertTrue("ClassBox Y matches Y Param", move.getStartY() == 9.20);
		// Update TextAreas
		move.updateTextAreas(80.20, 9.20, 130, 130);
		assertTrue("ClassBox X matches X Param", move.getStartX() == 80.20);
		assertTrue("ClassBox Y matches Y Param", move.getStartY() == 9.20);
		// Increase X & Y
		// Update Boxes
		move.updateBoxes(600.23, 1000.1, 130, 130);
		assertTrue("ClassBox X matches X Param", move.getStartX() == 600.23);
		assertTrue("ClassBox Y matches Y Param", move.getStartY() == 1000.1);
		// Update TextAreas
		move.updateTextAreas(600.23, 1000.1, 130, 130);
		assertTrue("ClassBox X matches X Param", move.getStartX() == 600.23);
		assertTrue("ClassBox Y matches Y Param", move.getStartY() == 1000.1);
	}

	@Test
	public void testAggregation() {
		Pane testPane = new Pane();

		// Initialization
		Relationship agg = new Relationship(testPane, "Aggregation", 100, 200, 300, 400);
		assertTrue("Relationship type matches input param", agg.getRelType().equals("Aggregation"));
		assertTrue("StartX value matches input param", agg.getStartXValue() == 100);
		assertTrue("StartY value matches input param", agg.getStartYValue() == 200);
		assertTrue("EndX value matches input param", agg.getEndXValue() == 300);
		assertTrue("EndY value matches input param", agg.getEndYValue() == 400);

		// Move Increase
		agg.updateRel(200, 300, 400, 500);
		assertTrue("StartX value matches input param", agg.getStartXValue() == 200);
		assertTrue("StartY value matches input param", agg.getStartYValue() == 300);
		assertTrue("EndX value matches input param", agg.getEndXValue() == 400);
		assertTrue("EndY value matches input param", agg.getEndYValue() == 500);

		// Move Decrease
		agg.updateRel(50, 75, 100, 125);
		assertTrue("StartX value matches input param", agg.getStartXValue() == 50);
		assertTrue("StartY value matches input param", agg.getStartYValue() == 75);
		assertTrue("EndX value matches input param", agg.getEndXValue() == 100);
		assertTrue("EndY value matches input param", agg.getEndYValue() == 125);

	}

	@Test
	public void testComposition() {
		Pane testPane = new Pane();

		// Initialization
		Relationship comp = new Relationship(testPane, "Composition", 100, 200, 300, 400);
		assertTrue("Relationship type matches input param", comp.getRelType().equals("Composition"));
		assertTrue("StartX value matches input param", comp.getStartXValue() == 100);
		assertTrue("StartY value matches input param", comp.getStartYValue() == 200);
		assertTrue("EndX value matches input param", comp.getEndXValue() == 300);
		assertTrue("EndY value matches input param", comp.getEndYValue() == 400);

		// Move Increase
		comp.updateRel(200, 300, 400, 500);
		assertTrue("StartX value matches input param", comp.getStartXValue() == 200);
		assertTrue("StartY value matches input param", comp.getStartYValue() == 300);
		assertTrue("EndX value matches input param", comp.getEndXValue() == 400);
		assertTrue("EndY value matches input param", comp.getEndYValue() == 500);

		// Move Decrease
		comp.updateRel(50, 75, 100, 125);
		assertTrue("StartX value matches input param", comp.getStartXValue() == 50);
		assertTrue("StartY value matches input param", comp.getStartYValue() == 75);
		assertTrue("EndX value matches input param", comp.getEndXValue() == 100);
		assertTrue("EndY value matches input param", comp.getEndYValue() == 125);
	}

	@Test
	public void testGeneralization() {
		Pane testPane = new Pane();

		// Initialization
		Relationship gen = new Relationship(testPane, "Generalization", 100, 200, 300, 400);
		assertTrue("Relationship type matches input param", gen.getRelType().equals("Generalization"));
		assertTrue("StartX value matches input param", gen.getStartXValue() == 100);
		assertTrue("StartY value matches input param", gen.getStartYValue() == 200);
		assertTrue("EndX value matches input param", gen.getEndXValue() == 300);
		assertTrue("EndY value matches input param", gen.getEndYValue() == 400);

		// Move Increase
		gen.updateRel(200, 300, 400, 500);
		assertTrue("StartX value matches input param", gen.getStartXValue() == 200);
		assertTrue("StartY value matches input param", gen.getStartYValue() == 300);
		assertTrue("EndX value matches input param", gen.getEndXValue() == 400);
		assertTrue("EndY value matches input param", gen.getEndYValue() == 500);

		// Move Decrease
		gen.updateRel(50, 75, 100, 125);
		assertTrue("StartX value matches input param", gen.getStartXValue() == 50);
		assertTrue("StartY value matches input param", gen.getStartYValue() == 75);
		assertTrue("EndX value matches input param", gen.getEndXValue() == 100);
		assertTrue("EndY value matches input param", gen.getEndYValue() == 125);
	}

	@Test
	public void testDependency() {
		Pane testPane = new Pane();

		// Initialization
		Relationship depend = new Relationship(testPane, "Dependency", 100, 200, 300, 400);
		assertTrue("Relationship type matches input param", depend.getRelType().equals("Dependency"));
		assertTrue("StartX value matches input param", depend.getStartXValue() == 100);
		assertTrue("StartY value matches input param", depend.getStartYValue() == 200);
		assertTrue("EndX value matches input param", depend.getEndXValue() == 300);
		assertTrue("EndY value matches input param", depend.getEndYValue() == 400);

		// Move Increase
		depend.updateRel(200, 300, 400, 500);
		assertTrue("StartX value matches input param", depend.getStartXValue() == 200);
		assertTrue("StartY value matches input param", depend.getStartYValue() == 300);
		assertTrue("EndX value matches input param", depend.getEndXValue() == 400);
		assertTrue("EndY value matches input param", depend.getEndYValue() == 500);

		// Move Decrease
		depend.updateRel(50, 75, 100, 125);
		assertTrue("StartX value matches input param", depend.getStartXValue() == 50);
		assertTrue("StartY value matches input param", depend.getStartYValue() == 75);
		assertTrue("EndX value matches input param", depend.getEndXValue() == 100);
		assertTrue("EndY value matches input param", depend.getEndYValue() == 125);
	}

	@Test
	public void testBackwardsDraw() {

		Pane testPane = new Pane();
		Relationship depend = new Relationship(testPane, "Dependency", 400, 300, 200, 100);
		assertTrue("Relationship type matches input param", depend.getRelType().equals("Dependency"));
		assertTrue("StartX value matches input param", depend.getStartXValue() == 400);
		assertTrue("StartY value matches input param", depend.getStartYValue() == 300);
		assertTrue("EndX value matches input param", depend.getEndXValue() == 200);
		assertTrue("EndY value matches input param", depend.getEndYValue() == 100);
	}

}
