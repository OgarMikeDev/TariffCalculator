package com.example.TarriffCalculator.services;

import com.example.TarriffCalculator.model.Departure;
import com.example.TarriffCalculator.model.Destination;
import com.example.TarriffCalculator.model.Package;
import com.example.TarriffCalculator.model.ResponseCreateOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {
    ObjectMapper objectMapper = new ObjectMapper();
//    @PostMapping("/createPackages")
//    public ResponseEntity<List<com.example.TarriffCalculator.model.Package>> createPackages(@RequestBody List<com.example.TarriffCalculator.model.Package> currentPackages) throws Exception {
//        /*
//        TODO Запрос json:
//            [
//                {
//                  "weight": 5000,
//                  "length": 345,
//                  "width": 589,
//                  "height": 234
//                },
//                {
//                  "weight": 3000,
//                  "length": 200,
//                  "width": 400,
//                  "height": 100
//                }
//            ]
//         */
//
//        System.out.println("Упаковки:" + currentPackages);
//        objectMapper.writeValue(new File("src/main/resources/packages.json"), currentPackages);
//        return new ResponseEntity<>(currentPackages, HttpStatus.OK);
//    }
//
//    @PostMapping("/createOrderSample")
//    public ResponseEntity<Map<String, Object>> createOrderSample(@RequestBody Map<String, Object> request) throws Exception {
//        /*
//        TODO Запрос json:
//            {
//              "packages":[
//                {
//                  "weight": 4564,
//                  "length": 345,
//                  "width": 589,
//                  "height": 234
//                },
//                {
//                    "weight": 3000,
//                    "length": 200,
//                    "width": 400,
//                    "height": 100
//                }
//              ],
//              "currencyName": "RUB"
//            }
//         */
//
//        List<com.example.TarriffCalculator.model.Package> packages = (List<com.example.TarriffCalculator.model.Package>) request.get("packages");
//        String currencyName = (String) request.get("currencyName");
//
//        System.out.println("Упаковки:\n" + packages);
//        System.out.println("Валюта:\n" + currencyName);
//        objectMapper.writeValue(new File("src/main/resources/order.json"), request);
//
//        return new ResponseEntity<>(request, HttpStatus.OK);
//    }

    public static double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Радиус Земли в км

        // Переводим градусы в радианы
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Разница координат
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Формула гаверсинусов
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance;
    }

    public ResponseEntity<ResponseCreateOrder> createOrder(Map<String, Object> request) throws Exception {
        /*
        TODO Запрос json:
            {
              "packages":[
                {
                  "weight": 4564,
                  "length": 345,
                  "width": 589,
                  "height": 234
                },
              "currencyName": "RUB"
            }

            TODO Ответ json:
                {
                "totalPrice": 1643.69,
                "minimalPrice": 500.00,
                "currencyCode": "RUB"
                }
         */

        List<Map<String, Object>> mapPackages = (List<Map<String, Object>>) request.get("packages");
        List<com.example.TarriffCalculator.model.Package> packages = com.example.TarriffCalculator.model.Package.fromListMapToListPackage(mapPackages);

        String currencyName = (String) request.get("currencyCode");

        Map<String, Object> objectMap1 = (Map<String, Object>) request.get("destination");
        Destination destination = Destination.fromMapObjectToDestination(objectMap1);

        Map<String, Object> objectMap2 = (Map<String, Object>) request.get("departure");
        Departure departure = Departure.fromMapObjectToDeparture(objectMap2);

        System.out.println("Упаковки:\n" + packages);
        System.out.println("Валюта:\n" + currencyName);
        System.out.println("Координаты точки отправления:\n" + departure);
        System.out.println("Координаты точки прибытия:\n" + destination);
        objectMapper.writeValue(new File("src/main/resources/order.json"), request);

        double finalPriceOrder = 0;
        for (com.example.TarriffCalculator.model.Package currentPackage : packages) {
            if (currentPackage.getWeight() > 150_000 ||
                    (currentPackage.getWidth() < 0 || currentPackage.getWidth() > 1500) ||
                    (currentPackage.getHeight() < 0 || currentPackage.getHeight() > 1500) ||
                    (currentPackage.getLength() < 0 || currentPackage.getLength() > 1500)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            double roundedWidth = (int) (Math.round(currentPackage.getWidth() / 50.0) * 50);
            double roundedHeight = (int) (Math.round(currentPackage.getHeight() / 50.0) * 50);
            double roundedLength = (int) (Math.round(currentPackage.getLength() / 50.0) * 50);
            System.out.println("ширина: " + roundedWidth);
            System.out.println("высота: " + roundedHeight);
            System.out.println("длина: " + roundedLength);

            double pricePackageWeight = currentPackage.getWeight() * com.example.TarriffCalculator.model.Package.priceOneGram;
            double pricePackageDimensions =
                    ((1.0 * roundedHeight * roundedLength * roundedWidth)
                            / 1_000_000_000) * Package.priceOneMetre;
            pricePackageWeight = Math.round(pricePackageWeight * 10_000) / 10_000.0;
            pricePackageDimensions = Math.round(pricePackageDimensions * 10_000) / 10_000.0;
            System.out.println("Цена упаковки по весу: " + pricePackageWeight);
            System.out.println("Цена упаковки по габаритам: " + pricePackageDimensions);
            finalPriceOrder += Math.max(pricePackageWeight, pricePackageDimensions);
        }
        if (finalPriceOrder < 0 ||
                packages.size() == 0 ||
                currencyName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        double distance = calculateHaversineDistance(
                departure.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
        if (distance > 450) {

        }
        ResponseCreateOrder currentResponseCreateOrder = new ResponseCreateOrder(
                finalPriceOrder, 500.00, currencyName);

        return new ResponseEntity<>(currentResponseCreateOrder, HttpStatus.OK);
    }
}
