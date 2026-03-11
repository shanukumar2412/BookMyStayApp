import java.util.*;

    public class BookMyApp {

        // Room Domain Object
        static class Room {
            private String roomType;
            private double price;

            public Room(String roomType, double price) {
                this.roomType = roomType;
                this.price = price;
            }

            public String getRoomType() {
                return roomType;
            }

            public double getPrice() {
                return price;
            }
        }

        // Centralized Inventory
        static class InventoryManager {
            private Map<String, Integer> availability = new HashMap<>();
            private Map<String, Room> rooms = new HashMap<>();

            public void addRoom(Room room, int quantity) {
                rooms.put(room.getRoomType(), room);
                availability.put(room.getRoomType(), quantity);
            }

            // Read-only access for search
            public Map<String, Integer> getAvailability() {
                return Collections.unmodifiableMap(availability);
            }

            public Room getRoom(String type) {
                return rooms.get(type);
            }
        }

        // Search Logic (Read-only)
        static class RoomSearchService {

            private InventoryManager inventory;

            public RoomSearchService(InventoryManager inventory) {
                this.inventory = inventory;
            }

            public void searchRooms() {

                Map<String, Integer> data = inventory.getAvailability();

                System.out.println("Available Rooms");
                System.out.println("-----------------------");

                for (String type : data.keySet()) {

                    int count = data.get(type);

                    if (count > 0) {
                        Room room = inventory.getRoom(type);

                        System.out.println("Room Type : " + room.getRoomType());
                        System.out.println("Price     : ₹" + room.getPrice());
                        System.out.println("Available : " + count);
                        System.out.println("-----------------------");
                    }
                }
            }
        }

        // Main Method
        public static void main(String[] args) {

            InventoryManager inventory = new InventoryManager();

            inventory.addRoom(new Room("Single", 2500), 4);
            inventory.addRoom(new Room("Double", 4000), 2);
            inventory.addRoom(new Room("Suite", 8000), 0);

            RoomSearchService searchService = new RoomSearchService(inventory);

            searchService.searchRooms();
        }
    }