package com.hsbc.poc.handlers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PdfParsingHandler {

	@Autowired
	private FilePartToMapConverter filePartToMapConverter;

	
	public Mono<ServerResponse> parsePdf(ServerRequest request) {

		Flux<Part> fileParts = request.body(BodyExtractors.toParts());
		return filePartToMapConverter.convertToMap(fileParts).flatMap(result -> okJson(ResponseEntity.ok(result)))
				.switchIfEmpty(badRequestJson());

	}

	private Mono<ServerResponse> okJson(ResponseEntity<Map<String, String>> responseEntity) {
		return ServerResponse.ok().contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				.body(org.springframework.web.reactive.function.BodyInserters.fromValue(responseEntity));
	}

	private Mono<ServerResponse> badRequestJson() {
		return ServerResponse.badRequest().contentType(org.springframework.http.MediaType.APPLICATION_JSON).build();
	}

}
