# ⚡ Eclipse ile Test Çalıştırma Rehberi

## 🎯 Eclipse'de Testler Nasıl Çalıştırılır?

Eclipse'de testlerinizi çalıştırmak için 3 kolay adım!

---

## 📥 **Adım 1: Eclipse'i Hazırlayın**

### Eclipse Yüklü Değilse:

1. **Eclipse IDE for Java Developers** indirin:
   ```
   https://www.eclipse.org/downloads/
   ```

2. **"Download" butonuna tıklayın**

3. İşletim sisteminizi seçin (Windows x64)

4. Eclipse Installer'ı çalıştırın:
   - **"Eclipse IDE for Java Developers"** seçin
   - Kurulum klasörünü seçin
   - "Install" butonuna tıklayın

---

## 📂 **Adım 2: Projeyi İçe Aktarın**

### 1. Eclipse'i Başlatın

### 2. Workspace Seçin
- Varsayılan workspace'i kullanabilirsiniz
- Veya: `C:\Users\Ahmet Furkan\eclipse-workspace`

### 3. Maven Projesini İçe Aktarın

**File → Import**

```
┌─────────────────────────────────────┐
│ Select an import wizard:            │
│                                     │
│ 🔍 Type filter text                 │
│                                     │
│ ▼ Maven                             │
│   ► Existing Maven Projects  ◄─── BUNU SEÇİN
│   ► Check out Maven Projects...    │
│                                     │
│          [Cancel]  [Next >]         │
└─────────────────────────────────────┘
```

### 4. Proje Klasörünü Seçin

**Next > butonuna tıkladıktan sonra:**

```
Root Directory: [Browse...]
```

**Browse** butonuna tıklayın ve şu klasörü seçin:
```
C:\Users\Ahmet Furkan\Desktop\hospital-appointment-system-java
```

### 5. pom.xml'i Seçin

Eclipse otomatik olarak `pom.xml` dosyasını bulacak:

```
☑ /pom.xml - hospital-reservation-system-java
```

**Finish** butonuna tıklayın!

### 6. Maven Update (Otomatik)

Eclipse otomatik olarak:
- ✅ Maven dependency'lerini indirecek
- ✅ JUnit'i ekleyecek
- ✅ Projeyi build edecek

⏳ **Bekleyin:** Sağ altta progress bar göreceksiniz
```
"Building workspace... (XX%)"
```

Bu işlem **2-3 dakika** sürebilir. İnternet bağlantısı gerekli!

---

## 🧪 **Adım 3: Test Çalıştırın**

### Yöntem 1: Tek Bir Test Dosyası

1. **Package Explorer'da** (sol panel) şu yolu açın:
   ```
   hospital-reservation-system-java
   └── src/test/java
       └── mertguler.Person
           └── PersonTest.java
   ```

2. **PersonTest.java** dosyasına **SAĞ TIK**

3. **"Run As" → "JUnit Test"** seçin

4. 🎉 **Sonuç:** Alt tarafta JUnit paneli açılacak!

```
┌─────────────────────────────────────────┐
│ JUnit                            [X] [_] │
├─────────────────────────────────────────┤
│ ✅ PersonTest                     15/15  │
│   ✅ testPersonCreation            12ms  │
│   ✅ testGetName                    3ms  │
│   ✅ testSetName                    2ms  │
│   ✅ testEquals_SameObject          2ms  │
│   ... (11 daha)                          │
│                                          │
│ Runs: 15/15   Errors: 0   Failures: 0   │
│ ██████████████████████████████  (100%)   │
└─────────────────────────────────────────┘
```

### Yöntem 2: Tüm Testleri Çalıştırın

1. **src/test/java** klasörüne **SAĞ TIK**

2. **"Run As" → "JUnit Test"** seçin

3. Tüm 12 test sınıfı (~325 test) çalışacak!

```
✅ mertguler.Person.PersonTest        15 passed
✅ mertguler.Person.PatientTest       20 passed
✅ mertguler.Person.DoctorTest        25 passed
✅ mertguler.Hospital.HospitalTest    35 passed
✅ mertguler.Hospital.SectionTest     30 passed
... (devamı)

Total: 325 tests - 8.5 seconds
```

