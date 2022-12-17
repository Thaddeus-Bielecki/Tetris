package Tetris;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Represents the game window
 * @author Thaddeus Bielecki
 *
 */
public class GameFrame extends JFrame {
	
	//Panels to be within the Frame
	static GamePanel myPanel;
	InformationPanel infoPanel;
	JPanel scorePanel;
	
	//Labels to be updated throughout program to keep user informed
	static JLabel score;
	static JLabel level;
	static JLabel rows;
	
	//Fonts to be used throughout the program
	static Font plainText = new Font("SansSerif", Font.PLAIN, 16);
	static Font boldText = new Font("SansSerif", Font.BOLD, 20);
	
	/**
	 * Class constructor - handles majority of actions
	 */
	GameFrame(){
		//set up the frame
		this.setLayout(new BorderLayout());
		this.add(myPanel = new GamePanel()); //add the main game panel
		this.setTitle("Tetris");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		//initialize the GameFrame's information panel
		infoPanel = new InformationPanel();
		
		//create Panel to track the player's score
		scorePanel = new JPanel();
		scorePanel.setLayout(new BorderLayout());
		
		//panel to be held within scorePanel
		//will hold the player's current level and score
		JPanel levelScore = new JPanel();
		levelScore.setLayout(new BoxLayout(levelScore, BoxLayout.Y_AXIS));
		
		//initialize the score JLabel
		//add the player's score to the levelScore Panel
		score = new JLabel("Score: " + myPanel.getScore());
		score.setFont(boldText);
		levelScore.add(score);
		
		//initialize the level JLabel
		//add the player's level to the levelScore Panel
		level = new JLabel("Level: " + myPanel.getLevel());
		level.setFont(boldText);
		levelScore.add(level);
		
		rows = new JLabel("Rows Cleared: " + myPanel.getRowsCleared());
		rows.setFont(boldText);
		levelScore.add(rows);
		
		//add the levelScore panel to the larger scorePanel
		scorePanel.add(levelScore, "North");
		
		//panel to contain the current leader board - will be held within the scorePanel
		JPanel leaderPanel = new JPanel();
		leaderPanel.setLayout(new BoxLayout(leaderPanel, BoxLayout.Y_AXIS));
		
		//read in the leaders from the file leaderboard.txt
		String line;
		int counter = 0;
		String [] leaders = new String[10];
		//could throw exception so surround in try/catch block
		try {
			BufferedReader reader = new BufferedReader(new FileReader("leaderboard.txt"));
			while((line = reader.readLine()) != null && counter < 10) {
				leaders[counter] = line;
				counter++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The file wasn't found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//in the case there were not yet 10 leaders -> change them to a blank space instead of null
		for(int i = 0; i < 10; i++) {
			if(leaders[i] == null) {
				leaders[i] = " ";
			}
		}
		
		//for more appealing look to user
		JLabel space = new JLabel("-----------------------");
		
		//the leader board header
		JLabel leader0 = new JLabel("Leaderboard");
		leader0.setFont(boldText);
		
		//turn the leaders into JLabels
		JLabel leader1 = new JLabel("1. " + leaders[0]);
		leader1.setFont(plainText);
		
		JLabel leader2 = new JLabel("2. " + leaders[1]);
		leader2.setFont(plainText);
		
		JLabel leader3 = new JLabel("3. " + leaders[2]);
		leader3.setFont(plainText);
		
		JLabel leader4 = new JLabel("4. " + leaders[3]);
		leader4.setFont(plainText);
		
		JLabel leader5 = new JLabel("5. " + leaders[4]);
		leader5.setFont(plainText);
		
		JLabel leader6 = new JLabel("6. " + leaders[5]);
		leader6.setFont(plainText);
		
		JLabel leader7 = new JLabel("7. " + leaders[6]);
		leader7.setFont(plainText);
		
		JLabel leader8 = new JLabel("8. " + leaders[7]);
		leader8.setFont(plainText);
		
		JLabel leader9 = new JLabel("9. " + leaders[8]);
		leader9.setFont(plainText);
		
		JLabel leader10 = new JLabel("10. " + leaders[9]);
		leader10.setFont(plainText);
		
		//add the JLabels to the leaderPanel
		leaderPanel.add(space);
		leaderPanel.add(leader0);
		leaderPanel.add(leader1);
		leaderPanel.add(leader2);
		leaderPanel.add(leader3);
		leaderPanel.add(leader4);
		leaderPanel.add(leader5);
		leaderPanel.add(leader6);
		leaderPanel.add(leader7);
		leaderPanel.add(leader8);
		leaderPanel.add(leader9);
		leaderPanel.add(leader10);
		
		//add the leaderPanel to the larger scorePanel
		scorePanel.add(leaderPanel, "Center");
		
		//set the scorePanel's size
		scorePanel.setPreferredSize(new Dimension(200, GamePanel.SCREEN_HEIGHT));
		
		//add the main game panel, information panel and score panel to the frame
		this.add(myPanel, "Center");
		this.add(infoPanel, "West");
		this.add(scorePanel, "East");
		
		//finish adjusting the frame
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
	}
	
	/**
	 * To update the score JLabel when the player's score increases
	 */
	public static void updateScore() {
		long newScore = myPanel.getScore();
		String stringScore = String.valueOf(newScore);
		score.setText("Score: " + stringScore);
	}
	
	/**
	 * To update the level JLabel when the player moves to next level
	 */
	public static void updateLevelsCleared() {
		int currentLevel = myPanel.getLevel();
		String stringlvl = String.valueOf(currentLevel);
		level.setText("Level: " + stringlvl);
		
	}
	
	public static void updateRowsCleared() {
		int currentRows = myPanel.getRowsCleared();
		String stringRows = String.valueOf(currentRows);
		rows.setText("Rows Cleared: " + stringRows);
	}
	
}