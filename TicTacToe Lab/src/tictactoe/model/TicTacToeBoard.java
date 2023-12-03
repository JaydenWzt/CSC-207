package tictactoe.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import gamecore.datastructures.vectors.Vector2i;
import gamecore.observe.IObserver;

/**
 * The outline of a Tic Tac Toe board (not necessarily a 3x3 board).
 * Positions are zero indexed in along both the x and y axis.
 * Each time a piece is placed or removed, an event is issued.
 * When the game ends, an OnCompleted event is NOT issued.
 * Instead, an appropriate ordinary event is issued since the game is allowed to be reset.
 */
public class TicTacToeBoard implements ITicTacToeBoard{

	/**
	 * The default constructor
	 */
	public TicTacToeBoard()
	{
		this(3,3,3);
	}
	
	/**
	 * The constructor to construct a new board, used for only testing purpose (flawed)
	 * @param width The width of the board
	 * @param height The height of the board
	 * @param winningLength The winninglength of the board
	 * @param itr The iterator with all Pieces to be placed onto the board
	 */
	public TicTacToeBoard(int width, int height, int winningLength, Iterator<PieceType> itr)
	{
		this(width, height, winningLength);
		for(int i = 0; i < Height; i++)
			for(int j = 0; j < Width; j++)
				Board [i][j] = itr.next();
	}
	
	/**
	 * The constructor to construct a new board
	 * @param width The width of the board
	 * @param height The height of the board
	 * @param winningLength The winninglength of the board
	 */
	public TicTacToeBoard(int width, int height, int winningLength)
	{
		this.Height = height;
		this.Width = width;
		this.WinningLength = winningLength;
		this.Board = new PieceType[Height][Width];
		for(int i = 0; i < Height; i++)//set all cells to NONE
			for(int j = 0; j < Width; j++)
				Board [i][j] = PieceType.NONE;
	}
	/**
	 * Gets the item at {@code index}.
	 * @param index The index to retrieve.
	 * @return Returns the item at {@code index}.
	 * @throws IndexOutOfBoundsException Thrown if {@code index} is out of bounds.
	 * @throws NoSuchElementException Thrown if no element exists at ({@code x},{@code y}).
	 * @throws NullPointerException Thrown if {@code index} is null.
	 */
	@Override
	public PieceType Get(Vector2i index) {
		if(index == null)
			throw new NullPointerException();
		if(index.X >= Width || index.Y >= Height || index.X < 0 || index.Y < 0)
			throw new IndexOutOfBoundsException();
		if(Board[index.Y][index.X] == null)
			throw new NoSuchElementException();

		return Board[index.Y][index.X];
	}

	/**
	 * Sets the item at {@code index} to {@code t}.
	 * @param t The item to place at {@code index}.
	 * @param index The index to place {@code t} in.
	 * @return Returns the value placed into the grid so that this can be used like an assignment operator like a civilized language.
	 * @throws IndexOutOfBoundsException Thrown if {@code index} is out of bounds.
	 * @throws NullPointerException Thrown {@code index} is null or if {@code t} is null and the implementing class does not permit null entries.
	 */
	@Override
	public PieceType Set(PieceType t, Vector2i index) {
		if(index == null || t == null)
			throw new NullPointerException();
		if(index.X >= Width || index.Y >= Height || index.X < 0 || index.Y < 0)
			throw new IndexOutOfBoundsException();
		
		Board[index.Y][index.X] = t;
		return t;
	}

	/**
	 * Removes the item (if any) at {@code index}.
	 * @param index The index to obliterate.
	 * @return Returns true if this grid was modified as a result of this call.
	 * @throws IndexOutOfBoundsException Thrown if {@code index} is out of bounds.
	 * @throws NullPointerException Thrown if {@code index} is null.
	 */
	@Override
	public boolean Remove(Vector2i index) {
		if(index == null)
			throw new NullPointerException();
		if(index.X >= Width || index.Y >= Height || index.X < 0 || index.Y < 0)
			throw new IndexOutOfBoundsException();
		
		int count = Count();
		Board[index.Y][index.X] = PieceType.NONE;
		return Count() == count - 1;
	}

