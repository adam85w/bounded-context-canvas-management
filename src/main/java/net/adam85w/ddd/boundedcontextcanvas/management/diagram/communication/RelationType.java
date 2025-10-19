package net.adam85w.ddd.boundedcontextcanvas.management.diagram.communication;

import net.adam85w.ddd.boundedcontextcanvas.model.communication.MessageType;

enum RelationType {

    QUERY("Query", "-->"),
    COMMAND("Command", "-[dotted]->"),
    EVENT("Event", "-[dashed]->");

    private final String name;
    private final String arrow;

    RelationType(String name, String arrow) {
        this.name = name;
        this.arrow = arrow;
    }

    public static RelationType valueOf(MessageType messageType) {
        return switch (messageType) {
            case EVENT -> EVENT;
            case QUERY -> QUERY;
            case COMMAND -> COMMAND;
        };
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return arrow;
    }
}
