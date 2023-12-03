package lab5;

/**
 * Represents a cell in a Sudoku game.
 * Cells with the same Value() are equal, and calls to .equals between them should return true.
 * Cells with different Value() values are not equal, so .equals calls between them should return false.
 * @author Dawn Nye
 */
public interface ICell
{
	/**
	 * The value of the cell.
	 * @return Returns a string representing the value of the cell.
	 */
	public String Value();
}