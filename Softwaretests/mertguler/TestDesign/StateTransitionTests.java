package mertguler.TestDesign;

import mertguler.Enums.City;
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
 * TEST TASARIM TEKNİĞİ: STATE TRANSITION TESTING
 * Durum Geçiş Testi ile Test Tasarımı
 * 
 * Açıklama: Bu test sınıfı sistemin farklı durumları arasındaki
 * geçişleri test eder.
 * 
 * DURUM GEÇİŞ DİYAGRAMI: Randezvous Lifecycle
 * 
 *     ┌──────────┐
 *     │  BAŞLANGIÇ│
 *     └─────┬────┘
 *           │ create()
 *           ▼
 *     ┌──────────┐
 *     │  OLUŞTURULDU│◄──────┐
 *     └─────┬────┘          │
 *           │ activate()    │
 *           ▼               │
 *     ┌──────────┐          │
 *     │   AKTİF   │          │
 *     └─────┬────┘          │
 *           │               │
 *       ┌───┴───┐           │
 *       │       │           │
 * expire()  delete()    reschedule()
 *       │       │           │
 *       ▼       ▼           │
 *  ┌────────┐ ┌──────┐     │
 *  │SÜRESİ  │ │SİLİNDİ│     │
 *  │GEÇMİŞ │ └──────┘     │
 *  └────┬───┘              │
 *       └──────────────────┘
 * 
 * DURUM GEÇİŞ TABLOSU:
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * Mevcut Durum │ Olay       │ Yeni Durum   │ Aksiyon
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * BAŞLANGIÇ    │ create()   │ OLUŞTURULDU  │ Randezvou ekle
 * OLUŞTURULDU  │ activate() │ AKTİF        │ İşaretle aktif
 * AKTİF        │ expire()   │ SÜRESİ GEÇMİŞ│ İşaretle expired
 * AKTİF        │ delete()   │ SİLİNDİ      │ Randezvou sil
 * SÜRESİ GEÇMİŞ│ reschedule()│ OLUŞTURULDU │ Yeni randezvou
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */
public class StateTransitionTests {
    
    private Hospital hospital;
    private Section section;
    private Doctor doctor;
    private Patient patient;
    
