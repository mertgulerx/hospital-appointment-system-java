package mertguler.Person;

import mertguler.Hospital.Rendezvous;

public class Patient extends Person {
    private Rendezvous rendezvous;

    public Patient(String name, Long national_id){
        super(name, national_id);
    }
}
