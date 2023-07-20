package maze;

import java.util.ArrayList;

import ds.Tuple;
import search.Search;
import search.SearchType;

public class MazeSearch<Grid extends MazeGrid<Cell>, Cell extends MazeCell<Cell>> extends Search<Cell, Cell> {

	final private Cell startingState;
	final private Cell goalState;
	
	public MazeSearch(Cell startingState, Cell goalState, SearchType searchType) {
		super(searchType);
		
		this.startingState = startingState;
		this.goalState = goalState;
	}
	
	@Override
	public ArrayList<Tuple<Cell, Double>> getActions(Cell state) {
		
		ArrayList<Tuple<Cell, Double>> actions = new ArrayList<Tuple<Cell, Double>>();
		for (Cell neighbor : state) {
			actions.add(new Tuple<Cell, Double>(neighbor, 1.0));
		}
		
		return actions;
	}

	@Override
	public boolean isGoal(Cell state) {
		return state == goalState;
	}

	public ArrayList<Cell> search() {
		return this.search(startingState);
	}
	
	@Override
	public double aStarHeuristic(Cell state) {
		return state.getEstimatedDistanceToCell(this.goalState);
	}
}
