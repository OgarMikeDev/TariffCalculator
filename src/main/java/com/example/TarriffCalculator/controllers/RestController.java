package com.example.TarriffCalculator.controllers;

import com.example.TarriffCalculator.model.Package;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @PostMapping("/create")
    public void createPackage(@RequestBody Package currentPackage) {
        System.out.println(currentPackage);
    }
}
