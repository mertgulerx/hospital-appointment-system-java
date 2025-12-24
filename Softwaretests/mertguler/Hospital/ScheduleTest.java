package mertguler.Hospital;

import mertguler.Enums.City;
import mertguler.Exceptions.DailyLimitException;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Test sınıfı: Schedule sınıfının tüm fonksiyonalitelerini test eder
 */
public class ScheduleTest {
    private Schedule schedule;
    private Doctor doctor;
    private Patient patient1;
    private Patient patient2;
    private Hospital hospital;
    private Section section;

    @Before
    public void setUp() {
        schedule = new Schedule(10); // Günlük max 10 hasta
        doctor = new Doctor("Dr. Test", 12345678901L, 10001);
        schedule.setDoctor(doctor);
        
        patient1 = new Patient("Hasta 1", 11111111111L, LocalDate.of(1990, 1, 1));
        patient2 = new Patient("Hasta 2", 22222222222L, LocalDate.of(1985, 5, 15));
        
        hospital = new Hospital("Test Hastanesi", City.ANKARA);
        section = new Section("Test Bölümü", hospital, false);
    }

    @Test
    public void testScheduleCreation() {
        assertNotNull("Schedule nesnesi oluşturulmalı", schedule);
        assertNotNull("Sessions listesi null olmamalı", schedule.getSessions());
        assertTrue("Yeni schedule boş olmalı", schedule.getSessions().isEmpty());
    }

    @Test
    public void testGetRendezvousCount_Empty() {
        assertEquals("Yeni schedule randevu sayısı 0 olmalı", 0, schedule.getRendezvousCount());
    }

    @Test
    public void testSetDoctor() {
        Schedule newSchedule = new Schedule(10);
        assertNull("Başlangıçta doktor null olmalı", newSchedule.getDoctor());
        
        newSchedule.setDoctor(doctor);
        assertEquals("Doktor atanmalı", doctor, newSchedule.getDoctor());
    }

    @Test
    public void testSetDoctor_Null() {
        Schedule newSchedule = new Schedule(10);
        newSchedule.setDoctor(null);
        
        // Null geçildiğinde hiçbir şey olmamalı (method kontrolü var)
        assertNull("Null doktor atanmamalı", newSchedule.getDoctor());
    }

    @Test
    public void testGetDoctor() {
        assertEquals("Doğru doktor dönmeli", doctor, schedule.getDoctor());
    }

    @Test
    public void testAddRendezvous() throws Exception {
        LocalDate date = LocalDate.now().plusDays(5);
        Rendezvous rendezvous = new Rendezvous(date, doctor, patient1, hospital, section);
        
        schedule.addRendezvous(rendezvous);
        
        assertEquals("Randevu sayısı 1 olmalı", 1, schedule.getRendezvousCount());
        assertTrue("Randezvous listede olmalı", schedule.getSessions().contains(rendezvous));
    }

    @Test
    public void testAddMultipleRendezvous() throws Exception {
        for (int i = 1; i <= 5; i++) {
            Rendezvous r = new Rendezvous(
                LocalDate.now().plusDays(i), 
                doctor, 
                patient1, 
                hospital, 
                section
            );
            schedule.addRendezvous(r);
        }
        
        assertEquals("Randevu sayısı 5 olmalı", 5, schedule.getRendezvousCount());
    }

    @Test(expected = DuplicateInfoException.class)
    public void testAddDuplicateRendezvous() throws Exception {
        LocalDate date = LocalDate.now().plusDays(5);
        Rendezvous rendezvous = new Rendezvous(date, doctor, patient1, hospital, section);
        
        schedule.addRendezvous(rendezvous);
        schedule.addRendezvous(rendezvous); // Aynı randevuyu tekrar eklemeye çalış
    }

    @Test
    public void testCheckRendezvousDuplication_NoDuplicate() throws Exception {
        LocalDate date = LocalDate.now().plusDays(5);
        Rendezvous r1 = new Rendezvous(date, doctor, patient1, hospital, section);
        
        schedule.addRendezvous(r1);
        
        // Farklı randevu için exception fırlatmamalı
        Rendezvous r2 = new Rendezvous(date.plusDays(1), doctor, patient1, hospital, section);
        schedule.checkRendezvousDuplication(r2);
    }

