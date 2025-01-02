package mertguler.CRS;

import mertguler.Exceptions.DailyLimitException;
import mertguler.Exceptions.IDException;
import mertguler.Exceptions.RendezvousLimitException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;

import java.io.*;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static mertguler.CRS.DateManager.checkDateRange;

public class CRS {
    private HashMap<Long, Patient> patients;
    private ArrayList<Rendezvous> rendezvouses;
    private HashMap<Integer, Hospital> hospitals;

    private PatientManager patientManager;
    private HospitalManager hospitalManager;

    // App wide date manager
    public static DateManager dateManager;
    public static int MAX_RENDEZVOUS_PER_PATIENT = 5;
    public static int RENDEZVOUS_DAY_LIMIT = 15;
    public static String dataPath = "data.ser";


    public CRS(){
        patients = new HashMap<>();
        rendezvouses = new ArrayList<>();
        hospitals = new HashMap<>();
        hospitalManager = new HospitalManager(hospitals);
        patientManager = new PatientManager(patients);
    }

    // Rendezvous

    public boolean makeRendezvous(long patientID, int hospitalID, int sectionID, int diplomaID, LocalDate desiredDate) throws IDException, DailyLimitException, RendezvousLimitException, DateTimeException {
        // DateTimeException
        checkDateRange(desiredDate);

        // ID Exception
        patientManager.checkPatientID(patientID);

        Patient patient = patientManager.getPatient(patientID);

        // RendezvousLimitException
        patient.checkValidity();

        // ID Exceptions
        hospitalManager.checkHospitalID(hospitalID);

        hospitalManager.getSectionManager().checkSectionID(hospitalID, sectionID);

        hospitalManager.getDoctorManager().checkDoctorID(hospitalID,sectionID,diplomaID);

        Hospital hospital = hospitals.get(hospitalID);
        Section section = hospital.getSection(sectionID);

        Doctor doctor = section.getDoctor(diplomaID);

        // DailyLimitException
        doctor.getSchedule().checkDailyLimit(desiredDate);

        Rendezvous rendezvous = new Rendezvous(desiredDate, doctor, patient, hospital, section);

        // Duplicate Info Exception
        doctor.getSchedule().addRendezvous(rendezvous);

        patient.addRendezvous(rendezvous);
        rendezvouses.add(rendezvous);
        return true;
    }

    public void deleteRendezvous(Rendezvous rendezvous) throws IDException{
        if (rendezvous == null) {
            throw new IDException("Rendezvous is null");
        }

        Doctor doctor = rendezvous.getDoctor();
        Patient patient = rendezvous.getPatient();

        doctor.getSchedule().deleteRendezvous(rendezvous);
        patient.deleteRendezvous(rendezvous);
        rendezvouses.remove(rendezvous);
    }

    // Thread Work

    public boolean expiredUpdater(){
        ExecutorService pool = Executors.newFixedThreadPool(4);
        int size = rendezvouses.size();
        int quarter = size / 4;
        int half = quarter * 2;
        int threeQuarter = quarter + half;

        Runnable thread1 = () ->
        {
            //Thread.currentThread().setName("firstQuarter");
            for (int i = 0; i < quarter; i++){
                rendezvouses.get(i).updateExpired();
            }
        };

        Runnable thread2 = () ->
        {
            //Thread.currentThread().setName("secondQuarter");
            for (int i = quarter; i < half; i++){
                rendezvouses.get(i).updateExpired();
            }
        };

        Runnable thread3 = () ->
        {
            //Thread.currentThread().setName("thirdQuarter");
            for (int i = half; i < threeQuarter; i++){
                rendezvouses.get(i).updateExpired();
            }
        };

        Runnable thread4 = () ->
        {
            //Thread.currentThread().setName("forthQuarter");
            for (int i = threeQuarter; i < size; i++){
                rendezvouses.get(i).updateExpired();
            }
        };

        pool.execute(thread1);
        pool.execute(thread2);
        pool.execute(thread3);
        pool.execute(thread4);
        pool.shutdown();
        return true;
    }

    // Serialization - Deserialization

    public void saveTablesToDisk(){
        try {
            FileOutputStream fileOut = new FileOutputStream(dataPath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(hospitals);
            out.writeObject(patients);
            out.writeObject(rendezvouses);
            out.close();
            fileOut.close();
            System.out.printf("\nSerialized data is saved to " + dataPath + " file.");
        } catch (IOException e) {
            System.out.println("\n" + e.getMessage());
        }
    }


    public boolean loadTablesFromDisk(){
        try {
            FileInputStream fileIn = new FileInputStream(dataPath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            hospitals = (HashMap<Integer, Hospital>) in.readObject();
            patients = (HashMap<Long, Patient>) in.readObject();
            rendezvouses = (ArrayList<Rendezvous>) in.readObject();
            in.close();
            fileIn.close();
            System.out.printf("\nSerialized data is read from " + dataPath + " file.");
            hospitalManager.updateHospitalMap(hospitals);
            patientManager.updatePatientsMap(patients);
        } catch (IOException i) {
            System.out.println("\n" + i.getMessage());
            return false;
        } catch (ClassNotFoundException c) {
            System.out.println("\n\n" + c.getMessage());
            return false;
        }
        expiredUpdater();
        return true;
    }


    public void saveSettings(){
        String path = "settings.txt";
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(String.valueOf(MAX_RENDEZVOUS_PER_PATIENT));
            fileWriter.write(",");
            fileWriter.write(String.valueOf(RENDEZVOUS_DAY_LIMIT));

            fileWriter.close();
            System.out.println("\nSettings are saved to " + path + "\n");
        } catch (IOException e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    public void loadSettings(){
        String path = "settings.txt";
        try {
            Scanner fileScanner = new Scanner(Paths.get(path));
            String settingsText = fileScanner.nextLine();
            String[] settings = settingsText.split(",");

            MAX_RENDEZVOUS_PER_PATIENT = Integer.valueOf(settings[0]);
            RENDEZVOUS_DAY_LIMIT = Integer.valueOf(settings[1]);

            System.out.println("\nSettings are loaded from: " + path + "\n");
        } catch (IOException e) {
            System.out.println("\n" + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException indexE){
            System.out.println("\nSettings file is corrupted");
        }
    }

    // Getters

    public ArrayList<Rendezvous> getRendezvouses(){
        return rendezvouses;
    }

    public HospitalManager getHospitalManager(){
        return hospitalManager;
    }

    public PatientManager getPatientManager(){
        return patientManager;
    }

    public int getRendezvousCount(){
        return rendezvouses.size();
    }

    // Setters

    public void setMaxRendezvousPerPatient(int newLimit){
        MAX_RENDEZVOUS_PER_PATIENT = newLimit;
    }

    public void setRendezvousDayLimit(int newLimit){
        RENDEZVOUS_DAY_LIMIT = newLimit;
    }

}
