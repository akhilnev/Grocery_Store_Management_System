#!/bin/bash

echo "Cleaning old class files..."
cd src/main/java
find . -name "*.class" -type f -delete

echo "Compiling Java files..."
javac -d . $(find . -name "*.java")

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running the program..."
    java Main
else
    echo "Compilation failed!"
    exit 1
fi

cd ../../..
echo "Program execution completed."