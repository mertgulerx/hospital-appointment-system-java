package mertguler.Person;

import mertguler.Enums.Age;
import mertguler.Enums.Gender;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.RendezvousLimitException;
import mertguler.Hospital.Rendezvous;

import java.util.ArrayList;

import static mertguler.CRS.CRS.MAX_RENDEZVOUS_PER_PATIENT;

public class Patient extends Person {
    private ArrayList<Rendezvous> rendezvouses;
    private Gender gender;
    private Age age;

    public Patient(String name, long national_id) {
        super(name, national_id);
        rendezvouses = new ArrayList<>();
        gender = Gender.UNDEFINED;
        age = Age.UNDEFINED;
    }

    public Patient(String name, long national_id, Gender gender, Age age) {
        super(name, national_id);
        rendezvouses = new ArrayList<>();
        this.gender = gender;
        this.age = age;
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
        if (rendezvouses.size() >= MAX_RENDEZVOUS_PER_PATIENT) {
            throw new RendezvousLimitException("Patient: " + this + " has more rendezvouses than limit: " + MAX_RENDEZVOUS_PER_PATIENT);
        }
    }

    public int getRendezvousCount() {
        return rendezvouses.size();
    }

    public ArrayList<Rendezvous> getRendezvouses() {
        return rendezvouses;
    }

    public Gender getGender(){
        return gender;
    }

    public Age getAge(){
        return age;
    }

}