	/**
	 * Determines if the cell at {@code index} is occupied.
	 * @param index The cell to check for occupation.
	 * @return Returns true if the cell is occupied and false otherwise.
	 * @throws IndexOutOfBoundsException Thrown in {@code index} is out of bounds.
	 * @throws NullPointerException Thrown if {@code index} is null.
	 */
	@Override
	public boolean IsCellOccupied(Vector2i index) {
		if(index == null)
			throw new NullPointerException();
		if(index.X >= Width || index.Y >= Height || index.X < 0 || index.Y < 0)
			throw new IndexOutOfBoundsException();
		
		return !IsEmptyIndex(index.Y, index.X);
	}

	/**
	 * Determines if the cell at {@code index} is vacant.
	 * @param index The cell to check for vacancy.
	 * @return Returns true if the cell is vacant and false otherwise.
	 * @throws IndexOutOfBoundsException Thrown in {@code index} is out of bounds.
	 * @throws NullPointerException Thrown if {@code index} is null.
	 */
	@Override
	public boolean IsCellEmpty(Vector2i index) {
		if(index == null)
			throw new NullPointerException();
		if(index.X >= Width || index.Y >= Height || index.X < 0 || index.Y < 0)
			throw new IndexOutOfBoundsException();
		
		return IsEmptyIndex(index.Y, index.X);
	}
	
	/**
	 * Returns an enumerable set of all items in the entire grid.
	 * @return Returns an enumeration of all the items in the entire grid.
	 */
	@Override
	public Iterable<PieceType> Items() {
		ArrayList<PieceType> lst = new ArrayList<PieceType>();
		for(int i = 0; i < Height; i++)
			for(int j = 0; j < Width; j++)
				lst.add(Board[i][j]);
		
		return new Iterable<PieceType>()
		{
			public Iterator<PieceType> iterator()
			{
				return new Iterator<PieceType>()
				{
					public boolean hasNext()
					{
						return i.hasNext();
					}
					
					public PieceType next()
					{
						if(!hasNext())
							throw new NoSuchElementException();
						return i.next();
					}
					protected Iterator<PieceType> i = lst.iterator();
				};
			}
		};
	}

	/**
	 * Returns an enumerable set of indices for the entire grid. 
	 * @return Returns an enumerable set containing all indicies in the grid.
	 */
	@Override
	public Iterable<Vector2i> IndexSet() {
		ArrayList<Vector2i> lst = new ArrayList<Vector2i>();
		for(int i = 0; i < Height; i++)
			for(int j = 0; j < Width; j++)
				lst.add(new Vector2i(j, i));
		
		return new Iterable<Vector2i>()
		{
			public Iterator<Vector2i> iterator()
			{
				return new Iterator<Vector2i>()
				{
					public boolean hasNext()
					{
						return i.hasNext();
					}
					
					public Vector2i next()
					{
						if(!hasNext())
							throw new NoSuchElementException();
						return i.next();
					}
					protected Iterator<Vector2i> i = lst.iterator();
				};
			}
		};
	}

	/**
	 * Returns an enumerable set of indices for the entire grid.
	 * @param nonempty If true, returns only nonempty indices.
	 * @return Returns an enumerable set containing all indicies in the grid.
	 */
	@Override
	public Iterable<Vector2i> IndexSet(boolean nonempty) {
		ArrayList<Vector2i> lst = new ArrayList<Vector2i>();
		for(int i = 0; i < Height; i++)
		{
			for(int j = 0; j < Width; j++)
			{
				if(nonempty)
				{
					if(!IsEmptyIndex(i, j))
						lst.add(new Vector2i(j, i));
				}
				else
					lst.add(new Vector2i(j, i));
			}
		}
			
		
		return new Iterable<Vector2i>()
		{
			public Iterator<Vector2i> iterator()
			{
				return new Iterator<Vector2i>()
				{
					public boolean hasNext()
					{
						return i.hasNext();
					}
					
					public Vector2i next()
					{
						if(!hasNext())
							throw new NoSuchElementException();
						return i.next();
					}
					protected Iterator<Vector2i> i = lst.iterator();
				};
			}
		};
	}
	
