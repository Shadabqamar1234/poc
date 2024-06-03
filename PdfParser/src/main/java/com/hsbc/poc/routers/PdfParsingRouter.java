package com.hsbc.poc.routers;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.hsbc.poc.handlers.PdfParsingHandler;

@Configuration(proxyBeanMethods = false)
public class PdfParsingRouter {

	@Bean
	RouterFunction<ServerResponse> pdfParsingRoutes(final PdfParsingHandler handler) {

		return RouterFunctions.route(POST("v1/api/parse-pdf"), request -> handler.parsePdf(request));

	}

}
