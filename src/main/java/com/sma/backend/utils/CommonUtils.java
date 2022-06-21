package com.sma.backend.utils;

import com.sma.backend.exception.EncodingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * encoding exception.
 *
 * @author sophea mak
 * @since 2022
 */
public final class CommonUtils {

    private CommonUtils() {
        // nothing
    }

    /**
     * base64Zip.
     *
     * @param inputStream inputstream
     * @param inputFileName filename
     * @return String with base64 encoding
     */
    public static String base64Zip(InputStream inputStream, String inputFileName) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            ZipEntry zipEntry = new ZipEntry(inputFileName);
            zipOutputStream.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
            return new String(Base64.getEncoder().encode(byteArrayOutputStream.toByteArray()));
        } catch (IOException ex) {
            throw new EncodingException("base64 zip encoding error", ex);
        }
    }
}
