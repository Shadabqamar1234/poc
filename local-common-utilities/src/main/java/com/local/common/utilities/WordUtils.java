package com.local.common.utilities;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public final class WordUtils {

	public static String extractTextFromWordDocument(String filePath) throws IOException {

		byte[] documentBytes = Files.readAllBytes(new File(filePath).toPath());
		StringBuilder textBuilder = new StringBuilder();

		try (ByteArrayInputStream bis = new ByteArrayInputStream(documentBytes);
				XWPFDocument document = new XWPFDocument(bis)) {
			for (XWPFParagraph paragraph : document.getParagraphs()) {
				textBuilder.append(paragraph.getText());
				textBuilder.append("\n"); // Add newline between paragraphs
			}
		}
		return textBuilder.toString();
	}

}
