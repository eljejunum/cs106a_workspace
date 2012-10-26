/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		//Initialize variables for the start of the game.  
		welcome();
		
		//initialize the game loop based on number of turns.  In this case it will be 13.  
		for(turn = 1; turn <= N_SCORING_CATEGORIES; turn++){
			for(int playerTurn = 1; playerTurn <= nPlayers; playerTurn++){
				startTurn(playerTurn);
				rollDice(N_DICE, DICE_SIDES);
				assignPoints(playerTurn);
			}
		}
		calculateFinalScores();
		printWinner();
	}
	
	private void welcome() {			
		display.printMessage("Welcome to Yahtzee!");
		pause(1500);
		//rgen.setSeed(1); //BUG TESTING - remove for production.  
		
		//initialize the score table so all values are the SENTINEL value (-1).  We can use this to check if a value has been added to it.  
		scoreTable = new int[nPlayers][N_CATEGORIES];
		for(int i = 0; i < nPlayers; i++){
			for(int j = 0; j < N_CATEGORIES; j++){
				scoreTable[i][j] = SENTINEL;
			}
		}
	}
	
	/**
	 * Establishes the player's turn and waits for them to roll the dice.  
	 * @param index = the Player whose turn it is from 1 - 4.  To use index in an array, subtract 1.  
	 */
	private void startTurn(int playerIndex){
		println("turn = " + playerNames[playerIndex - 1] + "-" + turn);
		String currentPlayer = playerNames[playerIndex - 1];
		display.printMessage(currentPlayer + "\'s turn!  Click \"Roll Dice\" button to roll the dice.");
		display.waitForPlayerToClickRoll(playerIndex); 
	}
	
	/**
	 * Rolls the dice and checks for re-rolls.  
	 * @param nDice = number of dice used.  In Yahtzee it will be 5.
	 * @param sidesDice = number of sides on the dice.  In this case we are using standard 6 sided dice.  
	 */
	private void rollDice(int nDice, int sidesDice){
		//give the results for each die on initial roll
		for(int i = 0; i < nDice; i++){
			dice[i] = rgen.nextInt(1, sidesDice);
			println("roll 1: " + dice[i]); //VARIABLE CHECK - remove for production.
		}
		display.displayDice(dice);
		
		//check which dice the player has selected to re-roll and assign a new value to it.  
		display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\".");
		display.waitForPlayerToSelectDice();
		for(int i = 0; i < nDice; i++){
			if(display.isDieSelected(i)){
				dice[i] = rgen.nextInt(1, sidesDice);
			}
			println("roll 2: " + dice[i]); //VARIABLE CHECK - remove for production.
		}
		display.displayDice(dice);
		
		//check which dice the player has selected to re-roll and assign a new value to it.  		
		display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\".");
		display.waitForPlayerToSelectDice();
		for(int i = 0; i < nDice; i++){
			if(display.isDieSelected(i)){
				dice[i] = rgen.nextInt(1, sidesDice);
			}
			println("roll 3: " + dice[i]); //VARIABLE CHECK - remove for production.
		}
		display.displayDice(dice);	
	}
	
	/**
	 * After player has rolled dice, player will select a category and a score will be assigned based on the roll. 
	 * If user has already selected a category, they will not be allowed to select it again.  
	 * The score will be written to the display and recorded in scoreTable[player][category].  
	 * @param playerIndex = the Player whose turn it is from 1 - 4.  To use index in an array, subtract 1.  
	 */
	private void assignPoints(int playerIndex){
		display.printMessage(playerNames[playerIndex - 1] + ", your rolls are done.  Please select a category for this roll.");
		int category;
		while(true){
			category = display.waitForPlayerToSelectCategory();
			if(scoreTable[playerIndex - 1][category - 1] == SENTINEL) break;
			display.printMessage("You have already chosen that category. Please select another category.");
		}
		
		//Calculate score for the category, save it in scoreTable, and post to display.  
		int score = calculateTurnScore(category);
		scoreTable[playerIndex - 1][category - 1] = score;
		display.updateScorecard(category, playerIndex, score);
		
		//Calculate current total score, save it in scoreTable, and post to display.  
		int scoreTotal = calculateTotalScore(playerIndex);
		scoreTable[playerIndex - 1][TOTAL - 1] = scoreTotal;
		display.updateScorecard(TOTAL, playerIndex, scoreTotal);
	}
	
	/**
	 * Run through the loops of players and determine upper score, upper bonus, lower score, and retabulate total.  
	 * 
	 */
	private void calculateFinalScores(){
		for(int i = 1; i <= nPlayers; i++){
			assignUpperScore(i);
			assignLowerScore(i);
		}
	}
	
	private void printWinner(){
		int winnerIndex = 0;
		for(int i = 1; i <= nPlayers; i++){
			if(isWinner(i)) winnerIndex = i;
		}
		String winner = playerNames[winnerIndex - 1];
		int winningScore = scoreTable[winnerIndex - 1][TOTAL - 1];
		display.printMessage("Congratulations, " + winner + ", you're the winner with a total score of " + winningScore + ".");
	}
	
	private boolean isWinner(int playerIndex){
		boolean winner;

		//find the top score
		int maxScore = 0;
		for(int i = 0; i < nPlayers; i++){
			if(scoreTable[i][TOTAL - 1] > maxScore){
				maxScore = scoreTable[i][TOTAL - 1];
			}
		}
		if(scoreTable[playerIndex - 1][TOTAL - 1] == maxScore) winner = true;
		else winner = false;

		return winner;
	}
	
	private void assignUpperScore(int playerIndex){
		int upperScore = 0;
		int upperBonusScore;
		for(int i = 0; i < SIXES; i++){
			upperScore += scoreTable[playerIndex - 1][i];
		}
		if(upperScore > UPPER_BONUS_CUTOFF){
			upperBonusScore = UPPER_BONUS_SCORE;
		}else{
			upperBonusScore = 0;
		}
		upperScore += upperBonusScore;
		scoreTable[playerIndex - 1][UPPER_BONUS - 1] = upperBonusScore;
		scoreTable[playerIndex - 1][UPPER_SCORE - 1] = upperScore;
		display.updateScorecard(UPPER_SCORE, playerIndex, upperScore);
		display.updateScorecard(UPPER_BONUS, playerIndex, upperBonusScore);
	}
	
	private void assignLowerScore(int playerIndex){
		int lowerScore = 0;
		for(int i = THREE_OF_A_KIND; i <= CHANCE; i++){
			lowerScore += scoreTable[playerIndex - 1][i - 1];
		}
		scoreTable[playerIndex - 1][LOWER_SCORE - 1] = lowerScore;
		display.updateScorecard(LOWER_SCORE, playerIndex, lowerScore);
	}
	
	/**
	 * calculates only the total score for the end of each turn.  Does not calculate subtotals. 
	 * Assumes TOTALS line is always stored as the last value in the array.  
	 * 			--> This last value for TOTALS needs to be excluded when adding the sum of other scores.   
	 * 			--> Also disregard all sentinel values of -1.  
	 * @param playerIndex = player whose turn it is from 1 - 4.  To use index in an array, subtract 1.  
	 */
	private int calculateTotalScore(int playerIndex){
		int scoreTotal = 0;
		for(int i = 0; i < N_CATEGORIES - 1; i++){
			if(scoreTable[playerIndex - 1][i] != SENTINEL){
				scoreTotal += scoreTable[playerIndex - 1][i];
			}
		}
		return scoreTotal;
	}
	
	/**
	 * Use this function to calculate the player's score after they chose a category.  
	 * @param catIndex = the category the player has chosen.  
	 * @return Player's score for the particular turn, based on category chosen.  
	 */
	private int calculateTurnScore(int catIndex){
		int score;
		switch (catIndex) {
			case ONES: 
				score = checkUpperScore(ONES);
				break;
			case TWOS: 
				score = checkUpperScore(TWOS);
				break;
			case THREES: 
				score = checkUpperScore(THREES);
				break;
			case FOURS: 
				score = checkUpperScore(FOURS);
				break;
			case FIVES: 
				score = checkUpperScore(FIVES);
				break;
			case SIXES: 
				score = checkUpperScore(SIXES);
				break;
			case THREE_OF_A_KIND: 
				score = int_of_a_kindScore(3);
				break;
			case FOUR_OF_A_KIND: 
				score = int_of_a_kindScore(4);
				break;
			case FULL_HOUSE: 
				if(isFullHouse()) score = FULL_HOUSE_SCORE;
				else score = 0;
				break;
			case SMALL_STRAIGHT: 
				if(isStraight(4)) score = SMALL_STRAIGHT_SCORE;
				else score = 0;
				break;
			case LARGE_STRAIGHT: 
				if(isStraight(5)) score = LARGE_STRAIGHT_SCORE;
				else score = 0;
				break;
			case YAHTZEE: 
				if(isYahtzee()) score = YAHTZEE_SCORE;
				else score = 0;
				break;
			case CHANCE: 
				score = chanceScore();
				break;
			default: 
				println("A valid category has not been selected.  Score set to 0.");
				score = 0;
				break;
		}
		return score;
	}
	
	/**
	 * Function designed specifically to calculate the scores for ONES through SIXES.
	 * For the upper scores, we assume that the index equals the number shown on the die that we will count.    
	 * @param index = int 1 through 6 corresponding to the category chosen.
	 * @return score of sum of all dice that match the index.  
	 */
	private int checkUpperScore(int index){
		int upperScore = 0;
		for(int i = 0; i < N_DICE; i++){
			if(dice[i] == index) upperScore += index;
		}
		return upperScore;
	}
	
	/**
	 * Calculate the CHANCE category score
	 * @return sum of all dice.
	 */
	private int chanceScore(){
		int chanceScore = 0;
		for(int i = 0; i < N_DICE; i++) chanceScore += dice[i];
		return chanceScore;
	}
	
	/**
	 * Calculate the score for the three of a kind category.  Assumes that you definitely have a 3 of a kind.  
	 * @param int number of matching dice to check for in the set.  It should be 3 or 4.  
	 * @return sum of the matching dice.  
	 */
	private int int_of_a_kindScore(int x_of_a_kind){
		int score = 0;
		
		//Set up an array to track the number of times each roll appears.  
		int[] rollTracker = new int[DICE_SIDES];
		
		// Use a nested for loop to increment each frame of the rollTracker for each number
		for(int i = 1; i <= DICE_SIDES; i++){
			for(int j = 0; j < N_DICE; j++){
				if(dice[j] == i) rollTracker[i-1]++;
			}
		}
		
		/* If any instance of the rollTracker is >= 3, that means that instance has at least 3 copies of it.  
		 * If any instance of the rollTracker is >= 4, that means that instance has at least 4 copies of it.  
		 * In this for loop, i = the possible values shown on each die, which is 1 to DICE_SIDES.  
		 * must use "i-1" in the array to check from the array [0] position.  
		 */
		for(int i = 1; i <= DICE_SIDES; i++){
			println("number of " + i + "\'s = " + rollTracker[i-1]); //VARIABLE CHECK - remove for production.  
			if(rollTracker[i-1] >= x_of_a_kind) {
				score = i * x_of_a_kind; //We can multiply the roll value by x_of_a_kind to get the sum of matching cards.  			
			} else ;//score will default to initialized zero value.  
		}
		return score;
	}
	
	private boolean isYahtzee(){
		boolean yahtzee = false;
		
		//Set up an array to track the number of times each roll appears.  
		int[] rollTracker = new int[DICE_SIDES];
		
		// Use a nested for loop to increment each frame of the rollTracker for each number
		for(int i = 1; i <= DICE_SIDES; i++){
			for(int j = 0; j < N_DICE; j++){
				if(dice[j] == i) rollTracker[i-1]++;
			}
		}
	
		for(int i = 1; i <= DICE_SIDES; i++){
			if(rollTracker[i-1] == N_DICE) yahtzee = true;
		}
		return yahtzee;
	}
	
	private boolean isFullHouse(){
		int threeMatch = 3;
		int twoMatch = 2;
		boolean fullHouse = false; 
		
		//Set up an array to track the number of times each roll appears.  
		int[] rollTracker = new int[DICE_SIDES];
		
		// Use a nested for loop to increment each frame of the rollTracker for each number
		for(int i = 1; i <= DICE_SIDES; i++){
			for(int j = 0; j < N_DICE; j++){
				if(dice[j] == i) rollTracker[i-1]++;
			}
		}
		for(int i = 0; i < DICE_SIDES; i++){
			for(int j = 0; j < DICE_SIDES; j++){
				if(rollTracker[i] == threeMatch && rollTracker[j] == twoMatch) {
					fullHouse = true;
				}
			}
		}
		return fullHouse;
	}
	
	private boolean isStraight(int length){
		
		//Set up an array to track the number of times each roll appears.  
		int[] rollTracker = new int[DICE_SIDES];
		
		// Use a nested for loop to increment each frame of the rollTracker for each number
		for(int i = 1; i <= DICE_SIDES; i++){
			for(int j = 0; j < N_DICE; j++){
				if(dice[j] == i) rollTracker[i-1]++;
			}
		}
		
		//Check for the large straight first.  There are two possible cases for large straight: 1-5 or 2-6.  
		if(length == 5){
			//first case 1-5.
			int straightCount = 0;
			for(int i = 0; i < DICE_SIDES - 1; i++){
				if(rollTracker[i] == 1){
					straightCount++;
				}
			}
			if (straightCount == length) return true;
			
			//second case 2-6
			straightCount = 0;
			for(int i = 1; i < DICE_SIDES; i++){
				if(rollTracker[i] == 1){
					straightCount++;
				}
			}
			if (straightCount == length) return true;
			else return false;
		}
		
		//Check for small straight.  There are three possible small straight cases: 1-4, 2-5, 3-6.
		else if(length == 4){
			//first case 1-4.
			int straightCount = 0;  
			for(int i = 0; i < DICE_SIDES - 2; i++){
				if(rollTracker[i] >= 1){
					straightCount++;
				}
			}
			if (straightCount == length) return true;
			
			//second case 2-5.
			straightCount = 0;
			for(int i = 1; i < DICE_SIDES - 1; i++){
				if(rollTracker[i] >= 1){
					straightCount++;
				}
			}
			if (straightCount == length) return true;
			
			//third case 3-6.
			straightCount = 0;
			for(int i = 2; i < DICE_SIDES; i++){
				if(rollTracker[i] >= 1){
					straightCount++;
				}
			}
			if (straightCount == length) return true;
			else return false;  
		}
		
		
		else return false;
	}
	
	
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	
	private int [][] scoreTable;
	private int[] dice = new int[N_DICE];
	private int turn;
	

}
