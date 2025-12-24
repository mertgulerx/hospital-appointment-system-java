package mertguler.Hospital;

import mertguler.CRS.DateManager;
import mertguler.Enums.City;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Test sınıfı: Rendezvous sınıfının tüm fonksiyonalitelerini test eder
 */
public class RendezvousTest {
    private Rendezvous rendezvous;
    private Doctor doctor;
    private Patient patient;
    private Hospital hospital;
    private Section section;
    private LocalDate futureDate;
    private LocalDate pastDate;

    @Before
    public void setUp() {
        doctor = new Doctor("Dr. Ahmet Yılmaz", 12345678901L, 10001);
        patient = new Patient("Ayşe Demir", 98765432109L, LocalDate.of(1990, 1, 1));
        hospital = new Hospital("Ankara Şehir Hastanesi", City.ANKARA);
        section = new Section("Kardiyoloji", hospital, false);
        
        futureDate = LocalDate.now().plusDays(5);
        pastDate = LocalDate.now().minusDays(5);
        
        rendezvous = new Rendezvous(futureDate, doctor, patient, hospital, section);
    }

    @Test
    public void testRendezvousCreation() {
        assertNotNull("Rendezvous nesnesi oluşturulmalı", rendezvous);
        assertEquals("Tarih doğru olmalı", futureDate, rendezvous.getDate());
        assertEquals("Doktor doğru olmalı", doctor, rendezvous.getDoctor());
        assertEquals("Hasta doğru olmalı", patient, rendezvous.getPatient());
        assertEquals("Hastane doğru olmalı", hospital, rendezvous.getHospital());
        assertEquals("Bölüm doğru olmalı", section, rendezvous.getSection());
    }

    @Test
    public void testGetDate() {
        assertEquals("Tarih doğru dönmeli", futureDate, rendezvous.getDate());
    }

    @Test
    public void testGetDoctor() {
        assertEquals("Doktor doğru dönmeli", doctor, rendezvous.getDoctor());
    }

    @Test
    public void testGetPatient() {
        assertEquals("Hasta doğru dönmeli", patient, rendezvous.getPatient());
    }

    @Test
    public void testGetHospital() {
        assertEquals("Hastane doğru dönmeli", hospital, rendezvous.getHospital());
    }

    @Test
    public void testGetSection() {
        assertEquals("Bölüm doğru dönmeli", section, rendezvous.getSection());
    }

    @Test
    public void testIsExpired_InitiallyFalse() {
        assertFalse("Yeni randevu expired olmamalı", rendezvous.isExpired());
    }

    @Test
    public void testIsExpired_FutureDate() {
        rendezvous.updateExpired();
        assertFalse("Gelecek tarihli randevu expired olmamalı", rendezvous.isExpired());
    }

    @Test
    public void testIsExpired_PastDate() {
        Rendezvous pastRendezvous = new Rendezvous(pastDate, doctor, patient, hospital, section);
        pastRendezvous.updateExpired();
        
        assertTrue("Geçmiş tarihli randevu expired olmalı", pastRendezvous.isExpired());
    }

    @Test
    public void testIsExpired_TodayDate() {
        Rendezvous todayRendezvous = new Rendezvous(
            LocalDate.now(), 
            doctor, 
            patient, 
            hospital, 
            section
        );
        todayRendezvous.updateExpired();
        
        // Bugünkü randevu expired olmamalı (before kontrolü var)
        assertFalse("Bugünkü randevu expired olmamalı", todayRendezvous.isExpired());
    }

    @Test
    public void testUpdateExpired() {
        Rendezvous pastRendezvous = new Rendezvous(pastDate, doctor, patient, hospital, section);
        
        assertFalse("Başlangıçta expired false olmalı", pastRendezvous.isExpired());
        
        pastRendezvous.updateExpired();
        
        assertTrue("Update sonrası expired true olmalı", pastRendezvous.isExpired());
    }

    @Test
    public void testUpdateExpired_MultipleCallsFutureDate() {
        rendezvous.updateExpired();
        rendezvous.updateExpired();
        rendezvous.updateExpired();
        
        assertFalse("Gelecek tarihli randevu her zaman false kalmalı", rendezvous.isExpired());
    }

