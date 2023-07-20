package maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public abstract class MazeGrid<Cell extends MazeCell<Cell>> {

	public enum GenerationType {
		ALDOUS_BRODER,
		WILSON,
		HUNT_AND_KILL,
		RECURSIVE_BACKTRACKING,
		KRUSKAL
	}
	
	public enum Shape {
		RECTANGLE_2D,
		CIRCLE,
		RECTANGLE_3D
	}
	
	final static private Random rng = new Random(100);
	
	final private ArrayList<Cell> grid;
	final private int size;
	
	public abstract void fillGrid(ArrayList<Cell> grid);
	
	public abstract ArrayList<Cell> allPossibleNeighbors(Cell cell);
	
	public MazeGrid(int size) {
		this.size = size;
		this.grid = new ArrayList<Cell>(this.size);
	}
	
	public void generate(GenerationType generationType) {
		
		this.fillGrid(grid);
		
		switch (generationType) {
		case ALDOUS_BRODER:
			aldousBroderGeneration();
			break;
		case RECURSIVE_BACKTRACKING:
			recursiveBacktrackingGeneration();
			break;
		
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public Cell getCell(int idx) {
		return this.grid.get(idx);
	}
	
	public int size() {
		return this.size;
	}
	
	private void aldousBroderGeneration() {
		
		boolean [] visited = new boolean [this.grid.size()];
		int remaining = this.grid.size();
		
		// Choose random location to start
		Cell currentLocation = this.grid.get(MazeGrid.rng.nextInt(remaining));
		
		// Mark the first cell as visited
		
		visited[currentLocation.getIdx()] = true;
		remaining--;
		
		// Random walk until all locations have been visited.
		
		while (remaining > 0) {
			
			ArrayList<Cell> neighbors = this.allPossibleNeighbors(currentLocation);
			Cell nextNeighbor = neighbors.get(MazeGrid.rng.nextInt(neighbors.size()));
			
			if (!visited[nextNeighbor.getIdx()]) {
				currentLocation.addNeighbor(nextNeighbor);
				nextNeighbor.addNeighbor(currentLocation);
				
				visited[nextNeighbor.getIdx()] = true;
				remaining--;
			}
			
			currentLocation = nextNeighbor;
		}
	}
	
	private void recursiveBacktrackingGeneration() {

		Stack<Cell> stack = new Stack<Cell>();
		boolean [] visited = new boolean [grid.size()];
		
		// Choose random location to start
		stack.push(this.grid.get(MazeGrid.rng.nextInt(grid.size())));
		
		while (!stack.isEmpty()) {
			
			Cell currentLocation = stack.peek();
			visited[currentLocation.getIdx()] = true;
			
			ArrayList<Cell> possibleNeighbors = this.allPossibleNeighbors(currentLocation);
			Collections.shuffle(possibleNeighbors, rng);
			
			boolean foundNeighbor = false;
			
			for (Cell neighbor : possibleNeighbors) {
				
				if (visited[neighbor.getIdx()]) {
					continue;
				}
				
				foundNeighbor = true;
				
				currentLocation.addNeighbor(neighbor);
				neighbor.addNeighbor(currentLocation);
				stack.push(neighbor);
				
				break;
			}
			
			if (!foundNeighbor) {
				stack.pop();
			}
		}
	}
}
