// AutoMover.java

// Raziel Maron, Chi Pang Kuok, Sandeepa Andra Hennadige
// Group 4


public class AutoMover extends GameMover{

    private final String[] autoBlockMoves;
    private int autoBlockIndex = 0;
    private int shapeIndex=0;

    public AutoMover(Tetris tetris){
        super(tetris);
        this.autoBlockMoves = tetris.getBlockActions();
    }

    // parse current move for shape taken from properties file
    public String currentAutoMove(){
        String curMove;
        if (autoBlockIndex < autoBlockMoves[shapeIndex].length()) {
            curMove = autoBlockMoves[shapeIndex].substring(autoBlockIndex, autoBlockIndex + 1);
            // iterate to next input for future
            autoBlockIndex++;
        } else {
            curMove = "";
        }
        return curMove;
    }

    public String getCurrentBlockMove(){
        return autoBlockMoves[shapeIndex].substring(autoBlockIndex, autoBlockIndex + 1);
    }

    // Based on the input in the properties file, the block can move automatically
    public void moveBlock(TetrisShape currentBlock, String keyEventStr) {
        switch (keyEventStr) {
            case "L":
                currentBlock.left();
                break;
            case "R":
                currentBlock.right();
                break;
            case "T":
                // if tetris madness, cannot rotate
                if (this.getTetris().getDifficulty() != Tetris.GameMode.MADNESS) {
                    currentBlock.rotate();
                }
                break;
            case "D":
                currentBlock.drop();
                break;
        }

    }

    public boolean canAutoPlay() {
        if (autoBlockMoves[shapeIndex] != null && !autoBlockMoves[shapeIndex].equals("")) {
            if (autoBlockMoves[shapeIndex].length() > autoBlockIndex) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // iterate to next string of inputs for a new shape
    public void iterShapeIndex(){
        this.autoBlockIndex = 0;
        this.shapeIndex++;
    }

}
