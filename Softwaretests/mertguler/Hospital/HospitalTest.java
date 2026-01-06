package mertguler.Hospital;

import mertguler.Enums.City;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test sınıfı: Hospital sınıfının tüm fonksiyonalitelerini test eder
 */
public class HospitalTest {
    private Hospital hospital1;
    private Hospital hospital2;
    private Hospital hospital3;
    private Section section1;
    private Section section2;

    @Before
    public void setUp() {
        hospital1 = new Hospital("Ankara Şehir Hastanesi", City.ANKARA);
        hospital2 = new Hospital("İstanbul Tıp Fakültesi", City.ISTANBUL);
        hospital3 = new Hospital("Ankara Şehir Hastanesi", City.ANKARA); // hospital1 ile aynı
        
        section1 = new Section("Kardiyoloji", hospital1, false);
        section2 = new Section("Çocuk Sağlığı", hospital1, true);
    }

    @Test
    public void testHospitalCreation() {
        assertNotNull("Hospital nesnesi oluşturulmalı", hospital1);
        assertEquals("Hastane adı doğru olmalı", "Ankara Şehir Hastanesi", hospital1.getName());
        assertEquals("Şehir doğru olmalı", City.ANKARA, hospital1.getCity());
        assertTrue("ID pozitif olmalı", hospital1.getId() > 0);
    }

    @Test
    public void testHospitalCreationWithId() {
        Hospital hospital = new Hospital("Test Hospital", 999, City.IZMIR);
        assertEquals("ID doğru atanmalı", 999, hospital.getId());
        assertEquals("İsim doğru atanmalı", "Test Hospital", hospital.getName());
        assertEquals("Şehir doğru atanmalı", City.IZMIR, hospital.getCity());
    }

    @Test
    public void testGetName() {
        assertEquals("Ankara Şehir Hastanesi", hospital1.getName());
        assertEquals("İstanbul Tıp Fakültesi", hospital2.getName());
    }

    @Test
    public void testSetName() {
        hospital1.setName("Yeni Hastane Adı");
        assertEquals("İsim değiştirilmeli", "Yeni Hastane Adı", hospital1.getName());
    }

    @Test
    public void testGetCity() {
        assertEquals(City.ANKARA, hospital1.getCity());
        assertEquals(City.ISTANBUL, hospital2.getCity());
    }

    @Test
    public void testGetId() {
        int id1 = hospital1.getId();
        int id2 = hospital2.getId();
        
        assertTrue("ID'ler pozitif olmalı", id1 > 0 && id2 > 0);
        assertNotEquals("Farklı hastaneler farklı ID'ye sahip olmalı", id1, id2);
    }

    @Test
    public void testAddSection() throws Exception {
        hospital1.addSection(section1);
        
        assertTrue("Bölüm eklenmeli", hospital1.getSections().contains(section1));
        assertEquals("Bölüm sayısı 1 olmalı", 1, hospital1.getSections().size());
    }

    @Test
    public void testAddMultipleSections() throws Exception {
        hospital1.addSection(section1);
        hospital1.addSection(section2);
        
        assertEquals("Bölüm sayısı 2 olmalı", 2, hospital1.getSections().size());
        assertTrue("İlk bölüm listede olmalı", hospital1.getSections().contains(section1));
        assertTrue("İkinci bölüm listede olmalı", hospital1.getSections().contains(section2));
    }

    @Test(expected = DuplicateInfoException.class)
    public void testAddDuplicateSection() throws Exception {
        hospital1.addSection(section1);
        hospital1.addSection(section1); // Aynı bölümü tekrar eklemeye çalış
    }

    @Test
    public void testGetSection_ById() throws Exception {
        hospital1.addSection(section1);
        
        Section retrieved = hospital1.getSection(section1.getId());
        assertNotNull("Bölüm bulunmalı", retrieved);
        assertEquals("Doğru bölüm dönmeli", section1, retrieved);
    }

