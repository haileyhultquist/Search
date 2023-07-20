package maze.grids.circle;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import maze.IMazeViewer;
import maze.MazeGrid.GenerationType;

public class ViewerCircle extends IMazeViewer<MazeCircle, CellCircle> {

	private double cellScale;
	
	public ViewerCircle(GraphicsContext context, double screenHeight, double screenWidth) {
		super(context, screenHeight, screenWidth);
	}

	@Override
	public void draw() {

		double lineWidth = 2;
    	
		this.context.setStroke(Color.AZURE);
		this.context.setLineWidth(lineWidth);
				
		for (int i = 0; i < this.maze.size(); i++) {
			
			CellCircle cell = this.maze.getCell(i);
			this.drawCellWalls(cell);
		}
		
		int radius = this.maze.getRadius();
		int innerCells = this.maze.getCellsOfRing(radius - 1);
		
		for (int position = 0; position < innerCells; position++) {
			
			int nextPosition = (position + 1 + innerCells % innerCells);
			
			double startX = radius * Math.cos(2 * Math.PI * (double)(position) / innerCells);
			double startY = radius * Math.sin(2 * Math.PI * (double)(position) / innerCells);
			
			double endX = radius * Math.cos(2 * Math.PI * (double)(nextPosition) / innerCells);
			double endY = radius * Math.sin(2 * Math.PI * (double)(nextPosition) / innerCells);
			
			this.context.strokeLine((this.screenWidth / 2) + this.cellScale * startX,
								(this.screenHeight / 2) - this.cellScale * startY,
								(this.screenWidth / 2) + this.cellScale * endX,
								(this.screenHeight / 2) - this.cellScale * endY);
			
		}
		
		
	}
	
	private void drawCellWalls(CellCircle cell) {
		
		// This only draws half of the cell walls every time, but it works ok.
		// Maybe allow the option to draw all of them eventually.
		
		int cellRing = cell.getRing();
		int cellPosition = cell.getPosition();
		
		ArrayList<CellCircle> possibleNeighbors = this.maze.allPossibleNeighbors(cell);
		
		for (CellCircle neighbor : cell) {
			possibleNeighbors.remove(neighbor);
		}
		
		for (CellCircle remaining : possibleNeighbors) {
			
			int neighborRing = remaining.getRing();
			int neighborPosition = remaining.getPosition();
			
			int cellsInNeighborRing = this.maze.getCellsOfRing(neighborRing);
			
			int dr = neighborRing - cellRing;
			int dp = neighborPosition - cellPosition;
			
			if (dr == 0 && Math.abs(dp) > 1) {
				// Flips in the case where it wraps around.
				dp = -(Math.abs(dp) / dp) * (cellsInNeighborRing - Math.abs(dp));
			}
			
			if (dr < 0 || dp < 0) {
				// this was already covered
				continue;
			}
			
			if (dr > 0) {
				
				int nextPosition = (neighborPosition + 1 + cellsInNeighborRing % cellsInNeighborRing);
				
				double startX = neighborRing * Math.cos(2 * Math.PI * (double)(neighborPosition) / cellsInNeighborRing);
				double startY = neighborRing * Math.sin(2 * Math.PI * (double)(neighborPosition) / cellsInNeighborRing);
				
				double endX = neighborRing * Math.cos(2 * Math.PI * (double)(nextPosition) / cellsInNeighborRing);
				double endY = neighborRing * Math.sin(2 * Math.PI * (double)(nextPosition) / cellsInNeighborRing);
				
				this.context.strokeLine((this.screenWidth / 2) + this.cellScale * startX,
									(this.screenHeight / 2) - this.cellScale * startY,
									(this.screenWidth / 2) + this.cellScale * endX,
									(this.screenHeight / 2) - this.cellScale * endY);
				
			} else if (dp > 0) {
				
				int nextRing = neighborRing + 1;
				
				double startX = neighborRing * Math.cos(2 * Math.PI * (double)(neighborPosition) / cellsInNeighborRing);
				double startY = neighborRing * Math.sin(2 * Math.PI * (double)(neighborPosition) / cellsInNeighborRing);
				
				double endX = nextRing * Math.cos(2 * Math.PI * (double)(neighborPosition) / cellsInNeighborRing);
				double endY = nextRing * Math.sin(2 * Math.PI * (double)(neighborPosition) / cellsInNeighborRing);
				
				this.context.strokeLine((this.screenWidth / 2) + this.cellScale * startX,
									(this.screenHeight / 2) - this.cellScale * startY,
									(this.screenWidth / 2) + this.cellScale * endX,
									(this.screenHeight / 2) - this.cellScale * endY);
				
			}
		}
		
	}

