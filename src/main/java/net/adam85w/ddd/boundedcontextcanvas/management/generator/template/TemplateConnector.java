package net.adam85w.ddd.boundedcontextcanvas.management.generator.template;

import net.adam85w.ddd.boundedcontextcanvas.management.generator.ClientHttpRequestFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
class TemplateConnector {

    private final String url;

    private final RestClient restClient;

    TemplateConnector(@Value("${application.integration.generator.template.url}") String url,
                      @Value("${application.integration.generator.template.read-timeout:200}") int readTimeout,
                      @Value("${application.integration.generator.template.connection-timeout:100}") int connectTimeout) {
        this.url = url;
        restClient = RestClient.builder()
                .requestFactory(ClientHttpRequestFactory.create(readTimeout, connectTimeout))
                .build();
    }

    ContextInfo perform() {
        return restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(ContextInfo.class).getBody();
    }
}
