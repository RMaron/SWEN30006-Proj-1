// Tetris.java

import ch.aplu.jgamegrid.*;

import java.security.Key;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class Tetris extends JFrame implements GGActListener {

    private Actor currentBlock = null;  // Currently active block
    private Actor blockPreview = null;   // block in preview window
    private int score = 0;
    private int baseSlowDown = 5;
    private int slowDown;
    private Random random = new Random(0);
    private GameMode difficulty;

    private TetrisGameCallback gameCallback;

    private boolean isAuto = false;

    private int seed = 30006;
    // For testing mode, the block will be moved automatically based on the blockActions.
    // L is for Left, R is for Right, T is for turning (rotating), and D for down
    private String [] blockActions = new String[10];
    private int blockActionIndex = 0;

    // Initialise object
    private void initWithProperties(Properties properties) {
        this.seed = Integer.parseInt(properties.getProperty("seed", "30006"));
        this.random = new Random(seed);
        this.isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        String blockActionProperty = properties.getProperty("autoBlockActions", "");
        this.blockActions = blockActionProperty.split(",");
        // Parse difficulty from property file
        this.difficulty = GameMode.findGameMode(properties.getProperty("difficulty"));
    }

    // Enum of Game modes
    public enum GameMode{
        EASY ("easy"),
        MEDIUM ("medium"),
        MADNESS ("madness");

        private final String str;

        GameMode(String str){
            this.str = str;
        }

        //Find enum from given string
        public static GameMode findGameMode(String str){
            for (GameMode gm : values()){
                if (gm.str.equals(str)){
                    return gm;
                }
            }
            return null;
        }
    }


    // Enum of shapes, value = shape ID
    public enum Shape{
        I_SHAPE (0),
        J_SHAPE (1),
        L_SHAPE (2),
        O_SHAPE (3),
        S_SHAPE (4),
        T_SHAPE (5),
        Z_SHAPE (6),
        P_SHAPE (7),
        Q_SHAPE (8),
        Plus_SHAPE (9);

        private final int id;

        Shape(int id){
            this.id = id;
        }

        // Find shape from given shape ID
        public static Shape findShape(int i){
            for (Shape s : values()){
                if (s.id == i){
                    return s;
                }
            }
            return null;
        }
    }


    public Tetris(TetrisGameCallback gameCallback, Properties properties) {
        // Initialise value
        initWithProperties(properties);
        this.gameCallback = gameCallback;
        blockActionIndex = 0;

        // Set up the UI components. No need to modify the UI Components
        tetrisComponents = new TetrisComponents();
        tetrisComponents.initComponents(this);
        gameGrid1.addActListener(this);
        gameGrid1.setSimulationPeriod(getSimulationTime());

        // Add the first block to start
        currentBlock = createRandomTetrisBlock();
        gameGrid1.addActor(currentBlock, new Location(6, 0));
        gameGrid1.doRun();

        // Do not lose keyboard focus when clicking this window
        gameGrid2.setFocusable(false);
        setTitle("SWEN30006 Tetris Madness");
        score = 0;
        showScore(score);
        switch (difficulty){
            case MEDIUM:
                slowDown = (int)(0.8*baseSlowDown);
                break;
            default:
                slowDown = baseSlowDown;
        }
    }

    // create a block and assign to a preview mode
    Actor createRandomTetrisBlock() {
        if (blockPreview != null)
            blockPreview.removeSelf();

        // If the game is in auto test mode, then the block will be moved according to the blockActions
        String currentBlockMove = "";
        if (blockActions.length > blockActionIndex) {
            currentBlockMove = blockActions[blockActionIndex];
        }

        blockActionIndex++;
        Actor t = null;
        int rnd;
        // Handles difficulty scaling for number of pieces
        switch (difficulty) {
            case MEDIUM:
            case MADNESS:
                rnd = random.nextInt(10);
                break;
            default:
                rnd = random.nextInt(7);
                break;
        }
        // Spawn random piece
        Shape s = Shape.findShape(rnd);
        switch (s) {
            case I_SHAPE:
                t = new I(this);
                if (isAuto) {
                    ((I) t).setAutoBlockMove(currentBlockMove);
                }

                I previewI = new I(this);
                previewI.display(gameGrid2, new Location(2, 1));
                blockPreview = previewI;
                break;
            case J_SHAPE:
                t = new J(this);
                if (isAuto) {
                    ((J) t).setAutoBlockMove(currentBlockMove);
                }
                J previewJ = new J(this);
                previewJ.display(gameGrid2, new Location(2, 1));
                blockPreview = previewJ;
                break;
            case L_SHAPE:
                t = new L(this);
                if (isAuto) {
                    ((L) t).setAutoBlockMove(currentBlockMove);
                }
                L previewL = new L(this);
                previewL.display(gameGrid2, new Location(2, 1));
                blockPreview = previewL;
                break;
            case O_SHAPE:
                t = new O(this);
                if (isAuto) {
                    ((O) t).setAutoBlockMove(currentBlockMove);
                }
                O previewO = new O(this);
                previewO.display(gameGrid2, new Location(2, 1));
                blockPreview = previewO;
                break;
            case S_SHAPE:
                t = new S(this);
                if (isAuto) {
                    ((S) t).setAutoBlockMove(currentBlockMove);
                }
                S previewS = new S(this);
                previewS.display(gameGrid2, new Location(2, 1));
                blockPreview = previewS;
                break;
            case T_SHAPE:
                t = new T(this);
                if (isAuto) {
                    ((T) t).setAutoBlockMove(currentBlockMove);
                }
                T previewT = new T(this);
                previewT.display(gameGrid2, new Location(2, 1));
                blockPreview = previewT;
                break;
            case Z_SHAPE:
                t = new Z(this);
                if (isAuto) {
                    ((Z) t).setAutoBlockMove(currentBlockMove);
                }
                Z previewZ = new Z(this);
                previewZ.display(gameGrid2, new Location(2, 1));
                blockPreview = previewZ;
                break;
            case P_SHAPE:
                t = new P(this);
                if (isAuto) {
                    ((P) t).setAutoBlockMove(currentBlockMove);
                }
                P previewP = new P(this);
                previewP.display(gameGrid2, new Location(2, 1));
                blockPreview = previewP;
                break;
            case Q_SHAPE:
                t = new Q(this);
                if (isAuto) {
                    ((Q) t).setAutoBlockMove(currentBlockMove);
                }
                Q previewQ = new Q(this);
                previewQ.display(gameGrid2, new Location(2, 1));
                blockPreview = previewQ;
                break;
            case Plus_SHAPE:
                t = new Plus(this);
                if (isAuto) {
                    ((Plus) t).setAutoBlockMove(currentBlockMove);
                }
                Plus previewPlus = new Plus(this);
                previewPlus.display(gameGrid2, new Location(2, 1));
                blockPreview = previewPlus;
                break;
        }
        // Show preview tetrisBlock

        // Set speed with respect to difficulty
        switch (difficulty){
            case MEDIUM:
                t.setSlowDown((int)(0.8*slowDown));
                break;
            case MADNESS:
                int rand = slowDown - random.nextInt(slowDown+1);
                t.setSlowDown(rand);
                break;
            default:
                t.setSlowDown(slowDown);
        }
        return t;
    }





    void setCurrentTetrisBlock(Actor t) {
        gameCallback.changeOfBlock(currentBlock);
        currentBlock = t;
    }

    // Handle user input to move block. Arrow left to move left, Arrow right to move right, Arrow up to rotate and
    // Arrow down for going down
    private void moveBlock(int keyEvent) {
        switch (keyEvent) {
                case KeyEvent.VK_UP:
                    if (difficulty != GameMode.MADNESS) {
                        ((TetrisShape) currentBlock).rotate();
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    ((TetrisShape) currentBlock).left();
                    break;
                case KeyEvent.VK_RIGHT:
                    ((TetrisShape) currentBlock).right();
                    break;
                case KeyEvent.VK_DOWN:
                    ((TetrisShape) currentBlock).drop();
                    break;
                default:
                    return;
        }
    }


    public void act(){
        removeFilledLine();
        moveBlock(gameGrid1.getKeyCode());
    }
    private void removeFilledLine() {
        for (int y = 0; y < gameGrid1.nbVertCells; y++) {
            boolean isLineComplete = true;
            TetroBlock[] blocks = new TetroBlock[gameGrid1.nbHorzCells];   // One line
            // Calculate if a line is complete
            for (int x = 0; x < gameGrid1.nbHorzCells; x++) {
                blocks[x] =
                        (TetroBlock) gameGrid1.getOneActorAt(new Location(x, y), TetroBlock.class);
                if (blocks[x] == null) {
                    isLineComplete = false;
                    break;
                }
            }
            if (isLineComplete) {
                // If a line is complete, we remove the component block of the shape that belongs to that line
                for (int x = 0; x < gameGrid1.nbHorzCells; x++)
                    gameGrid1.removeActor(blocks[x]);
                ArrayList<Actor> allBlocks = gameGrid1.getActors(TetroBlock.class);
                for (Actor a : allBlocks) {
                    int z = a.getY();
                    if (z < y)
                        a.setY(z + 1);
                }
                gameGrid1.refresh();
                score++;
                gameCallback.changeOfScore(score);
                showScore(score);
                // Set speed of tetrisBlocks
                if (score == 11)
                    slowDown-= 1;
                if (score == 21)
                    slowDown-= 1;
                if (score == 31)
                    slowDown-= 1;
                if (score == 41)
                    slowDown -= 1;
                if (score == 51)
                    slowDown -= 1;
                if (slowDown < 0){
                    slowDown = 0;
                }
            }
        }
    }

    // Show Score and Game Over

    private void showScore(final int score) {
        EventQueue.invokeLater(() -> scoreText.setText(score + " points"));

    }

    void gameOver() {
        gameGrid1.addActor(new Actor("sprites/gameover.gif"), new Location(5, 5));
        gameGrid1.doPause();
        if (isAuto) {
            System.exit(0);
        }
    }

    // Start a new game
    public void startBtnActionPerformed(java.awt.event.ActionEvent evt)
    {
        gameGrid1.doPause();
        gameGrid1.removeAllActors();
        gameGrid2.removeAllActors();
        gameGrid1.refresh();
        gameGrid2.refresh();
        gameGrid2.delay(getDelayTime());
        blockActionIndex = 0;
        currentBlock = createRandomTetrisBlock();
        gameGrid1.addActor(currentBlock, new Location(6, 0));
        gameGrid1.doRun();
        gameGrid1.requestFocus();
        score = 0;
        showScore(score);
        slowDown = 5;
    }

    // Different speed for manual and auto mode
    private int getSimulationTime() {
        if (isAuto) {
            return 10;
        } else {
            return 100;
        }
    }

    private int getDelayTime() {
        if (isAuto) {
            return 200;
        } else {
            return 2000;
        }
    }

    // AUTO GENERATED - do not modify//GEN-BEGIN:variables
    public ch.aplu.jgamegrid.GameGrid gameGrid1;
    public ch.aplu.jgamegrid.GameGrid gameGrid2;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea jTextArea1;
    public javax.swing.JTextField scoreText;
    public javax.swing.JButton startBtn;
    private TetrisComponents tetrisComponents;
    // End of variables declaration//GEN-END:variables

}
