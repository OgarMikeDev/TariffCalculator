package com.example.TarriffCalculator.controllers;

import com.example.TarriffCalculator.model.Package;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @PostMapping("/createPackage")
    public ResponseEntity<List<Package>> createPackage(@RequestBody List<Package> currentPackage) {
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

        System.out.println(currentPackage);
        return new ResponseEntity<>(currentPackage, HttpStatus.OK);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> request) {
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

        System.out.println("packages:\n" + packages);
        System.out.println("currencyName:\n" + currencyName);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }
}
