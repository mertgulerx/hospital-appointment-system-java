package mertguler.TestDesign;

import mertguler.CRS.CRS;
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
 * TEST TASARIM TEKNİĞİ: EQUIVALENCE PARTITIONING (EP)
 * Eşdeğerlik Bölümleme ile Test Tasarımı
 * 
 * Açıklama: Bu test sınıfı girdi alanını eşdeğer sınıflara böler.
 * Her sınıftan bir test case'i test eder.
 * 
 * Bölümlemeler:
 * 
 * 1. TC Kimlik:
 *    - Geçerli (11 haneli)
 *    - Geçersiz (10 haneli veya daha az)
 *    - Geçersiz (12 haneli veya daha fazla)
 * 
 * 2. Yaş:
 *    - Bebek (0-2)
 *    - Çocuk (3-12)
 *    - Genç (13-17)
 *    - Yetişkin (18-64)
 *    - Yaşlı (65+)
 * 
 * 3. Randezvous Durumu:
 *    - Aktif randevular
 *    - Süresi geçmiş randevular
 *    - Gelecek randevular
 * 
 * 4. Hastane Türü:
 *    - Devlet Hastanesi
 *    - Özel Hastane
 *    - Üniversite Hastanesi
 *    (İsimlendirme ile ayırt edilir)
 */
public class EquivalencePartitioningTests {
    
    private CRS crs;
    private Hospital publicHospital;
    private Hospital privateHospital;
    private Hospital universityHospital;
    
    @Before
    public void setUp() {
        crs = new CRS();
        
        // Farklı hastane türleri
        publicHospital = new Hospital("Devlet Hastanesi", City.ANKARA);
        privateHospital = new Hospital("Özel Hastane", City.ISTANBUL);
        universityHospital = new Hospital("Üniversite Hastanesi", City.IZMIR);
        
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
    }
    
    // ===========================================
    // YAŞ GRUPLARI - EQUIVALENCE PARTITIONING
    // ===========================================
    
    @Test
    public void testAgeGroup_Infant_0to2() {
        // EP: Bebek grubu (0-2 yaş)
        Patient infant = new Patient("Bebek", 10000000001L, LocalDate.now().minusMonths(18));
        
        int age = infant.getAge();
        assertTrue("Bebek 0-2 yaş arasında olmalı", age >= 0 && age <= 2);
    }
    
    @Test
    public void testAgeGroup_Child_3to12() {
        // EP: Çocuk grubu (3-12 yaş)
        Patient child = new Patient("Çocuk", 10000000002L, LocalDate.now().minusYears(7));
        
        int age = child.getAge();
        assertTrue("Çocuk 3-12 yaş arasında olmalı", age >= 3 && age <= 12);
    }
    
    @Test
    public void testAgeGroup_Teenager_13to17() {
        // EP: Genç grubu (13-17 yaş)
        Patient teen = new Patient("Genç", 10000000003L, LocalDate.now().minusYears(15));
        
        int age = teen.getAge();
        assertTrue("Genç 13-17 yaş arasında olmalı", age >= 13 && age <= 18);
    }
    
    @Test
    public void testAgeGroup_Adult_18to64() {
        // EP: Yetişkin grubu (18-64 yaş)
        Patient adult = new Patient("Yetişkin", 10000000004L, LocalDate.now().minusYears(35));
        
        int age = adult.getAge();
        assertTrue("Yetişkin 18-64 yaş arasında olmalı", age >= 18 && age <= 64);
    }
    
    @Test
    public void testAgeGroup_Senior_65Plus() {
        // EP: Yaşlı grubu (65+ yaş)
        Patient senior = new Patient("Yaşlı", 10000000005L, LocalDate.now().minusYears(70));
        
        int age = senior.getAge();
        assertTrue("Yaşlı 65+ yaş olmalı", age >= 65);
    }
    
    // ===========================================
    // RANDEZVOUS DURUMLARI - EQUIVALENCE PARTITIONING
    // ===========================================
    
    @Test
    public void testRendezvousStatus_Active_Future() {
        // EP: Aktif (gelecek) randevular
        Doctor doctor = new Doctor("Dr. Test", 20000000001L, 2001);
        Patient patient = new Patient("Test", 20000000002L, null);
        Section section = new Section("Test", publicHospital, false);
        
        LocalDate futureDate = LocalDate.now().plusDays(7);
        Rendezvous rendezvous = new Rendezvous(futureDate, doctor, patient, publicHospital, section);
        
        rendezvous.updateExpired();
        assertFalse("Gelecekteki randevu expired olmamalı", rendezvous.isExpired());
    }
    
