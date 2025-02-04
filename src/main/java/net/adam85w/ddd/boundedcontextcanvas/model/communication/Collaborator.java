package net.adam85w.ddd.boundedcontextcanvas.model.communication;

public class Collaborator {

    private final String name;
    private final CollaboratorType type;

    public Collaborator(String name, CollaboratorType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public CollaboratorType getType() {
        return type;
    }
}
