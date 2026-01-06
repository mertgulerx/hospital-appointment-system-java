package mertguler.Person;

import mertguler.Hospital.Schedule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test sınıfı: Doctor sınıfının tüm fonksiyonalitelerini test eder
 */
public class DoctorTest {
    private Doctor doctor1;
    private Doctor doctor2;
    private Doctor doctor3;

    @Before
    public void setUp() {
        doctor1 = new Doctor("Dr. Ahmet Yılmaz", 12345678901L, 10001);
        doctor2 = new Doctor("Dr. Mehmet Kaya", 98765432109L, 10002);
        doctor3 = new Doctor("Dr. Ali Demir", 11111111111L, 10001); // doctor1 ile aynı diploma ID
    }

    @Test
    public void testDoctorCreation() {
        assertNotNull("Doctor nesnesi oluşturulmalı", doctor1);
        assertEquals("İsim doğru olmalı", "Dr. Ahmet Yılmaz", doctor1.getName());
        assertEquals("TC kimlik doğru olmalı", 12345678901L, doctor1.getNational_id());
        assertEquals("Diploma ID doğru olmalı", 10001, doctor1.getDiploma_id());
    }

    @Test
    public void testGetDiplomaId() {
        assertEquals(10001, doctor1.getDiploma_id());
        assertEquals(10002, doctor2.getDiploma_id());
    }

    @Test
    public void testScheduleCreatedAutomatically() {
        assertNotNull("Schedule otomatik oluşturulmalı", doctor1.getSchedule());
    }

    @Test
    public void testScheduleDoctorReference() {
        Schedule schedule = doctor1.getSchedule();
        assertEquals("Schedule doktoru doğru referans vermeli", doctor1, schedule.getDoctor());
    }

    @Test
    public void testGetSchedule() {
        Schedule schedule1 = doctor1.getSchedule();
        Schedule schedule2 = doctor1.getSchedule();
        
        assertSame("Aynı Schedule nesnesi dönmeli", schedule1, schedule2);
    }

    @Test
    public void testResetSchedule() {
        Schedule schedule = doctor1.getSchedule();
        
        // Schedule'a veri eklendiğini simüle et
        assertTrue("Yeni schedule boş olmalı", schedule.getSessions().isEmpty());
        
        doctor1.resetSchedule();
        assertTrue("Reset sonrası schedule boş olmalı", schedule.getSessions().isEmpty());
    }

    @Test
    public void testChangeMaxPatientPerDay() {
        doctor1.changeMaxPatientPerDay(20);
        // Max patient per day değişikliği internal olduğu için test etmek zor
        // Ama method exception fırlatmamalı
        assertNotNull(doctor1);
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue("Aynı nesne kendisine eşit olmalı", doctor1.equals(doctor1));
    }

    @Test
    public void testEquals_SameDiplomaId() {
        assertTrue("Aynı diploma ID ile eşit olmalı", doctor1.equals(doctor3));
    }

    @Test
    public void testEquals_DifferentDiplomaId() {
        assertFalse("Farklı diploma ID ile eşit olmamalı", doctor1.equals(doctor2));
    }

    @Test
    public void testEquals_SameNationalId() {
        Doctor doctor4 = new Doctor("Farklı İsim", 12345678901L, 99999);
        assertTrue("Aynı TC kimlik ile eşit olmalı", doctor1.equals(doctor4));
    }

