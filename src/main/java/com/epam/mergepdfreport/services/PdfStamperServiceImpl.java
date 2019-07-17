package com.epam.mergepdfreport.services;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;

@Service
class PdfStamperServiceImpl implements PdfStamperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfStamperServiceImpl.class);

    public ByteArrayOutputStream stampPageNumbers(final ByteArrayInputStream byteArrayInputStream) {
        try {
            final PdfReader pdfReader = new PdfReader(byteArrayInputStream);
            final int totalPagesNumbers = pdfReader.getNumberOfPages();
            final ByteArrayOutputStream stampedPdfOutput = new ByteArrayOutputStream();
            final PdfStamper stamper = new PdfStamper(pdfReader, stampedPdfOutput);
            PdfContentByte over;
            Rectangle pageSize;
            for (int i = 1; i <= totalPagesNumbers; i++) {
                pageSize = pdfReader.getPageSizeWithRotation(i);
                over = stamper.getOverContent(i);
                maskFooterByRectangle(over, pageSize);
                addPageNumber(over, pageSize, getPageNumberText(i, totalPagesNumbers));
            }
            stamper.close();
            pdfReader.close();
            return stampedPdfOutput;
        } catch (DocumentException | IOException e) {
            LOGGER.error("Problem with stamp page numbers to document", e);
        }
        return new ByteArrayOutputStream();
    }

    public void stampWatermark(final ByteArrayInputStream byteArrayInputStream) {
        try {
            final PdfReader reader = new PdfReader(byteArrayInputStream);
            final int totalPagesNumbers = reader.getNumberOfPages();
            final PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("mergedWatermark.pdf"));
            final PdfGState gs1 = new PdfGState();
            gs1.setFillOpacity(0.1f);
            PdfContentByte over;
            for (int i = 1; i <= totalPagesNumbers; i++) {
                over = stamper.getOverContent(i);
                over.saveState();
                over.setGState(gs1);
                addWatermarkImage(over, reader.getPageSizeWithRotation(i));
                over.restoreState();
            }
            stamper.setFormFlattening(true);
            stamper.close();
            reader.close();
        } catch (IOException | DocumentException e) {
            LOGGER.error("Problem with stamp watermark to document", e);
        }

    }

    private void maskFooterByRectangle(final PdfContentByte over, final Rectangle pageSize) {
        Rectangle rect = new Rectangle(pageSize.getWidth(), pageSize.getBottom() + 30 + 30);
        rect.setBackgroundColor(Color.white);
        over.rectangle(rect);
    }

    private String getPageNumberText(final int i, final int totalPagesNumbers) {
        return String.format("Page %d of %d", i, totalPagesNumbers);
    }

    private void addPageNumber(final PdfContentByte over, final Rectangle pageSize, final String pageNumberText) {
        final float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
        final float y = pageSize.getBottom() + 30;
        ColumnText.showTextAligned(over, Element.ALIGN_CENTER,
                new Phrase(pageNumberText), x, y, 0);
    }

    private void addWatermarkImage(final PdfContentByte over, final Rectangle pageSize) throws IOException, DocumentException {
        final Image waterMarkImage = Image.getInstance("src/main/resources/water.jpg");
        final float imageWidth = waterMarkImage.getScaledWidth();
        final float imageHeight = waterMarkImage.getScaledHeight();
        final float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
        final float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
        over.addImage(waterMarkImage, imageWidth, 0, 0, imageHeight, x - (imageWidth / 2), y - (imageHeight / 2));
    }
}
