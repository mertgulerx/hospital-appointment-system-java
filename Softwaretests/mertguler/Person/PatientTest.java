package mertguler.Person;

import mertguler.CRS.CRS;
import mertguler.Enums.City;
import mertguler.Exceptions.RendezvousLimitException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Hospital.Section;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Test sınıfı: Patient sınıfının tüm fonksiyonalitelerini test eder
 */
public class PatientTest {
    private Patient patient1;
    private Patient patient2;
    private Patient patientWithBirthDate;
    private Patient patientWithoutBirthDate;
    
    private Hospital testHospital;
    private Section testSection;
    private Doctor testDoctor;

    @Before
    public void setUp() {
        // Normal hastalar
        patient1 = new Patient("Ayşe Yılmaz", 12345678901L, LocalDate.of(1990, 5, 15));
        patient2 = new Patient("Fatma Kaya", 98765432109L, LocalDate.of(1985, 3, 20));
        
        // Doğum tarihi olan ve olmayan hastalar
        patientWithBirthDate = new Patient("Çocuk Hasta", 11111111111L, LocalDate.of(2015, 1, 1));
        patientWithoutBirthDate = new Patient("Yaş Bilinmeyen", 22222222222L, null);
        
        // Test için hastane ve doktor oluştur
        testHospital = new Hospital("Test Hastanesi", 1, City.ANKARA);
        testSection = new Section("Test Bölümü", testHospital, false);
        testDoctor = new Doctor("Dr. Test", 33333333333L, 12345);
    }

    @Test
    public void testPatientCreation() {
        assertNotNull("Patient nesnesi oluşturulmalı", patient1);
        assertEquals("İsim doğru olmalı", "Ayşe Yılmaz", patient1.getName());
        assertEquals("TC kimlik doğru olmalı", 12345678901L, patient1.getNational_id());
    }

    @Test
    public void testGetAge_WithBirthDate() {
        int age = patientWithBirthDate.getAge();
        int expectedAge = LocalDate.now().getYear() - 2015;
        
        // Doğum günü geçmediyse yaş 1 az olabilir
        assertTrue("Yaş doğru hesaplanmalı", age == expectedAge || age == expectedAge - 1);
    }

    @Test
    public void testGetAge_WithoutBirthDate() {
        assertEquals("Doğum tarihi yoksa yaş 0 dönmeli", 0, patientWithoutBirthDate.getAge());
    }

    @Test
    public void testGetAge_AdultPatient() {
        int age = patient1.getAge();
        assertTrue("Yetişkin hasta 18 yaşından büyük olmalı", age > 18);
    }

    @Test
    public void testGetAge_ChildPatient() {
        int age = patientWithBirthDate.getAge();
        assertTrue("Çocuk hasta 18 yaşından küçük olmalı", age < 18);
    }

    @Test
    public void testAddRendezvous() {
        Rendezvous rendezvous = new Rendezvous(
            LocalDate.now().plusDays(5),
            testDoctor,
            patient1,
            testHospital,
            testSection
        );
        
        patient1.addRendezvous(rendezvous);
        assertEquals("Randevu sayısı 1 olmalı", 1, patient1.getRendezvousCount());
        assertTrue("Randevu listesinde olmalı", patient1.getRendezvouses().contains(rendezvous));
    }

    @Test
    public void testDeleteRendezvous() {
        Rendezvous rendezvous = new Rendezvous(
            LocalDate.now().plusDays(5),
            testDoctor,
            patient1,
            testHospital,
            testSection
        );
        
        patient1.addRendezvous(rendezvous);
        assertEquals("Randevu sayısı 1 olmalı", 1, patient1.getRendezvousCount());
        
        patient1.deleteRendezvous(rendezvous);
        assertEquals("Randevu sayısı 0 olmalı", 0, patient1.getRendezvousCount());
        assertFalse("Randevu listesinde olmamalı", patient1.getRendezvouses().contains(rendezvous));
    }

    @Test
    public void testGetRendezvousCount_EmptyList() {
        assertEquals("Yeni hasta randevu sayısı 0 olmalı", 0, patient1.getRendezvousCount());
    }

    @Test
    public void testGetRendezvousCount_MultipleRendezvous() {
        for (int i = 1; i <= 3; i++) {
            Rendezvous rendezvous = new Rendezvous(
                LocalDate.now().plusDays(i),
                testDoctor,
                patient1,
                testHospital,
                testSection
            );
            patient1.addRendezvous(rendezvous);
        }
        
        assertEquals("Randevu sayısı 3 olmalı", 3, patient1.getRendezvousCount());
    }

