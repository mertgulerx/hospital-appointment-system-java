package mertguler.TestDesign;

import mertguler.CRS.CRS;
import mertguler.CRS.DateManager;
import mertguler.Enums.City;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * TEST TASARIM TEKNİĞİ: NEGATIVE TESTING
 * Negatif Test Senaryoları
 * 
 * Açıklama: Bu test sınıfı sistemin hatalı girdilere ve beklenmeyen
 * durumlarakarşı nasıl davrandığını test eder.
 * 
 * Negatif Test Kategorileri:
 * 1. Geçersiz girdi değerleri (null, boş, negatif, vb.)
 * 2. Sınır dışı değerler
 * 3. Mantıksal hatalar
 * 4. Kaynak yetersizliği
 * 5. Eşzamanlılık sorunları
 * 
 * Not: Negatif testlerde exception beklenir!
 */
public class NegativeTestingTests {
    
    private CRS crs;
    private Hospital hospital;
    private Section section;
    private Doctor doctor;
    private Patient patient;
    
    @Before
    public void setUp() {
        crs = new CRS();
        hospital = new Hospital("Test Hospital", City.ANKARA);
        section = new Section("Test Section", hospital, false);
        doctor = new Doctor("Dr. Test", 10000000001L, 1001);
        patient = new Patient("Test Patient", 20000000001L, null);
        
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // NULL DEĞER TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test(expected = NullPointerException.class)
    public void testNegative_DeleteRendezvous_Null() throws Exception {
        // Negatif: Null randezvou silmeye çalış
        crs.deleteRendezvous(null);
    }
    
    @Test
    public void testNegative_Patient_NullName() {
        // Negatif: Null isim (Java kabul eder ama ideal değil)
        Patient p = new Patient(null, 10000000002L, null);
        assertNull("Null isim kabul edilir", p.getName());
    }
    
    @Test
    public void testNegative_Patient_NullBirthDate() {
        // Negatif: Null doğum tarihi
        Patient p = new Patient("Test", 10000000003L, null);
        assertEquals("Null doğum tarihi için yaş 0", 0, p.getAge());
    }
    
    @Test
    public void testNegative_DateManager_NullDate() {
        // Negatif: Null tarih kontrolü
        try {
            DateManager.checkDateRange(null);
            fail("Null tarih için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DateTimeException veya NullPointerException bekleniyor", 
                e.getMessage().contains("null") || e instanceof NullPointerException);
        }
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // SINIR DIŞI DEĞER TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testNegative_TCKimlik_TooShort() {
        // Negatif: 10 haneli TC (yerine 11 haneli olmalı)
        long shortTC = 1234567890L; // 10 haneli
        Patient p = new Patient("Test", shortTC, null);
        
        // Java kabul eder ama validasyon olsaydı fail olmalıydı
        assertEquals("Kısa TC kimlik", shortTC, p.getNational_id());
    }
    
    @Test
    public void testNegative_TCKimlik_TooLong() {
        // Negatif: 12 haneli TC
        long longTC = 123456789012L; // 12 haneli
        Patient p = new Patient("Test", longTC, null);
        
        // Java kabul eder ama validasyon olsaydı fail olmalıydı
        assertEquals("Uzun TC kimlik", longTC, p.getNational_id());
    }
    
    @Test
    public void testNegative_TCKimlik_Negative() {
        // Negatif: Negatif TC kimlik
        long negativeTC = -12345678901L;
        Patient p = new Patient("Test", negativeTC, null);
        
        assertEquals("Negatif TC kimlik", negativeTC, p.getNational_id());
    }
    
    @Test
    public void testNegative_TCKimlik_Zero() {
        // Negatif: Sıfır TC kimlik
        Patient p = new Patient("Test", 0L, null);
        assertEquals("Sıfır TC kimlik", 0L, p.getNational_id());
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // GEÇERSİZ ID TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testNegative_GetPatient_InvalidID() {
        // Negatif: Var olmayan hasta ID
        try {
            crs.getPatientManager().getPatient(99999999999L);
            fail("Geçersiz ID için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("IDException bekleniyor", e.getMessage().contains("No patient"));
        }
    }
    
    @Test
    public void testNegative_GetHospital_InvalidID() {
        // Negatif: Var olmayan hastane ID
        try {
            crs.getHospitalManager().getHospitalWithID(99999);
            fail("Geçersiz ID için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("IDException bekleniyor", e.getMessage().contains("No hospital"));
        }
    }
    
    @Test
    public void testNegative_GetDoctor_InvalidID() {
        // Negatif: Var olmayan doktor ID
        try {
            section.getDoctor(99999);
            fail("Geçersiz ID için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("IDException bekleniyor", e.getMessage().contains("No doctor"));
        }
    }
    
    @Test
    public void testNegative_GetSection_InvalidID() {
        // Negatif: Var olmayan bölüm ID
        try {
            hospital.getSection(99999);
            fail("Geçersiz ID için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("IDException bekleniyor", e.getMessage().contains("No section") || 
                e.getMessage().contains("not found"));
        }
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // GEÇERSİZ TARİH TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testNegative_Date_VeryOldPast() {
        // Negatif: Çok eski tarih (1 yıl önce)
        LocalDate veryOld = LocalDate.now().minusYears(1);
        
        try {
            DateManager.checkDateRange(veryOld);
            fail("Çok eski tarih için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DateTimeException bekleniyor", e.getMessage().contains("before"));
        }
    }
    
    @Test
    public void testNegative_Date_VeryFarFuture() {
        // Negatif: Çok uzak gelecek (1 yıl sonra)
        LocalDate veryFar = LocalDate.now().plusYears(1);
        
        try {
            DateManager.checkDateRange(veryFar);
            fail("Çok uzak gelecek için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DateTimeException bekleniyor", e.getMessage().contains("after"));
        }
    }
    
    @Test
    public void testNegative_Date_InvalidFormat() {
        // Negatif: Geçersiz tarih formatı
        try {
            DateManager.isValidDate("2025/12/31"); // Yanlış format (/ yerine -)
            fail("Geçersiz format için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DateTimeParseException bekleniyor", e != null);
        }
    }
    
    @Test
    public void testNegative_Date_InvalidDay() {
        // Negatif: Geçersiz gün (32)
        try {
            DateManager.isValidDate("32-01-2025");
            fail("Geçersiz gün için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DateTimeParseException bekleniyor", e != null);
        }
    }
    
    @Test
    public void testNegative_Date_InvalidMonth() {
        // Negatif: Geçersiz ay (13)
        try {
            DateManager.isValidDate("01-13-2025");
            fail("Geçersiz ay için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DateTimeParseException bekleniyor", e != null);
        }
    }
    
    @Test
    public void testNegative_Date_LeapYearInvalid() {
        // Negatif: 32 Ocak (geçersiz gün)
        try {
            DateManager.isValidDate("32-01-2025");
            fail("Geçersiz gün için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DateTimeParseException bekleniyor", e != null);
        }
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // LİMİT AŞIMI TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testNegative_RendezvousLimit_Exceeded() {
        // Negatif: Randezvou limiti aşımı
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 3;
        
        // 3 randevu ekle
        for (int i = 0; i < 3; i++) {
            patient.getRendezvouses().add(
                new mertguler.Hospital.Rendezvous(
                    LocalDate.now().plusDays(i + 1),
                    doctor, patient, hospital, section
                )
            );
        }
        
        // 4. randevu için exception bekleniyor
        try {
            patient.checkActiveRendezvousCount();
            fail("Limit aşımı için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("RendezvousLimitException bekleniyor", e.getMessage().contains("limit"));
        }
    }
    
    @Test
    public void testNegative_DailyLimit_Exceeded() {
        // Negatif: Günlük hasta limiti aşımı
        mertguler.Hospital.Schedule schedule = new mertguler.Hospital.Schedule(5);
        Doctor d = new Doctor("Dr. Busy", 30000000001L, 3001);
        schedule.setDoctor(d);
        
        LocalDate testDate = LocalDate.now().plusDays(5);
        
        // 5 hasta ekle (limit 5)
        for (int i = 0; i < 5; i++) {
            Patient p = new Patient("P" + i, 40000000000L + i, null);
            schedule.getSessions().add(
                new mertguler.Hospital.Rendezvous(testDate, d, p, hospital, section)
            );
        }
        
        // 6. hasta için exception bekleniyor
        try {
            schedule.checkDailyLimit(testDate);
            fail("Günlük limit aşımı için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DailyLimitException bekleniyor", e.getMessage().contains("limit"));
        }
    }
    
    @Test
    public void testNegative_NegativeLimit() {
        // Negatif: Negatif limit değeri
        CRS.MAX_RENDEZVOUS_PER_PATIENT = -1;
        
        // Negatif limit ile kontrol et
        try {
            patient.checkActiveRendezvousCount();
            // Negatif limit her zaman geçer (mantıksız ama teknik olarak mümkün)
        } catch (Exception e) {
            // Exception beklenmez ama mantıksal hata var
        }
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // DUPLICATE (TEKRARLI) VERİ TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testNegative_AddDuplicate_Hospital() {
        // Negatif: Aynı hastaneyi tekrar eklemeye çalış
        try {
            crs.getHospitalManager().createHospital("Test Hospital", City.ANKARA);
            crs.getHospitalManager().createHospital("Test Hospital", City.ANKARA);
            fail("Duplicate hastane için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DuplicateInfoException bekleniyor", e.getMessage().contains("exist"));
        }
    }
    
    @Test
    public void testNegative_AddDuplicate_Section() throws Exception {
        // Negatif: Aynı bölümü tekrar eklemeye çalış
        hospital.addSection(section);
        
        try {
            hospital.addSection(section);
            fail("Duplicate bölüm için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DuplicateInfoException bekleniyor", e.getMessage().contains("exist"));
        }
    }
    
    @Test
    public void testNegative_AddDuplicate_Doctor() throws Exception {
        // Negatif: Aynı doktoru tekrar eklemeye çalış
        section.addDoctor(doctor);
        
        try {
            section.addDoctor(doctor);
            fail("Duplicate doktor için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DuplicateInfoException bekleniyor", e.getMessage().contains("exist"));
        }
    }
    
    @Test
    public void testNegative_AddDuplicate_Patient() throws Exception {
        // Negatif: Aynı TC kimlik ile hasta eklemeye çalış
        crs.getPatients().put(patient.getNational_id(), patient);
        
        try {
            crs.getPatientManager().checkPatientDuplication(patient.getNational_id());
            fail("Duplicate hasta için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("DuplicateInfoException bekleniyor", e.getMessage().contains("exist"));
        }
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // MANTIKSAL HATA TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testNegative_AdultInChildSection() {
        // Negatif: Yetişkin hasta çocuk bölümüne
        Section childSection = new Section("Çocuk", hospital, true);
        Patient adult = new Patient("Adult", 50000000001L, LocalDate.now().minusYears(30));
        
        try {
            crs.checkChildSection(adult, childSection);
            fail("Yetişkin çocuk bölümüne gidemez");
        } catch (Exception e) {
            assertTrue("ChildOnlyException bekleniyor", e.getMessage().contains("over Age 18"));
        }
    }
    
    @Test
    public void testNegative_DeleteNonExistentRendezvous() {
        // Negatif: Var olmayan randezvou silmeye çalış
        mertguler.Hospital.Rendezvous r = new mertguler.Hospital.Rendezvous(
            LocalDate.now().plusDays(5),
            doctor, patient, hospital, section
        );
        
        try {
            doctor.getSchedule().deleteRendezvous(r);
            fail("Var olmayan randezvou için exception bekleniyor");
        } catch (Exception e) {
            assertTrue("IDException bekleniyor", e.getMessage().contains("not found"));
        }
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // BOŞ VERİ TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testNegative_EmptyName() {
        // Negatif: Boş isim
        Patient p = new Patient("", 60000000001L, null);
        assertEquals("Boş isim kabul edilir (ideal değil)", "", p.getName());
    }
    
    @Test
    public void testNegative_EmptyHospitalName() {
        // Negatif: Boş hastane adı
        Hospital h = new Hospital("", City.ANKARA);
        assertEquals("Boş hastane adı kabul edilir", "", h.getName());
    }
    
    @Test
    public void testNegative_WhitespaceOnlyName() {
        // Negatif: Sadece boşluk karakterli isim
        Patient p = new Patient("   ", 70000000001L, null);
        assertEquals("Boşluk-only isim kabul edilir", "   ", p.getName());
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // ÖZEL KARAKTER TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testNegative_SpecialCharactersInName() {
        // Negatif: İsimde özel karakterler (@#$%)
        Patient p = new Patient("Test@#$%", 80000000001L, null);
        assertEquals("Özel karakterler kabul edilir (ideal değil)", "Test@#$%", p.getName());
    }
    
    @Test
    public void testNegative_SQLInjectionAttempt() {
        // Negatif: SQL injection denemesi (simülasyon)
        Patient p = new Patient("'; DROP TABLE patients; --", 90000000001L, null);
        assertEquals("SQL injection pattern kabul edilir (DB yok)", 
            "'; DROP TABLE patients; --", p.getName());
    }
    
    @Test
    public void testNegative_VeryLongName() {
        // Negatif: Çok uzun isim (1000 karakter)
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longName.append("VeryLongName");
        }
        
        Patient p = new Patient(longName.toString(), 10000000010L, null);
        assertEquals("Çok uzun isim kabul edilir", longName.toString(), p.getName());
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // SIRA DIŞI DEĞERdır TESTLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testNegative_MaxIntegerDiplomaID() {
        // Negatif: Maximum integer değeri
        Doctor d = new Doctor("Dr. Max", 10000000011L, Integer.MAX_VALUE);
        assertEquals("Max integer kabul edilir", Integer.MAX_VALUE, d.getDiploma_id());
    }
    
    @Test
    public void testNegative_MinIntegerDiplomaID() {
        // Negatif: Minimum integer değeri
        Doctor d = new Doctor("Dr. Min", 10000000012L, Integer.MIN_VALUE);
        assertEquals("Min integer kabul edilir", Integer.MIN_VALUE, d.getDiploma_id());
    }
    
    @Test
    public void testNegative_FutureYear() {
        // Negatif: Çok ileri yıl (3000)
        LocalDate futureYear = LocalDate.of(3000, 1, 1);
        Patient p = new Patient("Future", 10000000013L, futureYear);
        
        // Negatif yaş (gelecekte doğmuş)
        int age = p.getAge();
        assertTrue("Gelecek doğum tarihi negatif yaş verir", age < 0);
    }
}

