import java.util.*;

// Custom Exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Add-On Service class
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() { return name; }
    public double getCost() { return cost; }

    @Override
    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private List<String> rooms;
    private List<AddOnService> services;

    public Reservation(String reservationId, String guestName) throws InvalidBookingException {
        if (reservationId == null || reservationId.isEmpty())
            throw new InvalidBookingException("Reservation ID cannot be empty.");
        if (guestName == null || guestName.isEmpty())
            throw new InvalidBookingException("Guest name cannot be empty.");

        this.reservationId = reservationId;
        this.guestName = guestName;
        this.rooms = new ArrayList<>();
        this.services = new ArrayList<>();
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public void addRoom(String room) throws InvalidBookingException {
        if (room == null || room.isEmpty()) 
            throw new InvalidBookingException("Room name/type cannot be empty.");
        rooms.add(room);
    }

    public void addService(AddOnService service) { services.add(service); }

    public List<AddOnService> getServices() { return services; }

    public double getTotalServiceCost() {
        double total = 0;
        for (AddOnService s : services) total += s.getCost();
        return total;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
               ", Guest: " + guestName +
               ", Rooms: " + rooms +
               ", Services: " + services +
               ", Add-On Total: ₹" + getTotalServiceCost();
    }
}

// Add-On Service Manager
class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {
        serviceMap.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
    }

    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    public double getTotalCost(String reservationId) {
        double total = 0;
        for (AddOnService s : getServices(reservationId)) total += s.getCost();
        return total;
    }
}

// Booking History
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation reservation) { history.add(reservation); }

    public List<Reservation> getAllReservations() { return history; }
}

// Booking Report Service
class BookingReportService {
    public void generateReport(BookingHistory history) {
        System.out.println("\n=== Booking History Report ===");
        List<Reservation> reservations = history.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        for (Reservation r : reservations) {
            System.out.println(r);
        }
        System.out.println("Total Reservations: " + reservations.size());
    }
}

// Main Application Class
public class BookMyApp {

    // Simulated inventory for validation (room type -> available count)
    private static Map<String, Integer> roomInventory = new HashMap<>();
    static {
        roomInventory.put("Single", 5);
        roomInventory.put("Double", 5);
        roomInventory.put("Suite", 2);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AddOnServiceManager serviceManager = new AddOnServiceManager();
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        try {
            System.out.print("Enter Guest Name: ");
            String guestName = sc.nextLine();

            System.out.print("Enter Reservation ID: ");
            String reservationId = sc.nextLine();

            Reservation reservation = new Reservation(reservationId, guestName);

            // Room selection with validation
            while (true) {
                System.out.print("Add a room (Single/Double/Suite) or 'done': ");
                String room = sc.nextLine();
                if (room.equalsIgnoreCase("done")) break;

                if (!roomInventory.containsKey(room)) {
                    System.out.println("Invalid room type! Choose Single, Double, or Suite.");
                    continue;
                }

                if (roomInventory.get(room) <= 0) {
                    System.out.println("Sorry, no " + room + " rooms available.");
                    continue;
                }

                reservation.addRoom(room);
                roomInventory.put(room, roomInventory.get(room) - 1); // decrease inventory
            }

            // Add-On Services
            while (true) {
                System.out.println("\nSelect Add-On Service:");
                System.out.println("1. Breakfast (₹500)");
                System.out.println("2. Airport Pickup (₹1000)");
                System.out.println("3. Extra Bed (₹700)");
                System.out.println("4. Done");

                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        AddOnService breakfast = new AddOnService("Breakfast", 500);
                        reservation.addService(breakfast);
                        serviceManager.addService(reservationId, breakfast);
                        break;
                    case 2:
                        AddOnService pickup = new AddOnService("Airport Pickup", 1000);
                        reservation.addService(pickup);
                        serviceManager.addService(reservationId, pickup);
                        break;
                    case 3:
                        AddOnService bed = new AddOnService("Extra Bed", 700);
                        reservation.addService(bed);
                        serviceManager.addService(reservationId, bed);
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invalid choice! Try again.");
                        continue;
                }
                if (choice == 4) break;
            }

            // Confirm booking and add to history
            bookingHistory.addReservation(reservation);
            System.out.println("\nReservation Confirmed: " + reservationId);

            // Generate booking report
            reportService.generateReport(bookingHistory);

        } catch (InvalidBookingException e) {
            System.out.println("Booking Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
