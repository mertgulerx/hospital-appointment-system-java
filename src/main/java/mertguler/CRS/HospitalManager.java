package mertguler.CRS;

import mertguler.Enums.City;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;

import java.util.HashMap;

public class HospitalManager {
    private HashMap<Integer, Hospital> hospitals;
    private SectionManager sectionManager;
    private DoctorManager doctorManager;
    public static int hospitalCount = 0;

    public HospitalManager(HashMap<Integer, Hospital> hospitals){
        this.hospitals = hospitals;
        sectionManager = new SectionManager();
        doctorManager = new DoctorManager();
        hospitalCount = hospitals.size();
    }

    public SectionManager getSectionManager(){
        return sectionManager;
    }
    public DoctorManager getDoctorManager() {
        return doctorManager;
    }

    public boolean updateHospitalMap(HashMap<Integer, Hospital> hospitals){
        this.hospitals = hospitals;
        hospitalCount = hospitals.size();
        return true;
    }


    // Creates a hospital with id
    public void createHospital(String name, City city){
        Hospital hospital = new Hospital(name, city);
        hospitals.put(hospital.getId(), hospital);
    }

    public void deleteHospital(int id) throws IDException{
        checkHospitalID(id);
        hospitals.remove(id);
    }

    public void renameHospital(int id, String newName) throws IDException{
       checkHospitalID(id);
       hospitals.get(id).setName(newName);

    }

    public Hospital getHospitalWithID(int id) throws IDException{
        checkHospitalID(id);
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
            throw new IDException("No hospital found with Hospital ID: " + hospital_id);
        }
    }

    public HashMap<Integer, Hospital> getHospitals(){
        return hospitals;
    }

    public int countAllSections(){
        int count = 0;

        if (hospitals.isEmpty()){
            return count;
        }

        for(Hospital hospital: hospitals.values()){
            count += hospital.getSections().size();
        }

        return count;
    }

    public int countAllDoctors(){
        int count = 0;

        for (Hospital hospital: hospitals.values()){
            for(Section section: hospital.getSections()){
                count += section.getDoctors().size();
            }
        }
        return count;
    }

    public class SectionManager{
        public void checkSectionID(int hospital_id, int section_id) throws IDException{
            if (hospitals.get(hospital_id).getSection(section_id) == null){
                throw new IDException("No section found with Section ID: " + section_id);
            }
        }
    }

    public class DoctorManager{
        public void checkDoctorID(int hospital_id, int section_id, int diploma_id) throws IDException{
            if (hospitals.get(hospital_id).getSection(section_id).getDoctor(diploma_id) == null){
                throw new IDException("No doctor found with Diploma ID: " + diploma_id);
            }
        }
    }

}

