package tictactoe.view;

import java.io.File;
import java.util.ArrayList;

import gamecore.GameEngine;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.datastructures.vectors.Vector2i;
import tictactoe.model.PieceType;
import gamecore.gui.gamecomponents.ImageComponent;
import gamecore.gui.gamecomponents.MultiImageComponent;

/**
 * Contains all of the actual drawing logic for a Tic Tac Toe board.
 * It can (and should) delegate subdrawing routines to other game components, but it is in charge of managing them.
 * @author Dawn Nye
 */
public class TicTacToeView implements ITicTacToeView{

	/**
	 * The default constructor
	 */
	public TicTacToeView()
	{
		this(3,3);
	}
	
	
	/**
	 * The constructor to visualize the TicTacToe game
	 * @param Height
	 * @param Width
	 */
	public TicTacToeView(int Height, int Width)
	{
		//initialize Height and Width
		this.Height = Height;
		this.Width = Width;
		
		//import GridCell
		File GridCell = new File("assets/images/GridCell.png");
		
		//Set up the visualization of cursor, initial position at (0,0)
		this.Cursor = new ImageComponent(new File("assets/images/Selection.png"));
		this.Cursor.Translate(new Vector2d(4,4));
		this.CursorPos = new Vector2i(0,0);
		GameEngine.Game().AddComponent(this.Cursor);
		
		//initialize the 2D arrays that will contain images
		this.Background = new ImageComponent [this.Width][this.Height];
		this.Pieces = new MultiImageComponent [this.Width][this.Height];
		
		//Four types of pieces(images) in each cell of Pieces
		ArrayList<Object> pictures = new ArrayList<Object>();
		pictures.add(new File("assets/images/Circle.png"));
		pictures.add(new File("assets/images/Cross.png"));
		pictures.add(new File("assets/images/Golden Circle.png"));
		pictures.add(new File("assets/images/Golden Cross.png"));
		
		//Looping through Background and Pieces to initialize all cells
		for(int i = 0; i < this.Width; i++)
		{
			for(int j = 0; j < this.Height; j++)
			{
				//set up visualization of each grid cell
				this.Background[i][j] = new ImageComponent(GridCell);
				this.Background[i][j].Translate(new Vector2d(168 * j, 168 * i));
				GameEngine.Game().AddComponent(this.Background[i][j]);
				
				//set up visualization of each pieces
				this.Pieces[i][j] = new MultiImageComponent(pictures);
				Pieces[i][j].SetSelectedImage(IndexN);//firstly initialized to no image shown
				this.Pieces[i][j].Translate(new Vector2d(168 * j, 168 * i));
				GameEngine.Game().AddComponent(this.Pieces[i][j]);
			}
		}
		
		this.Disposed = false;
	}
	/**
	 * Places a piece on the board in cell {@code pos}.
	 * @param pos The cell of the board to place the piece.
	 * @param piece The piece to place. To clear the cell, use NONE.
	 * @throws NullPointerException Thrown if {@code pos} or {@code piece} is null.
	 */
	@Override
	public void PlacePiece(Vector2i pos, PieceType piece) {
		if(piece == null)
			throw new NullPointerException();
		
		switch(piece) {
		  case NONE:
			  Pieces[pos.Y][pos.X].SetSelectedImage(-1);
			  break;
		  case CIRCLE:
			  Pieces[pos.Y][pos.X].SetSelectedImage(IndexO);
			  break;
		  case CROSS:
			  Pieces[pos.Y][pos.X].SetSelectedImage(IndexX);
			   break;
		  }
	}

	/**
	 * Makes the piece in cell {@code pos} golden.
	 * @param pos The cell of the board containing the piece to make golden.
	 * @return Returns true if a piece was made golden and false otherwise. This should only fail if no piece is in the cell or if the piece is already golden.
	 * @throws NullPointerException Thrown if {@code pos} is null.
	 */
	@Override
	public boolean MakeGolden(Vector2i pos) {
		if(pos == null)
			throw new NullPointerException();
		
		int index = Pieces[pos.Y][pos.X].GetSelectedImage();//get the index of the image currently shown
		if (index == IndexO)//if circle is shown, change to golden circle
			Pieces[pos.Y][pos.X].SetSelectedImage(IndexGO);
		else if (index == IndexX)//if cross is shown, change to golden cross
			Pieces[pos.Y][pos.X].SetSelectedImage(IndexGX);
		else//if not normal circle or cross shown, do nothing
			return false;
		return true;
	}

	/**
	 * Clears the board by removing all pieces from its cells.
	 */
	@Override
	public void Clear() {
		for(int i = 0; i < this.Width; i++)
			for(int j = 0; j < this.Height; j++)
				Pieces[i][j].SetSelectedImage(IndexN);
	}

