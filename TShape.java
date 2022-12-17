package TetrisBlocks;

import java.awt.Color;

import Tetris.Block;

/**
 * to represent the T shaped Tetris block object, extends the block class
 * @author Thaddeus Bielecki
 *
 */
public class TShape extends Block {

	/**
	 * constructor which passes on the desired block dimensions and color to the parent class
	 */
	public TShape() {
		super( new int[][]{ {0,1,0}, {1,1,1} }, Color.MAGENTA );
	}
}