	@Override
	public void fillCell(CellCircle cell) {
			
		int cellRing = cell.getRing();
		int cellPosition = cell.getPosition();
		
		int cellsInRing = this.maze.getCellsOfRing(cellRing);
		
		int nextRing = cellRing + 1;
		int nextPosition = (cellPosition + 1 + cellsInRing % cellsInRing);
		
		double [] xPoints = {
			this.screenWidth / 2 + cellScale * cellRing * Math.cos(2 * Math.PI * (double)(cellPosition) / cellsInRing),
			this.screenWidth / 2 + cellScale * nextRing * Math.cos(2 * Math.PI * (double)(cellPosition) / cellsInRing),
			this.screenWidth / 2 + cellScale * nextRing * Math.cos(2 * Math.PI * (double)(nextPosition) / cellsInRing),
			this.screenWidth / 2 + cellScale * cellRing * Math.cos(2 * Math.PI * (double)(nextPosition) / cellsInRing),
		};
		
		double [] yPoints = {
			this.screenHeight / 2 - cellScale * cellRing * Math.sin(2 * Math.PI * (double)(cellPosition) / cellsInRing),
			this.screenHeight / 2 - cellScale * nextRing * Math.sin(2 * Math.PI * (double)(cellPosition) / cellsInRing),	
			this.screenHeight / 2 - cellScale * nextRing * Math.sin(2 * Math.PI * (double)(nextPosition) / cellsInRing),	
			this.screenHeight / 2 - cellScale * cellRing * Math.sin(2 * Math.PI * (double)(nextPosition) / cellsInRing),	
		};
		
		this.context.fillPolygon(xPoints, yPoints, 4);
		
		this.drawCellWalls(cell);
	}

	@Override
	public void newMaze(int length, GenerationType generationType) {
		this.maze = new MazeCircle(length, 6, generationType);
		
		this.cellScale = Math.min(this.screenWidth / this.maze.getRadius(),
				this.screenHeight / this.maze.getRadius()) / 2;
	}

	@Override
	protected CellCircle getNextCell(CellCircle currentCell, String key) {
		
		int oldRing = currentCell.getRing();
		int oldPosition = currentCell.getPosition();
		
		int newRing = oldRing;
		int newPosition = oldPosition;
		
		boolean upLeft = false;
		
		switch (key) {
		case "LEFT":
		case "A":
			newPosition += 1;
			break;
		case "Q":
			upLeft = true;
		case "UP":
		case "W":
			newRing += 1;
			break;
		case "RIGHT":
		case "D":
			newPosition -= 1;
			break;
		case "DOWN":
		case "S":
			newRing -= 1;
			break;
		default:
			break;
		}
		
		if (newRing < 0 || newRing >= this.maze.getRadius()) {
			return null;
		}
		
		int oldRingSize = this.maze.getCellsOfRing(oldRing);
		int newRingSize = this.maze.getCellsOfRing(newRing);

		if (oldRingSize == newRingSize) {
			newPosition = (newPosition + oldRingSize) % oldRingSize;
		} else if (oldRingSize < newRingSize) {
			newPosition = 2 * newPosition + (upLeft ? 1 : 0);
		} else {
			newPosition /= 2;
		}
		
		CellCircle neighbor = this.maze.getCell(newRing, newPosition);
		
		if (!currentCell.isNeighbor(neighbor)) {
			return null;
		}
		
		return neighbor;
	}

}
