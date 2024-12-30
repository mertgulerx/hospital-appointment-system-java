package mertguler.Hospital;

import mertguler.Exceptions.DailyLimitException;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Person.Doctor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Rendezvous> sessions;
    private int maxPatientPerDay;
    private Doctor doctor;

    public Schedule(int maxPatientPerDay){
        this.maxPatientPerDay = maxPatientPerDay;
        sessions = new ArrayList<>();
    }

    public void addRendezvous(Rendezvous rendezvous) throws DuplicateInfoException {
        if (sessions.contains(rendezvous)){
            throw new DuplicateInfoException("Rendezvous: " + rendezvous + " is already exist");
        }

        sessions.add(rendezvous);
    }

    public void deleteRendezvous(Rendezvous rendezvous) throws IDException{
        if (sessions.contains(rendezvous)){
            throw new IDException("Rendezvous is not found");
        }

        sessions.remove(rendezvous);
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
    public void checkDailyLimit(LocalDate desiredDate) throws DailyLimitException {
        int countForDay = 0;

        for (Rendezvous rendezvous: sessions){
            if (rendezvous.getDate().getMonthValue() == desiredDate.getMonthValue() &&
                    rendezvous.getDate().getDayOfMonth() == desiredDate.getDayOfMonth()){
                countForDay++;
            }
        }

        if (countForDay == maxPatientPerDay){
            throw new DailyLimitException("Doctor: " + doctor + " has reached daily rendezvous limit for date: " + desiredDate);
        }

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

    public ArrayList<Rendezvous> getSessions(){
        return sessions;
    }

}
