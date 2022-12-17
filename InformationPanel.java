package Tetris;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import TetrisBlocks.IShape;
import TetrisBlocks.JShape;
import TetrisBlocks.LShape;
import TetrisBlocks.OShape;
import TetrisBlocks.SShape;
import TetrisBlocks.TShape;
import TetrisBlocks.ZShape;

/**
 * Represents the panel to hold general information about the game
 * @author Thaddeus Bielecki
 *
 */
public class InformationPanel extends JPanel{
	//Font plainText = new Font("SansSerif", Font.PLAIN, 16);
	
	InformationPanel(){
		//set the panel's layout
		this.setLayout(new BorderLayout());
		
		//create the greeting label
		JLabel gretting = new JLabel("Welcome to my Tetris Game!");
		gretting.setFont(GameFrame.boldText);
		
		//label for easier reading
		JLabel space = new JLabel("-----------------------------");
		
		//label to act as section header
		JLabel controls = new JLabel("How to play");
		controls.setFont(GameFrame.boldText);
		
		//create labels for how to play the game
		JLabel direction1 = new JLabel(" Up arrow key - rotate up");
		direction1.setFont(GameFrame.plainText);
		JLabel direction2 = new JLabel(" Down arrow key - rotate down");
		direction2.setFont(GameFrame.plainText);
		JLabel direction3 = new JLabel(" Left arrow key - slide left");
		direction3.setFont(GameFrame.plainText);
		JLabel direction4 = new JLabel(" Right arrow - slide right");
		direction4.setFont(GameFrame.plainText);
		JLabel direction5 = new JLabel(" Space bar - drop block faster");
		direction5.setFont(GameFrame.plainText);
		
		//create panel to hold the labels
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		
		//add it to the larger panel object
		this.add(innerPanel);
		
		//add the labels to the panel
		innerPanel.add(space);
		innerPanel.add(controls);
		innerPanel.add(direction1);
		innerPanel.add(direction2);
		innerPanel.add(direction3);
		innerPanel.add(direction4);
		innerPanel.add(direction5);
		
		
		//create new inner panel to inform the user of scoring methods
		JPanel scoringExplained = new JPanel();
		scoringExplained.setLayout(new BoxLayout(scoringExplained, BoxLayout.Y_AXIS));
		
		//section header
		JLabel scoringTitle = new JLabel(" Scoring");
		scoringTitle.setFont(GameFrame.boldText);
		
		//labels describing the scoring system
		JLabel scoring1 = new JLabel(" 1 row cleared is 100 points");
		scoring1.setFont(GameFrame.plainText);
		JLabel scoring2 = new JLabel(" 2 rows cleared is 500 points");
		scoring2.setFont(GameFrame.plainText);
		JLabel scoring3 = new JLabel(" 3 row cleared is 1000 points");
		scoring3.setFont(GameFrame.plainText);
		JLabel scoring4 = new JLabel(" 4 row cleared is 2000 points");
		scoring4.setFont(GameFrame.plainText);
		JLabel scoring5 = new JLabel(" x1.5 points multiplier each level!");
		scoring5.setFont(GameFrame.plainText);
		
		//add the labels to the inner scoringExplained panel
		scoringExplained.add(scoringTitle);
		scoringExplained.add(scoring1);
		scoringExplained.add(scoring2);
		scoringExplained.add(scoring3);
		scoringExplained.add(scoring4);
		scoringExplained.add(scoring5);
		
		
		//add the inner panels to appropriate places
		this.add(gretting, "North");
		this.add(innerPanel, "Center");
		this.add(scoringExplained, "South");
		
	}
	
}
