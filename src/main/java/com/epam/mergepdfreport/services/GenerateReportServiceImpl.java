package com.epam.mergepdfreport.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class GenerateReportServiceImpl implements GenerateReportService {

    private final JasperReportService jasperReportService;
    private final MergeService mergeService;
    private final PdfStamperService pdfStamperService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateReportServiceImpl.class);

    @Autowired
    public GenerateReportServiceImpl(final JasperReportService jasperReportService,
                                     final MergeService mergeService,
                                     final PdfStamperService pdfStamperService) {
        this.jasperReportService = jasperReportService;
        this.mergeService = mergeService;
        this.pdfStamperService = pdfStamperService;
    }

    public void generateReport() {
        LOGGER.info("===START GENERATE REPORT===");
        final ByteArrayOutputStream jasperReportOutput = jasperReportService.generateJasperReport();

        final ByteArrayOutputStream mergedReportOutput = mergeService.mergePdfs(new ByteArrayInputStream(jasperReportOutput.toByteArray()));

        final ByteArrayOutputStream pageNumbersOutput = pdfStamperService.stampPageNumbers(new ByteArrayInputStream(mergedReportOutput.toByteArray()));

        pdfStamperService.stampWatermark(new ByteArrayInputStream(pageNumbersOutput.toByteArray()));
        LOGGER.info("===END GENERATE REPORT===");
    }

}
