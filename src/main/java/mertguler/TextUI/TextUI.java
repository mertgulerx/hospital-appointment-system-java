package mertguler.TextUI;

import mertguler.CRS.CRS;
import mertguler.TextUI.AdminMenu.AdminMenu;

import java.util.Scanner;

public class TextUI {
    private Scanner scanner;
    private CRS crs;
    private final AdminMenu adminMenu;

    public TextUI(Scanner scanner, CRS crs) {
        this.scanner = scanner;
        this.crs = crs;
        adminMenu = new AdminMenu(scanner, crs);
    }

    public void start() {
        mainMenu();
    }

    public void mainMenu() {
        while (true) {
            clear();
            header();
            int input = 9;
            System.out.println("\nAdmin Mode: 0");
            System.out.println("User Mode: 1");

            System.out.println("\nSelect operating mode (0/1):");

            try {
                input = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only 0 or 1.");
                System.out.println("\nPress anything to return");
                scanner.nextLine();
                continue;
            }

            if (input == 0) {
                adminMenu.start();
            }
        }
    }

    public static void header() {
        System.out.println("Hospital Rendezvous System - Text UI V1.0");
    }

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static long nationalIDSelector(Scanner scanner) {
        clear();
        header();
        long national_id = 0;

        System.out.println("\nEnterNational ID: ");

        try {
            national_id = Long.valueOf(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("\nPlease enter only valid numbers\n");
            returner(scanner);
            clear();
            return 0;
        }

        return national_id;
    }

    public static void returner(Scanner scanner) {
        System.out.println("Press anything to return");
        scanner.nextLine();
    }

}