	/**
	 * Returns an enumerable set of empty indices for the entire grid.
	 * @return Returns an enumerable set containing all emptry indicies in the grid.
	 */
	@Override
	public Iterable<Vector2i> EmptyIndexSet() {
		ArrayList<Vector2i> lst = new ArrayList<Vector2i>();
		for(int i = 0; i < Height; i++)
		{
			for(int j = 0; j < Width; j++)
			{
				if(IsEmptyIndex(i, j))
						lst.add(new Vector2i(j, i));
			}
		}
		
		return new Iterable<Vector2i>()
		{
			public Iterator<Vector2i> iterator()
			{
				return new Iterator<Vector2i>()
				{
					public boolean hasNext()
					{
						return i.hasNext();
					}
					
					public Vector2i next()
					{
						if(!hasNext())
							throw new NoSuchElementException();
						return i.next();
					}
					protected Iterator<Vector2i> i = lst.iterator();
				};
			}
		};
	}

	/**
	 * Returns an enumerable set of neighbors of {@code index}.
	 * @param index The index whose neighbors we want to obtain.
	 * @return Returns an enumerable set of neighbors of {@code index}.
	 * @throws IndexOutOfBoundsException Thrown if {@code index} is out of bounds.
	 * @throws NullPointerException Thrown if {@code index} is null.
	 */
	@Override
	public Iterable<PieceType> Neighbors(Vector2i index) {
		if(index == null)
			throw new NullPointerException();
		if(index.X >= Width || index.Y >= Height || index.X < 0 || index.Y < 0)
			throw new IndexOutOfBoundsException();
		
		ArrayList<PieceType> lst = new ArrayList<PieceType>();
		for(int i = index.Y - 1; i <= index.Y + 1; i++)
			for(int j = index.X -1; j <= index.X +1; j++)
				if(!(i < 0 || j < 0 || i >= Height || j >= Width) && (i != index.Y && j != index.X))
					lst.add(Board[i][j]);
		
		return new Iterable<PieceType>()
		{
			public Iterator<PieceType> iterator()
			{
				return new Iterator<PieceType>()
				{
					public boolean hasNext()
					{
						return i.hasNext();
					}
					
					public PieceType next()
					{
						if(!hasNext())
							throw new NoSuchElementException();
						return i.next();
					}
					protected Iterator<PieceType> i = lst.iterator();
				};
			}
		};
	}

	/**
	 * Returns an enumerable set of neightbors of {@code index}.
	 * @param index The index to obtain the neighbors of.
	 * @return Returns an enumerable set containing all indicies adjacent to {@code index}.
	 * @throws IndexOutOfBoundsException Thrown if {@code index} is out of bounds.
	 * @throws NullPointerException Thrown if {@code index} is null.
	 */
	@Override
	public Iterable<Vector2i> NeighborIndexSet(Vector2i index) {
		if(index == null)
			throw new NullPointerException();
		if(index.X >= Width || index.Y >= Height|| index.X < 0 || index.Y < 0)
			throw new IndexOutOfBoundsException();
		
		ArrayList<Vector2i> lst = new ArrayList<Vector2i>();
		for(int i = index.Y - 1; i <= index.Y + 1; i++)
			for(int j = index.X -1; j <= index.X +1; j++)
				if(!(i < 0 || j < 0 || i >= Height || j >= Width) && (i != index.Y && j != index.X))
					lst.add(new Vector2i(j, i));
		
		return new Iterable<Vector2i>()
		{
			public Iterator<Vector2i> iterator()
			{
				return new Iterator<Vector2i>()
				{
					public boolean hasNext()
					{
						return i.hasNext();
					}
					
					public Vector2i next()
					{
						if(!hasNext())
							throw new NoSuchElementException();
						return i.next();
					}
					protected Iterator<Vector2i> i = lst.iterator();
				};
			}
		};
	}

