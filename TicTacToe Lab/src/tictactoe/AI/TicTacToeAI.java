package tictactoe.AI;

import gamecore.datastructures.vectors.Vector2i;
import tictactoe.model.ITicTacToeBoard;
import tictactoe.model.PieceType;
import tictactoe.model.Player;

/**
 * The class representing an AI player
 * @author Zitan Wang
 *
 */
public class TicTacToeAI implements ITicTacToeAI{

	/**
	 * The constructor
	 * @param player The player AI is representing
	 * @param difficulty The level of difficulty, ranging from 1 -10
	 */
	public TicTacToeAI(Player player, int difficulty)
	{
		this.Role = player;
		this.Difficulty = difficulty;
		if(Role == Player.CIRCLE)
			this.Type = PieceType.CIRCLE;
		else if(Role == Player.CROSS)
			this.Type = PieceType.CROSS;
		else
			this.Type = PieceType.NONE;
	}
	
	/**
	 * Gets the AI's next move from the given board state.
	 * @param board The current state of the game.
	 * @return Returns the position the AI will claim next or null if it cannot make a move.
	 * @throws NullPointerException Thrown if {@code board} is null.
	 */
	@Override
	public Vector2i GetNextMove(ITicTacToeBoard board) 
	{
		if(board == null)
			throw new NullPointerException();
		
		int bestScore = Integer.MIN_VALUE;
		Vector2i bestMove = null;
		
		int emptyCells = board.Size()-board.Count();
		//make sure the AI could response in 30 seconds when there are many empty cells and difficulty level is high
		if(emptyCells >= 17 && Difficulty > 7)
			MaxDepth = (int) (Difficulty * 0.5);
		else if(emptyCells >= 10 && Difficulty > 7)
			MaxDepth = (int) (Difficulty * 0.6);
		else
			MaxDepth = Difficulty;
	    
	  //For each possible move, get a score, the next move is the move with highest score
	    for (Vector2i move : board.EmptyIndexSet())
	    {
	    	//get a cloned new board
	        ITicTacToeBoard newBoard = board.Clone();
	        //place the piece on the board
	        newBoard.Set(this.Type, move);
	        //get the score of this piece placement by using minimax
	        int score = minimax(newBoard, this.Type == PieceType.CIRCLE ? PieceType.CROSS : PieceType.CIRCLE, 1);
	        if (score >= bestScore) 
	        {
	        	bestScore = score;
	            bestMove = move;
	        }
	    }
	    return bestMove;
	}

	/**
	 * This is a helper function of GetNextMove, it takes in a TicTacToe board and 
	 * give back a score of this move(made in GetNextMove)
	 * @param board The TicTacToe board to be evaluated
	 * @param type The type of piece going to be placed at this step (CROSS or CIRCLE)
	 * @param depth The current depth of the tree
	 * @return The score evaluated for this step
	 */
	private int minimax(ITicTacToeBoard board, PieceType type, int depth) 
	{
	    if (board.IsFinished() || depth >= MaxDepth) //If the game is finished, or MaxDepth is reached
	        return Evaluate(board); //Evaluate the current board to get a score
	    
	    if (type == this.Type)//If AI playing this step, maximize the benefit of AI
	    {
	        int bestScore = Integer.MIN_VALUE;
	        for (Vector2i move : board.EmptyIndexSet()) //Iterate through all the options for the next step
	        {
	        	ITicTacToeBoard newBoard = board.Clone();
		        newBoard.Set(type, move);
	            int score = minimax(newBoard, type == PieceType.CIRCLE ? PieceType.CROSS : PieceType.CIRCLE, depth + 1);
	            bestScore = Math.max(bestScore, score);//Maximize the benefit of AI
	        }
	        return bestScore;
	    } 
	    
	    else //If the other player playing this step, minimize the benefit of AI
	    {
	        int bestScore = Integer.MAX_VALUE;
	        for (Vector2i move : board.EmptyIndexSet()) //Iterate through all the options for the next step
	        {
	            ITicTacToeBoard newBoard = board.Clone();
		        newBoard.Set(type, move);
	            int score = minimax(newBoard, type == PieceType.CIRCLE ? PieceType.CROSS : PieceType.CIRCLE, depth + 1);
	            bestScore = Math.min(bestScore, score);//minimize the benefit of AI
	        }
	        return bestScore;
	    }
	}
	
