# ⚡ 2 Dakikada Test Çalıştırma

## 🎯 Adım Adım Rehber

### 📥 **Adım 1: IntelliJ IDEA İndirin (2 dk)**

1. Tarayıcınızda şu adresi açın:
   ```
   https://www.jetbrains.com/idea/download/
   ```

2. **Community Edition** (ÜCRETSİZ) butonuna tıklayın

3. İndirme tamamlanınca kurulum dosyasını çalıştırın

4. Kurulum sırasında:
   - ✅ "Add to PATH" işaretleyin
   - ✅ ".java files" ile ilişkilendir
   - ✅ Varsayılan ayarlarla devam edin

---

### 📂 **Adım 2: Projeyi Açın (30 sn)**

1. IntelliJ IDEA'yı başlatın

2. **"Open"** butonuna tıklayın

3. Şu klasörü seçin:
   ```
   C:\Users\Ahmet Furkan\Desktop\hospital-appointment-system-java
   ```

4. **"Trust Project"** diyaloğunda **"Trust Project"** seçin

5. Bekleyin... IntelliJ otomatik olarak:
   - ✅ Maven'i tanıyacak
   - ✅ pom.xml'i okuyacak
   - ✅ JUnit'i indirecek
   - ✅ Projeyi build edecek
   
   (Sağ altta progress bar göreceksiniz - bitsin)

---

### 🧪 **Adım 3: Test Çalıştırın (10 sn)**

1. Sol taraftaki **Project** panelinde şu yolu açın:
   ```
   src → test → java → mertguler → Person
   ```

2. **PersonTest.java** dosyasına **ÇİFT TIKLAYIN**

3. Dosya açıldığında, **SOL tarafta** yeşil play ikonları (▶️) göreceksiniz

4. **Sınıf adının yanındaki** yeşil play'e tıklayın

5. **"Run 'PersonTest'"** seçeneğine tıklayın

6. 🎉 **SONUÇ:** Alt tarafta test sonuçları görünecek!
   ```
   ✅ Tests passed: 15 of 15 tests
   ```

---

## 🎥 **Görsel Rehber**

### Şöyle görünecek:

```
┌─────────────────────────────────────────────────┐
│ IntelliJ IDEA                          [- □ X] │
├──────────┬──────────────────────────────────────┤
│ Project  │ PersonTest.java                      │
│          │                                       │
│ ▼ src    │ package mertguler.Person;            │
│   ▼ test │                                       │
│     ▼ java│ import org.junit.Test;              │
│       ▼ mertguler│                              │
│         ▶ CRS   │ public class PersonTest {     │
│         ▶ Hospital│   @Test                     │
│         ▶ Person  │   public void testPerson()  │
│           ▶ PersonTest.java  ◀── BURAYA ÇİFT TIK│
│         ▶ Exceptions│   }                        │
│                   │ }                            │
├───────────────────┴──────────────────────────────┤
│ ▶️ Run: PersonTest                               │
│ ✅ testPersonCreation                    12ms   │
│ ✅ testGetName                           3ms    │
│ ✅ testEquals_SameObject                 2ms    │
│                                                  │
│ Tests passed: 15 of 15 tests - 150ms           │
└──────────────────────────────────────────────────┘
```

---

## 🚀 **Alternatif: Tüm Testleri Çalıştır**

1. Sol panelde **`src/test/java`** klasörüne **SAĞ TIK**

2. **"Run 'All Tests'"** seçin

3. Tüm 325+ test çalışacak!
   ```
   ✅ PersonTest: 15 passed
   ✅ PatientTest: 20 passed
   ✅ DoctorTest: 25 passed
   ✅ HospitalTest: 35 passed
   ... (devamı)
   
   Total: 325 tests passed in 8.5s
   ```

---

## ❓ **Neden IntelliJ?**

### ✅ **Otomatik Hallediyor:**
- ✅ Maven'i tanıyor
- ✅ JUnit'i indiriyor
- ✅ Projeyi compile ediyor
- ✅ Classpath'i ayarlıyor
- ✅ Testleri çalıştırıyor

### 🆚 **Manuel Yöntem vs IntelliJ:**

| Özellik | Manuel (javac) | IntelliJ IDEA |
|---------|---------------|---------------|
| Kurulum | JUnit manuel indir | Otomatik |
| Build | javac komutları | Tek tık |
| Classpath | Manuel ayarla | Otomatik |
| Test Çalıştır | java komutları | Tek tık ▶️ |
| Süre | ~30 dakika | ~2 dakika |
| Zorluk | 😫😫😫 | 😊 |

---

## 🔄 **Sorun mu Yaşadınız?**

### Problem 1: Maven indirmiyor
**Çözüm:**
1. Sağ altta **"Import"** veya **"Load Maven Changes"** butonuna tıklayın
2. Veya: `File → Invalidate Caches → Restart`

### Problem 2: Test dosyaları tanınmıyor
**Çözüm:**
1. `src/test/java` klasörüne **SAĞ TIK**
2. **"Mark Directory as → Test Sources Root"** seçin

### Problem 3: JUnit bulunamıyor
**Çözüm:**
1. `View → Tool Windows → Maven`
2. Yenile (🔄) ikonuna tıklayın
3. `Lifecycle → clean` sonra `Lifecycle → install` çalıştırın

---

## 🎓 **Bonus: Klavye Kısayolları**

- `Ctrl + Shift + F10` → Test çalıştır
- `Shift + F10` → Son testi tekrar çalıştır
- `Ctrl + Shift + F9` → Debug mode
- `Ctrl + F5` → Testi yeniden çalıştır

---

## ✅ **Başarı Kontrol Listesi**

Testler çalıştıysa şunu görmelisiniz:

```
✅ Yeşil check mark'lar
✅ "Tests passed: X of X"
✅ Alt tarafta yeşil bar
✅ Hiç kırmızı hata yok
```

---

## 📞 **Hala Sorun mu Var?**

IntelliJ IDEA'da sorun yaşarsanız:

1. **Önce şunu deneyin:**
   ```
   File → Invalidate Caches → Restart
   ```

2. **Maven'i yeniden yükleyin:**
   - Maven panelini açın (sağ taraf)
   - 🔄 Reload butonuna tıklayın

3. **JDK'yı kontrol edin:**
   ```
   File → Project Structure → Project
   SDK: Java 21 olmalı
   ```

---

## 🎉 **Başardınız mı?**

İlk test çalıştığında:
- ✅ Test yazmayı öğrendiniz
- ✅ JUnit kullanmayı öğrendiniz
- ✅ Maven projesini anladınız
- ✅ Modern IDE kullanmaya başladınız

**Tebrikler! 🎊**

---

**Not:** Bu rehber yeni başlayanlar için yazılmıştır. 
IntelliJ IDEA, Java geliştirme için en popüler IDE'lerden biridir.

**İyi kodlamalar! 💻**

