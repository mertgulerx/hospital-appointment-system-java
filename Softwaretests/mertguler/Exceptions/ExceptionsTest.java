package mertguler.Exceptions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test sınıfı: Tüm özel Exception sınıflarını test eder
 */
public class ExceptionsTest {

    @Test
    public void testIDException_WithMessage() {
        String message = "Test ID exception message";
        IDException exception = new IDException(message);
        
        assertNotNull("Exception oluşturulmalı", exception);
        assertEquals("Mesaj doğru olmalı", message, exception.getMessage());
    }

    @Test
    public void testIDException_Throw() {
        try {
            throw new IDException("Test exception");
        } catch (IDException e) {
            assertTrue("IDException fırlatılmalı", e instanceof IDException);
            assertEquals("Mesaj doğru olmalı", "Test exception", e.getMessage());
        }
    }

    @Test
    public void testIDException_EmptyMessage() {
        IDException exception = new IDException("");
        assertEquals("Boş mesaj kabul edilmeli", "", exception.getMessage());
    }

    @Test
    public void testIDException_NullMessage() {
        IDException exception = new IDException(null);
        assertNull("Null mesaj kabul edilmeli", exception.getMessage());
    }

    @Test
    public void testDuplicateInfoException_WithMessage() {
        String message = "Duplicate information found";
        DuplicateInfoException exception = new DuplicateInfoException(message);
        
        assertNotNull("Exception oluşturulmalı", exception);
        assertEquals("Mesaj doğru olmalı", message, exception.getMessage());
    }

    @Test
    public void testDuplicateInfoException_Throw() {
        try {
            throw new DuplicateInfoException("Duplicate test");
        } catch (DuplicateInfoException e) {
            assertTrue("DuplicateInfoException fırlatılmalı", e instanceof DuplicateInfoException);
            assertEquals("Mesaj doğru olmalı", "Duplicate test", e.getMessage());
        }
    }

    @Test
    public void testDuplicateInfoException_LongMessage() {
        String longMessage = "Bu çok uzun bir hata mesajı test durumu için kullanılıyor ve exception handling'in düzgün çalıştığını doğrulamak için";
        DuplicateInfoException exception = new DuplicateInfoException(longMessage);
        
        assertEquals("Uzun mesaj desteklenmeli", longMessage, exception.getMessage());
    }

    @Test
    public void testDailyLimitException_WithMessage() {
        String message = "Daily limit exceeded";
        DailyLimitException exception = new DailyLimitException(message);
        
        assertNotNull("Exception oluşturulmalı", exception);
        assertEquals("Mesaj doğru olmalı", message, exception.getMessage());
    }

    @Test
    public void testDailyLimitException_Throw() {
        try {
            throw new DailyLimitException("Limit exceeded");
        } catch (DailyLimitException e) {
            assertTrue("DailyLimitException fırlatılmalı", e instanceof DailyLimitException);
            assertEquals("Mesaj doğru olmalı", "Limit exceeded", e.getMessage());
        }
    }

    @Test
    public void testDailyLimitException_WithDetails() {
        String message = "Doctor has reached daily limit of 10 patients for date 2025-05-15";
        DailyLimitException exception = new DailyLimitException(message);
        
        assertTrue("Mesaj detay içermeli", exception.getMessage().contains("daily limit"));
        assertTrue("Mesaj sayı içermeli", exception.getMessage().contains("10"));
    }

    @Test
    public void testRendezvousLimitException_WithMessage() {
        String message = "Rendezvous limit exceeded";
        RendezvousLimitException exception = new RendezvousLimitException(message);
        
        assertNotNull("Exception oluşturulmalı", exception);
        assertEquals("Mesaj doğru olmalı", message, exception.getMessage());
    }

    @Test
    public void testRendezvousLimitException_Throw() {
        try {
            throw new RendezvousLimitException("Too many appointments");
        } catch (RendezvousLimitException e) {
            assertTrue("RendezvousLimitException fırlatılmalı", e instanceof RendezvousLimitException);
            assertEquals("Mesaj doğru olmalı", "Too many appointments", e.getMessage());
        }
    }

