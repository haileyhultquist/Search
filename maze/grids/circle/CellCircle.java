package maze.grids.circle;

import maze.MazeCell;

public class CellCircle extends MazeCell<CellCircle> {

	final private int ring;
	final private int position;
	
	public CellCircle(int idx, int ring, int position) {
		super(idx);
		
		this.ring = ring;
		this.position = position;
	}

	public int getRing() {
		return this.ring;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public int getEstimatedDistanceToCell(CellCircle cell) {
		return 0;
	}
}
