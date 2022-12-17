package TetrisBlocks;

import java.awt.Color;

import Tetris.Block;

/**
 * to represent the I shaped Tetris block object, extends the block class
 * @author Thaddeus Bielecki
 *
 */
public class IShape extends Block {

	/**
	 * constructor which passes on the desired block dimensions and color to the parent class
	 */
	public IShape(){
		super(new int[][] {{1,1,1,1}}, Color.CYAN);
	}
	
	
	@Override
	/**
	 * special change to rotateUp so the block rotates more naturally
	 */
	public void rotateUp() {
		super.rotateUp();
		
		//stop the block from swinging open like a door and rotate more in place
		if(this.getWidth() == 1) {
			this.setX(getX() + 1);
			this.setY(getY() - 1);
		} else {
			this.setX(getX() - 1);
			this.setY(getY() + 1);
		}
	}
	
	@Override
	/**
	 * special change to rotateDown so the block rotates more naturally
	 */
	public void rotateDown() {
		super.rotateUp();
		
		//stop the block from swinging open like a door and rotate more in place
		if(this.getWidth() == 1) {
			this.setX(getX() + 1);
			this.setY(getY() - 1);
		} else {
			this.setX(getX() - 1);
			this.setY(getY() + 1);
		}
	}
}
