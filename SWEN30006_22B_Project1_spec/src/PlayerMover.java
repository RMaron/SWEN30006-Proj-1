import java.awt.event.KeyEvent;

public class PlayerMover extends GameMover {

    public PlayerMover(Tetris tetris){
        super(tetris);
    }

    // Handle user input to move block. Arrow left to move left, Arrow right to move right, Arrow up to rotate and
    // Arrow down for going down
    public void moveBlock(TetrisShape currentBlock, String keyEventStr){
        int keyEvent = Integer.parseInt(keyEventStr);

        switch (keyEvent) {
            case KeyEvent.VK_UP:
                if (this.getTetris().getDifficulty() != Tetris.GameMode.MADNESS) {
                    currentBlock.rotate();
                }
                break;
            case KeyEvent.VK_LEFT:
                currentBlock.left();
                break;
            case KeyEvent.VK_RIGHT:
                currentBlock.right();
                break;
            case KeyEvent.VK_DOWN:
                currentBlock.drop();
                break;
            default:
                return;
        }
    }
    public boolean canAutoPlay(){
        return false;
    }
}
