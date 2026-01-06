package mertguler.CRS;

import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Person.Patient;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Test sınıfı: PatientManager sınıfının tüm fonksiyonalitelerini test eder
 */
public class PatientManagerTest {
    private PatientManager patientManager;
    private CRS crs;
    private HashMap<Long, Patient> patients;
    private Patient patient1;
    private Patient patient2;

    @Before
    public void setUp() {
        crs = new CRS();
        patients = new HashMap<>();
        patientManager = new PatientManager(patients, crs);
        
        patient1 = new Patient("Ayşe Yılmaz", 12345678901L, LocalDate.of(1990, 5, 15));
        patient2 = new Patient("Mehmet Demir", 98765432109L, LocalDate.of(1985, 3, 20));
    }

    @Test
    public void testPatientManagerCreation() {
        assertNotNull("PatientManager nesnesi oluşturulmalı", patientManager);
    }

    @Test
    public void testUpdatePatientsMap() {
        HashMap<Long, Patient> newPatients = new HashMap<>();
        newPatients.put(patient1.getNational_id(), patient1);
        newPatients.put(patient2.getNational_id(), patient2);
        
        boolean result = patientManager.updatePatientsMap(newPatients);
        
        assertTrue("Update başarılı olmalı", result);
    }

    @Test
    public void testCheckPatientDuplication_NoDuplicate() throws Exception {
        patients.put(patient1.getNational_id(), patient1);
        
        // Farklı TC kimlik için exception fırlatmamalı
        patientManager.checkPatientDuplication(patient2.getNational_id());
    }

    @Test(expected = DuplicateInfoException.class)
    public void testCheckPatientDuplication_WithDuplicate() throws Exception {
        patients.put(patient1.getNational_id(), patient1);
        
        // Aynı TC kimlik için exception fırlatmalı
        patientManager.checkPatientDuplication(patient1.getNational_id());
    }

    @Test
    public void testCheckPatientDuplication_EmptyMap() throws Exception {
        // Boş map için exception fırlatmamalı
        patientManager.checkPatientDuplication(12345678901L);
    }

    @Test
    public void testCheckPatientID_Exists() throws Exception {
        patients.put(patient1.getNational_id(), patient1);
        
        // Exception fırlatmamalı
        patientManager.checkPatientID(patient1.getNational_id());
    }

    @Test(expected = IDException.class)
    public void testCheckPatientID_NotExists() throws Exception {
        patients.put(patient1.getNational_id(), patient1);
        
        // Olmayan TC kimlik için exception fırlatmalı
        patientManager.checkPatientID(99999999999L);
    }

    @Test(expected = IDException.class)
    public void testCheckPatientID_EmptyMap() throws Exception {
        // Boş map için exception fırlatmalı
        patientManager.checkPatientID(12345678901L);
    }

    @Test
    public void testGetPatient_Exists() throws Exception {
        patients.put(patient1.getNational_id(), patient1);
        
        Patient retrieved = patientManager.getPatient(patient1.getNational_id());
        
        assertNotNull("Hasta bulunmalı", retrieved);
        assertEquals("Doğru hasta dönmeli", patient1, retrieved);
    }

    @Test(expected = IDException.class)
    public void testGetPatient_NotExists() throws Exception {
        patients.put(patient1.getNational_id(), patient1);
        
        patientManager.getPatient(99999999999L);
    }

    @Test
    public void testGetPatient_MultiplePatients() throws Exception {
        patients.put(patient1.getNational_id(), patient1);
        patients.put(patient2.getNational_id(), patient2);
        
        Patient retrieved1 = patientManager.getPatient(patient1.getNational_id());
        Patient retrieved2 = patientManager.getPatient(patient2.getNational_id());
        
        assertEquals("İlk hasta doğru olmalı", patient1, retrieved1);
        assertEquals("İkinci hasta doğru olmalı", patient2, retrieved2);
    }

    @Test
    public void testPatientDeleter() throws Exception {
        // Hasta ve randevu ekle
        patients.put(patient1.getNational_id(), patient1);
        
        // Silme işlemi
        patientManager.patientDeleter(patient1.getNational_id());
        
        // Hasta artık map'te olmamalı
        assertFalse("Hasta silinmeli", patients.containsKey(patient1.getNational_id()));
    }

    @Test(expected = IDException.class)
    public void testPatientDeleter_NotExists() throws Exception {
        patientManager.patientDeleter(99999999999L);
    }

    @Test
    public void testPatientDeleter_RemovesRendezvous() throws Exception {
        // Hasta ekle
        patients.put(patient1.getNational_id(), patient1);
        
        // CRS'ye hasta ekle (update ile)
        crs.getPatients().put(patient1.getNational_id(), patient1);
        
        // Silme işlemi
        patientManager.patientDeleter(patient1.getNational_id());
        
        // Hasta silinmeli
        assertFalse("Hasta CRS'den silinmeli", crs.getPatients().containsKey(patient1.getNational_id()));
    }

    @Test
    public void testMultiplePatientsManagement() throws Exception {
        // Çok sayıda hasta ekle
        for (int i = 0; i < 10; i++) {
            long nationalId = 10000000000L + i;
            Patient patient = new Patient("Hasta " + i, nationalId, null);
            patients.put(nationalId, patient);
        }
        
        // Tüm hastaları kontrol et
        for (int i = 0; i < 10; i++) {
            long nationalId = 10000000000L + i;
            patientManager.checkPatientID(nationalId);
            
            Patient retrieved = patientManager.getPatient(nationalId);
            assertNotNull("Hasta bulunmalı", retrieved);
            assertEquals("TC kimlik doğru olmalı", nationalId, retrieved.getNational_id());
        }
    }

