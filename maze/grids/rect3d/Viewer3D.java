package maze.grids.rect3d;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import maze.IMazeViewer;
import maze.MazeGrid.GenerationType;

public class Viewer3D extends IMazeViewer<Maze3D, Cell3D> {

	private double cellScale;
	
	final public static Color SHADOW = new Color(CURRENT_HUMAN_PATH.getRed() / 3,
												CURRENT_HUMAN_PATH.getGreen() / 3,
												CURRENT_HUMAN_PATH.getBlue() / 3,
												1.0);
	
	public Viewer3D(GraphicsContext context, double screenHeight, double screenWidth) {
		super(context, screenHeight, screenWidth);
	}

	@Override
	public void draw() {
		
		double lineWidth = 2;
		
		this.context.setStroke(Color.AZURE);
		this.context.setLineWidth(lineWidth);
		
		double scale = Math.min(this.screenWidth / (this.maze.getWidth() * this.maze.getLength()),
				this.screenHeight / (this.maze.getHeight()));
		
		this.context.setFont(new Font(scale / 4));
		
		for (int i = 0; i < this.maze.size(); i++) {
			this.drawCellWalls(this.maze.getCell(i), true);
		}
		
		this.context.setLineWidth(4 * lineWidth);
		this.context.strokeLine(this.screenWidth / 2, 0, this.screenWidth / 2, this.screenHeight);
	}
	
	private void drawCellWalls(Cell3D cell, boolean halfOnly) {
		
		this.context.setLineWidth(2);
		
		int cellY = cell.getY();
		int cellX = cell.getX();
		int cellZ = cell.getZ();
		
		ArrayList<Cell3D> possibleNeighbors = this.maze.allPossibleNeighbors(cell);
		
		for (Cell3D neighbor : cell) {
			possibleNeighbors.remove(neighbor);
		}
		
		for (Cell3D remaining : possibleNeighbors) {
			
			int neighborY = remaining.getY();
			int neighborX = remaining.getX();
			int neighborZ = remaining.getZ();
			
			int dy = neighborY - cellY;
			int dx = neighborX - cellX;
			int dz = neighborZ - cellZ;

			if (halfOnly && (dy < 0 || dx < 0)) {
				// this was already covered
				continue;
			}
			
			if (dz != 0) {
				// This case is reversed in that it is marked if it is open, so it will be done later.
				continue;
			}
			
			double startX = this.cellScale * (cellZ * this.maze.getWidth()) + this.cellScale * (cellX + Math.max(dx, 0));
			double startY = this.cellScale * (cellY + Math.max(dy, 0));
			double endX = startX + this.cellScale * Math.abs(dy);
			double endY = startY + this.cellScale * Math.abs(dx);
			
			this.context.strokeLine(startX, startY, endX, endY);
		}
		
		for (Cell3D neighbor : cell) {
			
			int dz = neighbor.getZ() - cell.getZ();
			
			if (dz == 0) {
				continue;
			}
			
			double xPos = this.cellScale * (cellZ * this.maze.getWidth()) + this.cellScale * cellX + this.cellScale / 4;
			
			if (dz < 0) {
				this.context.strokeText("<<", xPos, this.cellScale * cellY + this.cellScale / 4);
			} else if (dz > 0) {
				this.context.strokeText(">>", xPos, this.cellScale * cellY + 3 * this.cellScale / 4);
			}
		}
		
		if ((cellX == this.maze.getWidth() - 1 && cellZ == 0)
				|| (cellX == 0 && cellZ == 1)) {
			
			// TODO this is excessive
			
			this.context.setLineWidth(8);
			this.context.strokeLine(this.screenWidth / 2, 0, this.screenWidth / 2, this.screenHeight);
		}
	}

	@Override
	public void fillCell(Cell3D cell) {
					
		int cellY = cell.getY();
		int cellX = cell.getX();
		int cellZ = cell.getZ();
		
		double xPos = cellScale * (cellZ * this.maze.getWidth()) + cellScale * cellX;
		
		this.context.fillRect(xPos, cellScale * cellY,
				cellScale, cellScale);
		
		this.drawCellWalls(cell, false);
	}

	@Override
	public void newMaze(int length, GenerationType generationType) {
		this.maze = new Maze3D(length, length / 2, 2, generationType);
		
		cellScale = Math.min(this.screenWidth / (this.maze.getWidth() * this.maze.getLength()),
				this.screenHeight / (this.maze.getHeight()));
	}

	@Override
	protected Cell3D getNextCell(Cell3D currentCell, String key) {
		
		int x = currentCell.getX();
		int y = currentCell.getY();
		int z = currentCell.getZ();
		
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
		case "Q":
			z -= 1;
			break;
		case "E":
			z += 1;
			break;
		default:
			break;
		}
		
		if (x < 0 || x >= this.maze.getWidth()
				|| y < 0 || y >= this.maze.getHeight()
				|| z < 0 || z >= this.maze.getLength()) {
			return null;
		}
		
		Cell3D neighbor = this.maze.getCell(y, x, z);
		
		if (!currentCell.isNeighbor(neighbor)) {
			return null;
		}
		
		return neighbor;
	}	
}
