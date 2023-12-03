package lab5;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Runs the game.
 * @author Dawn Nye
 */
public class Bootstrap
{
	/**
	 * Boots the game logic.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args)
	{
		// The grid
		IGrid grid = new SudokuGrid(BOX_WIDTH,BOX_HEIGHT);
		
		// Populate the initial grid locations
		LinkedList<Triple> l = new LinkedList<Triple>();
		
		// We could do this randomly, but whatever
		l.add(new Triple(0,0,8));
		l.add(new Triple(2,0,1));
		l.add(new Triple(2,1,9));
		l.add(new Triple(2,0,2));
		l.add(new Triple(1,2,5));
		
		l.add(new Triple(5,0,3));
		l.add(new Triple(5,1,7));
		l.add(new Triple(3,2,1));
		
		l.add(new Triple(6,0,9));
		l.add(new Triple(8,0,6));
		l.add(new Triple(6,1,8));
		l.add(new Triple(7,1,5));
		l.add(new Triple(8,1,1));
		l.add(new Triple(6,2,4));
		
		l.add(new Triple(0,3,5));
		l.add(new Triple(0,4,7));
		l.add(new Triple(1,4,6));
		l.add(new Triple(1,5,3));
		l.add(new Triple(2,5,2));
		
		l.add(new Triple(4,3,6));
		l.add(new Triple(5,3,1));
		l.add(new Triple(3,4,8));
		l.add(new Triple(4,4,3));
		
		l.add(new Triple(6,3,7));
		l.add(new Triple(8,3,4));
		
		l.add(new Triple(1,6,2));
		l.add(new Triple(2,7,5));
		
		l.add(new Triple(4,6,1));
		l.add(new Triple(5,6,9));
		l.add(new Triple(3,8,4));
		l.add(new Triple(4,8,5));
		l.add(new Triple(5,8,2));
		
		l.add(new Triple(6,6,5));
		l.add(new Triple(6,7,3));
		l.add(new Triple(8,7,2));
		l.add(new Triple(6,8,1));
		l.add(new Triple(7,8,9));
		l.add(new Triple(8,8,7));
		
		for(Triple t : l)
			grid.SetCell(new NumberCell(t.Item3),t.Item1,t.Item2);
		
		// Print the initial board
		PrintBoard(grid);
		
		// Loop until we're done
		while(!grid.Complete())
		{
			// Get an input
			Triple t = GetNextInput();
			
			if(t == null)
			{
				System.out.println("Game Terminated");
				return;
			}
			
			if(l.contains(t))
			{
				System.err.println("You cannot change an initial value on the grid. Try again.");
				continue;
			}
			
			grid.SetCell(t.Item3 < 1 ? null : new NumberCell(t.Item3),t.Item1,t.Item2);
			PrintBoard(grid);
		}
		
		System.out.println("\nVICTORY");
		return;
	}
	
	/**
	 * Prints the grid to the console.
	 * @param grid The grind to print.
	 */
	protected static void PrintBoard(IGrid grid)
	{
		String[][] board = new String[grid.CellWidth() + 2][grid.CellHeight() + 2];
		int max_len = Math.max(Integer.toString(BOX_WIDTH).length(),Integer.toString(BOX_HEIGHT).length());
		
		for(int i = 0;i < 2;i++)
			for(int j = 0;j < 2;j++)
				board[i][j] = "";
		
		for(int i = 2;i < board.length;i++)
		{
			board[i][0] = Integer.toString(i - 2);
			board[i][1] = "";
		}
		
		for(int i = 2;i < board[0].length;i++)
		{
			board[0][i] = Integer.toString(i - 2);
			board[1][i] = "";
		}
		
		// Convert everything to a String and find the maximum String length
		for(int i = 0;i < grid.CellWidth();i++)
			for(int j = 0;j < grid.CellHeight();j++)
			{
				ICell c = grid.GetCell(i,j);
				board[i + 2][j + 2] = c == null ? "." : c.Value();
				
				if(board[i][j].length() > max_len)
					max_len = board[i + 2][j + 2].length();
			}
		
		// Pad things that are too short
		for(int i = 0;i < board.length;i++)
			for(int j = 0;j < board[i].length;j++)
				while(board[i][j].length() < max_len)
					board[i][j] = " " + board[i][j];
		
		// Print the board
		for(int j = 0;j < board[0].length;j++)
		{
			for(int i = 0;i < board.length;i++)
			{
				System.out.print(board[i][j]);
				
				if(i < board.length - 1)
					System.out.print(" ");
				else
					System.out.println();
			}
		}
		
		System.out.println();
		return;
	}
	
	/**
	 * Gets the next input (x,y,value) to put into a Sudoku grid.
	 * The third item value should be placed at position (x,y).
	 * If q is entered alone as an input, this returns null to indicate the game should end.
	 */
	protected static Triple GetNextInput()
	{
		while(true)
		{
			System.out.println("Enter a zero-index position (x,y) (two numbers seperated by white space) and number to place at this cell position in the grid. A number less than 1 for the last value will clear the cell.");
			
			if(!sin.hasNextInt())
			{
				// Consume the invalid input
				if(sin.next().equals("q"))
					return null;
				
				System.err.println("The first input must be a number.");
				continue;
			}
			
			int x = sin.nextInt();
			
			if(!sin.hasNextInt())
			{
				// Consume the invalid input
				if(sin.next().equals("q"))
					return null;
				
				System.err.println("The second input must be a number.");
				continue;
			}
			
			int y = sin.nextInt();
			
			if(!sin.hasNextInt())
			{
				// Consume the invalid input
				if(sin.next().equals("q"))
					return null;
				
				System.err.println("The third input must be a number.");
				continue;
			}
			
			return new Triple(x,y,sin.nextInt());
		}
	}
	
	/**
	 * The number of boxes wide the grid will be.
	 */
	public static final int BOX_WIDTH = 3;
	
	/**
	 * The number of boxes tall the grid will be.
	 */
	public static final int BOX_HEIGHT = 3;
	
	/**
	 * Used to read input.
	 */
	protected static final Scanner sin = new Scanner(System.in);
	
	/**
	 * Contains a triple of ints.
	 * @author Dawn Nye
	 */
	protected static class Triple
	{
		/**
		 * Creates a new triple (a,b,c).
		 */
		public Triple(int a, int b, int c)
		{
			Item1 = a;
			Item2 = b;
			Item3 = c;
			
			return;
		}
		
		@Override public boolean equals(Object obj)
		{
			if(obj instanceof Triple)
				return Item1 == ((Triple) obj).Item1 && Item2 == ((Triple) obj).Item2;
			
			return false;
		}
		
		public int Item1;
		public int Item2;
		public int Item3;
	}
}