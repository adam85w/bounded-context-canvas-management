package net.adam85w.ddd.boundedcontextcanvas.management;

import net.adam85w.ddd.boundedcontextcanvas.model.Communication;

final class CommunicationValidationRuleHelper {

    static boolean isAnyCollaboratorNameEmpty(Communication communication) {
        return communication.getCollaborators().stream().anyMatch(collaborator -> collaborator == null || collaborator.getName().isBlank());
    }

    static boolean isAnyMessageNameEmpty(Communication communication) {
        return communication.getMessages().stream().anyMatch(message -> message == null || message.getName().isBlank());
    }
}