	/**
	 * Returns an enumerable set of neightbors of {@code index}.
	 * @param index The index to obtain the neighbors of.
	 * @param nonempty If true, returns only nonempty indices.
	 * @return Returns an enumerable set containing all indicies adjacent to {@code index}.
	 * @throws IndexOutOfBoundsException Thrown if {@code index} is out of bounds.
	 * @throws NullPointerException Thrown if {@code index} is null.
	 */
	@Override
	public Iterable<Vector2i> NeighborIndexSet(Vector2i index, boolean nonempty) {
		if(index == null)
			throw new NullPointerException();
		if(index.X >= Width || index.Y >= Height || index.X < 0 || index.Y < 0)
			throw new IndexOutOfBoundsException();
		
		ArrayList<Vector2i> lst = new ArrayList<Vector2i>();
		for(int i = 0; i < Height; i++)
		{
			for(int j = 0; j < Width; j++)
			{
				if(nonempty)
				{
					if(!IsEmptyIndex(i, j))
					{
						if(!(i < 0 || j < 0 || i >= Height || j >= Width) && (i != index.Y && j != index.X))
							lst.add(new Vector2i(j, i));
					}
				}
				else
				{
					if(!(i < 0 || j < 0 || i >= Height || j >= Width) && (i != index.Y && j != index.X))
						lst.add(new Vector2i(j, i));
				}
			}
		}
		
		return new Iterable<Vector2i>()
		{
			public Iterator<Vector2i> iterator()
			{
				return new Iterator<Vector2i>()
				{
					public boolean hasNext()
					{
						return i.hasNext();
					}
					
					public Vector2i next()
					{
						if(!hasNext())
							throw new NoSuchElementException();
						return i.next();
					}
					protected Iterator<Vector2i> i = lst.iterator();
				};
			}
		};
	}

	/**
	 * Determines if the given index lise on this grid.
	 * @param index The index to check.
	 * @return Returns true if the index is in bounds and false otherwise.
	 * @throws NullPointerException Thrown if {@code index} is null.
	 */
	@Override
	public boolean ContainsIndex(Vector2i index) {
		if(index == null)
			throw new NullPointerException();
		return index.Y >= 0 && index.X >= 0 && index.Y < Height && index.X < Width;
	}

	/**
	 * Clears the grid (if able).
	 * @return Returns true if the grid was cleared and false otherwise.
	 */
	@Override
	public boolean Clear() {
		for(int i = 0; i < Height; i++)
			for(int j = 0; j < Width; j++)
				Board [i][j] = PieceType.NONE;
		return Count() == 0;
	}

	/**
	 * Determines the number of items stored in this grid.
	 * @return Returns the number of items stored in this grid.
	 */
	@Override
	public int Count() {
		int count = 0;
		for(int i = 0; i < Height; i++)
			for(int j = 0; j < Width; j++)
				if(!IsEmptyIndex(i, j))
					count ++;
		return count;
	}

	/**
	 * Determines the number of cells in this grid.
	 * @return Returns the number of cells in this grid.
	 * If there are infinitely many cells, a negative value is returned.
	 */
	@Override
	public int Size() {
		return Height * Width;
	}

	/**
	 * Causes {@code eye} to begin observing this.
	 * Observers are allowed to subscribe multiple times if desired.
	 * Observers are garunteed to be notified in the order of subscription.
	 * @param eye The observer.
	 * @throws NullPointerException Thrown if {@code eye} is null.
	 */
	@Override
	public void Subscribe(IObserver<TicTacToeEvent> eye) {
		return;//unused
	}

	/**
	 * Causes {@code eye} to stop observing this.
	 * Only removes at most the first/oldest instance of {@code eye} if subscribed multiple times.
	 * @param eye THe observer.
	 * @throws NullPointerException Thrown if {@code eye} is null.
	 */
	@Override
	public void Unsubscribe(IObserver<TicTacToeEvent> eye) {
		return;//unused
	}

	/**
	 * Clones this board.
	 * The new board does not contain any of this boards subscribers to its events.
	 * @return Returns a deep copy of this board.
	 */
	@Override
	public ITicTacToeBoard Clone() {
		TicTacToeBoard newBoard = new TicTacToeBoard(Width, Height, WinningLength);
		for(int i = 0; i < Height; i++)
			for(int j = 0; j < Width; j++)
				newBoard.Board[i][j] = this.Board[i][j];
		
		return newBoard;
	}

