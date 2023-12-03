package maze;

import gamecore.datastructures.queues.PriorityQueue;
import gamecore.datastructures.tuples.Triple;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.datastructures.vectors.Vector2i;
import maze.tile.MazeBigTile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Random;

/**
 * This class represents a maze map, it can create maps and visualize it
 * @author Zitan Wang
 */
public class MazeMap{

    /**
     * The constructor, it takes the height and width of the map,
     * Note it does not initialize the paths (the structure) of the maze
     * @param width The width of the map
     * @param height The height of the map
     * @throws IndexOutOfBoundsException Thrown if width or height is less than or equal to 0
     */
    public MazeMap(int width, int height)
    {
        if(width <= 0 || height <= 0)
            throw new IndexOutOfBoundsException();

        Width = width;
        Height = height;
        Exit = null;
        Teleporter = new ArrayList<>();
        Destination = new ArrayList<>();
        Displacement = new ArrayList<>();

        Visited = new boolean[Height][Width];
        Paths = new Neighbors[Height][Width];
        View = new MazeBigTile[Height][Width];

        Initialize();
    }


    /**
     * This disposes the original map(if exist), and then create a new maze map
     * @throws IOException
     */
    public void GenerateNewMap() throws IOException {
        Initialize();
        CreatePaths();
        CreatStart();
        CreateExit();
        CreateTeleporter();
        System.out.println(this.Teleporter);
        System.out.println(this.Destination);
        Visualize();
    }

    /**
     * This initializes Paths and Visited and dispose all the MazeBigTile in View (if any)
     * Teleporter, destination and Displacement set to empty arraylist
     * View should contain all null after call
     */
    public void Initialize()
    {
        for(int i = 0; i < Height; i++)
        {
            for (int j = 0; j < Width; j++)
            {
                Paths[i][j] = new Neighbors();
                Visited[i][j] = false;
                if(View[i][j] != null)
                {
                    View[i][j].Dispose();
                    View[i][j] = null;
                }
            }
        }
        Teleporter = new ArrayList<>();
        Destination = new ArrayList<>();
        Displacement = new ArrayList<>();

    }

    /**
     * This generates random paths between different big tiles
     * The connections(paths) will be stored in Paths
     */
    protected void CreatePaths()
    {
        //pick a random position to start generate path
        Vector2i start = new Vector2i(rand.nextInt(0, Width), rand.nextInt(0, Height));
        //label this position as visited
        Visit(start);

        //priority queue(actually arraylist) of links (connections between two adjacent positions)
        //Item1: the starting position of the link, already visited
        //Item2: the ending position of the link
        //Item3: the weight of the link between the two positions
        ArrayList<Triple <Vector2i, Vector2i, Integer>> links = new ArrayList<>();

        //for all the neighbors of start, add their connection in to links with a random weight
        for(Vector2i dir: DIRECTIONS)
        {
            Vector2i neighbor = start.Add(dir);
            if(IndexValid(neighbor))
            {
                links.add(new Triple<>(start, neighbor, rand.nextInt(0, 100)));
                links.sort(Cmp);
            }
        }

        //while not all positions are visited
        while(!links.isEmpty())
        {
            Triple <Vector2i, Vector2i, Integer> next = links.remove(0);

            if(!IsVisited(next.Item2))//if the second position is not visited, add new connection
            {
                Visit(next.Item2);
                CreateConnection(next.Item1, next.Item2);
            }

            for(Vector2i dir: DIRECTIONS)//generate new edges starting from next
            {
                Vector2i neighbor = next.Item2.Add(dir);
                //if the neighbor has valid index and not visited, add new links to the list
                if(IndexValid(neighbor) && !IsVisited(neighbor))
                {
                    links.add(new Triple<>(next.Item2, neighbor, rand.nextInt(0, 100)));
                    links.sort(Cmp);
                }

            }
        }
    }

