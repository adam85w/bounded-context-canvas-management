package net.adam85w.ddd.boundedcontextcanvas.model.communication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Message {

    @NotBlank
    private final String name;
    @NotNull
    private final MessageType type;

    public Message(String name, MessageType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public MessageType getType() {
        return type;
    }
}
