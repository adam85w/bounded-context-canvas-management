package net.adam85w.ddd.boundedcontextcanvas.management;

import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class NonEmptyOutboundMessagesValidationRule implements ValidationRule {

    @Override
    public String getMessage() {
        return "You should specify all message names for outbound communications.";
    }

    @Override
    public Set<ValidationRuleType> getTypes() {
        return ValidationRuleType.ALL;
    }

    @Override
    public boolean test(BoundedContext boundedContext) {
        return boundedContext.getOutboundCommunication().stream().anyMatch(CommunicationValidationRuleHelper::isAnyMessageNameEmpty);
    }
}