    @Test
    public void testRendezvousLimitException_WithPatientInfo() {
        String message = "Patient: John Doe, National ID: 12345678901 has more rendezvouses than limit: 5";
        RendezvousLimitException exception = new RendezvousLimitException(message);
        
        assertTrue("Mesaj hasta bilgisi içermeli", exception.getMessage().contains("Patient:"));
        assertTrue("Mesaj limit bilgisi içermeli", exception.getMessage().contains("limit:"));
    }

    @Test
    public void testChildOnlyException_WithMessage() {
        String message = "This section is only for children";
        ChildOnlyException exception = new ChildOnlyException(message);
        
        assertNotNull("Exception oluşturulmalı", exception);
        assertEquals("Mesaj doğru olmalı", message, exception.getMessage());
    }

    @Test
    public void testChildOnlyException_Throw() {
        try {
            throw new ChildOnlyException("Adult patient in child section");
        } catch (ChildOnlyException e) {
            assertTrue("ChildOnlyException fırlatılmalı", e instanceof ChildOnlyException);
            assertEquals("Mesaj doğru olmalı", "Adult patient in child section", e.getMessage());
        }
    }

    @Test
    public void testChildOnlyException_WithAgeInfo() {
        String message = "This section is only for children! Patient: John is over Age 18";
        ChildOnlyException exception = new ChildOnlyException(message);
        
        assertTrue("Mesaj yaş bilgisi içermeli", exception.getMessage().contains("Age 18"));
        assertTrue("Mesaj çocuk bilgisi içermeli", exception.getMessage().contains("children"));
    }

    @Test
    public void testAllExceptionsAreExceptions() {
        // Tüm custom exception'ların Exception'dan türediğini kontrol et
        IDException idEx = new IDException("test");
        DuplicateInfoException dupEx = new DuplicateInfoException("test");
        DailyLimitException dailyEx = new DailyLimitException("test");
        RendezvousLimitException rendEx = new RendezvousLimitException("test");
        ChildOnlyException childEx = new ChildOnlyException("test");
        
        assertTrue("IDException, Exception'dan türemeli", idEx instanceof Exception);
        assertTrue("DuplicateInfoException, Exception'dan türemeli", dupEx instanceof Exception);
        assertTrue("DailyLimitException, Exception'dan türemeli", dailyEx instanceof Exception);
        assertTrue("RendezvousLimitException, Exception'dan türemeli", rendEx instanceof Exception);
        assertTrue("ChildOnlyException, Exception'dan türemeli", childEx instanceof Exception);
    }

    @Test
    public void testExceptionCatching() {
        // Farklı exception türlerinin doğru yakalandığını test et
        try {
            throwIDException();
            fail("Exception fırlatılmalıydı");
        } catch (IDException e) {
            assertEquals("ID exception yakalandı", "ID error", e.getMessage());
        }
        
        try {
            throwDuplicateException();
            fail("Exception fırlatılmalıydı");
        } catch (DuplicateInfoException e) {
            assertEquals("Duplicate exception yakalandı", "Duplicate error", e.getMessage());
        }
    }

    private void throwIDException() throws IDException {
        throw new IDException("ID error");
    }

    private void throwDuplicateException() throws DuplicateInfoException {
        throw new DuplicateInfoException("Duplicate error");
    }

    @Test
    public void testExceptionStackTrace() {
        try {
            throw new IDException("Test stack trace");
        } catch (IDException e) {
            assertNotNull("Stack trace null olmamalı", e.getStackTrace());
            assertTrue("Stack trace boş olmamalı", e.getStackTrace().length > 0);
        }
    }

