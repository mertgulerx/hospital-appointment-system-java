package mertguler.Hospital;

import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Person.Doctor;

import java.io.Serializable;
import java.util.LinkedList;

public class Section implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String name;
    private LinkedList<Doctor> doctors;

    public Section(String name, int id){
        this.name = name;
        this.id = id;
        doctors = new LinkedList<Doctor>();
    }

    public void addDoctor (Doctor doctor) throws DuplicateInfoException{
        // Must be same with for loop
        if (doctors.contains(doctor)){
            throw new DuplicateInfoException("This doctor already exists");
        }

    }

    public void listDoctors(){
        // Empty for GUI
    }

    public Doctor getDoctor(int diploma_id){
        if (doctors.isEmpty()){
            return null;
        }

        for (Doctor doctor: doctors){
            if (doctor.getDiploma_id() == diploma_id){
                return doctor;
            }
        }

        return null;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }

        if (!(object instanceof Section)){
            return false;
        }

        Section comparedSection = (Section) object;

        if (this.getId() == comparedSection.getId()){
            return true;
        }

        if (this.getName().equals(comparedSection.getName())){
            return true;
        }

        return false;
    }
}
