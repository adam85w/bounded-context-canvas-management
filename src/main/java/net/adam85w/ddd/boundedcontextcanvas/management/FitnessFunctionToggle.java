package net.adam85w.ddd.boundedcontextcanvas.management;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@Aspect
class FitnessFunctionToggle {

    private final boolean enabled;

    FitnessFunctionToggle(@Value("${application.fitness-function.enabled}") boolean enabled) {
        this.enabled = enabled;
    }

    @Before("within(@org.springframework.stereotype.Controller *) && args(model,..))")
    public void enrichModel(Model model) {
        model.addAttribute("fitnessFunctionEnabled", enabled);
    }
}
