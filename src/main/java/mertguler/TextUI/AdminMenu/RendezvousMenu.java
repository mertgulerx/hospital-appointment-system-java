package mertguler.TextUI.AdminMenu;

import mertguler.CRS;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static mertguler.CRS.isValidDate;
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

            Doctor doctor = doctorMenu.selectDoctor(section);

            if (doctor == null){
                return false;
            }

            LocalDateTime currentDate = crs.getCurrentDate();

            System.out.println("Hospital: " + hospital);
            System.out.println("Section: " + section);
            System.out.println("Doctor: " + doctor);
            System.out.println("Current Time: " + crs.getFormatedDate(currentDate));

            System.out.println("\nYou can create rendezvous for next " + crs.getRendezvousDayLimit() + " days");
            LocalDateTime lastRendezvousDate = crs.getLastDate();
            System.out.println("Last Date: " + crs.getFormatedDate(lastRendezvousDate));

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

            LocalDate desiredDate= isValidDate(desiredDateStr);
            if (desiredDate == null){
                System.out.println("Enter valid date with specified date format");
                returner();
                clear();
                return false;
            }

            if (desiredDate.isAfter(lastRendezvousDate.toLocalDate())){
                System.out.println("Entered date is after the last rendezvous date");
                returner();
                clear();
                return false;
            }

            if (desiredDate.isBefore(currentDate.toLocalDate())){
                System.out.println("Entered date is before current date");
                returner();
                clear();
                return false;
            }

            try {
                crs.makeRendezvous(patient_id, hospital.getId(), section.getId(), doctor.getDiploma_id(), desiredDate.atStartOfDay());
                System.out.println("Rendezvous is successfully made");
                returner();
                return true;
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }

        }
    }

    public void returner(){
        System.out.println("Press anything to return");
        scanner.nextLine();
    }
}
