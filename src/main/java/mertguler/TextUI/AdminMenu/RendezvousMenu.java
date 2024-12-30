package mertguler.TextUI.AdminMenu;

import mertguler.CRS.CRS;
import mertguler.CRS.DateManager;
import mertguler.Exceptions.DailyLimitException;
import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;


import static mertguler.CRS.CRS.RENDEZVOUS_DAY_LIMIT;
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
                rendezvousDeleter();
            } else if (input == 3){
                rendezvousInfoChecker();
            }
        }
    }

    public long nationalIDSelector(){
        while(true){
            clear();
            header();
            long national_id = 0;

            System.out.println("\nEnterNational ID: ");

            try {
                national_id = Long.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("\nPlease enter only valid numbers\n");
                returner();
                clear();
                return 0;
            }

            return national_id;
        }
    }

    public boolean rendezvousAdder(){
        while (true){
            clear();
            header();
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
            LocalDate lastRendezvousDate = DateManager.getLastDate();

            System.out.println("Hospital: " + hospital);
            System.out.println("Section: " + section);
            System.out.println("Doctor: " + doctor);
            System.out.println("Current Time: " + DateManager.getFormatedDate(currentDate));

            System.out.println("\nYou can create rendezvous for next " + RENDEZVOUS_DAY_LIMIT + " days");
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
                clear();
                header();
                Patient patient = crs.getPatientManager().getPatient(patient_id);
                System.out.println("\nRendezvous is successfully made");
                System.out.println("Patient Name: " + patient.getName());
                System.out.println("Patient National ID: " + patient.getNational_id());
                System.out.println("Doctor Name: " + doctor.getName());
                System.out.println("Section: " + section.getName());
                System.out.println("Hospital: " + hospital.getName());
                System.out.println("Date: " + desiredDateStr + "\n");
                returner();
                clear();
                return true;
            } catch (DailyLimitException e){
                System.out.println(e.getMessage());
                return false;
            }

        }
    }

    public boolean rendezvousDeleter(){
        while (true){
            clear();
            header();
            long national_id = 0;

            System.out.println("\nEnter Patient`s National ID: ");

            try {
                national_id = Long.valueOf(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Enter valid numbers only");
                returner();
                clear();
                return false;
            }

            Patient patient;

            try {
                patient = crs.getPatientManager().getPatient(national_id);
            } catch (IDException e){
                System.out.println(e.getMessage()) ;
                returner();
                clear();
                return false;
            }

            ArrayList<Rendezvous> rendezvousList = patient.getRendezvouses();
            if (rendezvousList.isEmpty()){
                System.out.println("This patient doesnt have any rendezvous");
                returner();
                clear();
                return false;
            }

            clear();
            header();
            System.out.println("\nRendezvous List for Patient: " + patient);

            int index = 0;
            for (Rendezvous rendezvous: rendezvousList){
                index++;
                System.out.println(rendezvous + ", ID: " + index);
            }

            System.out.println("\nWhich Rendezvous to delete? Enter Rendezvous ID from list above:");

            Rendezvous rendezvous;
            try {
                index = Integer.valueOf(scanner.nextLine());
                rendezvous = rendezvousList.get(index);
            } catch (Exception e){
                System.out.println("\nEnter valid numbers only\n");
                returner();
                clear();
                return false;
            }

            crs.deleteRendezvous(rendezvous);

        }
    }

    public void returner(){
        System.out.println("Press anything to return");
        scanner.nextLine();
    }
}
