/*
	Creator: 		Bud Linville
	Last updated: 	08/09/17
	Project Name: 	LOTR RISK PROBABILITY CALCULATOR
	Overview:		The goal of this program was to calculate the the ratios of every possible outcome (OUTPUT) given every possible
					dice-rolling scenario (INPUT) in the game of LOTR RISK.

					LOTR RISK follows the conventional RISK dice-rolling rules where the highest scoring dice (1-6) from the attacker 
					and defender are compared as well as the second highest scoring dice from the attacker and defender. The one 
					exception to normal RISK, however, is that in addition to the leaders (which give either the attacker or defender
					a +1 bonus to all dice rolls), there are also "strongholds", which if the defender's piece is sitting on, gives 
					the defender an additional +1 bonus. This allows for a potential (albeit unlikely) scenario in which the defender 
					has a +2 bonus. The following program tests for and lists the probablities of all possible outcomes given all 
					possible scenarios.
					
	NOTE:			Rather than having separate defender bonus and attacker bonus variables, it was simpler in implementation to have 
					one variable named "attBonus" to account for both. A negative value means that the defender has a defense bonus
					over the attack (either a -1 or -2). If this variable is set to 0, it means there is no bonus and both players
					roll standard dice with standard scoring, yielding results ranging from 1 to 6.
*/

import java.util.Random;

public class Prob {
	//global variables set by attack() method corresponding to the number of soldiers 
	//an attacker and a defender would lose in a game of LOTR risk
	//IMPORTANT to reset to 0 after its use has expired
	static int numAttLoses;
	static int numDefLoses;
	
	//generate a random number
	private static int getRandomNum(int min, int max) {
		Random rand = new Random();
		int  n = rand.nextInt(max) + min;
		
		return n;
	}
	
	public static void attack(int attBonus, int numAttDice, int numDefDice) {
		//variable corresponding to the two highest dice rolls by either an attacker or defender in the game of RISK
		//set to 0 by default. Even if the defender in real life only has 1 defender, there are always two defender and two attacker
		//dice compared. This is okay because the attacker's second dice will always be higher than 0.
		int highAttDice1 = 0;
		int highAttDice2 = 0;
		int highDefDice1 = 0;
		int highDefDice2 = 0;
		
		//temporary variable corresponding to a singular roll of a dice. If it is higher than the current highAttDice1 or highDefDice1,
		//there will be avalue shift as shown below.
		int roll = 0;
		
		//roll for 2 or 3 times (possibly 1, but probably not)
		for (int i = 0; i < numAttDice; i++) {
			if (attBonus == 1) {
				roll = getRandomNum(2,7);			//attack bonus is 1... roll with +1 bonus
			} else {
				roll = getRandomNum(1,6);			//all other situation, potentially with -1 attack bonus (which means defense gets +1 roll)
			}
			
			if (roll >= highAttDice1) {				//replace high score and second high score accordingly
				highAttDice2 = highAttDice1;		
				highAttDice1 = roll;
			}
		}
		
		for (int i = 0; i < numDefDice; i++) {
			if (attBonus == -1) {					//attack bonus could be negative, where the defense has a stronghold OR leader
				roll = getRandomNum(2,7);			
			} else if (attBonus == -2) {			//attack bonus coule be 2 x negative, where defense has a stronghold AND leader (unlikely)
				roll = getRandomNum(3,8);
			} else {								//all other situations, roll normally
				roll = getRandomNum(1,6);
			}
			
			if (roll >= highDefDice1) {				//replace high score and second high score accordingly
				highDefDice2 = highDefDice1;
				highDefDice1 = roll;
			}
		}
		
		//compares highest dice, and change global variables accordingly
		if (highAttDice1 > highDefDice1) {
			numDefLoses++;
		} else if (highAttDice1 == highDefDice1) {
			numDefLoses++;
			numAttLoses++;
		} else {
			numAttLoses++;
		}
		
		//compares second highest dice, and change global variables accordingly
		if (highAttDice2 > highDefDice2) {
			numDefLoses++;
		} else if (highAttDice2 == highDefDice2) {
			numDefLoses++;
			numAttLoses++;
		} else {
			numAttLoses++;
		}
	}

	public static void main(String[] args) {	
		for (int i = -2; i <= 1; i++) {			//corresponds to attack bonus
			for (int j = 1; j <= 3; j++) {		//corresponds to num attackers
				for (int k = 1; k <= 2; k++) {	//corresponds to num defenders
					
					//various states corresponding to the possible outcomes of a RISK dice-roll-off
					//not exactly a "clean" way of doing this, but it works.
					int stateA = 0; //att loses 0; def loses 0;
					int stateB = 0; //att loses 0; def loses 1;
					int stateC = 0;	//att loses 0; def loses 2;
					int stateD = 0; //att loses 1; def loses 0;
					int stateE = 0; //att loses 1; def loses 1;
					int stateF = 0; //att loses 1; def loses 2;
					int stateG = 0;	//att loses 2; def loses 0;
					int stateH = 0; //att loses 2; def loses 1;
					int stateI = 0; //att loses 2; def loses 2;
					
					//constant variable used multiple times through the program. 
					//corresponds to the amount of times a singular scenario (such as 2 attackers, 1 defender with an attack bonus of +1) is tested
					//The greater the number, the more accurate the corresponding overall outcome ratio is
					const int numTests = 100000;
				
					for (int l = 0; l < numTests; l++) {
						numAttLoses = 0;
						numDefLoses = 0;
						attack(i, j, k);
						
						switch (numAttLoses) {
							case 0 : switch (numDefLoses) {
								case 0 : stateA++; break;
								case 1 : stateB++; break;
								case 2 : stateC++; break; 
							}; break;
							
							case 1 : switch (numDefLoses) {
								case 0 : stateD++; break;
								case 1 : stateE++; break;
								case 2 : stateF++; break; 
							}; break;
							
							case 2 : switch (numDefLoses) {
								case 0 : stateG++; break;
								case 1 : stateH++; break;
								case 2 : stateI++; break; 
							}; break;
						} 
					}
					
					//echo results to page. A stands for num attackers lost. D stands for num defenders lost
					//echos the percentage chance of a particular outcome (OUTPUT) given a certain scenario (INPUT)
					System.out.println("Attack bonus: " + i + " Number of attackers: " + j + " Number of defenders: " + k);
					System.out.println("\t0A 0D: " + ((double)(stateA) / (double)(numTests) * 100) + " %");
					System.out.println("\t0A 1D: " + ((double)(stateB) / (double)(numTests) * 100) + " %");
					System.out.println("\t0A 2D: " + ((double)(stateC) / (double)(numTests) * 100) + " %");
					System.out.println("\t1A 0D: " + ((double)(stateD) / (double)(numTests) * 100) + " %");
					System.out.println("\t1A 1D: " + ((double)(stateE) / (double)(numTests) * 100) + " %");
					System.out.println("\t1A 2D: " + ((double)(stateF) / (double)(numTests) * 100) + " %");
					System.out.println("\t2A 0D: " + ((double)(stateG) / (double)(numTests) * 100) + " %");
					System.out.println("\t2A 1D: " + ((double)(stateH) / (double)(numTests) * 100) + " %");
					System.out.println("\t2A 2D: " + ((double)(stateI) / (double)(numTests) * 100) + " %");
					System.out.println("\n");
				}
			}
		}
	}
}