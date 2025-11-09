package net.adam85w.ddd.boundedcontextcanvas.management;

import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class NonEmptyOutboundCollaboratorsValidationRule implements ValidationRule {

    @Override
    public String getMessage() {
        return String.format("You should specify all collaborator names with a minimum length of %s characters for outbound communications.",
                ValidationRuleHelper.MIN_COLLABORATOR_NAME_LENGTH);
    }

    @Override
    public Set<ValidationRuleType> getTypes() {
        return ValidationRuleType.ALL;
    }

    @Override
    public boolean test(BoundedContext boundedContext) {
        return boundedContext.getOutboundCommunication().stream().anyMatch(ValidationRuleHelper::isAnyCollaboratorNameEmpty) ||
                boundedContext.getOutboundCommunication().stream().anyMatch(ValidationRuleHelper::isAnyCollaboratorNameIncorrectLength);
    }
}
