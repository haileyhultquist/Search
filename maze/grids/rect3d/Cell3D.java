package maze.grids.rect3d;

import maze.MazeCell;

public class Cell3D extends MazeCell<Cell3D> {

	final private int y;
	final private int x;
	final private int z;
	
	public Cell3D(int idx, int y, int x, int z) {
		super(idx);
		
		this.y = y;
		this.x = x;
		this.z = z;
	}

	public int getY() {
		return this.y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public int getEstimatedDistanceToCell(Cell3D cell) {
		
		return Math.abs(this.getX() - cell.getX()) 
				+ Math.abs(this.getY() - cell.getY())
				+ Math.abs(this.getZ() - cell.getZ());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		sb.append(y);
		sb.append(", ");
		sb.append(x);
		sb.append(", ");
		sb.append(z);
		sb.append(")");
		
		return sb.toString();
	}
}
