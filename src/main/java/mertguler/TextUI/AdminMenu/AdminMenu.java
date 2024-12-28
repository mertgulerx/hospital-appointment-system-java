package mertguler.TextUI.AdminMenu;

import java.util.Scanner;

import static mertguler.TextUI.TextUI.clear;
import static mertguler.TextUI.TextUI.header;

public class AdminMenu {
    private Scanner scanner;
    private HospitalMenu hospitalMenu;

    public AdminMenu(Scanner scanner, HospitalMenu hospitalMenu){
        this.scanner = scanner;
        this.hospitalMenu = hospitalMenu;
    }

    public void start() {
        while (true) {
            clear();
            header();
            int input = 9;
            System.out.println("\nHospital Management: 1");
            System.out.println("Section Management: 2");
            System.out.println("Doctor Management: 3");
            System.out.println("Patient Management: 4");
            System.out.println("Rendezvous Management: 5");
            System.out.println("Return to last menu: 0");
            System.out.println("\nSelect operating mode (1-5):");
            try {
                input = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers within range");
                System.out.println("\nPress anything to return");
                scanner.nextLine();
                continue;
            }

            if (input == 0) {
                break;
            } else if (input == 1) {
                hospitalMenu.hospitalManager();
            }
        }
    }
}