    @Test(expected = DuplicateInfoException.class)
    public void testCheckRendezvousDuplication_WithDuplicate() throws Exception {
        LocalDate date = LocalDate.now().plusDays(5);
        Rendezvous rendezvous = new Rendezvous(date, doctor, patient1, hospital, section);
        
        schedule.addRendezvous(rendezvous);
        schedule.checkRendezvousDuplication(rendezvous);
    }

    @Test
    public void testDeleteRendezvous() throws Exception {
        LocalDate date = LocalDate.now().plusDays(5);
        Rendezvous rendezvous = new Rendezvous(date, doctor, patient1, hospital, section);
        
        schedule.addRendezvous(rendezvous);
        assertEquals("Randevu sayısı 1 olmalı", 1, schedule.getRendezvousCount());
        
        schedule.deleteRendezvous(rendezvous);
        
        assertEquals("Randevu sayısı 0 olmalı", 0, schedule.getRendezvousCount());
        assertFalse("Randezvous listede olmamalı", schedule.getSessions().contains(rendezvous));
    }

    @Test(expected = IDException.class)
    public void testDeleteRendezvous_NotFound() throws Exception {
        LocalDate date = LocalDate.now().plusDays(5);
        Rendezvous rendezvous = new Rendezvous(date, doctor, patient1, hospital, section);
        
        schedule.deleteRendezvous(rendezvous); // Schedule'da olmayan randevu
    }

    @Test
    public void testCheckDailyLimit_BelowLimit() throws Exception {
        LocalDate date = LocalDate.now().plusDays(10);
        
        // 9 randevu ekle (limit 10)
        for (int i = 0; i < 9; i++) {
            Rendezvous r = new Rendezvous(date, doctor, patient1, hospital, section);
            schedule.getSessions().add(r);
        }
        
        // Exception fırlatmamalı
        schedule.checkDailyLimit(date);
    }

    @Test(expected = DailyLimitException.class)
    public void testCheckDailyLimit_AtLimit() throws Exception {
        LocalDate date = LocalDate.now().plusDays(10);
        
        // 10 randevu ekle (limit 10)
        for (int i = 0; i < 10; i++) {
            Patient p = new Patient("Hasta " + i, 10000000000L + i, null);
            Rendezvous r = new Rendezvous(date, doctor, p, hospital, section);
            schedule.getSessions().add(r);
        }
        
        // Exception fırlatmalı
        schedule.checkDailyLimit(date);
    }

    @Test
    public void testCheckDailyLimit_DifferentDays() throws Exception {
        LocalDate date1 = LocalDate.now().plusDays(10);
        LocalDate date2 = LocalDate.now().plusDays(11);
        
        // 10 randevu date1 için ekle
        for (int i = 0; i < 10; i++) {
            Patient p = new Patient("Hasta " + i, 10000000000L + i, null);
            Rendezvous r = new Rendezvous(date1, doctor, p, hospital, section);
            schedule.getSessions().add(r);
        }
        
        // date2 için exception fırlatmamalı (farklı gün)
        schedule.checkDailyLimit(date2);
    }

    @Test
    public void testCheckDailyLimit_SameMonthDifferentDay() throws Exception {
        LocalDate date1 = LocalDate.of(2025, 5, 10);
        LocalDate date2 = LocalDate.of(2025, 5, 15);
        
        // 10 randevu date1 için ekle
        for (int i = 0; i < 10; i++) {
            Patient p = new Patient("Hasta " + i, 10000000000L + i, null);
            Rendezvous r = new Rendezvous(date1, doctor, p, hospital, section);
            schedule.getSessions().add(r);
        }
        
        // date2 için exception fırlatmamalı (aynı ay, farklı gün)
        schedule.checkDailyLimit(date2);
    }

    @Test
    public void testCheckDailyLimit_DifferentMonthSameDay() throws Exception {
        LocalDate date1 = LocalDate.of(2025, 5, 10);
        LocalDate date2 = LocalDate.of(2025, 6, 10);
        
        // 10 randevu date1 için ekle
        for (int i = 0; i < 10; i++) {
            Patient p = new Patient("Hasta " + i, 10000000000L + i, null);
            Rendezvous r = new Rendezvous(date1, doctor, p, hospital, section);
            schedule.getSessions().add(r);
        }
        
        // date2 için exception fırlatmamalı (farklı ay)
        schedule.checkDailyLimit(date2);
    }

    @Test
    public void testGetSessions() {
        assertNotNull("Sessions listesi null olmamalı", schedule.getSessions());
        assertEquals("Başlangıçta boş olmalı", 0, schedule.getSessions().size());
    }

