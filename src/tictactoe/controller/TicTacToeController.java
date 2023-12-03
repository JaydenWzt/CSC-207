package tictactoe.controller;

import gamecore.GameEngine;
import gamecore.datastructures.vectors.Vector2i;
import gamecore.input.InputManager;
import tictactoe.AI.ITicTacToeAI;
import tictactoe.AI.TicTacToeAI;
import tictactoe.model.ITicTacToeBoard;
import tictactoe.model.PieceType;
import tictactoe.model.Player;
import tictactoe.model.TicTacToeBoard;
import tictactoe.model.TicTacToeEvent;
import tictactoe.view.ITicTacToeView;
import tictactoe.view.TicTacToeView;

/**
 * Controls a Tic Tac Toe game.
 * @author Dawn Nye
 * @author Zitan Wang
 */
public class TicTacToeController implements ITicTacToeController
{
	/**
	 * Creates a new Tic Tac Toe controller with two human players.
	 * @param width The width of the game board.
	 * @param height The height of the game board.
	 * @param win_len The required combo length to have a winning position.
	 */
	public TicTacToeController(int width, int height, int win_len)
	{
		this(width,height,win_len,true,0,true,0);
		return;
	}
	
	/**
	 * Creates a new Tic Tac Toe controller.
	 * @param width The width of the game board.
	 * @param height The height of the game board.
	 * @param win_len The required combo length to have a winning position.
	 * @param player1_human If true, then the first player is human. If false, the first player is an AI.
	 * @param ai1_difficulty If player 1 is an AI, this is its difficulty.
	 * @param player2_human If true, then the second player is human. If false, the second player is an AI.
	 * @param ai1_difficulty If player 2 is an AI, this is its difficulty.
	 */
	public TicTacToeController(int width, int height, int win_len, boolean player1_human, int ai1_difficulty, boolean player2_human, int ai2_difficulty)
	{
		Width = width;
		Height = height;
		WinningLength = win_len;
		NextMovePos = new Vector2i(0, 0);
		
		IsPlayerOneHuman = player1_human;
		
		if(!IsPlayerOneHuman)
			PlayerOneAI = new TicTacToeAI(Player.CROSS,ai1_difficulty);
		
		IsPlayerTwoHuman = player2_human;
		
		if(!IsPlayerTwoHuman)
			PlayerTwoAI = new TicTacToeAI(Player.CIRCLE,ai2_difficulty);
		
		return;
	}
	
	/**
	 * Called before the first update to initialize the game component.
	 */
	public void Initialize()
	{
		// Create the board
		Model = new TicTacToeBoard(Width,Height,WinningLength);
		Model.Subscribe(this);
		
		// Create the view
		View = new TicTacToeView(Width,Height);
		
		// Initialize the game state
		ActivePlayer = Player.CROSS;
		
		Initialized = true;
		return;
	}
	
	/**
	 * Determines if this game component is initialized.
	 * @return Returns true if this game component is initialized and false otherwise.
	 */
	public boolean Initialized()
	{return Initialized;}
	
	/**
	 * Advances a game component by some time {@code delta}.
	 * @param delta The amount of time in milliseconds that has passed since the last update.
	 */
	public void Update(long delta)
	{
		// Update our input manager
		Input = GameEngine.Game().GetService(InputManager.class);
		
		//allows the user to exit or to reset
		if(Input.GracelessInputSatisfied("Reset"))
			this.ResetGame();
		if(Input.GracelessInputSatisfied("Exit"))
			this.Dispose();
		
		
		//displaying winning animation if there is a winning set
		Iterable<Vector2i> set = this.Model.WinningSet();
		if(set != null)
		{
			for(Vector2i i: set)
				this.View.MakeGolden(i);
		}
		
		//when the game is not finished, cursor moving and AI predicting is allowed
		if(!this.Model.IsFinished())
		{
			// Allow the player to move the cursor as desired
			if(Input.GracelessInputSatisfied("Left"))
				View.MoveCursor(Vector2i.LEFT);
			if(Input.GracelessInputSatisfied("Right"))
				View.MoveCursor(Vector2i.RIGHT);
			if(Input.GracelessInputSatisfied("Up"))
				View.MoveCursor(Vector2i.UP);
			if(Input.GracelessInputSatisfied("Down"))
				View.MoveCursor(Vector2i.DOWN);
			NextMovePos = View.CursorPosition();
			
			//If PlayerOneAI is active
			if(this.ActivePlayer == Player.CROSS && !this.IsPlayerOneHuman)
			{
				NextMovePos = this.PlayerOneAI.GetNextMove(Model.Clone());
				PlacePiece();
			}
			
			//If palyertwoAI is active
			else if(this.ActivePlayer == Player.CIRCLE && !this.IsPlayerTwoHuman)
			{
				NextMovePos = this.PlayerTwoAI.GetNextMove(Model.Clone());
				PlacePiece();
			}
		}
		
		//Place the next piece by human selection
		if(Input.GracelessInputSatisfied("Select") && this.Model.IsCellEmpty(NextMovePos))
			PlacePiece();
		return;
	}
	
