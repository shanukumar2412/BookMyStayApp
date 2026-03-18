import java.util.*;

// Add-On Service class
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

// Manager class
class AddOnServiceManager {

    // Map<ReservationID, List<Service>>
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service
    public void addService(String reservationId, AddOnService service) {
        serviceMap
            .computeIfAbsent(reservationId, k -> new ArrayList<>())
            .add(service);
    }

    // Get services
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost
    public double getTotalCost(String reservationId) {
        double total = 0;
        for (AddOnService s : getServices(reservationId)) {
            total += s.getCost();
        }
        return total;
    }

    // Display services
    public void display(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("\nSelected Services:");
        for (AddOnService s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: ₹" + getTotalCost(reservationId));
    }
}

// MAIN CLASS (as required)
public class BookMyApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.print("Enter Reservation ID: ");
        String reservationId = sc.nextLine();

        while (true) {
            System.out.println("\nChoose Add-On Service:");
            System.out.println("1. Breakfast (₹500)");
            System.out.println("2. Airport Pickup (₹1000)");
            System.out.println("3. Extra Bed (₹700)");
            System.out.println("4. Finish");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.addService(reservationId, new AddOnService("Breakfast", 500));
                    break;
                case 2:
                    manager.addService(reservationId, new AddOnService("Airport Pickup", 1000));
                    break;
                case 3:
                    manager.addService(reservationId, new AddOnService("Extra Bed", 700));
                    break;
                case 4:
                    System.out.println("\nReservation ID: " + reservationId);
                    manager.display(reservationId);
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
