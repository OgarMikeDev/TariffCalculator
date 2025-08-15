import com.example.TarriffCalculator.model.*;
import com.example.TarriffCalculator.services.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ServiceSpringTest {

    @InjectMocks
    private Service service;

    @Test
    void calculateHaversineDistance_shouldReturnCorrectDistance() {
        // Arrange
        double moscowLat = 55.7558;
        double moscowLon = 37.6176;
        double spbLat = 59.9343;
        double spbLon = 30.3351;

        // Act
        double distance = service.calculateHaversineDistance(moscowLat, moscowLon, spbLat, spbLon);

        // Assert
        assertThat(distance).isBetween(630.0, 640.0); // Using AssertJ
    }

    @Test
    void createOrder_shouldReturnBadRequestForInvalidPackage() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        List<Map<String, Object>> packages = new ArrayList<>();
        Map<String, Object> pkg = new HashMap<>();
        pkg.put("weight", 200000); // Invalid weight
        pkg.put("length", 100);
        pkg.put("width", 100);
        pkg.put("height", 100);
        packages.add(pkg);
        request.put("packages", packages);
        request.put("currencyCode", "RUB");
        request.put("destination", Map.of("latitude", 55.75, "longitude", 37.62));
        request.put("departure", Map.of("latitude", 59.93, "longitude", 30.33));

        // Act
        ResponseEntity<ResponseCreateOrder> response = null;
        try {
             response = service.createOrder(request);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createOrder_shouldCalculateCorrectPrice() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        List<Map<String, Object>> packages = new ArrayList<>();
        Map<String, Object> pkg = new HashMap<>();
        pkg.put("weight", 5000); // 5000 grams = 500 RUB (0.1 RUB/g)
        pkg.put("length", 50);   // 50x50x50 cm = 0.125 m³ = 1250 RUB
        pkg.put("width", 50);
        pkg.put("height", 50);
        packages.add(pkg);
        request.put("packages", packages);
        request.put("currencyCode", "RUB");
        request.put("destination", Map.of("latitude", 55.75, "longitude", 37.62));
        request.put("departure", Map.of("latitude", 59.93, "longitude", 30.33));

        // Act
        ResponseEntity<ResponseCreateOrder> response = null;
        try {
            response = service.createOrder(request);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPrice()).isEqualTo(1250.0);
    }

    @Test
    void createOrder_shouldApplyDistanceMultiplierForLongDistance() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        List<Map<String, Object>> packages = new ArrayList<>();
        Map<String, Object> pkg = new HashMap<>();
        pkg.put("weight", 10000); // 1000 RUB
        pkg.put("length", 100);   // 1 m³ = 10000 RUB
        pkg.put("width", 100);
        pkg.put("height", 100);
        packages.add(pkg);
        request.put("packages", packages);
        request.put("currencyCode", "RUB");
        // Coordinates with distance > 450 km
        request.put("destination", Map.of("latitude", 55.75, "longitude", 37.62)); // Moscow
        request.put("departure", Map.of("latitude", 43.11, "longitude", 131.87)); // Vladivostok (~6400 km)

        // Act
        ResponseEntity<ResponseCreateOrder> response = null;
        try {
            response = service.createOrder(request);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalPrice()).isGreaterThan(10000);
    }

    @Test
    void createOrder_shouldReturnBadRequestForInvalidCoordinates() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        List<Map<String, Object>> packages = new ArrayList<>();
        Map<String, Object> pkg = new HashMap<>();
        pkg.put("weight", 5000);
        pkg.put("length", 50);
        pkg.put("width", 50);
        pkg.put("height", 50);
        packages.add(pkg);
        request.put("packages", packages);
        request.put("currencyCode", "RUB");
        // Invalid coordinates (latitude out of 45-65 range)
        request.put("destination", Map.of("latitude", 70.0, "longitude", 37.62));
        request.put("departure", Map.of("latitude", 59.93, "longitude", 30.33));

        // Act
        ResponseEntity<ResponseCreateOrder> response = null;
        try {
           response = service.createOrder(request);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createOrder_shouldRoundPriceToTwoDecimals() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        List<Map<String, Object>> packages = new ArrayList<>();
        Map<String, Object> pkg = new HashMap<>();
        pkg.put("weight", 4564); // 456.4 RUB
        pkg.put("length", 345);  // ~0.0476 m³ = 476.0 RUB
        pkg.put("width", 589);
        pkg.put("height", 234);
        packages.add(pkg);
        request.put("packages", packages);
        request.put("currencyCode", "RUB");
        request.put("destination", Map.of("latitude", 55.75, "longitude", 37.62));
        request.put("departure", Map.of("latitude", 59.93, "longitude", 30.33));

        // Act
        ResponseEntity<ResponseCreateOrder> response = null;
        try {
            response = service.createOrder(request);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalPrice()).isEqualTo(476.0);
    }
}