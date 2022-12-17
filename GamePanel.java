package Tetris;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import TetrisBlocks.IShape;
import TetrisBlocks.JShape;
import TetrisBlocks.LShape;
import TetrisBlocks.OShape;
import TetrisBlocks.SShape;
import TetrisBlocks.TShape;
import TetrisBlocks.ZShape;


/**
 * The main panel, to execute the actual game processes
 * @author Thaddeus Bielecki
 *
 */
public class GamePanel extends JPanel implements ActionListener{

	//constants
	static final int SCREEN_WIDTH = 250;
	static final int SCREEN_HEIGHT = 500;
	static final int UNIT_SIZE = 25;
	static final int UNITS_WIDE = SCREEN_WIDTH / UNIT_SIZE;
	static final int UNITS_TALL = SCREEN_HEIGHT / UNIT_SIZE;
	//timer delay - will be altered as user progresses to new levels
	static int DELAY = 750;
	
	boolean running; // to be set true while game is running
	Timer timer; //to control the time the run of the program
	Random random; //used to generate random values
	long score; //to hold a player's score
	int level = 1; //holds the level of the game the player is on
	int totalRowsCleared = 0; //tracks total number of rows cleared
	int lastBlock = -1; //tracks the last block that was spawned
	int scoreMultiplier = 1; //adjusts score as player progresses through levels
	
	private Block block;//the block object to be manipulated
	
	private Color [][] background = new Color[UNITS_TALL][UNITS_WIDE];//array of colors to act as the background of the game
	private Block[] blocks; //array to hold all possible blocks
	
	GamePanel(){
		
		//initialize
		random = new Random();
		blocks = new Block[] { new IShape(), new JShape(), new LShape(), new OShape(), new SShape(), new TShape(), new ZShape()};
		
		//setup the panel
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		
		//spawn in the block and start the game
		spawnBlock();
		startGame();
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//draw the rest of panel and block
		draw(g);
		drawBlock(g);
	}
	
