package maze.grids.rect2d;

import maze.MazeCell;

public class Cell2D extends MazeCell<Cell2D> {

	final private int y;
	final private int x;
	
	public Cell2D(int idx, int y, int x) {
		super(idx);
		
		this.y = y;
		this.x = x;
	}

	public int getY() {
		return this.y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getEstimatedDistanceToCell(Cell2D cell) {
		
		return Math.abs(this.getX() - cell.getX()) 
				+ Math.abs(this.getY() - cell.getY());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.getX());
		sb.append(", ");
		sb.append(this.getY());
		sb.append(")");
		return sb.toString();
	}
}
