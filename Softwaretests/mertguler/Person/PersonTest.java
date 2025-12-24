package mertguler.Person;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test sınıfı: Person sınıfının tüm fonksiyonalitelerini test eder
 */
public class PersonTest {
    private Person person1;
    private Person person2;
    private Person person3;

    @Before
    public void setUp() {
        person1 = new Person("Ahmet Yılmaz", 12345678901L);
        person2 = new Person("Mehmet Demir", 98765432109L);
        person3 = new Person("Ahmet Yılmaz", 12345678901L); // person1 ile aynı
    }

    @Test
    public void testPersonCreation() {
        assertNotNull("Person nesnesi oluşturulmalı", person1);
        assertEquals("İsim doğru olmalı", "Ahmet Yılmaz", person1.getName());
        assertEquals("TC kimlik numarası doğru olmalı", 12345678901L, person1.getNational_id());
    }

    @Test
    public void testGetName() {
        assertEquals("Ahmet Yılmaz", person1.getName());
        assertEquals("Mehmet Demir", person2.getName());
    }

    @Test
    public void testSetName() {
        person1.setName("Ali Kaya");
        assertEquals("İsim değiştirilmeli", "Ali Kaya", person1.getName());
        
        // TC kimlik değişmemeli
        assertEquals("TC kimlik değişmemeli", 12345678901L, person1.getNational_id());
    }

    @Test
    public void testGetNationalId() {
        assertEquals(12345678901L, person1.getNational_id());
        assertEquals(98765432109L, person2.getNational_id());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue("Aynı nesne kendisine eşit olmalı", person1.equals(person1));
    }

    @Test
    public void testEquals_SameNationalId() {
        assertTrue("Aynı TC kimlik numarası ile eşit olmalı", person1.equals(person3));
        assertTrue("Eşitlik simetrik olmalı", person3.equals(person1));
    }

    @Test
    public void testEquals_DifferentNationalId() {
        assertFalse("Farklı TC kimlik numarası ile eşit olmamalı", person1.equals(person2));
        assertFalse("Eşitlik simetrik olmalı", person2.equals(person1));
    }

    @Test
    public void testEquals_Null() {
        assertFalse("Null ile eşit olmamalı", person1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse("Farklı sınıf ile eşit olmamalı", person1.equals("String nesnesi"));
        assertFalse("Farklı sınıf ile eşit olmamalı", person1.equals(123));
    }

    @Test
    public void testToString() {
        String expected = "Ahmet Yılmaz, 12345678901";
        assertEquals("toString formatı doğru olmalı", expected, person1.toString());
    }

    @Test
    public void testToString_AfterNameChange() {
        person1.setName("Yeni İsim");
        String expected = "Yeni İsim, 12345678901";
        assertEquals("İsim değişikliği toString'e yansımalı", expected, person1.toString());
    }

    @Test
    public void testImmutableNationalId() {
        long originalId = person1.getNational_id();
        person1.setName("Değişen İsim");
        assertEquals("TC kimlik final olduğu için değişmemeli", originalId, person1.getNational_id());
    }

    @Test
    public void testMultiplePersonsWithDifferentIds() {
        Person p1 = new Person("Test1", 11111111111L);
        Person p2 = new Person("Test2", 22222222222L);
        Person p3 = new Person("Test3", 33333333333L);

        assertFalse(p1.equals(p2));
        assertFalse(p2.equals(p3));
        assertFalse(p1.equals(p3));
    }

    @Test
    public void testNameWithSpecialCharacters() {
        Person person = new Person("Ömer Çağlar Şahin", 55555555555L);
        assertEquals("Türkçe karakterler desteklenmeli", "Ömer Çağlar Şahin", person.getName());
    }

    @Test
    public void testEmptyName() {
        Person person = new Person("", 11111111111L);
        assertEquals("Boş isim kabul edilmeli", "", person.getName());
    }

    @Test
    public void testVeryLongName() {
        String longName = "Çok Uzun İsim Test Deneme Ahmet Mehmet Ali Veli Hasan Hüseyin";
        Person person = new Person(longName, 77777777777L);
        assertEquals("Uzun isim kabul edilmeli", longName, person.getName());
    }
}

