package mertguler.CRS;

import mertguler.Enums.City;
import mertguler.Exceptions.*;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Test sınıfı: CRS (Central Reservation System) sınıfının tüm fonksiyonalitelerini test eder
 */
public class CRSTest {
    private CRS crs;
    private Hospital hospital;
    private Section section;
    private Section childSection;
    private Doctor doctor;
    private Patient patient;
    private Patient childPatient;
    private LocalDate futureDate;

    @Before
    public void setUp() {
        crs = new CRS();
        
        // Test için ayarlar
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
        CRS.dataPath = "test_data.ser";
        
        // Test verileri
        hospital = new Hospital("Test Hastanesi", City.ANKARA);
        section = new Section("Kardiyoloji", hospital, false);
        childSection = new Section("Çocuk Sağlığı", hospital, true);
        
        doctor = new Doctor("Dr. Test", 12345678901L, 10001);
        patient = new Patient("Test Hasta", 98765432109L, LocalDate.of(1990, 1, 1));
        childPatient = new Patient("Çocuk Hasta", 11111111111L, LocalDate.of(2015, 1, 1));
        
        futureDate = LocalDate.now().plusDays(5);
        
        // CRS'ye ekle
        crs.getHospitals().put(hospital.getId(), hospital);
        crs.getPatients().put(patient.getNational_id(), patient);
        crs.getPatients().put(childPatient.getNational_id(), childPatient);
        
        try {
            hospital.addSection(section);
            hospital.addSection(childSection);
            section.addDoctor(doctor);
        } catch (Exception e) {
            fail("Setup sırasında hata: " + e.getMessage());
        }
    }

    @Test
    public void testCRSCreation() {
        assertNotNull("CRS nesnesi oluşturulmalı", crs);
        assertNotNull("Patients map null olmamalı", crs.getPatients());
        assertNotNull("Hospitals map null olmamalı", crs.getHospitals());
        assertNotNull("Rendezvouses list null olmamalı", crs.getRendezvouses());
    }

    @Test
    public void testGetHospitalManager() {
        assertNotNull("HospitalManager null olmamalı", crs.getHospitalManager());
    }

    @Test
    public void testGetPatientManager() {
        assertNotNull("PatientManager null olmamalı", crs.getPatientManager());
    }

    @Test
    public void testMakeRendezvous_Success() throws Exception {
        boolean result = crs.makeRendezvous(
            patient.getNational_id(),
            hospital.getId(),
            section.getId(),
            doctor.getDiploma_id(),
            futureDate
        );
        
        assertTrue("Randezvou oluşturulmalı", result);
        assertEquals("Randezvous sayısı 1 olmalı", 1, crs.getRendezvousCount());
    }

    @Test(expected = IDException.class)
    public void testMakeRendezvous_InvalidPatient() throws Exception {
        crs.makeRendezvous(
            99999999999L, // Geçersiz patient ID
            hospital.getId(),
            section.getId(),
            doctor.getDiploma_id(),
            futureDate
        );
    }

    @Test(expected = IDException.class)
    public void testMakeRendezvous_InvalidHospital() throws Exception {
        crs.makeRendezvous(
            patient.getNational_id(),
            99999, // Geçersiz hospital ID
            section.getId(),
            doctor.getDiploma_id(),
            futureDate
        );
    }

    @Test(expected = IDException.class)
    public void testMakeRendezvous_InvalidSection() throws Exception {
        crs.makeRendezvous(
            patient.getNational_id(),
            hospital.getId(),
            99999, // Geçersiz section ID
            doctor.getDiploma_id(),
            futureDate
        );
    }

    @Test(expected = IDException.class)
    public void testMakeRendezvous_InvalidDoctor() throws Exception {
        crs.makeRendezvous(
            patient.getNational_id(),
            hospital.getId(),
            section.getId(),
            99999, // Geçersiz doctor ID
            futureDate
        );
    }

    @Test(expected = DateTimeException.class)
    public void testMakeRendezvous_PastDate() throws Exception {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        crs.makeRendezvous(
            patient.getNational_id(),
            hospital.getId(),
            section.getId(),
            doctor.getDiploma_id(),
            pastDate
        );
    }

