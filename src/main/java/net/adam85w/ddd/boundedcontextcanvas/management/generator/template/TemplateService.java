package net.adam85w.ddd.boundedcontextcanvas.management.generator.template;

import net.adam85w.ddd.boundedcontextcanvas.management.TemplateObtainer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class TemplateService implements TemplateObtainer {

    private final TemplateConnector connector;

    private final TemplateCache cache;

    TemplateService(TemplateConnector connector, TemplateCache cache) {
        this.connector = connector;
        this.cache = cache;
    }

    public Map<String, Set<String>> obtain() {
        if (cache.get().isEmpty()) {
            synchronized (cache) {
                if (cache.get().isEmpty()) {
                    var contextInfo = connector.perform();
                    var templates = new TreeMap<String, Set<String>>();
                    contextInfo.templates().forEach(template -> addTemplate(templates, template));
                    cache.add(templates);
                }
            }
        }
        return cache.get().get();
    }

    private void addTemplate(Map<String, Set<String>> templates, Template template) {
        for (String type: template.support()) {
            if (templates.containsKey(type)) {
                templates.get(type).add(template.name());
            } else {
                var names = new TreeSet<String>();
                names.add(template.name());
                templates.put(type, names);
            }
        }
    }
}
