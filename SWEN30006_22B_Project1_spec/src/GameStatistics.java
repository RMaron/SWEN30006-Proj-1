// GameStatistics.java

// Raziel Maron, Chi Pang Kuok, Sandeepa Andra Hennadige
// Group 4


import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileWriter;
import java.util.Objects;

public class GameStatistics extends JFrame {
    private Tetris tetris;
    private int score = 0;
    private ArrayList<Integer> scoreList = new ArrayList<>();
    private int[] shapeCnt = new int[10];
    private ArrayList<int[]> shapeCntList = new ArrayList<>();
    private int round = 1;

    private int totalScore = 0;

    GameStatistics(Tetris tetris){
        this.tetris = tetris;
    }

    public int getScore() {
        return score;
    }

    public int getRound() {
        return round;
    }

    public void addShapeCnt(int index){
        shapeCnt[index] +=1;
    }

    public void addScore(int i){
        this.score += i;
    }

    // Update each round once complete, Write game stats to file,
    //  and prepare for next round
    public void roundUpdate() throws IOException{
        this.totalScore += this.score;
        this.shapeCntList.add(Arrays.copyOf(this.shapeCnt, this.shapeCnt.length));
        this.scoreList.add(this.score);
        writeFile();
        Arrays.fill(this.shapeCnt, 0);
        this.round += 1;
        this.score = 0;
    }


    // write game stats to file
    public void writeFile() throws IOException {

        FileWriter fw = new  FileWriter("statistics.txt");

        fw.write("Difficulty: " + (this.tetris.getDifficultyStr()).substring(0,1).toUpperCase() + (this.tetris.getDifficultyStr()).substring(1) + "\n");

        fw.write("Average score per round: " + (this.totalScore/(double)this.round));



        for (int i = 0; i < this.round; i++) {
            fw.write("\n" + "------------------------------------------");
            fw.write("\n" + "Round #" + (i+1));
            fw.write("\n" + "Score: " + this.scoreList.get(i));
            int difficultyShapes = Objects.equals(this.tetris.getDifficultyStr(), "easy") ? 7 : 10;
            for (int j = 0; j < difficultyShapes; j++) {
                fw.write("\n" + Tetris.Shape.findShape(j).getId() + ": " + this.shapeCntList.get(i)[Tetris.Shape.findShape(j).ordinal()]);
            }
        }
        fw.close();
    }

}
