import java.io.*;
import java.util.*;
import java.util.concurrent.*;

// Custom Exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) { super(message); }
}

// Add-On Service class
class AddOnService implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private double cost;
    public AddOnService(String name, double cost) { this.name = name; this.cost = cost; }
    public double getCost() { return cost; }
    @Override
    public String toString() { return name + " (₹" + cost + ")"; }
}

// Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private List<String> rooms = new ArrayList<>();
    private List<AddOnService> services = new ArrayList<>();

    public Reservation(String reservationId, String guestName) throws InvalidBookingException {
        if (reservationId == null || reservationId.isEmpty()) throw new InvalidBookingException("Invalid reservation ID.");
        if (guestName == null || guestName.isEmpty()) throw new InvalidBookingException("Invalid guest name.");
        this.reservationId = reservationId;
        this.guestName = guestName;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public List<String> getRooms() { return rooms; }
    public void addRoom(String room) { rooms.add(room); }
    public void addService(AddOnService service) { services.add(service); }
    public List<AddOnService> getServices() { return services; }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId + ", Guest: " + guestName +
               ", Rooms: " + rooms + ", Services: " + services;
    }
}

// Thread-safe Booking History with Serialization
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> history = Collections.synchronizedList(new ArrayList<>());
    public void addReservation(Reservation r) { history.add(r); }
    public void removeReservation(String reservationId) { history.removeIf(r -> r.getReservationId().equals(reservationId)); }
    public Reservation getReservation(String reservationId) {
        for (Reservation r : history) { if (r.getReservationId().equals(reservationId)) return r; }
        return null;
    }
    public List<Reservation> getAllReservations() { return history; }
}

// Thread-safe Room Inventory with Serialization
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory = new HashMap<>();
    public RoomInventory() { inventory.put("Single", 5); inventory.put("Double", 5); inventory.put("Suite", 2); }
    public synchronized boolean allocateRoom(String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) return false;
        inventory.put(roomType, inventory.get(roomType) - 1);
        return true;
    }
    public synchronized void releaseRoom(String roomType) { inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1); }
    public synchronized Map<String, Integer> getInventorySnapshot() { return new HashMap<>(inventory); }
}

// Booking Processor (Runnable) for concurrency
class BookingProcessor implements Runnable {
    private Reservation reservation;
    private RoomInventory inventory;
    private BookingHistory history;

    public BookingProcessor(Reservation reservation, RoomInventory inventory, BookingHistory history) {
        this.reservation = reservation;
        this.inventory = inventory;
        this.history = history;
    }

    @Override
    public void run() {
        synchronized (inventory) { // critical section
            boolean success = true;
            for (String room : reservation.getRooms()) {
                if (!inventory.allocateRoom(room)) { success = false; break; }
            }
            if (success) {
                history.addReservation(reservation);
                System.out.println("Booking confirmed for " + reservation.getGuestName() +
                                   " (ID: " + reservation.getReservationId() + ")");
            } else {
                System.out.println("Booking failed for " + reservation.getGuestName() +
                                   " due to insufficient inventory.");
            }
        }
    }
}

// Persistence Service
class PersistenceService {
    private static final String DATA_FILE = "booking_data.ser";

    public static void saveData(BookingHistory history, RoomInventory inventory) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(history);
            out.writeObject(inventory);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static Object[] loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            BookingHistory history = (BookingHistory) in.readObject();
            RoomInventory inventory = (RoomInventory) in.readObject();
            System.out.println("\nSystem state loaded successfully.");
            return new Object[]{history, inventory};
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return null;
    }
}

// Main Application Class
public class BookMyApp {

    public static void main(String[] args) throws InterruptedException {
        BookingHistory history;
        RoomInventory inventory;

        // Load persisted data if available
        Object[] persisted = PersistenceService.loadData();
        if (persisted != null) {
            history = (BookingHistory) persisted[0];
            inventory = (RoomInventory) persisted[1];
        } else {
            history = new BookingHistory();
            inventory = new RoomInventory();
        }

        Scanner sc = new Scanner(System.in);

        // Sample concurrent bookings
        List<Reservation> guestReservations = new ArrayList<>();
        try {
            Reservation r1 = new Reservation("R001", "Alice"); r1.addRoom("Single"); r1.addRoom("Double"); guestReservations.add(r1);
            Reservation r2 = new Reservation("R002", "Bob"); r2.addRoom("Double"); r2.addRoom("Suite"); guestReservations.add(r2);
            Reservation r3 = new Reservation("R003", "Charlie"); r3.addRoom("Single"); r3.addRoom("Suite"); guestReservations.add(r3);
        } catch (InvalidBookingException e) { System.out.println("Error creating reservation: " + e.getMessage()); }

        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (Reservation r : guestReservations) executor.submit(new BookingProcessor(r, inventory, history));
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Show current state
        System.out.println("\nCurrent Inventory: " + inventory.getInventorySnapshot());
        System.out.println("\nCurrent Booking History:");
        synchronized (history.getAllReservations()) {
            for (Reservation r : history.getAllReservations()) System.out.println(r);
        }

        // Save data before exit
        PersistenceService.saveData(history, inventory);
    }
}
