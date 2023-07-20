package maze;

import java.util.ArrayList;
import java.util.Iterator;

import ds.graph.general.IGraphNode;

public abstract class MazeCell<Cell extends MazeCell<Cell>> implements IGraphNode<Cell> {

	final private int idx;
	
	final private ArrayList<Cell> neighbors;
	
	public MazeCell (int idx) {
		this.idx = idx;
		this.neighbors = new ArrayList<Cell>();
	}
	
	public int getIdx() {
		return this.idx;
	}
	
	@Override
	public void addNeighbor(Cell neighbor) {
		this.neighbors.add(neighbor);
	}
	
	@Override
	public void removeNeighbor(Cell neighbor) {
		this.neighbors.remove(neighbor);
	}
	
	public boolean isNeighbor(Cell neighbor) {
		return this.neighbors.contains(neighbor);
	}
	
	@Override
	public int getDegree() {
		return this.neighbors.size();
	}
	
	public abstract int getEstimatedDistanceToCell(Cell neighbor);
	
	@Override
	public Iterator<Cell> iterator() {
		return this.neighbors.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(idx);
		return sb.toString();
	}
}
