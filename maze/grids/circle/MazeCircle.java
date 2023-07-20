package maze.grids.circle;

import java.util.ArrayList;
import maze.MazeGrid;

public class MazeCircle extends MazeGrid<CellCircle> {

	final private int radius;
	final private int innerCells;
	
	final private int [] startLookup;
	final private int [] sizeLookup;
	
	public MazeCircle(int radius, int innerCells, GenerationType generationType) {
		super(MazeCircle.getSizeWithRadius(radius, innerCells));
		
		this.radius = radius;
		this.innerCells = innerCells;
		
		this.startLookup = new int [this.radius];
		this.sizeLookup = new int [this.radius];
		
		int runningTotal = 0;
		
		for (int i = 0; i < this.radius; i++) {
			
			int numCells = (int) Math.pow(2, Math.floor(Math.log(i + 1) / Math.log(2))) * this.innerCells;
			
			this.startLookup[i] = runningTotal;
			this.sizeLookup[i] = numCells;
			
			runningTotal += numCells;
		}
		
		this.generate(generationType);
	}

	public void fillGrid(ArrayList<CellCircle> grid) {
		
		grid.clear();
		
		int idx = 0;
		for (int ring = 0; ring < this.radius; ring++) {
			for (int position = 0; position < this.sizeLookup[ring]; position++) {
				grid.add(new CellCircle(idx++, ring, position));
			}
		}
	}
	
	@Override
	public ArrayList<CellCircle> allPossibleNeighbors(CellCircle cell) {
		
		int ring = cell.getRing();
		int position = cell.getPosition();
		int ringSize = this.getCellsOfRing(ring);

		ArrayList<CellCircle> possibleNeighbors = new ArrayList<CellCircle>();
		
		possibleNeighbors.add(this.getCell(ring, (position - 1 + ringSize) % ringSize));
		possibleNeighbors.add(this.getCell(ring, (position + 1) % ringSize));
		
		if (ring > 0) {
			
			// 2 outer cells might map to the same inner cell.
			
			// will either be 2 or 1
			int diff = this.sizeLookup[ring] / this.sizeLookup[ring - 1];
			possibleNeighbors.add(this.getCell(ring - 1, position / diff));
		}
		
		if (ring < radius - 1) {
			
			// 1 inner cell might map to 2 outer cells.
			
			if (this.sizeLookup[ring] == this.sizeLookup[ring + 1]) {
				possibleNeighbors.add(this.getCell(ring + 1, position));
			} else {
				possibleNeighbors.add(this.getCell(ring + 1, position * 2));
				possibleNeighbors.add(this.getCell(ring + 1, position * 2 + 1));
			}
		}

		return possibleNeighbors;
	}
	
	public CellCircle getCell(int ring, int position) {
		return this.getCell(this.startLookup[ring] + position);
	}
	
	public int getRadius() {
		return this.radius;
	}
	
	public int getCellsOfRing(int ring) {
		return this.sizeLookup[ring];
	}
		
	private static int getSizeWithRadius(int radius, int innerCells) {
		
		int runningTotal = 0;
		
		for (int i = 0; i < radius; i++) {
			int numCells = (int) Math.pow(2, Math.floor(Math.log(i + 1) / Math.log(2))) * innerCells;
			runningTotal += numCells;
		}
		
		return runningTotal;
	}
	
	// Ring 1 has n cells
	// Ring 2-3 has 2n cells
	// Rings 4-7 ring have 4n cells
	
	// Ring 2^x - 2^(x+1) - 1 have 2^x * n cells

}
