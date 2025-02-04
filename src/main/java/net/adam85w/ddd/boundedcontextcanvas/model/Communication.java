package net.adam85w.ddd.boundedcontextcanvas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.Collaborator;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.Message;

import java.util.List;

public class Communication {

    @Valid
    @Schema(name = "collaborators", description = """
                    Collaborators are other systems or sub-systems that send messages to this context. They can be other bounded contexts, frontends (web or mobile), or something else.
                    If the Bounded Context owns the user interface (e.g. micro-frontend) then the collaborator type is direct user interaction.""")
    private final List<Collaborator> collaborators;

    @Valid
    @Schema(name = "messages", description = """
                    Messages are the information that one collaborator sends to another. There are three types of conversation that can occur between bounded contexts. A request to do something (a command), a request for some information (a query), or notification that something has happened (an event).
                    The word message is used in the general sense and not tied to any implementation. No message bus or asynchronous workflow is obligatory. A command, for example, could simply be posting data from an HTML form as a HTTP POST command.""")
    private final List<Message> messages;

    public Communication(List<Collaborator> collaborators, List<Message> messages) {
        this.collaborators = collaborators;
        this.messages = messages;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
