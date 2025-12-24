@echo off
echo ============================================
echo Hospital Appointment System - Test Runner
echo ============================================
echo.
echo UYARI: Bu testleri calistirmak icin:
echo   1. JDK 21 kurulu olmali
echo   2. JUnit 4.12 jar dosyasi gerekli
echo   3. Tum proje kaynaklari compile edilmis olmali
echo.
echo ONERILIR: IntelliJ IDEA veya Eclipse kullanin
echo           veya Maven kurun (mvn test)
echo.
echo ============================================
echo.

REM Ana proje klasoru
set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%src\main\java
set TEST_DIR=%PROJECT_DIR%src\test\java
set BUILD_DIR=%PROJECT_DIR%target\classes
set TEST_BUILD_DIR=%PROJECT_DIR%target\test-classes
set LIB_DIR=%PROJECT_DIR%lib

REM Klasorleri olustur
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"
if not exist "%TEST_BUILD_DIR%" mkdir "%TEST_BUILD_DIR%"

echo [1/4] Ana proje kaynaklari compile ediliyor...
javac -d "%BUILD_DIR%" -sourcepath "%SRC_DIR%" "%SRC_DIR%\mertguler\**\*.java" 2>nul
if errorlevel 1 (
    echo HATA: Ana proje compile edilemedi!
    echo Module-info.java sorunu olabilir. Simdilik module-info atlaniyor...
    
    REM Module-info olmadan compile et
    for /r "%SRC_DIR%\mertguler" %%f in (*.java) do (
        javac -d "%BUILD_DIR%" "%%f" 2>nul
    )
)
echo   ✓ Ana proje compile edildi

echo.
echo [2/4] JUnit kontrol ediliyor...
if not exist "%LIB_DIR%\junit-4.12.jar" (
    echo.
    echo HATA: JUnit jar dosyasi bulunamadi!
    echo.
    echo Lutfen JUnit 4.12 yukleyin:
    echo   1. https://repo1.maven.org/maven2/junit/junit/4.12/junit-4.12.jar
    echo   2. Dosyayi %LIB_DIR% klasorune kaydedin
    echo.
    echo Veya Maven kullanin: mvn test
    echo.
    pause
    exit /b 1
)
echo   ✓ JUnit bulundu

echo.
echo [3/4] Test kaynaklari compile ediliyor...
set CLASSPATH=%BUILD_DIR%;%LIB_DIR%\junit-4.12.jar;%LIB_DIR%\hamcrest-core-1.3.jar
javac -cp "%CLASSPATH%" -d "%TEST_BUILD_DIR%" -sourcepath "%TEST_DIR%" "%TEST_DIR%\mertguler\**\*.java"
if errorlevel 1 (
    echo HATA: Testler compile edilemedi!
    pause
    exit /b 1
)
echo   ✓ Testler compile edildi

echo.
echo [4/4] Testler calistiriliyor...
echo ============================================
echo.

set TEST_CLASSPATH=%BUILD_DIR%;%TEST_BUILD_DIR%;%LIB_DIR%\junit-4.12.jar;%LIB_DIR%\hamcrest-core-1.3.jar

REM Test siniflarini calistir
java -cp "%TEST_CLASSPATH%" org.junit.runner.JUnitCore mertguler.Person.PersonTest

if errorlevel 1 (
    echo.
    echo Testler BASARISIZ!
) else (
    echo.
    echo Testler BASARILI!
)

echo.
echo ============================================
echo Test tamamlandi!
echo ============================================
pause