	/**
	 * The helper function to calculate the score for a board
	 * @param board The TicTacToe Board to be evaluated
	 * @return The score of this Board 
	 */
	public int Evaluate(ITicTacToeBoard board)
	{
		if(!board.IsFinished())//If the game is not completed yet
			return EvaluateIncompleteBoard(board);
		
		Player victor = board.Victor();
		if(victor == Role)//If the AI won
			return Integer.MAX_VALUE;
		else if(victor == (Role == Player.CIRCLE ? Player.CROSS : Player.CIRCLE))//If the other player won
			return Integer.MIN_VALUE;
		else if (victor == Player.NEITHER)//If tie
			return 0;
		else//just in case
		{
			return EvaluateIncompleteBoard(board);
		}
	}
	
	
	/**
	 * The helper function to evaluate a not completed board, because the maximum depth is reached
	 * @param board The TicTacToe Board to be evaluated
	 * @return The score of this Board 
	 */
	public int EvaluateIncompleteBoard(ITicTacToeBoard board)
	{
		int score = 0;
		double count = 0;//the number of successive pieces having the same type
		PieceType type = PieceType.NONE;
		
		//Check the board horizontally for continuous same pieces
		for(int i = 0; i < board.Height(); i++)
		{
			for(int j = 0; j < board.Width(); j++)
			{
				PieceType posType = board.Get(new Vector2i(j, i));
				if(type == posType)//If there are continues same pieces
					count ++;
				else//The type of pieces changed
				{
					if(type == this.Type && board.Width() - count > board.WinningLength())//if pieces tracked in count is from the AI's side
					{
						score += (count/board.WinningLength()) * 10;
					}
					else if(type == (this.Type == PieceType.CIRCLE ? PieceType.CROSS : PieceType.CIRCLE))
						//if pieces are from the other player's side
					{
						score -= (count/board.WinningLength()) * 12;
					}
					count = 0;
					type = posType;
				}
			}
			count = 0;
			type = PieceType.NONE;
		}
		
		//Check the board vertically for continuous same pieces
		for(int j = 0; j < board.Width(); j++)
		{
			for(int i = 0; i < board.Height(); i++)
			{
				PieceType posType = board.Get(new Vector2i(j, i));
				if(type == posType)//If there are continues same pieces
					count ++;
				else//The type of pieces changed
				{
					if(type == this.Type && board.Height() - count > board.WinningLength())//if pieces tracked in count is from the AI's side
					{
						score += ((count - 1)/board.WinningLength()) * 10; //increase the score
					}
					else if(type == (this.Type == PieceType.CIRCLE ? PieceType.CROSS : PieceType.CIRCLE))
						//if pieces are from the other player's side
					{
						score -= ((count - 1)/board.WinningLength()) * 12; //decrease the score
					}
					count = 0;
					type = posType;
				}
			}
			count = 0;
			type = PieceType.NONE;
		}
			
		//Check the board diagonally (direction: from top left to bottom right) for continuous same pieces
		for(int i = 0; i < board.Height(); i++)
		{
			for(int j = 0; j < board.Width(); j++)
			{
				if(i != 0 && j != 0)
					continue;
				for(int y = i, x = j; board.ContainsIndex(new Vector2i(x, y)); y++, x++)
				{
					PieceType posType = board.Get(new Vector2i(x, y));
					if(type == posType)//If there are continues same pieces
						count ++;
					else//The type of pieces changed
					{
						if(type == this.Type)//if pieces tracked in count is from the AI's side
						{
							score += ((count - 1)/board.WinningLength()) * 10;
						}
						else if(type == (this.Type == PieceType.CIRCLE ? PieceType.CROSS : PieceType.CIRCLE))
							//if pieces are from the other player's side
						{
							score -= ((count - 1)/board.WinningLength()) * 12;
						}
						count = 0;
						type = posType;
					}
				}
				count = 0;
				type = PieceType.NONE;
			}
		}
		
		//Check the board diagonally (direction: from top Right to bottom left) for continuous same pieces
		for(int i = 0; i < board.Height(); i++)
		{
			for(int j = 0; j < board.Width(); j++)
			{
				if(i != 0 && j != board.Width() - 1)
					continue;
				for(int y = i, x = j; board.ContainsIndex(new Vector2i(x,y)); y++, x--)
				{
					PieceType posType = board.Get(new Vector2i(x, y));
					if(type == posType)//If there are continues same pieces
						count ++;
					else//The type of pieces changed
					{
						if(type == this.Type)//if pieces tracked in count is from the AI's side
						{
							score += ((count - 1)/board.WinningLength()) * 10;
						}
						else if(type == (this.Type == PieceType.CIRCLE ? PieceType.CROSS : PieceType.CIRCLE))
							//if pieces are from the other player's side
						{
							score -= ((count - 1)/board.WinningLength()) * 12;
						}
						count = 0;
						type = posType;
					}
				}
				count = 0;
				type = PieceType.NONE;
			}
		}

		return score;
	}

	/**
	 * Determines which player the AI controls.
	 */
	@Override
	public Player GetPlayer() {
		return Role;
	}
	
	protected Player Role;
	protected PieceType Type;
	protected int Difficulty;
	protected int MaxDepth;

}
