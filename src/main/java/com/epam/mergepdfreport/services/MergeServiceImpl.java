package com.epam.mergepdfreport.services;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BadPdfFormatException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
class MergeServiceImpl implements MergeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MergeServiceImpl.class);

    public ByteArrayOutputStream mergePdfs(final ByteArrayInputStream jasperReport) {
        try {
            final Document pdfDocument = new Document();
            final ByteArrayOutputStream mergedOutputStream = new ByteArrayOutputStream();
            final InputStream inputTestPdf = new FileInputStream("src/main/resources/test.pdf");
            final InputStream inputExamplePdf = new FileInputStream("src/main/resources/example.pdf");
            final PdfCopy copy = new PdfCopy(pdfDocument, mergedOutputStream);

            pdfDocument.open();

            final PdfReader testPdfReader = new PdfReader(inputTestPdf);
            final PdfReader examplePdfReader = new PdfReader(inputExamplePdf);
            final PdfReader jasperPdfReader = new PdfReader(jasperReport);

            addPagesFromReader(copy, jasperPdfReader);
            addPagesFromReader(copy, testPdfReader);
            addPagesFromReader(copy, examplePdfReader);

            pdfDocument.close();

            return mergedOutputStream;
        } catch (final IOException | DocumentException e) {
            LOGGER.error("Error with merge pdf documents ", e);
        }
        return new ByteArrayOutputStream();
    }

    private void addPagesFromReader(final PdfCopy copy, final PdfReader jasperPdfReader) throws IOException, BadPdfFormatException {
        for (int i = 1; i <= jasperPdfReader.getNumberOfPages(); i++) {
            copy.addPage(copy.getImportedPage(jasperPdfReader, i));
        }
    }
}