	/**
	 * The helper function to put a piece that the ActivePlayer places on the board at the position NextMovePos
	 * and then switch player
	 */
	private void PlacePiece()
	{
		this.View.PlacePiece(NextMovePos, ActivePlayer == Player.CROSS ? PieceType.CROSS : PieceType.CIRCLE);
		this.Model.Set(ActivePlayer == Player.CROSS ? PieceType.CROSS : PieceType.CIRCLE, NextMovePos);
		ActivePlayer = ActivePlayer == Player.CROSS ? Player.CIRCLE : Player.CROSS;
	}
	
	/**
	 * Called when the game component is removed from the game.
	 */
	public void Dispose()
	{
		if(Disposed())
			return;
		
		if(!View.Disposed())
			View.Dispose();
		
		Disposed = true;
		return;
	}
	
	/**
	 * Determines if this game component has been disposed.
	 * @return Returns true if this game component has been disposed and false otherwise.
	 */
	public boolean Disposed()
	{return Disposed;}
	
	/**
	 * Called when an observation is made.
	 * @param event The observation.
	 */
	public void OnNext(TicTacToeEvent event)
	{
		
		
		return;
	}
	
	/**
	 * Called when an error occurs.
	 * @param e The observed error.
	 */
	public void OnError(Exception e)
	{return;}
	
	/**
	 * Called when the observable has finished sending observations.
	 */
	public void OnCompleted()
	{return;}
	
	/**
	 * Resets the game.
	 */
	public void ResetGame()
	{
		this.Model.Clear();
		this.View.Clear();
		this.ActivePlayer = Player.CROSS;
		return;
	}
	
	/**
	 * Obtains the active player (if any).
	 * This value will be NEITHER if the game is over.
	 */
	public Player ActivePlayer()
	{return ActivePlayer;}
	
	/**
	 * Obtains the width of the board.
	 */
	public int Width()
	{return Width;}
	
	/**
	 * Obtains the height of the board.
	 */
	public int Height()
	{return Height;}
	
	/**
	 * Obtains the number of pieces a player needs in a row to win.
	 */
	public int WinningLength()
	{return WinningLength;}
	
	/**
	 * The Tic Tac Toe board.
	 */
	protected ITicTacToeBoard Model;
	
	/**
	 * The Tic Tac Toe view.
	 */
	protected ITicTacToeView View;
	
	/**
	 * The input manager for the game.
	 * This is registered as a service.
	 */
	protected InputManager Input;
	
	/**
	 * The width of the board.
	 */
	protected int Width;
	
	/**
	 * The height of the board.
	 */
	protected int Height;
	
	/**
	 * The winning length of the board.
	 */
	protected int WinningLength;
	
	/**
	 * If true, then player 1 (the cross player) is human controlled.
	 */
	protected boolean IsPlayerOneHuman;
	
	/**
	 * If player 1 is controlled by an AI, this is it.
	 */
	protected ITicTacToeAI PlayerOneAI;
	
	/**
	 * If true, then player 2 (the circle player) is human controlled.
	 */
	protected boolean IsPlayerTwoHuman;
	
	/**
	 * If player 2 is controlled by an AI, this is it.
	 */
	protected ITicTacToeAI PlayerTwoAI;
	
	/**
	 * The active player (if any).
	 */
	protected Player ActivePlayer;
	
	/**
	 * The amount of time delay between goldenizations upon victory.
	 */
	protected final long GoldenLag = 100;
	
	/**
	 * If true, then this component has been initialized.
	 */
	protected boolean Initialized;
	
	/**
	 * If true, then this component has been disposed.
	 */
	protected boolean Disposed;
	
	/**
	 * Indicating the next move to be made (regardless AI or human)
	 */
	protected Vector2i NextMovePos;
	
}
