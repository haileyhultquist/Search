package maze.grids.rect2d;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import maze.IMazeViewer;
import maze.MazeGrid.GenerationType;

public class Viewer2D extends IMazeViewer<Maze2D, Cell2D> {

	private double cellScale;
	
	public Viewer2D(GraphicsContext context, double screenHeight, double screenWidth) {
		super(context, screenHeight, screenWidth);
	}

	@Override
	public void draw() {
		
		double lineWidth = 2;
		
		this.context.setStroke(Color.AZURE);
		this.context.setLineWidth(lineWidth);
		
		for (int i = 0; i < this.maze.size(); i++) {
			this.drawCellWalls(this.maze.getCell(i), true);
		}
		
	}
	
	private void drawCellWalls(Cell2D cell, boolean halfOnly) {
		
		this.context.setLineWidth(2);
		
		int cellY = cell.getY();
		int cellX = cell.getX();
		
		ArrayList<Cell2D> possibleNeighbors = this.maze.allPossibleNeighbors(cell);
		
		for (Cell2D neighbor : cell) {
			possibleNeighbors.remove(neighbor);
		}
		
		for (Cell2D remaining : possibleNeighbors) {
			
			int neighborY = remaining.getY();
			int neighborX = remaining.getX();
			
			int dy = neighborY - cellY;
			int dx = neighborX - cellX;
			
			if (halfOnly && (dy < 0 || dx < 0)) {
				// this was already covered
				continue;
			}
			
			int startX = cellX + Math.max(dx, 0);
			int startY = cellY + Math.max(dy, 0);
			int endX = startX + Math.abs(dy);
			int endY = startY + Math.abs(dx);
			
			this.context.strokeLine(this.cellScale * startX, this.cellScale * startY,
						this.cellScale * endX, this.cellScale * endY);
		}
	}

	@Override
	public void fillCell(Cell2D cell) {
					
		int cellY = cell.getY();
		int cellX = cell.getX();
		
		this.context.fillRect(this.cellScale * cellX, this.cellScale * cellY,
				this.cellScale, this.cellScale);
		
		this.drawCellWalls(cell, false);
	}

	@Override
	public void newMaze(int length, GenerationType generationType) {
		this.maze = new Maze2D(length, length, generationType);
		
		cellScale = Math.min(this.screenWidth / this.maze.getWidth(),
				this.screenHeight / this.maze.getHeight());
	}

	@Override
	protected Cell2D getNextCell(Cell2D currentCell, String key) {
		
		int x = currentCell.getX();
		int y = currentCell.getY();
		
		switch (key) {
		case "LEFT":
		case "A":
			x -= 1;
			break;
		case "UP":
		case "W":
			y -= 1;
			break;
		case "RIGHT":
		case "D":
			x += 1;
			break;
		case "DOWN":
		case "S":
			y += 1;
			break;
		default:
			break;
		}
		
		if (x < 0 || x >= this.maze.getWidth() || y < 0 || y >= this.maze.getHeight()) {
			return null;
		}
		
		Cell2D neighbor = this.maze.getCell(y, x);
		
		if (!currentCell.isNeighbor(neighbor)) {
			return null;
		}
		
		return neighbor;
	}

}