	/**
	 * Determines if this game is finished.
	 * A game is finished if no more moves can be made or if a player has at least {@code WinningLength()} number of pieces in a row horizontally, vertically, or diagonally.
	 * @return Returns true if the game is over and false otherwise.
	 */
	@Override
	public boolean IsFinished() {
		if(this.Count() == this.Size() || this.WinningSet() != null)
			return true;
		
		return false;
	}

	/**
	 * Obtains a winning set of positions if one exists.
	 * @return If no one has won, null is returned.
	 * Otherwise, a set of positions representing a winning set for the winning player is returned.
	 * For example, in a 3x3 game with a winning length of 3, the winning player may win with a diagonal, so an example return value would be the set {(0,0),(1,1),(2,2)}. 
	 */
	@Override
	public Iterable<Vector2i> WinningSet() {
		ArrayList<Vector2i> lst = WinningSetMapping(null);//get the winningset from the helper
		if(lst == null)
			return null;
		return new Iterable<Vector2i>()
		{
			public Iterator<Vector2i> iterator()
			{
				return new Iterator<Vector2i>()
				{
					public boolean hasNext()
					{
						return i.hasNext();
					}
					
					public Vector2i next()
					{
						if(!hasNext())
							throw new NoSuchElementException();
						return i.next();
					}
					protected Iterator<Vector2i> i = lst.iterator();
				};
			}
		};
	}
	
	/**
	 * The helper function to get the winningset of the board
	 * @param use_me The index need to be contained in the set, ignored when is null
	 * @return The winningset, null if no winningset
	 */
	private ArrayList<Vector2i> WinningSetMapping(Vector2i use_me)
	{
		ArrayList<Vector2i> lst = new ArrayList<Vector2i>();//the arraylist to store the position of cells in the winningset
		int count = 0;
		PieceType type = PieceType.NONE;
		
		//go through the board horizontally to get winningset (if there is any)
		for(int i = 0; i < Height; i++)
		{
			for(int j = 0; j <Width; j++)
			{
				if(Board[i][j] == type && type != PieceType.NONE)//if find successive elements with same piecetype and is not NONE
				{
					count++;
				}
				else//if the former and this piece are in different types
				{
					type = Board[i][j];
					lst.clear();
					count = 1;
				}
				lst.add(new Vector2i(j, i));
				if(use_me == null && count >= this.WinningLength)//if use_me is ignored
					return lst;
				if(use_me != null && count >= this.WinningLength && lst.contains(use_me))//if use_me is not ignored, check whether use_me is in the list
					return lst;
			}
			//reset after checking each row
			lst.clear();
			count = 0;
			type = PieceType.NONE;
		}
		
		//go through the board vertically to get winningset (if there is any)
		for(int j = 0; j < Width; j++)
		{
			for(int i = 0; i < Height; i++)
			{
				//The logic is same as the above comments
				if(Board[i][j] == type && type != PieceType.NONE)
				{
					count++;
				}
				else
				{
					type = Board[i][j];
					lst.clear();
					count = 1;
				}
				lst.add(new Vector2i(j, i));
				if(use_me == null && count >= this.WinningLength)
					return lst;
				if(use_me != null && count >= this.WinningLength && lst.contains(use_me))
					return lst;
			}
			lst.clear();
			count = 0;
			type = PieceType.NONE;
		}
		
		//go through the board diagonally (from top left to bottom right) to get winningset (if there is any)
		for(int i = 0; i < Height; i++)
		{
			for(int j = 0; j < Width; j++)
			{
				if(i != 0 && j != 0)//The beginning of each diagonal line start from the first row or the first column
					continue;
				for(int y = i, x = j; this.ContainsIndex(new Vector2i(x,y)); y++, x++)
				{
					//The logic is same as the above comments
					if(Board[y][x] == type && type != PieceType.NONE)
					{
						count++;
					}
					else
					{
						type = Board[y][x];
						lst.clear();
						count = 1;
					}
					lst.add(new Vector2i(x, y));
					if(use_me == null && count >= this.WinningLength)
						return lst;
					if(use_me != null && count >= this.WinningLength && lst.contains(use_me))
						return lst;
				}
				lst.clear();
				count = 0;
				type = PieceType.NONE;
			}
		}
		
		//go through the board diagonally (from top right to bottom left) to get winningset (if there is any)
		for(int i = 0; i < Height; i++)
		{
			for(int j = 0; j < Width; j++)
			{
				if(i != 0 && j != this.Width - 1)//The beginning of each diagonal line start from the first row or the last column
					continue;
				for(int y = i, x = j; this.ContainsIndex(new Vector2i(x,y)); y++, x--)
				{
					//The logic is same as the above comments
					if(Board[y][x] == type && type != PieceType.NONE)
					{
						count++;
					}
					else
					{
						type = Board[y][x];
						lst.clear();
						count = 1;
					}
					lst.add(new Vector2i(x, y));
					if(use_me == null && count >= this.WinningLength)
						return lst;
					if(use_me != null && count >= this.WinningLength && lst.contains(use_me))
						return lst;
				}
				lst.clear();
				count = 0;
				type = PieceType.NONE;
			}
		}
		return null;
	}
	


