package maze.tile;

import java.io.IOException;
import java.util.EnumSet;

import gamecore.GameEngine;
import gamecore.IUpdatable;
import gamecore.datastructures.grids.FixedSizeGrid;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.datastructures.vectors.Vector2i;
import gamecore.gui.gamecomponents.AffineComponent;
import maze.tile.tiles.PlainTile;
import maze.tile.tiles.TeleportDestinationTile;
import maze.tile.tiles.TeleportStartTile;

/**
 * A 3x3 block of MazeTiles used to make maze generation easy.
 * @author Dawn Nye
 */
public class MazeBigTile extends AffineComponent implements IUpdatable
{
	/**
	 * Creates a 3x3 collection of maze tiles that require no additional parameters to construct.
	 * @param type The tile type. This constructor will only accept {@code PLAIN}, {@code TELEPORT_DESTINATION} and {@code START}.
	 *             If it is not one of these, it will default to PLAIN.
	 * @param exits Each element of this set will provide an exit (and entrance) to this tile.
	 * @param pos The position of this big tile, in the scale of big tiles (zero-indexed)
	 * @throws IOException Thrown if something goes wrong with sprite loading.
	 */
	public MazeBigTile(TileTypes type, EnumSet<Exit> exits, Vector2i pos) throws IOException
	{
		Tiles = new FixedSizeGrid<>(3, 3);
		Pos = pos;

		if(type == TileTypes.TELEPORT_DESTINATION)
			Tiles.Set(new TeleportDestinationTile(), MIDDLE);
		else if(type == TileTypes.START)
			Tiles.Set(new MazeTile(MazeTile.TileID.START, false, false), MIDDLE);
		else
			Tiles.Set(new PlainTile(), MIDDLE);

		ExitsSetup(exits);
		RegularTilesSetup();

	}
	
	/**
	 * Creates a 3x3 collection of maze tiles that require a {@code Vector2d} to construct.
	 * @param type The tile type. This constructor will only accept {@code TELEPORTER}. If it is not one of these, it will default to PLAIN.
	 * @param exits The exits to the big tile.
	 * @param displacement The diplacement the teleporter does to the player.
	 * @param pos The position of this big tile, in the scale of big tiles (zero-indexed)
	 * @throws IOException Thrown if something goes wrong with sprite loading.
	 */
	public MazeBigTile(TileTypes type, EnumSet<Exit> exits, Vector2i displacement, Vector2i pos) throws IOException
	{
		Tiles = new FixedSizeGrid<>(3,3);
		Pos = pos;

		if(type == TileTypes.TELEPORTER)
			Tiles.Set(new TeleportStartTile(displacement), MIDDLE);
			//Tiles.Set(new PlainTile(), MIDDLE);

		ExitsSetup(exits);
		RegularTilesSetup();

	}
	
	/**
	 * Creates a 3x3 collection of maze tiles that require an {@code Exit} to construct.
	 * @param type The tile type. This constructor will only accept {@code EXIT}. If it is not one of these, it will default to PLAIN.
	 * @param exits The exits to the big tile. If this does not contain {@code true_exit}, it will be added to the set.
	 * @param true_exit The exit direction out of the maze.
	 * @param pos The position of this big tile, in the scale of big tiles (zero-indexed)
	 * @throws IOException Thrown if something goes wrong with sprite loading.
	 */
	public MazeBigTile(TileTypes type, EnumSet<Exit> exits, Exit true_exit, Vector2i pos) throws IOException
	{
		Tiles = new FixedSizeGrid<>(3,3);
		Pos = pos;

		Tiles.Set(new PlainTile(), MIDDLE);

		switch(true_exit)//real exit setup
		{
			case UP:
				Tiles.Set(new MazeTile(MazeTile.TileID.T_EXIT, false, true), TOP);
				if(exits.contains(MazeBigTile.Exit.LEFT))
					Tiles.Set(new MazeTile(MazeTile.TileID.L_T_EXIT_WALL, true, false), TL);
				if(exits.contains(MazeBigTile.Exit.RIGHT))
					Tiles.Set(new MazeTile(MazeTile.TileID.R_T_EXIT_WALL, true, false), TR);
				break;
			case DOWN:
				Tiles.Set(new MazeTile(MazeTile.TileID.B_EXIT, false, true), BOTTOM);
				if(exits.contains(MazeBigTile.Exit.LEFT))
					Tiles.Set(new MazeTile(MazeTile.TileID.L_B_EXIT_WALL, true, false), BL);
				if(exits.contains(MazeBigTile.Exit.RIGHT))
				Tiles.Set(new MazeTile(MazeTile.TileID.R_B_EXIT_WALL, true, false), BR);
				break;
			case LEFT:
				Tiles.Set(new MazeTile(MazeTile.TileID.L_EXIT, false, true), LEFT);
				if(exits.contains(MazeBigTile.Exit.UP))
					Tiles.Set(new MazeTile(MazeTile.TileID.T_L_EXIT_WALL, true, false), TL);
				if(exits.contains(MazeBigTile.Exit.DOWN))
					Tiles.Set(new MazeTile(MazeTile.TileID.B_L_EXIT_WALL, true, false), BL);
				break;
			case RIGHT:
				Tiles.Set(new MazeTile(MazeTile.TileID.R_EXIT, false, true), RIGHT);
				if(exits.contains(MazeBigTile.Exit.UP))
					Tiles.Set(new MazeTile(MazeTile.TileID.T_R_EXIT_WALL, true, false), TR);
				if(exits.contains(MazeBigTile.Exit.DOWN))
					Tiles.Set(new MazeTile(MazeTile.TileID.B_R_EXIT_WALL, true, false), BR);
				break;
		}

		ExitsSetup(exits);
		RegularTilesSetup();
	}