    /**
     * This reads all the connections between positions which are stored in Paths to visualize the maze map
     * It sets up View
     * @throws IOException
     */
    public void Visualize() throws IOException {
        for(int i = 0; i < Height; i++)
        {
            for (int j = 0; j < Width; j++)
            {
                //Acquire the directions of exits from this big tile to other tiles
                //This will include the real exit later
                EnumSet<MazeBigTile.Exit> exits = EnumSet.noneOf(MazeBigTile.Exit.class);
                if(Paths[i][j].DirConnected[0])
                    exits.add(MazeBigTile.Exit.UP);
                if(Paths[i][j].DirConnected[1])
                    exits.add(MazeBigTile.Exit.DOWN);
                if(Paths[i][j].DirConnected[2])
                    exits.add(MazeBigTile.Exit.LEFT);
                if(Paths[i][j].DirConnected[3])
                    exits.add(MazeBigTile.Exit.RIGHT);

                //System.out.println(exits);

                //position of the current big tile
                Vector2i pos = new Vector2i(j, i);
                //System.out.println(pos);

                if(Exit.equals(pos))//if this big tile is the real exit
                {
                    //get the direction of the real exit
                    MazeBigTile.Exit trueExit;
                    if(Exit.X == 0) {
                        trueExit = MazeBigTile.Exit.LEFT;
                        exits.add(MazeBigTile.Exit.LEFT);
                    }
                    else if(Exit.X == Width - 1) {
                        trueExit = MazeBigTile.Exit.RIGHT;
                        exits.add(MazeBigTile.Exit.RIGHT);
                    }
                    else if(Exit.Y == 0) {
                        trueExit = MazeBigTile.Exit.UP;
                        exits.add(MazeBigTile.Exit.UP);
                    }
                    else {
                        trueExit = MazeBigTile.Exit.DOWN;
                        exits.add(MazeBigTile.Exit.DOWN);
                    }

                    View[i][j] = new MazeBigTile(MazeBigTile.TileTypes.EXIT, exits, trueExit, pos);
                }

                else if(Start.equals(pos))//if this big tile is the starting point
                {
                    //System.out.println("Start");
                    View[i][j] = new MazeBigTile(MazeBigTile.TileTypes.START, exits, pos);
                }

                else if(Destination != null && Destination.contains(pos))//if this big tile is the teleport destination point
                {
                    View[i][j] = new MazeBigTile(MazeBigTile.TileTypes.TELEPORT_DESTINATION, exits, pos);
                }

                else if(Teleporter != null && Teleporter.contains(pos))//if this big tile is the teleport start point
                {
                    int index = Teleporter.indexOf(pos);
                    System.out.println(pos);
                    //the index of this start position is the same as the index of its displacement though not in different list
                    View[i][j] = new MazeBigTile(MazeBigTile.TileTypes.TELEPORTER, exits, Displacement.get(index), pos);
                }
                else//just a normal big tile
                {
                    View[i][j] = new MazeBigTile(MazeBigTile.TileTypes.PLAIN, exits, pos);
                }
                View[i][j].OnAdd();
            }
        }
    }

    /**
     * Get the connections, for debugging purpose only
     */
    public void GetPaths()
    {
        for(int i = 0; i < Height; i++)
        {
            for(int j = 0; j < Width; j++)
            {
                System.out.print("(" + i + j + ")");
                if(Paths[i][j].DirConnected[0])
                    System.out.print(" UP");
                if(Paths[i][j].DirConnected[1])
                    System.out.print(" DOWN");
                if(Paths[i][j].DirConnected[2])
                    System.out.print(" LEFT");
                if(Paths[i][j].DirConnected[3])
                    System.out.print(" RIGHT");
                System.out.println("");
            }
        }
    }
    /**
     * Check whether the position is in bound
     * @param pos The Vector2i varaible representing a position
     * @return True if in bound, false otherwise
     * @throws NullPointerException Thrown if pos is null
     */
    protected boolean IndexValid(Vector2i pos)
    {
        if(pos == null)
            throw new NullPointerException();
        return (pos.X < Width && pos.X >= 0) && (pos.Y < Height && pos.Y >= 0);
    }

    /**
     * This labels the position as visited
     * @param pos The position been visited
     * @throws NullPointerException Thrown if pos is null
     * @throws IndexOutOfBoundsException Thrown if the position is invalid
     */
    protected void Visit(Vector2i pos)
    {
        if(pos == null)
            throw new NullPointerException();
        if(!IndexValid(pos))
            throw new IndexOutOfBoundsException();
        Visited[pos.Y][pos.X] = true;
    }

    /**
     * This checks whether the position is already visited
     * @param pos The position to be checked
     * @return Ture if visited, false otherwise
     * @throws NullPointerException Thrown if pos is null
     * @throws IndexOutOfBoundsException Thrown if the position is invalid
     */
    protected boolean IsVisited(Vector2i pos)
    {
        if(pos == null)
            throw new NullPointerException();
        if(!IndexValid(pos))
            throw new IndexOutOfBoundsException();
        return Visited[pos.Y][pos.X];
    }

    /**
     * Create a connection between two adjacent positions
     * @param posA The first position
     * @param posB The second position
     */
    protected void CreateConnection(Vector2i posA, Vector2i posB) {
        for (int i = 0; i < 4; i++)
        {
            if(posA.Add(DIRECTIONS[i]).equals(posB))
                Paths[posA.Y][posA.X].DirConnected[i] = true;

            if(posB.Add(DIRECTIONS[i]).equals(posA))
                Paths[posB.Y][posB.X].DirConnected[i] = true;
        }
    }

