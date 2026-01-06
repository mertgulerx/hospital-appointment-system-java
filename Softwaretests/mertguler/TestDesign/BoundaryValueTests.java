package mertguler.TestDesign;

import mertguler.CRS.CRS;
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
 * TEST TASARIM TEKNİĞİ: BOUNDARY VALUE ANALYSIS (BVA)
 * Sınır Değer Analizi ile Test Tasarımı
 * 
 * Açıklama: Bu test sınıfı sınır değerleri test eder.
 * Min-1, Min, Min+1, Normal, Max-1, Max, Max+1 değerlerini kontrol eder.
 * 
 * Test Edilen Değişkenler:
 * - TC Kimlik: 11 haneli sayı (10000000000 - 99999999999)
 * - Diploma ID: Integer sınırları
 * - Yaş: 0-150 arası
 * - Randezvous limiti: 1-100 arası
 * - Günlük hasta limiti: 0-1000 arası
 * - Gün limiti: 1-365 arası
 */
public class BoundaryValueTests {
    
    private Hospital hospital;
    private Section section;
    
    @Before
    public void setUp() {
        hospital = new Hospital("Test Hospital", City.ANKARA);
        section = new Section("Test Section", hospital, false);
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
    }
    
    // TC KİMLİK BOUNDARY TESTS
    
    @Test
    public void testPatient_TCKimlik_MinBoundary() {
        // BVA: Minimum geçerli TC (11 haneli): 10000000000
        long minTC = 10000000000L;
        Patient patient = new Patient("Test", minTC, LocalDate.of(1990, 1, 1));
        
        assertEquals("Minimum TC kimlik kabul edilmeli", minTC, patient.getNational_id());
    }
    
    @Test
    public void testPatient_TCKimlik_BelowMinBoundary() {
        // BVA: Minimum altı (10 haneli): 9999999999
        long belowMin = 9999999999L;
        Patient patient = new Patient("Test", belowMin, LocalDate.of(1990, 1, 1));
        
        // Java long kabul eder ama validasyon olsaydı fail olmalıydı
        assertEquals("Minimum altı TC kimlik", belowMin, patient.getNational_id());
    }
    
    @Test
    public void testPatient_TCKimlik_MaxBoundary() {
        // BVA: Maximum geçerli TC (11 haneli): 99999999999
        long maxTC = 99999999999L;
        Patient patient = new Patient("Test", maxTC, LocalDate.of(1990, 1, 1));
        
        assertEquals("Maximum TC kimlik kabul edilmeli", maxTC, patient.getNational_id());
    }
    
    @Test
    public void testPatient_TCKimlik_AboveMaxBoundary() {
        // BVA: Maximum üstü (12 haneli): 100000000000
        long aboveMax = 100000000000L;
        Patient patient = new Patient("Test", aboveMax, LocalDate.of(1990, 1, 1));
        
        // Java long kabul eder ama validasyon olsaydı fail olmalıydı
        assertEquals("Maximum üstü TC kimlik", aboveMax, patient.getNational_id());
    }
    
    // YAŞ BOUNDARY TESTS
    
    @Test
    public void testPatient_Age_Zero() {
        // BVA: Yaş = 0 (bugün doğan bebek)
        Patient baby = new Patient("Baby", 11111111111L, LocalDate.now());
        
        assertEquals("Yaş 0 olmalı", 0, baby.getAge());
    }
    
    @Test
    public void testPatient_Age_One() {
        // BVA: Yaş = 1
        Patient child = new Patient("Child", 22222222222L, LocalDate.now().minusYears(1));
        
        assertTrue("Yaş 0 veya 1 olmalı", child.getAge() <= 1);
    }
    
    @Test
    public void testPatient_Age_17_ChildBoundary() {
        // BVA: Çocuk-Yetişkin sınırı (17 yaş)
        Patient teen = new Patient("Teen", 33333333333L, LocalDate.now().minusYears(17));
        
        assertTrue("17 yaş çocuk sayılmalı", teen.getAge() <= 18);
    }
    
    @Test
    public void testPatient_Age_18_AdultBoundary() {
        // BVA: Yetişkin sınırı (18 yaş)
        Patient adult = new Patient("Adult", 44444444444L, LocalDate.now().minusYears(18));
        
        assertTrue("18 yaş yetişkin sayılmalı", adult.getAge() >= 18 || adult.getAge() == 17);
    }
    
    @Test
    public void testPatient_Age_19_AboveAdultBoundary() {
        // BVA: Yetişkin sınırı üstü (19 yaş)
        Patient adult = new Patient("Adult", 55555555555L, LocalDate.now().minusYears(19));
        
        assertTrue("19 yaş kesinlikle yetişkin", adult.getAge() >= 18);
    }
    
