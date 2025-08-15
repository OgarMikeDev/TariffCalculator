package com.example.TarriffCalculator.controllers;

import com.example.TarriffCalculator.model.Package;
import com.example.TarriffCalculator.model.ResponseCreateOrder;
import com.example.TarriffCalculator.services.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final Service service;

    public RestController(Service service) {
        this.service = service;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<ResponseCreateOrder> createOrder(@RequestBody Map<String, Object> request) throws Exception {
        return service.createOrder(request);
    }
}
