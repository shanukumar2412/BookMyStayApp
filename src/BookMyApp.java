import java.util.Queue;
import java.util.LinkedList;

public class BookMyApp {

    // Reservation represents a booking request
    static class Reservation {
        String guestName;
        String roomType;

        Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    // Queue for booking requests
    private static Queue<Reservation> bookingQueue = new LinkedList<>();

    // Accept booking request
    public static void submitBookingRequest(String guestName, String roomType) {
        bookingQueue.add(new Reservation(guestName, roomType));
    }

    // Display requests
    public static void displayBookingRequests() {

        System.out.println("Booking Requests in Arrival Order:\n");

        for (Reservation r : bookingQueue) {
            System.out.println("Guest: " + r.guestName + " | Requested Room: " + r.roomType);
        }

        System.out.println("\nTotal Requests in Queue: " + bookingQueue.size());
    }

    public static void main(String[] args) {

        submitBookingRequest("Alice", "Single");
        submitBookingRequest("Bob", "Double");
        submitBookingRequest("Charlie", "Suite");
        submitBookingRequest("David", "Single");

        displayBookingRequests();
    }
}