    @Test(expected = IDException.class)
    public void testGetSection_InvalidId() throws Exception {
        hospital1.getSection(99999);
    }

    @Test(expected = IDException.class)
    public void testGetSection_EmptyHospital() throws Exception {
        hospital1.getSection(1);
    }

    @Test
    public void testGetSection_ByName() throws Exception {
        hospital1.addSection(section1);
        
        Section retrieved = hospital1.getSection("Kardiyoloji");
        assertNotNull("Bölüm bulunmalı", retrieved);
        assertEquals("Doğru bölüm dönmeli", section1, retrieved);
    }

    @Test
    public void testGetSection_ByNameCaseInsensitive() throws Exception {
        hospital1.addSection(section1);
        
        Section retrieved = hospital1.getSection("kardiyoloji");
        assertNotNull("Büyük/küçük harf duyarsız olmalı", retrieved);
        assertEquals("Doğru bölüm dönmeli", section1, retrieved);
    }

    @Test
    public void testGetSection_ByNameWithSpaces() throws Exception {
        hospital1.addSection(section1);
        
        Section retrieved = hospital1.getSection("  Kardiyoloji  ");
        assertNotNull("Boşluklarla çalışmalı", retrieved);
        assertEquals("Doğru bölüm dönmeli", section1, retrieved);
    }

    @Test(expected = IDException.class)
    public void testGetSection_InvalidName() throws Exception {
        hospital1.addSection(section1);
        hospital1.getSection("Olmayan Bölüm");
    }

    @Test
    public void testDeleteSection() throws Exception {
        hospital1.addSection(section1);
        assertEquals("Bölüm sayısı 1 olmalı", 1, hospital1.getSections().size());
        
        boolean result = hospital1.deleteSection(section1.getId());
        
        assertTrue("Silme başarılı olmalı", result);
        assertEquals("Bölüm sayısı 0 olmalı", 0, hospital1.getSections().size());
        assertFalse("Bölüm listede olmamalı", hospital1.getSections().contains(section1));
    }

    @Test(expected = IDException.class)
    public void testDeleteSection_InvalidId() throws Exception {
        hospital1.deleteSection(99999);
    }

    @Test
    public void testCountAllRendezvouses() throws Exception {
        hospital1.addSection(section1);
        hospital1.addSection(section2);
        
        // Gerçek randevu testleri için doctor ve patient gerekli
        // Şimdilik sayının 0 olduğunu test edelim
        int count = hospital1.countAllRendezvouses();
        assertEquals("Yeni hastane randevu sayısı 0 olmalı", 0, count);
    }

    @Test
    public void testCountAllDoctors() throws Exception {
        hospital1.addSection(section1);
        hospital1.addSection(section2);
        
        int count = hospital1.countAllDoctors();
        assertEquals("Doktor eklenmemişse 0 olmalı", 0, count);
    }

    @Test
    public void testIncreaseAllTimeSectionCount() {
        int initialCount = hospital1.getAllTimeSectionCount();
        
        hospital1.increaseAllTimeSectionCount();
        assertEquals("Sayaç 1 artmalı", initialCount + 1, hospital1.getAllTimeSectionCount());
        
        hospital1.increaseAllTimeSectionCount();
        assertEquals("Sayaç 2 artmalı", initialCount + 2, hospital1.getAllTimeSectionCount());
    }

    @Test
    public void testGetAllTimeSectionCount() {
        int count = hospital1.getAllTimeSectionCount();
        assertTrue("Sayaç negatif olmamalı", count >= 0);
    }

    @Test
    public void testGetSections() {
        assertNotNull("Sections listesi null olmamalı", hospital1.getSections());
        assertTrue("Yeni hastane sections listesi boş olmalı", hospital1.getSections().isEmpty());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue("Aynı nesne kendisine eşit olmalı", hospital1.equals(hospital1));
    }

