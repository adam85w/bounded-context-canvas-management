package net.adam85w.ddd.boundedcontextcanvas.management;

import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class NonEmptyInboundMessagesValidationRule implements ValidationRule {

    @Override
    public String getMessage() {
        return "You should specify all message names for inbound communications.";
    }

    @Override
    public Set<ValidationRuleType> getTypes() {
        return ValidationRuleType.ALL;
    }

    @Override
    public boolean test(BoundedContext boundedContext) {
        return boundedContext.getInboundCommunication().stream().anyMatch(CommunicationValidationRuleHelper::isAnyMessageNameEmpty);
    }
}
