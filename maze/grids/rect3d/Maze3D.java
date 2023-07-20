package maze.grids.rect3d;

import java.util.ArrayList;

import maze.MazeGrid;

public class Maze3D extends MazeGrid<Cell3D> {

	final private int height;
	final private int width;
	final private int length;
	
	public Maze3D (int height, int width, int length, GenerationType generationType) {
		super(height * width * length);
		
		this.height = height;
		this.width = width;
		this.length = length;
		
		this.generate(generationType);
	}
	
	public void fillGrid(ArrayList<Cell3D> grid) {
		
		grid.clear();
		
		for (int i = 0; i < this.size(); i++) {
			
			int z  = i / (this.height * this.width);
			int x = (i % (this.height * this.width)) / this.height;
			int y = (i % (this.height * this.width)) % this.height;
			
			grid.add(new Cell3D(i, y, x, z));
		}
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public Cell3D getCell(int y, int x, int z) {
		return this.getCell(y + this.height * x + this.height * this.width * z);
	}

	@Override
	public ArrayList<Cell3D> allPossibleNeighbors(Cell3D cell) {
		
		ArrayList<Cell3D> neighbors = new ArrayList<Cell3D>();
		
		int y = cell.getY();
		int x = cell.getX();
		int z = cell.getZ();
		
		if (x > 0) {
			neighbors.add(this.getCell(y, x - 1, z));
		}
		
		if (x < this.width - 1) {
			neighbors.add(this.getCell(y, x + 1, z));
		}
		
		if (y > 0) {
			neighbors.add(this.getCell(y - 1, x, z));
		}
		
		if (y < this.height - 1) {
			neighbors.add(this.getCell(y + 1, x, z));
		}
		
		if (z > 0) {
			neighbors.add(this.getCell(y, x, z - 1));
		}
		
		if (z < this.length - 1) {
			neighbors.add(this.getCell(y, x, z + 1));
		}
		
		return neighbors;
	}
}
