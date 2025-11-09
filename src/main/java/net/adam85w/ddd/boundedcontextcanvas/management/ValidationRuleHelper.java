package net.adam85w.ddd.boundedcontextcanvas.management;

import net.adam85w.ddd.boundedcontextcanvas.model.Communication;

final class ValidationRuleHelper {

    public final static int MIN_COLLABORATOR_NAME_LENGTH = 3;

    static boolean isAnyCollaboratorNameEmpty(Communication communication) {
        return communication.getCollaborators().stream().anyMatch(collaborator -> collaborator == null || collaborator.getName().isBlank());
    }

    static boolean isAnyMessageNameEmpty(Communication communication) {
        return communication.getMessages().stream().anyMatch(message -> message == null || message.getName().isBlank());
    }

    public static boolean isAnyCollaboratorNameIncorrectLength(Communication communication) {
        return communication.getCollaborators().stream().anyMatch(collaborator -> collaborator == null || collaborator.getName().length() < MIN_COLLABORATOR_NAME_LENGTH);
    }
}
