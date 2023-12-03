package lab5;

/**
 * The abstract representation of a box within a larger grid in a Sudoku game.
 * The box has dimensions n (width) by m (height) with an ICell at each zero-index position (x,y).
 * For the box to be properly filled, there must be no more than one of each value in the cells within this box.
 * For example, a 3x3 box in a standard Sudoku game will contain the values 1-9.
 * @author Dawn Nye
 */
public interface IBox
{
	/**
	 * Gets the contents of the box stored at the zero-index position (x,y).
	 * @param x The x position.
	 * @param y The y position.
	 * @return Returns the cell stored at position (x,y) or null if no cell currently occupies that position. Also returns null if (x,y) is out of bounds.
	 */
	public ICell Get(int x, int y);
	
	/**
	 * Sets the contents of the box at the zero-index position (x,y).
	 * If this position is out of bounds, this function does nothing and fails silently.
	 * @param c The cell to place at the position (x,y). If this value is null, it clears the cell.
	 * @param x The x position.
	 * @param y The y position.
	 */
	public void Set(ICell c, int x, int  y);
	
	/**
	 * Determines if this box is complete.
	 * @return Returns true if each position in this box contains a cell and no value is duplicated. Returns false otherwise.
	 */
	public boolean Complete();
	
	/**
	 * Obtains the width of this box.
	 */
	public int Width();
	
	/**
	 * Obtains the height of this box.
	 */
	public int Height();
}