---

## 🎨 **Görsel Rehber**

### Eclipse Ekranı Şöyle Görünecek:

```
┌────────────────────────────────────────────────────────┐
│ Eclipse IDE                             [- □ X]        │
├─────────────┬──────────────────────────────────────────┤
│ Package     │ PersonTest.java                  [Save]  │
│ Explorer    │                                           │
│             │ package mertguler.Person;                 │
│ ▼ hospital  │                                           │
│   ▼ src     │ import org.junit.Test;                    │
│     ▼ test  │ import static org.junit.Assert.*;         │
│       ▼ java│                                           │
│    ▼mertguler│ public class PersonTest {                │
│      ▶ CRS   │                                          │
│      ▶ Hospital│    @Test                               │
│      ▼ Person│    public void testPersonCreation() {   │
│        PersonTest.java ◄─ SAĞ TIK → Run As → JUnit    │
│        PatientTest.java│        Person p = new Person(...);│
│        DoctorTest.java│        assertNotNull(p);         │
│      ▶ Exceptions│    }                                  │
│             │ }                                          │
├─────────────┴──────────────────────────────────────────┤
│ JUnit                                            [X][_] │
│ ✅ PersonTest                              15/15 (150ms)│
│   ✅ testPersonCreation                          12ms   │
│   ✅ testGetName                                  3ms   │
│   ✅ testSetName                                  2ms   │
│                                                         │
│ Runs: 15/15  ██████████████████████  Errors: 0  Fail: 0│
└─────────────────────────────────────────────────────────┘
```

---

## 🔧 **Sorun Giderme**

### Problem 1: Maven dependency'ler inmiyor

**Çözüm:**
1. Projeye **SAĞ TIK**
2. **"Maven" → "Update Project"** seçin
3. ☑ **"Force Update of Snapshots/Releases"** işaretleyin
4. **OK** butonuna tıklayın

```
┌─────────────────────────────────────┐
│ Update Maven Project                │
│                                     │
│ ☑ hospital-reservation-system-java │
│                                     │
│ ☑ Force Update of Snapshots/Releases │
│ ☐ Update project configuration      │
│ ☐ Refresh workspace resources       │
│ ☐ Clean projects                    │
│                                     │
│          [Cancel]  [OK]              │
└─────────────────────────────────────┘
```

### Problem 2: JUnit bulunamıyor

**Çözüm:**
1. `pom.xml` dosyasını açın
2. Dosyada herhangi bir yere **SAĞ TIK**
3. **"Maven" → "Add Dependency"** seçin
4. Arama: `junit`
5. `junit:junit:4.12` seçin
6. **OK** tıklayın

### Problem 3: Test klasörü tanınmıyor

**Çözüm:**
1. `src/test/java` klasörüne **SAĞ TIK**
2. **"Build Path" → "Use as Source Folder"** seçin

### Problem 4: "Cannot resolve symbol" hataları

**Çözüm:**
1. **"Project" → "Clean"** menüsüne tıklayın
2. ☑ **"Clean all projects"** seçin
3. **OK** tıklayın
4. Eclipse otomatik rebuild edecek

### Problem 5: Java 21 bulunamıyor

**Çözüm:**
1. Projeye **SAĞ TIK**
2. **"Properties"** seçin
3. Sol menüden **"Java Build Path"** seçin
4. **"Libraries"** tab'ına tıklayın
5. **"JRE System Library"** seçin → **Edit** butonuna tıklayın
6. **"Installed JREs"** butonuna tıklayın
7. **"Add"** → JDK 21 yolunu gösterin

---

## ⌨️ **Klavye Kısayolları (Eclipse)**

- `Alt + Shift + X, T` → JUnit Test çalıştır
- `Ctrl + F11` → Son testi tekrar çalıştır
- `F11` → Debug mode ile çalıştır
- `Ctrl + Shift + T` → Test dosyası ara

---

## 📊 **Başarı Göstergeleri**

Testler başarıyla çalışıyorsa:

```
✅ Yeşil bar (progress bar)
✅ "Runs: X/X" (hepsi çalıştı)
✅ "Errors: 0"
✅ "Failures: 0"
✅ Tüm testlerin yanında yeşil ✓ işareti
```

