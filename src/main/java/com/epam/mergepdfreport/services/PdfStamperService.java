package com.epam.mergepdfreport.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public interface PdfStamperService {

    ByteArrayOutputStream stampPageNumbers(final ByteArrayInputStream byteArrayInputStream);

    void stampWatermark(final ByteArrayInputStream byteArrayInputStream);
}
