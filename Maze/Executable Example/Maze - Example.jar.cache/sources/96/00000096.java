package maze.tile.tiles;

import gamecore.GameEngine;
import gamecore.datastructures.vectors.Vector2d;
import gamecore.gui.gamecomponents.AnimatedComponent;
import gamecore.input.InputManager;
import gamecore.observe.IObserver;
import gamecore.sprites.Animation;
import gamecore.time.TimePartition;
import java.io.File;
import java.io.IOException;
import maze.Player;
import maze.tile.MazeTile;

/* loaded from: Maze - Example.jar:maze/tile/tiles/TeleportTile.class */
public class TeleportTile extends MazeTile implements IObserver<TimePartition.TimeEvent> {
    public final Vector2d Displacement;
    protected AnimatedComponent Teleporter;
    protected AnimatedComponent TeleOut;
    protected AnimatedComponent TeleIn;
    protected boolean Teleporting;
    protected boolean Teleporting2;
    protected boolean InGame;

    public TeleportTile(Vector2d displacement) throws IOException {
        super(MazeTile.TileID.PLAIN, false, true);
        this.Teleporter = new AnimatedComponent(new Animation(new File("assets/animations/Active Teleport.animation")));
        this.Teleporter.SetParent(this);
        Animation a = new Animation(new File("assets/animations/Portal.animation"));
        a.Subscribe(this);
        this.TeleOut = new AnimatedComponent(a);
        this.TeleOut.SetParent(this);
        this.TeleOut.Translate(-8.0d, 0.0d);
        Animation a2 = new Animation(new File("assets/animations/Reverse Portal.animation"));
        a2.Subscribe(this);
        this.TeleIn = new AnimatedComponent(a2);
        this.TeleIn.SetParent(this);
        this.TeleIn.Translate(displacement);
        this.TeleIn.Translate(-8.0d, 0.0d);
        this.Displacement = displacement;
        this.Teleporting = false;
        this.Teleporting2 = false;
        this.InGame = false;
    }

    @Override // gamecore.gui.gamecomponents.ImageComponent, gamecore.IUpdatable
    public void Update(long delta) {
        Player p = (Player) GameEngine.Game().GetService(Player.class);
        if (GetBoundary().Intersection(p.GetBoundary()).Area() > 0.5d * p.GetBoundary().Area() && ((InputManager) GameEngine.Game().GetService(InputManager.class)).GracelessInputSatisfied("A") && !this.Teleporting && !this.Teleporting2) {
            p.Freeze();
            GameEngine.Game().AddComponent(this.TeleOut, 0);
            this.TeleOut.Play();
            this.Teleporting = true;
        }
    }

    @Override // gamecore.observe.IObserver
    public void OnNext(TimePartition.TimeEvent event) {
        if (event.IsEndOfTime()) {
            if (this.Teleporting && !this.Teleporting2) {
                ((Player) GameEngine.Game().GetService(Player.class)).Translate(this.Displacement);
                GameEngine.Game().RemoveComponent(this.TeleOut);
                this.TeleOut.Stop();
                GameEngine.Game().AddComponent(this.TeleIn, 0);
                this.TeleIn.Play();
                this.Teleporting = false;
                this.Teleporting2 = true;
            } else if (this.Teleporting2) {
                ((Player) GameEngine.Game().GetService(Player.class)).Unfreeze();
                GameEngine.Game().RemoveComponent(this.TeleIn);
                this.TeleIn.Stop();
                this.Teleporting2 = false;
            }
        }
    }

    @Override // gamecore.observe.IObserver
    public void OnError(Exception e) {
    }

    @Override // gamecore.observe.IObserver
    public void OnCompleted() {
    }

    @Override // gamecore.gui.gamecomponents.ImageComponent, gamecore.IUpdatable
    public void Dispose() {
        if (!this.Teleporter.Disposed()) {
            this.Teleporter.Dispose();
        }
        if (!this.TeleOut.Disposed()) {
            this.TeleOut.Dispose();
        }
        if (!this.TeleIn.Disposed()) {
            this.TeleIn.Dispose();
        }
        super.Dispose();
    }

    @Override // maze.tile.MazeTile, gamecore.IUpdatable
    public void OnAdd() {
        if (!this.InGame) {
            GameEngine.Game().AddComponent(this.Teleporter, GameEngine.Game().IndexOfComponent(this));
        }
        this.InGame = true;
    }

    @Override // maze.tile.MazeTile, gamecore.IUpdatable
    public void OnRemove() {
        if (this.InGame) {
            this.Teleporting = false;
            this.Teleporting2 = false;
            GameEngine.Game().RemoveComponent(this.TeleIn);
            GameEngine.Game().RemoveComponent(this.TeleOut);
            GameEngine.Game().RemoveComponent(this.Teleporter);
        }
        this.InGame = false;
    }
}