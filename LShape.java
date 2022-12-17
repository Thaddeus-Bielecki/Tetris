package TetrisBlocks;

import java.awt.Color;

import Tetris.Block;


/**
 * to represent the L shaped Tetris block object, extends the block class
 * @author Thaddeus Bielecki
 *
 */
public class LShape extends Block{

	/**
	 * constructor which passes on the desired block dimensions and color to the parent class
	 */
	public LShape() {
		super( new int[][]{ {0,0,1}, {1,1,1} }, Color.ORANGE );
	}
}
