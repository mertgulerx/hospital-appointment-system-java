package mertguler.CRS;

import mertguler.Exceptions.DailyLimitException;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Exceptions.RendezvousLimitException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static mertguler.CRS.DateManager.checkDateRange;

public class CRS {
    private HashMap<Long, Patient> patients;
    private ArrayList<Rendezvous> rendezvouses;
    private HashMap<Integer, Hospital> hospitals;

    private PatientManager patientManager;
    private HospitalManager hospitalManager;

    // App wide date manager
    public static DateManager dateManager;
    public static final int MAX_RENDEZVOUS_PER_PATIENT = 5;
    public static final int RENDEZVOUS_DAY_LIMIT = 15;


    public CRS(){
        patients = new HashMap<>();
        rendezvouses = new ArrayList<>();
        hospitals = new HashMap<>();
        hospitalManager = new HospitalManager(hospitals);
        patientManager = new PatientManager(patients);
    }

    public void saveTablesToDisk(String path){
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(hospitals);
            out.writeObject(patients);
            out.writeObject(rendezvouses);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved to " + path + " file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadTablesFromDisk(String path){
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
           // patients = (HashMap) in.readObject();
            hospitals = (HashMap<Integer, Hospital>) in.readObject();
            patients = (HashMap<Long, Patient>) in.readObject();
            rendezvouses = (ArrayList<Rendezvous>) in.readObject();
            in.close();
            fileIn.close();
            System.out.printf("Serialized data is read from " + path + " file.");
            hospitalManager.updateHospitalMap(hospitals);
            patientManager.updatePatientsMap(patients);
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

    // We can use multithreading here - No need for hashmaps as they are quite fast
    // For now won`t implement multithreading
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

        Doctor doctor = hospitals.get(hospitalID).getSection(sectionID).getDoctor(diplomaID);

        // DailyLimitException
        doctor.getSchedule().checkDailyLimit(desiredDate);

        Rendezvous rendezvous = new Rendezvous(desiredDate, doctor, patient);

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


}
