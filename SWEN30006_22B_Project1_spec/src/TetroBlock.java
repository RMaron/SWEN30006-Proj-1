// TetroBlock.java

// Raziel Maron, Chi Pang Kuok, Sandeepa Andra Hennadige
// Group 4

import ch.aplu.jgamegrid.*;

public class TetroBlock extends Actor
{
  private Location[] relLoc = new Location[4];
  // Display a single square in a Tetris piece
  public TetroBlock(int blockId, Location[] relLoc)
  {
    // loop over sprites for tetris madness
    super("sprites/tetroblock" + (blockId%7)+ ".gif");
    this.relLoc = relLoc.clone();
  }

  public Location getRelLoc(int rotId)
  {
    return relLoc[rotId];
  }

  /**
   * Do not change this method. For testing and marking purpose
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (this.gameGrid != null) {
      sb.append(this.getLocation());
    }
    return sb.toString();
  }
}