	/**
	 * This sets up the exit tiles to other big tiles.
	 * It will not set tiles that are already been set up(in this case, the real exit)
	 * @param exits The EnumSet including all the exits.
	 * @throws IOException
	 */
	protected void ExitsSetup(EnumSet<Exit> exits) throws IOException {
		for(Exit e: exits)
		{
			switch(e)
			{
				case UP:
					if(Tiles.IsCellEmpty(TOP))
						Tiles.Set(new PlainTile(), TOP);
					break;
				case DOWN:
					if(Tiles.IsCellEmpty(BOTTOM))
						Tiles.Set(new PlainTile(), BOTTOM);
					break;
				case LEFT:
					if(Tiles.IsCellEmpty(LEFT))
						Tiles.Set(new PlainTile(), LEFT);
					break;
				case RIGHT:
					if(Tiles.IsCellEmpty(RIGHT))
						Tiles.Set(new PlainTile(), RIGHT);
					break;
			}
		}
	}

	/**
	 * After finish setting up all the exits(to other big tiles or the real exit) and the middle tile,
	 * Set up other regular tiles.
	 * @throws IOException
	 */
	protected void RegularTilesSetup() throws IOException {
		SetTopLeft();
		SetTopRight();
		SetBottomLeft();
		SetBottomRight();
		SetLRTB();
	}

	/**
	 * Set up the top left tile depending on te status of the two adjacent tiles(whether they are exit or not)
	 * @throws IOException
	 */
	protected void SetTopLeft() throws IOException {
		if(Tiles.IsCellOccupied(TL))//if already set up
			return;
		if(Tiles.IsCellOccupied(TOP) && Tiles.IsCellOccupied(LEFT))//if top and left are both exits
			Tiles.Set(new MazeTile(MazeTile.TileID.TL_BULGE_OUT, true, false), TL);
		else if(Tiles.IsCellOccupied(TOP))//if top is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.L_STRAIGHT_WALL, true, false), TL);
		else if(Tiles.IsCellOccupied(LEFT))//if left is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.T_STRAIGHT_WALL, true, false), TL);
		else//if neither is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.TL_BULGE_IN, true, false), TL);
	}

	/**
	 * Set up the top right tile depending on te status of the two adjacent tiles(whether they are exit or not)
	 * @throws IOException
	 */
	protected void SetTopRight() throws IOException {
		if(Tiles.IsCellOccupied(TR))//if already set up
			return;
		if(Tiles.IsCellOccupied(TOP) && Tiles.IsCellOccupied(RIGHT))//if top and right are both exits
			Tiles.Set(new MazeTile(MazeTile.TileID.TR_BULGE_OUT, true, false), TR);
		else if(Tiles.IsCellOccupied(TOP))//if top is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.R_STRAIGHT_WALL, true, false), TR);
		else if(Tiles.IsCellOccupied(RIGHT))//if right is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.T_STRAIGHT_WALL, true, false), TR);
		else//if neither is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.TR_BULGE_IN, true, false), TR);
	}

	/**
	 * Set up the bottom left tile depending on te status of the two adjacent tiles(whether they are exit or not)
	 * @throws IOException
	 */
	protected void SetBottomLeft() throws IOException {
		if(Tiles.IsCellOccupied(BL))//if already set up
			return;
		if(Tiles.IsCellOccupied(BOTTOM) && Tiles.IsCellOccupied(LEFT))//if bottom and left are both exits
			Tiles.Set(new MazeTile(MazeTile.TileID.BL_BULGE_OUT, true, false), BL);
		else if(Tiles.IsCellOccupied(BOTTOM))//if bottom is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.L_STRAIGHT_WALL, true, false), BL);
		else if(Tiles.IsCellOccupied(LEFT))//if left is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.B_STRAIGHT_WALL, true, false), BL);
		else//if neither is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.BL_BULGE_IN, true, false), BL);
	}