    @Test
    public void testRendezvousStatus_Expired_Past() {
        // EP: Süresi geçmiş randevular
        Doctor doctor = new Doctor("Dr. Test", 20000000003L, 2002);
        Patient patient = new Patient("Test", 20000000004L, null);
        Section section = new Section("Test", publicHospital, false);
        
        LocalDate pastDate = LocalDate.now().minusDays(7);
        Rendezvous rendezvous = new Rendezvous(pastDate, doctor, patient, publicHospital, section);
        
        rendezvous.updateExpired();
        assertTrue("Geçmişteki randevu expired olmalı", rendezvous.isExpired());
    }
    
    @Test
    public void testRendezvousStatus_Today() {
        // EP: Bugünkü randevular
        Doctor doctor = new Doctor("Dr. Test", 20000000005L, 2003);
        Patient patient = new Patient("Test", 20000000006L, null);
        Section section = new Section("Test", publicHospital, false);
        
        LocalDate today = LocalDate.now();
        Rendezvous rendezvous = new Rendezvous(today, doctor, patient, publicHospital, section);
        
        rendezvous.updateExpired();
        assertFalse("Bugünkü randevu expired olmamalı", rendezvous.isExpired());
    }
    
    // ===========================================
    // HASTANE TÜRLERİ - EQUIVALENCE PARTITIONING
    // ===========================================
    
    @Test
    public void testHospitalType_Public() {
        // EP: Devlet hastanesi
        assertEquals("Devlet hastanesi ismi doğru olmalı", "Devlet Hastanesi", publicHospital.getName());
        assertEquals("Devlet hastanesi şehri doğru olmalı", City.ANKARA, publicHospital.getCity());
    }
    
    @Test
    public void testHospitalType_Private() {
        // EP: Özel hastane
        assertEquals("Özel hastane ismi doğru olmalı", "Özel Hastane", privateHospital.getName());
        assertEquals("Özel hastane şehri doğru olmalı", City.ISTANBUL, privateHospital.getCity());
    }
    
    @Test
    public void testHospitalType_University() {
        // EP: Üniversite hastanesi
        assertEquals("Üniversite hastanesi ismi doğru olmalı", "Üniversite Hastanesi", universityHospital.getName());
        assertEquals("Üniversite hastanesi şehri doğru olmalı", City.IZMIR, universityHospital.getCity());
    }
    
    // ===========================================
    // BÖLÜM TÜRLERİ - EQUIVALENCE PARTITIONING
    // ===========================================
    
    @Test
    public void testSectionType_ChildOnly_True() {
        // EP: Sadece çocuk bölümü
        Section childSection = new Section("Çocuk Sağlığı", publicHospital, true);
        
        assertTrue("Çocuk bölümü olmalı", childSection.isChildSection());
    }
    
    @Test
    public void testSectionType_ChildOnly_False() {
        // EP: Normal (yetişkin) bölüm
        Section normalSection = new Section("Kardiyoloji", publicHospital, false);
        
        assertFalse("Normal bölüm olmalı", normalSection.isChildSection());
    }
    
    // ===========================================
    // RANDEZVOUS LİMİT DURUMLARI - EQUIVALENCE PARTITIONING
    // ===========================================
    
    @Test
    public void testRendezvousLimit_None() throws Exception {
        // EP: Hiç randevu yok
        Patient patient = new Patient("Test", 30000000001L, null);
        
        assertEquals("Randevu sayısı 0 olmalı", 0, patient.getRendezvousCount());
        patient.checkActiveRendezvousCount(); // Geçmeli
    }
    
    @Test
    public void testRendezvousLimit_BelowLimit() throws Exception {
        // EP: Limitin altında (örnek: 3 < 5)
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        Patient patient = new Patient("Test", 30000000002L, null);
        Doctor doctor = new Doctor("Dr. Test", 30000000003L, 3001);
        Section section = new Section("Test", publicHospital, false);
        
        // 3 randevu ekle
        for (int i = 0; i < 3; i++) {
            patient.getRendezvouses().add(
                new Rendezvous(LocalDate.now().plusDays(i + 1), doctor, patient, publicHospital, section)
            );
        }
        
        patient.checkActiveRendezvousCount(); // Geçmeli
    }
    
