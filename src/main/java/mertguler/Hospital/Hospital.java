package mertguler.Hospital;

import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;

import java.io.Serializable;
import java.util.ArrayList;

public class Hospital implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Section> sections;
    private String name;
    private final int id;


    public Hospital(String name, int id){
        this.name = name;
        this.id = id;
        sections = new ArrayList<>();
    }

    public void addSection(Section section) throws DuplicateInfoException {
        if (sections.contains(section)){
            throw new DuplicateInfoException("This section already exists");
        }

        sections.add(section);
    }

    // WHY FOR
    public Section getSection(String name){
        if (sections.isEmpty()){
            return null;
        }

        for (Section section: sections){
            if (section.getName().equals(name)){
                return section;
            }
        }

        return null;
    }

    // Used in CRS
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

    @Override
    public String toString(){
        return name + ", " + id;
    }
}


