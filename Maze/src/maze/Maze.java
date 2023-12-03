package maze;

import java.awt.event.KeyEvent;
import java.io.IOException;

import gamecore.GameEngine;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.input.InputManager;
import gamecore.input.InputMap;
import maze.collision.CollisionEngine;

/**
 * Creates a maze exploration game.
 * @author Dawn Nye
 */
public class Maze extends GameEngine
{
	public Maze(int width, int height)
	{
		super("CSC 207 Maze",null,16 * 3 * width + 16,16 * 3 * height + 39);
		
		Width = width;
		Height = height;
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
		
		Bindings.AddKeyBinding("m_A",KeyEvent.VK_SPACE);
		Bindings.AddKeyBinding("a_A",KeyEvent.VK_E);
		Bindings.AddORBinding("A","m_A","a_A");
		
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
		Input.AddInput("Left",() -> Bindings.GetBinding("Left").DigitalEvaluation.Evaluate());
		Input.AddInput("Right",() -> Bindings.GetBinding("Right").DigitalEvaluation.Evaluate());
		Input.AddInput("Up",() -> Bindings.GetBinding("Up").DigitalEvaluation.Evaluate());
		Input.AddInput("Down",() -> Bindings.GetBinding("Down").DigitalEvaluation.Evaluate());
		Input.AddInput("A",() -> Bindings.GetBinding("A").DigitalEvaluation.Evaluate(),true);
		
		// Initialize the collision engine
		CollisionResolver = new CollisionEngine();
		AddService(CollisionResolver);

		Map = new MazeMap(Width, Height);

		// Generate a new maze
		try {
			GenerateNewMaze();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return;
	}
	
	/**
	 * Generates a new maze.
	 */
	public void GenerateNewMaze() throws IOException
	{
		// Clear the components (but not the important ones)
		RemoveComponent(Input,false);
		RemoveComponent(CollisionResolver,false);
		
		ClearComponents();
		CollisionResolver.Clear();
		
		AddComponent(Input);
		
		// Add the player first so that it's on top of everything else
		// We translate it to its start position later
		AddComponent(player = new Player());
		PutService(Player.class,player);

		// Generate the map
		Map.GenerateNewMap();
		player.SetPos(Map.GetStart());
		
		// Make sure everything gets added to the collision engine fully
		CollisionResolver.Flush();
		
		return;
	}
	
	@Override protected void LateInitialize()
	{
		if(!CollisionResolver.Initialized())
			CollisionResolver.Initialize();
		
		return;
	}
	
	@Override protected void Update(long delta)
	{
		Vector2d pos = player.GetBoundary().Center();
		if(pos.X < 0 || pos.X > 16 * 3 * Width || pos.Y < 0 || pos.Y > 16 * 3 * Height) {
			try {
				GenerateNewMaze();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@Override protected void LateUpdate(long delta)
	{
		if(Input.GracelessInputSatisfied("Exit"))
			Quit();
		
		// We resolve collisions last so that hopefully Java's swing library will paint after we do that and we won't see ugly in between states
		CollisionResolver.Update(delta);
		
		return;
	}
	
	@Override protected void Dispose()
	{return;}
	
	@Override protected void LateDispose()
	{
		if(!CollisionResolver.Disposed())
			CollisionResolver.Dispose();
		
		return;
	}
	
	/**
	 * The input manager for the game.
	 * This is registered as a service.
	 */
	protected InputManager Input;
	
	/**
	 * The player.
	 */
	protected Player player;
	
	/**
	 * The collision resolution engine.
	 */
	protected CollisionEngine CollisionResolver;
	
	/**
	 * The width of the board.
	 */
	protected int Width;
	
	/**
	 * The height of the board.
	 */
	protected int Height;
	
	/**
	 * The maximum number of teleporters allowed.
	 * This value is arbitrary and could be ignored.
	 */
	protected final static int MAX_TELEPORTERS = 3;

	protected MazeMap Map;
}
