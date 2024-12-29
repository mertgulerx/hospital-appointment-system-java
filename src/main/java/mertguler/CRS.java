package mertguler;

import mertguler.Exceptions.DailyLimitException;
import mertguler.Exceptions.DuplicateInfoException;
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
    public boolean makeRendezvous(long patientID, int hospitalID, int sectionID, int diplomaID, LocalDateTime desiredDate) throws IDException{
        checkPatientID(patientID);

        checkHospitalID(hospitalID);

        checkSectionID(hospitalID, sectionID);

        checkDoctorID(hospitalID,sectionID,diplomaID);

        Doctor doctor = hospitals.get(hospitalID).getSection(sectionID).getDoctor(diplomaID);
        if (!(doctor.getSchedule().addRendezvous(patients.get(patientID), desiredDate))){
            throw new DailyLimitException("Daily limit is exceeded for doctor: " + doctor + ", at the date: " + desiredDate);
        }

        return doctor.getSchedule().addRendezvous(patients.get(patientID), desiredDate);
    }

    // Hospital Management //

    public void createHospital(String name, int id){
        if (hospitals.containsKey(id)){
            throw new IDException("Hospital with this ID is already exists");
        } else {
            Hospital hospital = new Hospital(name, id);
            hospitals.put(id, hospital);
        }
    }

    public void deleteHospital(int id){
        if(!(hospitals.containsKey(id))){
            throw new IDException("Hospital with ID: " + id + " is not found");
        } else {
            hospitals.remove(id);
        }
    }

    public void renameHospital(int id, String name){
        if(!(hospitals.containsKey(id))){
            throw new IDException("Hospital with ID: " + id + " is not found");
        } else {
            hospitals.get(id).setName(name);
        }
    }

    public HashMap<Integer, Hospital> getHospitals(){
        return hospitals;
    }

    public Hospital getHospitalWithID(int id) throws IDException{
        if (!(hospitals.containsKey(id))){
            throw new IDException("Hospital with id: " + id + " is not found");
        }

        return hospitals.get(id);
    }

    public Hospital getHospitalWithName(String name) throws IDException{
        if (hospitals.isEmpty()){
            throw new IDException("No hospital found!");
        }

        name = name.trim();
        for (Hospital hospital: hospitals.values()){
            if (hospital.getName().trim().equalsIgnoreCase(name)){
                return hospital;
            }
        }

        throw new IDException("Hospital with name: " + name + " is not found");
    }

    public void checkHospitalDuplication(int hospital_id) throws DuplicateInfoException{
        if (hospitals.containsKey(hospital_id)){
            throw new DuplicateInfoException("Hospital with Hospital ID: " + hospital_id + " is already exist");
        }
    }

    public void checkHospitalID(int hospital_id) throws IDException {
        if (!hospitals.containsKey(hospital_id)){
            throw new DuplicateInfoException("No hospital found with Hospital ID: " + hospital_id);
        }
    }

    // Patient Management //

    public void checkPatientDuplication(long national_id) throws DuplicateInfoException{
        if (patients.containsKey(national_id)){
            throw new DuplicateInfoException("Patient with National ID: " + national_id + " is already exist");
        }
    }

    public void checkPatientID(long national_id) throws IDException{
        if (!(patients.containsKey(national_id))){
            throw new IDException("No patient found with National ID: " + national_id);
        }
    }

    public void patientAdder(String name, long national_id) throws DuplicateInfoException{
        checkPatientDuplication(national_id);
        Patient patient = new Patient(name, national_id);
        patients.put(national_id, patient);
    }

    public void patientDeleter(long national_id) throws IDException{
        checkPatientID(national_id);
        patients.remove(national_id);
    }

    public void patientRenamer(long national_id, String name) throws IDException{
        checkPatientID(national_id);
        patients.get(national_id).setName(name);
    }

    public HashMap<Long, Patient> getPatients(){
        return patients;
    }

    // Section Management //

    public void checkSectionID(int hospital_id, int section_id) throws IDException{
        if (hospitals.get(hospital_id).getSection(section_id) == null){
            throw new IDException("No section found with Section ID: " + section_id);
        }
    }

    // Doctor Management //

    public void checkDoctorID(int hospital_id, int section_id, int diploma_id) throws IDException{
        if (hospitals.get(hospital_id).getSection(section_id).getDoctor(diploma_id) == null){
            throw new IDException("No doctor found with Diploma ID: " + diploma_id);
        }
    }


}
