package mertguler.CRS;

import mertguler.Exceptions.DailyLimitException;
import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class CRS {
    private HashMap<Long, Patient> patients;
    private ArrayList<Rendezvous> rendezvouses;
    private HashMap<Integer, Hospital> hospitals;
    private HospitalManager hospitalManager;
    private PatientManager patientManager;

    // App Wide date manager
    public static DateManager dateManager;


    public CRS(){
        patients = new HashMap<>();
        rendezvouses = new ArrayList<>();
        hospitals = new HashMap<>();
        dateManager = new DateManager();
        hospitalManager = new HospitalManager(hospitals);
        patientManager = new PatientManager(patients);
    }

    public void saveTablesToDisk(String path){
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(hospitals);
            out.writeObject(patients);
            // Maybe save rendezvous too ?
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved to " + path + " file.");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    public void loadTablesFromDisk(String path){
        try {
            FileInputStream fileIn = new FileInputStream("employee.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            patients = (HashMap) in.readObject();
            hospitals = (HashMap<Integer, Hospital>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }
    }

    // We can use multithreading here - No need for hashmaps as they are quite fast
    // For now won`t implement multithreading
    public boolean makeRendezvous(long patientID, int hospitalID, int sectionID, int diplomaID, LocalDate desiredDate) throws IDException, DailyLimitException{
        patientManager.checkPatientID(patientID);

        hospitalManager.checkHospitalID(hospitalID);

        hospitalManager.getSectionManager().checkSectionID(hospitalID, sectionID);

        checkDoctorID(hospitalID,sectionID,diplomaID);

        Doctor doctor = hospitals.get(hospitalID).getSection(sectionID).getDoctor(diplomaID);

        if (!(doctor.getSchedule().addRendezvous(patients.get(patientID), desiredDate))){
            throw new DailyLimitException("Daily limit is exceeded for doctor: " + doctor + ", at the date: " + desiredDate);
        } else {
            return true;
        }

    }

    // Doctor Management //

    public void checkDoctorID(int hospital_id, int section_id, int diploma_id) throws IDException{
        if (hospitals.get(hospital_id).getSection(section_id).getDoctor(diploma_id) == null){
            throw new IDException("No doctor found with Diploma ID: " + diploma_id);
        }
    }

    // Rendezvous Management //

    public ArrayList<Rendezvous> getRendezvouses(){
        return rendezvouses;
    }

    public HospitalManager getHospitalManager(){
        return hospitalManager;
    }

    public PatientManager getPatientManager(){
        return patientManager;
    }


}
