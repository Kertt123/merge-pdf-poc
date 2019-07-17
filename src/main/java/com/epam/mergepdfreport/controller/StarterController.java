package com.epam.mergepdfreport.controller;

import com.epam.mergepdfreport.services.GenerateReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StarterController {

    private final GenerateReportService generateReportService;

    @Autowired
    public StarterController(final GenerateReportService generateReportService) {
        this.generateReportService = generateReportService;
    }

    @RequestMapping("/start")
    public void starter() {
        generateReportService.generateReport();
    }
}
