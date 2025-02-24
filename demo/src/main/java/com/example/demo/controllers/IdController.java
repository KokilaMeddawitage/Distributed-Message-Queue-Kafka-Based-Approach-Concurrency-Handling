package com.example.demo.controllers;

import com.example.demo.consumers.IdConsumer;
import com.example.demo.services.IdService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ids")
public class IdController {
    private final IdService idService;

    public IdController(IdService idService) {
        this.idService = idService;
    }

    @GetMapping("/generate/{airlineDesignator}/{id}")
    public String generateId(@PathVariable String airlineDesignator, @PathVariable String id) {
        String generateStatus = idService.generateId(airlineDesignator, id);
        return "id Generate status: " + generateStatus;
    }

    @GetMapping("/getId/{airlineDesignator}")
//    public List<String> getId() {
    public String getId(@PathVariable String airlineDesignator) {
        return idService.getId(airlineDesignator);
    }
}
