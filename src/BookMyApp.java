import java.util.*;

public class BookMyApp {

    // Add-On Service class
    static class Service {
        private String serviceName;
        private double price;

        public Service(String serviceName, double price) {
            this.serviceName = serviceName;
            this.price = price;
        }

        public String getServiceName() {
            return serviceName;
        }

        public double getPrice() {
            return price;
        }

        public void displayService() {
            System.out.println(serviceName + " - $" + price);
        }
    }

    // Manager class to map reservation to services
    static class AddOnServiceManager {

        private Map<String, List<Service>> reservationServices = new HashMap<>();

        public void addService(String reservationId, Service service) {

            reservationServices
                    .computeIfAbsent(reservationId, k -> new ArrayList<>())
                    .add(service);
        }

        public void displayServices(String reservationId) {

            List<Service> services = reservationServices.get(reservationId);

            if (services == null || services.isEmpty()) {
                System.out.println("No services selected.");
                return;
            }

            System.out.println("Selected Add-On Services:");

            for (Service s : services) {
                s.displayService();
            }
        }

        public double calculateTotalServiceCost(String reservationId) {

            double total = 0;

            List<Service> services = reservationServices.get(reservationId);

            if (services != null) {
                for (Service s : services) {
                    total += s.getPrice();
                }
            }

            return total;
        }
    }

    // Use Case 7 main execution
    static class UseCase7AddOnServiceSelection {

        public static void main(String[] args) {

            System.out.println("===== Book My Stay - Add-On Service Selection =====");

            String reservationId = "RES101";

            AddOnServiceManager manager = new AddOnServiceManager();

            // Guest selects services
            Service breakfast = new Service("Breakfast", 20);
            Service airportPickup = new Service("Airport Pickup", 50);
            Service spa = new Service("Spa Access", 40);

            manager.addService(reservationId, breakfast);
            manager.addService(reservationId, airportPickup);
            manager.addService(reservationId, spa);

            // Display services
            manager.displayServices(reservationId);

            // Calculate cost
            double total = manager.calculateTotalServiceCost(reservationId);

            System.out.println("--------------------------------");
            System.out.println("Total Additional Cost: $" + total);

            System.out.println("Core booking and inventory remain unchanged.");
        }
    }
}