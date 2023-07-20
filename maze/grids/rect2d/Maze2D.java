package maze.grids.rect2d;

import java.util.ArrayList;

import maze.MazeGrid;

public class Maze2D extends MazeGrid<Cell2D> {

	final private int height;
	final private int width;
	
	public Maze2D (int height, int width, GenerationType generationType) {
		super(height * width);
		
		this.height = height;
		this.width = width;
		
		this.generate(generationType);
	}
	
	public void fillGrid(ArrayList<Cell2D> grid) {
		
		grid.clear();
		
		for (int i = 0; i < this.size(); i++) {
			grid.add(new Cell2D(i, i % this.height, i / this.height));
		}
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public Cell2D getCell(int y, int x) {
		return this.getCell(y + this.height * x);
	}

	@Override
	public ArrayList<Cell2D> allPossibleNeighbors(Cell2D cell) {
		
		ArrayList<Cell2D> neighbors = new ArrayList<Cell2D>();
		
		int y = cell.getY();
		int x = cell.getX();
		
		if (x > 0) {
			neighbors.add(this.getCell(y, x - 1));
		}
		
		if (x < this.width - 1) {
			neighbors.add(this.getCell(y, x + 1));
		}
		
		if (y > 0) {
			neighbors.add(this.getCell(y - 1, x));
		}
		
		if (y < this.height - 1) {
			neighbors.add(this.getCell(y + 1, x));
		}
		
		return neighbors;
	}
}
