package mertguler.TestDesign;

import mertguler.CRS.CRS;
import mertguler.Enums.City;
import mertguler.Exceptions.ChildOnlyException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Hospital.Section;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * TEST TASARIM TEKNİĞİ: DECISION TABLE TESTING
 * Karar Tablosu ile Test Tasarımı
 * 
 * Açıklama: Bu test sınıfı çoklu koşulların kombinasyonlarını test eder.
 * Karar tabloları karmaşık iş kurallarını test etmek için kullanılır.
 * 
 * KARAR TABLOSU 1: Randezvous Oluşturma
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * Koşullar:                    | T1 | T2 | T3 | T4 | T5 | T6 | T7 | T8 |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * Hasta geçerli?               | T  | T  | T  | T  | F  | F  | F  | F  |
 * Doktor müsait?               | T  | T  | F  | F  | T  | T  | F  | F  |
 * Tarih geçerli?               | T  | F  | T  | F  | T  | F  | T  | F  |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * Aksiyon: Randezvous Oluştur  | ✓  | X  | X  | X  | X  | X  | X  | X  |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * KARAR TABLOSU 2: Çocuk Bölümü Kontrolü
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * Koşullar:                | T1 | T2 | T3 | T4 |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * Bölüm çocuk bölümü mü?   | T  | T  | F  | F  |
 * Hasta 18 yaş altı mı?    | T  | F  | T  | F  |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * Aksiyon: İzin Ver        | ✓  | X  | ✓  | ✓  |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */
public class DecisionTableTests {
    
    private CRS crs;
    private Hospital hospital;
    private Section normalSection;
    private Section childSection;
    private Doctor availableDoctor;
    private Doctor busyDoctor;
    private Patient childPatient;
    private Patient adultPatient;
    private LocalDate validDate;
    private LocalDate invalidPastDate;
    private LocalDate invalidFutureDate;
    