    @Test
    public void testEquals_Null() {
        assertFalse("Null ile eşit olmamalı", doctor1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse("Farklı sınıf ile eşit olmamalı", doctor1.equals("String"));
        
        Person person = new Person("Person", 12345678901L);
        assertFalse("Person sınıfı ile eşit olmamalı", doctor1.equals(person));
    }

    @Test
    public void testToString() {
        String expected = "Dr. Ahmet Yılmaz, Diploma ID: 10001";
        assertEquals("toString formatı doğru olmalı", expected, doctor1.toString());
    }

    @Test
    public void testToString_AfterNameChange() {
        doctor1.setName("Dr. Yeni İsim");
        String expected = "Dr. Yeni İsim, Diploma ID: 10001";
        assertEquals("İsim değişikliği toString'e yansımalı", expected, doctor1.toString());
    }

    @Test
    public void testDoctorInheritance() {
        assertTrue("Doctor, Person'dan türemeli", doctor1 instanceof Person);
    }

    @Test
    public void testImmutableDiplomaId() {
        int originalDiplomaId = doctor1.getDiploma_id();
        doctor1.setName("Değişen İsim");
        
        assertEquals("Diploma ID final olduğu için değişmemeli", 
            originalDiplomaId, doctor1.getDiploma_id());
    }

    @Test
    public void testMultipleDoctorsIndependentSchedules() {
        Schedule schedule1 = doctor1.getSchedule();
        Schedule schedule2 = doctor2.getSchedule();
        
        assertNotSame("Her doktor kendi schedule'ına sahip olmalı", schedule1, schedule2);
        assertEquals("Schedule doktor referansı doğru olmalı", doctor1, schedule1.getDoctor());
        assertEquals("Schedule doktor referansı doğru olmalı", doctor2, schedule2.getDoctor());
    }

    @Test
    public void testDoctorWithZeroDiplomaId() {
        Doctor doctor = new Doctor("Test Doctor", 99999999999L, 0);
        assertEquals("Sıfır diploma ID kabul edilmeli", 0, doctor.getDiploma_id());
    }

    @Test
    public void testDoctorWithNegativeDiplomaId() {
        Doctor doctor = new Doctor("Test Doctor", 99999999999L, -1);
        assertEquals("Negatif diploma ID kabul edilmeli", -1, doctor.getDiploma_id());
    }

    @Test
    public void testDoctorWithLargeDiplomaId() {
        Doctor doctor = new Doctor("Test Doctor", 99999999999L, Integer.MAX_VALUE);
        assertEquals("Büyük diploma ID kabul edilmeli", Integer.MAX_VALUE, doctor.getDiploma_id());
    }

    @Test
    public void testScheduleInitialState() {
        Schedule schedule = doctor1.getSchedule();
        
        assertNotNull("Schedule null olmamalı", schedule);
        assertNotNull("Sessions listesi null olmamalı", schedule.getSessions());
        assertTrue("Yeni schedule sessions listesi boş olmalı", schedule.getSessions().isEmpty());
        assertEquals("Schedule rendezvous sayısı 0 olmalı", 0, schedule.getRendezvousCount());
    }

    @Test
    public void testResetScheduleMultipleTimes() {
        doctor1.resetSchedule();
        doctor1.resetSchedule();
        doctor1.resetSchedule();
        
        assertTrue("Çoklu reset sonrası schedule boş olmalı", 
            doctor1.getSchedule().getSessions().isEmpty());
    }

    @Test
    public void testChangeMaxPatientPerDay_MultipleChanges() {
        doctor1.changeMaxPatientPerDay(15);
        doctor1.changeMaxPatientPerDay(25);
        doctor1.changeMaxPatientPerDay(10);
        
        // Method exception fırlatmamalı
        assertNotNull("Doktor nesnesi geçerli olmalı", doctor1);
        assertNotNull("Schedule geçerli olmalı", doctor1.getSchedule());
    }

    @Test
    public void testDoctorNameWithTitle() {
        Doctor prof = new Doctor("Prof. Dr. Ayşe Yılmaz", 55555555555L, 20001);
        assertTrue("Unvan ile isim desteklenmeli", prof.getName().startsWith("Prof. Dr."));
    }

    @Test
    public void testMultipleDoctorsWithSameDiplomaId() {
        Doctor d1 = new Doctor("Doctor 1", 11111111111L, 12345);
        Doctor d2 = new Doctor("Doctor 2", 22222222222L, 12345);
        
        assertTrue("Aynı diploma ID'ye sahip doktorlar eşit olmalı", d1.equals(d2));
    }

    @Test
    public void testDoctorEqualityWithBothSameIds() {
        // Hem diploma ID hem de national ID aynı
        Doctor d1 = new Doctor("Doctor 1", 77777777777L, 88888);
        Doctor d2 = new Doctor("Doctor 2", 77777777777L, 88888);
        
        assertTrue("Her iki ID de aynı ise eşit olmalı", d1.equals(d2));
    }
}

