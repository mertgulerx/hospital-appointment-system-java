package mertguler.TextUI.AdminMenu;

import mertguler.CRS;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;

import java.util.Scanner;

import static mertguler.TextUI.TextUI.clear;
import static mertguler.TextUI.TextUI.header;

public class DoctorMenu {
    private Scanner scanner;
    private CRS crs;
    private Section section;

    public DoctorMenu(Scanner scanner, CRS crs){
        this.scanner = scanner;
        this.crs = crs;
    }

    public void doctorManager(){
        HospitalMenu hospitalMenu = new HospitalMenu(scanner, crs);
        Hospital hospital = hospitalMenu.hospitalSelector();
        SectionMenu sectionMenu = new SectionMenu(scanner,crs, hospital);
        section = sectionMenu.sectionSelector();

        if (section == null){
            return;
        }

        while (true) {
            clear();
            header();
            int input = 9;
            System.out.println("\n" + "Hospital:" + hospital.getName() + ", ID: " + hospital.getId());
            System.out.println("Section: " + section.getName() + ", ID: " + section.getId() + "\n");
            System.out.println("Add Doctor: 1");
            System.out.println("Delete Doctor: 2");
            System.out.println("List Doctors: 3");
            System.out.println("Return to last menu: 0");
            System.out.println("\nSelect operating mode (1-3):");
            try {
                input = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers within range");
                returner();
                continue;
            }

            if (input == 0) {
                break;
            } else if (input == 1){
                doctorAdder();
            } else if (input == 2){
                doctorDeleter();
            } else if (input == 3) {
                clear();
                header();
                section.listDoctors();
                returner();
            }
        }
    }

    public boolean doctorAdder(){
        while (true){
            clear();
            header();
            String name = "";
            long national_id = 0;
            int diploma_id = 0;

            System.out.println("Enter National ID: ");

            try {
                national_id = Long.valueOf(scanner.nextLine());
            } catch (Exception e){
                System.out.println("Enter valid numbers only");
                returner();
                return false;
            }

            System.out.println("Enter Diploma ID: ");

            try{
                diploma_id = Integer.valueOf(scanner.nextLine());
            } catch (Exception e){
                System.out.println("Enter valid numbers only");
                returner();
                return false;
            }

            System.out.println("Enter Name: ");

            try{
                name = scanner.nextLine();
            } catch (Exception e){
                System.out.println("Enter valid characters only");
                returner();
                return false;
            }

            try{
                section.addDoctor(new Doctor(name, national_id, diploma_id));
                System.out.println("Doctor is successfully added");
                returner();
                return true;
            } catch (DuplicateInfoException e) {
                System.out.println(e.getMessage());
                returner();
                return false;
            }
        }
    }

    public boolean doctorDeleter(){
        while (true){
            clear();
            header();
            int diploma_id = 0;

            System.out.println("Enter Diploma ID: ");

            try{
                diploma_id = Integer.valueOf(scanner.nextLine());
            } catch (Exception e){
                System.out.println("Enter valid numbers only");
                returner();
                return false;
            }

            if (section.deleteDoctor(diploma_id)){
                System.out.println("Doctor is successfully deleted");
                returner();
                return true;
            } else {
                System.out.println("Doctor with Diploma ID: " + diploma_id + " is not found");
                returner();
                return false;
            }

        }
    }

    public void returner(){
        System.out.println("\nPress anything to return");
        scanner.nextLine();
    }
}
