// Tetris.java

// Raziel Maron, Chi Pang Kuok, Sandeepa Andra Hennadige
// Group 4

import ch.aplu.jgamegrid.*;



import java.security.Key;
import java.io.IOException;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Tetris extends JFrame implements GGActListener {

    private TetrisShape currentBlock = null;  // Currently active block
    private TetrisShape blockPreview = null;   // block in preview window
    private static final int baseSlowDown = 5;
    private int slowDown;
    private Random random = new Random(0);
    private GameMode difficulty;
    private GameMover gameMover;

    private final TetrisGameCallback gameCallback;

    private boolean isAuto = false;
    private final GameStatistics gameStatistics;


    private int seed = 30006;
    // For testing mode, the block will be moved automatically based on the blockActions.
    // L is for Left, R is for Right, T is for turning (rotating), and D for down
    private String [] blockActions = new String[10];


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

        // get number of Shape types for each difficulty
        public int getNumPieces(){
            switch (this){
                case MEDIUM:
                case MADNESS:
                    return 10;
                default:
                    return 7;
            }

        }
    }


    // Enum of shapes, ordinal = shape ID
    // *** Maintain current order ***
    public enum Shape{
        I_SHAPE ("I"),
        J_SHAPE ("J"),
        L_SHAPE ("L"),
        O_SHAPE ("O"),
        S_SHAPE ("S"),
        T_SHAPE ("T"),
        Z_SHAPE ("Z"),
        Plus_SHAPE ("+"),
        P_SHAPE ("P"),
        Q_SHAPE ("Q");


        private final String id;

        Shape( String id){
            this.id = id;
        }

        // Find shape from given shape ID
        public static Shape findShape(int i){
            for (Shape s : values()){
                if (s.ordinal() == i){
                    return s;
                }
            }
            return null;
        }

        public String getId(){
            return this.id;
        }

    }


    public Tetris(TetrisGameCallback gameCallback, Properties properties) {
        // Initialise value
        initWithProperties(properties);
        this.gameCallback = gameCallback;


        // Set up the UI components. No need to modify the UI Components
        tetrisComponents = new TetrisComponents();
        tetrisComponents.initComponents(this);
        gameGrid1.addActListener(this);
        gameGrid1.setSimulationPeriod(getSimulationTime());

        this.gameStatistics = new GameStatistics(this);
        if (isAuto) {
            this.gameMover = new AutoMover(this);
        } else {
            this.gameMover = new PlayerMover(this);
        }


        // Add the first block to start
        currentBlock = createRandomTetrisBlock();
        gameGrid1.addActor(currentBlock, new Location(6, 0));
        gameGrid1.doRun();

        // Do not lose keyboard focus when clicking this window
        gameGrid2.setFocusable(false);
        setTitle("SWEN30006 Tetris Madness");

        showScore(gameStatistics.getScore());
        slowDown = baseSlowDown;

    }

    // create a block and assign to a preview mode
    TetrisShape createRandomTetrisBlock() {
        if (blockPreview != null)
            blockPreview.removeSelf();


        TetrisShape t = null;
        TetrisShape preview = null;

        // Generates random number based on number of pieces based on difficulty
        int rnd = random.nextInt(difficulty.getNumPieces());

        // Spawn random piece + preview
        Shape s = Shape.findShape(rnd);
        switch (s) {
            case I_SHAPE:
                t = new I(this);
                preview = new I(this);

                break;
            case J_SHAPE:
                t = new J(this);
                preview = new J(this);

                break;
            case L_SHAPE:
                t = new L(this);
                preview = new L(this);

                break;
            case O_SHAPE:
                t = new O(this);
                preview = new O(this);

                break;
            case S_SHAPE:
                t = new S(this);
                preview = new S(this);

                break;
            case T_SHAPE:
                t = new T(this);
                preview = new T(this);

                break;
            case Z_SHAPE:
                t = new Z(this);
                preview = new Z(this);

                break;
            case P_SHAPE:
                t = new P(this);
                preview = new P(this);

                break;
            case Q_SHAPE:
                t = new Q(this);
                preview = new Q(this);

                break;
            case Plus_SHAPE:
                t = new Plus(this);
                preview = new Plus(this);

                break;
        }

        // Show preview tetrisBlock
        preview.display(gameGrid2, new Location(2, 1));
        blockPreview = preview;
        gameStatistics.addShapeCnt(preview.getShape().ordinal());

        // Set speed with respect to difficulty
        switch (difficulty){
            case MEDIUM:
                // set speed 20% faster than easy
                t.setSlowDown((int)(0.8*slowDown));
                break;
            case MADNESS:
                // set random speed between slow down and 1/2 slow down
                int rand = slowDown - random.nextInt(slowDown/2+1);

                t.setSlowDown(rand);
                break;
            default:
                t.setSlowDown(slowDown);
        }
        return t;
    }


    void setCurrentTetrisBlock(TetrisShape t) {
        gameCallback.changeOfBlock(currentBlock);
        currentBlock = t;
        if (isAuto) {
            ((AutoMover) gameMover).iterShapeIndex();
        }
    }


    public void act(){
        removeFilledLine();
        gameMover.moveBlock((TetrisShape) currentBlock, ((Integer)gameGrid1.getKeyCode()).toString());
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
                int prevScore = gameStatistics.getScore();
                gameStatistics.addScore(1);
                gameCallback.changeOfScore(gameStatistics.getScore());
                showScore(gameStatistics.getScore());
                // Set speed of tetrisBlocks
                // include redundancy in score checking in case adding scores>1 is possible in future implementation
                if (prevScore <=10 && gameStatistics.getScore() > 10) {
                    slowDown -= 1;
                }
                if (prevScore <=20 && gameStatistics.getScore() > 20) {
                    slowDown -= 1;
                }
                if (prevScore <=30 && gameStatistics.getScore() > 30) {
                    slowDown -= 1;
                }
                if (prevScore <=40 && gameStatistics.getScore() > 40) {
                    slowDown -= 1;
                }
                if (prevScore <=50 && gameStatistics.getScore() > 50) {
                    slowDown -= 1;
                }
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

    public void gameOver() throws IOException {
        gameGrid1.addActor(new Actor("sprites/gameover.gif"), new Location(5, 5));
        gameGrid1.doPause();

        gameStatistics.roundUpdate();

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
        currentBlock = createRandomTetrisBlock();
        if (isAuto) {
            gameMover = new AutoMover(this);
        } else {
            gameMover = new PlayerMover(this);
        }
        gameGrid1.addActor(currentBlock, new Location(6, 0));
        gameGrid1.doRun();
        gameGrid1.requestFocus();
        showScore(gameStatistics.getScore());
        slowDown = baseSlowDown;

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


    // basic getters

    public String getDifficultyStr(){
        return this.difficulty.str;
    }

    public GameMode getDifficulty(){
        return this.difficulty;
    }

    public GameMover getGameMover(){
        return this.gameMover;
    }

    public String[] getBlockActions(){
        return this.blockActions;
    }

}