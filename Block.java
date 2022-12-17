package Tetris;
import java.awt.Color;
import java.util.Random;

/**
 * To serve as a parent class for all Tetris blocks to inherit
 * @author Thaddeus Bielecki
 *
 */
public class Block {
	
	private int[][] shape; //array of arrays to hold the shape of the block
	private Color color; //block's color
	//its current x and y coordinates
	private int x;
	private int y;
	
	//3D array to hold all of the shapes possible rotations
	private int [][][] shapes;
	private int currentRotation; // to hold index of current rotation
	
	
	/**
	 * Block constructor, will create all possible rotations of the block and set key features
	 * @param givenShape - the desired shape of the block
	 * @param givenColor - desired color of the block
	 */
	public Block(int [][] givenShape, Color givenColor){
		//set shape and color
		this.shape = givenShape;
		this.color = givenColor;
		
		//call function to initialize possible rotations of the block
		initShapes();
	}
	
	/**
	 * Function to set all possible block rotations in the shapes variable
	 */
	private void initShapes() {
		//set size of the 3D array
		shapes = new int [4][][];
		
		//loop to create the new block shapes for each rotation and store in the array
		for(int i = 0; i < 4; i++) {
			int rows = shape[0].length;
			int columns = shape.length;
			
			shapes[i] = new int[rows][columns];
			
			for(int j = 0; j < rows; j++) {
				for(int x = 0; x < columns; x++) {
					shapes[i][j][x] = shape[columns - x - 1][j];
				}
			}
			//set the shape to the rotation
			shape = shapes[i];
		}
	}
	
	/**
	 * to rotate the shape upward
	 */
	public void rotateUp() {
		//update the current rotation
		currentRotation++;
		if(currentRotation > 3) { //adjust if out of bounds for the shapes 3D array
			currentRotation = 0;
		}
		//set the block's shape to the new rotation
		shape = shapes[currentRotation];
	}
	
	/**
	 * to rotate the shape downwards
	 */
	public void rotateDown() {
		//update the current rotation
		currentRotation--;
		if(currentRotation < 0) { //adjust if out of bounds for the shapes 3D array
			currentRotation = 3;
		}
		//set the block's shape to the new rotation
		shape = shapes[currentRotation];
	}
	
	/**
	 * create a new block
	 * @param width - where horizontally on the screen the block will spawn
	 */
	public void spawn(int width) {
		//choose a random rotation
		Random random = new Random();
		currentRotation = random.nextInt(4);
		shape = shapes[currentRotation];
		
		//start the block just off the screen
		y = 0 - getHeight();
		//choose a random x value to spawn the block to 
		x = random.nextInt(GamePanel.UNITS_WIDE - getWidth());
	}
	
	/**
	 * to find the bottom edge of the block
	 * @return - the bottom edge of the block multiplied by the size of a grid square
	 */
	public int getBottomEdge() {
		return (y + getHeight()) * GamePanel.UNIT_SIZE;
	}
	
	/**
	 * to find the right most edge of the block
	 * @return the right most edge - (not multiplied by size of a grid square)
	 */
	public int getRightEdge() {
		return (x + getWidth());
	}
	
	/**
	 * move the block to the left
	 */
	public void moveLeft() {
		x--;
	}
	
	/**
	 * move the block to the right
	 */
	public void moveRight() {
		x++;
	}
	
	/**
	 * move the block downward
	 */
	public void moveDown() {
		y++;
	}
	
	/**
	 * to get the blocks shape
	 * @return - a 2D array to represent the block's shape
	 */
	public int[][] getShape(){
		return shape;
	}
	
	/**
	 * get the block's color
	 * @return - the color of the block
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * get the vertical height of the block
	 * @return - the length of the outer array of the 2D array aka height
	 */
	public int getHeight() {
		return shape.length;
	}
	
	/**
	 * get the width of the block
	 * @return - the length of the inner array aka width
	 */
	public int getWidth() {
		return shape[0].length;
	}
	
	/**
	 * get the block's x coordinate (top left most)
	 * @return - x coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * get the block's y coordinate (top left most)
	 * @return - y coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * set the block's x coordinate
	 * @param x - the desired value of the x coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * set the block's y coordinate
	 * @param y - the desired value of the y coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
}