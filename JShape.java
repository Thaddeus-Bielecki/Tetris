package TetrisBlocks;

import java.awt.Color;

import Tetris.Block;


/**
 * to represent the J shaped Tetris block object, extends the block class
 * @author Thaddeus Bielecki
 *
 */
public class JShape extends Block {
	
	/**
	 * constructor which passes on the desired block dimensions and color to the parent class
	 */
	public JShape(){
		super(new int[][]{{1,0}, {1,0}, {1,1}}, Color.BLUE);
	}
}
