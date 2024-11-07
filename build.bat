@echo off
echo Building Grocery Store Management System...

rem Clean previous compilation
call clean.bat

echo Compiling Java files...
cd src\main\java

rem Compile all Java files maintaining package structure
javac -d . Main.java ^
    inventory\InventoryManagementSystem.java ^
    inventory\interfaces\InventoryInterface.java ^
    inventory\model\Product.java ^
    inventory\service\HeadOfficeManager.java ^
    inventory\service\InventoryManager.java ^
    order\OrderManagementSystem.java ^
    order\model\Order.java ^
    store\model\Store.java ^
    store\service\StoreManager.java

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