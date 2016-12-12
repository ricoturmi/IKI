/*
 * File: Yahtzee.java
 * ------------------
 * Rico Bakker
 * 11220821
 * 
 * The program is not complete as I have run out of time and dont know how to fix the scores and
 * the winning part. The rest works fine but that crucial part misses.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
    
    public static void main(String[] args) {
        new Yahtzee().start(args);
    }
    
    public void run() {
	currentPlayer = 1;
        IODialog dialog = getDialog();
	nPlayers = dialog.readInt("Enter number of players");
        playerNames = new String[nPlayers];
        for (int i = 1; i <= nPlayers; i++) {
            playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
        }
        display = new YahtzeeDisplay(getGCanvas(), playerNames);
	playerScores = new int[nPlayers][N_CATEGORIES];
	playGame();
		
		
		
		
			
	
    }
    /* This is the game loop   */
    private void playGame() {
        while(true) {
		
		rollDice();
		rerollDice();
		rerollDice();
		noteScore();
		switchPlayer();
		clearNumber();
		computeUpperScore();
		
	}
	
    }
   /*This rolls the dice and says who's turn it is */
    private void rollDice() {
	name = playerNames[currentPlayer-1];
	display.printMessage(name + "'s turn.");
	display.waitForPlayerToClickRoll(currentPlayer);
	for(int i=0; i<N_DICE;i++) {
		dice[i] = rgen.nextInt(1,6);
	display.displayDice(dice);
	
	}
    }
    /* This gives you the option to reroll  */
    private void rerollDice() {
	display.waitForPlayerToSelectDice();
	for(int i=0; i<N_DICE;i++) {
		if(display.isDieSelected(i)) {
			dice[i] = rgen.nextInt(1,6);
		}
	}
	display.displayDice(dice);
    }
    /* This large part computes the score per category and notes it down on the scorecard.  */
    private void noteScore() {
	category = display.waitForPlayerToSelectCategory();
	score = 0;
	nScore = playerScores[currentPlayer-1][category-1];
	
	
	switch(category) {
		case ONES: 
			numberOnDice = 1;
			onesToSixes(numberOnDice,score);
			playerScores[currentPlayer-1][category-1] = score;
			break;
			
		case TWOS: 
			numberOnDice = 2;
			onesToSixes(numberOnDice,score);
			playerScores[currentPlayer-1][category-1] = score;
			
			break;
		case THREES: 
			numberOnDice = 3;
			onesToSixes(numberOnDice,score);
			playerScores[currentPlayer-1][category-1] = score;
			
			break;

		case FOURS: 
			numberOnDice = 4;
			onesToSixes(numberOnDice,score);
			playerScores[currentPlayer-1][category-1] = score;
			break;

		case FIVES: 
			numberOnDice = 5;
			onesToSixes(numberOnDice,score);
			playerScores[currentPlayer-1][category-1] = score;
			break;
	
		case SIXES: 
			numberOnDice = 6;
			onesToSixes(numberOnDice,score);
			playerScores[currentPlayer-1][category-1] = score;
			break;
		case THREE_OF_A_KIND: 
			storeDice();
			
			for(int i=0;i<N_SIDES_DICE;i++) {
				if(number[i] >= 3) {
					for(int j=0;j<N_DICE;j++) {
						score += dice[j];
					}
				}
				
			}
			display.updateScorecard(category,currentPlayer,score);
			playerScores[currentPlayer-1][category-1] = score;

			
			break;	
		case FOUR_OF_A_KIND: 
			storeDice();
			for(int i=0;i<N_DICE;i++) {
				score += dice[i];
			}
			for(int i=0;i<N_SIDES_DICE;i++) {
				if(number[i] >= 4) {
					display.updateScorecard(category,currentPlayer,score);
					}
				
			}
			playerScores[currentPlayer-1][category-1] = score;
			break;
		case FULL_HOUSE: 
			storeDice();
			for(int i=0;i<N_DICE;i++) {
				for(int j=0;j<N_DICE;j++) {
					if(number[i] == 3 && number[j] == 2 && 
					  number[i] != number[j]) {
						score = 25;
						
					}
					
				}
			}
			display.updateScorecard(category,currentPlayer,score);
			playerScores[currentPlayer-1][category-1] = score;
			break;
				
		case SMALL_STRAIGHT: 
			storeDice();
			for(int i=0; i<N_DICE;i++) {
				if(number[i] >= 1) {
					if(number[i+1] >= 1) {
						if(number[i+2] >= 1) {
							if(number[i+3] >= 1) {
								score = 30;
								
							}
						}
					}
				}
			}
			display.updateScorecard(category,currentPlayer,score);
			playerScores[currentPlayer-1][category-1] = score;
			break;
		case LARGE_STRAIGHT:
			storeDice();
			for(int i=0; i<N_DICE;i++) {
				if(number[i] >= 1) {
					if(number[i+1] >= 1) {
						if(number[i+2] >= 1) {
							if(number[i+3] >= 1) {
								if(number[i+4] >= 1) {
									score = 40;
								}
							}
								
						
						}
					}
				}
			
			}
			display.updateScorecard(category,currentPlayer,score);
			playerScores[currentPlayer-1][category-1] = score;
			break;
		case YAHTZEE:
			storeDice();
			for(int i=0;i<N_SIDES_DICE;i++) {
				if(number[i] >= 5) {
					score = 50;
					}
			}
			display.updateScorecard(category,currentPlayer,score);
			playerScores[currentPlayer-1][category-1] = score;
			break;
		case CHANCE: 
			chance();
			
			break;	
	}
	
	


    }
    /* This switches the player to the next one and if it reaches over the meximum it goes back
       to player 1 */
    private void switchPlayer() {
	
	if(currentPlayer == nPlayers) {
		currentPlayer = 1;
	}
	else {
		currentPlayer++;
	}
		
    }
    /* The number array needs to be set to zero everytime as this functions as a counter for 
       the amount of times a number is rolled on a dice.  */
    private void clearNumber() {
	for(int i=0;i<N_SIDES_DICE;i++) {
		number[i] = 0;
		}
    }
    /* This number array is the amount of times a number is rolled and stores that and uses it 
       uses it in the computation of the score.  */
    private void storeDice() {
	
	for(int i=0;i<N_DICE;i++) {
		if(dice[i] == 1) {
			number[0] += 1;
		}
		if(dice[i] == 2) {
			number[1] += 1;
		}
		if(dice[i] == 3) {
			number[2] += 1;
		}
		if(dice[i] == 4) {
			number[3] += 1;
		}
		if(dice[i] == 5) {
			number[4] += 1;
		}
		if(dice[i] == 6) {
			number[5] += 1;
		}
		
	}
    }
    /* This is the storeDice() but computes the score within it. */
    private void chance() {
	
	for(int i=0;i<N_DICE;i++) {
		if(dice[i] == 1) {
			number[0] += 1;
		}
		if(dice[i] == 2) {
			number[1] += 1;
		}
		if(dice[i] == 3) {
			number[2] += 1;
		}
		if(dice[i] == 4) {
			number[3] += 1;
		}
		if(dice[i] == 5) {
			number[4] += 1;
		}
		if(dice[i] == 6) {
			number[5] += 1;
		}
		score += dice[i];
		
	}
	display.updateScorecard(category,currentPlayer,score);
    }

    /* This computes the upperscore but it doesnt work yet. I couldnt make it so that it only computes when the game is over.   */
    private void computeUpperScore() {
	
		for(int i=0;i<nPlayers;i++) {
			for(int j=0;j<6;j++) {
					upperScore += playerScores[i][j];
					
			}
			display.updateScorecard(UPPER_SCORE,i+1,upperScore);
		}
    }

  /*  privatevoid computeLowerScore() {
		for(int i=0;i<nPlayers;i++) {
			for(int j=9;j<16;j++) {
				if(playerScores[i][j] >= 0) {
					lowerScore += playerScores[i][j];
				}
			}
			display.updateScorecard(LOWER_SCORE,i,lowerScore);
		}
		
    }
	*/
			
			
    /*  This is the code to compute the scores for the category ONES to SIXES  */
    private void onesToSixes(int numberOnDice,int score) {
	for(int i=0;i<N_DICE;i++) {
		if(dice[i] == numberOnDice) {
			score += numberOnDice;
		}
	}
	display.updateScorecard(category,currentPlayer,score);
			
    }
    
	
	
        
    // Private instance variables
    private int nPlayers;
    private String[] playerNames;
    private int[] dice = new int[N_DICE];
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
    private int currentPlayer = 1;
   
    private int category;
    private int score = 0;
    
    private int[] number = new int[N_SIDES_DICE];
 
    private String name; 
    private int numberOnDice; /* this number is used in the one to sixes categories to see if the 
    number is the wanted number for the category     */
    private int upperScore;
    private int[][] playerScores; /* This two dimensional array was supposed to store the scores of the category and it does that I think but i couldnt add up those numbers in the actual method */
    private int nScore;
    

}