	/**
	 * Moves the cursor in the direciton {@code dir}.
	 * The cursor can only move one square at a time (diagonals count as one square).
	 * If the cursor would move diagonally off the board but can move a space horizontally or vertically, the latter movement will occur.
	 * @param dir The direction to move the cursor. Only the sign of each of its values will be used, not its magnitude.
	 * @return Returns true if the cursor moved (if not as far as originally intended) and false otherwise, such as if it attempted to move out of bounds.
	 * @throws NullPointerException Thrown if {@code dir} is null.
	 */
	@Override
	public boolean MoveCursor(Vector2i dir) {
		if(dir == null)
			throw new NullPointerException();
		
		//Making sure that dir has length one or zero in both x and y direction, so only the sign matters
		int x = dir.X == 0 ? 0 : dir.X/Math.abs(dir.X);
		int y = dir.Y == 0 ? 0 : dir.Y/Math.abs(dir.Y);
		dir = new Vector2i(x, y);
		
		//if the intended movement wouldn't go out of the board on either direction
		if(!XMoveOutOfBound(dir) && !YMoveOutOfBound(dir))
		{
			//move the cursor
			Cursor.Translate(new Vector2d(dir.X * 168, dir.Y * 168));
			CursorPos = CursorPos.Add(dir);
			return true;
		}
		//if the intended movement will go out of the board on both directions
		else if(XMoveOutOfBound(dir) && YMoveOutOfBound(dir))
		{
			//can not make the movement
			return false;
		}
		//if the intended movement will go out of the board only on the x direction
		else if(XMoveOutOfBound(dir))
		{
			//move the cursor along the designated y direction
			Cursor.Translate(new Vector2d(0, dir.Y * 168));
			CursorPos = new Vector2i(CursorPos.X, CursorPos.Y + dir.Y);
			return true;
		}
		//if the intended movement will go out of the board only on the y direction
		else if(YMoveOutOfBound(dir))
		{
			//move the cursor along the designated x direction
			Cursor.Translate(new Vector2d(dir.X * 168, 0));
			CursorPos = new Vector2i(CursorPos.X + dir.X, CursorPos.Y);
			return true;
		}
		return false;
	}
	
	/**
	 * The helper function to indicate whether the intended movement will go out of bound on the x direction
	 * @param dir The intended movement of the cursor
	 * @return True if will go out of bound, false if not
	 */
	private boolean XMoveOutOfBound(Vector2i dir)
	{return dir.X + CursorPos.X >= Width || dir.X + CursorPos.X < 0;}
	
	/**
	 * The helper function to indicate whether the intended movement will go out of bound on the y direction
	 * @param dir The intended movement of the cursor
	 * @return True if will go out of bound, false if not
	 */
	private boolean YMoveOutOfBound(Vector2i dir)
	{return dir.Y + CursorPos.Y >= Height || dir.Y + CursorPos.Y < 0;}

	/**
	 * Removes any components this view added to the game engine from the engine.
	 * This operation cannot be undone.
	 */
	@Override
	public void Dispose() {
		for(int i = 0; i < this.Width; i++)
		{
			for(int j = 0; j < this.Height; j++)
			{
				GameEngine.Game().RemoveComponent(this.Background[i][j]);
				GameEngine.Game().RemoveComponent(this.Pieces[i][j]);
			}
		}
		GameEngine.Game().RemoveComponent(this.Cursor);
		Disposed = true;
	}

	/**
	 * If true, then this view has been disposed. Returns false otherwise.
	 */
	@Override
	public boolean Disposed() {
		return Disposed;
	}

	/**
	 * Obtains the current cursor position.
	 */
	@Override
	public Vector2i CursorPosition() {
		return CursorPos;
	}

	/**
	 * Obtains the width of the game board (in cells).
	 */
	@Override
	public int Width() {
		return Width;
	}

	/**
	 * Obtains the height of the game board (in cells).
	 */
	@Override
	public int Height() {
		return Height;
	}
	
	//height of the board
	protected int Height;
	
	//width of the board
	protected int Width;
	
	//the index in MultiImageComponent representing no image shown
	protected final int IndexN = -1;
	
	////the index in MultiImageComponent representing circle
	protected final int IndexO = 0;
	
	//the index in MultiImageComponent representing cross
	protected final int IndexX = 1;
	
	//the index in MultiImageComponent representing golden circle
	protected final int IndexGO = 2;
	
	//the index in MultiImageComponent representing golden cross
	protected final int IndexGX = 3;
	
	//Whether this view is disposed
	protected boolean Disposed;
	
	//The current cursor position (in terms of index of the board)
	protected Vector2i CursorPos;
	
	//The image of a cursor
	protected ImageComponent Cursor; 
	
	//The 2D array containing all the background images
	protected ImageComponent Background [][];
	
	//The 2D array containing all the images of pieces
	protected MultiImageComponent Pieces [][];


}
