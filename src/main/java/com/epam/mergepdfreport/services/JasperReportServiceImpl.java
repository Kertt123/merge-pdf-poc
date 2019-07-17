package com.epam.mergepdfreport.services;

import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
class JasperReportServiceImpl implements JasperReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportServiceImpl.class);

    public ByteArrayOutputStream generateJasperReport() {
        try {
            final JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/report.jrxml");
            final JRDataSource jrDataSource = new JREmptyDataSource();
            final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, jrDataSource);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return outputStream;
        } catch (final JRException e) {
            LOGGER.error("Error during generate pdf from jasper report", e);
        }
        return new ByteArrayOutputStream();
    }

}
