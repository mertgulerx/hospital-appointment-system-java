# 🧪 Test Çalıştırma Rehberi

## ⚠️ Önemli Not

Test dosyaları **Maven projesi** için yazılmıştır. Doğrudan `javac` ile compile edilemez çünkü:

1. ❌ Ana proje kodları classpath'te olmalı
2. ❌ JUnit kütüphanesi gerekli
3. ❌ Tüm dependency'ler classpath'e eklenmiş olmalı

## ✅ Önerilen Yöntemler

### 🥇 **Yöntem 1: IntelliJ IDEA (EN KOLAY - ÖNERİLİR)**

#### Adımlar:
1. **Projeyi Aç**
   - IntelliJ IDEA'yı açın
   - `File → Open → hospital-appointment-system-java` klasörünü seçin
   - `pom.xml` dosyasını Maven projesi olarak açın

2. **Maven Dependencies'i İndir**
   - IntelliJ otomatik olarak Maven dependency'lerini indirecektir
   - Veya sağ üst köşede "Load Maven Changes" butonuna tıklayın

3. **Testleri Çalıştır**
   - Sol taraftaki proje ağacında `src/test/java` klasörünü açın
   - Herhangi bir test sınıfına sağ tıklayın (örn: `PersonTest.java`)
   - **"Run 'PersonTest'"** seçeneğine tıklayın
   
4. **Tüm Testleri Çalıştır**
   - `src/test/java` klasörüne sağ tıklayın
   - **"Run 'All Tests'"** seçin

#### Video Rehber:
[IntelliJ IDEA ile JUnit Testleri Çalıştırma](https://www.jetbrains.com/help/idea/performing-tests.html)

---

### 🥈 **Yöntem 2: Eclipse IDE**

#### Adımlar:
1. **Projeyi İçe Aktar**
   - `File → Import → Existing Maven Projects`
   - Proje klasörünü seçin
   - `Finish` butonuna tıklayın

2. **Maven Update**
   - Projeye sağ tıklayın
   - `Maven → Update Project` seçin
   - `Force Update of Snapshots/Releases` işaretleyin
   - `OK` tıklayın

3. **Testleri Çalıştır**
   - Test dosyasına sağ tıklayın
   - **Run As → JUnit Test** seçin

---

### 🥉 **Yöntem 3: Maven Komut Satırı**

#### Maven Kurulumu:

**Windows (Chocolatey ile):**
```powershell
# Chocolatey kurulu değilse önce onu kurun
Set-ExecutionPolicy Bypass -Scope Process -Force; 
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; 
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Maven'i kurun
choco install maven
```

**Manuel Kurulum:**
1. https://maven.apache.org/download.cgi adresinden indirin
2. ZIP dosyasını açın (örn: `C:\Program Files\Apache\maven`)
3. Sistem PATH değişkenine ekleyin: `C:\Program Files\Apache\maven\bin`
4. Yeni bir terminal açın ve test edin: `mvn --version`

#### Maven ile Test Çalıştırma:

```powershell
# Proje klasörüne gidin
cd "C:\Users\Ahmet Furkan\Desktop\hospital-appointment-system-java"

# Tüm testleri çalıştır
mvn test

# Belirli bir test sınıfını çalıştır
mvn test -Dtest=PersonTest

# Belirli bir test metodunu çalıştır
mvn test -Dtest=PersonTest#testPersonCreation

# Verbose output ile
mvn test -X
```

#### Beklenen Çıktı:
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running mertguler.Person.PersonTest
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running mertguler.Person.PatientTest
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
...
[INFO] BUILD SUCCESS
```

---

### 🔧 **Yöntem 4: VS Code**

#### Gereksinimler:
1. **Extension'ları Kurun:**
   - Extension Pack for Java
   - Maven for Java
   - Test Runner for Java

#### Adımlar:
1. Projeyi VS Code'da açın
2. Sol taraftaki "Testing" ikonuna tıklayın (beaker icon)
3. Test dosyalarını görün ve çalıştırın
4. Veya test dosyasını açın ve üstte görünen "Run Test" butonuna tıklayın

---

## 🚫 **Yapmayın:**

### ❌ Doğrudan javac ile Compile Etmeyin

```powershell
# BU ÇALIŞMAZ!
javac CRSTest.java
java CRSTest
```

**Neden?**
- Ana proje kodları compile edilmemiş
- JUnit kütüphanesi yok
- Classpath ayarları eksik
- Module system konfigürasyonu gerekli

---

## 📊 **Test Sonuçları Nasıl Görünür?**

### IntelliJ IDEA:
```
✓ PersonTest
  ✓ testPersonCreation (12ms)
  ✓ testGetName (3ms)
  ✓ testEquals_SameObject (2ms)
  ...

Tests passed: 15 of 15 tests - 150ms
```

### Maven:
```
Results :

Tests run: 325, Failures: 0, Errors: 0, Skipped: 0

[INFO] BUILD SUCCESS
[INFO] Total time: 8.523 s
```

---

## 🐛 **Sorun Giderme**

### Problem: "Module not found" hatası
**Çözüm:** 
- IntelliJ'de: `File → Project Structure → Modules → Dependencies` kontrol edin
- Maven'de: `mvn clean install` çalıştırın

### Problem: JUnit bulunamıyor
**Çözüm:**
- Maven dependency'lerini yeniden indirin
- IntelliJ'de "Reload Maven Project" tıklayın
- `pom.xml` dosyasının doğru olduğundan emin olun

### Problem: Test dosyaları tanınmıyor
**Çözüm:**
- `src/test/java` klasörünün "Test Sources Root" olarak işaretlendiğinden emin olun
- IntelliJ'de klasöre sağ tık → "Mark Directory as → Test Sources Root"

### Problem: Compile hatası
**Çözüm:**
```powershell
# Maven cache'i temizle
mvn clean

# Dependency'leri yeniden indir
mvn clean install -U

# IDE'yi yeniden başlat
```

---

## 📝 **Hızlı Başlangıç - IntelliJ IDEA**

```
1. IntelliJ IDEA'yı Aç
2. Open → Proje klasörünü seç
3. Maven otomatik olarak yüklenecek (biraz bekleyin)
4. src/test/java/mertguler/Person/PersonTest.java dosyasını aç
5. Yeşil play butonuna tıkla ▶️
6. Test sonuçlarını gör ✅
```

**İşte bu kadar!** ✨

---

## 💡 **İpuçları**

✅ **En kolay yol:** IntelliJ IDEA Community Edition (ücretsiz)
- https://www.jetbrains.com/idea/download/

✅ **Test yazarken:**
- Her testten sonra IDE'de otomatik çalıştır
- Kırmızı/yeşil feedback döngüsü
- Debug mode ile sorun giderin

✅ **CI/CD için:**
- GitHub Actions ile otomatik test
- Maven ile build pipeline

---

## 📞 **Yardım**

Sorun yaşarsanız:
1. IDE'nin Maven/Gradle konsol loglarını kontrol edin
2. `mvn --version` ile Maven kurulumunu doğrulayın
3. JDK 21 kurulu olduğundan emin olun: `java -version`

---

**Not:** Testler Maven standardına uygun yazılmıştır. Production ortamında kullanılabilir.

**İyi testler! 🚀**

