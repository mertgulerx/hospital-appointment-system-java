package mertguler.Person;

import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private final Long national_id;


    public Person(String name, long national_id){
        this.name = name;
        this.national_id = national_id;
    }

    public String getName(){
        return name;
    }

    public Long getNational_id(){
        return national_id;
    }

    @Override
    public String toString(){
        return name + ", " + national_id;
    }
}
