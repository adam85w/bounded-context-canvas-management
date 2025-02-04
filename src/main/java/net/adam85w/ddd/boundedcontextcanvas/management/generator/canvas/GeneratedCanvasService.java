package net.adam85w.ddd.boundedcontextcanvas.management.generator.canvas;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwarenessService;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
class GeneratedCanvasService {

    private final GeneratedCanvasConnector connector;

    private final GeneratedCanvasCache cache;

    private final BoundedContextAwarenessService canvasService;

    private final ObjectMapper mapper;

    GeneratedCanvasService(GeneratedCanvasConnector connector, GeneratedCanvasCache cache, BoundedContextAwarenessService canvasService, ObjectMapper mapper) {
        this.connector = connector;
        this.cache = cache;
        this.canvasService = canvasService;
        this.mapper = mapper;
    }

    public String generate(String templateType, String templateName, long id) throws Exception {
        return generate(templateType, templateName, mapper.readValue(canvasService.obtain(id).retrieveContext(), BoundedContext.class));
    }

    public String generate(String templateType, String templateName, BoundedContext boundedContext) throws Exception {
        var key = generateKey(templateType, templateName, mapper.writeValueAsString(boundedContext));
        synchronized (cache) {
            if (cache.get(key).isEmpty()) {
                cache.add(key, connector.perform(boundedContext, templateType, templateName));
            }
        }
        return key;
    }

    public GeneratedCanvas obtainGeneratedCanvas(String key) {
        return cache.get(key).orElseThrow(() -> new ValidationException("The canvas could not be found for the specified key."));
    }

    private static String generateKey(String templateType, String templateName, String boundedContext) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest((templateType + templateName + boundedContext).getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().encodeToString(hash);
    }
}
