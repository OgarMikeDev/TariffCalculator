package com.example.TarriffCalculator.controllers;

import com.example.TarriffCalculator.model.Package;
import com.example.TarriffCalculator.model.ResponseCreateOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    ObjectMapper objectMapper = new ObjectMapper();
    @PostMapping("/createPackages")
    public ResponseEntity<List<Package>> createPackages(@RequestBody List<Package> currentPackages) throws Exception {
        /*
        TODO Запрос json:
            [
                {
                  "weight": 5000,
                  "length": 345,
                  "width": 589,
                  "height": 234
                },
                {
                  "weight": 3000,
                  "length": 200,
                  "width": 400,
                  "height": 100
                }
            ]
         */

        System.out.println("Упаковки:" + currentPackages);
        objectMapper.writeValue(new File("src/main/resources/packages.json"), currentPackages);
        return new ResponseEntity<>(currentPackages, HttpStatus.OK);
    }

    @PostMapping("/createOrderSample")
    public ResponseEntity<Map<String, Object>> createOrderSample(@RequestBody Map<String, Object> request) throws Exception {
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
                {
                    "weight": 3000,
                    "length": 200,
                    "width": 400,
                    "height": 100
                }
              ],
              "currencyName": "RUB"
            }
         */

        List<Package> packages = (List<Package>) request.get("packages");
        String currencyName = (String) request.get("currencyName");

        System.out.println("Упаковки:\n" + packages);
        System.out.println("Валюта:\n" + currencyName);
        objectMapper.writeValue(new File("src/main/resources/order.json"), request);

        return new ResponseEntity<>(request, HttpStatus.OK);
    }
    @PostMapping("/createOrder")
    public ResponseEntity<ResponseCreateOrder> createOrder(@RequestBody Map<String, Object> request) throws Exception {
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
        List<Package> packages = Package.fromListMapToListPackage(mapPackages);

        String currencyName = (String) request.get("currencyName");

        System.out.println("Упаковки:\n" + packages);
        System.out.println("Валюта:\n" + currencyName);
        objectMapper.writeValue(new File("src/main/resources/order.json"), request);

        double fullnessWeightPackages = 0;
        for (Package currentPackage : packages) {
            fullnessWeightPackages += currentPackage.getWeight();
        }
        if (fullnessWeightPackages < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        double finalPriceOrder = ResponseCreateOrder.priceOneKg * (fullnessWeightPackages / 1000);
        ResponseCreateOrder currentResponseCreateOrder = new ResponseCreateOrder(
                finalPriceOrder, 500.00, currencyName);

        return new ResponseEntity<>(currentResponseCreateOrder, HttpStatus.OK);
    }
}
