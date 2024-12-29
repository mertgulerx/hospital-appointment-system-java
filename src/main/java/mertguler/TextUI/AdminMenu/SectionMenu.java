package mertguler.TextUI.AdminMenu;

import mertguler.CRS;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;

import java.util.Comparator;
import java.util.Scanner;

import static mertguler.TextUI.TextUI.clear;
import static mertguler.TextUI.TextUI.header;

public class SectionMenu {
    private Scanner scanner;
    private CRS crs;
    private Hospital hospital;

    public SectionMenu(Scanner scanner, CRS crs){
        this.scanner = scanner;
        this.crs = crs;
    }

    public SectionMenu(Scanner scanner, CRS crs, Hospital hospital){
        this.scanner = scanner;
        this.crs = crs;
        this.hospital = hospital;
    }

    public void sectionManager() {
        HospitalMenu hospitalMenu = new HospitalMenu(scanner, crs);
        hospital = hospitalMenu.hospitalSelector();
        if (hospital == null){
            return;
        }

        while (true) {
            clear();
            header();
            int input = 9;
            System.out.println("\nHospital: " + hospital.getName() + ", ID: " + hospital.getId() + "\n");
            System.out.println("Add Section: 1");
            System.out.println("Delete Section: 2");
            System.out.println("Rename Section: 3");
            System.out.println("List Sections: 4");
            System.out.println("Return to last menu: 0");
            System.out.println("\nSelect operating mode (1-4):");
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
                sectionAdder();
            } else if (input == 2){
                sectionDeleter();
            } else if (input == 3){
                sectionRenamer();
            } else if (input == 4){
                sectionLister();
            }
        }
    }

    public boolean sectionLister(){
        clear();
        header();
        if (hospital.getSections().isEmpty()){
            System.out.println("\nSection list is empty.");
            returner();
            return false;
        }

        Comparator<Section> comparator = Comparator
                .comparing(Section::getId);

        System.out.println();

        hospital.getSections().stream()
                .sorted(comparator)
                .forEach(System.out::println);

        returner();
        return true;
    }

    public boolean sectionRenamer(){
        while (true) {
            clear();
            header();
            int id = 9;
            String name = "";

            System.out.println("Enter Section ID: ");

            try {
                id = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers within range");
                returner();
                return false;
            }

            if (hospital.getSection(id) == null){
                System.out.println("Section with ID: " + id + " is not found");
                returner();
                return false;
            }

            System.out.println("Enter the new name: ");

            try {
                name = scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter only valid characters");
                returner();
                return false;
            }

            hospital.getSection(id).setName(name);
            return true;
        }
    }

    public boolean sectionAdder(){
        while (true) {
            clear();
            header();
            int id = 9;
            String name = "";
            System.out.println("Enter Section Name: ");

            try {
                name = scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter only valid characters");
                returner();
                continue;
            }

            System.out.println("Enter Section ID: ");

            try {
                id = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers within range");
                returner();
                continue;
            }

            try {
                hospital.addSection(new Section(name, id));
                System.out.println("Selection is successfully added.");
                returner();
                return true;
            } catch (DuplicateInfoException e){
                System.out.println("Section with this id is already exists");
                returner();
                return false;
            }
        }
    }

    public boolean sectionDeleter(){
        while (true) {
            clear();
            header();
            int id = 9;
            System.out.println("Enter Section ID: ");

            try {
                id = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers within range");
                returner();
                return false;
            }

            // False if searched section is null.
            if (!hospital.deleteSection(id)){
                System.out.println("Section with id: " + id + " is not found.");
                returner();
                return false;
            } else {
                System.out.println("Section is successfully deleted.");
                returner();
                return true;
            }
        }
    }

    public Section sectionSelector(){
        // useless
        if (hospital == null){
            return null;
        }

        if(hospital.getSections().isEmpty()){
            clear();
            header();
            System.out.println("\nNo Section found for Hospital: " + hospital);
            returner();
            return null;
        }

        while (true){
            clear();
            header();
            int input = 9;
            System.out.println("\n" + hospital);
            System.out.println("Select a Section to continue with next menu.\n");
            System.out.println("Select Section with id: 1");
            System.out.println("Select Section with name: 2");
            System.out.println("List Sections: 3");
            System.out.println("Return to main menu: 0");
            System.out.println("\nSelect operating mode (1-3):");

            try {
                input = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers within range");
                returner();
                continue;
            }

            if (input == 0) {
                return null;
            } else if (input == 1){
                return getSectionWithId();
            } else if (input == 2){
                return getSectionWithName();
            } else if (input == 3){
                clear();
                header();
                sectionLister();
            }
        }
    }

    public Section getSectionWithId(){
        while (true){
            clear();
            header();
            int id = 9;

            System.out.println("Enter Section ID: ");

            try {
                id = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers");
                returner();
                return null;
            }

            Section section = hospital.getSection(id);

            if (section == null){
                System.out.println("Section with ID: " + id + " is not found");
                returner();
                return null;
            } else {
                return section;
            }

        }
    }

    public Section getSectionWithName(){
        while (true){
            clear();
            header();
            String name = "";

            System.out.println("Enter Section Name: ");

            try {
                name = scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter only valid numbers");
                returner();
                return null;
            }

            Section section = hospital.getSection(name);
            if (section == null){
                System.out.println("Section with name: " + name + " is not found");
                returner();
                return null;
            } else {
                return section;
            }

        }
    }

    public void returner(){
        System.out.println("\nPress anything to return");
        scanner.nextLine();
    }


}
