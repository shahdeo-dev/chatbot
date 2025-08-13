package com.example.pdfchatbot.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfUtils {
    public static String extractText(InputStream is) throws Exception {
        try (PDDocument doc = PDDocument.load(is)) {
            return new PDFTextStripper().getText(doc);
        }
    }

    public static List<String> chunkText(String text, int size, int overlap) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < text.length(); i += (size - overlap)) {
            chunks.add(text.substring(i, Math.min(text.length(), i + size)));
        }
        return chunks;
    }
}
