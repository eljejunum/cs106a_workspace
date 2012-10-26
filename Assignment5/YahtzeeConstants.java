/*
 * File: YahtzeeConstants.java
 * ---------------------------
 * This file declares several constants that are shared by the
 * different modules in the Yahtzee game.
 */

public interface YahtzeeConstants {

/** The width of the application window */
	public static final int APPLICATION_WIDTH = 600;

/** The height of the application window */
	public static final int APPLICATION_HEIGHT = 350;

/** The number of dice in the game */
	public static final int N_DICE = 5;
	public static final int DICE_SIDES = 6;

/** The maximum number of players */
	public static final int MAX_PLAYERS = 4;

/** The total number of categories */
	public static final int N_CATEGORIES = 17;

/** The number of categories in which the player can score */
	public static final int N_SCORING_CATEGORIES = 13;

/** The constants that specify categories on the scoresheet */
	public static final int ONES = 1;
	public static final int TWOS = 2;
	public static final int THREES = 3;
	public static final int FOURS = 4;
	public static final int FIVES = 5;
	public static final int SIXES = 6;
	public static final int UPPER_SCORE = 7;
	public static final int UPPER_BONUS = 8;
	public static final int THREE_OF_A_KIND = 9;
	public static final int FOUR_OF_A_KIND = 10;
	public static final int FULL_HOUSE = 11;
	public static final int SMALL_STRAIGHT = 12;
	public static final int LARGE_STRAIGHT = 13;
	public static final int YAHTZEE = 14;
	public static final int CHANCE = 15;
	public static final int LOWER_SCORE = 16;
	public static final int TOTAL = 17;

/** The constants specify the fixed scores values and bonuses */
	public static final int UPPER_BONUS_SCORE = 35;
	public static final int FULL_HOUSE_SCORE = 25;
	public static final int SMALL_STRAIGHT_SCORE = 30;
	public static final int LARGE_STRAIGHT_SCORE = 40;
	public static final int YAHTZEE_SCORE = 50;
	
/** This constant is used to check if a score has been recorded.  It should always be a negative number*/	
	public static final int SENTINEL = -1;

/** Upper Score Total needs to be greater than this value to give Upper Bonus */
	public static final int UPPER_BONUS_CUTOFF = 63;
}


