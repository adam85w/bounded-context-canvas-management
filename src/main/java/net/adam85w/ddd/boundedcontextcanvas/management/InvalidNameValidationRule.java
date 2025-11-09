package net.adam85w.ddd.boundedcontextcanvas.management;

import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class InvalidNameValidationRule implements ValidationRule {

    @Override
    public String getMessage() {
        return String.format("The name of a bounded context should not be empty and must have at least %s characters.",
                ValidationRuleHelper.MIN_COLLABORATOR_NAME_LENGTH);
    }

    @Override
    public Set<ValidationRuleType> getTypes() {
        return ValidationRuleType.ALL;
    }

    @Override
    public boolean test(BoundedContext boundedContext) {
        return boundedContext.getName().isBlank() || boundedContext.getName().length() < ValidationRuleHelper.MIN_COLLABORATOR_NAME_LENGTH;
    }
}
