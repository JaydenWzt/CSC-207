package tictactoe;

import java.awt.event.KeyEvent;

import gamecore.GameEngine;
import gamecore.input.InputManager;
import gamecore.input.InputMap;
import tictactoe.controller.ITicTacToeController;
import tictactoe.controller.TicTacToeController;

/**
 * Plays tic tac toe.
 * @author Dawn Nye
 */
public class TicTacToe extends GameEngine
{
	/**
	 * Creates a new tic tac toe game between two humans
	 * @param width The width of the board. This value must be positive.
	 * @param height The height of the board. This value must be positive.
	 * @param win_len The required winning length of the board. This must be positive and at most {@code width} and at most {@code height}.
	 * @throws IllegalArgumentException Thrown if {@code width}, {@code height}, or {@code win_len} is nonpositive or if {@code win_len} is greater than either {@code width} and {@code height}.
	 */
	public TicTacToe(int width, int height, int win_len)
	{
		this(width, height, win_len, true, true, 5);
	}
	
	/**
	 * Creates a new tic tac toe game between one human and one AI, AI difficulty level set to 5
     * @param width The width of the board. This value must be positive.
	 * @param height The height of the board. This value must be positive.
	 * @param win_len The required winning length of the board. This must be positive and at most {@code width} and at most {@code height}.
	 * @param player1Human If true, player1 is human, otherwise, player2 is human
	 */
	public TicTacToe(int width, int height, int win_len, boolean player1Human)
	{
		this(width, height, win_len, player1Human, !player1Human, 5);
	}
	
	/**
	 * Creates a new tic tac toe game between one human and one AI
     * @param width The width of the board. This value must be positive.
	 * @param height The height of the board. This value must be positive.
	 * @param win_len The required winning length of the board. This must be positive and at most {@code width} and at most {@code height}.
	 * @param player1Human If true, player1 is human, otherwise, player2 is human
	 * @param difficulty The difficulty of AI

	 */
	public TicTacToe(int width, int height, int win_len, boolean player1Human, int difficulty)
	{
		this(width, height, win_len, player1Human, !player1Human, difficulty);
	}
	
	
	/**
	 * Creates a new tic tac toe game.
	 * @param width The width of the board. This value must be positive.
	 * @param height The height of the board. This value must be positive.
	 * @param win_len The required winning length of the board. This must be positive and at most {@code width} and at most {@code height}.
	 * @throws IllegalArgumentException Thrown if {@code width}, {@code height}, or {@code win_len} is nonpositive or if {@code win_len} is greater than either {@code width} and {@code height}
	 *         or {@code difficulty} is nonpositive or greater than 10.
	 */
	private TicTacToe(int width, int height, int win_len, boolean player1Human, boolean player2Human, int difficulty)
	{
		super("CSC 207 Tic Tac Toe",null,168 * width + 16,168 * height + 39);

		if(width <= 0 || height <= 0 || win_len <= 0 || win_len > width || win_len > height || difficulty <= 0 || difficulty > 10)
			throw new IllegalArgumentException();
		
		Width = width;
		Height = height;
		WinningLength = win_len;
		Player1Human = player1Human;
		Player2Human = player2Human;
		Difficulty = difficulty;
		
		return;
	}
	
	@Override protected void Initialize()
	{
		// Initialize input data
		Input = new InputManager();
		InputMap Bindings = InputMap.Map();
		
		// Add the input manager to the game and make it as a service
		AddComponent(Input);
		AddService(Input);
		
		// Initialize some key bindings
		Bindings.AddKeyBinding("Exit",KeyEvent.VK_ESCAPE);
		Bindings.AddKeyBinding("Reset",KeyEvent.VK_TAB);
		
		Bindings.AddKeyBinding("m_Select",KeyEvent.VK_SPACE);
		Bindings.AddKeyBinding("a_Select",KeyEvent.VK_ENTER);
		Bindings.AddORBinding("Select","m_Select","a_Select");
		
		Bindings.AddKeyBinding("m_Left",KeyEvent.VK_LEFT);
		Bindings.AddKeyBinding("a_Left",KeyEvent.VK_A);
		Bindings.AddORBinding("Left","m_Left","a_Left");
		
		Bindings.AddKeyBinding("m_Right",KeyEvent.VK_RIGHT);
		Bindings.AddKeyBinding("a_Right",KeyEvent.VK_D);
		Bindings.AddORBinding("Right","m_Right","a_Right");
		
		Bindings.AddKeyBinding("m_Up",KeyEvent.VK_UP);
		Bindings.AddKeyBinding("a_Up",KeyEvent.VK_W);
		Bindings.AddORBinding("Up","m_Up","a_Up");
		
		Bindings.AddKeyBinding("m_Down",KeyEvent.VK_DOWN);
		Bindings.AddKeyBinding("a_Down",KeyEvent.VK_S);
		Bindings.AddORBinding("Down","m_Down","a_Down");
		
		// Initialize some input tracking
		Input.AddInput("Exit",() -> Bindings.GetBinding("Exit").DigitalEvaluation.Evaluate());
		Input.AddInput("Reset",() -> Bindings.GetBinding("Reset").DigitalEvaluation.Evaluate(),true);
		Input.AddInput("Left",() -> Bindings.GetBinding("Left").DigitalEvaluation.Evaluate(),true);
		Input.AddInput("Right",() -> Bindings.GetBinding("Right").DigitalEvaluation.Evaluate(),true);
		Input.AddInput("Up",() -> Bindings.GetBinding("Up").DigitalEvaluation.Evaluate(),true);
		Input.AddInput("Down",() -> Bindings.GetBinding("Down").DigitalEvaluation.Evaluate(),true);
		Input.AddInput("Select",() -> Bindings.GetBinding("Select").DigitalEvaluation.Evaluate(),true);
		
		// Construct the controller
		Controller = new TicTacToeController(Width,Height,WinningLength,Player1Human,Difficulty,Player2Human,Difficulty);
		AddComponent(Controller);
		
		return;
	}
	
	@Override protected void LateInitialize()
	{return;}
	
	@Override protected void Update(long delta)
	{return;}
	
	@Override protected void LateUpdate(long delta)
	{
		if(Input.GracelessInputSatisfied("Exit"))
			Quit();
		else if(Input.GracelessInputSatisfied("Reset"))
			Controller.ResetGame();
		
		return;
	}
	
	@Override protected void Dispose()
	{return;}
	
	@Override protected void LateDispose()
	{return;}
	
	/**
	 * The Tic Tac Toe controller.
	 */
	protected ITicTacToeController Controller;
	
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
	 * True if the first player is human
	 */
	protected boolean Player1Human;
	
	/**
	 * True if the second player is human
	 */
	protected boolean Player2Human;
	
	/**
	 * The difficulty of AI
	 */
	protected int Difficulty;
}