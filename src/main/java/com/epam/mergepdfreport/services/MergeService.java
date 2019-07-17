package com.epam.mergepdfreport.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public interface MergeService {

    ByteArrayOutputStream mergePdfs(final ByteArrayInputStream jasperReport);
}