	/**
	 * Set up the bottom right tile depending on te status of the two adjacent tiles(whether they are exit or not)
	 * @throws IOException
	 */
	protected void SetBottomRight() throws IOException {
		if(Tiles.IsCellOccupied(BR))//if already set up
			return;
		if(Tiles.IsCellOccupied(BOTTOM) && Tiles.IsCellOccupied(RIGHT))//if bottom and right are both exits
			Tiles.Set(new MazeTile(MazeTile.TileID.BR_BULGE_OUT, true, false), BR);
		else if(Tiles.IsCellOccupied(BOTTOM))//if bottom is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.R_STRAIGHT_WALL, true, false), BR);
		else if(Tiles.IsCellOccupied(RIGHT))//if left is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.B_STRAIGHT_WALL, true, false), BR);
		else//if neither is an exit
			Tiles.Set(new MazeTile(MazeTile.TileID.BR_BULGE_IN, true, false), BR);
	}

	/**
	 * If either of top, bottom, left and right tiles are not exit, set them to walls
	 * @throws IOException
	 */
	protected void SetLRTB() throws IOException {
		if(Tiles.IsCellEmpty(LEFT))
			Tiles.Set(new MazeTile(MazeTile.TileID.L_STRAIGHT_WALL, true, false), LEFT);
		if(Tiles.IsCellEmpty(RIGHT))
			Tiles.Set(new MazeTile(MazeTile.TileID.R_STRAIGHT_WALL, true, false), RIGHT);
		if(Tiles.IsCellEmpty(TOP))
			Tiles.Set(new MazeTile(MazeTile.TileID.T_STRAIGHT_WALL, true, false), TOP);
		if(Tiles.IsCellEmpty(BOTTOM))
			Tiles.Set(new MazeTile(MazeTile.TileID.B_STRAIGHT_WALL, true, false), BOTTOM);
	}


	public void Initialize()
	{
		Initialized = true;
		return;
	}
	
	public boolean Initialized()
	{return Initialized;}
	
	public void Update(long delta)
	{return;}
	
	public void Dispose()
	{
		// We'll just manually dispose of our children if we haven't gotten to them yet
		// Removal will be done in OnRemove if we need to (disposal at the end of the game's execution doesn't need to deal with removal)
		for(MazeTile t : Tiles.Items())
			if(!t.Disposed())
				t.Dispose();
		
		Disposed = true;
		return;
	}
	
	public boolean Disposed()
	{return Disposed;}
	
	@Override public void OnAdd()
	{
		if(!InGame)
			for(int i = 0; i < 3; i++)
			{
				for(int j = 0; j < 3; j++)
				{
					Tiles.Get(j, i).Translate(new Vector2d(16 * (j + 3 * Pos.X), 16 * (i + 3 * Pos.Y)));
					GameEngine.Game().AddComponent(Tiles.Get(j, i));
				}
			}
		
		InGame = true;
		return;
	}
	
	@Override public void OnRemove()
	{
		if(InGame)
			for(MazeTile t : Tiles.Items())
				GameEngine.Game().RemoveComponent(t);
		
		InGame = false;
		return;
	}
	
	/**
	 * The tiles comprising this maze tile.
	 */
	protected FixedSizeGrid<MazeTile> Tiles;
	
	/**
	 * If true, this tile has been initialized.
	 */
	protected boolean Initialized;
	
	/**
	 * If true, this tile has been disposed.
	 */
	protected boolean Disposed;
	
	/**
	 * If true, we're added into the game.
	 */
	protected boolean InGame;


	//The position of this big tile in the entire maze in the scale of big tiles(zero-indexed)
	protected Vector2i Pos;

	//The vectors representing the nine positions of MazeTiles in the big tile
	protected final Vector2i LEFT = new Vector2i(0, 1);
	protected final Vector2i RIGHT = new Vector2i(2, 1);
	protected final Vector2i TOP = new Vector2i(1, 0);
	protected final Vector2i BOTTOM = new Vector2i(1, 2);
	protected final Vector2i MIDDLE = new Vector2i(1, 1);
	protected final Vector2i TL = new Vector2i(0, 0);
	protected final Vector2i TR = new Vector2i(2, 0);
	protected final Vector2i BL = new Vector2i(0, 2);
	protected final Vector2i BR = new Vector2i(2, 2);


	/**
	 * Represents which directions you can exit (or enter) this tile.
	 * @author Dawn Nye
	 */
	public static enum Exit
	{
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	/**
	 * Designates a number of special tiles type for when constructors cannot otherwise reasonably destinguish between them.
	 * @author Dawn Nye
	 */
	public static enum TileTypes
	{
		PLAIN, // An ordinary center tile
		TELEPORT_DESTINATION, // A teleport destination tile
		START, // A start tile
		TELEPORTER, // A teleporter (requires a Vector2d)
		EXIT // An exit from the maze (requires an Exit)
	}
}