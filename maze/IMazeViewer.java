package maze;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import maze.MazeGrid.GenerationType;
import search.SearchType;

public abstract class IMazeViewer<Grid extends MazeGrid<Cell>, Cell extends MazeCell<Cell>> {

	final protected GraphicsContext context;
	
	final protected double screenWidth;
	final protected double screenHeight;
	
	protected Grid maze;
	
	private Cell currentCell;
	
	final public static Color BACKGROUND = Color.BLACK;
	
	final public static Color SOLUTION = Color.GREENYELLOW;
	
	final public static Color CURRENT_SEARCH_PATH = new Color(0, (187.0 / 255), (180.0 / 255), 1);
	final public static Color PAST_SEARCH_PATH = new Color(0, 0, 0.5, 1);
	
	final public static Color CURRENT_HUMAN_PATH = Color.RED;
	final public static Color PAST_HUMAN_PATH = Color.LIGHTPINK;
	
	final public static Color START_CELL = Color.BLUE;
	final public static Color FINISH_CELL = Color.BLUE;
	
	final public static int LINE_WIDTH = 2;
	
	public IMazeViewer(GraphicsContext context, double screenHeight, double screenWidth) {
		this.context = context;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		
		this.currentCell = null;
	}
	
	private void clear() {
		
		this.context.setFill(IMazeViewer.BACKGROUND);
		this.context.fillRect(0, 0, screenWidth, screenHeight);
	}
	
	public void redraw(boolean clearFirst) {
		
		if (clearFirst) {
			this.clear();
		}
		
		this.draw();
	}
	
	public void redraw() {
		this.redraw(true);
	}
	
	public void drawNewMaze(int length, GenerationType generationType) {
		this.newMaze(length, generationType);
		this.redraw();
		
		this.currentCell = null;
		
		this.colorCells(this.maze.getCell(0), START_CELL);
		this.colorCells(this.maze.getCell(this.maze.size() - 1), FINISH_CELL);
	}
	
	public void searchAndColor(SearchType searchType) {
		MazeSearch<Grid, Cell> search = new MazeSearch<Grid, Cell>
			(this.maze.getCell(0), this.maze.getCell(this.maze.size() - 1), searchType);
		
		this.colorCells(search.search(), SOLUTION);
	}
	
	public void animateSolution(SearchType searchType) {
		
		if (maze == null) {
			return;
		}
		
		MazeSearch<Grid, Cell> search = new MazeSearch<Grid, Cell>
			(this.maze.getCell(0), this.maze.getCell(this.maze.size() - 1), searchType);
		
		search.initSearch(maze.getCell(0)); // TODO
		
		AnimationTimer timer = new AnimationTimer() {

			ArrayList<Cell> currentPath = null;
			int loopNum = 0;
			
			@Override
			public void handle(long arg0) {
				
				loopNum++;
				
				if (currentPath != null) {
					colorCells(currentPath, IMazeViewer.PAST_SEARCH_PATH);
				}
				
				Cell current = search.peekAtQueue();
				Cell solution = search.searchStep();
				
				currentPath = search.backtrack(current);
				
				colorCells(currentPath, IMazeViewer.CURRENT_SEARCH_PATH);
				
				if (solution != null || search.peekAtQueue() == null) {
					this.stop();
				}
				
				// Could probably optimize this coloring so you don't recolor the entire path twice
			}
			
		};
		
		timer.start();
	}
	
	protected void updateCurrentCell(String key) {
		
		if (this.currentCell == null) {
			this.currentCell = this.maze.getCell(0);
			this.colorCells(this.currentCell, IMazeViewer.CURRENT_HUMAN_PATH);
			return;
		}
		
		Cell nextCell = this.getNextCell(currentCell, key);
		
		if (nextCell == null) {
			return;
		}
		
		this.colorCells(this.currentCell, IMazeViewer.PAST_HUMAN_PATH);
		this.currentCell = nextCell;
		this.colorCells(this.currentCell, IMazeViewer.CURRENT_HUMAN_PATH);
		
	}
	
	public void colorCells(Cell cell, Color color) {
		
		this.context.setFill(color);
		this.fillCell(cell);
	}
	
	public void colorCells(ArrayList<Cell> cells, Color color) {
		
		this.context.setFill(color);
		
		for (Cell cell : cells) {
			this.fillCell(cell);
		}
	}
	
	protected abstract Cell getNextCell(Cell currentCell, String key);
	
	protected abstract void draw();
		
	protected abstract void fillCell(Cell solution);
	
	protected abstract void newMaze(int length, GenerationType generationType);
	
}
