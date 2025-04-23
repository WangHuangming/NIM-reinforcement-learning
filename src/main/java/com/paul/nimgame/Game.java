package com.paul.nimgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private int numOfSticks;
    private int maxPick;
    private LearningTable learningTable;
    
    public Game(int startingSticks, int maxPick) {
        numOfSticks= startingSticks;
        this.maxPick = maxPick;
        DBHelper dbHelper = DBHelper.getInstance(this);
        learningTable = dbHelper.readTable();
        if(learningTable == null) {
            dbHelper.createTable();
            learningTable = dbHelper.readTable();
        }
        
    }

    public int getStartingSticks() {
        return numOfSticks;
    }

    public int getmMaxPick() {
        return maxPick;
    }

    public int setStartingSticks(int startingSticks) {
        return numOfSticks = startingSticks;
    }

    public void greeting() {
        System.out.println("Welcome to NIM.");
        System.out.println("There are " + numOfSticks + " sticks in the pile.");
        System.out.println("You can pick up to " + maxPick + " sticks once at a time.");
        System.out.println("The computer will go first.");
        System.out.println("If you pick up the last stick, you win.");
        System.out.println("If the computer picks up the last stick, you lose.");
    }

    public void play(Scanner myScanner){
        List<String> locations= new ArrayList<>();
        boolean isWin= false;
        while (numOfSticks != 0) {
            // computer's turn
            System.out.println("-------------------------------------------");
            System.out.println("It is computer's turn");

            int col= numOfSticks-1;
            int row= learningTable.optimalIndex(col);
            int computerPick = row+1;
            locations.add(row+","+col);
            numOfSticks = numOfSticks - computerPick;
            System.out.println("Computer picks up " + computerPick + " sticks");
            System.out.println("There are " + numOfSticks + " sticks left");
            if (numOfSticks == 0) {
                System.out.println("Computer won!");
                isWin = true;
                break;
            }

            // human's turn
            System.out.println("-------------------------------------------");
            System.out.println("It is Your turn");
            System.out.println("Enter the num of sticks you want.");
            
            int userPick = myScanner.nextInt();
            if (userPick < 1 || userPick > maxPick) {
                System.out.println("You violate the rule!!!!!");
                System.out.println("YOU LOSE!!!!!");
                return;
            }

            numOfSticks = numOfSticks - userPick;
            System.out.println("You pick up " + userPick + " sticks");
            System.out.println("There are " + numOfSticks + " sticks left");
            if (numOfSticks == 0) {
                System.out.println("YOU WON!");
                isWin = false;
                break;
            }
        }
        learn(learningTable, locations, isWin);
    }

    public void learn(LearningTable table, List<String> locations, boolean isWin) {
        DBHelper dbHelper = DBHelper.getInstance(this);
        for (String location : locations) {
            String[] parts = location.split(",");
            int row = Integer.parseInt(parts[0]);
            int column = Integer.parseInt(parts[1]);
            if (isWin) {
                table.setValue(row, column, table.getValue(row, column) + 1);
                dbHelper.updateTable(table, row, column, table.getValue(row, column));
            } else {
                table.setValue(row, column, table.getValue(row, column) - 1);
                dbHelper.updateTable(table, row, column, table.getValue(row, column));
            }
        }
    }


    public static void main(String[] args) {
        // set up Scanner
        Scanner myScanner = new Scanner(System.in);
        boolean playAgain = false;
        Game game = new Game(10, 3);
        game.greeting();
        do{
            
            game.play(myScanner);

            System.out.println("Do you want to play again? (Y/N)");
            String answer = myScanner.next();
            if(answer.equalsIgnoreCase("Y")) {
                playAgain = true;
                game.setStartingSticks(10);
            } else if(answer.equalsIgnoreCase("N")) {
                playAgain = false;
            } else {
                System.out.println("Invalid input. Please enter Y or N.");
            }
            // reset the game
            
        } while(playAgain);

        System.out.println("Thank you for playing NIM.");
        System.out.println("Goodbye.");
        myScanner.close();
    }
}