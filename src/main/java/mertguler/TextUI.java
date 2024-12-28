package mertguler;

import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;

import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class TextUI {
    private Scanner scanner;
    private CRS crs;

    public TextUI(Scanner scanner, CRS crs) {
        this.scanner = scanner;
        this.crs = crs;
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
                adminMenu();
            }
        }
    }

    public void adminMenu() {
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
                hospitalManager();
            }
        }
    }

    public void hospitalManager() {
        while (true) {
            clear();
            header();
            int input = 9;
            System.out.println("\nAdd Hospital: 1");
            System.out.println("Delete Hospital: 2");
            System.out.println("Rename Hospital: 3");
            System.out.println("List Hospitals: 4");
            System.out.println("Return to last menu: 0");

            System.out.println("\nSelect operating mode (1-4):");
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
            }

            if (input == 1) {
                hospitalAdder();
            } else if (input == 2) {
                hospitalDeleter();
            } else if (input == 3) {
                hospitalRenamer();
            } else if (input == 4) {
                hospitalLister();
            }
        }
    }

    public void hospitalAdder(){
        clear();
        header();
        int id = 0;
        System.out.println("\nEnter name: ");
        String name = scanner.nextLine();
        if (name.isEmpty()){
            System.out.println("Please enter valid names only");
            System.out.println("Press anything to return");
            scanner.nextLine();
            return;
        }
        System.out.println("Enter ID");

        try {
            id = Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException e){
            System.out.println("Please enter only valid numbers");
            System.out.println("\nPress anything to return");
            scanner.nextLine();
            return;
        }

        try {
            crs.createHospital(name,id);
            System.out.println("Hospital successfully added.");
        } catch (IDException e) {
            System.out.println("Hospital with this id already exists");
        }

        System.out.println("\nPress anything to return");
        scanner.nextLine();
    }

    public void hospitalDeleter(){
        clear();
        header();
        int id = 0;
        System.out.println("Enter the ID: ");

        try {
            id = Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException e){
            System.out.println("Please enter only valid numbers");
            System.out.println("\nPress anything to return");
            scanner.nextLine();
            return;
        }

        if(!(crs.getHospitals().containsKey(id))){
            System.out.println("Hospital with ID: " + id + " is not found.");
            System.out.println("\nPress anything to return");
            scanner.nextLine();
            return;
        }

        crs.getHospitals().remove(id);
        System.out.println("Hospital successfully deleted.");
        System.out.println("Press anything to return");
        scanner.nextLine();
    }

    public void hospitalRenamer(){
        clear();
        header();
        int id = 0;
        System.out.println("\nEnter the ID: ");

        try {
            id = Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException e){
            System.out.println("Please enter only valid numbers");
            System.out.println("\nPress anything to return");
            scanner.nextLine();
            return;
        }

        if(!(crs.getHospitals().containsKey(id))){
            System.out.println("Hospital with ID: " + id + " is not found.");
            System.out.println("\nPress anything to return");
            scanner.nextLine();
            return;
        }

        System.out.println("Enter new name: ");
        String name = scanner.nextLine();

        crs.getHospitals().get(id).setName(name);
        System.out.println("Hospital successfully renamed to: " + name);
        System.out.println("Press anything to return");
        scanner.nextLine();
    }

    public void hospitalLister(){
        clear();
        header();
        if (crs.getHospitals().isEmpty()){
            System.out.println("\nHospital list is empty.");
            System.out.println("Press anything to return");
            scanner.nextLine();
            return;
        }

        Comparator<Hospital> comparator = Comparator
                .comparing(Hospital::getId);

        crs.getHospitals().values().stream()
                                .sorted(comparator)
                                        .forEach(System.out::println);

        System.out.println("Press anything to return");
        scanner.nextLine();
    }

    public void header(){
        System.out.println("Hospital Rendezvous System - Text UI V1.0");
    }

    public void clear(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
