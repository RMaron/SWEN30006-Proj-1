//GameMover.java

// Raziel Maron, Chi Pang Kuok, Sandeepa Andra Hennadige
// Group 4

public abstract class GameMover {

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
