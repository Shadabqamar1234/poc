package com.hsbc.poc.handlers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Component
public class FilePartToMapConverter {

	public Mono<Map<String, String>> convertToMap(Flux<Part> fileParts) {
		
		return fileParts.cast(FilePart.class)
				.flatMap(filePart -> DataBufferUtils.join(filePart.content()).map(dataBuffer -> {
					return covertToBytes(dataBuffer);
				}).map(bytes -> {
					return getExtractedFileDetails(filePart, bytes);
				})).collectMap(Tuple2::getT1, Tuple2::getT2);
	}

	private Tuple2<String, String> getExtractedFileDetails(FilePart filePart, byte[] bytes) {
		String parsedText = parsePdf(new ByteArrayInputStream(bytes));
		return Tuples.of(filePart.filename(), parsedText);
	}

	private byte[] covertToBytes(DataBuffer dataBuffer) {
		byte[] bytes = new byte[dataBuffer.readableByteCount()];
		dataBuffer.read(bytes);
		DataBufferUtils.release(dataBuffer);
		return bytes;
	}

	private String parsePdf(InputStream pdfInputStream) {
		try (PDDocument document = PDDocument.load(pdfInputStream)) {
			PDFTextStripper textStripper = new PDFTextStripper();
			return textStripper.getText(document);
		} catch (IOException e) {
			throw new RuntimeException("Error parsing PDF document", e);
		}
	}

}
