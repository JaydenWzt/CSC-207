package lab5;
/**
 * Implementation of ICell, description see ICell
 * This class includes a override of .equals
 * @author Zitan Wang, Mingguang Wang
 *
 */
public class NumberCell implements ICell{
	
	/**
	 * Constructor of NumberCell
	 * @param num The number to be stored
	 */
	public NumberCell(int num)
	{
		this.num = num;
	}
	
	/**
	 * It converts num into String and returns the String
	 * @return String The String version of num
	 */
	public String Value()
	{
		return Integer.toString(num);
	}
	
	/**
	 * Cells with the same Value() are equal, and calls to .equals between them should return true.
     * Cells with different Value() values are not equal, so .equals calls between them should return false.
	 * @return boolean Whether the two objects are equal
	 */
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ICell))
			return false;
		return this.Value().equals(((ICell) obj).Value());
	}
	
	protected int num;
}
