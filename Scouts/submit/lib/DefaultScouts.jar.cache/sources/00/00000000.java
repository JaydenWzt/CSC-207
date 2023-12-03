package scouts.AI.robot;

import gamecore.datastructures.graphs.IGraph;
import gamecore.gui.gamecomponents.AnimatedComponent;
import gamecore.sprites.Animation;
import java.io.File;
import java.io.IOException;
import scouts.AI.Artifact;
import scouts.AI.IRobot;
import scouts.AI.move.Direction;
import scouts.AI.move.Move;
import scouts.AI.move.VisionData;

/* loaded from: DefaultScouts.jar:scouts/AI/robot/MyRobot.class */
public class MyRobot extends AnimatedComponent implements IRobot {
    public MyRobot() throws IOException {
        super(new Animation(new File("student assets/animations/Default.animation")));
    }

    public Move GetNextAction() {
        return Move.CreateNoOP();
    }

    public void EnergyUpdate(int e) {
    }

    public void VisionUpdate(Iterable<IGraph<VisionData, Direction>> vision) {
    }

    public void ReportSuccess(boolean success) {
    }

    public boolean StruggleForArtifact(Artifact a, int cost, boolean offense) {
        return false;
    }

    public void ReportTheft(Artifact a, boolean gained) {
    }

    public void Teleported() {
    }

    public String GetName() {
        return "A Statue";
    }
}