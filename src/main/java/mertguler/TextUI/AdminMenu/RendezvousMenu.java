package mertguler.TextUI.AdminMenu;

import mertguler.CRS.CRS;
import mertguler.CRS.DateManager;
import mertguler.Exceptions.DailyLimitException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


import static mertguler.TextUI.TextUI.clear;
import static mertguler.TextUI.TextUI.header;

public class RendezvousMenu {
    private Scanner scanner;
    private CRS crs;
    private HospitalMenu hospitalMenu;
    private SectionMenu sectionMenu;
    private DoctorMenu doctorMenu;

    public RendezvousMenu(Scanner scanner, CRS crs, HospitalMenu hospitalMenu, SectionMenu sectionMenu,
                          DoctorMenu doctorMenu){
        this.scanner = scanner;
        this.crs = crs;
        this.hospitalMenu = hospitalMenu;
        this.sectionMenu = sectionMenu;
        this.doctorMenu = doctorMenu;
    }

    public void rendezvousManager(){
        while (true){
            clear();
            header();
            int input = 9;

            System.out.println("\nAdd Rendezvous: 1");
            System.out.println("Delete Rendezvous: 2");
            System.out.println("Check Rendezvous Info: 3");
            System.out.println("Return to last menu: 0");
            System.out.println("\nSelect operating mode (1-3):");

            try {
                input = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers within range");
                returner();
                continue;
            }

            if (input == 0){
                return;
            } else if (input == 1){
                rendezvousAdder();
            } else if (input == 2){
                // rendezvousDeleter();
            } else if (input == 3){
                // rendezvousInfoChecker();
            }
        }
    }

    public boolean rendezvousAdder(){
        while (true){
            clear();
            header();
            int input = 9;
            long patient_id = 0;

            System.out.println("Enter Patient National ID: ");

            try {
                patient_id = Long.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers");
                returner();
                return false;
            }

            Hospital hospital = hospitalMenu.hospitalSelector();

            // Check this part
            if (hospital == null){
                return false;
            }

            sectionMenu.setHospital(hospital);

            Section section = sectionMenu.sectionSelector();

            // Check this part
            if (section == null){
                return false;
            }

            Doctor doctor = doctorMenu.doctorSelector(section);

            if (doctor == null){
                return false;
            }

            LocalDate currentDate = DateManager.getCurrentDate();

            System.out.println("Hospital: " + hospital);
            System.out.println("Section: " + section);
            System.out.println("Doctor: " + doctor);
            System.out.println("Current Time: " + DateManager.getFormatedDate(currentDate));

            System.out.println("\nYou can create rendezvous for next " + DateManager.rendezvousDayLimit + " days");
            LocalDate lastRendezvousDate = DateManager.getLastDate();
            System.out.println("Last Date: " + DateManager.getFormatedDate(lastRendezvousDate));

            System.out.println("\nPlease enter desired date as dd-mm-yyyy format");
            System.out.println("Enter date within the day limit");

            String desiredDateStr = "";

            try {
                desiredDateStr = scanner.nextLine();
            } catch (Exception e){
                System.out.println("\nEnter valid characters only");
                returner();
                clear();
                return false;
            }

            LocalDate desiredDate = null;

            try {
                desiredDate = DateManager.isValidDate(desiredDateStr);
            } catch (DateTimeParseException e){
                System.out.println(e.getMessage());
                returner();
                clear();
                return false;
            }

            try {
                DateManager.checkDateRange(desiredDate);
            } catch (Exception e){
                System.out.println(e.getMessage());
                returner();
                clear();
                return false;
            }

            try {
                crs.makeRendezvous(patient_id, hospital.getId(), section.getId(), doctor.getDiploma_id(), desiredDate);
                System.out.println("\nRendezvous is successfully made");
                returner();
                clear();
                return true;
            } catch (DailyLimitException e){
                System.out.println(e.getMessage());
                return false;
            }

        }
    }

    public void returner(){
        System.out.println("Press anything to return");
        scanner.nextLine();
    }
}
