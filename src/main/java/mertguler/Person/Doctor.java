package mertguler.Person;

import mertguler.Hospital.Schedule;

public class Doctor extends Person{
    private final int diploma_id;
    private Schedule schedule;

    public Doctor(String name, Long national_id, int diploma_id, Schedule schedule){
        super(name,national_id);
        this.diploma_id = diploma_id;
        this.schedule = schedule;
    }


    public Schedule getSchedule(){
        return schedule;
    }

    public int getDiploma_id(){
        return diploma_id;
    }

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }

        if (!(object instanceof Doctor)){
            return false;
        }

        Doctor comparedDoctor = (Doctor) object;

        // Should we check for national id, would be better for frauds of course.
        if (this.diploma_id == comparedDoctor.getDiploma_id() && this.getNational_id().equals(comparedDoctor.getNational_id())){
            return true;
        }

        return false;
    }

}
