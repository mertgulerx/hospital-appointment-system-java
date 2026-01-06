package mertguler.CRS;

import org.junit.Before;
import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.*;

/**
 * Test sınıfı: DateManager sınıfının tüm fonksiyonalitelerini test eder
 */
public class DateManagerTest {
    
    @Before
    public void setUp() {
        // Test için RENDEZVOUS_DAY_LIMIT'i varsayılan değere ayarla
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
    }

    @Test
    public void testGetCurrentDate() {
        LocalDate currentDate = DateManager.getCurrentDate();
        assertNotNull("Current date null olmamalı", currentDate);
        assertEquals("Bugünün tarihi olmalı", LocalDate.now(), currentDate);
    }

    @Test
    public void testGetLastDate() {
        LocalDate lastDate = DateManager.getLastDate();
        LocalDate expected = LocalDate.now().plusDays(CRS.RENDEZVOUS_DAY_LIMIT);
        
        assertEquals("Son tarih doğru hesaplanmalı", expected, lastDate);
    }

    @Test
    public void testGetLastDate_WithDifferentLimit() {
        CRS.RENDEZVOUS_DAY_LIMIT = 60;
        LocalDate lastDate = DateManager.getLastDate();
        LocalDate expected = LocalDate.now().plusDays(60);
        
        assertEquals("Farklı limitlerle çalışmalı", expected, lastDate);
    }

    @Test
    public void testGetFormatedDate() {
        LocalDate date = LocalDate.of(2025, 5, 15);
        String formatted = DateManager.getFormatedDate(date);
        
        assertNotNull("Format edilmiş tarih null olmamalı", formatted);
        assertEquals("Format doğru olmalı", "15-05-2025", formatted);
    }

    @Test
    public void testGetFormatedDate_SingleDigit() {
        LocalDate date = LocalDate.of(2025, 1, 5);
        String formatted = DateManager.getFormatedDate(date);
        
        assertEquals("Tek haneli gün/ay formatı doğru olmalı", "05-01-2025", formatted);
    }

    @Test
    public void testIsValidDate_ValidFormat() throws Exception {
        String dateString = "15-05-2025";
        LocalDate parsed = DateManager.isValidDate(dateString);
        
        assertNotNull("Parse edilmiş tarih null olmamalı", parsed);
        assertEquals("Gün doğru olmalı", 15, parsed.getDayOfMonth());
        assertEquals("Ay doğru olmalı", 5, parsed.getMonthValue());
        assertEquals("Yıl doğru olmalı", 2025, parsed.getYear());
    }

    @Test
    public void testIsValidDate_SingleDigits() throws Exception {
        String dateString = "05-01-2025";
        LocalDate parsed = DateManager.isValidDate(dateString);
        
        assertEquals("Tek haneli tarih parse edilmeli", 5, parsed.getDayOfMonth());
        assertEquals("Tek haneli ay parse edilmeli", 1, parsed.getMonthValue());
    }

    @Test(expected = DateTimeParseException.class)
    public void testIsValidDate_InvalidFormat() throws Exception {
        DateManager.isValidDate("2025-05-15"); // Yanlış format (yyyy-MM-dd yerine dd-MM-yyyy)
    }

    @Test(expected = DateTimeParseException.class)
    public void testIsValidDate_InvalidDay() throws Exception {
        DateManager.isValidDate("32-05-2025"); // 32. gün geçersiz
    }

    @Test(expected = DateTimeParseException.class)
    public void testIsValidDate_InvalidMonth() throws Exception {
        DateManager.isValidDate("15-13-2025"); // 13. ay geçersiz
    }

    @Test(expected = DateTimeParseException.class)
    public void testIsValidDate_InvalidString() throws Exception {
        DateManager.isValidDate("geçersiz tarih");
    }

    @Test(expected = DateTimeParseException.class)
    public void testIsValidDate_EmptyString() throws Exception {
        DateManager.isValidDate("");
    }

    @Test
    public void testGetYearDifference_ValidDate() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        int years = DateManager.getYearDifference(birthDate);
        
