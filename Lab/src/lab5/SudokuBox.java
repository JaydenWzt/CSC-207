package lab5;

/**
 * A implementation of IBox, description see IBox
 * @author Zitan Wang, Mingguang Wang
 * */
public class SudokuBox implements IBox{
	
	/**
	 * constructor for SudokuBox
	 * @param wide The n in the comment above
	 * @param tall The m in the comment above
	 */
	public SudokuBox(int wide, int tall)
	{
		this.WideCell = wide;
		this.TallCell = tall;
		SudokuCells = new ICell [WideCell][TallCell];
	}
	
	/**
	 * Gets the contents of the box stored at the zero-index position (x,y).
	 * @param x The x position.
	 * @param y The y position.
	 * @return Returns the cell stored at position (x,y) or null if no cell currently occupies that position. Also returns null if (x,y) is out of bounds.
	 */
	public ICell Get(int x, int y)
	{
		if(x>=WideCell || y>=TallCell)
			return null;
		else
			return SudokuCells[x][y];
	}
	
	/**
	 * Sets the contents of the box at the zero-index position (x,y).
	 * If this position is out of bounds, this function does nothing and fails silently.
	 * @param c The cell to place at the position (x,y). If this value is null, it clears the cell.
	 * @param x The x position.
	 * @param y The y position.
	 */
	public void Set(ICell c, int x, int y)
	{
		if(x < WideCell && y <TallCell && x >= 0 && y >= 0)
			SudokuCells[x][y] = c;
	}
	
	/**
	 * Determines if this box is complete.
	 * @return Returns true if each position in this box contains a cell and no value is duplicated. Returns false otherwise.
	 */
	public boolean Complete()
	{
		//check whether all elements in SudokuCells are not null,
		//if not return false
		for(int i=0; i<WideCell; i++)
		{
			for(int j=0; j<TallCell; j++)
			{
				if(!(SudokuCells[i][j] instanceof ICell))
					return false;
			}
		}
		
		//check whether there are repetitive values in SudokuCells,
		//if yes, return false
		for(int i=0; i<WideCell; i++)
		{
			for(int j=0; j<TallCell; j++)
			{
				for(int m=i; m<WideCell; m++)
				{
					for(int n=j; n<TallCell; n++)
					{
						if(m==i && n==j)
							continue;
						else
						{
							if(SudokuCells[i][j].equals(SudokuCells[m][n]))
								return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Obtains the width of this box.
	 */
	public int Width()
	{
		return WideCell;
	}
	
	/**
	 * Obtains the height of this box.
	 */
	public int Height()
	{
		return TallCell;
	}
	
	protected int WideCell;
	protected int TallCell;
	protected ICell SudokuCells [][];

}
