package net.adam85w.ddd.boundedcontextcanvas.management.generator.canvas;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import net.adam85w.ddd.boundedcontextcanvas.management.generator.ClientHttpRequestFactory;

import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Component
class GeneratedCanvasConnector {

    private final String url;

    private final RestClient restClient;

    private final ObjectMapper mapper;

    GeneratedCanvasConnector(@Value("${application.integration.generator.canvas.url}") String url,
                             @Value("${application.integration.generator.canvas.read-timeout:2000}") int readTimeout,
                             @Value("${application.integration.generator.canvas.connection-timeout:100}") int connectTimeout,
                             ObjectMapper mapper) {
        this.url = url;
        restClient = RestClient.builder()
                .requestFactory(ClientHttpRequestFactory.create(readTimeout, connectTimeout))
                .build();
        this.mapper = mapper;
    }

    GeneratedCanvas perform(BoundedContext boundedContext, String templateType, String templateName) throws IOException {
        try {
            var responseEntity = restClient.post()
                    .uri(url + "{templateType}?templateName={templateName}", templateType.toLowerCase(), templateName)
                    .body(boundedContext)
                    .retrieve()
                    .toEntity(byte[].class);
            return new GeneratedCanvas(boundedContext.getName(), (responseEntity.getHeaders().getContentType() == null) ? MediaType.ALL_VALUE : responseEntity.getHeaders().getContentType().toString(), responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            throw new ValidationException(mapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class).message());
        }
    }
}
