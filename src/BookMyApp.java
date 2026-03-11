import java.util.Queue;
import java.util.LinkedList;

public class BookMyApp {

    // Inner class representing a booking request
    static class Reservation {
        String guestName;
        String roomType;

        Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        void displayRequest() {
            System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
        }
    }

    public static void main(String[] args) {

        // Booking Request Queue (FIFO)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Guests submit booking requests
        bookingQueue.add(new Reservation("Alice", "Single"));
        bookingQueue.add(new Reservation("Bob", "Double"));
        bookingQueue.add(new Reservation("Charlie", "Suite"));
        bookingQueue.add(new Reservation("David", "Single"));

        System.out.println("Booking Requests in Arrival Order:\n");

        // Display requests in queue order
        for (Reservation request : bookingQueue) {
            request.displayRequest();
        }

        System.out.println("\nTotal Requests in Queue: " + bookingQueue.size());
    }
}