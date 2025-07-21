package com.aurionpro.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputUtil {

 public static int getIntegerInput(Scanner scanner) {
     while (true) {
         try {
             int value = scanner.nextInt();
             scanner.nextLine(); 
             return value;
         } catch (InputMismatchException e) {
             System.out.println("Invalid input. Please enter a valid number.");
             scanner.nextLine(); 
             return -1; 
         }
     }
 }

 public static double getDoubleInput(Scanner scanner) {
     while (true) {
         try {
             double value = scanner.nextDouble();
             scanner.nextLine(); 
             return value;
         } catch (InputMismatchException e) {
             System.out.println("Invalid input. Please enter a valid decimal number.");
             scanner.nextLine(); 
             return -1.0;
         }
     }
 }


 public static String getStringInput(Scanner scanner, String prompt) {
     String input;
     do {
         System.out.print(prompt);
         input = scanner.nextLine().trim();
         if (input.isEmpty()) {
             System.out.println("Input cannot be empty. Please try again.");
         }
     } while (input.isEmpty());
     return input;
 }

 public static String getOptionalStringInput(Scanner scanner, String prompt) {
     System.out.print(prompt);
     return scanner.nextLine().trim();
 }
}