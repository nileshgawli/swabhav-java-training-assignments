package com.aurionpro.model;

import java.util.Random;
import java.util.Scanner;

public class PigGame {
    private boolean turnOver;
    private int turnCount;
    private int turnScore;
    private int totalScore;

    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();

    public void startGame() {
        System.out.println("*** Welcome to Pig Die Game ***");
        System.out.println("---------------------------------");
        while (totalScore < 20) {
            playTurn();
        }
        System.out.println("\nYou finished in " + turnCount + " turns.");
        System.out.println("Game over.");
    }

    private void playTurn() {
        turnCount++;
        turnOver = false;
        turnScore = 0;

        System.out.println("\n*** TURN " + turnCount + " ***");

        while (!turnOver) {
            System.out.print("Roll or hold? (r/h): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "r" -> rollDice();
                case "h" -> holdTurn();
                default -> System.out.println("Invalid input. Please enter 'r' to roll or 'h' to hold.");
            }
        }
        System.out.println();
    }

    private void rollDice() {
        int roll = random.nextInt(6) + 1;
        System.out.println("Die rolled: " + roll);

        if (roll == 1) {
            System.out.println("Rolled a 1. Turn over. No score for this turn.");
            turnScore = 0;
            turnOver = true;
        } else {
            turnScore += roll;
        }
    }

    private void holdTurn() {
        totalScore += turnScore;
        System.out.println("Score for this turn: " + turnScore);
        System.out.println("Total score: " + totalScore);
        turnOver = true;
    }
}