    @Test(expected = DateTimeException.class)
    public void testMakeRendezvous_TooFarFuture() throws Exception {
        LocalDate tooFar = LocalDate.now().plusDays(CRS.RENDEZVOUS_DAY_LIMIT + 1);
        crs.makeRendezvous(
            patient.getNational_id(),
            hospital.getId(),
            section.getId(),
            doctor.getDiploma_id(),
            tooFar
        );
    }

    @Test(expected = ChildOnlyException.class)
    public void testMakeRendezvous_AdultInChildSection() throws Exception {
        crs.makeRendezvous(
            patient.getNational_id(), // Yetişkin hasta
            hospital.getId(),
            childSection.getId(), // Çocuk bölümü
            doctor.getDiploma_id(),
            futureDate
        );
    }

    @Test
    public void testMakeRendezvous_ChildInChildSection() throws Exception {
        // Çocuk hastayı çocuk bölümüne kaydet - başarılı olmalı
        boolean result = crs.makeRendezvous(
            childPatient.getNational_id(),
            hospital.getId(),
            childSection.getId(),
            doctor.getDiploma_id(),
            futureDate
        );
        
        assertTrue("Çocuk hasta çocuk bölümüne kaydedilmeli", result);
    }

    @Test
    public void testCheckChildSection_Adult() throws Exception {
        try {
            crs.checkChildSection(patient, childSection);
            fail("ChildOnlyException bekleniyor");
        } catch (ChildOnlyException e) {
            // Beklenen durum
            assertTrue(e.getMessage().contains("over Age 18"));
        }
    }

    @Test
    public void testCheckChildSection_Child() throws Exception {
        // Exception fırlatmamalı
        crs.checkChildSection(childPatient, childSection);
    }

    @Test
    public void testCheckChildSection_NormalSection() throws Exception {
        // Normal bölüm için yaş kontrolü yapılmamalı
        crs.checkChildSection(patient, section);
        crs.checkChildSection(childPatient, section);
    }

    @Test
    public void testAddRendezvous() throws Exception {
        Rendezvous rendezvous = new Rendezvous(futureDate, doctor, patient, hospital, section);
        
        crs.addRendezvous(doctor, rendezvous, patient);
        
        assertEquals("Randezvous sayısı 1 olmalı", 1, crs.getRendezvousCount());
        assertTrue("Hasta randevuya sahip olmalı", patient.getRendezvouses().contains(rendezvous));
        assertTrue("Doktor schedule'ında olmalı", doctor.getSchedule().getSessions().contains(rendezvous));
    }