    @Before
    public void setUp() {
        crs = new CRS();
        hospital = new Hospital("Test Hospital", City.ANKARA);
        normalSection = new Section("Kardiyoloji", hospital, false);
        childSection = new Section("Çocuk Sağlığı", hospital, true);
        
        availableDoctor = new Doctor("Dr. Available", 10000000001L, 1001);
        busyDoctor = new Doctor("Dr. Busy", 10000000002L, 1002);
        
        childPatient = new Patient("Çocuk", 20000000001L, LocalDate.now().minusYears(10));
        adultPatient = new Patient("Yetişkin", 20000000002L, LocalDate.now().minusYears(30));
        
        validDate = LocalDate.now().plusDays(7);
        invalidPastDate = LocalDate.now().minusDays(7);
        invalidFutureDate = LocalDate.now().plusDays(100);
        
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
        
        // Busy doctor'un schedule'ını doldur (10 hasta limiti)
        for (int i = 0; i < 10; i++) {
            Patient p = new Patient("P" + i, 30000000000L + i, null);
            busyDoctor.getSchedule().getSessions().add(
                new Rendezvous(validDate, busyDoctor, p, hospital, normalSection)
            );
        }
        
        // CRS'ye ekle
        crs.getHospitals().put(hospital.getId(), hospital);
        crs.getPatients().put(childPatient.getNational_id(), childPatient);
        crs.getPatients().put(adultPatient.getNational_id(), adultPatient);
        
        try {
            hospital.addSection(normalSection);
            hospital.addSection(childSection);
            normalSection.addDoctor(availableDoctor);
            normalSection.addDoctor(busyDoctor);
            childSection.addDoctor(availableDoctor);
        } catch (Exception e) {
            fail("Setup failed: " + e.getMessage());
        }
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // KARAR TABLOSU 1: RANDEZVOUS OLUŞTURMA
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testDecisionTable1_T1_AllTrue() throws Exception {
        // T1: Hasta geçerli=T, Doktor müsait=T, Tarih geçerli=T → Başarılı
        boolean result = crs.makeRendezvous(
            adultPatient.getNational_id(),
            hospital.getId(),
            normalSection.getId(),
            availableDoctor.getDiploma_id(),
            validDate
        );
        
        assertTrue("T1: Tüm koşullar geçerli, randezvous oluşmalı", result);
    }
    
    @Test
    public void testDecisionTable1_T2_InvalidDate_Past() {
        // T2: Hasta geçerli=T, Doktor müsait=T, Tarih geçerli=F (geçmiş) → Başarısız
        try {
            crs.makeRendezvous(
                adultPatient.getNational_id(),
                hospital.getId(),
                normalSection.getId(),
                availableDoctor.getDiploma_id(),
                invalidPastDate
            );
            fail("T2: Geçmiş tarih için exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("DateTimeException bekleniyor", e.getMessage().contains("before"));
        }
    }
    
    @Test
    public void testDecisionTable1_T3_DoctorBusy() {
        // T3: Hasta geçerli=T, Doktor müsait=F, Tarih geçerli=T → Başarısız
        try {
            crs.makeRendezvous(
                adultPatient.getNational_id(),
                hospital.getId(),
                normalSection.getId(),
                busyDoctor.getDiploma_id(), // Busy doctor
                validDate
            );
            fail("T3: Doktor dolu ise exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("DailyLimitException bekleniyor", e.getMessage().contains("limit"));
        }
    }
    
    @Test
    public void testDecisionTable1_T4_DoctorBusy_InvalidDate() {
        // T4: Hasta geçerli=T, Doktor müsait=F, Tarih geçerli=F → Başarısız (tarih kontrolü önce)
        try {
            crs.makeRendezvous(
                adultPatient.getNational_id(),
                hospital.getId(),
                normalSection.getId(),
                busyDoctor.getDiploma_id(),
                invalidPastDate
            );
            fail("T4: Geçersiz tarih için exception fırlatmalı");
        } catch (Exception e) {
            // Tarih kontrolü önce yapılır
            assertTrue("DateTimeException bekleniyor", e.getMessage().contains("before"));
        }
    }
    
    @Test
    public void testDecisionTable1_T5_InvalidPatient() {
        // T5: Hasta geçerli=F, Doktor müsait=T, Tarih geçerli=T → Başarısız
        try {
            crs.makeRendezvous(
                99999999999L, // Geçersiz hasta ID
                hospital.getId(),
                normalSection.getId(),
                availableDoctor.getDiploma_id(),
                validDate
            );
            fail("T5: Geçersiz hasta için exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("IDException bekleniyor", e.getMessage().contains("No patient"));
        }
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // KARAR TABLOSU 2: ÇOCUK BÖLÜMÜ KONTROLÜ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testDecisionTable2_T1_ChildSection_ChildPatient() throws Exception {
        // T1: Çocuk bölümü=T, Hasta çocuk=T → İzin ver
        crs.checkChildSection(childPatient, childSection);
        // Exception fırlatmıyorsa test geçer
    }
    
    @Test
    public void testDecisionTable2_T2_ChildSection_AdultPatient() {
        // T2: Çocuk bölümü=T, Hasta çocuk=F → İzin verme
        try {
            crs.checkChildSection(adultPatient, childSection);
            fail("T2: Yetişkin hasta çocuk bölümüne giremez");
        } catch (ChildOnlyException e) {
            assertTrue("ChildOnlyException bekleniyor", e.getMessage().contains("over Age 18"));
        }
    }
    
    @Test
    public void testDecisionTable2_T3_NormalSection_ChildPatient() throws Exception {
        // T3: Çocuk bölümü=F, Hasta çocuk=T → İzin ver (normal bölüme çocuk gidebilir)
        crs.checkChildSection(childPatient, normalSection);
        // Exception fırlatmıyorsa test geçer
    }
    
    @Test
    public void testDecisionTable2_T4_NormalSection_AdultPatient() throws Exception {
        // T4: Çocuk bölümü=F, Hasta çocuk=F → İzin ver
        crs.checkChildSection(adultPatient, normalSection);
        // Exception fırlatmıyorsa test geçer
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // KARAR TABLOSU 3: RANDEZVOUS LİMİTİ KONTROLÜ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    /**
     * KARAR TABLOSU 3: Randezvous Limit Kontrolü
     * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
     * Koşullar:                     | T1 | T2 | T3 | T4 |
     * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
     * Aktif randevu sayısı < limit? | T  | F  | T  | F  |
     * Randevular süresi geçmiş mi?  | F  | F  | T  | T  |
     * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
     * Aksiyon: Yeni randezvuya izin | ✓  | X  | ✓  | X  |
     * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
     */
    
    @Test
    public void testDecisionTable3_T1_BelowLimit_ActiveRendezvous() throws Exception {
        // T1: Aktif randevu < limit=T, Geçmiş randevu=F → İzin ver
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        Patient patient = new Patient("Test", 40000000001L, null);
        
        // 3 aktif randevu ekle
        for (int i = 0; i < 3; i++) {
            patient.getRendezvouses().add(
                new Rendezvous(
                    LocalDate.now().plusDays(i + 1),
                    availableDoctor, patient, hospital, normalSection
                )
            );
        }
        
        patient.checkActiveRendezvousCount(); // Geçmeli
    }
    
    @Test
    public void testDecisionTable3_T2_AtLimit_ActiveRendezvous() {
        // T2: Aktif randevu < limit=F, Geçmiş randevu=F → İzin verme
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        Patient patient = new Patient("Test", 40000000002L, null);
        
        // 5 aktif randevu ekle (limit)
        for (int i = 0; i < 5; i++) {
            patient.getRendezvouses().add(
                new Rendezvous(
                    LocalDate.now().plusDays(i + 1),
                    availableDoctor, patient, hospital, normalSection
                )
            );
        }
        
        try {
            patient.checkActiveRendezvousCount();
            fail("T2: Limitte iken exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("RendezvousLimitException bekleniyor", e.getMessage().contains("limit"));
        }
    }
    
    @Test
    public void testDecisionTable3_T3_BelowLimit_WithExpiredRendezvous() throws Exception {
        // T3: Aktif randevu < limit=T, Geçmiş randevu=T → İzin ver (geçmişler sayılmaz)
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        Patient patient = new Patient("Test", 40000000003L, null);
        
        // 5 geçmiş randevu ekle
        for (int i = 0; i < 5; i++) {
            Rendezvous expired = new Rendezvous(
                LocalDate.now().minusDays(i + 1),
                availableDoctor, patient, hospital, normalSection
            );
            expired.updateExpired();
            patient.getRendezvouses().add(expired);
        }
        
        // 2 aktif randevu ekle
        for (int i = 0; i < 2; i++) {
            patient.getRendezvouses().add(
                new Rendezvous(
                    LocalDate.now().plusDays(i + 1),
                    availableDoctor, patient, hospital, normalSection
                )
            );
        }
        
        patient.checkActiveRendezvousCount(); // Geçmeli (sadece aktifler sayılır)
    }
    
    @Test
    public void testDecisionTable3_T4_AtLimit_MixedRendezvous() {
        // T4: Aktif randevu < limit=F, Geçmiş randevu=T → İzin verme
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        Patient patient = new Patient("Test", 40000000004L, null);
        
        // 3 geçmiş randevu
        for (int i = 0; i < 3; i++) {
            Rendezvous expired = new Rendezvous(
                LocalDate.now().minusDays(i + 1),
                availableDoctor, patient, hospital, normalSection
            );
            expired.updateExpired();
            patient.getRendezvouses().add(expired);
        }
        
        // 5 aktif randevu (limit)
        for (int i = 0; i < 5; i++) {
            patient.getRendezvouses().add(
                new Rendezvous(
                    LocalDate.now().plusDays(i + 1),
                    availableDoctor, patient, hospital, normalSection
                )
            );
        }
        
        try {
            patient.checkActiveRendezvousCount();
            fail("T4: Aktif randevu limitte iken exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("RendezvousLimitException bekleniyor", e.getMessage().contains("limit"));
        }
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // KARAR TABLOSU 4: DOKTOR MÜSAİTLİĞİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    /**
     * KARAR TABLOSU 4: Doktor Müsaitliği
     * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
     * Koşullar:                  | T1 | T2 | T3 | T4 |
     * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
     * Günlük hasta < limit?      | T  | F  | T  | F  |
     * Farklı gün seçildi mi?     | T  | T  | F  | F  |
     * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
     * Aksiyon: Randezvuya izin   | ✓  | ✓  | ✓  | X  |
     * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
     */
    
    @Test
    public void testDecisionTable4_T1_Available_DifferentDay() throws Exception {
        // T1: Günlük hasta < limit=T, Farklı gün=T → İzin ver
        LocalDate differentDay = validDate.plusDays(5);
        availableDoctor.getSchedule().checkDailyLimit(differentDay); // Geçmeli
    }
    
    @Test
    public void testDecisionTable4_T2_Full_DifferentDay() throws Exception {
        // T2: Günlük hasta < limit=F, Farklı gün=T → İzin ver (farklı gün boş)
        LocalDate differentDay = validDate.plusDays(10);
        busyDoctor.getSchedule().checkDailyLimit(differentDay); // Geçmeli
    }
    
    @Test
    public void testDecisionTable4_T3_Available_SameDay() throws Exception {
        // T3: Günlük hasta < limit=T, Farklı gün=F → İzin ver
        LocalDate emptyDay = LocalDate.now().plusDays(20);
        availableDoctor.getSchedule().checkDailyLimit(emptyDay); // Geçmeli
    }
    
    @Test
    public void testDecisionTable4_T4_Full_SameDay() {
        // T4: Günlük hasta < limit=F, Farklı gün=F → İzin verme
        try {
            busyDoctor.getSchedule().checkDailyLimit(validDate);
            fail("T4: Dolu günde exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("DailyLimitException bekleniyor", e.getMessage().contains("limit"));
        }
    }
}

