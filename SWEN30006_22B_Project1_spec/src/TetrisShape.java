// TetrisShape.java

// Raziel Maron, Chi Pang Kuok, Sandeepa Andra Hennadige
// Group 4


import ch.aplu.jgamegrid.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class TetrisShape extends Actor{

    private final Tetris.Shape shape;

    private final Tetris tetris;
    private boolean isStarting=true;
    private int rotId = 0;
    private int nb=0;



    private final ArrayList<TetroBlock> blocks = new ArrayList<TetroBlock>();
    private TetrisShape nextTetrisBlock;

    TetrisShape(Tetris tetris, Tetris.Shape shape){
        this.tetris = tetris;
        this.shape = shape;
    }




    public String toString() {
        return "For testing, do not change: Block: " + shape.getId() + ". Location: " + blocks + ". Rotation: " + rotId;
    }

    public void act()
    {
        if (isStarting) {
            for (TetroBlock a : blocks) {
                Location loc =
                        new Location(getX() + a.getRelLoc(0).x, getY() + a.getRelLoc(0).y);
                gameGrid.addActor(a, loc);
            }
            isStarting = false;
            nb = 0;
        } if (nb >= blocks.size() && (this.tetris.getGameMover().canAutoPlay())) {
            this.tetris.getGameMover().moveBlock(this, ((AutoMover)this.tetris.getGameMover()).currentAutoMove());
        } else
        {
            setDirection(90);
            if (nb == 1)
                nextTetrisBlock = tetris.createRandomTetrisBlock();
            if (!advance())
            {
                if (nb == 0)  // Game is over when tetrisBlock cannot fall down
                {
                    try {
                        tetris.gameOver();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    setActEnabled(false);
                    gameGrid.addActor(nextTetrisBlock, new Location(6, 0));
                    tetris.setCurrentTetrisBlock(nextTetrisBlock);
                }
            }
            nb++;
        }
    }



    void display(GameGrid gg, Location location)
    {
        for (TetroBlock a : blocks)
        {
            Location loc =
                    new Location(location.x + a.getRelLoc(0).x, location.y + a.getRelLoc(0).y);
            gg.addActor(a, loc);
        }
    }

    // Actual actions on the block: move the block left, right, drop and rotate the block
    void left()
    {
        if (isStarting)
            return;
        setDirection(180);
        advance();
    }

    void right()
    {
        if (isStarting)
            return;
        setDirection(0);
        advance();
    }

    void rotate()
    {
        if (isStarting)
            return;

        int oldRotId = rotId; // Save it
        rotId++;
        if (rotId == 4)
            rotId = 0;

        if (canRotate(rotId))
        {
            for (TetroBlock a : blocks)
            {
                Location loc = new Location(getX() + a.getRelLoc(rotId).x, getY() + a.getRelLoc(rotId).y);
                a.setLocation(loc);
            }
        }
        else
            rotId = oldRotId;  // Restore

    }

    private boolean canRotate(int rotId)
    {
        // Check for every rotated tetroBlock within the tetrisBlock
        for (TetroBlock a : blocks)
        {
            Location loc =
                    new Location(getX() + a.getRelLoc(rotId).x, getY() + a.getRelLoc(rotId).y);
            if (!gameGrid.isInGrid(loc))  // outside grid->not permitted
                return false;
            TetroBlock block =
                    (TetroBlock)(gameGrid.getOneActorAt(loc, TetroBlock.class));
            if (blocks.contains(block))  // in same tetrisBlock->skip
                break;
            if (block != null)  // Another tetroBlock->not permitted
                return false;
        }
        return true;
    }

    void drop()
    {
        if (isStarting)
            return;
        setSlowDown(0);
    }

    // Logic to check if the block has been removed (as winning a line) or drop to the bottom
    public boolean advance() {
        boolean canMove = false;
        for (TetroBlock a : blocks) {
            if (!a.isRemoved()) {
                canMove = true;
            }
        }
        for (TetroBlock a : blocks) {
            if (a.isRemoved())
                continue;
            if (!gameGrid.isInGrid(a.getNextMoveLocation())) {
                canMove = false;
                break;
            }
        }
        for (TetroBlock a : blocks)
        {
            if (a.isRemoved())
                continue;
            TetroBlock block =
                    (TetroBlock)(gameGrid.getOneActorAt(a.getNextMoveLocation(),
                            TetroBlock.class));
            if (block != null && !blocks.contains(block))
            {
                canMove = false;
                break;
            }
        }

        if (canMove)
        {
            move();
            return true;
        }
        return false;
    }


    // Override Actor.setDirection()
    @Override
    public void setDirection(double dir)
    {
        super.setDirection(dir);
        for (TetroBlock a : blocks)
            a.setDirection(dir);
    }

    // Override Actor.move()
    @Override
    public void move()
    {
        if (isRemoved())
            return;
        super.move();
        for (TetroBlock a : blocks)
        {
            if (a.isRemoved())
                break;
            a.move();
        }
    }

    // Override Actor.removeSelf()
    @Override
    public void removeSelf()
    {
        super.removeSelf();
        for (TetroBlock a : blocks)
            a.removeSelf();
    }

    public Tetris.Shape getShape(){
        return this.shape;
    }

    public Tetris getTetris(){
        return this.tetris;
    }

    public void addBlocks(TetroBlock tb){
        this.blocks.add(tb);
    }

    public boolean getIsStarting(){
        return this.isStarting;
    }

}
