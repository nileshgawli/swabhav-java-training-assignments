package com.aurionpro.model;

import java.util.Random;
import java.util.Scanner;

public class GuessTheWord {

	public static void main(String[] args) {
		Random random = new Random();
		Scanner scanner = new Scanner(System.in);
		int lives = 6;

		String[] array = { "apple", "mango", "banana", "cherry" };
		String word = array[random.nextInt(array.length)];
		System.out.println("*** Word to be guessed: " + word + " *** \n");
		
		char[] blanks = new char[word.length()];
		for (int i = 0; i < blanks.length; i++) {
			blanks[i] = '_';
		}

		while (true) {
			
			System.out.print("\nWord: ");
			for(int i=0; i<blanks.length; i++) {
				System.out.print(blanks[i] + " ");
			}
			
			System.out.print("\nGuess a letter: ");
			char guessedLetter = scanner.next().toLowerCase().charAt(0);

			boolean found = false;
			for (int i = 0; i < word.length(); i++) {
				if (guessedLetter == word.charAt(i) && blanks[i] == '_') {
					blanks[i] = guessedLetter;
					found = true;
				}
			}

			if (!found) {
				lives--;
				System.out.println("Wrong guess.");
			}

			boolean allFilled = true;
			for (int i = 0; i < blanks.length; i++) {
				if (blanks[i] == '_') {
					allFilled = false;
					break;
				}
			}

			if (allFilled) {
				System.out.println("You guessed the word correct: " + word + "\nGame Over!!!");
				break;
			}

			if (lives == 0) {
				System.out.println("You run out of lives!! The word was: " + word + "\nGame Over!!!");
				break;
			}
		}

	}

}
