package net.adam85w.ddd.boundedcontextcanvas.management.diagram.communication;

import net.adam85w.ddd.boundedcontextcanvas.model.communication.CollaboratorType;

enum ComponentType {

    BOUNDED_CONTEXT("Bounded Context" ,"component", "navajowhite"),
    EXTERNAL_SYSTEM("External System" , "component", "lightgrey"),
    FRONTEND("Frontend", "person", "lightskyblue"),
    USER_INTERACTION("Direct User Interaction", "person", "lightseagreen"),
    OTHER("Other", "component", "lavenderblush");

    private final String type;
    private final String name;
    private final String color;

    ComponentType(String type, String name, String color) {
        this.type = type;
        this.name = name;
        this.color = color;
    }

    public static ComponentType valueOf(CollaboratorType collaboratorType) {
        return switch (collaboratorType) {
            case BOUNDED_CONTEXT -> BOUNDED_CONTEXT;
            case EXTERNAL_SYSTEM -> EXTERNAL_SYSTEM;
            case FRONTEND -> FRONTEND;
            case USER_INTERACTION -> USER_INTERACTION;
            case OTHER -> OTHER;
        };
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }
}
