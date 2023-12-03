package scouts.AI.robot;

import gamecore.algorithms.GraphAlgorithms;
import gamecore.datastructures.ArrayList;
import gamecore.datastructures.graphs.IGraph;
import gamecore.datastructures.grids.VariableSizeGrid;
import gamecore.datastructures.matrices.Matrix2D;
import gamecore.datastructures.tuples.Pair;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.datastructures.vectors.Vector2i;
import gamecore.gui.gamecomponents.ImageComponent;
import scouts.AI.Artifact;
import scouts.AI.IRobot;
import scouts.AI.move.Direction;
import scouts.AI.move.Move;
import scouts.AI.move.VisionData;
import scouts.arena.Area;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class MyRobot extends ImageComponent implements IRobot {

    public MyRobot() throws IOException  {
        super(new File("my asset/ghost2.png"));
        Translate(new Vector2d(1.0d, 3.0d));


        Disposed = false;
        Initialized = false;
        MyPos = new Vector2i(0, 0);
        NextPos = MyPos;
        MyTile = null;
        MyGrid = new VariableSizeGrid<>();
        ArtifactsGained = new ArrayList<>();
        Artifacts = new ArrayList<>();
        NextMove = Move.CreateNoOP();
        Energy = -1;
        TakeArtifact = false;
        TargetArtifact = null;
        ArtifactsAccessed = new ArrayList<>();
        //visited = new VariableSizeGrid<>();
        //visited.Set(true, MyPos);
    }
    /**
     * Determines the next action the robot wishes to take.
     *
     * @return Returns the desired action to undergo.
     * @implSpec This function's execution will be terminated if it takes too long.
     * As such, it must be able to start fresh each call.
     * Be careful not to permit your data to become corrupt.
     * You should store information in such a way that if you halt early, your data is valid but incomplete rather than incorrect.
     */
    @Override
    public Move GetNextAction() {
        NextMove = Move.CreateNoOP();

        if(MyGrid.Get(MyPos).HasBattery)//if the robot is on a battery
            return NextMove = Move.CreateBatteryPickup();
        if(MyGrid.Get(MyPos).IsIdol() && ArtifactsGained.isEmpty() && !ArtifactsAccessed.contains(MyPos))//if the robot is on an artifact
        {
            TakeArtifact = true;
            switch (MyGrid.Get(MyPos).Environment)
            {
                case NE:
                    TargetArtifact = Artifact.NE_IDOL;
                    break;
                case NW:
                    TargetArtifact = Artifact.NW_IDOL;
                    break;
                case SE:
                    TargetArtifact = Artifact.SE_IDOL;
                    break;
                case SW:
                    TargetArtifact = Artifact.SW_IDOL;
                    break;
            }
            return NextMove = Move.CreateIdolPickup();
        }
        if(MyGrid.Get(MyPos).IsAltar() && !ArtifactsGained.isEmpty())//if the robot is on an altar and has an artifact
        {
            return NextMove = Move.CreateIdolDrop(ArtifactsGained.remove(0));
        }
        //the order of these three if statements is does not matter, as only onn of them can be true at a time

        /*
        //this part check whether adjacent to another robot and if so, whether it has an artifact
        //if it does, it will attempt to steal it
        if(Energy > 1000 && ArtifactsGained.isEmpty())
        {
            for(Vector2i neighbotPos: MyGrid.NeighborIndexSet(MyPos, true))
            {
                if(MyGrid.Get(neighbotPos).HasRobot())
                {
                    int thisRobotID = MyGrid.Get(neighbotPos).RobotID;
                    for(Pair<Vector2i, Integer> artifact: Artifacts)
                    {
                        if(thisRobotID == artifact.Item2)
                        {
                            Vector2i dir = neighbotPos.Subtract(MyPos);
                            if(dir.X == -1)
                                return NextMove = Move.CreateTheft(Direction.LEFT);
                            else if(dir.X == 1)
                                return NextMove = Move.CreateTheft(Direction.RIGHT);
                            else if(dir.Y == -1)
                                return NextMove = Move.CreateTheft(Direction.DOWN);
                            else if(dir.Y == 1)
                                return NextMove = Move.CreateTheft(Direction.UP);
                        }
                    }
                }
            }
        }
         */

        double distance = Integer.MAX_VALUE;

        //System.out.println("MyMap");
        //System.out.println(MyGrid);
        //System.out.println();
        //System.out.println("Have artifacts: " + this.ArtifactsGained);
        //This part checks the entire map that this robot can see for artifacts, altars, batteries, and robots with artifacts
        for(Vector2i tilePos: MyGrid.IndexSet(true))
        {
            //System.out.println("Tile: " + MyGrid.Get(tilePos));

            //if the tile has an artifact and the robot does not have an artifact
            if(MyGrid.Get(tilePos).IsIdol() && ArtifactsGained.isEmpty() && !ArtifactsAccessed.contains(tilePos))
            {
                //System.out.println("To Artifact");
                return NextMove = NextMoveToDes(tilePos, true);
            }
            //if the tile is an altar and the robot has an artifact
            if(MyGrid.Get(tilePos).IsAltar() && !ArtifactsGained.isEmpty())
            {
                //System.out.println("To Altar");
                return NextMove = NextMoveToDes(tilePos, true);
            }
            /*
            //if the tile has a battery and the robot is not on a battery and the battery is closer than the current closest battery
            if(MyGrid.Get(tilePos).HasBattery && tilePos.Subtract(MyPos).Magnitude() < distance)
            {
                distance = tilePos.Subtract(MyPos).Magnitude();
                NextMove = Move.CreateBatteryPickup();
            }
            */
            /*
            //if the tile has a robot and the robot has an artifact and the robot holding an artifact
            if(MyGrid.Get(tilePos).HasRobot() && Energy > 1000)
            {
                int thisRobotID = MyGrid.Get(tilePos).RobotID;
                for(Pair<Vector2i, Integer> artifact: Artifacts)
                {
                    if(thisRobotID == artifact.Item2)
                        return NextMove = NextMoveToDes(tilePos, true);
                }
            }

             */
        }

        //if the robot has an artifact and the altar is not in sight, it will try to move to the altar
        if(!ArtifactsGained.isEmpty())
        {
            //System.out.println("Try to go to altar");
            return NextMove = NextMoveToAltar();
        }

        return NextMove = RandomMove();
    }

    /**
     * This function to indicate which direction the robot needs to move to reach the destination
     * The destination can either be explicit or implicit(simply which direction to move)
     * @param destination the destination to move to
     * @return
     */
    private Move NextMoveToDes(Vector2i destination, boolean explicit)
    {
        //System.out.println(MyPos + " to " + destination + " explicit: " + explicit);
        Vector2i dir;
        if(explicit)
            dir = destination.Subtract(MyPos);//get the relative direction between the robot and the destination
        else
            dir = destination;//if implicit, the destination is the direction to move

        //System.out.println("dir: " + dir);
        int dirX = dir.X == 0? 0 : dir.X/Math.abs(dir.X);//get whether the destination is to the left or right of the robot
        int dirY = dir.Y == 0? 0 : dir.Y/Math.abs(dir.Y);//get whether the destination is to the up or down of the robot
        Vector2i choiceX = MyPos.Add(new Vector2i(dirX, 0));//get whether the destination is to the left or right of the robot
        Vector2i choiceY = MyPos.Add(new Vector2i(0, dirY));//get whether the destination is to the up or down of the robot

        //System.out.println("dirX: " + dirX);
        //System.out.println("dirY: " + dirY);
        //if the robot is further to the destination in the x direction or if the robot is equidistant in both directions and the x direction is cheaper
        if(Math.abs(dir.X) > Math.abs(dir.Y) || Math.abs(dir.X) == Math.abs(dir.Y))
        {
            if(dir.X > 0)//if the destination is to the right of the robot
            {
                NextPos = MyPos.Add(new Vector2i(1, 0));
                //System.out.println("Go Right");
                //System.out.println("MyPos: " + MyPos);
                //System.out.println("NextPos: " + NextPos);
                return Move.CreateMovement(Direction.RIGHT);
            }
            else//if the destination is to the left of the robot
            {
                NextPos = MyPos.Add(new Vector2i(-1, 0));
                //System.out.println(NextPos);
                //System.out.println("Go Left");
                //System.out.println("MyPos: " + MyPos);
                //System.out.println("NextPos: " + NextPos);
                return Move.CreateMovement(Direction.LEFT);
            }
        }
        //if the robot is further to the destination in the y direction or if the robot is equidistant in both directions and the y direction is cheaper
        else if(Math.abs(dir.X) < Math.abs(dir.Y))
        {
            if (dir.Y > 0)//if the destination is to the up of the robot
            {
                NextPos = MyPos.Add(new Vector2i(0, 1));
                //System.out.println("Go Up");
                //System.out.println("MyPos: " + MyPos);
                //System.out.println("NextPos: " + NextPos);
                return Move.CreateMovement(Direction.UP);

            }
            else//if the destination is to the down of the robot
            {
                NextPos = MyPos.Add(new Vector2i(0, -1));
                //System.out.println(NextPos);
                //System.out.println("Go Down");
                //System.out.println("MyPos: " + MyPos);
                //System.out.println("NextPos: " + NextPos);
                return Move.CreateMovement(Direction.DOWN);
            }
        }
        else//just in case
            return RandomMove();
    }

    private Move NextMoveToAltar()
    {
        //System.out.println("To Altar, Unknown Position");
        boolean leftdiffer = false;
        boolean rightdiffer = false;
        boolean updiffer = false;
        boolean downdiffer = false;

        Vector2i up = MyPos.Add(Vector2i.DOWN);
        Vector2i down = MyPos.Add(Vector2i.UP);
        Vector2i left = MyPos.Add(Vector2i.LEFT);
        Vector2i right = MyPos.Add(Vector2i.RIGHT);

        //check if the environment is different in each direction
        //to make prediction of the current pos relative to the altar
        if(!MyGrid.IsCellEmpty(left) && !MyTile.Environment.equals(MyGrid.Get(left).Environment))
            leftdiffer = true;
        else if(!MyGrid.IsCellEmpty(right) && !MyTile.Environment.equals(MyGrid.Get(right).Environment))
            rightdiffer = true;
        if(!MyGrid.IsCellEmpty(up) && !MyTile.Environment.equals(MyGrid.Get(up).Environment))
            updiffer = true;
        else if(!MyGrid.IsCellEmpty(down) && !MyTile.Environment.equals(MyGrid.Get(down).Environment))
            downdiffer = true;

        //if the environment is the same in all directions
        //then the robot will move diagonally to approach the altar
        if(!(leftdiffer || rightdiffer || updiffer || downdiffer))
        {
            if(MyTile.Environment.equals(Area.NE))
                return NextMoveToDes(new Vector2i(-1, -1), false);
            else if(MyTile.Environment.equals(Area.NW))
                return NextMoveToDes(new Vector2i(1, -1), false);
            else if(MyTile.Environment.equals(Area.SE))
                return NextMoveToDes(new Vector2i(-1, 1), false);
            else if(MyTile.Environment.equals(Area.SW))
                return NextMoveToDes(new Vector2i(1, 1), false);
            else//just in case
                return RandomMove();
        }

        //if the environment is different in each direction
        //then the robot will move along the edge of the different environment to reach the altar
        else if((rightdiffer && MyTile.Environment.equals(Area.NW)) || (leftdiffer && MyTile.Environment.equals(Area.NE)))
            return NextMoveToDes(new Vector2i(0, -1), false);
        else if((rightdiffer && MyTile.Environment.equals(Area.SW)) || (leftdiffer && MyTile.Environment.equals(Area.SE)))
            return NextMoveToDes(new Vector2i(0, 1), false);
        else if((updiffer && MyTile.Environment.equals(Area.SW)) || (downdiffer && MyTile.Environment.equals(Area.NW)))
            return NextMoveToDes(new Vector2i(1, 0), false);
        else if((updiffer && MyTile.Environment.equals(Area.SE)) || (downdiffer && MyTile.Environment.equals(Area.NE)))
            return NextMoveToDes(new Vector2i(-1, 0), false);
        else//just in case
            return RandomMove();

    }

    private Move RandomMove()
    {
        //System.out.println("Random Move");
        Vector2i up = MyPos.Add(Vector2i.DOWN);
        Vector2i down = MyPos.Add(Vector2i.UP);
        Vector2i left = MyPos.Add(Vector2i.LEFT);
        Vector2i right = MyPos.Add(Vector2i.RIGHT);

        /*
        boolean [] n = new boolean[4];
        n[0] = !visited.IsCellEmpty(up);
        n[1] = !visited.IsCellEmpty(down);
        n[2] = !visited.IsCellEmpty(left);
        n[3] = !visited.IsCellEmpty(right);

        int choice = (int)(Math.random() * 4);

        if(n[0] && n[1] && n[2] && n[3])
            choice = (int)(Math.random() * 4);
        else
            while(n[choice])
                choice = (int)(Math.random() * 4);
         */
        int choice = (int)(Math.random() * 4);

        if(choice == 0)
        {
            /*
            if(MyGrid.IsCellEmpty(up) || MyGrid.Get(up).IsWall())
                return RandomMove();
            else
            {


             */
                NextPos = MyPos.Add(Vector2i.DOWN);
                //System.out.println("Go Up");
                return Move.CreateMovement(Direction.UP);
            //}
        }
        if(choice == 1)
        {
            /*
            if(MyGrid.IsCellEmpty(down) || MyGrid.Get(down).IsWall())
                return RandomMove();
            else
            {

             */
                NextPos = MyPos.Add(Vector2i.UP);
                //System.out.println("Go Down");
                return Move.CreateMovement(Direction.DOWN);
            //}
        }
        if(choice == 2)
        {
            /*
            if(MyGrid.IsCellEmpty(left) || !MyGrid.Get(left).IsWall())
                return RandomMove();
            else
            {

             */
                NextPos = MyPos.Add(Vector2i.LEFT);
                //System.out.println("Go Left");
                return Move.CreateMovement(Direction.LEFT);
            //}
        }
        if(choice == 3)
        {
            /*
            if(MyGrid.IsCellEmpty(right) || MyGrid.Get(MyPos.Add(Vector2i.RIGHT)).IsWall())
                return RandomMove();
            else
            {

             */
                NextPos = MyPos.Add(Vector2i.RIGHT);
                //System.out.println("Go Right");
                return Move.CreateMovement(Direction.RIGHT);
            //}
        }
        //System.out.println("No Move");
        return Move.CreateNoOP();
    }

    /**
     * Informs the robot of how much energy it has.
     *
     * @param e The amount of energy the robot has.
     */
    @Override
    public void EnergyUpdate(int e) {
        Energy = e;
    }

    /**
     * Updates the robot on what it can currently see.
     *
     * @param vision The vision graphs.
     *               <ul>
     *               	<li>The first graph (always present) included is the robot's vision.</li>
     *               	<li>
     *               		The next graphs correspond to drone vision for any drones this robot has released.
     *               		These graphs are given in the order that the drones were created.
     *               		Drones which are out of power (dead) do not appear in this list of graphs.
     *               	</li>
     *               	<li>Lastly, any robots carrying an idol are always visible and placed into their own graph containing only they tile they're on. This can include this robot as well.</li>
     *               </ul>
     *               Each graph is built so that each vertex contains data about one specific tile.
     *               The edge data contains which direction in the world a robot must travel to reach the destination vertex from the source vertex (via a single move action).
     *               The source of the vision (a robot or a drone) is always located at the vertex with ID 0.
     * @implSpec This function's execution will be terminated if it takes too long.
     * As such, it must be able to start fresh each call.
     * Be careful not to permit your data to become corrupt.
     * You should store information in such a way that if you halt early, your data is valid but incomplete rather than incorrect.
     */
    @Override
    public void VisionUpdate(Iterable<IGraph<VisionData, Direction>> vision) {
        Iterator<IGraph<VisionData, Direction>> visions = vision.iterator();//The vision graphs

        MapUpdate(vision.iterator().next());//The first map is the robot's vision, and used to update the map

        //no drone was or will be released, so the rest are all vision of robots carrying idols
        Artifacts.clear();
        while(visions.hasNext()) //Keep record of the artifacts that are carried by other robots
        {
            VisionData PlayerTile = visions.next().GetVertex(0);
            Artifacts.add(new Pair(ArtifactDirection(PlayerTile), PlayerTile.RobotID));
        }
    }

    /**
     * Updates the robot on what artifacts are carried by other robots, and provides the rough direction
     * @param robotTile The tile the other robot is on
     * @return The direction of the artifact
     */
    private Vector2i ArtifactDirection(VisionData robotTile)
    {
        Vector2i player = new Vector2i(-1, -1);
        Vector2i me = new Vector2i(-1, -1);

        switch(robotTile.Environment)//The robot's position
        {
            case NE:
                player = new Vector2i(1, 0);
            case NW:
                player = new Vector2i(0, 0);
            case SE:
                player = new Vector2i(1, 1);
            case SW:
                player = new Vector2i(0, 1);
        }
        switch(MyTile.Environment)//My position
        {
            case NE:
                me = new Vector2i(1, 0);
            case NW:
                me = new Vector2i(0, 0);
            case SE:
                me = new Vector2i(1, 1);
            case SW:
                me = new Vector2i(0, 1);
        }
        return player.Subtract(me);//The direction of the artifact
    }

    /**
     * Updates the map of the world based on the vision of my robot
     * @param vision The vision of my robot
     */
    private void MapUpdate(IGraph<VisionData, Direction> vision)
    {
        //HashSet<Integer> visited = new HashSet<>();//The vertices that have been visited
        MyTile = vision.GetVertex(0); //The tile that my robot is on
        //visited.add(0); //Add the tile that my robot is on to the visited list
        MyGrid.Set(MyTile, MyPos);
        for(int ID:vision.VertexIDs())//For each vertex in the vision
        {
            if(ID == 0)
                continue;
            Vector2i pos = new Vector2i(MyPos);
            int prevID = 0;
            for(int nextID: GraphAlgorithms.FindPath(vision, 0, ID))//For each vertex in the path from my robot to the vertex
            {
                if(nextID == 0)
                    continue;
                Direction dir = vision.GetEdge(prevID, nextID);

                switch (dir)//Add the direction to the position
                {
                    case UP:
                        pos = pos.Add(Vector2i.DOWN);
                        break;
                    case DOWN:
                        pos = pos.Add(Vector2i.UP);
                        break;
                    case LEFT:
                        pos = pos.Add(Vector2i.LEFT);
                        break;
                    case RIGHT:
                        pos = pos.Add(Vector2i.RIGHT);
                        break;
                }

                prevID = nextID;//Set the previous ID to the current ID
            }
            MyGrid.Set(vision.GetVertex(ID), pos);
        }
    }


    /**
     * Reports whether a requested move was successful or not.
     *
     * @param success If true, then the last move this robot attempted was successful. If false, then the last move was unsuccessful.
     * @implNote This function is called exactly once after a robot is asked to move.
     * If a request for a move is terminated early, this will be called after regardless to notify the robot of the failure.
     */
    @Override
    public void ReportSuccess(boolean success) {
        //if(success)
            //System.out.println("Move successful");
        //else
            //System.out.println("Move unsuccessful");
        if(success)
        {
            //System.out.println("last Pos: " + MyPos);
            //System.out.println("next Pos: " + NextPos);
            MyPos = NextPos;
            //MyTile = MyGrid.IsCellEmpty(MyPos) ? null : MyGrid.Get(MyPos);
            //visited.Set(true, MyPos);
            if(TakeArtifact)
            {
                ArtifactsGained.add(TargetArtifact);
                ArtifactsAccessed.add(MyPos);
                TakeArtifact = false;
            }

        }
        else
        {
            NextPos = MyPos;
        }
        TakeArtifact = false;
        //System.out.println("//////////////////////////////");
        //System.out.println("//////////////////////////////");
    }

    /**
     * Asks if the robot wishes to struggle for an artifact.
     *
     * @param a       The artifact that is up for grabs.
     * @param cost    The energy cost required to continue the struggle.
     * @param offense If true, then this robot is the one attempting to steal the artifact {@code a}.
     * @return Returns true if this robot wants to struggle for artifact {@code a} and false otherwise.
     */
    @Override
    public boolean StruggleForArtifact(Artifact a, int cost, boolean offense) {
        return Energy - cost > 100;
    }

    /**
     * Reports a theft of an artifact.
     *
     * @param a      The artifact gained or lost.
     * @param gained If true, then this robot gained artifact {@code a}. If false, it lose artifact {@code a}.
     */
    @Override
    public void ReportTheft(Artifact a, boolean gained) {
        if(gained)
            ArtifactsGained.add(a);
        else
            ArtifactsGained.remove(a);
    }

    /**
     * Called when the robot has been teleported.
     */
    @Override
    public void Teleported() {
        MyPos = new Vector2i(0,0);
        MyTile = null;
        MyGrid = new VariableSizeGrid<>();
        NextMove = Move.CreateNoOP();
    }

    /**
     * Translates the robot's drawing position.
     *
     * @param v The position to translate by.
     * @implNote If you extend a class such as {@code ImageComponent}, {@code MultiImageComponent}, or {@code AnimatedComponent}, you already have a {@code Translate(Vector2d)} function.
     * In this case you will not need to override this.
     * Regardless, you should not take advantage of this function (or any other like it) to cheat the game and attempt to determine your absolute position.
     */
    @Override
    public void Translate(Vector2d v) {
        super.Translate(v);
    }

    /**
     * Returns the name of this robot.
     *
     * @return
     */
    @Override
    public String GetName() {
        return "Majestic Robot";
    }

    /**
     * Initiates a draw call of this component in Java.
     *
     * @param cam The camera matrix for the next draw call.
     *            This allows universal transformations according to some universal parent object we call a camera.
     *            This changes how we view objects without changing their position in world space.
     * @throws NullPointerException A null pointer will eventually be thrown if {@code cam} is null and this function is not called with a non-null parameter before Swing's next paint cycle.
     */
    @Override
    public void Draw(Matrix2D cam) {
        super.Draw(cam);
    }

    /**
     * Called before the first update to initialize the game component.
     */
    @Override
    public void Initialize() {
        super.Initialize();
        Initialized = true;
    }

    /**
     * Determines if this game component is initialized.
     *
     * @return Returns true if this game component is initialized and false otherwise.
     */
    @Override
    public boolean Initialized() {
        return Initialized;
    }

    /**
     * Advances a game component by some time {@code delta}.
     *
     * @param delta The amount of time in milliseconds that has passed since the last update.
     */
    @Override
    public void Update(long delta) {
        super.Update(delta);
    }

    /**
     * Called when the game component is removed from the game.
     */
    @Override
    public void Dispose() {
        super.Dispose();
        Disposed = true;
    }

    /**
     * Determines if this game component has been disposed.
     *
     * @return Returns true if this game component has been disposed and false otherwise.
     */
    @Override
    public boolean Disposed() {
        return Disposed;
    }

    protected boolean Disposed;
    protected boolean Initialized;
    protected Vector2i MyPos;//The position of my robot, in terms of the grid
    protected Vector2i NextPos;//The next position of my robot, in terms of the grid
    protected VisionData MyTile;//The tile that my robot is on
    protected VariableSizeGrid<VisionData> MyGrid;//The grid of tiles that my robot has seen
    protected ArrayList<Artifact> ArtifactsGained;//The artifacts that my robot has gained
    protected ArrayList<Pair<Vector2i, Integer>> Artifacts;//The artifacts that my robot is seeing from other robots' visions
    protected Move NextMove;//The next move that my robot will make
    protected int Energy;//The energy of my robot
    protected boolean TakeArtifact;//Whether my robot will take an artifact

    protected Artifact TargetArtifact;//The artifact that my robot is targeting

    protected ArrayList<Vector2i> ArtifactsAccessed;//The artifacts that my robot has accessed
    protected VariableSizeGrid<Boolean> visited; //The grid of tiles that my robot has visited
}
