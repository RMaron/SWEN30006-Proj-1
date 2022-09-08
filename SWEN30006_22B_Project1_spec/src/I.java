// I.java

// Raziel Maron, Chi Pang Kuok, Sandeepa Andra Hennadige
// Group 4

import ch.aplu.jgamegrid.*;

import java.util.ArrayList;


class I extends TetrisShape {

  private static final Tetris.Shape s = Tetris.Shape.I_SHAPE;


  I(Tetris tetris) {
    super(tetris, s);

    // 4 block shape, 4 rotation directions
    Location[][] r = new Location[4][4];

    // rotId 0
    r[0][0] = new Location(new Location(-1, 0));
    r[1][0] = new Location(new Location(0, 0));
    r[2][0] = new Location(new Location(1, 0));
    r[3][0] = new Location(new Location(2, 0));
    // rotId 1
    r[0][1] = new Location(new Location(0, -1));
    r[1][1] = new Location(new Location(0, 0));
    r[2][1] = new Location(new Location(0, 1));
    r[3][1] = new Location(new Location(0, 2));
    // rotId 2
    r[0][2] = new Location(new Location(-1, 0));
    r[1][2] = new Location(new Location(0, 0));
    r[2][2] = new Location(new Location(1, 0));
    r[3][2] = new Location(new Location(2, 0));
    // rotId 3
    r[0][3] = new Location(new Location(0, -1));
    r[1][3] = new Location(new Location(0, 0));
    r[2][3] = new Location(new Location(0, 1));
    r[3][3] = new Location(new Location(0, 2));

    for (int i = 0; i < r.length; i++)
      this.addBlocks(new TetroBlock(this.getShape().ordinal(), r[i]));
  }

}