	/**
	 * Obtains a winning set of positions using {@code use_me} if one exists.
	 * @param use_me This must be part of the winning set.
	 * @return If no one has won using {@code use_me}, null is returned.
	 * Otherwise, a set of positions representing a winning set for the winning player is returned.
	 * For example, in a 3x3 game with a winning length of 3, the winning player may win with a diagonal, so an example return value would be the set {(0,0),(1,1),(2,2)}. 
	 */
	@Override
	public Iterable<Vector2i> WinningSet(Vector2i use_me) {
		ArrayList<Vector2i> lst = WinningSetMapping(use_me);//get the winning set from the helper
		if(lst == null)
			return null;
		return new Iterable<Vector2i>()
		{
			public Iterator<Vector2i> iterator()
			{
				return new Iterator<Vector2i>()
				{
					public boolean hasNext()
					{
						return i.hasNext();
					}
					
					public Vector2i next()
					{
						if(!hasNext())
							throw new NoSuchElementException();
						return i.next();
					}
					protected Iterator<Vector2i> i = lst.iterator();
				};
			}
		};
	}

	/**
	 * Determines the winner of this game.
	 * @return If the game is not finished, {@code NULL} is returned. If the game is a tie, {@code NEITHER} is returned. Otherwise, the winning player is return.
	 */
	@Override
	public Player Victor() {
		if(!IsFinished())//if game not finished, there is not victor
			return Player.NULL;
		
		Iterable<Vector2i> i = WinningSet();//get the winning set and see what kind of piece is in it
		if(i == null)//no one win as winningset is null
			return Player.NEITHER;
		Iterator<Vector2i> itr = WinningSet().iterator();
		if(this.Get(itr.next()) == PieceType.CIRCLE)
			return Player.CIRCLE;
		else if(this.Get(itr.next()) == PieceType.CROSS)
			return Player.CROSS;
		else 
			return Player.NEITHER;
	}

	/**
	 * Obtains the width of the board.
	 */
	@Override
	public int Width() {
		return Width;
	}

	/**
	 * Obtains the height of the board.
	 */
	@Override
	public int Height() {
		return Height;
	}

	/**
	 * Obtains the number of pieces a player needs in a row to win.
	 */
	@Override
	public int WinningLength() {
		return WinningLength;
	}
	
	/**
	 * The helper function to examing whether the cell at this index is empty(NONE or null)
	 * @param i The row index
	 * @param j The column index
	 * @return True if empty, false if not empty
	 */
	private boolean IsEmptyIndex(int i, int j)
	{
		if(i >= Height || j >= Width || i < 0 || j < 0)
			throw new IndexOutOfBoundsException();
		return Board[i][j] == PieceType.NONE || Board[i][j] == null;
	}
	
	//The width of the board
	protected int Width;
	
	//The Height of the board
	protected int Height;
	
	//winninglength of the board (number of successive same pieces to win the game)
	protected int WinningLength;
	
	//The 2D array storing all the pieces on the board
	protected PieceType [][] Board;

}
