package maze.tile.tiles;

import java.io.File;
import java.io.IOException;

import gamecore.GameEngine;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.datastructures.vectors.Vector2i;
import gamecore.gui.gamecomponents.AnimatedComponent;
import gamecore.input.InputManager;
import gamecore.observe.IObserver;
import gamecore.sprites.Animation;
import gamecore.time.TimePartition;
import maze.Player;
import maze.collision.ICollidable;
import maze.tile.MazeTile;

/**
 * A start tile of teleporting.
 * Revised from TeleportDestinationTile
 * @author Zitan Wang
 */
public class TeleportStartTile extends MazeTile
{
    /**
     * Creates a new teleport start tile.
     * @throws IOException Thrown if something goes wrong with the sprite loading.
     */
    public TeleportStartTile(Vector2i displacemement) throws IOException
    {
        super(TileID.PLAIN,false,true);

        Displacement = (new Vector2d(displacemement.Multiply(3*16)));
        TeleporterStart = new AnimatedComponent(new Animation(new File("assets/animations/Active Teleport.animation")));
        TeleporterStart.SetParent(this);



        return;
    }

    public void Dispose()
    {
        // We'll just manually dispose of our children if we haven't gotten to them yet
        // Removal will be done in OnRemove if we need to (disposal at the end of the game's execution doesn't need to deal with removal)
        if(!TeleporterStart.Disposed())
            TeleporterStart.Dispose();

        super.Dispose();
        return;
    }

    @Override public void OnAdd()
    {
        if(InGame)
            return;

        super.OnAdd();

        // Add the teleporter after this component so it gets drawn on top of it
        GameEngine.Game().AddComponent(TeleporterStart,GameEngine.Game().IndexOfComponent(this));

        return;
    }

    @Override public void OnRemove()
    {
        if(!InGame)
            return;

        super.OnRemove();
        GameEngine.Game().RemoveComponent(TeleporterStart);

        return;
    }

    /**
     * This is called when it collides with another object. Only works on a plyaer object to play the teleport animation and teleport the player
     * @param other The collidable object that bumped into this. In this case, this should be the player object
     */
    public void Trigger(ICollidable other)
    {
        if(!(other instanceof Player))
            return;

        InputManager Input = GameEngine.Game().<InputManager>GetService(InputManager.class);
        if(Input.GracelessInputSatisfied("A"))
        {
            ((Player) other).Teleport(Displacement);
            /*
            ((Player) other).Freeze();
            GameEngine.Game().AddComponent(this.Portal, 0);
            this.Portal.Play();

            other.Translate(Displacement);
            ((Player) other).ReversePortalAnimation();
            ((Player) other).Unfreeze();
             */
        }
    }


    /**
     * The animation to play for the teleporter.
     */
    protected AnimatedComponent TeleporterStart;

    protected Vector2d Displacement;



}