    @Test
    public void testGetSessions_AfterAddingRendezvous() throws Exception {
        Rendezvous r1 = new Rendezvous(
            LocalDate.now().plusDays(1), 
            doctor, 
            patient1, 
            hospital, 
            section
        );
        
        schedule.addRendezvous(r1);
        
        assertEquals("1 randevu olmalı", 1, schedule.getSessions().size());
        assertTrue("Randezvous listede olmalı", schedule.getSessions().contains(r1));
    }

    @Test
    public void testScheduleWithDifferentMaxPatients() throws Exception {
        Schedule schedule5 = new Schedule(5);
        Schedule schedule20 = new Schedule(20);
        
        Doctor d1 = new Doctor("Dr. 1", 11111111111L, 1001);
        Doctor d2 = new Doctor("Dr. 2", 22222222222L, 1002);
        
        schedule5.setDoctor(d1);
        schedule20.setDoctor(d2);
        
        LocalDate date = LocalDate.now().plusDays(5);
        
        // 5 hasta için dolmalı
        for (int i = 0; i < 5; i++) {
            Patient p = new Patient("P" + i, 30000000000L + i, null);
            schedule5.getSessions().add(new Rendezvous(date, d1, p, hospital, section));
        }
        
        // Exception fırlatmalı
        try {
            schedule5.checkDailyLimit(date);
            fail("DailyLimitException bekleniyor");
        } catch (DailyLimitException e) {
            // Beklenen durum
        }
        
        // 20 hasta için henüz dolmamalı
        for (int i = 0; i < 15; i++) {
            Patient p = new Patient("P" + i, 40000000000L + i, null);
            schedule20.getSessions().add(new Rendezvous(date, d2, p, hospital, section));
        }
        
        // Exception fırlatmamalı
        schedule20.checkDailyLimit(date);
    }

    @Test
    public void testMultipleRendezvousSameDay() throws Exception {
        LocalDate date = LocalDate.now().plusDays(5);
        
        for (int i = 0; i < 5; i++) {
            Patient p = new Patient("Hasta " + i, 50000000000L + i, null);
            Rendezvous r = new Rendezvous(date, doctor, p, hospital, section);
            schedule.addRendezvous(r);
        }
        
        assertEquals("5 randevu olmalı", 5, schedule.getRendezvousCount());
    }

    @Test
    public void testRendezvousCountAfterMultipleOperations() throws Exception {
        LocalDate date = LocalDate.now().plusDays(5);
        
        Rendezvous r1 = new Rendezvous(date, doctor, patient1, hospital, section);
        Rendezvous r2 = new Rendezvous(date.plusDays(1), doctor, patient2, hospital, section);
        
        schedule.addRendezvous(r1);
        assertEquals("1 randevu olmalı", 1, schedule.getRendezvousCount());
        
        schedule.addRendezvous(r2);
        assertEquals("2 randevu olmalı", 2, schedule.getRendezvousCount());
        
        schedule.deleteRendezvous(r1);
        assertEquals("1 randevu olmalı", 1, schedule.getRendezvousCount());
        
        schedule.deleteRendezvous(r2);
        assertEquals("0 randevu olmalı", 0, schedule.getRendezvousCount());
    }

    @Test
    public void testScheduleWithZeroMaxPatients() {
        Schedule scheduleZero = new Schedule(0);
        LocalDate date = LocalDate.now().plusDays(5);
        
        try {
            scheduleZero.checkDailyLimit(date);
            fail("Limit 0 ise hemen exception fırlatmalı");
        } catch (DailyLimitException e) {
            // Beklenen durum
        }
    }

    @Test
    public void testScheduleWithLargeMaxPatients() throws Exception {
        Schedule scheduleLarge = new Schedule(1000);
        Doctor d = new Doctor("Dr. Large", 88888888888L, 8888);
        scheduleLarge.setDoctor(d);
        
        LocalDate date = LocalDate.now().plusDays(5);
        
        // 500 randevu ekle
        for (int i = 0; i < 500; i++) {
            Patient p = new Patient("P" + i, 60000000000L + i, null);
            scheduleLarge.getSessions().add(new Rendezvous(date, d, p, hospital, section));
        }
        
        // Henüz limite ulaşılmadı, exception fırlatmamalı
        scheduleLarge.checkDailyLimit(date);
    }
}

