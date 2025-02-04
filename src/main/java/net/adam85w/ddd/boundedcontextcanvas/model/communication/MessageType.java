package net.adam85w.ddd.boundedcontextcanvas.model.communication;

public enum MessageType {
    QUERY("Query"),
    COMMAND("Command"),
    EVENT("Event");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