    @Test
    public void testPatientDeleter_Multiple() throws Exception {
        // 3 hasta ekle
        Patient p1 = new Patient("P1", 11111111111L, null);
        Patient p2 = new Patient("P2", 22222222222L, null);
        Patient p3 = new Patient("P3", 33333333333L, null);
        
        patients.put(p1.getNational_id(), p1);
        patients.put(p2.getNational_id(), p2);
        patients.put(p3.getNational_id(), p3);
        
        assertEquals("3 hasta olmalı", 3, patients.size());
        
        // Ortadakini sil
        crs.getPatients().putAll(patients);
        patientManager.patientDeleter(p2.getNational_id());
        
        assertEquals("2 hasta kalmalı", 2, patients.size());
        assertTrue("P1 kalmalı", patients.containsKey(p1.getNational_id()));
        assertFalse("P2 silinmeli", patients.containsKey(p2.getNational_id()));
        assertTrue("P3 kalmalı", patients.containsKey(p3.getNational_id()));
    }

    @Test
    public void testCheckPatientDuplication_AfterUpdate() throws Exception {
        patients.put(patient1.getNational_id(), patient1);
        
        HashMap<Long, Patient> newPatients = new HashMap<>();
        newPatients.put(patient2.getNational_id(), patient2);
        
        patientManager.updatePatientsMap(newPatients);
        
        // patient1 artık map'te yok, duplicate olmamalı
        patientManager.checkPatientDuplication(patient1.getNational_id());
        
        // patient2 map'te var, duplicate olmalı
        try {
            patientManager.checkPatientDuplication(patient2.getNational_id());
            fail("DuplicateInfoException bekleniyor");
        } catch (DuplicateInfoException e) {
            // Beklenen durum
        }
    }

    @Test
    public void testGetPatient_AfterUpdate() throws Exception {
        patients.put(patient1.getNational_id(), patient1);
        
        Patient retrieved1 = patientManager.getPatient(patient1.getNational_id());
        assertEquals("Update öncesi hasta bulunmalı", patient1, retrieved1);
        
        // Map'i güncelle
        HashMap<Long, Patient> newPatients = new HashMap<>();
        newPatients.put(patient2.getNational_id(), patient2);
        patientManager.updatePatientsMap(newPatients);
        
        // patient1 artık bulunamaz
        try {
            patientManager.getPatient(patient1.getNational_id());
            fail("IDException bekleniyor");
        } catch (IDException e) {
            // Beklenen durum
        }
        
        // patient2 bulunmalı
        Patient retrieved2 = patientManager.getPatient(patient2.getNational_id());
        assertEquals("Update sonrası yeni hasta bulunmalı", patient2, retrieved2);
    }

    @Test
    public void testPatientManagerWithEmptyMap() throws Exception {
        // Boş map ile test
        PatientManager emptyManager = new PatientManager(new HashMap<>(), crs);
        
        try {
            emptyManager.checkPatientID(12345678901L);
            fail("IDException bekleniyor");
        } catch (IDException e) {
            // Beklenen durum
        }
        
        // Duplicate kontrolü exception fırlatmamalı
        emptyManager.checkPatientDuplication(12345678901L);
    }

    @Test
    public void testPatientManagerWithLargeDataset() throws Exception {
        // 1000 hasta ekle
        for (int i = 0; i < 1000; i++) {
            long nationalId = 20000000000L + i;
            Patient patient = new Patient("Hasta " + i, nationalId, null);
            patients.put(nationalId, patient);
        }
        
        // Rastgele hasta sorgula
        Patient retrieved = patientManager.getPatient(20000000500L);
        assertNotNull("500. hasta bulunmalı", retrieved);
        assertEquals("TC kimlik doğru olmalı", 20000000500L, retrieved.getNational_id());
        
        // Var olmayan hasta
        try {
            patientManager.getPatient(99999999999L);
            fail("IDException bekleniyor");
        } catch (IDException e) {
            // Beklenen durum
        }
    }

    @Test
    public void testCheckPatientID_BoundaryValues() throws Exception {
        // Minimum valid TC kimlik (11 haneli)
        long minId = 10000000000L;
        Patient minPatient = new Patient("Min", minId, null);
        patients.put(minId, minPatient);
        
        patientManager.checkPatientID(minId);
        
        // Maximum valid TC kimlik (11 haneli)
        long maxId = 99999999999L;
        Patient maxPatient = new Patient("Max", maxId, null);
        patients.put(maxId, maxPatient);
        
        patientManager.checkPatientID(maxId);
    }

    @Test
    public void testPatientDeleter_PreservesOthers() throws Exception {
        // 5 hasta ekle
        for (int i = 0; i < 5; i++) {
            long id = 30000000000L + i;
            Patient p = new Patient("P" + i, id, null);
            patients.put(id, p);
            crs.getPatients().put(id, p);
        }
        
        // 3. hastayı sil
        long deleteId = 30000000002L;
        patientManager.patientDeleter(deleteId);
        
        // Diğer hastalar etkilenmemeli
        for (int i = 0; i < 5; i++) {
            long id = 30000000000L + i;
            if (id == deleteId) {
                assertFalse("Silinen hasta olmamalı", patients.containsKey(id));
            } else {
                assertTrue("Diğer hastalar korunmalı", patients.containsKey(id));
            }
        }
    }
}

