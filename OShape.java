package TetrisBlocks;

import java.awt.Color;

import Tetris.Block;

/**
 * to represent the O shaped Tetris block object, extends the block class
 * @author Thaddeus Bielecki
 *
 */
public class OShape extends Block {

	/**
	 * constructor which passes on the desired block dimensions and color to the parent class
	 */
	public OShape(){
		super( new int[][]{ {1,1}, {1,1} }, Color.YELLOW );
	}
}