    @Before
    public void setUp() {
        hospital = new Hospital("Test Hospital", City.ANKARA);
        section = new Section("Test Section", hospital, false);
        doctor = new Doctor("Dr. Test", 10000000001L, 1001);
        patient = new Patient("Test Patient", 20000000001L, null);
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // DURUM: BAŞLANGIÇ → OLUŞTURULDU
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testStateTransition_Initial_ToCreated() {
        // Durum: BAŞLANGIÇ
        assertEquals("Hasta başlangıçta randevu yok", 0, patient.getRendezvousCount());
        
        // Olay: create()
        LocalDate futureDate = LocalDate.now().plusDays(5);
        Rendezvous rendezvous = new Rendezvous(futureDate, doctor, patient, hospital, section);
        patient.addRendezvous(rendezvous);
        
        // Yeni Durum: OLUŞTURULDU
        assertEquals("Randezvou oluşturuldu", 1, patient.getRendezvousCount());
        assertFalse("Henüz expired değil", rendezvous.isExpired());
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // DURUM: OLUŞTURULDU → AKTİF
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testStateTransition_Created_ToActive() {
        // Durum: OLUŞTURULDU
        LocalDate futureDate = LocalDate.now().plusDays(5);
        Rendezvous rendezvous = new Rendezvous(futureDate, doctor, patient, hospital, section);
        patient.addRendezvous(rendezvous);
        
        // Olay: activate() - burada sadece doğrulama
        rendezvous.updateExpired();
        
        // Yeni Durum: AKTİF (gelecek tarihli, expired değil)
        assertFalse("Randezvou aktif", rendezvous.isExpired());
        assertEquals("Tarih gelecekte", futureDate, rendezvous.getDate());
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // DURUM: AKTİF → SÜRESİ GEÇMİŞ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testStateTransition_Active_ToExpired() {
        // Durum: AKTİF
        LocalDate pastDate = LocalDate.now().minusDays(5);
        Rendezvous rendezvous = new Rendezvous(pastDate, doctor, patient, hospital, section);
        patient.addRendezvous(rendezvous);
        
        assertFalse("Başlangıçta expired false", rendezvous.isExpired());
        
        // Olay: expire()
        rendezvous.updateExpired();
        
        // Yeni Durum: SÜRESİ GEÇMİŞ
        assertTrue("Randezvou süresi geçmiş", rendezvous.isExpired());
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // DURUM: AKTİF → SİLİNDİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testStateTransition_Active_ToDeleted() {
        // Durum: AKTİF
        LocalDate futureDate = LocalDate.now().plusDays(5);
        Rendezvous rendezvous = new Rendezvous(futureDate, doctor, patient, hospital, section);
        patient.addRendezvous(rendezvous);
        doctor.getSchedule().getSessions().add(rendezvous);
        
        assertEquals("1 randezvou var", 1, patient.getRendezvousCount());
        
        // Olay: delete()
        patient.deleteRendezvous(rendezvous);
        doctor.getSchedule().getSessions().remove(rendezvous);
        
        // Yeni Durum: SİLİNDİ
        assertEquals("Randezvou silindi", 0, patient.getRendezvousCount());
        assertFalse("Listede yok", patient.getRendezvouses().contains(rendezvous));
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // DURUM: SÜRESİ GEÇMİŞ → OLUŞTURULDU (Reschedule)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testStateTransition_Expired_ToCreated_Reschedule() {
        // Durum: SÜRESİ GEÇMİŞ
        LocalDate pastDate = LocalDate.now().minusDays(5);
        Rendezvous expiredRendezvous = new Rendezvous(pastDate, doctor, patient, hospital, section);
        patient.addRendezvous(expiredRendezvous);
        expiredRendezvous.updateExpired();
        
        assertTrue("Randezvou süresi geçmiş", expiredRendezvous.isExpired());
        
        // Olay: reschedule() - yeni randezvou oluştur
        patient.deleteRendezvous(expiredRendezvous);
        
        LocalDate newDate = LocalDate.now().plusDays(7);
        Rendezvous newRendezvous = new Rendezvous(newDate, doctor, patient, hospital, section);
        patient.addRendezvous(newRendezvous);
        
        // Yeni Durum: OLUŞTURULDU
        assertEquals("Yeni randezvou oluşturuldu", 1, patient.getRendezvousCount());
        assertFalse("Yeni randezvou aktif", newRendezvous.isExpired());
        assertFalse("Eski randezvou listede yok", patient.getRendezvouses().contains(expiredRendezvous));
    }
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // KOMPLEKS DURUM GEÇİŞLERİ
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    
    @Test
    public void testStateTransition_FullLifecycle() {
        // 1. BAŞLANGIÇ → OLUŞTURULDU
        assertEquals("Başlangıç: 0 randezvou", 0, patient.getRendezvousCount());
        
        LocalDate futureDate = LocalDate.now().plusDays(5);
        Rendezvous rendezvous = new Rendezvous(futureDate, doctor, patient, hospital, section);
        patient.addRendezvous(rendezvous);
        
        assertEquals("Oluşturuldu: 1 randezvou", 1, patient.getRendezvousCount());
        
        // 2. OLUŞTURULDU → AKTİF
        rendezvous.updateExpired();
        assertFalse("Aktif: expired false", rendezvous.isExpired());
        
        // 3. AKTİF → SİLİNDİ
        patient.deleteRendezvous(rendezvous);
        assertEquals("Silindi: 0 randezvou", 0, patient.getRendezvousCount());
    }
    
    @Test
    public void testStateTransition_MultipleRendezvous() {
        // Çoklu randezvou durum geçişleri
        LocalDate date1 = LocalDate.now().plusDays(5);
        LocalDate date2 = LocalDate.now().plusDays(10);
        LocalDate date3 = LocalDate.now().minusDays(5); // Expired
        
        Rendezvous r1 = new Rendezvous(date1, doctor, patient, hospital, section);
        Rendezvous r2 = new Rendezvous(date2, doctor, patient, hospital, section);
        Rendezvous r3 = new Rendezvous(date3, doctor, patient, hospital, section);
        
        // Hepsini ekle
        patient.addRendezvous(r1);
        patient.addRendezvous(r2);
        patient.addRendezvous(r3);
        
        assertEquals("3 randezvou eklendi", 3, patient.getRendezvousCount());
        
        // Expired güncelle
        r1.updateExpired();
        r2.updateExpired();
        r3.updateExpired();
        
        assertFalse("R1 aktif", r1.isExpired());
        assertFalse("R2 aktif", r2.isExpired());
        assertTrue("R3 expired", r3.isExpired());
        
        // Bir tanesini sil
        patient.deleteRendezvous(r1);
        
        assertEquals("2 randezvou kaldı", 2, patient.getRendezvousCount());
        assertFalse("R1 listede yok", patient.getRendezvouses().contains(r1));
        assertTrue("R2 listede var", patient.getRendezvouses().contains(r2));
        assertTrue("R3 listede var", patient.getRendezvouses().contains(r3));
    }
    
    @Test
    public void testStateTransition_ExpiredThenActive() {
        // Expired durumdan active'e dönemez (tarih değiştirilemez)
        LocalDate pastDate = LocalDate.now().minusDays(5);
        Rendezvous rendezvous = new Rendezvous(pastDate, doctor, patient, hospital, section);
        
        rendezvous.updateExpired();
        assertTrue("İlk update: expired", rendezvous.isExpired());
        
        // Tekrar update et (tarih değişmez, hala expired)
        rendezvous.updateExpired();
        assertTrue("İkinci update: hala expired", rendezvous.isExpired());
        
        // Tarih değiştirilemez, immutable
        assertEquals("Tarih değişmez", pastDate, rendezvous.getDate());
    }
    
    @Test
    public void testStateTransition_CascadeDelete() {
        // Hasta silindiğinde randevular da silinmeli (cascade)
        Rendezvous r1 = new Rendezvous(LocalDate.now().plusDays(5), doctor, patient, hospital, section);
        Rendezvous r2 = new Rendezvous(LocalDate.now().plusDays(10), doctor, patient, hospital, section);
        
        patient.addRendezvous(r1);
        patient.addRendezvous(r2);
        doctor.getSchedule().getSessions().add(r1);
        doctor.getSchedule().getSessions().add(r2);
        
        assertEquals("Hasta: 2 randezvou", 2, patient.getRendezvousCount());
        assertEquals("Doktor: 2 randezvou", 2, doctor.getSchedule().getRendezvousCount());
        
        // Tüm randevuları sil (hasta silme simülasyonu)
        patient.getRendezvouses().clear();
        doctor.getSchedule().getSessions().clear();
        
        assertEquals("Hasta: 0 randezvou", 0, patient.getRendezvousCount());
        assertEquals("Doktor: 0 randezvou", 0, doctor.getSchedule().getRendezvousCount());
    }
    
    @Test
    public void testStateTransition_DateBoundary_TodayToTomorrow() {
        // Bugün aktif, yarın expired kontrolü
        LocalDate today = LocalDate.now();
        Rendezvous rendezvous = new Rendezvous(today, doctor, patient, hospital, section);
        
        rendezvous.updateExpired();
        assertFalse("Bugün: aktif", rendezvous.isExpired());
        
        // Yarına simüle edilemez çünkü LocalDate.now() her zaman "şimdi"
        // Ama test konsepti: bugün aktif, yarın expired olacak
    }
    
    @Test
    public void testStateTransition_BulkExpire() {
        // Toplu expired güncelleme
        LocalDate[] dates = {
            LocalDate.now().minusDays(10),
            LocalDate.now().minusDays(5),
            LocalDate.now().minusDays(1),
            LocalDate.now(),
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5)
        };
        
        Rendezvous[] rendezvouses = new Rendezvous[dates.length];
        
        for (int i = 0; i < dates.length; i++) {
            rendezvouses[i] = new Rendezvous(dates[i], doctor, patient, hospital, section);
            patient.addRendezvous(rendezvouses[i]);
        }
        
        // Toplu update
        for (Rendezvous r : rendezvouses) {
            r.updateExpired();
        }
        
        // İlk 3 expired, son 3 aktif olmalı
        assertTrue("Expired: -10 gün", rendezvouses[0].isExpired());
        assertTrue("Expired: -5 gün", rendezvouses[1].isExpired());
        assertTrue("Expired: -1 gün", rendezvouses[2].isExpired());
        assertFalse("Aktif: bugün", rendezvouses[3].isExpired());
        assertFalse("Aktif: +1 gün", rendezvouses[4].isExpired());
        assertFalse("Aktif: +5 gün", rendezvouses[5].isExpired());
    }
}

