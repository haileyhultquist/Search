package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import ds.Tuple;

public abstract class Search<State, Action> {

	final private SearchQueue sq;
	
	public Search(SearchType searchType) {
		
		SearchQueue sq = null;
		
		switch(searchType) {
		case DEPTH_FIRST_Q:
			sq = new SearchQueueDFS();
			break;
		case DIJKSTRA:
			sq = new SearchQueueDijkstra();
			break;
		case A_STAR:
			sq = new SearchQueueAStar();
			break;
		default:
			break;
		}
		
		this.sq = sq;
	}
	
	public abstract ArrayList<Tuple<State, Double>> getActions(State state);
	
	public abstract double aStarHeuristic(State state);
	
	public abstract boolean isGoal(State state);
	
	public void initSearch(State startingState) {
		this.sq.clear();
		this.sq.addToQueue(startingState, null, 0, this.sq.getPriority(startingState, 0));
	}
	
	public ArrayList<State> search(State startingState) {

		this.initSearch(startingState);
		
		State solutionState = null;
		
		while (solutionState == null && !this.sq.isEmpty()) {
			solutionState = this.searchStep();
		}
		
		if (solutionState == null) {
			System.out.println("No Solution");
			return null;
		}
		
		ArrayList<State> backtrack = this.backtrack(solutionState);
		
		ArrayList<State> solution = new ArrayList<State>();
		
		for (int i = backtrack.size() - 1; i >= 0; i--) {
			solution.add(backtrack.get(i));
		}
		
		return solution;
	}
	
	public State searchStep() {
		
		State state = this.sq.pop();
		
		// Don't need to check for visited because it would not have gone into the queue if it was a duplicate.
		
		if (this.isGoal(state)) {
			return state;
		}
		
		this.sq.addSuccessorsToQueue(state);
		
		return null;
	}

	public ArrayList<State> backtrack(State state) {
		return this.sq.backtrack(state);
	}
	
	public State peekAtQueue() {
		return this.sq.peek();
	}
	
	private class SearchQueueDFS extends SearchQueue {
		
		public SearchQueueDFS() {
			super(new PriorityQueue<SQElement>());
		}
		
		@Override
		public double getPriority(State state, double cost) {
			return -cost;
		}
	}
	
	private class SearchQueueDijkstra extends SearchQueue {
		
		public SearchQueueDijkstra() {
			super(new PriorityQueue<SQElement>());
		}
		
		@Override
		public double getPriority(State state, double cost) {
			return cost;
		}
	}
	
	private class SearchQueueAStar extends SearchQueue {

		public SearchQueueAStar() {
			super(new PriorityQueue<SQElement>());
		}
		
		@Override
		public double getPriority(State state, double cost) {
			return cost + aStarHeuristic(state);
		}
	}
	
	private abstract class SearchQueue {

		// Priority queue with a hash map to each entry
		// so that you can change the parent of an element easily.
		
		final protected Queue<SQElement> sq;
		final protected HashMap<State, SQElement> map;
		
		public SearchQueue (Queue<SQElement> sq) {
			this.sq = sq;
			this.map = new HashMap<State, SQElement>();
		}
		
		public abstract double getPriority(State state, double cost);
		
		public void addSuccessorsToQueue(State state) {
			
			double cost = this.getCost(state);
			
			ArrayList<Tuple<State, Double>> actions = getActions(state);
			
			for (Tuple<State, Double> action : actions) {
				State nextState = action.x;
				this.addToQueue(nextState, state, cost + action.y, this.getPriority(nextState, cost));
			}
		}
				
		public void addToQueue(State state, State parentState, double cost, double priority) {
			
			SQElement element = this.map.get(state);
			
			// check if it is already in the queue
			if (element == null) {
				// if it is not, you can just add it
				element = new SQElement(state, parentState, cost, priority);
				this.sq.add(element);
				this.map.put(state, element);
			} else {
				// if it is, maybe replace it.
				if (element.isVisited()) {
					if (cost < element.getCost()) {
						// I think this means your heuristic wasn't good or something
						throw new IllegalArgumentException();
					}
					// otherwise it just means you found a loop
				} else {
					if (cost < element.getCost()) {
						
						// In order for the priority to be updated in the queue,
						// you have to remove the element and reinsert it.
						
						this.sq.remove(element);
						element.newParent(parentState, cost, priority);
						this.sq.add(element);
					}
				}
			}
		}
		
		public State pop() {
			SQElement queueElement = this.sq.poll();
			queueElement.markVisited();
			
			return queueElement.getState();
		}
		
		public State peek() {
			return this.isEmpty() ? null : this.sq.peek().getState();
		}
		
		public ArrayList<State> backtrack(State state) {
			
			ArrayList<State> solution = new ArrayList<State>();
			
			while (state != null) {
				solution.add(state);
				state = this.map.get(state).getParentState();
			}
			
			return solution;
		}

		public double getCost(State state) {
			return this.map.get(state).getCost();
		}
		
		public void clear() {
			this.sq.clear();
			this.map.clear();
		}
		
		public boolean isEmpty() {
			return this.sq.isEmpty();
		}
	}

	private class SQElement implements Comparable<SQElement> {
		
		final private State state;
		
		// Parent state; used for backtracking
		private State parentState;
		
		// Whether this state has already been expanded
		private boolean visited;
		
		// Cost to reach this state so far
		private double cost;
		
		// The priority given for this state in a PQ; not always used.
		// For A-Star, this is the cost + the estimated remaining cost.
		// For BFS and DFS, this is unused.
		private double priority;
		
		SQElement (State state, State parentState, double cost, double priority) {
			
			this.state = state;
			
			this.parentState = parentState;
			
			this.cost = cost;
			this.priority = priority;
			
			this.visited = false;
		}
		
		State getState() {
			return this.state;
		}
		
		State getParentState() {
			return this.parentState;
		}
		
		double getCost() {
			return this.cost;
		}
		
		@SuppressWarnings("unused")
		double getPriority() {
			return this.priority;
		}
		
		boolean isVisited() {
			return this.visited;
		}
		
		void markVisited() {
			this.visited = true;
		}
		
		void newParent(State parentState, double cost, double priority) {
			this.parentState = parentState;
			this.cost = cost;
			this.priority = priority;
		}
		
		@Override
		public int compareTo(SQElement element) {
			return Double.compare(this.priority, element.priority);
		}
	}
	
}