Hata varsa:
```
❌ Kırmızı bar
❌ "Failures: X" (sıfırdan fazla)
❌ Bazı testlerin yanında kırmızı X
```

---

## 🎯 **Test Sonuçlarını Anlama**

### Başarılı Test:
```
✅ testPersonCreation    12ms
```
- Yeşil check mark = Test geçti
- 12ms = Test süresi

### Başarısız Test:
```
❌ testPersonCreation    Failed
   Expected: <Person> but was: <null>
   at PersonTest.java:25
```
- Kırmızı X = Test başarısız
- Hata mesajı gösterir ne beklediğini

---

## 🚀 **İleri Seviye: Maven Terminal**

Eclipse'de Maven komutları da çalıştırabilirsiniz:

1. Projeye **SAĞ TIK**
2. **"Run As" → "Maven build..."** seçin
3. Goals: `test`
4. **Run** butonuna tıklayın

Console'da Maven çıktısını göreceksiniz:
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running mertguler.Person.PersonTest
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
...
[INFO] BUILD SUCCESS
```

---

## 💡 **İpuçları**

### ✅ **Otomatik Test Çalıştırma**

Eclipse'de **"Save Actions"** ile her kaydettiğinizde otomatik test:

1. **"Window" → "Preferences"**
2. **"Java" → "Editor" → "Save Actions"**
3. ☑ **"Perform the selected actions on save"** işaretleyin
4. **"Additional actions"** → **Configure**
5. İstediğiniz aksiyonları seçin

### ✅ **Code Coverage Görüntüleme**

Test coverage'i görmek için:

1. Test dosyasına **SAĞ TIK**
2. **"Coverage As" → "JUnit Test"** seçin
3. Yeşil = test edildi, Kırmızı = test edilmedi

### ✅ **Hızlı Test Oluşturma**

Yeni test eklemek için:
1. Test edilecek sınıfın içindeyken
2. **Ctrl + N** → **"JUnit Test Case"**
3. Eclipse otomatik test template oluşturur

---

## 🎓 **Eclipse vs IntelliJ**

| Özellik | Eclipse | IntelliJ IDEA |
|---------|---------|---------------|
| Ücretsiz | ✅ Tamamen | ✅ Community |
| Maven | ✅ Manuel update | ✅ Otomatik |
| Hafıza | Hafif | Biraz ağır |
| Kullanım | Orta | Çok kolay |
| Setup | 3-5 dk | 2 dk |

**Her ikisi de testlerinizi çalıştırır!** 🎉

---

## 📝 **Checklist: Eclipse'de Test Başarılı mı?**

- [ ] Projeyi Maven projesi olarak import ettim
- [ ] Maven update yaptım (sağ tık → Maven → Update Project)
- [ ] pom.xml dosyası var ve açılıyor
- [ ] src/test/java klasörü görünüyor
- [ ] Test dosyasına sağ tık → Run As → JUnit Test seçeneği var
- [ ] Test çalıştırınca JUnit paneli açılıyor
- [ ] Yeşil bar görüyorum
- [ ] Tüm testler geçiyor (Errors: 0, Failures: 0)

**Tümü ✅ ise başardınız!** 🎊

---

## 📞 **Yardım Lazım mı?**

### Sık Karşılaşılan Hatalar:

**"Build path is incomplete"**
→ Maven Update yapın

**"JUnit not found"**
→ pom.xml kontrolü + Maven Update

**"Tests not running"**
→ src/test/java → Build Path → Use as Source Folder

**"Java version mismatch"**
→ Properties → Java Compiler → 21 seçin

---

## ✅ **Başardınız mı?**

Test çalıştıysa artık:
- ✅ Eclipse'de Maven projesi açtınız
- ✅ JUnit testleri çalıştırdınız
- ✅ Test sonuçlarını yorumladınız
- ✅ Modern Java geliştirme yaptınız

**Tebrikler! Eclipse'de testler çalışıyor! 🎉**

---

**Not:** Eclipse, Java geliştirme için en eski ve güvenilir IDE'lerden biridir.
Birçok profesyonel geliştirici Eclipse kullanır.

**İyi testler! 🚀**