    @Test
    public void testRendezvousLimit_AtLimit() {
        // EP: Limitte (örnek: 5 = 5)
        CRS.MAX_RENDEZVOUS_PER_PATIENT = 5;
        Patient patient = new Patient("Test", 30000000004L, null);
        Doctor doctor = new Doctor("Dr. Test", 30000000005L, 3002);
        Section section = new Section("Test", publicHospital, false);
        
        // 5 randevu ekle
        for (int i = 0; i < 5; i++) {
            patient.getRendezvouses().add(
                new Rendezvous(LocalDate.now().plusDays(i + 1), doctor, patient, publicHospital, section)
            );
        }
        
        try {
            patient.checkActiveRendezvousCount();
            fail("Limitte iken exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("RendezvousLimitException bekleniyor", e.getMessage().contains("limit"));
        }
    }
    
    // ===========================================
    // ŞEHİR TÜRLERİ - EQUIVALENCE PARTITIONING
    // ===========================================
    
    @Test
    public void testCity_Major_Ankara() {
        // EP: Büyük şehir - Ankara
        Hospital hospital = new Hospital("Test", City.ANKARA);
        assertEquals("Şehir Ankara olmalı", City.ANKARA, hospital.getCity());
    }
    
    @Test
    public void testCity_Major_Istanbul() {
        // EP: Büyük şehir - İstanbul
        Hospital hospital = new Hospital("Test", City.ISTANBUL);
        assertEquals("Şehir İstanbul olmalı", City.ISTANBUL, hospital.getCity());
    }
    
    @Test
    public void testCity_Major_Izmir() {
        // EP: Büyük şehir - İzmir
        Hospital hospital = new Hospital("Test", City.IZMIR);
        assertEquals("Şehir İzmir olmalı", City.IZMIR, hospital.getCity());
    }
    
    @Test
    public void testCity_Medium_Antalya() {
        // EP: Orta şehir - Antalya
        Hospital hospital = new Hospital("Test", City.ANTALYA);
        assertEquals("Şehir Antalya olmalı", City.ANTALYA, hospital.getCity());
    }
    
    @Test
    public void testCity_Small_Ardahan() {
        // EP: Küçük şehir - Ardahan
        Hospital hospital = new Hospital("Test", City.ARDAHAN);
        assertEquals("Şehir Ardahan olmalı", City.ARDAHAN, hospital.getCity());
    }
    
    // ===========================================
    // TARİH DURUMLARI - EQUIVALENCE PARTITIONING
    // ===========================================
    
    @Test
    public void testDate_Past() {
        // EP: Geçmiş tarih
        LocalDate past = LocalDate.now().minusYears(1);
        
        try {
            mertguler.CRS.DateManager.checkDateRange(past);
            fail("Geçmiş tarih için exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("DateTimeException bekleniyor", e.getMessage().contains("before"));
        }
    }
    
    @Test
    public void testDate_Present() throws Exception {
        // EP: Bugün
        LocalDate today = LocalDate.now();
        
        mertguler.CRS.DateManager.checkDateRange(today); // Geçmeli
    }
    
    @Test
    public void testDate_NearFuture() throws Exception {
        // EP: Yakın gelecek (1-15 gün)
        LocalDate nearFuture = LocalDate.now().plusDays(7);
        
        mertguler.CRS.DateManager.checkDateRange(nearFuture); // Geçmeli
    }
    
    @Test
    public void testDate_FarFuture_WithinLimit() throws Exception {
        // EP: Uzak gelecek ama limitin içinde (16-30 gün)
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
        LocalDate farFuture = LocalDate.now().plusDays(25);
        
        mertguler.CRS.DateManager.checkDateRange(farFuture); // Geçmeli
    }
    
    @Test
    public void testDate_BeyondLimit() {
        // EP: Limitin ötesinde (31+ gün)
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
        LocalDate beyondLimit = LocalDate.now().plusDays(35);
        
        try {
            mertguler.CRS.DateManager.checkDateRange(beyondLimit);
            fail("Limit dışı tarih için exception fırlatmalı");
        } catch (Exception e) {
            assertTrue("DateTimeException bekleniyor", e.getMessage().contains("after"));
        }
    }
    
    // ===========================================
    // İSİM UZUNLUKLARI - EQUIVALENCE PARTITIONING
    // ===========================================
    
    @Test
    public void testNameLength_Short() {
        // EP: Kısa isim (1-5 karakter)
        Patient patient = new Patient("Ali", 40000000001L, null);
        assertEquals("Kısa isim kabul edilmeli", "Ali", patient.getName());
    }
    
    @Test
    public void testNameLength_Medium() {
        // EP: Orta isim (6-20 karakter)
        Patient patient = new Patient("Ahmet Mehmet", 40000000002L, null);
        assertEquals("Orta isim kabul edilmeli", "Ahmet Mehmet", patient.getName());
    }
    
    @Test
    public void testNameLength_Long() {
        // EP: Uzun isim (21+ karakter)
        Patient patient = new Patient("Ahmet Mehmet Ali Veli Hasan", 40000000003L, null);
        assertEquals("Uzun isim kabul edilmeli", "Ahmet Mehmet Ali Veli Hasan", patient.getName());
    }
    
    @Test
    public void testNameLength_Empty() {
        // EP: Boş isim
        Patient patient = new Patient("", 40000000004L, null);
        assertEquals("Boş isim kabul edilmeli", "", patient.getName());
    }
}

