package mertguler.CRS;

import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;

import java.util.HashMap;

public class HospitalManager {
    private HashMap<Integer, Hospital> hospitals;
    private SectionManager sectionManager;

    public HospitalManager(HashMap<Integer, Hospital> hospitals){
        this.hospitals = hospitals;
        sectionManager = new SectionManager();
    }

    public SectionManager getSectionManager(){
        return sectionManager;
    }

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

    public void checkHospitalDuplication(int hospital_id) throws DuplicateInfoException {
        if (hospitals.containsKey(hospital_id)){
            throw new DuplicateInfoException("Hospital with Hospital ID: " + hospital_id + " is already exist");
        }
    }

    public void checkHospitalID(int hospital_id) throws IDException {
        if (!hospitals.containsKey(hospital_id)){
            throw new DuplicateInfoException("No hospital found with Hospital ID: " + hospital_id);
        }
    }

    public class SectionManager{

        public void checkSectionID(int hospital_id, int section_id) throws IDException{
            if (hospitals.get(hospital_id).getSection(section_id) == null){
                throw new IDException("No section found with Section ID: " + section_id);
            }
        }


    }

}

