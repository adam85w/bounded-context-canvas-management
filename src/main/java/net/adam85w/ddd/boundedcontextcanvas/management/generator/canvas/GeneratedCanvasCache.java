package net.adam85w.ddd.boundedcontextcanvas.management.generator.canvas;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
class GeneratedCanvasCache {

    private final IMap<String, GeneratedCanvas> canvases;

    private final int evictTime;

    GeneratedCanvasCache(HazelcastInstance hazelcastInstance, @Value("${application.integration.generator.canvas.cache.evict-time:10000}") int evictTime) {
        canvases = hazelcastInstance.getMap("canvases");
        this.evictTime = evictTime;
    }

    void add(String key, GeneratedCanvas canvas) {
        canvases.put(key, canvas,evictTime, TimeUnit.MILLISECONDS);
    }

    Optional<GeneratedCanvas> get(String key) {
        var canvas = canvases.get(key);
        if (canvas == null) {
            return Optional.empty();
        }
        return Optional.of(canvas);
    }
}
