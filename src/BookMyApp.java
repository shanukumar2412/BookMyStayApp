
import java.util.HashMap;
import java.util.Map;

public class BookMyApp {

        // Centralized inventory using HashMap
        private static HashMap<String, Integer> roomInventory = new HashMap<>();

        // Initialize room types
        public static void initializeInventory() {
            roomInventory.put("Single", 10);
            roomInventory.put("Double", 8);
            roomInventory.put("Suite", 5);
        }

        // Get availability
        public static int getAvailability(String roomType) {
            return roomInventory.getOrDefault(roomType, 0);
        }

        // Update availability
        public static void updateAvailability(String roomType, int newCount) {
            if (roomInventory.containsKey(roomType)) {
                roomInventory.put(roomType, newCount);
            } else {
                System.out.println("Room type not found.");
            }
        }

        // Display inventory
        public static void displayInventory() {
            System.out.println("Current Room Inventory:");
            for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }

        public static void main(String[] args) {

            // Initialize inventory
            initializeInventory();

            // Display inventory
            displayInventory();

            // Check availability
            System.out.println("\nAvailable Single Rooms: " + getAvailability("Single"));

            // Update inventory
            updateAvailability("Single", 7);

            // Display updated inventory
            System.out.println("\nUpdated Inventory:");
            displayInventory();
        }
    }
