/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */


import acm.graphics.*;
import java.awt.Canvas;

public class HangmanCanvas extends GCanvas {
	
	// Establish all the parts of the gallows and hangman
	private GLine scaffold;
	private GLine beam;
	private GLine rope;
	
	private GOval head;
	private GLine body;
	private GLine rightArm;
	private GLine rightHand;
	private GLine leftArm;
	private GLine leftHand;
	private GLine rightHip;
	private GLine rightLeg;
	private GLine rightFoot;
	private GLine leftHip;
	private GLine leftLeg;
	private GLine leftFoot;
	
	private GLabel hiddenWord;
	private GLabel badLetters;
	private String badLettersString;
	
	
/** Resets the display so that only the scaffold appears */
	public void reset() {
		// Remove everything from the Canvas and start fresh
		removeAll();
		badLettersString = "Incorrect guesses: ";
		
		// First draw in the scaffolding.  
		int scaffX = cX - BEAM_LENGTH;
		int scaffY = cY - (BODY_LENGTH/2) - (HEAD_DIAMETER) - ROPE_LENGTH;
		int floorY = scaffY + SCAFFOLD_HEIGHT;
		int beamX = cX;
		int ropeY = scaffY + ROPE_LENGTH;
		scaffold = new GLine(scaffX, floorY, scaffX, scaffY);
		beam = new GLine(scaffX, scaffY, beamX, scaffY);
		rope = new GLine(beamX, scaffY, beamX, ropeY);
		
		add(scaffold);
		add(beam);
		add(rope);
		
		//set up all the other body parts, but don't draw them in yet.  
		head = new GOval(cX - HEAD_RADIUS, ropeY, HEAD_DIAMETER, HEAD_DIAMETER);
		body = new GLine(cX, cY - BODY_LENGTH/2, cX, cY + BODY_LENGTH/2);
		rightArm = new GLine (cX, shoulderY, cX + UPPER_ARM_LENGTH, shoulderY);
		rightHand = new GLine (cX + UPPER_ARM_LENGTH, shoulderY, cX + UPPER_ARM_LENGTH, shoulderY + LOWER_ARM_LENGTH);
		leftArm = new GLine (cX, shoulderY, cX - UPPER_ARM_LENGTH, shoulderY);
		leftHand = new GLine (cX - UPPER_ARM_LENGTH, shoulderY, cX - UPPER_ARM_LENGTH, shoulderY + LOWER_ARM_LENGTH);
		rightHip = new GLine (cX, cY + BODY_LENGTH/2, cX + HIP_WIDTH, cY + BODY_LENGTH/2);
		rightLeg = new GLine (cX + HIP_WIDTH, cY + BODY_LENGTH/2, cX + HIP_WIDTH, footY);
		rightFoot = new GLine (cX + HIP_WIDTH, footY, cX + HIP_WIDTH + FOOT_LENGTH, footY);
		leftHip = new GLine (cX, cY + BODY_LENGTH/2, cX - HIP_WIDTH, cY + BODY_LENGTH/2);
		leftLeg = new GLine (cX - HIP_WIDTH, cY + BODY_LENGTH/2, cX - HIP_WIDTH, footY);
		leftFoot = new GLine (cX - HIP_WIDTH, footY, cX - HIP_WIDTH - FOOT_LENGTH, footY);
		
	
	/* Uncomment out if you need to test the layout. 
		add(head);
		add(body);
		add(rightArm);
		add(rightHand);
		add(leftArm);
		add(leftHand);
		add(rightHip);
		add(rightLeg);
		add(rightFoot);
		add(leftHip);
		add(leftLeg);
		add(leftFoot);
	*/
		
		hiddenWord = new GLabel("The WORD: ", 15, footY + 50);
		//hiddenWord.setFont(new Font("Serif", Font.BOLD, 15));
		
		badLetters = new GLabel(badLettersString, 15, footY + 90);
		//badLetters.setFont(new Font("Serif", Font.BOLD, 10));
		
		add(hiddenWord);
		add(badLetters);
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String wordLabel) {
		hiddenWord.setLabel("THE WORD: " + wordLabel);
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter, int bodyCounter) {
		
		switch (bodyCounter){
			case 8: break;
			case 7: 
				add(head);
				break;
			case 6:
				add(body);
				break;
			case 5:
				add(rightArm);
				add(rightHand);
				break;
			case 4:
				add(leftArm);
				add(leftHand);
				break;
			case 3:
				add(rightHip);
				add(rightLeg);
				break;
			case 2:
				add(rightFoot);
				break;
			case 1:
				add(leftHip);
				add(leftLeg);
				break;
			case 0:
				add(leftFoot);
				break;				
		}
		String str = Character.toString(letter);		
		badLettersString += str;
		badLetters.setLabel(badLettersString);
	}

/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;

/* Useful Shorthand */
	//canvas center
	public static final int cWidth = 400;
	public static final int cHeight = 400;
	private int cX = cWidth/2;
	private int cY = cHeight/2;
	private static final int HEAD_DIAMETER = HEAD_RADIUS * 2;
	private int shoulderY = cY - BODY_LENGTH/2 + ARM_OFFSET_FROM_HEAD;
	private int footY = cY + BODY_LENGTH/2 + LEG_LENGTH; 

}
