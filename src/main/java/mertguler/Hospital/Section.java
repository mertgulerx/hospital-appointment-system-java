package mertguler.Hospital;

import mertguler.CRS.HospitalManager;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Person.Doctor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import static mertguler.Main.gui_mode;

public class Section implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String name;
    private ArrayList<Doctor> doctors;

    public Section(String name, int id){
        this.name = name;
        this.id = id;
        doctors = new ArrayList<>();
    }

    public Section(String name, Hospital hospital){
        this.name = name;
        this.id = hospital.getId() * 1000 + hospital.getSections().size() + 1;
        doctors = new ArrayList<>();
    }

    public void addDoctor (Doctor doctor) throws DuplicateInfoException{
        // Must be same with for loop
        if (doctors.contains(doctor)){
            throw new DuplicateInfoException("Doctor with Diploma ID: " + doctor.getDiploma_id() + " is already exists");
        }

        doctors.add(doctor);
    }

    public boolean deleteDoctor(int diploma_id){
        Doctor doctor = getDoctor(diploma_id);
        if (doctor == null){
            return false;
        } else {
            doctors.remove(doctor);
            return true;
        }
    }

    public void listDoctors(){
        // Empty for GUI
        if (!gui_mode){
            if (doctors.isEmpty()){
                System.out.println("This section doesnt have any doctors");
                return;
            }

            Comparator<Doctor> comparator = Comparator
                    .comparing(Doctor::getDiploma_id);

            System.out.println();

            doctors.stream()
                    .sorted(comparator)
                    .forEach(System.out::println);
        }
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

    public ArrayList<Doctor> getDoctor(String name){
        if (doctors.isEmpty()){
            return null;
        }

        name = name.trim();
        ArrayList<Doctor> foundDoctors = new ArrayList<>();

        for (Doctor doctor: doctors){
            if (doctor.getName().trim().equalsIgnoreCase(name)){
                foundDoctors.add(doctor);
            }
        }

        if (!foundDoctors.isEmpty()){
            return foundDoctors;
        } else {
            return null;
        }

    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Doctor> getDoctors(){
        return doctors;
    }

    public void setName(String name){
        this.name = name;
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

        return false;
    }

    @Override
    public String toString(){
        return name + ", " + id;
    }
}