        int expectedYears = LocalDate.now().getYear() - 1990;
        assertTrue("Yaş hesaplaması doğru olmalı", 
            years == expectedYears || years == expectedYears - 1);
    }

    @Test
    public void testGetYearDifference_NullDate() {
        int years = DateManager.getYearDifference(null);
        assertEquals("Null tarih için 0 dönmeli", 0, years);
    }

    @Test
    public void testGetYearDifference_Today() {
        LocalDate today = LocalDate.now();
        int years = DateManager.getYearDifference(today);
        
        assertEquals("Bugün doğan için 0 olmalı", 0, years);
    }

    @Test
    public void testGetYearDifference_Yesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        int years = DateManager.getYearDifference(yesterday);
        
        assertEquals("Dün doğan için 0 olmalı", 0, years);
    }

    @Test
    public void testCheckDateRange_ValidDate() throws Exception {
        LocalDate validDate = LocalDate.now().plusDays(5);
        
        // Exception fırlatmamalı
        DateManager.checkDateRange(validDate);
    }

    @Test(expected = DateTimeException.class)
    public void testCheckDateRange_NullDate() throws Exception {
        DateManager.checkDateRange(null);
    }

    @Test(expected = DateTimeException.class)
    public void testCheckDateRange_PastDate() throws Exception {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        DateManager.checkDateRange(pastDate);
    }

    @Test(expected = DateTimeException.class)
    public void testCheckDateRange_TooFarFuture() throws Exception {
        LocalDate tooFar = LocalDate.now().plusDays(CRS.RENDEZVOUS_DAY_LIMIT + 1);
        DateManager.checkDateRange(tooFar);
    }

    @Test
    public void testCheckDateRange_TodayDate() throws Exception {
        LocalDate today = LocalDate.now();
        
        // Bugün geçerli olmalı (before kontrolü var)
        DateManager.checkDateRange(today);
    }

    @Test
    public void testCheckDateRange_LastValidDate() throws Exception {
        LocalDate lastValidDate = LocalDate.now().plusDays(CRS.RENDEZVOUS_DAY_LIMIT);
        
        // Son gün dahil olmalı
        DateManager.checkDateRange(lastValidDate);
    }

    @Test(expected = DateTimeException.class)
    public void testCheckDateRange_OneDayAfterLimit() throws Exception {
        LocalDate oneDayAfter = LocalDate.now().plusDays(CRS.RENDEZVOUS_DAY_LIMIT + 1);
        DateManager.checkDateRange(oneDayAfter);
    }

    @Test
    public void testCheckDateRange_TomorrowDate() throws Exception {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        // Yarın geçerli olmalı
        DateManager.checkDateRange(tomorrow);
    }

    @Test
    public void testDatePattern() {
        assertEquals("Date pattern doğru olmalı", "dd-MM-yyyy", DateManager.datePattern);
    }

    @Test
    public void testDateFormatter() {
        assertNotNull("DateFormatter null olmamalı", DateManager.dateFormatter);
    }

    @Test
    public void testDateManagerConstructor() {
        DateManager manager = new DateManager("yyyy-MM-dd");
        
        // Constructor pattern'i değiştirmeli
        assertEquals("Pattern değiştirilmeli", "yyyy-MM-dd", DateManager.datePattern);
        
        // Eski pattern'e geri dön
        DateManager.datePattern = "dd-MM-yyyy";
    }

    @Test
    public void testFormatAndParseRoundTrip() throws Exception {
        LocalDate original = LocalDate.of(2025, 6, 20);
        
        String formatted = DateManager.getFormatedDate(original);
        LocalDate parsed = DateManager.isValidDate(formatted);
        
        assertEquals("Format ve parse döngüsü tutarlı olmalı", original, parsed);
    }

    @Test
    public void testMultipleDates() throws Exception {
        String[] dates = {
            "01-01-2025",
            "15-06-2025",
            "31-12-2025",
            "29-02-2024" // Leap year
        };
        
        for (String dateStr : dates) {
            LocalDate parsed = DateManager.isValidDate(dateStr);
            assertNotNull("Tarih parse edilmeli: " + dateStr, parsed);
            
            String formatted = DateManager.getFormatedDate(parsed);
            assertEquals("Format tutarlı olmalı", dateStr, formatted);
        }
    }

    @Test(expected = DateTimeParseException.class)
    public void testInvalidLeapYear() throws Exception {
        // 2025 leap year değil, 29 Şubat geçersiz
        DateManager.isValidDate("29-02-2025");
    }

    @Test
    public void testCheckDateRange_WithDifferentLimits() throws Exception {
        // 10 günlük limit
        CRS.RENDEZVOUS_DAY_LIMIT = 10;
        LocalDate validDate = LocalDate.now().plusDays(5);
        DateManager.checkDateRange(validDate);
        
        // 60 günlük limit
        CRS.RENDEZVOUS_DAY_LIMIT = 60;
        LocalDate farDate = LocalDate.now().plusDays(50);
        DateManager.checkDateRange(farDate);
        
        // Eski değere dön
        CRS.RENDEZVOUS_DAY_LIMIT = 30;
    }

    @Test
    public void testGetYearDifference_FutureDate() {
        LocalDate futureDate = LocalDate.now().plusYears(5);
        int years = DateManager.getYearDifference(futureDate);
        
        assertTrue("Gelecek tarih için negatif değer olmalı", years < 0);
    }

    @Test
    public void testEdgeCaseDates() throws Exception {
        // Yıl başı
        LocalDate newYear = LocalDate.of(2025, 1, 1);
        String formatted = DateManager.getFormatedDate(newYear);
        assertEquals("Yıl başı formatı doğru olmalı", "01-01-2025", formatted);
        
        // Yıl sonu
        LocalDate endYear = LocalDate.of(2025, 12, 31);
        formatted = DateManager.getFormatedDate(endYear);
        assertEquals("Yıl sonu formatı doğru olmalı", "31-12-2025", formatted);
    }

    @Test
    public void testGetCurrentDate_NotNull() {
        for (int i = 0; i < 10; i++) {
            assertNotNull("Her çağrıda null olmamalı", DateManager.getCurrentDate());
        }
    }

    @Test
    public void testGetLastDate_Consistency() {
        LocalDate last1 = DateManager.getLastDate();
        LocalDate last2 = DateManager.getLastDate();
        
        // Aynı anda çağrılırsa aynı sonucu vermeli
        assertEquals("Tutarlı sonuç vermeli", last1, last2);
    }
}