    @Test
    public void testEquals_SameNameAndCity() {
        assertTrue("Aynı isim ve şehir ile eşit olmalı", hospital1.equals(hospital3));
        assertTrue("Eşitlik simetrik olmalı", hospital3.equals(hospital1));
    }

    @Test
    public void testEquals_DifferentName() {
        assertFalse("Farklı isimle eşit olmamalı", hospital1.equals(hospital2));
    }

    @Test
    public void testEquals_DifferentCity() {
        Hospital hospital4 = new Hospital("Ankara Şehir Hastanesi", City.IZMIR);
        assertFalse("Farklı şehirle eşit olmamalı", hospital1.equals(hospital4));
    }

    @Test
    public void testEquals_CaseInsensitive() {
        Hospital hospital4 = new Hospital("ANKARA ŞEHİR HASTANESİ", City.ANKARA);
        assertTrue("İsim karşılaştırma case-insensitive olmalı", hospital1.equals(hospital4));
    }

    @Test
    public void testEquals_WithSpaces() {
        Hospital hospital4 = new Hospital("  Ankara Şehir Hastanesi  ", City.ANKARA);
        assertTrue("Boşluklarla çalışmalı", hospital1.equals(hospital4));
    }

    @Test
    public void testEquals_Null() {
        assertFalse("Null ile eşit olmamalı", hospital1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse("Farklı sınıf ile eşit olmamalı", hospital1.equals("String"));
    }

    @Test
    public void testToString() {
        String result = hospital1.toString();
        
        assertTrue("ToString isim içermeli", result.contains("Ankara Şehir Hastanesi"));
        assertTrue("ToString şehir içermeli", result.contains(City.ANKARA.toString()));
        assertTrue("ToString ID içermeli", result.contains("ID:"));
    }

    @Test
    public void testToString_AfterNameChange() {
        hospital1.setName("Değişen İsim");
        String result = hospital1.toString();
        
        assertTrue("ToString yeni ismi içermeli", result.contains("Değişen İsim"));
    }

    @Test
    public void testHospitalWithAllCities() {
        for (City city : City.values()) {
            Hospital hospital = new Hospital("Test Hospital", city);
            assertEquals("Her şehir desteklenmeli", city, hospital.getCity());
        }
    }

    @Test
    public void testMultipleHospitalsIndependentSections() throws Exception {
        hospital1.addSection(section1);
        
        Section section3 = new Section("Test Bölüm", hospital2, false);
        hospital2.addSection(section3);
        
        assertEquals("Hospital1'in 1 bölümü olmalı", 1, hospital1.getSections().size());
        assertEquals("Hospital2'nin 1 bölümü olmalı", 1, hospital2.getSections().size());
        assertTrue("Hospital1 kendi bölümünü içermeli", hospital1.getSections().contains(section1));
        assertFalse("Hospital1 diğer hastane bölümünü içermemeli", hospital1.getSections().contains(section3));
    }

    @Test
    public void testEmptyHospitalName() {
        Hospital hospital = new Hospital("", City.ANKARA);
        assertEquals("Boş isim kabul edilmeli", "", hospital.getName());
    }

    @Test
    public void testVeryLongHospitalName() {
        String longName = "Çok Uzun Hastane İsmi Test Deneme Ankara Şehir Tıp Fakültesi Hastanesi";
        Hospital hospital = new Hospital(longName, City.ANKARA);
        assertEquals("Uzun isim kabul edilmeli", longName, hospital.getName());
    }

    @Test
    public void testHospitalNameWithSpecialCharacters() {
        Hospital hospital = new Hospital("Üniversite Tıp Fakültesi (Özel Bölüm)", City.ANKARA);
        assertTrue("Özel karakterler desteklenmeli", hospital.getName().contains("("));
        assertTrue("Özel karakterler desteklenmeli", hospital.getName().contains(")"));
    }
}