    @Test
    public void testDeleteRendezvous() throws Exception {
        Rendezvous rendezvous = new Rendezvous(futureDate, doctor, patient, hospital, section);
        crs.addRendezvous(doctor, rendezvous, patient);
        
        assertEquals("1 randezvous olmalı", 1, crs.getRendezvousCount());
        
        crs.deleteRendezvous(rendezvous);
        
        assertEquals("Randezvous silinmeli", 0, crs.getRendezvousCount());
        assertFalse("Hasta randevusundan silinmeli", patient.getRendezvouses().contains(rendezvous));
        assertFalse("Doktor schedule'ından silinmeli", doctor.getSchedule().getSessions().contains(rendezvous));
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteRendezvous_Null() throws Exception {
        crs.deleteRendezvous(null);
    }

    @Test
    public void testCheckValidity_Success() throws Exception {
        // Tüm validasyonlar geçmeli
        crs.checkValidity(patient.getNational_id(), doctor, futureDate);
    }

    @Test(expected = IDException.class)
    public void testCheckValidity_InvalidPatient() throws Exception {
        crs.checkValidity(99999999999L, doctor, futureDate);
    }

    @Test(expected = RendezvousLimitException.class)
    public void testCheckValidity_RendezvousLimit() throws Exception {
        // Maksimum sayıda randevu ekle
        for (int i = 0; i < CRS.MAX_RENDEZVOUS_PER_PATIENT; i++) {
            Rendezvous r = new Rendezvous(
                futureDate.plusDays(i),
                doctor,
                patient,
                hospital,
                section
            );
            patient.addRendezvous(r);
        }
        
        crs.checkValidity(patient.getNational_id(), doctor, futureDate.plusDays(10));
    }

    @Test(expected = DailyLimitException.class)
    public void testCheckValidity_DailyLimit() throws Exception {
        // Doktorun günlük limitini doldur
        for (int i = 0; i < 10; i++) {
            Patient p = new Patient("P" + i, 20000000000L + i, null);
            Rendezvous r = new Rendezvous(futureDate, doctor, p, hospital, section);
            doctor.getSchedule().getSessions().add(r);
        }
        
        crs.checkValidity(patient.getNational_id(), doctor, futureDate);
    }

    @Test
    public void testUpdateExpired() throws Exception {
        // Geçmiş ve gelecek randevular ekle
        LocalDate pastDate = LocalDate.now().minusDays(5);
        
        Rendezvous pastRendezvous = new Rendezvous(pastDate, doctor, patient, hospital, section);
        Rendezvous futureRendezvous = new Rendezvous(futureDate, doctor, patient, hospital, section);
        
        crs.getRendezvouses().add(pastRendezvous);
        crs.getRendezvouses().add(futureRendezvous);
        
        crs.updateExpired();
        
        assertTrue("Geçmiş randevu expired olmalı", pastRendezvous.isExpired());
        assertFalse("Gelecek randevu expired olmamalı", futureRendezvous.isExpired());
    }

    @Test
    public void testUpdateExpired_LargeDataset() {
        // Çok sayıda randevu ekle (multithreading testi)
        for (int i = 0; i < 1000; i++) {
            LocalDate date = i < 500 ? 
                LocalDate.now().minusDays(i + 1) : 
                LocalDate.now().plusDays(i - 500 + 1);
            
            Patient p = new Patient("P" + i, 30000000000L + i, null);
            Rendezvous r = new Rendezvous(date, doctor, p, hospital, section);
            crs.getRendezvouses().add(r);
        }
        
        crs.updateExpired();
        
        // İlk 500 expired, sonraki 500 aktif olmalı
        int expiredCount = 0;
        for (Rendezvous r : crs.getRendezvouses()) {
            if (r.isExpired()) expiredCount++;
        }
        
        assertEquals("500 randevu expired olmalı", 500, expiredCount);
    }

    @Test
    public void testSaveTablesToDisk() {
        boolean result = crs.saveTablesToDisk();
        assertTrue("Kayıt başarılı olmalı", result);
        
        // Dosya oluşturulmuş mu kontrol et
        File file = new File(CRS.dataPath);
        assertTrue("Dosya oluşturulmalı", file.exists());
        
        // Temizlik
        file.delete();
    }

    @Test
    public void testLoadTablesFromDisk() throws Exception {
        // Önce veri kaydet
        crs.getPatients().put(patient.getNational_id(), patient);
        crs.getHospitals().put(hospital.getId(), hospital);
        crs.saveTablesToDisk();
        
        // Yeni CRS oluştur ve veriyi yükle
        CRS newCRS = new CRS();
        boolean result = newCRS.loadTablesFromDisk();
        
        assertTrue("Yükleme başarılı olmalı", result);
        assertTrue("Hasta yüklenmiş olmalı", 
            newCRS.getPatients().containsKey(patient.getNational_id()));
        
        // Temizlik
        File file = new File(CRS.dataPath);
        file.delete();
    }

    @Test
    public void testSaveAndLoadRoundTrip() throws Exception {
        // Veri ekle
        crs.makeRendezvous(
            patient.getNational_id(),
            hospital.getId(),
            section.getId(),
            doctor.getDiploma_id(),
            futureDate
        );
        
        int originalCount = crs.getRendezvousCount();
        
        // Kaydet
        crs.saveTablesToDisk();
        
        // Yükle
        CRS newCRS = new CRS();
        newCRS.loadTablesFromDisk();
        
        assertEquals("Randezvous sayısı aynı olmalı", originalCount, newCRS.getRendezvousCount());
        
        // Temizlik
        File file = new File(CRS.dataPath);
        file.delete();
    }

    @Test
    public void testGetRendezvousCount() {
        assertEquals("Başlangıçta 0 olmalı", 0, crs.getRendezvousCount());
        
        crs.getRendezvouses().add(new Rendezvous(futureDate, doctor, patient, hospital, section));
        assertEquals("1 olmalı", 1, crs.getRendezvousCount());
        
        crs.getRendezvouses().add(new Rendezvous(futureDate.plusDays(1), doctor, patient, hospital, section));
        assertEquals("2 olmalı", 2, crs.getRendezvousCount());
    }

    @Test
    public void testGetPatientCount() {
        int count = crs.getPatientCount();
        assertEquals("2 hasta olmalı (setup'tan)", 2, count);
        
        Patient newPatient = new Patient("Yeni", 44444444444L, null);
        crs.getPatients().put(newPatient.getNational_id(), newPatient);
        
        assertEquals("3 hasta olmalı", 3, crs.getPatientCount());
    }

    @Test
    public void testGetHospitalCount() {
        int count = crs.getHospitalCount();
        assertEquals("1 hastane olmalı (setup'tan)", 1, count);
        
        Hospital newHospital = new Hospital("Yeni Hastane", City.ISTANBUL);
        crs.getHospitals().put(newHospital.getId(), newHospital);
        
        assertEquals("2 hastane olmalı", 2, crs.getHospitalCount());
    }

    @Test
    public void testSetMaxRendezvousPerPatient() {
        crs.setMaxRendezvousPerPatient(10);
        assertEquals("Limit değişmeli", 10, CRS.MAX_RENDEZVOUS_PER_PATIENT);
        
        crs.setMaxRendezvousPerPatient(5); // Eski değere dön
    }

    @Test
    public void testSetRendezvousDayLimit() {
        crs.setRendezvousDayLimit(60);
        assertEquals("Limit değişmeli", 60, CRS.RENDEZVOUS_DAY_LIMIT);
        
        crs.setRendezvousDayLimit(30); // Eski değere dön
    }

    @Test
    public void testSaveSettings() throws Exception {
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 7;
        CRS.RENDEZVOUS_DAY_LIMIT = 45;
        
        crs.saveSettings();
        
        // Dosya oluşturulmuş mu kontrol et
        File file = new File("settings.txt");
        assertTrue("Settings dosyası oluşturulmalı", file.exists());
        
        // Temizlik
        file.delete();
        
        // Eski değerlere dön
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
    }

    @Test
    public void testLoadSettings() throws Exception {
        // Önce ayarları kaydet
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 8;
        CRS.RENDEZVOUS_DAY_LIMIT = 50;
        crs.saveSettings();
        
        // Ayarları değiştir
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 1;
        CRS.RENDEZVOUS_DAY_LIMIT = 1;
        
        // Ayarları yükle
        crs.loadSettings();
        
        assertEquals("Max rendezvous yüklenmeli", 8, CRS.MAX_RENDEZVOUS_PER_PATIENT);
        assertEquals("Day limit yüklenmeli", 50, CRS.RENDEZVOUS_DAY_LIMIT);
        
        // Temizlik
        File file = new File("settings.txt");
        file.delete();
        
        // Eski değerlere dön
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
    }

    @Test
    public void testMultipleRendezvousManagement() throws Exception {
        // Birden fazla randevu oluştur
        for (int i = 0; i < 3; i++) {
            crs.makeRendezvous(
                patient.getNational_id(),
                hospital.getId(),
                section.getId(),
                doctor.getDiploma_id(),
                futureDate.plusDays(i)
            );
        }
        
        assertEquals("3 randevu olmalı", 3, crs.getRendezvousCount());
        assertEquals("Hasta 3 randevuya sahip olmalı", 3, patient.getRendezvousCount());
    }

    @Test
    public void testDefaultSettings() {
        // Varsayılan değerleri kontrol et
        assertTrue("Max rendezvous pozitif olmalı", CRS.MAX_RENDEZVOUS_PER_PATIENT > 0);
        assertTrue("Day limit pozitif olmalı", CRS.RENDEZVOUS_DAY_LIMIT > 0);
        assertNotNull("Data path null olmamalı", CRS.dataPath);
    }
}