    @Test
    public void testUpdateExpired_MultipleCallsPastDate() {
        Rendezvous pastRendezvous = new Rendezvous(pastDate, doctor, patient, hospital, section);
        
        pastRendezvous.updateExpired();
        assertTrue("İlk update sonrası true olmalı", pastRendezvous.isExpired());
        
        pastRendezvous.updateExpired();
        assertTrue("İkinci update sonrası true kalmalı", pastRendezvous.isExpired());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue("Aynı nesne kendisine eşit olmalı", rendezvous.equals(rendezvous));
    }

    @Test
    public void testEquals_SameDateDoctorPatient() {
        Rendezvous rendezvous2 = new Rendezvous(futureDate, doctor, patient, hospital, section);
        
        assertTrue("Aynı tarih, doktor ve hasta ile eşit olmalı", rendezvous.equals(rendezvous2));
        assertTrue("Eşitlik simetrik olmalı", rendezvous2.equals(rendezvous));
    }

    @Test
    public void testEquals_DifferentDate() {
        Rendezvous rendezvous2 = new Rendezvous(
            futureDate.plusDays(1), 
            doctor, 
            patient, 
            hospital, 
            section
        );
        
        assertFalse("Farklı tarihle eşit olmamalı", rendezvous.equals(rendezvous2));
    }

    @Test
    public void testEquals_DifferentDoctor() {
        Doctor doctor2 = new Doctor("Dr. Farklı", 11111111111L, 10002);
        Rendezvous rendezvous2 = new Rendezvous(futureDate, doctor2, patient, hospital, section);
        
        assertFalse("Farklı doktorla eşit olmamalı", rendezvous.equals(rendezvous2));
    }

    @Test
    public void testEquals_DifferentPatient() {
        Patient patient2 = new Patient("Farklı Hasta", 22222222222L, null);
        Rendezvous rendezvous2 = new Rendezvous(futureDate, doctor, patient2, hospital, section);
        
        assertFalse("Farklı hastayla eşit olmamalı", rendezvous.equals(rendezvous2));
    }

    @Test
    public void testEquals_DifferentHospital() {
        Hospital hospital2 = new Hospital("Farklı Hastane", City.ISTANBUL);
        Rendezvous rendezvous2 = new Rendezvous(futureDate, doctor, patient, hospital2, section);
        
        // Equals metodu hastane kontrolü yapmıyor, ama farklı nesne olabilir
        // Tarih, doktor ve hasta aynı ise eşit sayılır
        assertTrue("Equals sadece tarih, doktor ve hastaya bakar", rendezvous.equals(rendezvous2));
    }

    @Test
    public void testEquals_DifferentSection() {
        Section section2 = new Section("Farklı Bölüm", hospital, false);
        Rendezvous rendezvous2 = new Rendezvous(futureDate, doctor, patient, hospital, section2);
        
        // Equals metodu bölüm kontrolü yapmıyor
        assertTrue("Equals sadece tarih, doktor ve hastaya bakar", rendezvous.equals(rendezvous2));
    }

