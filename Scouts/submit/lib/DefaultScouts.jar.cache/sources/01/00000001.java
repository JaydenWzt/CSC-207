package scouts.AI.robot;

import gamecore.datastructures.graphs.IGraph;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.gui.gamecomponents.AnimatedComponent;
import gamecore.sprites.Animation;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import scouts.AI.Artifact;
import scouts.AI.IRobot;
import scouts.AI.move.Direction;
import scouts.AI.move.Move;
import scouts.AI.move.VisionData;
import scouts.arena.Area;

/* loaded from: DefaultScouts.jar:scouts/AI/robot/RandomRobot.class */
public class RandomRobot extends AnimatedComponent implements IRobot {
    protected final Random rand;
    protected boolean StandingOnIdol;
    protected Direction ToIdol;
    protected boolean HasIdol;
    protected Artifact IdolType;
    protected boolean StandingOnAltar;
    protected Direction ToAltar;
    protected EnumSet<Artifact> Collected;

    public RandomRobot() throws IOException {
        super(new Animation(new File("student assets/animations/Random.animation")));
        Translate(new Vector2d(0.0d, -3.0d));
        this.rand = new Random();
        this.StandingOnIdol = false;
        this.HasIdol = false;
        this.ToIdol = null;
        this.IdolType = null;
        this.StandingOnAltar = false;
        this.ToAltar = null;
        this.Collected = EnumSet.noneOf(Artifact.class);
    }

    public void Initialize() {
        super.Initialize();
        Play();
    }

    public Move GetNextAction() {
        if (this.StandingOnIdol && !this.HasIdol && !this.Collected.contains(this.IdolType)) {
            return Move.CreateIdolPickup();
        }
        if (!this.HasIdol && this.ToIdol != null && !this.Collected.contains(this.IdolType)) {
            return Move.CreateMovement(this.ToIdol);
        }
        if (this.StandingOnAltar && this.HasIdol) {
            return Move.CreateIdolDrop(this.IdolType);
        }
        if (this.HasIdol && this.ToAltar != null) {
            return Move.CreateMovement(this.ToAltar);
        }
        switch (this.rand.nextInt(4)) {
            case 0:
                return Move.CreateMovement(Direction.UP);
            case 1:
                return Move.CreateMovement(Direction.DOWN);
            case 2:
                return Move.CreateMovement(Direction.LEFT);
            case 3:
                return Move.CreateMovement(Direction.RIGHT);
            default:
                return Move.CreateNoOP();
        }
    }

    public void EnergyUpdate(int e) {
    }

    public void VisionUpdate(Iterable<IGraph<VisionData, Direction>> vision) {
        IGraph<VisionData, Direction> g = vision.iterator().next();
        if (!this.HasIdol) {
            this.StandingOnIdol = ((VisionData) g.GetVertex(0)).IsIdol();
            if (this.StandingOnIdol) {
                this.IdolType = Area.ToArtifact(((VisionData) g.GetVertex(0)).Environment);
            }
            this.ToIdol = null;
            Iterator it = g.Neighbors(0).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Integer n = (Integer) it.next();
                if (((VisionData) g.GetVertex(n.intValue())).IsIdol()) {
                    this.ToIdol = (Direction) g.GetEdge(0, n.intValue());
                    this.IdolType = Area.ToArtifact(((VisionData) g.GetVertex(n.intValue())).Environment);
                    break;
                }
            }
        } else {
            this.StandingOnIdol = false;
            this.ToIdol = null;
        }
        if (this.HasIdol) {
            this.StandingOnAltar = ((VisionData) g.GetVertex(0)).IsAltar();
            this.ToAltar = null;
            for (Integer n2 : g.Neighbors(0)) {
                if (((VisionData) g.GetVertex(n2.intValue())).IsAltar()) {
                    this.ToAltar = (Direction) g.GetEdge(0, n2.intValue());
                    return;
                }
            }
            return;
        }
        this.StandingOnAltar = false;
        this.ToAltar = null;
    }

    public void ReportSuccess(boolean success) {
        if (success) {
            if (this.StandingOnIdol) {
                this.HasIdol = true;
            } else if (this.StandingOnAltar) {
                this.HasIdol = false;
                this.Collected.add(this.IdolType);
                this.IdolType = null;
            }
        }
    }

    public boolean StruggleForArtifact(Artifact a, int cost, boolean offense) {
        return false;
    }

    public void ReportTheft(Artifact a, boolean gained) {
        if (!gained) {
            this.HasIdol = false;
            this.IdolType = null;
        }
    }

    public void Teleported() {
    }

    public String GetName() {
        return "Rando";
    }
}