    @Test
    public void testCheckActiveRendezvousCount_BelowLimit() throws Exception {
        // Maksimum limitin altında randevu ekle
        int limit = CRS.MAX_RENDEZVOUS_PER_PATIENT - 1;
        
        for (int i = 0; i < limit; i++) {
            Rendezvous rendezvous = new Rendezvous(
                LocalDate.now().plusDays(i + 1),
                testDoctor,
                patient1,
                testHospital,
                testSection
            );
            patient1.addRendezvous(rendezvous);
        }
        
        // Exception fırlatmamalı
        patient1.checkActiveRendezvousCount();
    }

    @Test(expected = RendezvousLimitException.class)
    public void testCheckActiveRendezvousCount_AtLimit() throws Exception {
        // Maksimum limite kadar randevu ekle
        int limit = CRS.MAX_RENDEZVOUS_PER_PATIENT;
        
        for (int i = 0; i < limit; i++) {
            Rendezvous rendezvous = new Rendezvous(
                LocalDate.now().plusDays(i + 1),
                testDoctor,
                patient1,
                testHospital,
                testSection
            );
            patient1.addRendezvous(rendezvous);
        }
        
        // Exception fırlatmalı
        patient1.checkActiveRendezvousCount();
    }

    @Test
    public void testCheckActiveRendezvousCount_WithExpiredRendezvous() throws Exception {
        // Eski (süresi geçmiş) randevular
        for (int i = 1; i <= 5; i++) {
            Rendezvous expiredRendezvous = new Rendezvous(
                LocalDate.now().minusDays(i),
                testDoctor,
                patient1,
                testHospital,
                testSection
            );
            expiredRendezvous.updateExpired();
            patient1.addRendezvous(expiredRendezvous);
        }
        
        // Aktif randevular (limit altında)
        for (int i = 1; i <= 2; i++) {
            Rendezvous activeRendezvous = new Rendezvous(
                LocalDate.now().plusDays(i),
                testDoctor,
                patient1,
                testHospital,
                testSection
            );
            patient1.addRendezvous(activeRendezvous);
        }
        
        // Toplam 7 randevu var ama sadece 2'si aktif, exception fırlatmamalı
        patient1.checkActiveRendezvousCount();
    }

    @Test
    public void testGetRendezvouses() {
        assertTrue("Yeni hasta randevu listesi boş olmalı", patient1.getRendezvouses().isEmpty());
        
        Rendezvous rendezvous = new Rendezvous(
            LocalDate.now().plusDays(1),
            testDoctor,
            patient1,
            testHospital,
            testSection
        );
        patient1.addRendezvous(rendezvous);
        
        assertFalse("Randevu eklenince liste boş olmamalı", patient1.getRendezvouses().isEmpty());
        assertEquals("Liste boyutu 1 olmalı", 1, patient1.getRendezvouses().size());
    }

    @Test
    public void testToString() {
        String expected = "Ayşe Yılmaz, National ID: 12345678901";
        assertEquals("toString formatı doğru olmalı", expected, patient1.toString());
    }

    @Test
    public void testPatientInheritance() {
        assertTrue("Patient, Person'dan türemeli", patient1 instanceof Person);
    }

    @Test
    public void testMultiplePatientsIndependentRendezvous() {
        Rendezvous r1 = new Rendezvous(
            LocalDate.now().plusDays(1),
            testDoctor,
            patient1,
            testHospital,
            testSection
        );
        
        Rendezvous r2 = new Rendezvous(
            LocalDate.now().plusDays(2),
            testDoctor,
            patient2,
            testHospital,
            testSection
        );
        
        patient1.addRendezvous(r1);
        patient2.addRendezvous(r2);
        
        assertEquals("Her hasta kendi randevusuna sahip olmalı", 1, patient1.getRendezvousCount());
        assertEquals("Her hasta kendi randevusuna sahip olmalı", 1, patient2.getRendezvousCount());
        assertFalse("Randevular karışmamalı", patient1.getRendezvouses().contains(r2));
        assertFalse("Randevular karışmamalı", patient2.getRendezvouses().contains(r1));
    }

    @Test
    public void testBirthDateEdgeCases() {
        // Bugün doğan bebek
        Patient baby = new Patient("Bebek", 44444444444L, LocalDate.now());
        assertEquals("Bugün doğan bebeğin yaşı 0 olmalı", 0, baby.getAge());
        
        // 18 yaşında (tam bugün)
        Patient exactly18 = new Patient("18 Yaş", 55555555555L, 
            LocalDate.now().minusYears(18));
        assertTrue("18 yaşındaki hasta 17-18 yaş arasında olmalı", 
            exactly18.getAge() >= 17 && exactly18.getAge() <= 18);
    }

    @Test
    public void testRendezvousListNotNull() {
        Patient newPatient = new Patient("Yeni Hasta", 66666666666L, null);
        assertNotNull("Randevu listesi null olmamalı", newPatient.getRendezvouses());
        assertEquals("Yeni hastanın randevu sayısı 0 olmalı", 0, newPatient.getRendezvousCount());
    }
}

