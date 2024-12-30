package mertguler.Hospital;

import mertguler.Person.Doctor;
import mertguler.Person.Patient;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


public class Rendezvous implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDate date;
    private Doctor doctor;
    private Patient patient;

    public Rendezvous(LocalDate desiredDate, Doctor doctor, Patient patient){
        date = desiredDate;
        this.doctor = doctor;
        this.patient = patient;
    }

    public LocalDate getDate(){
        return date;
    }

    public Doctor getDoctor(){
        return doctor;
    }

    public Patient getPatient(){
        return patient;
    }

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }

        if (!(object instanceof Rendezvous)){
            return false;
        }

        Rendezvous checkedRendezvous = (Rendezvous) object;

        if (this.date == checkedRendezvous.date && this.doctor == checkedRendezvous.doctor && this.patient == checkedRendezvous.patient){
            return true;
        }

        return false;
    }

}
