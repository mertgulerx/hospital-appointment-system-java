package mertguler.Hospital;

import mertguler.Person.Doctor;
import mertguler.Person.Patient;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Rendezvous implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime dateTime;
    private Doctor doctor;
    private Patient patient;


    public Rendezvous(LocalDateTime desiredDate, Doctor doctor, Patient patient){
        dateTime = desiredDate;
        this.doctor = doctor;
        this.patient = patient;
    }

    public LocalDateTime getDate(){
        return dateTime;
    }

}
