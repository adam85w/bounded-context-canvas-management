package net.adam85w.ddd.boundedcontextcanvas.management.generator.template;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
class TemplateCacheInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final TemplateService service;

    TemplateCacheInitializer(TemplateService service) {
        this.service = service;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        service.obtain();
    }
}
