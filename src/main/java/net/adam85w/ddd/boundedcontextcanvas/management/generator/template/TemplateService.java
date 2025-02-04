package net.adam85w.ddd.boundedcontextcanvas.management.generator.template;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TemplateService {

    private final TemplateConnector connector;

    private final TemplateCache cache;

    TemplateService(TemplateConnector connector, TemplateCache cache) {
        this.connector = connector;
        this.cache = cache;
    }

    public Map<String, List<String>> obtain() {
        return cache.get().orElseGet(() -> {
            synchronized (cache) {
                var contextInfo = connector.perform();
                var templates = new TreeMap<String, List<String>>();
                contextInfo.templates().forEach(template -> addTemplate(templates, template));
                cache.add(templates);
                return templates;
            }
        });
    }

    private void addTemplate(Map<String, List<String>> templates, Template template) {
        for (String type: template.support()) {
            if (templates.containsKey(type)) {
                templates.get(type).add(template.name());
            } else {
                var list = new ArrayList<String>();
                list.add(template.name());
                templates.put(type, list);
            }
        }
        templates.values().forEach(list -> list.sort(String.CASE_INSENSITIVE_ORDER));
    }
}
