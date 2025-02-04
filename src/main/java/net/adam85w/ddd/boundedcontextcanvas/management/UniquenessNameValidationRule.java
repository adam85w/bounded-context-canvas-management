package net.adam85w.ddd.boundedcontextcanvas.management;

import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class UniquenessNameValidationRule implements ValidationRule {

    private final static Set<ValidationRuleType> TYPES = Set.of(ValidationRuleType.CREATE);

    private final CanvasRepository repository;

    UniquenessNameValidationRule(CanvasRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getMessage() {
        return "The name of a bounded context should be unique.";
    }

    @Override
    public Set<ValidationRuleType> getTypes() {
        return TYPES;
    }

    @Override
    public boolean test(BoundedContext boundedContext) {
        return repository.existsByName(boundedContext.getName());
    }
}