    @Test
    public void testEquals_Null() {
        assertFalse("Null ile eşit olmamalı", rendezvous.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse("Farklı sınıf ile eşit olmamalı", rendezvous.equals("String"));
        assertFalse("Farklı sınıf ile eşit olmamalı", rendezvous.equals(123));
    }

    @Test
    public void testToString() {
        String result = rendezvous.toString();
        
        assertTrue("ToString hasta adı içermeli", result.contains(patient.getName()));
        assertTrue("ToString hastane adı içermeli", result.contains(hospital.getName()));
        assertTrue("ToString bölüm adı içermeli", result.contains(section.getName()));
        assertTrue("ToString doktor adı içermeli", result.contains(doctor.getName()));
        assertTrue("ToString tarih içermeli", result.contains(futureDate.toString()));
    }

    @Test
    public void testToString_Format() {
        String expected = patient.getName() + ", " + 
                         hospital.getName() + ", " + 
                         section.getName() + ", " + 
                         doctor.getName() + ", " + 
                         futureDate;
        
        assertEquals("ToString formatı doğru olmalı", expected, rendezvous.toString());
    }

    @Test
    public void testRendezvousImmutability() {
        LocalDate originalDate = rendezvous.getDate();
        Doctor originalDoctor = rendezvous.getDoctor();
        Patient originalPatient = rendezvous.getPatient();
        
        // Değerler final olduğu için değişmemeli
        assertEquals("Tarih değişmemeli", originalDate, rendezvous.getDate());
        assertEquals("Doktor değişmemeli", originalDoctor, rendezvous.getDoctor());
        assertEquals("Hasta değişmemeli", originalPatient, rendezvous.getPatient());
    }

    @Test
    public void testMultipleRendezvousSameDayDifferentPatients() {
        Patient patient2 = new Patient("Hasta 2", 33333333333L, null);
        Patient patient3 = new Patient("Hasta 3", 44444444444L, null);
        
        Rendezvous r1 = new Rendezvous(futureDate, doctor, patient, hospital, section);
        Rendezvous r2 = new Rendezvous(futureDate, doctor, patient2, hospital, section);
        Rendezvous r3 = new Rendezvous(futureDate, doctor, patient3, hospital, section);
        
        assertFalse("Farklı hastalarla eşit olmamalı", r1.equals(r2));
        assertFalse("Farklı hastalarla eşit olmamalı", r2.equals(r3));
        assertFalse("Farklı hastalarla eşit olmamalı", r1.equals(r3));
    }

    @Test
    public void testRendezvousWithSamePatientDifferentDays() {
        Rendezvous r1 = new Rendezvous(futureDate, doctor, patient, hospital, section);
        Rendezvous r2 = new Rendezvous(futureDate.plusDays(1), doctor, patient, hospital, section);
        Rendezvous r3 = new Rendezvous(futureDate.plusDays(2), doctor, patient, hospital, section);
        
        assertFalse("Farklı günlerde eşit olmamalı", r1.equals(r2));
        assertFalse("Farklı günlerde eşit olmamalı", r2.equals(r3));
        assertFalse("Farklı günlerde eşit olmamalı", r1.equals(r3));
    }

    @Test
    public void testExpiredStatusWithDateManager() {
        LocalDate currentDate = DateManager.getCurrentDate();
        
        Rendezvous yesterdayRendezvous = new Rendezvous(
            currentDate.minusDays(1), 
            doctor, 
            patient, 
            hospital, 
            section
        );
        
        Rendezvous tomorrowRendezvous = new Rendezvous(
            currentDate.plusDays(1), 
            doctor, 
            patient, 
            hospital, 
            section
        );
        
        yesterdayRendezvous.updateExpired();
        tomorrowRendezvous.updateExpired();
        
        assertTrue("Dünkü randevu expired olmalı", yesterdayRendezvous.isExpired());
        assertFalse("Yarınki randevu expired olmamalı", tomorrowRendezvous.isExpired());
    }

    @Test
    public void testRendezvousWithChildSection() {
        Section childSection = new Section("Çocuk Sağlığı", hospital, true);
        Patient childPatient = new Patient("Çocuk", 55555555555L, LocalDate.of(2015, 1, 1));
        
        Rendezvous childRendezvous = new Rendezvous(
            futureDate, 
            doctor, 
            childPatient, 
            hospital, 
            childSection
        );
        
        assertEquals("Bölüm doğru olmalı", childSection, childRendezvous.getSection());
        assertTrue("Child section olmalı", childRendezvous.getSection().isChildSection());
    }

    @Test
    public void testRendezvousDateEdgeCases() {
        // Çok uzak gelecek
        LocalDate farFuture = LocalDate.now().plusYears(1);
        Rendezvous farFutureRendezvous = new Rendezvous(
            farFuture, 
            doctor, 
            patient, 
            hospital, 
            section
        );
        farFutureRendezvous.updateExpired();
        assertFalse("Uzak gelecek expired olmamalı", farFutureRendezvous.isExpired());
        
        // Çok uzak geçmiş
        LocalDate farPast = LocalDate.now().minusYears(1);
        Rendezvous farPastRendezvous = new Rendezvous(
            farPast, 
            doctor, 
            patient, 
            hospital, 
            section
        );
        farPastRendezvous.updateExpired();
        assertTrue("Uzak geçmiş expired olmalı", farPastRendezvous.isExpired());
    }
}

