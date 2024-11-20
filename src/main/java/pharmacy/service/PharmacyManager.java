package pharmacy.service;

import pharmacy.model.*;
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;

public class PharmacyManager {
    private Map<String, Medication> medicationInventory;
    private Map<String, Prescription> prescriptions;
    private String storeId;
    private static final int MINIMUM_STOCK_LEVEL = 10;

    public PharmacyManager(String storeId) {
        this.storeId = storeId;
        this.medicationInventory = new HashMap<>();
        this.prescriptions = new HashMap<>();
        loadInitialInventory();
        loadPrescriptions();
    }

    private void loadInitialInventory() {
        // Load from file or initialize with sample data
        try {
            String fileName = "./src/main/java/store/data/" + storeId + "_pharmacy_inventory.txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Medication med = new Medication(parts[0], parts[1], parts[2], 
                    Double.parseDouble(parts[3]), Integer.parseInt(parts[4]),
                    LocalDateTime.parse(parts[5]), parts[6], parts[7], 
                    Integer.parseInt(parts[8]));
                medicationInventory.put(med.getMedicationId(), med);
            }
        } catch (IOException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
            initializeDefaultInventory();
        }
    }

    private void initializeDefaultInventory() {
        // Add some sample medications
        Medication med1 = new Medication("MED001", "Aspirin", "OTC", 9.99, 100,
            LocalDateTime.now().plusYears(2), "Bayer", "Take with food", 20);
        medicationInventory.put(med1.getMedicationId(), med1);
    }

    private void loadPrescriptions() {
        try {
            String fileName = "./src/main/java/store/data/" + storeId + "_prescriptions.txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Prescription prescription = new Prescription(
                    parts[0],  // prescriptionId
                    parts[1],  // customerId
                    parts[2],  // medicationName
                    parts[3],  // dosage
                    Integer.parseInt(parts[4]),  // quantity
                    parts[5],  // instructions
                    parts[6],  // doctorName
                    LocalDateTime.parse(parts[7]),  // expirationDate
                    parts[9],  // insuranceProvider
                    Double.parseDouble(parts[10])  // price
                );
                prescription.setApproved(Boolean.parseBoolean(parts[8]));
                prescriptions.put(prescription.getPrescriptionId(), prescription);
            }
        } catch (IOException e) {
            System.err.println("Error loading prescriptions: " + e.getMessage());
        }
    }

    public boolean verifyPrescription(String prescriptionId) {
        Prescription prescription = prescriptions.get(prescriptionId);
        if (prescription == null) return false;
        return !prescription.getExpirationDate().isBefore(LocalDateTime.now());
    }

    public boolean hasSufficientStock(String medicationId, int quantity) {
        Medication medication = medicationInventory.get(medicationId);
        return medication != null && medication.getStockLevel() >= quantity;
    }

    public void updateStock(String medicationId, int quantity) {
        Medication medication = medicationInventory.get(medicationId);
        if (medication != null) {
            medication.setStockLevel(medication.getStockLevel() - quantity);
            checkStockLevels();
        }
    }

    private void checkStockLevels() {
        for (Medication med : medicationInventory.values()) {
            if (med.getStockLevel() < med.getMinimumStockLevel()) {
                System.out.println("WARNING: Low stock for " + med.getName() + 
                    " - Current level: " + med.getStockLevel());
            }
        }
    }

    public void savePrescription(Prescription prescription) {
        String fileName = "./src/main/java/store/data/" + storeId + "_prescriptions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%s,%s,%s,%s,%d,%s,%s,%s,%s,%s,%.2f%n",
                prescription.getPrescriptionId(),
                prescription.getCustomerId(),
                prescription.getMedicationName(),
                prescription.getDosage(),
                prescription.getQuantity(),
                prescription.getInstructions(),
                prescription.getDoctorName(),
                prescription.getExpirationDate(),
                prescription.isApproved(),
                prescription.getInsuranceProvider(),
                prescription.getPrice()));
        } catch (IOException e) {
            System.err.println("Error saving prescription: " + e.getMessage());
        }
    }

    public Medication getMedication(String medicationId) {
        return medicationInventory.get(medicationId);
    }

    public Collection<Medication> getAllMedications() {
        return medicationInventory.values();
    }

    public boolean recordRestock(String medicationId, int quantity, String expirationDate) {
        Medication medication = medicationInventory.get(medicationId);
        if (medication != null) {
            medication.setStockLevel(medication.getStockLevel() + quantity);
            return true;
        }
        return false;
    }

    public List<Medication> getExpiringMedications() {
        List<Medication> expiring = new ArrayList<>();
        LocalDateTime warningDate = LocalDateTime.now().plusMonths(3);
        
        for (Medication med : medicationInventory.values()) {
            if (med.getExpirationDate().isBefore(warningDate)) {
                expiring.add(med);
            }
        }
        return expiring;
    }

    public Prescription getPrescription(String prescriptionId) {
        return prescriptions.get(prescriptionId);
    }
}