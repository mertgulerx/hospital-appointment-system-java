package mertguler.Hospital;

import mertguler.Person.Doctor;
import mertguler.Person.Patient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;
    private LinkedList<Rendezvous> sessions;
    private int maxPatientPerDay;
    private Doctor doctor;

    public Schedule(int maxPatientPerDay){
        this.maxPatientPerDay = maxPatientPerDay;
        sessions = new LinkedList<>();
    }

    public boolean addRendezvous(Patient patient, LocalDateTime desiredDate){
        if (!(checkDailyLimit(desiredDate))){
            return false;
        }

        sessions.add(new Rendezvous(desiredDate, doctor, patient));
        return true;
    }

    // Get rendezvouses in that day as a list
    public LinkedList<Rendezvous> getRendezvousForDay(LocalDateTime desiredDate){
        LinkedList<Rendezvous> rendezvousList = new LinkedList<>();

        for (Rendezvous rendezvous: sessions){
            if (rendezvous.getDate().getMonthValue() == desiredDate.getMonthValue() &&
                    rendezvous.getDate().getDayOfMonth() == desiredDate.getDayOfMonth()){
              rendezvousList.add(rendezvous);
            }
        }

        return rendezvousList;
    }

    // Daily Patient Limit Checker
    public boolean checkDailyLimit(LocalDateTime desiredDate){
        int countForDay = 0;

        for (Rendezvous rendezvous: sessions){
            if (rendezvous.getDate().getMonthValue() == desiredDate.getMonthValue() &&
                    rendezvous.getDate().getDayOfMonth() == desiredDate.getDayOfMonth()){
                countForDay++;
            }
        }

        if (countForDay == maxPatientPerDay){
            return false;
        }

        return true;
    }

    public boolean setDoctor(Doctor doctor){
        if (doctor == null){
            return false;
        }

        this.doctor = doctor;
        return true;
    }

    public Doctor getDoctor(){
        return doctor;
    }

    public LinkedList<Rendezvous> getSessions(){
        return sessions;
    }

}