    @Test
    public void testMultipleExceptionTypes() {
        Exception[] exceptions = {
            new IDException("ID"),
            new DuplicateInfoException("Duplicate"),
            new DailyLimitException("Daily"),
            new RendezvousLimitException("Rendezvous"),
            new ChildOnlyException("Child")
        };
        
        for (Exception e : exceptions) {
            assertNotNull("Her exception null olmamalı", e);
            assertNotNull("Her exception mesajı null olmamalı", e.getMessage());
            assertNotNull("Her exception stack trace'i null olmamalı", e.getStackTrace());
        }
    }

    @Test
    public void testExceptionHierarchy() {
        // Exception hiyerarşisini test et
        IDException idEx = new IDException("test");
        
        assertTrue("Exception olarak yakalanabilmeli", idEx instanceof Exception);
        assertTrue("Throwable olarak yakalanabilmeli", idEx instanceof Throwable);
        assertTrue("IDException olarak yakalanabilmeli", idEx instanceof IDException);
    }

    @Test
    public void testExceptionWithSpecialCharacters() {
        String message = "Özel karakterler: ğüşıöçĞÜŞİÖÇ @#$%^&*()";
        IDException exception = new IDException(message);
        
        assertEquals("Özel karakterler desteklenmeli", message, exception.getMessage());
    }

    @Test
    public void testExceptionToString() {
        IDException exception = new IDException("Test message");
        String toString = exception.toString();
        
        assertNotNull("toString null olmamalı", toString);
        assertTrue("toString sınıf adı içermeli", toString.contains("IDException"));
    }

    @Test
    public void testNestedExceptions() {
        try {
            try {
                throw new IDException("Inner exception");
            } catch (IDException inner) {
                throw new DuplicateInfoException("Outer: " + inner.getMessage());
            }
        } catch (DuplicateInfoException outer) {
            assertTrue("Outer exception mesajı inner'ı içermeli", 
                outer.getMessage().contains("Inner exception"));
        }
    }

    @Test
    public void testExceptionInTryCatch() {
        boolean exceptionCaught = false;
        
        try {
            if (true) {
                throw new RendezvousLimitException("Limit test");
            }
        } catch (RendezvousLimitException e) {
            exceptionCaught = true;
        }
        
        assertTrue("Exception yakalanmalıydı", exceptionCaught);
    }

    @Test
    public void testMultipleCatchBlocks() {
        String caughtType = "";
        
        try {
            throw new DailyLimitException("Daily limit");
        } catch (IDException e) {
            caughtType = "ID";
        } catch (DailyLimitException e) {
            caughtType = "Daily";
        } catch (Exception e) {
            caughtType = "Generic";
        }
        
        assertEquals("Doğru catch bloğu çalışmalı", "Daily", caughtType);
    }

    @Test
    public void testExceptionMessage_TurkishCharacters() {
        String message = "Çocuk bölümü için yaş sınırı aşıldı";
        ChildOnlyException exception = new ChildOnlyException(message);
        
        assertEquals("Türkçe karakterler korunmalı", message, exception.getMessage());
        assertTrue("Türkçe karakter içermeli", exception.getMessage().contains("ü"));
    }

    @Test
    public void testExceptionEqualityByMessage() {
        IDException ex1 = new IDException("Same message");
        IDException ex2 = new IDException("Same message");
        
        // Exception'lar farklı nesneler
        assertNotSame("Farklı nesneler olmalı", ex1, ex2);
        
        // Ama mesajları aynı
        assertEquals("Mesajlar aynı olmalı", ex1.getMessage(), ex2.getMessage());
    }

    @Test
    public void testAllExceptionClasses() {
        // Tüm exception sınıflarının var olduğunu ve kullanılabilir olduğunu test et
        Class<?>[] exceptionClasses = {
            IDException.class,
            DuplicateInfoException.class,
            DailyLimitException.class,
            RendezvousLimitException.class,
            ChildOnlyException.class
        };
        
        for (Class<?> exClass : exceptionClasses) {
            assertNotNull("Exception sınıfı null olmamalı", exClass);
            assertTrue("Exception sınıfından türemeli", Exception.class.isAssignableFrom(exClass));
        }
    }
}

