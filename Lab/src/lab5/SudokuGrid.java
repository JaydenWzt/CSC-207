package lab5;

/**
 * A implementation of IGrid, description see IGrid
 * @author Zitan Wang, Mingguang Wang
 *
 */
public class SudokuGrid implements IGrid{
	
	/**
	 * The constructor of SudokuGrid
	 * It initializes SudokuBoxes
	 * @param x The width in term of boxes
	 * @param y The height in term of boxes
	 */
	public SudokuGrid(int x, int y)
	{
		this.WideBox = x;
		this.TallBox = y;
		SudokuBoxes = new IBox [x][y];
		for(int i=0; i<WideBox; i++)
		{
			for(int j=0; j<TallBox; j++)
			{
				SudokuBoxes[i][j] = new SudokuBox(y, x);
			}
		}
	}
	
	/**
	 * Obtains the IBox at zero-index position (x,y).
	 * @param x The x position.
	 * @param y The y position.
	 * @return Returns the IBox at the position (x,y) or null if (x,y) is out of bounds.
	 */
	public IBox Get(int x, int y)
	{
		if(x>=WideBox || y>=TallBox || x<0 || y<0)
			return null;
		return SudokuBoxes[x][y];
	}


	/**
	 * Determines if this grid is complete.
	 * <br><br>
	 * Assume, for the sake of clarity, that the values to be placed in this grid range from 1 to mn.
	 * The grid is complete when the following conditions hold:
	 * <ul>
	 * 	<li>Each box's Complete method returns true.</li>
	 * 	<li>
	 * 		Each row of this grid (this is all positions with the same box y value and y value within those boxes) contains each value 1 to mn precisely once.
	 * 		For example, if we call GetBox(x1,2) [x1 is any value in range] and then call Get(x2,3) [x2 is again any value in range], this should return a unique value for each choice of (x1,x2).
	 * 		In a regular m = n = 3 Sudoku game, this corresponds to having the values 1-9 in each row of the 9x9 grid of cells. 
	 * 	</li>
	 * 	<li>Each column of this grid should contain the values 1 to mn precisely once as described in the row condition above, only with the x and y coordinates swapped.</li>
	 * </ul>
	 * For more details, see Wikipedia's <a href="https://en.wikipedia.org/wiki/Sudoku">Sudoku</a> entry.
	 * @return Returns true if the above conditions are satisfied and false otherwise.
	 */
	public boolean Complete()
	{
		//check whether each box is complete,
		//if not, return false
		for(int i=0; i<WideBox; i++)
		{
			for(int j=0; j<TallBox; j++)
			{
				if(!SudokuBoxes[i][j].Complete())
					return false;
			}
		}
		
		//Check whether there are duplicate values horizontally
		//If yes, return false
		for(int i=0; i<CellWidth(); i++)
		{
			for(int j=0; j<CellHeight(); j++)
			{
				for(int k=j+1; k<CellHeight(); k++)
				{
					if(GetCell(i,j).equals(GetCell(i,k)))
						return false;
				}
			}
		}
		
		//Check whether there are duplicate values vertically
		//If yes, return false
		for(int j=0; j<CellHeight(); j++)
		{
			for(int i=0; i<CellWidth(); i++)
			{
				for(int k=i+1; k<CellWidth(); k++)
				{
					if(GetCell(i,j).equals(GetCell(k,j)))
						return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Obtains the width of this grid in terms of boxes.
	 * In the documentation for the interface, this corresponds to the parameter m.
	 */
	public int Width()
	{
		return WideBox;
	}
	
	/**
	 * Obtains the height of this grid in terms of boxes.
	 * In the documentation for the interface, this corresponds to the parameter n.
	 */
	public int Height()
	{
		return TallBox;
	}
	
	protected int WideBox;
	protected int TallBox;
	private IBox SudokuBoxes [][];

}