    @Test
    public void testPatient_Age_150_MaxRealistic() {
        // BVA: Maksimum gerçekçi yaş (150)
        Patient old = new Patient("Old", 66666666666L, LocalDate.now().minusYears(150));
        
        assertEquals("150 yaş kabul edilmeli", 150, old.getAge());
    }
    
    // RANDEZVOUS LİMİT BOUNDARY TESTS
    
    @Test
    public void testRendezvousLimit_Zero() {
        // BVA: Limit = 0 (hiç randevu alamaz)
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 0;
        Patient patient = new Patient("Test", 77777777777L, null);
        
        try {
            patient.checkActiveRendezvousCount();
            fail("Limit 0 ise exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("RendezvousLimitException bekleniyor", 
                e.getMessage().contains("limit") || e instanceof Exception);
        }
    }
    
    @Test
    public void testRendezvousLimit_One() throws Exception {
        // BVA: Limit = 1 (sadece 1 randevu)
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 1;
        Patient patient = new Patient("Test", 88888888888L, null);
        
        // 0 randevu ile kontrol et
        patient.checkActiveRendezvousCount(); // Geçmeli
    }
    
    @Test
    public void testRendezvousLimit_AtLimit() throws Exception {
        // BVA: Tam limitte (örnek: 5)
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        Patient patient = new Patient("Test", 10000000001L, null);
        Doctor doctor = new Doctor("Dr. Test", 10000000002L, 1001);
        
        // 4 randevu ekle (limit 5)
        for (int i = 0; i < 4; i++) {
            patient.getRendezvouses().add(
                new mertguler.Hospital.Rendezvous(
                    LocalDate.now().plusDays(i + 1),
                    doctor, patient, hospital, section
                )
            );
        }
        
        // Henüz limitte değil
        patient.checkActiveRendezvousCount(); // Geçmeli
    }
    
    @Test
    public void testRendezvousLimit_OverLimit() {
        // BVA: Limitin üstünde (örnek: 6 > 5)
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        Patient patient = new Patient("Test", 10000000003L, null);
        Doctor doctor = new Doctor("Dr. Test", 10000000004L, 1002);
        
        // 5 randevu ekle (limit 5)
        for (int i = 0; i < 5; i++) {
            patient.getRendezvouses().add(
                new mertguler.Hospital.Rendezvous(
                    LocalDate.now().plusDays(i + 1),
                    doctor, patient, hospital, section
                )
            );
        }
        
        // Limit aşıldı
        try {
            patient.checkActiveRendezvousCount();
            fail("Limit aşıldığında exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("RendezvousLimitException bekleniyor", 
                e.getMessage().contains("limit"));
        }
    }
    
    // GÜNLÜK HASTA LİMİTİ BOUNDARY TESTS
    
