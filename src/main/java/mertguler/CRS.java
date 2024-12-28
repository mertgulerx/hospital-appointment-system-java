package mertguler;

import mertguler.Exceptions.DailyLimitException;
import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

public class CRS {
    private HashMap<Long, Patient> patients;
    private LinkedList<Rendezvous> rendezvous;
    private HashMap<Integer, Hospital> hospitals;

    public CRS(){
        patients = new HashMap<>();
        rendezvous = new LinkedList<>();
        hospitals = new HashMap<>();
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
    public boolean makeRendezvous(long patientID, int hospitalID, int sectionID, int diplomaID, LocalDateTime desiredDate){
        if (!((patients.containsKey(patientID)))){
            throw new IDException("No patient found for National ID: " + patientID);
        }

        if (!((hospitals.containsKey(hospitalID)))){
            throw new IDException("No hospital found for ID: " + hospitalID);
        }

        if (hospitals.get(hospitalID).getSection(sectionID) == null){
            throw new IDException("No section found for ID: " + sectionID);
        }

        if (hospitals.get(hospitalID).getSection(sectionID).getDoctor(diplomaID) == null){
            throw new IDException("No doctor found with Diploma ID: " + diplomaID);
        }

        /*
        We wont need this probably
        if (hospitals.get(hospitalID).getSection(sectionID).getDoctor(diplomaID).getSchedule() == null){
            hospitals.get(hospitalID).getSection(sectionID).getDoctor(diplomaID).createSchedule();
        }
         */

        Doctor doctor = hospitals.get(hospitalID).getSection(sectionID).getDoctor(diplomaID);
        if (!(doctor.getSchedule().addRendezvous(patients.get(patientID), desiredDate))){
            throw new DailyLimitException("Daily limit is exceeded for doctor: " + doctor + ", at the date: " + desiredDate);
        }

        return doctor.getSchedule().addRendezvous(patients.get(patientID), desiredDate);
    }

    public void createPatient(String name, long national_id){
        Patient patient = new Patient(name, national_id);
        if (patients.containsKey(national_id)){
            throw new IDException("Patient with this National ID is already exists");
        }

        patients.put(national_id, patient);
    }

    public void createHospital(String name, int id){
        Hospital hospital = new Hospital(name, id);
        if (hospitals.containsKey(id)){
            throw new IDException("Hospital with this ID is already exists");
        }

        hospitals.put(id, hospital);
    }

    public HashMap<Integer, Hospital> getHospitals(){
        return hospitals;
    }


}