	/**
	 * To start the game - setting the timer and running flag to true
	 */
	public void startGame() {
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	/**
	 * in-charge of drawing the background as well as the white grid lines
	 * @param g the graphics object passed by paintComponent
	 */
	public void draw(Graphics g) {
		//draw white grid lines as long as program is running
		if(running) {
			g.setColor(Color.white);
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			//draw the background
			Color color; 
			for(int i = 0; i < UNITS_TALL; i++) {
				for(int j = 0; j < UNITS_WIDE; j++) {
					color = background[i][j];
					
					if(color != null) {
						int x = j * UNIT_SIZE;
						int y = i * UNIT_SIZE;
						
						//function to draw each grid square of the background
						drawGridSquare(g, color, x , y);
					}
				}
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//check the game is running
		if(running) {
			//check for collisions of any kind
			if(checkCollision()) {
				//check if the block is out of bounds
				if(isBlockOutOfBounds()) {
					//set the block to null and end the game
					block = null;
					running = false;
					gameOver();
					timer.stop();
				} else {
					try {
					//otherwise move the block to the background and spawn a new one
					moveBlockToBackground();
					spawnBlock();
					repaint();
					}catch(IndexOutOfBoundsException ie) {
						System.out.println("tried moving block to background but index was out of bounds");
						ie.printStackTrace();
						//gameOver();
						running = false;
						//timer.stop();
					}
				}
			}
			//re-check the game is still running
			if(running) {
				//surround in try catch blocks to account for exceptions
				try {
					//attempt to move and repaint
					move();
					repaint();
				} catch (IndexOutOfBoundsException ex) {
					ex.printStackTrace();
					System.out.println("There was an index out of bounds exception");
				} catch(Exception ex) {
					System.out.println("A different form of exception occured");
					ex.printStackTrace();
				}
				
			}
		
		} else {
			//if not running the game is over
			block = null;
			gameOver();
			timer.stop();
		}
		
	}
	
	/**
	 * responsible for moving the all objects that need moved in the game
	 */
	public void move() {
		block.moveDown();
		clearLines();
		repaint();
	}
	
	/**
	 * To inform the user the game has ended and request them to save their name to the leader board
	 */
	public void gameOver() {
		
		String str; //to hold the user's initials 
		str = JOptionPane.showInputDialog("Game Over :( Please enter your initials to save your score (max 3 characters)");
		if(str != null) {
			while(str.length() > 3) {
				str = JOptionPane.showInputDialog("ERROR -- too many characters were entered please try again... (max 3 characters)");
			}
		}
		
		
		try {
			boolean isEmpty; //flag
			
			//scanners for input use
			Scanner scanner = null;
			Scanner innerScan = null;
			
			//variables to help read in and process records
			String line = "";
			int properIndex = 0;
			boolean isLessThanAll = false;
			
			//to hold the leaders without the current player's info
			ArrayList<String> leaderInitials = new ArrayList<>();
			ArrayList<String> leaderScores = new ArrayList<>();
			
			//the hold the lists with new elements inserted
			ArrayList<String> newInitials;
			ArrayList<String> newScores;
			//String [] currentLeaders1 = new String[];
			
			//check if the file is empty
			File output = new File("leaderboard.txt");
			if(output.length() == 0) {
				isEmpty = true;
			} else {
				isEmpty = false;
			}
			
			//check the isEmpty flag
			if(!isEmpty) {
				
				//create scanner to read from the file
				scanner = new Scanner(new File("leaderboard.txt"));
				scanner.useDelimiter(" "); //each record will be space separated
				
				while(scanner.hasNextLine()) {
					try {
					
						//use one scanner to read in from file
						line = scanner.nextLine();
						//use other scanner to seperate the input into proper lists
						innerScan = new Scanner(line);
						leaderInitials.add(innerScan.next());
						leaderScores.add(innerScan.next());
					
					} catch(NoSuchElementException e) {
						System.out.println("There was a blank line");
						e.printStackTrace();
					}
					
				}
				//close both scanners
				scanner.close();
				innerScan.close();
				
				//loop to find correct index to insert record
				for(int i = 0; i < leaderInitials.size(); i++) { 
					if(score > Long.valueOf(leaderScores.get(i))) {
						properIndex = i;
						isLessThanAll = false;
						break;
					}
					isLessThanAll = true;
				}
				
				//block to run if there is a new last place
				if(isLessThanAll) {
					//add the player's values to the end of the list
					leaderInitials.add(str);
					leaderScores.add(String.valueOf(score));
			
					BufferedWriter writer = new BufferedWriter(new FileWriter("leaderboard.txt"));
					//write the lists to the file
					for(int i = 0; i < leaderInitials.size(); i++) {
						writer.write(leaderInitials.get(i) + " ");
						writer.write(leaderScores.get(i));
						writer.newLine();
					}
					writer.close();
					
					
				} else {
					
					//use copy constructor to create new arrayLists of each
					newInitials = new ArrayList<String>(leaderInitials);
					newScores = new ArrayList<String>(leaderScores);
					
					//add a second instance of the last element to the end of the new list
					newInitials.add(newInitials.get(newInitials.size()-1));
					newScores.add(newScores.get(newScores.size()-1));
					
					//move all of the elements behind the spot of new element back one
					for(int i = newInitials.size() - 1; i > properIndex; i--) {
						newInitials.set(i, newInitials.get(i - 1));
						newScores.set(i, newScores.get(i - 1));
					}
					
					//add the values to the new array list at correct spots
					newInitials.set(properIndex, str);
					newScores.set(properIndex, String.valueOf(score));
					
					
					//ensure the file is empty by writing an empty string to it
					PrintWriter writer2 = new PrintWriter("leaderboard.txt");
					writer2.print("");
					writer2.close();

					BufferedWriter writer = new BufferedWriter(new FileWriter("leaderboard.txt", false));
					
					//loop to write to the file
					for(int i = 0; i < newInitials.size(); i++) {
						writer.write(newInitials.get(i) + " ");
						writer.write(newScores.get(i));
						writer.newLine();
						
					}
					
					writer.close();
				}
				
			} else { //block runs if the output file is empty
				BufferedWriter writer = new BufferedWriter(new FileWriter("leaderboard.txt", true));
				writer.write(str);
				writer.write(" ");
				writer.write(String.valueOf(score));
				writer.close();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "Thanks for playing!");
	}
	
	
	/**
	 * To spawn a new block to the user
	 */
	public void spawnBlock() {
		
		//to determine a random block to spawn
		int rand = random.nextInt(7);
		
		//if it is the same number as the last block.. change it up
		if(rand == lastBlock) {
			if(rand > 0) {
				rand -= 1;
			} else {
				rand += 1;
			}
		}
		
		lastBlock = rand; //set new last block
		
		//create the block and call it's spawn function
		block = blocks[rand];
		block.spawn(UNITS_WIDE / 2 - 1);
		
		repaint();
		
	}
	
	
	/**
	 * function to draw the block itself
	 * @param g Graphics parameter to allow the display of the object
	 */
	public void drawBlock(Graphics g) {
		
		 for(int i = 0; i < block.getHeight(); i++) {
			for(int j = 0; j < block.getWidth(); j++) {
				//if the array's value is 1, paint
				if(block.getShape()[i][j] == 1) {
					//multiply by unit size for proper sizing
					int x = (block.getX() + j)* UNIT_SIZE;
					int y = (block.getY() + i) * UNIT_SIZE;
					//call function to draw each square of the block
					drawGridSquare(g, block.getColor(), x, y);
				}
			}
		}
		
	}
	
	/**
	 * To check if the block is out of bounds
	 * @return true - if block is out of bounds, false if otherwise
	 */
	public boolean isBlockOutOfBounds() {
		if(block.getY() < 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * to move the block to the background of the game
	 * @throws IndexOutOfBoundsException if block to be moved to background is out of bounds, throw exception
	 */
	public void moveBlockToBackground() throws IndexOutOfBoundsException{
		
		Color color = block.getColor();
		for(int i = 0; i < block.getHeight(); i++) {
			for(int j = 0; j < block.getWidth(); j++) {
				//if index is too small throw the exception
				if(i < 0 || j < 0) {
					throw new IndexOutOfBoundsException();
				}
				//otherwise paint it to the background
				if(block.getShape()[i][j] == 1) {
						background[block.getY() + i][block.getX() + j] = color;
					
				}
				
			}
		}
	}
	
	
	/**
	 * To draw a square of a block
	 * @param g - Graphics to draw with
	 * @param color - the desired color of the square
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	private void drawGridSquare(Graphics g, Color color, int x, int y) {
		g.setColor(color);
		g.fillRect(x, y, UNIT_SIZE, UNIT_SIZE);
		g.setColor(Color.WHITE);
		g.drawRect(x, y, UNIT_SIZE, UNIT_SIZE);
	}
	
	/**
	 * Check the if the block has collided with anything
	 * @return - true if there has been a collision, false otherwise
	 */
	public boolean checkCollision() {
		if(block.getBottomEdge() == SCREEN_HEIGHT) { //check if it hit the bottom
			return true;
		}
		
		if(checkBottomBackground()) {
			return true;
		}
		
		if(checkLeftBackground()) {
			return true;
		}
		
		if(checkRightBackground()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * to check if the bottom of the block has hit the background under it
	 * @return - true if it has hit, false if not
	 */
	private boolean checkBottomBackground() {
		
		int[][] shape = block.getShape();
		
		for(int i = 0; i < block.getWidth(); i++) {
			for(int j = block.getHeight() - 1; j >= 0; j--) {
				if(shape[j][i] == 1) {
					int x = i + block.getX();
					int y = j + block.getY() + 1;
					if(y < 0) { break; }
					if(background[y][x] != null) { return true; } 
						break;
				}
			}
		}
		return false;
		
	}
	
	/**
	 * to check if the block has hit the background to its left
	 * @return - true if it has hit, false otherwise
	 */
	private boolean checkLeftBackground() {
		
		int[][] shape = block.getShape();
		
		for(int i = 0; i < block.getHeight(); i++) {
			for(int j = 0; j < block.getWidth(); j++) {
				if(shape[i][j] == 1) {
					int x = j + block.getX();
					int y = i + block.getY();
					if(y < 0) { break; }
					if(background[y][x] != null) { return true; } 
						break;
				}
			}
		}
		return false;
	}
	
	/**
	 * to check if the block has hit the background to its right
	 * @return
	 */
	private boolean checkRightBackground() {
		int[][] shape = block.getShape();
		
		for(int i = 0; i < block.getHeight(); i++) {
			for(int j = block.getWidth() - 1; j >= 0; j--) {
				if(shape[i][j] == 1) {
					int x = j + block.getX();// + 1;
					int y = i + block.getY();
					if(y < 0) { break; }
					if(background[y][x] != null) { return true; } 
						break;
				}
			}
		}
		return false;
	}
	
	/**
	 * to clear filled rows
	 */
	public void clearLines() {
		
		boolean lineFilled;
		int numberOfClears = 0;
		for(int i = UNITS_TALL - 1; i >= 0; i--) {
			lineFilled = true;
			for(int j = 0; j < UNITS_WIDE; j++) {
				if(background[i][j] == null) {
					lineFilled = false;
					break;
				}
			}
			
			if(lineFilled) {
				//clear an individual line & shift everything down
				clearLine(i);
				shiftDown(i);
				i++; 
				clearLine(0);
				
				numberOfClears += 1; //increment for scoring purposes
				switch(numberOfClears) {
				case 1:
					score += (100 * scoreMultiplier);
					break;
				case 2:
					score += (500 * scoreMultiplier);
					break;
				case 3:
					score += (1000 * scoreMultiplier);
					break;
				case 4:
					score += (2000 * scoreMultiplier);
					break;
				}
				
				//update total rows cleared and level as needed as well as the score the user sees
				totalRowsCleared++;
				updateLevel();
				GameFrame.updateScore();
				GameFrame.updateLevelsCleared();
				GameFrame.updateRowsCleared();
				repaint();
			}
		}
	}
	
	/**
	 * clear an individual row
	 * @param row - the row to be cleared
	 */
	private void clearLine(int row) {
		for(int k = 0; k < UNITS_WIDE; k++) {
			background[row][k] = null;
		}
	}
	
	/**
	 * shift the background above a given row down
	 * @param row - the row that is now empty
	 */
	private void shiftDown(int row) {
		for(int i = row; i > 0; i--) {
			for(int j = 0; j < UNITS_WIDE; j++) {
				background[i][j] = background[i - 1][j];
			}
		}
	}
	
	/**
	 * to update the player's level and scoreMultiplier
	 */
	private void updateLevel() {
		switch(totalRowsCleared) {
		case 5:
			level = 2;
			scoreMultiplier *= 1.5;
			break;
		case 10:
			level = 3;
			scoreMultiplier *= 1.5;
			break;
		case 15:
			level = 4;
			scoreMultiplier *= 1.5;
			break;
		case 20:
			level = 5;
			scoreMultiplier *= 1.5;
			break;
		case 25:
			level = 6;
			scoreMultiplier *= 1.5;
			break;
		case 30:
			level = 7;
			scoreMultiplier *= 1.5;
			break;
		case 35:
			level = 8;
			scoreMultiplier *= 1.5;
			break;
		case 40:
			level = 9;
			scoreMultiplier *= 1.5;
			break;
		case 45:
			level = 10;
			scoreMultiplier *= 1.5;
			break;
		case 50:
			level = 11;
			scoreMultiplier *= 1.5;
			break;
		case 55:
			level = 12;
			scoreMultiplier *= 1.5;
			break;
		case 60:
			level = 13;
			scoreMultiplier *= 1.5;
			break;
		case 65:
			level = 14;
			scoreMultiplier *= 1.5;
			break;
		case 70:
			level = 15;
			scoreMultiplier *= 1.5;
			break;
		case 75:
			level = 16;
			scoreMultiplier *= 1.5;
			break;
		case 80:
			level = 17;
			scoreMultiplier *= 1.5;
			break;
		case 85:
			level = 18;
			scoreMultiplier *= 1.5;
			break;
		case 90:
			level = 19;
			scoreMultiplier *= 1.5;
			break;
		case 95:
			level = 20;
			scoreMultiplier *= 1.5;
			break;
		}
		if(level <= 20) {//needs to be last level in the switch statement
			DELAY -= 25; //change game speed
			timer.setDelay(DELAY);
		}
	}
	
	/**
	 * getter for the player's score
	 * @return - the score variable
	 */
	public long getScore() {
		return score;
	}
	
	/**
	 * getter for the player's level
	 * @return - the level variable
	 */
	public int getLevel() {
		return level;
	}
	
	public int getRowsCleared() {
		return totalRowsCleared;
	}
	
	/**
	 * Inner class to track user key usage
	 * @author Thaddeus Bielecki
	 *
	 */
	public class MyKeyAdapter extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				
				if(block == null) { return; }
				//if not along left wall.. move left
				if(block.getX() > 0){
					block.moveLeft();
					//check if there was a collision
					if(checkCollision()) {
						//if so move back to the right
						//and move block to the background and create a new block
						block.moveRight();
						moveBlockToBackground();
						spawnBlock();
					}
					repaint();
				}
				break;
				
			case KeyEvent.VK_RIGHT:
				if(block == null) { return; }
				//if not along the right wall
				if(block.getRightEdge() < 10) {
					block.moveRight();
					//check for collision
					if(checkCollision()) {
						//if so move back to the left
						//and move block to the background and create a new block
						block.moveLeft();
						moveBlockToBackground();
						spawnBlock();
					}
					repaint();
				}
				break;
				
			case KeyEvent.VK_SPACE:
				if(block == null) { return; }
				
				//check the block can be moved down - must do this to prevent the block to going to an out of bounds index
				if(block.getBottomEdge() < (SCREEN_HEIGHT - UNIT_SIZE) && !checkCollision()) {
					block.moveDown();
					repaint();
				}
				break;
				
			case KeyEvent.VK_UP: //rotate up
				if(block == null) { return; }
				block.rotateUp();
				//check if there were any collisions with boundaries or the background
				if(block.getX() < 0 || block.getRightEdge() > 10 || checkCollision() || block.getBottomEdge() > SCREEN_HEIGHT) {
					//if so rotate back the other direction
					block.rotateDown();
				}
				repaint();
				break;
				
			case KeyEvent.VK_DOWN: //rotate down
				if(block == null) { return; }
				block.rotateDown();
				//check if there were any collisions with boundaries or the background
				if(block.getX() < 0 || block.getRightEdge() > 10 || checkCollision() || block.getBottomEdge() > SCREEN_HEIGHT) {
					//if so rotate back the other direction
					block.rotateUp();
				}
				repaint();
				break;
			}
		}
	}
	
}
