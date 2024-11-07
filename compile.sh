# Create output directory
mkdir -p out

# 1. Compile interfaces
javac -d out src/main/java/inventory/interfaces/*.java

# 2. Compile models
javac -d out src/main/java/inventory/model/*.java
javac -d out src/main/java/order/model/*.java
javac -d out src/main/java/store/model/*.java
javac -d out src/main/java/supplier/model/*.java
javac -d out src/main/java/returns/model/*.java
javac -d out src/main/java/report/model/*.java

# 3. Compile services
javac -d out -cp out src/main/java/inventory/service/*.java
javac -d out -cp out src/main/java/store/service/*.java
javac -d out -cp out src/main/java/supplier/service/*.java
javac -d out -cp out src/main/java/returns/service/*.java
javac -d out -cp out src/main/java/report/service/*.java

# 4. Compile management systems
javac -d out -cp out src/main/java/inventory/*.java
javac -d out -cp out src/main/java/order/*.java
javac -d out -cp out src/main/java/supplier/*.java
javac -d out -cp out src/main/java/returns/*.java
javac -d out -cp out src/main/java/report/*.java

# 5. Compile Main
javac -d out -cp out src/main/java/Main.java