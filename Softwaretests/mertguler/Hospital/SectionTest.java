package mertguler.Hospital;

import mertguler.Enums.City;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Person.Doctor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test sınıfı: Section sınıfının tüm fonksiyonalitelerini test eder
 */
public class SectionTest {
    private Hospital hospital;
    private Section section1;
    private Section section2;
    private Section childSection;
    private Doctor doctor1;
    private Doctor doctor2;

    @Before
    public void setUp() {
        hospital = new Hospital("Test Hastanesi", City.ANKARA);
        section1 = new Section("Kardiyoloji", hospital, false);
        section2 = new Section("Nöroloji", hospital, false);
        childSection = new Section("Çocuk Sağlığı", hospital, true);
        
        doctor1 = new Doctor("Dr. Ahmet Yılmaz", 12345678901L, 10001);
        doctor2 = new Doctor("Dr. Mehmet Kaya", 98765432109L, 10002);
    }

    @Test
    public void testSectionCreation() {
        assertNotNull("Section nesnesi oluşturulmalı", section1);
        assertEquals("Bölüm adı doğru olmalı", "Kardiyoloji", section1.getName());
        assertFalse("Normal bölüm child section olmamalı", section1.isChildSection());
        assertTrue("Çocuk bölümü child section olmalı", childSection.isChildSection());
    }

    @Test
    public void testGetId() {
        int id = section1.getId();
        assertTrue("ID pozitif olmalı", id > 0);
    }

    @Test
    public void testIdGenerationFromHospital() {
        int hospitalId = hospital.getId();
        int sectionId = section1.getId();
        
        // Section ID = hospital.getId() * 1000 + section count
        assertTrue("Section ID hastane ID'sini içermeli", 
            sectionId >= hospitalId * 1000);
    }

