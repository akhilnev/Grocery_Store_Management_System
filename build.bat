@echo off
echo Building Grocery Store Management System...

rem Clean previous compilation
call clean.bat

echo Compiling Java files...
cd src\main\java

rem Compile all Java files maintaining package structure
javac -d . Main.java ^
    assistance\model\AssistanceRequest.java ^
    assistance\service\CustomerAssistanceManager.java ^
    employee\PayrollSystem.java ^
    inventory\interfaces\InventoryInterface.java ^
    inventory\interfaces\HeadOfficeInterface.java ^
    inventory\model\Product.java ^
    inventory\service\InventoryManager.java ^
    inventory\service\HeadOfficeManager.java ^
    inventory\InventoryManagementSystem.java ^
    maintenance\MaintenanceSystem.java ^
    marketing\service\MarketingManager.java ^
    order\model\Order.java ^
    order\OrderManagementSystem.java ^
    report\SalesReportSystem.java ^
    returns\model\Return.java ^
    returns\model\ReturnItem.java ^
    returns\service\ReturnManager.java ^
    returns\ReturnManagementSystem.java ^
    security\model\SecurityAlertSystem.java ^
    security\model\SecurityIncident.java ^
    security\service\SecurityManager.java ^
    store\model\Store.java ^
    store\service\StoreManager.java ^
    supplier\model\Shipment.java ^
    supplier\service\SupplierManager.java ^
    supplier\SupplierManagementSystem.java

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