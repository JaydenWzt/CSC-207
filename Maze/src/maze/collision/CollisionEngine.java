package maze.collision;

import java.util.Iterator;

import gamecore.IUpdatable;
import gamecore.LINQ.LINQ;
import gamecore.datastructures.CellRectangle;
import gamecore.datastructures.ICollection;
import gamecore.datastructures.LinkedList;
import gamecore.datastructures.trees.AABBTree;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.datastructures.vectors.Vector2i;
import maze.Player;

/**
 * Handles collisions.
 * @author Dawn Nye
 */
public class CollisionEngine implements IUpdatable, ICollection<ICollidable>
{
	/**
	 * Creates an empty collision engine.
	 */
	public CollisionEngine()
	{
		Kinetics = new LinkedList<ICollidable>();
		Statics = new AABBTree<ICollidable>(c -> c.GetBoundary());
		
		DelayedAdds = new LinkedList<ICollidable>();
		
		Initialized = false;
		Disposed = false;
		
		return;
	}
	
	/**
	 * Creates a collision engine.
	 * @param seed The initial elements to include in the collision engine.
	 */
	public CollisionEngine(Iterable<? extends ICollidable> seed)
	{
		Kinetics = new LinkedList<ICollidable>();
		Statics = new AABBTree<ICollidable>(c -> c.GetBoundary(),seed);
		
		DelayedAdds = new LinkedList<ICollidable>();
		
		Initialized = false;
		Disposed = false;
		
		return;
	}
	
	public void Initialize()
	{return;}
	
	public boolean Initialized()
	{return Initialized;}

	public void Update(long delta)
	{
		// We only care about kinetic-static collisions
		for(ICollidable c : Kinetics)
			for(ICollidable stat : Statics.Query(c))
			{
				// If we're a trigger, do that first in case magic happens
				if(stat.IsTrigger())
					stat.Trigger(c);
				
				// We don't do an else if here because maybe we trigger something by bumping into a solid
				if(stat.IsSolid())
					ResolveKineticStaticCollision(c,stat);
			}
		
		return;
	}
	
	/**
	 * Resolves a kinetic-static collision by pushing {@code kinetic} out to the nearest surface.
	 * @param kinetic The kinetic object.
	 * @param stat The static object.
	 * @throws NullPointerException Thrown if {@code kinetic} or {@code stat} is null.
	 */
	protected void ResolveKineticStaticCollision(ICollidable kinetic, ICollidable stat)
	{

		CellRectangle k = kinetic.GetBoundary();
		CellRectangle s = stat.GetBoundary();
		CellRectangle intersection = k.Intersection(s);

		if(intersection.IsEmpty())//if the two are not colliding, do nothing
			return;

		//Get the position of static relative to kinetic
		int dirX = (k.Center().X < s.Center().X) ? 1 : -1;
		int dirY = (k.Center().Y < s.Center().Y) ? 1 : -1;

		if (intersection.Width() < intersection.Height())//collision on left or right
		{
			kinetic.Translate(new Vector2d(-1.1 * intersection.Width() * dirX, 0));
		}
		else//collision on top or bottom
		{
			kinetic.Translate(new Vector2d(0, -1.1 * intersection.Height() * dirY));
		}
	}
	
	public void Dispose()
	{
		Disposed = true;
		return;
	}
	
	public boolean Disposed()
	{return Disposed;}
	
	/**
	 * Adds {@code c} to the collision engine.
	 * @param c The collidable object to add.
	 * @return Returns true if the add was successful and false otherwise.
	 * @implNote We do not prohibit duplicate values.
	 * @throws NullPointerException Thrown if {@code c} is null.
	 */
	public boolean Add(ICollidable c)
	{
		if(c.IsStatic())
			return Statics.Add(c);
		else
			return Kinetics.Add(c);
	}
	
	/**
	 * Adds {@code c} to the collision engine.
	 * However, it does not add this right away.
	 * It waits until a call to {@code Flush} is made.
	 * This is primarily useful for adding static objects to the collision engine before they have been moved to their final location.
	 * @param c The collidable object to add.
	 * @throws NullPointerException Thrown if {@code c} is null.
	 */
	public void DelayAdd(ICollidable c)
	{
		if(c == null)
			throw new NullPointerException();
		
		DelayedAdds.Add(c);
		return;
	}
	
	/**
	 * Flushes the delayed add list by adding all of them to the collision engine properly.
	 * @return Returns the number of items added successfully.
	 */
	public int Flush()
	{
		int ret = 0;
		
		for(ICollidable c : DelayedAdds)
			if(Add(c))
				ret++;
		
		DelayedAdds.Clear();
		return ret;
	}
	
	/**
	 * Removes {@code c} from the collision engine.
	 * @param c The collidable object to remove.
	 * @return Returns true if the remove was successful and false otherwise.
	 * @throws NullPointerException Thrown if {@code c} is null.
	 */
	public boolean Remove(ICollidable c)
	{
		if(c.IsStatic())
			return Statics.Remove(c);
		else
			return Kinetics.Remove(c);
	}
	
	@Override public boolean Contains(ICollidable t)
	{return Kinetics.Contains(t) || Statics.Contains(t);}
	
	public int Count()
	{return Kinetics.Count() + Statics.Count();}
	
	/**
	 * Empties the collision engine of all collidable objects
	 */
	public void Clear()
	{
		Kinetics.Clear();
		Statics.Clear();
		
		return;
	}
	
	@Override public Iterator<ICollidable> iterator()
	{return LINQ.Concatenate(Kinetics,Statics).iterator();}
	
	/**
	 * The kinetic objects belonging to this collision engine.
	 */
	protected LinkedList<ICollidable> Kinetics;
	
	/**
	 * The static objects belonging to this collision engine.
	 */
	protected AABBTree<ICollidable> Statics;
	
	/**
	 * The collidable objects we are delaying to add until a Flush call.
	 */
	protected LinkedList<ICollidable> DelayedAdds;
	
	/**
	 * If true, this collision engine is initialized.
	 */
	protected boolean Initialized;
	
	/**
	 * If true, this collision engine is disposed.
	 */
	protected boolean Disposed;
}
