package mertguler.Hospital;

import mertguler.CRS.HospitalManager;
import mertguler.Enums.City;
import mertguler.Exceptions.DuplicateInfoException;
import java.io.Serializable;
import java.util.ArrayList;

public class Hospital implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<Section> sections;
    private String name;
    private final int id;
    private City city;


    public Hospital(String name, int id){
        this.name = name;
        this.id = id;
        sections = new ArrayList<>();
    }

    public Hospital(String name, City city){
        this.name = name;
        HospitalManager.hospitalCount++;
        id = HospitalManager.hospitalCount;
        sections = new ArrayList<>();
        this.city = city;
    }


    public void addSection(Section section) throws DuplicateInfoException {
        if (sections.contains(section)){
            throw new DuplicateInfoException("Section with ID: " + section.getId() + ", already exist");
        }

        sections.add(section);
    }

    public Section getSection(String name){
        if (sections.isEmpty()){
            return null;
        }

        name = name.trim();
        for (Section section: sections){
            if (section.getName().trim().equalsIgnoreCase(name)){
                return section;
            }
        }

        return null;
    }

    public Section getSection(int id) {
        if (sections.isEmpty()){
            return null;
        }

        for (Section section: sections){
            if (section.getId() == id){
                return section;
            }
        }

        return null;
    }

    public ArrayList<Section> getSections(){
        return sections;
    }

    public boolean deleteSection(int id){
        Section section = getSection(id);
        if (section == null){
            return false;
        }

        return sections.remove(section);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public City getCity(){
        return city;
    }

    @Override
    public String toString(){
        return name + ", " + city + ", ID: " + id;
    }
}


