#!/bin/bash

# Create output directory if it doesn't exist
mkdir -p out

# Compile all Java files
javac -d out src/main/java/inventory/model/*.java \
    src/main/java/inventory/service/*.java \
    src/main/java/inventory/interfaces/*.java \
    src/main/java/order/model/*.java \
    src/main/java/store/model/*.java \
    src/main/java/store/service/*.java \
    src/main/java/supplier/model/*.java \
    src/main/java/supplier/service/*.java \
    src/main/java/returns/model/*.java \
    src/main/java/returns/service/*.java \
    src/main/java/report/model/*.java \
    src/main/java/report/service/*.java \
    src/main/java/report/*.java \
    src/main/java/inventory/*.java \
    src/main/java/order/*.java \
    src/main/java/supplier/*.java \
    src/main/java/returns/*.java \
    src/main/java/employee/model/*.java \
    src/main/java/employee/service/*.java \
    src/main/java/employee/*.java \
    src/main/java/maintenance/model/*.java \
    src/main/java/maintenance/service/*.java \
    src/main/java/maintenance/*.java \
    src/main/java/Main.java

# Run the program if compilation was successful
if [ $? -eq 0 ]; then
    java -cp out Main
else
    echo "Compilation failed"
fi 