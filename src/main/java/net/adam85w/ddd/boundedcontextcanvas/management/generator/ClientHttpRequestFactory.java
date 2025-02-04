package net.adam85w.ddd.boundedcontextcanvas.management.generator;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public final class ClientHttpRequestFactory {

    public static org.springframework.http.client.ClientHttpRequestFactory create(int readTimeout, int connectTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);
        return factory;
    }
}
