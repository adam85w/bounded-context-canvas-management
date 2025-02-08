package net.adam85w.ddd.boundedcontextcanvas.management.diagram.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAware;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwareService;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import net.adam85w.ddd.boundedcontextcanvas.model.Communication;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.Collaborator;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.CollaboratorType;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.Message;

import java.util.HashSet;
import java.util.Set;

@org.springframework.stereotype.Component
class CommunicationDiagramGenerator {

    private final BoundedContextAwareService service;

    private final ObjectMapper mapper;

    CommunicationDiagramGenerator(BoundedContextAwareService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    String generate() throws JsonProcessingException {
        Set<Relationship> relationships = new HashSet<>();
        Set<Component> components = new HashSet<>();
        for (BoundedContextAware boundedContextAware : service.obtainAll()) {
            BoundedContext boundedContext = mapper.readValue(boundedContextAware.retrieveContext(), BoundedContext.class);
            components.add(new Component(boundedContext.getName(), ComponentType.valueOf(CollaboratorType.BOUNDED_CONTEXT)));
            for (Communication communication : boundedContext.getInboundCommunication()) {
                for (Collaborator collaborator : communication.getCollaborators()) {
                    components.add(new Component(collaborator.getName(), ComponentType.valueOf(collaborator.getType())));
                    for (Message message : communication.getMessages()) {
                        relationships.add(new Relationship(new Component(boundedContext.getName(), ComponentType.valueOf(CollaboratorType.BOUNDED_CONTEXT)),
                                new Component(collaborator.getName(), ComponentType.valueOf(collaborator.getType())),
                                new Relation(message.getName(), RelationType.valueOf(message.getType()))));
                    }
                }
            }
            for (Communication communication : boundedContext.getOutboundCommunication()) {
                for (Collaborator collaborator : communication.getCollaborators()) {
                    if (!(collaborator.getName() == null || collaborator.getName().isBlank())) {
                        components.add(new Component(collaborator.getName(), ComponentType.valueOf(collaborator.getType())));
                    }
                    for (Message message : communication.getMessages()) {
                        relationships.add(new Relationship(new Component(collaborator.getName(), ComponentType.valueOf(collaborator.getType())),
                                new Component(boundedContext.getName(), ComponentType.valueOf(CollaboratorType.BOUNDED_CONTEXT)),
                                new Relation(message.getName(), RelationType.valueOf(message.getType()))));
                    }
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("@startuml\n");
        builder.append("\n\n");
        builder.append("!theme plain\n");
        builder.append("skinparam BackgroundColor transparent\n");
        builder.append("skinparam componentStyle rectangle)\n");
        builder.append("\n\n");
        builder.append("legend\n");
        builder.append("|= |= Type |\n");
        for (ComponentType componentType : ComponentType.values()) {
            builder.append("|<back:" + componentType.getColor() + "> </back> | " + componentType.getType() +  "   |\n");
        }
        builder.append("endlegend\n");
        builder.append("\n\n");
        for (Component component : components) {
            builder.append(component.type().getName() + " \"" + component.name() + "\" as " + format(component.name()) + " #" + component.type().getColor() + "\n");
        }
        builder.append("\n\n");
        for (Relationship relationship : relationships) {
            builder.append(format(relationship.componentA().name()) + " " + relationship.relation().type().getStyle() + " " + format(relationship.componentB().name()) + " : " + relationship.relation().name() + "\n");
        }
        builder.append("\n\n");
        builder.append("@enduml");
        return builder.toString();
    }

    private String format(String text) {
        return text.replaceAll(" ", "_");
    }
}
