/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

//import acm.graphics.*;
import acm.program.*;
import acm.util.*;
//import java.awt.*;

public class Hangman extends ConsoleProgram {
	
	public final static int ATTEMPTS = 8;
	
	public void init() {
		canvas = new HangmanCanvas();
		canvas.setSize(400, 400);
		println("width = " + canvas.getWidth());
		add(canvas);
	}
	
    public void run() {
    	println("Welcome to Hangman!");
		selectWord();
		canvas.reset();
		while(guessesRemaining > 0 && !isWordCorrect()){
			displayWordState();
			submitLetter();
			checkLetter();
		}
		endState();
	}
    
    /**Pulls a word from the HangmanLexicon class and deconstruct it.
    * Establishes the starting state for the hidden word with dashes.   
    */
    public void selectWord(){
    	int wordNumber = rGen.nextInt(0, (lexicon.getWordCount() - 1));
    	word = lexicon.getWord(wordNumber);
    	wordLength = word.length();
    		for (int i = 0; i < wordLength; i++){
    			hiddenWord += "-";
    		}
    }
    
    /** Displays the word for each turn
     * 
     */
    public void displayWordState(){
    	canvas.displayWord(hiddenWord);
    	println("The word now looks like this: " + hiddenWord);
    	if (guessesRemaining > 1){
    		println("You have " + guessesRemaining + " guesses left.");
    	} else {
    		println("You only have one guess left.");
    	}
    	//println("answer: " + word);  //debug - remove for final
    }
    
    /** Reads the users guess, capitalizes it, and checks if it is a valid entry.
     *  
     */
    public void submitLetter(){
    	userEntry = readLine("Your guess: ");
    	
    	//confirm entry is only one character
    	if (userEntry.length() != 1) invalidEntry(userEntry);
    	
    	//convert to uppoercase
    	userEntry = userEntry.toUpperCase();
    	
    	//extract character from userEntry
    	guess = userEntry.charAt(0);
    	
    	//determine if the character is a letter
    	if (!(guess >= 'A' && guess <= 'Z')){
    		invalidEntry(userEntry);
    	}
    }
    
    /** Checks if the letter fits and update hidden word.
     * Confirmed Letter is a valid alphabetical character.
     * Letter has already been capitalized.
     * If letter matches send to WordState.
     * If letter does not match, remove a guess.
     */
    public void checkLetter(){
    	//tracking progress on hidden word.
    	String beforeFoundLetter = "";
    	String afterFoundLetter = "";
		String s = Character.toString(guess);
    	
    	//Keep track if there are matches or no matches.  
    	int correctGuessCounter = 0;
    	
    	//loop to check all the letters.  
    	for (int i = 0; i < wordLength; i++){
    		char ch = word.charAt(i);
    		Character letter = new Character(ch);
    		if (letter.equals(guess)){
    			beforeFoundLetter = hiddenWord.substring(0, i);
    			afterFoundLetter = hiddenWord.substring(i+1);
    			hiddenWord = beforeFoundLetter + s + afterFoundLetter;
    			correctGuessCounter++;
    		}
    	}
    	
		if (correctGuessCounter == 0) {
			println("There is no " + s + " in the word.");
			guessesRemaining -= 1;
			canvas.noteIncorrectGuess(guess, guessesRemaining);
		} else{
			println("That guess is correct!");
		}
    }
    
    /** Keeps Track if the user has completed the word.  
     * @return boolean
     */
    public boolean isWordCorrect(){
    	// If the hidden word contains a dash, you have not completed the word yet.  
    	return !(hiddenWord.contains("-"));
    }
    
    /** Checks if the user won or lost.  
     * Gives users the option to play again.
     */
    public void endState(){
    	if (isWordCorrect()) {
    		println("You guessed the word: " + word);
    		println("You Won!");
    		canvas.displayWord(hiddenWord);
    		hiddenWord = "";
    		word = "";
    	}
    	else {
    		println("You Lost :(");
    		println("The word was: " + word);
    		canvas.displayWord(word);
    		hiddenWord = "";
    		word ="";
    	}
    	int sentinel = readInt("Enter 0 to start over");
    	if (sentinel == 0) {
    		guessesRemaining = ATTEMPTS;
    		run();
    	}
    }
    
    // Error that exists within submitEntry function if user does not submit an acceptable letter.  
    public void invalidEntry(String str){
		println(str + " is not a valid entry.  Please enter a single letter");
		userEntry = "";
		submitLetter();
    }
    
    //Non-static Variables
    private HangmanLexicon lexicon = new HangmanLexicon();
    private HangmanCanvas canvas;
    private RandomGenerator rGen = RandomGenerator.getInstance();
    private int guessesRemaining = ATTEMPTS;
    private char guess;
    public String word;
    private int wordLength;
    private String hiddenWord = "";
    private String userEntry = null;
    
    
}
