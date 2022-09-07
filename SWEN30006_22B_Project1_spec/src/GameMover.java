

import javax.swing.*;

public abstract class GameMover extends JFrame  {

    private final Tetris tetris;

    GameMover(Tetris tetris){
        this.tetris = tetris;
    }


    public abstract void moveBlock(TetrisShape ts, String move);

    public abstract boolean canAutoPlay();

    public Tetris getTetris(){
        return this.tetris;
    }



}
