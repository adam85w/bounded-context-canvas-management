package net.adam85w.ddd.boundedcontextcanvas.management.generator.template;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class TemplateCache {

    private final ISet<Map<String, List<String>>> templates;

    TemplateCache(HazelcastInstance hazelcastInstance) {
        templates = hazelcastInstance.getSet("templates");
    }

    void add(Map<String, List<String>> templates) {
        this.templates.add(templates);
    }

    Optional<Map<String, List<String>>> get() {
        return templates.stream().findFirst();
    }

    @Scheduled(fixedRateString = "${application.integration.generator.template.cache.evict-time:360000}",
            initialDelayString = "${application.integration.generator.template.cache.evict-time:360000}")
    void evict() {
        templates.clear();
    }
}
