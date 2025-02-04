package net.adam85w.ddd.boundedcontextcanvas.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
class BoundedContextConfiguration {

    @Bean
    BoundedContext createBoundedContextExample(ObjectMapper mapper, ResourceLoader loader, @Value("${application.bounded-context.example.file-name}") String fileName) throws IOException {
        return mapper.readValue(loader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX + fileName).getInputStream(), BoundedContext.class);
    }
}
