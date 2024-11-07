@echo off
echo Building Grocery Store Management System...

rem Clean previous compilation
call clean.bat

echo Compiling Java files...
cd src\main\java

rem Compile all Java files in all subdirectories
javac -d . Main.java assistance\**\*.java inventory\**\*.java order\**\*.java security\**\*.java store\**\*.java

if errorlevel 1 (
    echo Compilation failed!
    cd ..\..\..
    exit /b 1
)

echo Running the program...
java Main

cd ..\..\..
echo Done.
pause