    /**
     * This creates the real exit of the maze map
     * Since the start position will be generated fist,
     * the exit should not have the same position as the start position
     */
    protected void CreateExit()
    {
        int indexX;
        int indexY;
        do
        {
            indexX = rand.nextInt(0, Width);//generates a random x index
            if(indexX == 0 || indexX == Width - 1)//if the exit is on the left edge or right edge
                indexY = rand.nextInt(0, Height);//choose any in bound y value
            else//if the exit is on the top edge or bottom edge
                indexY = (Height - 1) * rand.nextInt(0, 2);//the y value can only be 0 or (Height - 1)
            Exit = new Vector2i(indexX, indexY);
        } while(Start.equals(Exit));
    }

    /**
     * Generate a random starting position
     */
    protected void CreatStart()
    {
        Start = new Vector2i(rand.nextInt(0, Width), rand.nextInt(0, Height));
    }

    /**
     * Each dead end will have 20% change to have a teleport
     */
    protected void CreateTeleporter()
    {
        ArrayList<Vector2i> ends = GetDeadEndes();
        while(ends.size() >= 2)//while the number of remaining dead ends is enough
        {
            int indexIn = rand.nextInt(0, ends.size());
            int indexOut= 0;
            while(indexIn == indexOut)
                indexOut = rand.nextInt(0, ends.size());
            //get the index of teleport and destination which should not be the same

            Vector2i t = ends.remove(indexIn);
            Vector2i d;
            if(indexIn < indexOut)
                d = ends.remove(indexOut - 1);
            else
                d = ends.remove(indexOut);
            //remove these two dead ends from the arraylist, so they will not be used again

            if(rand.nextInt(0, 10) <= 1)//20% chance to generate this teleport
            {
                Teleporter.add(t);
                Destination.add(d);
                Displacement.add(t.Subtract(d).Multiply(-1));
            }
        }
    }

    /**
     * This gets the indexes of dead ends
     * Note that a start position or an exit(real) position should not be counted as a dead end
     * It is guaranteed that neither Start nor Exit is null
     * @return The ArrayList include indexes of all dead ends
     */
    protected ArrayList<Vector2i> GetDeadEndes()
    {
        ArrayList<Vector2i> ends = new ArrayList<>();
        //for each position, if it is only connected to one adjacent position, it is a dead end
        for(int i = 0; i < Height; i++)
        {
            for(int j = 0; j < Width; j++)
            {
                int count = 0;
                for(int k = 0; k < 4; k++)
                {
                    if(Paths[i][j].DirConnected[k])
                        count ++;
                }
                Vector2i pos = new Vector2i(j, i);
                if(count == 1 && !pos.equals(Start) && !pos.equals(Exit))
                    ends.add(new Vector2i(j, i));
            }
        }
        return ends;
    }

    public Vector2d GetStart()
    {
        return new Vector2d(Start.X * 3 * 16 + 16, Start.Y * 3 * 16 + 16);
    }

    //The width of the maze board(in terms of big tiles)
    protected int Width;

    //The height of the maze board(in terms of big tiles)
    protected int Height;

    //The position of the real exit
    protected Vector2i Exit;

    //The position of the start
    protected Vector2i Start;

    //The set of all displacements made by teleporting
    protected ArrayList<Vector2i> Displacement;

    //The starting positions of teleporter
    protected ArrayList<Vector2i> Teleporter;

    //The destinations of teleporter
    protected ArrayList<Vector2i> Destination;

    //The 2D array indicating whether each position os visited or not
    protected boolean Visited [][];

    //The 2D array labeling all the connections between each position and its adjacent positions
    protected Neighbors Paths [][];

    //The 2D array containing all the MazeBigTiles
    protected MazeBigTile View [][];

    //random
    protected Random rand = new Random();

    //The comparator to compare triples in the priority queue
    protected final Comparator Cmp = new LinkComparator();

    //The array indicating directions of movement (in the scale of BigMazeTile)
    protected final Vector2i DIRECTIONS [] = {
        Vector2i.UP, Vector2i.DOWN, Vector2i.LEFT, Vector2i.RIGHT
    };



    /**
     * The class indicating the connection to its neighbors
     */
    public class Neighbors
    {
        /**
         * constructor
         */
        public Neighbors()
        {
            DirConnected = new boolean[4];
        }

        //From index 0-3, representing up, down, left, right
        //True if connected, false otherwise
        protected boolean DirConnected [];
    }

    /**
     * The comparator to compare two links
     * The one with greater weight(Item3) wins
     */
    public class LinkComparator implements Comparator<Triple<Vector2i, Vector2i, Integer>> {
        @Override
        public int compare(Triple<Vector2i, Vector2i, Integer> a, Triple<Vector2i, Vector2i, Integer> b) {
            return a.Item3.compareTo(b.Item3);
        }
    }
}