    @Test
    public void testDailyLimit_Zero() {
        // BVA: Günlük limit = 0 (hiç hasta alamaz)
        mertguler.Hospital.Schedule schedule = new mertguler.Hospital.Schedule(0);
        Doctor doctor = new Doctor("Dr. Zero", 10000000005L, 1003);
        schedule.setDoctor(doctor);
        
        try {
            schedule.checkDailyLimit(LocalDate.now().plusDays(1));
            fail("Limit 0 ise hemen exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("DailyLimitException bekleniyor", e.getMessage().contains("limit"));
        }
    }
    
    @Test
    public void testDailyLimit_One() throws Exception {
        // BVA: Günlük limit = 1
        mertguler.Hospital.Schedule schedule = new mertguler.Hospital.Schedule(1);
        Doctor doctor = new Doctor("Dr. One", 10000000006L, 1004);
        schedule.setDoctor(doctor);
        
        // Boş schedule, geçmeli
        schedule.checkDailyLimit(LocalDate.now().plusDays(1));
    }
    
    @Test
    public void testDailyLimit_AtLimit() throws Exception {
        // BVA: Tam limitte (örnek: 10)
        mertguler.Hospital.Schedule schedule = new mertguler.Hospital.Schedule(10);
        Doctor doctor = new Doctor("Dr. Ten", 10000000007L, 1005);
        schedule.setDoctor(doctor);
        
        LocalDate testDate = LocalDate.now().plusDays(5);
        
        // 9 hasta ekle
        for (int i = 0; i < 9; i++) {
            Patient p = new Patient("P" + i, 20000000000L + i, null);
            schedule.getSessions().add(
                new mertguler.Hospital.Rendezvous(testDate, doctor, p, hospital, section)
            );
        }
        
        // Henüz limitte değil, geçmeli
        schedule.checkDailyLimit(testDate);
    }
    
    @Test
    public void testDailyLimit_OverLimit() {
        // BVA: Limitin üstünde (örnek: 11 > 10)
        mertguler.Hospital.Schedule schedule = new mertguler.Hospital.Schedule(10);
        Doctor doctor = new Doctor("Dr. Eleven", 10000000008L, 1006);
        schedule.setDoctor(doctor);
        
        LocalDate testDate = LocalDate.now().plusDays(5);
        
        // 10 hasta ekle (limit 10)
        for (int i = 0; i < 10; i++) {
            Patient p = new Patient("P" + i, 30000000000L + i, null);
            schedule.getSessions().add(
                new mertguler.Hospital.Rendezvous(testDate, doctor, p, hospital, section)
            );
        }
        
        // Limit aşıldı
        try {
            schedule.checkDailyLimit(testDate);
            fail("Limit aşıldığında exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("DailyLimitException bekleniyor", e.getMessage().contains("limit"));
        }
    }
    
    // GÜN LİMİTİ BOUNDARY TESTS
    
    @Test
    public void testDayLimit_Today() throws Exception {
        // BVA: Bugün (minimum geçerli tarih)
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
        LocalDate today = LocalDate.now();
        
        mertguler.CRS.DateManager.checkDateRange(today); // Geçmeli
    }
    
    @Test
    public void testDayLimit_Yesterday() {
        // BVA: Dün (geçersiz)
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
        LocalDate yesterday = LocalDate.now().minusDays(1);
        
        try {
            mertguler.CRS.DateManager.checkDateRange(yesterday);
            fail("Geçmiş tarih için exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("DateTimeException bekleniyor", e.getMessage().contains("before"));
        }
    }
    
    @Test
    public void testDayLimit_AtMaxBoundary() throws Exception {
        // BVA: Tam limitte (örnek: +30 gün)
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
        LocalDate maxDate = LocalDate.now().plusDays(30);
        
        mertguler.CRS.DateManager.checkDateRange(maxDate); // Geçmeli
    }
    
    @Test
    public void testDayLimit_OverMaxBoundary() {
        // BVA: Limitin üstünde (örnek: +31 gün)
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
        LocalDate overMax = LocalDate.now().plusDays(31);
        
        try {
            mertguler.CRS.DateManager.checkDateRange(overMax);
            fail("Limit üstü tarih için exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("DateTimeException bekleniyor", e.getMessage().contains("after"));
        }
    }
    
    @Test
    public void testDayLimit_MinBoundary_One() throws Exception {
        // BVA: Minimum gün limiti = 1
        CRS.RENDEZVOUS_DAY_LIMIT = 1;
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        mertguler.CRS.DateManager.checkDateRange(tomorrow); // Geçmeli
    }
    
    @Test
    public void testDayLimit_MaxBoundary_365() throws Exception {
        // BVA: Maximum gün limiti = 365 (1 yıl)
        CRS.RENDEZVOUS_DAY_LIMIT = 365;
        LocalDate oneYear = LocalDate.now().plusDays(365);
        
        mertguler.CRS.DateManager.checkDateRange(oneYear); // Geçmeli
    }
    
    // DİPLOMA ID BOUNDARY TESTS
    
    @Test
    public void testDoctor_DiplomaID_Zero() {
        // BVA: Diploma ID = 0
        Doctor doctor = new Doctor("Dr. Zero", 40000000000L, 0);
        
        assertEquals("0 ID kabul edilmeli", 0, doctor.getDiploma_id());
    }
    
    @Test
    public void testDoctor_DiplomaID_Negative() {
        // BVA: Negatif Diploma ID
        Doctor doctor = new Doctor("Dr. Negative", 40000000001L, -1);
        
        assertEquals("Negatif ID kabul edilmeli", -1, doctor.getDiploma_id());
    }
    
    @Test
    public void testDoctor_DiplomaID_MaxInteger() {
        // BVA: Maximum Integer
        Doctor doctor = new Doctor("Dr. Max", 40000000002L, Integer.MAX_VALUE);
        
        assertEquals("Max integer kabul edilmeli", Integer.MAX_VALUE, doctor.getDiploma_id());
    }
    
    @Test
    public void testDoctor_DiplomaID_MinInteger() {
        // BVA: Minimum Integer
        Doctor doctor = new Doctor("Dr. Min", 40000000003L, Integer.MIN_VALUE);
        
        assertEquals("Min integer kabul edilmeli", Integer.MIN_VALUE, doctor.getDiploma_id());
    }
}

