package com.epam.mergepdfreport.services;

import java.io.ByteArrayOutputStream;

public interface JasperReportService {

    ByteArrayOutputStream generateJasperReport();
}
