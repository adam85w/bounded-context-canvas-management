package net.adam85w.ddd.boundedcontextcanvas.model.communication;

public enum CollaboratorType {
    BOUNDED_CONTEXT("Bounded Context"),
    EXTERNAL_SYSTEM("External System"),
    FRONTEND("Frontend"),
    USER_INTERACTION("Direct User Interaction"),
    OTHER("Other");

    private final String name;

    CollaboratorType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
