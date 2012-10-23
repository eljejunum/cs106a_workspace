/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	   (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;
 
/** Number of turns */
	private static final int NTURNS = 3;
	
/** Set the gravity for the game */
	private static final double GRAVITY = 3;

/** Animation Delay (in milliseconds) */
	private static final int DELAY = 10;
	
	
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {		
		//set the stage and all the game objects.  
		setup();
		
		//run the game and all the controls.
		play(NTURNS);
		
		//Options for end of game.
		//gameEnd();		
	}
	
	/**Sets the stage for the game. Contains three components to initialize game objects plus addes mouse listeners*/
	public void setup(){
		bricks();
		initializePaddle();
		initializeBall();
		//initializeGUI();
		addMouseListeners();
	}
	
		/** Builds out the rows and columns of bricks*/
		public void bricks(){
			brickCounter = 0;
			//lay out the bricks
			for(int i = 0; i < NBRICK_ROWS; i++){
				for(int j = 0; j < NBRICKS_PER_ROW; j++){
					int brickX = j * (BRICK_SEP + BRICK_WIDTH) + BRICK_SEP/2;
					int brickY = BRICK_Y_OFFSET + (i * (BRICK_SEP + BRICK_HEIGHT)); 
					brick = new GRect(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
					brick.setFilled(true);
					brick.setColor(Color.WHITE);
					
					//Color in the bricks
					if(i < 2) {brick.setFillColor(Color.RED);}
					else if(i < 4) {brick.setFillColor(Color.ORANGE);}
					else if(i < 6) {brick.setFillColor(Color.YELLOW);}
					else if(i < 8) {brick.setFillColor(Color.GREEN);}
					else {brick.setFillColor(Color.CYAN);}
					add(brick);
					brickCounter++;
				}
			}
		}
	
		// Setting up the paddle
		public void initializePaddle(){
			//intialize paddle
			paddleX = cX -  PADDLE_WIDTH / 2;
			paddleY = HEIGHT - PADDLE_Y_OFFSET;
			paddle = new GRect(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
			paddle.setFilled(true);
			paddle.setFillColor(Color.BLACK);
			add(paddle);
		}
	
		//putting the ball on the stage.  
		public void initializeBall(){
			//place ball
			double ballX = cX - BALL_RADIUS;
			double ballY = cY + BALL_RADIUS;
			ball = new GOval(ballX, ballY, BALL_RADIUS, BALL_RADIUS);
			ball.setFilled(true);
			ball.setFillColor(Color.BLACK);
			add(ball);
			vy = GRAVITY;
		}
		
		public void initializeGUI(){
			GLabel GUI = new GLabel ("Lives:" + lives);
			double GUIx = WIDTH - GUI.getWidth();
			double GUIy = GUI.getAscent();
			GUI.setLocation(GUIx, GUIy);
			add(GUI);
		}
		
	public void play(int nturns){
		lives = nturns;
		while(!gameOver()){
			paddleMovement();
			moveBall();
			checkCollision();
			debug();
			pause(DELAY);
		}
	}
	
		/** end the game if player runs out of lives or destroys all the bricks*/
		public boolean gameOver(){
			return (lives < 0 || brickCounter == 0);
		}
	
		//get a variable to track the mouse.  
		public void mouseMoved(MouseEvent e){
			mouseX = e.getX();
		}
		
		/** link the paddle to the mouse X position and limit so that the paddle does not go off the screen.  */
		public void paddleMovement(){
			int paddleLeftLimit = (PADDLE_WIDTH/2);
			int paddleRightLimit = WIDTH - (PADDLE_WIDTH/2);
			if(mouseX <= paddleLeftLimit) {
				paddleX = 0;
			}else if(mouseX >= paddleRightLimit) {
				paddleX = WIDTH - PADDLE_WIDTH;
			}else{ 
				paddleX = (int) mouseX - (PADDLE_WIDTH / 2); 
			
				//reset the paddle position
				paddle.setLocation(paddleX, paddleY);
			}
		}
	
		public void moveBall(){
			ball.move(vx, vy);
		}
	
		public void checkCollision(){
			//establish outer boundaries of the ball for collisions.  
			ballLeftBorder = ball.getX();
			ballRightBorder = ballLeftBorder + 2*BALL_RADIUS;
			ballTopBorder = ball.getY();
			ballBottomBorder = ballTopBorder + 2*BALL_RADIUS;
			
			//set up an object to track if the ball has hit anything
			collider = getSurfaceCollider();
			
			collideWithSurfaces();
			collideWithObjects();
		}
			
		
		
		
			private GObject getSurfaceCollider(){
				GObject hitObject;
				
				GPoint ballTop = new GPoint();
				ballTop.setLocation(ballLeftBorder, ballTopBorder);
				
				GPoint ballLeft = new GPoint();
				ballLeft.setLocation(ballLeftBorder, ballBottomBorder);
				
				GPoint ballRight = new GPoint();
				ballRight.setLocation(ballRightBorder, ballTopBorder);
				
				GPoint ballBottom = new GPoint();
				ballBottom.setLocation(ballRightBorder, ballBottomBorder);
				
				if (getElementAt(ballTop) != null){
					hitObject = getElementAt(ballTop);
				}else if (getElementAt(ballLeft) != null){
					hitObject = getElementAt(ballLeft);
				}else if (getElementAt(ballRight) != null){
					hitObject = getElementAt(ballRight);
				}else if (getElementAt(ballBottom) != null){
					hitObject = getElementAt(ballBottom);
				}else{
					hitObject = null;
				}
				
				return hitObject;
				
			}
		
			public void collideWithSurfaces(){
				if (ballTopBorder < 0){
					vy = -vy;
					vx = rgen.nextDouble(1,3);
					if(rgen.nextBoolean(.5)) vx = -vx;
				} else if(ballLeftBorder < 0 || ballRightBorder > WIDTH){
					vx = -vx;
				} else if(ballBottomBorder > HEIGHT) {
					remove(ball);
					lives--;
					GLabel loseLife = new GLabel("you suck. " + lives + " tries remaining.", cX, cY);
					add(loseLife);
					pause(2000);
					remove(loseLife);
					initializeBall();
				}
			}
			
			public void collideWithObjects(){
				if (collider == paddle){
					vy = -vy;
					vx = rgen.nextDouble(1,3);
					if(rgen.nextBoolean(.5)) vx = -vx;
				} else if (collider != null){
					vy = -vy;
					vx = rgen.nextDouble(1,3);
					if(rgen.nextBoolean(.5)) vx = -vx;
					brickCounter--;
					remove(collider);
				}
			}
			
		public void debug(){
			println("collided object: " + collider);
			println(brickCounter);
		}
			
	//establish all game objects
	GRect brick;
	GRect paddle;
	GOval ball;
	GLine wall;
	GLine ceiling;
	GLine floor;
	GObject collider;
	
	//convenient center variables
	double cX = WIDTH/2;
	double cY = HEIGHT/2;
	
	//ivar for end game scenarios
	int lives;
	int brickCounter;
	
	//ball elements 
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	double ballLeftBorder;
	double ballRightBorder;
	double ballTopBorder;
	double ballBottomBorder;
	
	//paddle positoning
	double paddleX;
	double paddleY;
	double mouseX;	
	


}

