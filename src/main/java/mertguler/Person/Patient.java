package mertguler.Person;

import mertguler.CRS.DateManager;
import mertguler.Exceptions.RendezvousLimitException;
import mertguler.Hospital.Rendezvous;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import static mertguler.CRS.CRS.MAX_RENDEZVOUS_PER_PATIENT;

public class Patient extends Person {
    private final ArrayList<Rendezvous> rendezvouses;
    private LocalDate birthDate;

    public Patient(String name, long national_id) {
        super(name, national_id);
        rendezvouses = new ArrayList<>();
        birthDate = null;
    }

    public Patient(String name, long national_id, LocalDate birthDate) {
        super(name, national_id);
        rendezvouses = new ArrayList<>();
        this.birthDate = birthDate;
    }

    public void addRendezvous(Rendezvous rendezvous){
        rendezvouses.add(rendezvous);
    }

    public void deleteRendezvous(Rendezvous rendezvous){
        rendezvouses.remove(rendezvous);
    }

    public boolean haveRendezvous(Rendezvous rendezvous){
        if (rendezvouses.contains(rendezvous)){
            return true;
        } else {
            return false;
        }
    }

    public void checkValidity(){
        int nonExpiredCount = 0;

        for (Rendezvous rendezvous: rendezvouses){
            if (!(rendezvous.isExpired())){
                nonExpiredCount++;
            }
        }

        if (nonExpiredCount >= MAX_RENDEZVOUS_PER_PATIENT) {
            throw new RendezvousLimitException("Patient: " + this + " has more rendezvouses than limit: " + MAX_RENDEZVOUS_PER_PATIENT);
        }
    }

    public int getRendezvousCount() {
        return rendezvouses.size();
    }

    public ArrayList<Rendezvous> getRendezvouses() {
        return rendezvouses;
    }

    public int getAge(){
        if ((birthDate != null)) {
            return Period.between(birthDate, DateManager.getCurrentDate()).getYears();
        } else {
            return 0;
        }
    }

    public void setBirthDate(LocalDate newBirthDate){
        birthDate = newBirthDate;
    }

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }

        if (!(object instanceof Patient)){
            return false;
        }

        Patient comparedPatient = (Patient) object;

        if (this.getNational_id() == (comparedPatient.getNational_id())){
            return true;
        }

        return false;
    }

    @Override
    public String toString(){
        return getName() + ", National ID: " + getNational_id();
    }

}
