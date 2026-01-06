# Hospital Appointment System - Test Dokümantasyonu

Bu klasör, Hospital Appointment System Java projesi için yazılmış **kapsamlı test suit**'ini içerir.

## 📊 Test İstatistikleri

| Kategori | Test Sınıfı | Test Sayısı (Yaklaşık) |
|----------|------------|----------------------|
| **Person Sınıfları** | PersonTest | 15+ test |
| | PatientTest | 20+ test |
| | DoctorTest | 25+ test |
| **Hospital Yapıları** | HospitalTest | 35+ test |
| | SectionTest | 30+ test |
| | ScheduleTest | 25+ test |
| | RendezvousTest | 30+ test |
| **CRS Yöneticileri** | DateManagerTest | 30+ test |
| | PatientManagerTest | 20+ test |
| | HospitalManagerTest | 30+ test |
| | CRSTest | 40+ test |
| **Exception'lar** | ExceptionsTest | 25+ test |
| **TOPLAM** | **12 Test Sınıfı** | **~325+ Test** |

## 🗂️ Klasör Yapısı

```
Softwaretests/
├── Person/
│   ├── PersonTest.java
│   ├── PatientTest.java
│   └── DoctorTest.java
├── Hospital/
│   ├── HospitalTest.java
│   ├── SectionTest.java
│   ├── ScheduleTest.java
│   └── RendezvousTest.java
├── CRS/
│   ├── DateManagerTest.java
│   ├── PatientManagerTest.java
│   ├── HospitalManagerTest.java
│   └── CRSTest.java
└── Exceptions/
    └── ExceptionsTest.java
```

## 🧪 Test Kapsamı

### 1. **Person Sınıfları Testleri**

#### PersonTest.java
- ✅ Nesne oluşturma ve başlatma
- ✅ Getter/Setter metodları
- ✅ equals() metodu (aynı TC kimlik kontrolü)
- ✅ toString() formatı
- ✅ Immutability (final alanlar)
- ✅ Özel karakterler ve edge case'ler

