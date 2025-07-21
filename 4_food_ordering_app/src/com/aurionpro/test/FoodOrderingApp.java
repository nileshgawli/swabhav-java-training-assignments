package com.aurionpro.test;
import java.util.Scanner;

import com.aurionpro.model.Customer;
import com.aurionpro.service.AdminService;
import com.aurionpro.service.CustomerService;
import com.aurionpro.service.DeliveryPartnerService;
import com.aurionpro.service.OrderService;
import com.aurionpro.ui.AdminUI;
import com.aurionpro.ui.AuthManager;
import com.aurionpro.ui.CustomerUI;
import com.aurionpro.ui.InputUtil;

public class FoodOrderingApp {

 private Scanner scanner;
 private AuthManager authManager;
 private AdminUI adminUI;
 private CustomerUI customerUI;

 public FoodOrderingApp() {
     scanner = new Scanner(System.in);
     AdminService.getInstance();
     CustomerService.getInstance();
     DeliveryPartnerService.getInstance();
     OrderService.getInstance(); 

     authManager = new AuthManager(scanner);
     adminUI = new AdminUI(scanner);
     customerUI = new CustomerUI(scanner);
 }

 public static void main(String[] args) {
     FoodOrderingApp app = new FoodOrderingApp();
     app.run();
 }

 public void run() {
     System.out.println("Welcome to the Console-Based Food Ordering System!");

     int choice;
     do {
         System.out.println("\nMain Menu:");
         System.out.println("1. Admin Login");
         System.out.println("2. Customer Login");
         System.out.println("3. Customer Register");
         System.out.println("4. Exit");
         System.out.print("Enter your choice: ");

         choice = InputUtil.getIntegerInput(scanner);
         if (choice == -1) { continue; }

         switch (choice) {
             case 1:
                 if (authManager.adminLogin()) {
                     adminUI.startAdminSession();
                 }
                 break;
             case 2:
                 Customer customer = authManager.customerLogin();
                 if (customer != null) {
                     customerUI.startCustomerSession(customer);
                 }
                 break;
             case 3:
                 registerCustomer();
                 break;
             case 4:
                 System.out.println("Exiting Food Ordering System. Goodbye!");
                 break;
             default:
                 System.out.println("Invalid choice. Please try again.");
         }
     } while (choice != 4);

     scanner.close();
 }

 private void registerCustomer() {
     System.out.println("\n--- Customer Registration ---");
     String name = InputUtil.getStringInput(scanner, "Enter your name: ");
     String contact = InputUtil.getOptionalStringInput(scanner, "Enter your contact number (optional): ");
     String address = InputUtil.getOptionalStringInput(scanner, "Enter your address (optional): ");

     Customer newCustomer = CustomerService.getInstance().registerCustomer(name, contact, address);
     if (newCustomer != null) {
         System.out.println("Registration successful! Your Customer ID is: " + newCustomer.getId());
     } else {
         System.out.println("Customer registration failed.");
     }
 }
}