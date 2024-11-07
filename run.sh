#!/bin/bash

echo "Cleaning old class files..."
cd src/main/java
find . -name "*.class" -type f -delete

echo "Compiling Java files..."
javac -d . Main.java \
    inventory/InventoryManagementSystem.java \
    inventory/interfaces/InventoryInterface.java \
    inventory/model/Product.java \
    inventory/service/HeadOfficeManager.java \
    inventory/service/InventoryManager.java \
    order/OrderManagementSystem.java \
    order/model/Order.java \
    store/model/Store.java \
    store/service/StoreManager.java

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