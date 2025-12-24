package mertguler.CRS;

import mertguler.Enums.City;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Test sınıfı: HospitalManager sınıfının tüm fonksiyonalitelerini test eder
 */
public class HospitalManagerTest {
    private HospitalManager hospitalManager;
    private CRS crs;
    private HashMap<Integer, Hospital> hospitals;

    @Before
    public void setUp() {
        crs = new CRS();
        hospitals = new HashMap<>();
        hospitalManager = new HospitalManager(hospitals, crs);
        
        // HospitalManager constructor'da hospitalCount güncellenir
        HospitalManager.hospitalCount = 0;
    }

    @Test
    public void testHospitalManagerCreation() {
        assertNotNull("HospitalManager nesnesi oluşturulmalı", hospitalManager);
        assertNotNull("SectionManager oluşturulmalı", hospitalManager.getSectionManager());
    }

    @Test
    public void testCreateHospital() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        
        assertEquals("1 hastane oluşturulmalı", 1, hospitals.size());
        assertEquals("Hospital count güncellenm eli", 1, HospitalManager.hospitalCount);
    }

    @Test
    public void testCreateMultipleHospitals() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        hospitalManager.createHospital("İstanbul Hastanesi", City.ISTANBUL);
        hospitalManager.createHospital("İzmir Hastanesi", City.IZMIR);
        
        assertEquals("3 hastane oluşturulmalı", 3, hospitals.size());
        assertEquals("Hospital count doğru olmalı", 3, HospitalManager.hospitalCount);
    }

    @Test(expected = DuplicateInfoException.class)
    public void testCreateHospital_Duplicate() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA); // Aynı isim ve şehir
    }

    @Test
    public void testCreateHospital_SameNameDifferentCity() throws Exception {
        hospitalManager.createHospital("Şehir Hastanesi", City.ANKARA);
        hospitalManager.createHospital("Şehir Hastanesi", City.ISTANBUL);
        
        assertEquals("Farklı şehirlerde aynı isim olabilir", 2, hospitals.size());
    }

    @Test
    public void testCheckHospitalDuplication_NoDuplicate() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        
        // Farklı isim veya şehir için exception fırlatmamalı
        hospitalManager.checkHospitalDuplication("İstanbul Hastanesi", City.ISTANBUL);
        hospitalManager.checkHospitalDuplication("Ankara Hastanesi", City.ISTANBUL);
        hospitalManager.checkHospitalDuplication("İstanbul Hastanesi", City.ANKARA);
    }

    @Test(expected = DuplicateInfoException.class)
    public void testCheckHospitalDuplication_WithDuplicate() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        hospitalManager.checkHospitalDuplication("Ankara Hastanesi", City.ANKARA);
    }

    @Test
    public void testCheckHospitalDuplication_CaseInsensitive() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        
        try {
            hospitalManager.checkHospitalDuplication("ANKARA HASTANESİ", City.ANKARA);
            fail("DuplicateInfoException bekleniyor");
        } catch (DuplicateInfoException e) {
            // Beklenen durum
        }
    }

    @Test
    public void testCheckHospitalID_Exists() throws Exception {
        hospitalManager.createHospital("Test Hastanesi", City.ANKARA);
        
        Hospital hospital = hospitals.values().iterator().next();
        
        // Exception fırlatmamalı
        hospitalManager.checkHospitalID(hospital.getId());
    }

    @Test(expected = IDException.class)
    public void testCheckHospitalID_NotExists() throws Exception {
        hospitalManager.checkHospitalID(99999);
    }

    @Test
    public void testGetHospitalWithID() throws Exception {
        hospitalManager.createHospital("Test Hastanesi", City.ANKARA);
        
        Hospital hospital = hospitals.values().iterator().next();
        Hospital retrieved = hospitalManager.getHospitalWithID(hospital.getId());
        
        assertNotNull("Hastane bulunmalı", retrieved);
        assertEquals("Doğru hastane dönmeli", hospital, retrieved);
    }

    @Test(expected = IDException.class)
    public void testGetHospitalWithID_NotExists() throws Exception {
        hospitalManager.getHospitalWithID(99999);
    }

    @Test
    public void testGetHospitalWithName() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        
        Hospital retrieved = hospitalManager.getHospitalWithName("Ankara Hastanesi");
        
        assertNotNull("Hastane bulunmalı", retrieved);
        assertEquals("Hastane adı doğru olmalı", "Ankara Hastanesi", retrieved.getName());
    }

    @Test
    public void testGetHospitalWithName_CaseInsensitive() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        
        Hospital retrieved = hospitalManager.getHospitalWithName("ankara hastanesi");
        assertNotNull("Büyük/küçük harf duyarsız olmalı", retrieved);
    }

    @Test
    public void testGetHospitalWithName_WithSpaces() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        
        Hospital retrieved = hospitalManager.getHospitalWithName("  Ankara Hastanesi  ");
        assertNotNull("Boşluklarla çalışmalı", retrieved);
    }

    @Test(expected = IDException.class)
    public void testGetHospitalWithName_NotExists() throws Exception {
        hospitalManager.createHospital("Ankara Hastanesi", City.ANKARA);
        hospitalManager.getHospitalWithName("Olmayan Hastane");
    }

    @Test(expected = IDException.class)
    public void testGetHospitalWithName_EmptyMap() throws Exception {
        hospitalManager.getHospitalWithName("Test");
    }

    @Test
    public void testDeleteHospital() throws Exception {
        hospitalManager.createHospital("Test Hastanesi", City.ANKARA);
        
        Hospital hospital = hospitals.values().iterator().next();
        int hospitalId = hospital.getId();
        
        hospitalManager.deleteHospital(hospitalId);
        
        assertFalse("Hastane silinmeli", hospitals.containsKey(hospitalId));
    }

    @Test(expected = IDException.class)
    public void testDeleteHospital_NotExists() throws Exception {
        hospitalManager.deleteHospital(99999);
    }

    @Test
    public void testUpdateHospitalMap() {
        HashMap<Integer, Hospital> newHospitals = new HashMap<>();
        Hospital h1 = new Hospital("H1", 1, City.ANKARA);
        Hospital h2 = new Hospital("H2", 2, City.ISTANBUL);
        
        newHospitals.put(h1.getId(), h1);
        newHospitals.put(h2.getId(), h2);
        
        hospitalManager.updateHospitalMap(newHospitals);
        
        assertEquals("Hospital count güncellenmel i", 2, HospitalManager.hospitalCount);
    }

    @Test
    public void testCountAllSections_Empty() {
        int count = hospitalManager.countAllSections();
        assertEquals("Boş hastaneler için 0 olmalı", 0, count);
    }

    @Test
    public void testCountAllSections() throws Exception {
        hospitalManager.createHospital("H1", City.ANKARA);
        hospitalManager.createHospital("H2", City.ISTANBUL);
        
        for (Hospital hospital : hospitals.values()) {
            Section s1 = new Section("S1", hospital, false);
            Section s2 = new Section("S2", hospital, false);
            hospital.getSections().add(s1);
            hospital.getSections().add(s2);
        }
        
        int count = hospitalManager.countAllSections();
        assertEquals("Toplam 4 bölüm olmalı", 4, count);
    }

    @Test
    public void testCountAllDoctors_Empty() {
        int count = hospitalManager.countAllDoctors();
        assertEquals("Doktor yoksa 0 olmalı", 0, count);
    }

    @Test
    public void testCountAllDoctors() throws Exception {
        hospitalManager.createHospital("H1", City.ANKARA);
        
        Hospital hospital = hospitals.values().iterator().next();
        Section section = new Section("S1", hospital, false);
        hospital.getSections().add(section);
        
        Doctor d1 = new Doctor("D1", 11111111111L, 1001);
        Doctor d2 = new Doctor("D2", 22222222222L, 1002);
        section.getDoctors().add(d1);
        section.getDoctors().add(d2);
        
        int count = hospitalManager.countAllDoctors();
        assertEquals("2 doktor olmalı", 2, count);
    }

    @Test
    public void testGetHospitals() {
        HashMap<Integer, Hospital> retrieved = hospitalManager.getHospitals();
        assertNotNull("Hospitals map'i null olmamalı", retrieved);
        assertSame("Aynı referans olmalı", hospitals, retrieved);
    }

    @Test
    public void testSectionManager() {
        HospitalManager.SectionManager sectionManager = hospitalManager.getSectionManager();
        
        assertNotNull("SectionManager null olmamalı", sectionManager);
        assertNotNull("DoctorManager null olmamalı", sectionManager.getDoctorManager());
    }

    @Test
    public void testSectionManager_DeleteSection() throws Exception {
        hospitalManager.createHospital("Test", City.ANKARA);
        Hospital hospital = hospitals.values().iterator().next();
        
        Section section = new Section("Test Bölüm", hospital, false);
        hospital.addSection(section);
        
        assertEquals("1 bölüm olmalı", 1, hospital.getSections().size());
        
        hospitalManager.getSectionManager().deleteSection(hospital.getId(), section.getId());
        
        assertEquals("Bölüm silinmeli", 0, hospital.getSections().size());
    }

    @Test
    public void testDoctorManager_DeleteDoctor() throws Exception {
        hospitalManager.createHospital("Test", City.ANKARA);
        Hospital hospital = hospitals.values().iterator().next();
        
        Section section = new Section("Test Bölüm", hospital, false);
        hospital.addSection(section);
        
        Doctor doctor = new Doctor("Dr. Test", 12345678901L, 1001);
        section.addDoctor(doctor);
        
        assertEquals("1 doktor olmalı", 1, section.getDoctors().size());
        
        hospitalManager.getSectionManager().getDoctorManager()
            .deleteDoctor(hospital.getId(), section.getId(), doctor.getDiploma_id());
        
        assertEquals("Doktor silinmeli", 0, section.getDoctors().size());
    }

    @Test
    public void testMultipleHospitalsWithSections() throws Exception {
        // 3 hastane, her birinde 2 bölüm
        for (int i = 0; i < 3; i++) {
            hospitalManager.createHospital("H" + i, City.values()[i % City.values().length]);
        }
        
        for (Hospital hospital : hospitals.values()) {
            for (int j = 0; j < 2; j++) {
                Section section = new Section("S" + j, hospital, false);
                hospital.getSections().add(section);
            }
        }
        
        assertEquals("3 hastane olmalı", 3, hospitals.size());
        assertEquals("Toplam 6 bölüm olmalı", 6, hospitalManager.countAllSections());
    }

    @Test
    public void testLargeScaleHospitals() throws Exception {
        // 100 hastane oluştur
        for (int i = 0; i < 100; i++) {
            City city = City.values()[i % City.values().length];
            hospitalManager.createHospital("Hastane " + i, city);
        }
        
        assertEquals("100 hastane oluşturulmalı", 100, hospitals.size());
        assertEquals("Hospital count doğru olmalı", 100, HospitalManager.hospitalCount);
    }

    @Test
    public void testDeleteHospital_RemovesRendezvous() throws Exception {
        hospitalManager.createHospital("Test", City.ANKARA);
        Hospital hospital = hospitals.values().iterator().next();
        
        crs.getHospitals().put(hospital.getId(), hospital);
        
        hospitalManager.deleteHospital(hospital.getId());
        
        assertFalse("Hastane CRS'den silinmeli", crs.getHospitals().containsKey(hospital.getId()));
    }

    @Test
    public void testHospitalCountPersistence() throws Exception {
        hospitalManager.createHospital("H1", City.ANKARA);
        int count1 = HospitalManager.hospitalCount;
        
        hospitalManager.createHospital("H2", City.ISTANBUL);
        int count2 = HospitalManager.hospitalCount;
        
        assertEquals("Her hastane eklendiğinde count artmalı", count1 + 1, count2);
    }
}