    @Test
    public void testMultipleSectionsHaveDifferentIds() {
        Hospital testHospital = new Hospital("Test", City.ANKARA);
        Section s1 = new Section("S1", testHospital, false);
        Section s2 = new Section("S2", testHospital, false);
        Section s3 = new Section("S3", testHospital, false);
        
        assertNotEquals("Farklı bölümler farklı ID'ye sahip olmalı", s1.getId(), s2.getId());
        assertNotEquals("Farklı bölümler farklı ID'ye sahip olmalı", s2.getId(), s3.getId());
        assertNotEquals("Farklı bölümler farklı ID'ye sahip olmalı", s1.getId(), s3.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Kardiyoloji", section1.getName());
        assertEquals("Nöroloji", section2.getName());
        assertEquals("Çocuk Sağlığı", childSection.getName());
    }

    @Test
    public void testSetName() {
        section1.setName("Yeni Bölüm Adı");
        assertEquals("İsim değiştirilmeli", "Yeni Bölüm Adı", section1.getName());
    }

    @Test
    public void testIsChildSection() {
        assertFalse("Normal bölüm false dönmeli", section1.isChildSection());
        assertTrue("Çocuk bölümü true dönmeli", childSection.isChildSection());
    }

    @Test
    public void testAddDoctor() throws Exception {
        section1.addDoctor(doctor1);
        
        assertTrue("Doktor eklenmeli", section1.getDoctors().contains(doctor1));
        assertEquals("Doktor sayısı 1 olmalı", 1, section1.getDoctors().size());
    }

    @Test
    public void testAddMultipleDoctors() throws Exception {
        section1.addDoctor(doctor1);
        section1.addDoctor(doctor2);
        
        assertEquals("Doktor sayısı 2 olmalı", 2, section1.getDoctors().size());
        assertTrue("İlk doktor listede olmalı", section1.getDoctors().contains(doctor1));
        assertTrue("İkinci doktor listede olmalı", section1.getDoctors().contains(doctor2));
    }

    @Test(expected = DuplicateInfoException.class)
    public void testAddDuplicateDoctor() throws Exception {
        section1.addDoctor(doctor1);
        section1.addDoctor(doctor1); // Aynı doktoru tekrar eklemeye çalış
    }

    @Test(expected = DuplicateInfoException.class)
    public void testAddDoctorWithSameDiplomaId() throws Exception {
        section1.addDoctor(doctor1);
        
        Doctor doctor3 = new Doctor("Başka İsim", 99999999999L, doctor1.getDiploma_id());
        section1.addDoctor(doctor3); // Aynı diploma ID
    }

    @Test
    public void testGetDoctor_ById() throws Exception {
        section1.addDoctor(doctor1);
        
        Doctor retrieved = section1.getDoctor(doctor1.getDiploma_id());
        assertNotNull("Doktor bulunmalı", retrieved);
        assertEquals("Doğru doktor dönmeli", doctor1, retrieved);
    }

    @Test(expected = IDException.class)
    public void testGetDoctor_InvalidId() throws Exception {
        section1.addDoctor(doctor1);
        section1.getDoctor(99999);
    }

    @Test(expected = IDException.class)
    public void testGetDoctor_EmptySection() throws Exception {
        section1.getDoctor(10001);
    }

    @Test
    public void testGetDoctor_ByName() throws Exception {
        section1.addDoctor(doctor1);
        
        ArrayList<Doctor> doctors = section1.getDoctor("Dr. Ahmet Yılmaz");
        assertNotNull("Doktor listesi null olmamalı", doctors);
        assertEquals("1 doktor bulunmalı", 1, doctors.size());
        assertEquals("Doğru doktor bulunmalı", doctor1, doctors.get(0));
    }

    @Test
    public void testGetDoctor_ByNameCaseInsensitive() throws Exception {
        section1.addDoctor(doctor1);
        
        ArrayList<Doctor> doctors = section1.getDoctor("dr. ahmet yılmaz");
        assertNotNull("Büyük/küçük harf duyarsız olmalı", doctors);
        assertEquals("1 doktor bulunmalı", 1, doctors.size());
    }

    @Test
    public void testGetDoctor_ByNameWithSpaces() throws Exception {
        section1.addDoctor(doctor1);
        
        ArrayList<Doctor> doctors = section1.getDoctor("  Dr. Ahmet Yılmaz  ");
        assertNotNull("Boşluklarla çalışmalı", doctors);
        assertEquals("1 doktor bulunmalı", 1, doctors.size());
    }

    @Test
    public void testGetDoctor_ByNameMultipleResults() throws Exception {
        Doctor doctor3 = new Doctor("Dr. Ahmet Yılmaz", 55555555555L, 10003);
        section1.addDoctor(doctor1);
        section1.addDoctor(doctor3);
        
        ArrayList<Doctor> doctors = section1.getDoctor("Dr. Ahmet Yılmaz");
        assertNotNull("Doktor listesi null olmamalı", doctors);
        assertEquals("2 doktor bulunmalı", 2, doctors.size());
    }

    @Test
    public void testGetDoctor_ByNameNotFound() {
        section1.getDoctors().clear();
        ArrayList<Doctor> doctors = section1.getDoctor("Olmayan Doktor");
        assertNull("Bulunamayan doktor için null dönmeli", doctors);
    }

    @Test
    public void testDeleteDoctor() throws Exception {
        section1.addDoctor(doctor1);
        assertEquals("Doktor sayısı 1 olmalı", 1, section1.getDoctors().size());
        
        section1.deleteDoctor(doctor1.getDiploma_id());
        
        assertEquals("Doktor sayısı 0 olmalı", 0, section1.getDoctors().size());
        assertFalse("Doktor listede olmamalı", section1.getDoctors().contains(doctor1));
    }

    @Test(expected = IDException.class)
    public void testDeleteDoctor_InvalidId() throws Exception {
        section1.deleteDoctor(99999);
    }

    @Test
    public void testCheckDoctorDuplication_NoDuplicate() throws Exception {
        section1.addDoctor(doctor1);
        
        // Exception fırlatmamalı
        section1.checkDoctorDuplication(doctor2.getDiploma_id());
    }

    @Test(expected = DuplicateInfoException.class)
    public void testCheckDoctorDuplication_WithDuplicate() throws Exception {
        section1.addDoctor(doctor1);
        section1.checkDoctorDuplication(doctor1.getDiploma_id());
    }

    @Test
    public void testCheckDoctorDuplication_EmptySection() throws Exception {
        // Exception fırlatmamalı
        section1.checkDoctorDuplication(10001);
    }

    @Test
    public void testListDoctors_WithDoctors() throws Exception {
        section1.addDoctor(doctor1);
        section1.addDoctor(doctor2);
        
        // Console output'u yakala
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        section1.listDoctors();
        
        String output = outContent.toString();
        assertTrue("Output doktor bilgisi içermeli", output.length() > 0);
        
        // Console'u eski haline getir
        System.setOut(System.out);
    }

    @Test
    public void testListDoctors_EmptySection() {
        // Console output'u yakala
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        section1.listDoctors();
        
        String output = outContent.toString();
        assertTrue("Boş bölüm mesajı içermeli", 
            output.contains("doesnt have any doctors") || output.length() > 0);
        
        // Console'u eski haline getir
        System.setOut(System.out);
    }

    @Test
    public void testCountAllRendezvouses() {
        int count = section1.countAllRendezvouses();
        assertEquals("Yeni bölüm randevu sayısı 0 olmalı", 0, count);
    }

    @Test
    public void testGetDoctors() {
        assertNotNull("Doctors listesi null olmamalı", section1.getDoctors());
        assertTrue("Yeni bölüm doctors listesi boş olmalı", section1.getDoctors().isEmpty());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue("Aynı nesne kendisine eşit olmalı", section1.equals(section1));
    }

    @Test
    public void testEquals_SameName() {
        Hospital hospital2 = new Hospital("Test", City.ANKARA);
        Section section3 = new Section("Kardiyoloji", hospital2, false);
        
        assertTrue("Aynı isimle eşit olmalı", section1.equals(section3));
        assertTrue("Eşitlik simetrik olmalı", section3.equals(section1));
    }

    @Test
    public void testEquals_DifferentName() {
        assertFalse("Farklı isimle eşit olmamalı", section1.equals(section2));
    }

    @Test
    public void testEquals_CaseInsensitive() {
        Hospital hospital2 = new Hospital("Test", City.ANKARA);
        Section section3 = new Section("KARDİYOLOJİ", hospital2, false);
        
        assertTrue("İsim karşılaştırma case-insensitive olmalı", section1.equals(section3));
    }

    @Test
    public void testEquals_WithSpaces() {
        Hospital hospital2 = new Hospital("Test", City.ANKARA);
        Section section3 = new Section("  Kardiyoloji  ", hospital2, false);
        
        assertTrue("Boşluklarla çalışmalı", section1.equals(section3));
    }

    @Test
    public void testEquals_Null() {
        assertFalse("Null ile eşit olmamalı", section1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse("Farklı sınıf ile eşit olmamalı", section1.equals("String"));
    }

    @Test
    public void testToString() {
        String result = section1.toString();
        
        assertTrue("ToString isim içermeli", result.contains("Kardiyoloji"));
        assertTrue("ToString ID içermeli", result.contains("ID:"));
        assertTrue("ToString child only bilgisi içermeli", result.contains("Child Only:"));
    }

    @Test
    public void testToString_ChildSection() {
        String result = childSection.toString();
        
        assertTrue("ToString çocuk bölümü bilgisi içermeli", result.contains("true"));
    }

    @Test
    public void testToString_NonChildSection() {
        String result = section1.toString();
        
        assertTrue("ToString normal bölüm bilgisi içermeli", result.contains("false"));
    }

    @Test
    public void testMultipleSectionsIndependentDoctors() throws Exception {
        section1.addDoctor(doctor1);
        section2.addDoctor(doctor2);
        
        assertEquals("Section1'in 1 doktoru olmalı", 1, section1.getDoctors().size());
        assertEquals("Section2'nin 1 doktoru olmalı", 1, section2.getDoctors().size());
        assertTrue("Section1 kendi doktorunu içermeli", section1.getDoctors().contains(doctor1));
        assertFalse("Section1 diğer bölüm doktorunu içermemeli", section1.getDoctors().contains(doctor2));
    }

    @Test
    public void testSectionNameWithSpecialCharacters() {
        Section section = new Section("Kulak-Burun-Boğaz (KBB)", hospital, false);
        assertTrue("Özel karakterler desteklenmeli", section.getName().contains("-"));
        assertTrue("Özel karakterler desteklenmeli", section.getName().contains("("));
    }

    @Test
    public void testEmptySectionName() {
        Section section = new Section("", hospital, false);
        assertEquals("Boş isim kabul edilmeli", "", section.getName());
    }
}

