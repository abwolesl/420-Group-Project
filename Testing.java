// JUnit tests for SWETR iteration 2

import org.junit.Test;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import static org.junit.Assert.*;

public class Testing {
		
	@Test
	public void testClassBoxConstructors() throws InterruptedException {
		
		// Below ensures JUnit testing works with JavaFX
		// Allows time for UML scene to set up properly
		// Source: http://stackoverflow.com/questions/18429422/basic-junit-test-for-javafx-8
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
		Thread.sleep(1000);
		
		// Test empty constructor
		ClassBox classBoxEmpty = new ClassBox();
		// empty constructor creates class box with start x and y of 200 and height and width of 130
		assertTrue("New Class Box without params", classBoxEmpty.getStartX() == 200 && classBoxEmpty.getStartY() == 200 && classBoxEmpty.getHeight() == 130 && classBoxEmpty.getWidth() == 130);
		
		// Test constructor with parameters
		ClassBox classBox = new ClassBox(200, 200, 130, 130);
		
		assertTrue("New Class Box with params, test X and Y", classBox.getStartX() == 200 && classBox.getStartY() == 200);
		assertTrue("New Class Box with params, test height and width", classBox.getHeight() == 130 && classBox.getWidth() == 130);
		
		// Test resizing class box
		ClassBox classBoxResize = new ClassBox(200, 200, 130, 130);
		
		// increase height and width
		classBox.updateBoxes(200, 200, 200, 200);

		assertTrue("Check resized (increased) height and width", classBoxResize.getHeight() == 200 && classBoxResize.getWidth() == 200);
		
		// decrease height and width
		classBox.updateBoxes(200, 200, 100, 100);
		// height and width less than 130 gets set to 130 to maintain minimum class box shape
		assertTrue("Check resized (decreased) height and width", classBoxResize.getHeight() == 130 && classBoxResize.getWidth() == 130);
		
		// Test moving class box (simulating user dragging box on screen)
		ClassBox classBoxMove = new ClassBox();
		
		// increase start x and start y
		classBoxMove.updateBoxes(300, 300, 200, 200);
		assertTrue("Check moved (increased) x and y", classBoxMove.getStartX() == 300 && classBoxMove.getStartY() == 300);

		// decrease start x and start y
		classBoxMove.updateBoxes(150, 150, 200, 200);
		assertTrue("Check moved (decreased) x and y", classBoxMove.getStartX() == 150 && classBoxMove.getStartY() == 150);
	}
	
	// Test relationship class
	@Test
	public void testRelationship() {
		
		Group testGroup = new Group();
		Scene testScene = new Scene(testGroup);
		
		// Relationship type is empty String
		Relationship emptyRelationship = new Relationship(testScene, testGroup, "");
		assertTrue("Check for relationship to be empty", emptyRelationship.getRelType() == "");
		
		// Relationship type is Aggregation
		Relationship aggRelationship = new Relationship(testScene, testGroup, "Aggregation");
		assertTrue("Check for relationship to be empty", aggRelationship.getRelType() == "Aggregation");
		
		// Relationship type is Composition
		Relationship compRelationship = new Relationship(testScene, testGroup, "Composition");
		assertTrue("Check for relationship to be empty", compRelationship.getRelType() == "Composition");
		
		// Relationship type is Generalization
		Relationship genRelationship = new Relationship(testScene, testGroup, "Generalization");
		assertTrue("Check for relationship to be empty", genRelationship.getRelType() == "Generalization");
		
		// Relationship type is Dependency
		Relationship depRelationship = new Relationship(testScene, testGroup, "Dependency");
		assertTrue("Check for relationship to be empty", depRelationship.getRelType() == "Dependency");
		
		// Check start x and y and end x and y of aggregation line once it is drawn
		// normally the start x and y and end x and y values are given from mouse presses
		aggRelationship.drawAggregationOrComposition(testGroup, 200, 200, 300, 300, "White");
		
		assertTrue("Check for relationship to be empty", aggRelationship.getStartXValue() == 200 && aggRelationship.getStartYValue() == 200);
		assertTrue("Check for relationship to be empty", aggRelationship.getEndXValue() == 300 && aggRelationship.getEndYValue() == 300);
		
		// Check start x and y and end x and y of composition line once it is drawn
		// normally the start x and y and end x and y values are given from mouse presses
		compRelationship.drawAggregationOrComposition(testGroup, 300, 300, 300, 300, "Black");
		
		assertTrue("Check for relationship to be empty", compRelationship.getStartXValue() == 300 && aggRelationship.getStartYValue() == 300);
		assertTrue("Check for relationship to be empty", compRelationship.getEndXValue() == 300 && aggRelationship.getEndYValue() == 300);
		
		// Check start x and y and end x and y of generalization line once it is drawn
		// normally the start x and y and end x and y values are given from mouse presses
		genRelationship.drawGeneralization(testGroup, 150, 150, 200, 200);
		
		assertTrue("Check for relationship to be empty", genRelationship.getStartXValue() == 150 && genRelationship.getStartYValue() == 150);
		assertTrue("Check for relationship to be empty", genRelationship.getEndXValue() == 200 && genRelationship.getEndYValue() == 200);
		
		// Check start x and y and end x and y of dependency line once it is drawn
		// normally the start x and y and end x and y values are given from mouse presses
		depRelationship.drawDependency(testGroup, 250, 150, 300, 200);
		
		assertTrue("Check for relationship to be empty", depRelationship.getStartXValue() == 250 && depRelationship.getStartYValue() == 150);
		assertTrue("Check for relationship to be empty", depRelationship.getEndXValue() == 300 && depRelationship.getEndYValue() == 200);
	}

}
