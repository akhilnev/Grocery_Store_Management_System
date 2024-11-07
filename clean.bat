@echo off
echo Cleaning old class files...
cd src\main\java
del /s /q *.class
echo Compilation directory cleaned.
cd ..\..\..