package TetrisBlocks;

import java.awt.Color;

import Tetris.Block;

/**
 * to represent the Z shaped Tetris block object, extends the block class
 * @author Thaddeus Bielecki
 *
 */
public class ZShape extends Block{

	/**
	 * constructor which passes on the desired block dimensions and color to the parent class
	 */
	public ZShape() {
		super( new int[][]{ {1,1,0}, {0,1,1} }, Color.RED );
	}
}
