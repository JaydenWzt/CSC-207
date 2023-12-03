package maze;

import java.io.File;
import java.io.IOException;

import gamecore.GameEngine;
import gamecore.datastructures.CellRectangle;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.datastructures.vectors.Vector2i;
import gamecore.gui.gamecomponents.AnimatedComponent;
import gamecore.gui.gamecomponents.ImageComponent;
import gamecore.input.InputManager;
import gamecore.observe.IObserver;
import gamecore.sprites.Animation;
import gamecore.time.TimePartition;
import maze.collision.CollisionEngine;
import maze.collision.ICollidable;

/**
 * A very simple player capable of moving around
 * @author Dawn Nye
 */
public class Player extends ImageComponent implements ICollidable, IObserver<TimePartition.TimeEvent>
{
	/**
	 * Creates a new player.
	 */
	public Player() throws IOException {
		super(new File("assets/images/Player.png"));

		Frozen = false;
		InGame = false;

		Animation animation1 = new Animation(new File("assets/animations/Portal.animation"));
		Portal = new AnimatedComponent(animation1);
		Portal.SetParent(this);
		Portal.Translate(new Vector2d(-8, -3));
		animation1.Subscribe(this);

		Animation animation2 = new Animation(new File("assets/animations/Reverse Portal.animation"));
		ReversePortal = new AnimatedComponent(animation2);
		ReversePortal.SetParent(this);
		ReversePortal.Translate(new Vector2d(-8, -3));
		animation2.Subscribe(this);

		PortalStarted = false;
		ReversePortalStarted = false;
		return;
	}

	/**
	 * This sets the player to the designated position
	 * @param pos The desginated position
	 */
	public void SetPos(Vector2d pos)
	{
		//set to (0,0)
		Translate(this.GetPosition(true).Multiply(-1.0));
		Translate(new Vector2d(pos.X + 3, pos.Y + 3.5));
	}

	/**
	 * Start the teleport, including animation
	 * @param displacement The displacement done to the player by the teleport
	 */
	public void Teleport(Vector2d displacement)
	{
		Freeze();

		Displacement = displacement;
		GameEngine.Game().AddComponent(Portal, 0);
		Portal.Play();
		PortalStarted = true;

	}

	/**
	 * Performed wrhen the first or second animation of teleport ends
	 * @param event The observation.
	 */
	@Override
	public void OnNext(TimePartition.TimeEvent event) {
		if(event.IsEndOfTime())
		{
			if(PortalStarted && !ReversePortalStarted)
			{
				Portal.Stop();
				GameEngine.Game().RemoveComponent(Portal);

				this.Translate(Displacement);

				GameEngine.Game().AddComponent(ReversePortal, 0);
				ReversePortal.Play();

				this.PortalStarted = false;
				this.ReversePortalStarted = true;
			}

			else if(ReversePortalStarted && !PortalStarted)
			{
				ReversePortal.Stop();
				GameEngine.Game().RemoveComponent(this.ReversePortal);

				ReversePortalStarted = false;

				Unfreeze();
			}
		}
	}

	
	public void Update(long delta)
	{
		if(IsFrozen())
			return;
			InputManager Input = GameEngine.Game().<InputManager>GetService(InputManager.class);

			if (Input.GracelessInputSatisfied("Left"))
				Translate(Vector2i.LEFT.Multiply(Speed));

			if (Input.GracelessInputSatisfied("Right"))
				Translate(Vector2i.RIGHT.Multiply(Speed));

			if (Input.GracelessInputSatisfied("Up"))
				Translate(Vector2i.UP.Multiply(Speed));

			if (Input.GracelessInputSatisfied("Down"))
				Translate(Vector2i.DOWN.Multiply(Speed));
	}
	
	@Override public void OnAdd()
	{
		super.OnAdd();

		if(InGame)
			return;
		
		InGame = true;
		
		GameEngine.Game().<CollisionEngine>GetService(CollisionEngine.class).Add(this);
		return;
	}
	
	@Override public void OnRemove()
	{
		super.OnRemove();
		
		if(!InGame)
			return;
		
		InGame = false;
		
		GameEngine.Game().<CollisionEngine>GetService(CollisionEngine.class).Remove(this);
		return;
	}
	
	/**
	 * Freezes the player to prevent them from moving.
	 */
	public void Freeze()
	{
		Frozen = true;
		return;
	}
	
	/**
	 * Allows the player to move if frozen.
	 */
	public void Unfreeze()
	{
		Frozen = false;
		return;
	}
	
	/**'
	 * Determines if the player is frozen.
	 */
	public boolean IsFrozen()
	{return Frozen;}
	
	@Override public Vector2d Velocity()
	{return delta_p;}
	
	public CellRectangle GetBoundary()
	{return new CellRectangle(new Vector2i(GetPosition(true)),10,10);}
	
	public boolean IsStatic()
	{return false;}
	
	public boolean IsSolid()
	{return true;}
	
	public boolean IsTrigger()
	{return false;}

	@Override
	public void OnError(Exception e) {
		return;
	}

	@Override
	public void OnCompleted() {
		return;
	}
	
	/**
	 * The velocity of this player.
	 */
	protected Vector2d delta_p;
	
	/**
	 * If true, we're frozen and cannot move.
	 */
	protected boolean Frozen;
	
	/**
	 * If true, we're already in the game.
	 */
	protected boolean InGame;

	
	/**
	 * The speed of the player.
	 */
	public static final double Speed = 2.0;



	//The teleporting animation
	protected AnimatedComponent Portal;

	//The reverse teleporting animation
	protected AnimatedComponent ReversePortal;

	//Whether the teleporting animation is started or not
	protected boolean PortalStarted;

	//whether the reversed teleporting animation is started or not
	protected boolean ReversePortalStarted;

	//The displacement one teleport done to this plyaer
	protected Vector2d Displacement;
}