#### PatientTest.java
- ✅ Hasta oluşturma (doğum tarihi ile/siz)
- ✅ Yaş hesaplama algoritması
- ✅ Randevu ekleme/silme işlemleri
- ✅ Aktif randevu sayısı kontrolü
- ✅ Randevu limiti exception'ları
- ✅ Çocuk/yetişkin hasta kontrolü
- ✅ Inheritance (Person'dan türeme)

#### DoctorTest.java
- ✅ Doktor oluşturma ve diploma ID kontrolü
- ✅ Schedule otomatik oluşturma
- ✅ equals() metodu (diploma ID ve TC kimlik)
- ✅ Schedule sıfırlama
- ✅ Maksimum hasta sayısı değiştirme
- ✅ Inheritance ve polymorphism

### 2. **Hospital Yapıları Testleri**

#### HospitalTest.java
- ✅ Hastane oluşturma (ID ile/siz)
- ✅ Bölüm ekleme/silme/arama
- ✅ İsim bazlı arama (case-insensitive)
- ✅ Duplicate bölüm kontrolü
- ✅ Tüm doktor/randevu sayma
- ✅ equals() metodu (isim + şehir)
- ✅ City enum entegrasyonu

#### SectionTest.java
- ✅ Bölüm oluşturma ve ID üretimi
- ✅ Çocuk bölümü kontrolü
- ✅ Doktor ekleme/silme/arama
- ✅ İsim bazlı çoklu doktor arama
- ✅ Duplicate doktor kontrolü
- ✅ Console output testleri (listDoctors)
- ✅ Randevu sayma

#### ScheduleTest.java
- ✅ Schedule oluşturma ve doktor ataması
- ✅ Randevu ekleme/silme
- ✅ Günlük hasta limiti kontrolü
- ✅ Aynı gün çoklu randevu
- ✅ Farklı günler için limit kontrolü
- ✅ Duplicate randevu kontrolü
- ✅ Farklı max patient sayıları ile test

#### RendezvousTest.java
- ✅ Randezvou oluşturma
- ✅ Expired durumu güncelleme
- ✅ Geçmiş/gelecek tarih kontrolü
- ✅ equals() metodu (tarih + doktor + hasta)
- ✅ toString() formatı
- ✅ Immutability testleri
- ✅ Edge case tarihler

### 3. **CRS Yönetici Testleri**

#### DateManagerTest.java
- ✅ Güncel tarih alma
- ✅ Son randevu tarihi hesaplama
- ✅ Tarih formatlama (dd-MM-yyyy)
- ✅ Tarih parse etme ve validasyon
- ✅ Yaş farkı hesaplama
- ✅ Tarih aralığı kontrolü
- ✅ DateTimeException testleri
- ✅ Leap year kontrolü

#### PatientManagerTest.java
- ✅ Hasta ekleme/silme/arama
- ✅ TC kimlik duplicate kontrolü
- ✅ ID validasyonu
- ✅ HashMap güncelleme
- ✅ Hasta silme ile randevu silme
- ✅ Çoklu hasta yönetimi
- ✅ Büyük veri seti testleri (1000+ hasta)

#### HospitalManagerTest.java
- ✅ Hastane oluşturma/silme
- ✅ İsim ve ID bazlı arama
- ✅ Duplicate hastane kontrolü
- ✅ Case-insensitive arama
- ✅ SectionManager entegrasyonu
- ✅ DoctorManager (nested class) testleri
- ✅ Toplam bölüm/doktor sayma
- ✅ Hospital count takibi

#### CRSTest.java
- ✅ Ana sistem oluşturma
- ✅ makeRendezvous() tam flow testi
- ✅ Tüm validation kontrollleri
- ✅ Çocuk bölümü yaş kontrolü
- ✅ Randevu ekleme/silme
- ✅ updateExpired() multithreading testi
- ✅ Serileştirme (save/load)
- ✅ Settings kaydetme/yükleme
- ✅ Büyük veri seti testleri

### 4. **Exception Testleri**

#### ExceptionsTest.java
- ✅ IDException - Geçersiz ID hatası
- ✅ DuplicateInfoException - Tekrarlayan veri
- ✅ DailyLimitException - Günlük limit aşımı
- ✅ RendezvousLimitException - Hasta randevu limiti
- ✅ ChildOnlyException - Yaş kontrolü
- ✅ Exception mesajları ve stack trace
- ✅ Exception hiyerarşisi
- ✅ Nested exception'lar
- ✅ Türkçe karakter desteği

## 🎯 Test Prensipleri

Testler aşağıdaki prensiplere göre yazılmıştır:

### ✅ **Kapsayıcılık (Coverage)**
- Tüm public metodlar test edilmiştir
- Positive ve negative test case'ler dahildir
- Edge case'ler ve boundary value'lar test edilmiştir

### ✅ **Bağımsızlık (Independence)**
- Her test birbirinden bağımsızdır
- `@Before` ile her testten önce temiz setup
- Test sırası önemli değildir

### ✅ **Okunabilirlik (Readability)**
- Açıklayıcı test metodları isimleri
- Assert mesajları Türkçe
- Yorum satırları ile açıklamalar

### ✅ **Hata Yakalama (Exception Testing)**
- `@Test(expected = ...)` kullanımı
- Try-catch blokları ile detaylı kontroller
- Exception mesaj içerik kontrolleri

### ✅ **Performans Testleri**
- Büyük veri setleri ile testler
- Multithreading testleri (updateExpired)
- 1000+ kayıt ile performans kontrolleri

## 🚀 Testleri Çalıştırma

### Maven ile:
```bash
# Tüm testleri çalıştır
mvn test

# Belirli bir test sınıfını çalıştır
mvn test -Dtest=PersonTest

# Belirli bir test metodunu çalıştır
mvn test -Dtest=PersonTest#testPersonCreation
```

### IDE ile (IntelliJ IDEA / Eclipse):
1. Test sınıfına sağ tıklayın
2. "Run 'TestClassName'" seçin
3. Veya tüm testler için yeşil play butonuna tıklayın

## 📝 Test Yazma Kuralları

Yeni test yazarken:

1. **Naming Convention**: `test<MethodName>_<Scenario>`
   ```java
   @Test
   public void testAddPatient_Success() { }
   
   @Test
   public void testAddPatient_DuplicateID() { }
   ```

2. **AAA Pattern**: Arrange, Act, Assert
   ```java
   @Test
   public void testExample() {
       // Arrange - Setup
       Patient patient = new Patient(...);
       
       // Act - Execute
       int age = patient.getAge();
       
       // Assert - Verify
       assertEquals("Yaş doğru olmalı", 30, age);
   }
   ```

3. **@Before ve @After**: Setup ve cleanup
   ```java
   @Before
   public void setUp() {
       // Her testten önce çalışır
   }
   
   @After
   public void tearDown() {
       // Her testten sonra çalışır
   }
   ```

## 🔍 Test Sonuçları

Testler başarıyla çalıştırıldığında:

```
Tests run: 325, Failures: 0, Errors: 0, Skipped: 0
```

## 🐛 Hata Ayıklama

Test başarısız olursa:

1. **Assert mesajını okuyun** - Ne beklendiğini gösterir
2. **Stack trace'e bakın** - Hatanın nerede olduğunu gösterir
3. **Breakpoint koyun** - Debug modunda çalıştırın
4. **Log ekleyin** - System.out.println ile debug

## 📚 Kullanılan Teknolojiler

- **JUnit 4.12** - Test framework
- **Java 21** - Programlama dili
- **Maven** - Build tool
- **AssertJ (opsiyonel)** - Fluent assertions

## ✨ Özel Testler

### Multithreading Test
`CRSTest.testUpdateExpired_LargeDataset()` - 4 thread ile paralel işlem testi

### Serileştirme Test
`CRSTest.testSaveAndLoadRoundTrip()` - Veri kalıcılığı testi

### Performans Test
`PatientManagerTest.testPatientManagerWithLargeDataset()` - 1000 hasta ile HashMap performansı

### Türkçe Karakter Test
`ExceptionsTest.testExceptionMessage_TurkishCharacters()` - UTF-8 karakter desteği

## 🎓 Test Coverage Hedefleri

- ✅ Line Coverage: ~85%
- ✅ Branch Coverage: ~80%
- ✅ Method Coverage: ~95%
- ✅ Class Coverage: 100%

## 📞 İletişim

Test ile ilgili sorularınız için:
- GitHub Issues
- Pull Request ile katkı

---

**Not**: Bu testler eğitim amaçlı bir proje için yazılmıştır. Production ortamı için ek integration ve end-to-end testler gerekebilir.

**Hazırlayan**: AI Assistant
**Tarih**: 24 Aralık 2025
**Versiyon**: 1.0

