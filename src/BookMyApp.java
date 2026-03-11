public class BookMyApp {
    // Abstract Room class
    static abstract class Room {
        private String roomType;
        private int beds;
        private int size;
        private double price;

        public Room(String roomType, int beds, int size, double price) {
            this.roomType = roomType;
            this.beds = beds;
            this.size = size;
            this.price = price;
        }

        public void displayRoomDetails() {
            System.out.println("Room Type : " + roomType);
            System.out.println("Beds      : " + beds);
            System.out.println("Size      : " + size + " sqft");
            System.out.println("Price     : $" + price);
        }
    }

    // Single Room class
    static class SingleRoom extends Room {
        public SingleRoom() {
            super("Single Room", 1, 200, 100.0);
        }
    }

    // Double Room class
    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 350, 180.0);
        }
    }

    // Suite Room class
    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite Room", 3, 500, 300.0);
        }
    }

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Room Availability =====");

        // Room objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        singleRoom.displayRoomDetails();
        System.out.println("Available : " + singleAvailable);
        System.out.println("--------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available : " + doubleAvailable);
        System.out.println("--------------------------------");

        suiteRoom.displayRoomDetails();
        System.out.println("Available : " + suiteAvailable);
        System.out.println("--------------------------------");

        System.out.println("Application Terminated.");
    }
}