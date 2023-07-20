package maze;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import maze.MazeGrid.GenerationType;
import maze.MazeGrid.Shape;
import maze.grids.circle.ViewerCircle;
import maze.grids.rect2d.Viewer2D;
import maze.grids.rect3d.Viewer3D;
import search.SearchType;

public class MazeMain extends Application {

//	final protected static double screenWidth = 1030;
//	final protected static double screenHeight = 1030;
	
	final protected static double screenWidth = 900;
	final protected static double screenHeight = 900;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Maze viewer");
		
    	StackPane root = new StackPane();		
		
		Canvas canvas = new Canvas(screenWidth, screenHeight);
		GraphicsContext context = canvas.getGraphicsContext2D();
		
		context.setFill(Color.BLACK);
		context.fillRect(0, 0, screenWidth, screenHeight);
		
		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		
    	primaryStage.setScene(scene);
        primaryStage.show();
        
        IMazeViewer<?, ?> [] mazeViewers = {
        	new Viewer2D(context, screenHeight, screenWidth),
        	new ViewerCircle(context, screenHeight, screenWidth),
        	new Viewer3D(context, screenHeight, screenWidth)
        };
        
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			
			IMazeViewer<?, ?> currentViewer = null;
			int size = 40;
			
	        @Override
	        public void handle(KeyEvent event) {
	        	switch (event.getCode()) {
	        	case R: {
	        		
	        		currentViewer = mazeViewers[Shape.RECTANGLE_2D.ordinal()];
	        		currentViewer.drawNewMaze(size, GenerationType.ALDOUS_BRODER);
	        		
	        		break;
	        		
	        	} case T: {
	        		
	        		currentViewer = mazeViewers[Shape.RECTANGLE_2D.ordinal()];
	        		currentViewer.drawNewMaze(size, GenerationType.RECURSIVE_BACKTRACKING);
	        		
	        		break;
	        		
	        	} case F: {
	        		
	        		currentViewer = mazeViewers[Shape.CIRCLE.ordinal()];
	        		currentViewer.drawNewMaze(size, GenerationType.ALDOUS_BRODER);
	        		
	        		break;
	        		
	        	} case G: {
	        		
	        		currentViewer = mazeViewers[Shape.CIRCLE.ordinal()];
	        		currentViewer.drawNewMaze(size, GenerationType.RECURSIVE_BACKTRACKING);
	        		
	        		break;
	        		
	        	} case V: {
	        		
	        		currentViewer = mazeViewers[Shape.RECTANGLE_3D.ordinal()];
	        		currentViewer.drawNewMaze(size / 2, GenerationType.ALDOUS_BRODER);
	        		
	        		break;
	        	
	        	} case B: {
	        		
	        		currentViewer = mazeViewers[Shape.RECTANGLE_3D.ordinal()];
	        		currentViewer.drawNewMaze(size / 2, GenerationType.RECURSIVE_BACKTRACKING);
	        		
	        		break;
	        		
	        	} case I: {
	        		
	        		currentViewer.searchAndColor(SearchType.DEPTH_FIRST_Q);
	        		break;
	        		
	        	} case O: {
	        		
	        		currentViewer.searchAndColor(SearchType.DIJKSTRA);
	        		break;

	        	}	case P: {
	        		
	        		currentViewer.searchAndColor(SearchType.A_STAR);
	        		break;
	        		
	        	} case J: {
	        		
	        		currentViewer.animateSolution(SearchType.DEPTH_FIRST_Q);
	        		break;

	        	} case K: {
	        		
	        		currentViewer.animateSolution(SearchType.DIJKSTRA);
	        		break;
	        		
	        	} case L: {
	        		
	        		currentViewer.animateSolution(SearchType.A_STAR);
	        		break;

	        	} case M: {
	        		
	        		currentViewer.redraw(false);
	        		break;
	        		
	        	} default:
	        		
	        		// For the human path
	        		if (currentViewer != null) {
	        			currentViewer.updateCurrentCell(event.getCode().toString());
	        		}
	        		
					break;
	        	}
	        }
	    });
	}
	
	public static void main (String [] args) {
		launch();
	}
	
}
