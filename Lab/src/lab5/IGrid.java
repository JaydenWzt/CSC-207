package lab5;

/**
 * A Sudoku grid of width m and height n IBoxes each of width n and height m.
 * Otherwise put, the grid has a total width mn cells and a total height of nm cells.
 * The game is generally played with m = n = 3.
 * To win the game, each row, column, and IBox must contain each value 1-mn precisely once.
 * In a normal game where m = n = 3, this corresponds to having the values 1-9.
 * Note that there is no reason why these values need be numbers, and you should account for this.
 * @author Dawn Nye
 */
public interface IGrid
{
	/**
	 * Obtains the IBox at zero-index position (x,y).
	 * @param x The x position.
	 * @param y The y position.
	 * @return Returns the IBox at the position (x,y) or null if (x,y) is out of bounds.
	 */
	public IBox Get(int x, int y);
	
	/**
	 * Obtains the cell at position (x,y) when the grid's boxes are treated with a contiguous coordinate system.
	 * For example, a regular call to Get(1,2).Get(0,1) with BoxWidth = BoxHeight = 3 
	 * (and thus we have boxes of cell width and cell height 3) is equivalent to GetCell(3,7).
	 * @param x The x position in terms of cells.
	 * @param y The y position in terms of cells.
	 * @return Returns the cell at position (x,y) or null if no such cell exists yet or (x,y) is out of bounds. 
	 */
	public default ICell GetCell(int x, int y)
	{
		if(x>=CellWidth() || y>=CellHeight())
			return null;
		
		int x_box, y_box, x_cell, y_cell;
		
		//get in indexes in terms of box and
		//in terms of inside that box
		x_box = x/Width();
		y_box = y/Height();
		x_cell = x%Width();
		y_cell = y%Width();
		
		if(Get(x_box, y_box)==null)
			return null;
		return Get(x_box,y_box).Get(x_cell, y_cell);
	}
	
	/**
	 * Sets the cell at position (x,y) when the grid's boxes are treated with a contiguous coordinate system.
	 * For example, a regular call to Get(1,2).Set(c,0,1) with BoxWidth = BoxHeight = 3 
	 * (and thus we have boxes of cell width and cell height 3) is equivalent to SetCell(c,3,7).
	 * If (x,y) is out of bounds, this does nothing and fails silently.
	 * @param c The cell to put into the grid at position (x,y). This value can be null to clear the position.
	 * @param x The x position in terms of cells.
	 * @param y The y position in terms of cells. 
	 */
	public default void SetCell(ICell c, int x, int y)
	{
		if(x>=CellWidth() || y>=CellHeight())
			return;
		
		int x_box, y_box, x_cell, y_cell;
		
		//get in indexes in terms of box and
		//in terms of inside that box
		x_box = x/Width();
		y_box = y/Height();
		x_cell = x%Width();
		y_cell = y%Width();
		
		if(Get(x_box, y_box)!=null)
		{
			Get(x_box, y_box).Set(c, x_cell, y_cell);
		}
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
	public boolean Complete();
	
	/**
	 * Obtains the width of this grid in terms of boxes.
	 * In the documentation for the interface, this corresponds to the parameter m.
	 */
	public int Width();
	
	/**
	 * Obtains the height of this grid in terms of boxes.
	 * In the documentation for the interface, this corresponds to the parameter n.
	 */
	public int Height();
	
	/**
	 * Obtains the width of this grid's boxes.
	 * In the documentation for the interface, this corresponds to the parameter n.
	 */
	public default int BoxWidth()
	{return Height();}
	
	/**
	 * Obtains the height of this grid's boxes.
	 * In the documentation for the interface, this corresponds to the parameter m.
	 */
	public default int BoxHeight()
	{return Width();}
	
	/**
	 * Obtains the total width of this grid in terms of cells.
	 * In the documentation for the interface, this corresponds to the parameter product mn.
	 * This value is always equal to CellHeight().
	 */
	public default int CellWidth()
	{return Width() * Height();}
	
	/**
	 * Obtains the total width of this grid in terms of cells.
	 * In the documentation for the interface, this corresponds to the parameter product mn.
	 * This value is always equal to CellWidth().
	 */
	public default int CellHeight()
	{return Width() * Height();}
}