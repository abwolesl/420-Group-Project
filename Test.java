
import javafx.scene.shape.*;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

import java.awt.Point;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.transform.Rotate;
import javafx.geometry.Point3D;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class Test extends Application {

	Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	double screenWidth = primaryScreenBounds.getWidth();
	double screenHeight = primaryScreenBounds.getHeight();
	boolean isMousePressed = false;
	double[] mouseCoords = new double[4];
	boolean buttonPressed = false;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage fxStage) throws InterruptedException {
		Group g = new Group();
		Scene fxScene = new Scene(g, screenWidth * .3, screenHeight);
		fxStage.setScene(fxScene);
		
		Button update = new Button("Update Line");
		
		Generalization a = new Generalization();
		a.drawMe(g);
	
		TextField startX = new TextField();
		Label sXL = new Label("startX");
		sXL.setLayoutY(85);
		startX.setLayoutY(100);
		TextField startY = new TextField();
		Label sYL = new Label("startY");
		sYL.setLayoutY(185);
		startY.setLayoutY(200);
		TextField endX = new TextField();
		Label eXL = new Label("endX");
		eXL.setLayoutY(285);
		endX.setLayoutY(300);
		TextField endY = new TextField();
		Label eYL = new Label("endY");
		eYL.setLayoutY(385);
		endY.setLayoutY(400);
		g.getChildren().addAll(update, startX,startY,endX,endY);
		g.getChildren().addAll(sXL,sYL,eXL,eYL);
		
		update.setOnAction((event) ->
		{
			if (!startX.getText().equals("")) {a.setStartX(Double.parseDouble(startX.getText()));}
			if (!startY.getText().equals("")) {a.setStartY(Double.parseDouble(startY.getText()));}
			if (!endX.getText().equals("")) {a.setEndX(Double.parseDouble(endX.getText()));}
			if (!endY.getText().equals("")) {a.setEndY(Double.parseDouble(endY.getText()));}
			startX.clear();
			startY.clear();
			endX.clear();
			endY.clear();
		});
		
		
		fxStage.show();
	}
	
	
}