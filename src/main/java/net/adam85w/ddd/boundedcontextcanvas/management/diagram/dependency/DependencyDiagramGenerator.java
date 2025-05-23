package net.adam85w.ddd.boundedcontextcanvas.management.diagram.dependency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAware;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwareService;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import net.adam85w.ddd.boundedcontextcanvas.model.Communication;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.Collaborator;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.CollaboratorType;

import java.util.HashSet;
import java.util.Set;

@org.springframework.stereotype.Component
class DependencyDiagramGenerator {

    private final BoundedContextAwareService service;

    private final ObjectMapper mapper;

    DependencyDiagramGenerator(BoundedContextAwareService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    String generate(Set<Long> ids) throws JsonProcessingException {
        Set<Dependency> relationships = new HashSet<>();
        Set<Component> components = new HashSet<>();
        for (BoundedContextAware boundedContextAware : obtainBoundedContexts(ids)) {
            BoundedContext boundedContext = mapper.readValue(boundedContextAware.retrieveContext(), BoundedContext.class);
            components.add(new Component(boundedContext.getName(), ComponentType.valueOf(CollaboratorType.BOUNDED_CONTEXT)));
            for (Communication communication : boundedContext.getInboundCommunication()) {
                for (Collaborator collaborator : communication.getCollaborators()) {
                    components.add(new Component(collaborator.getName(), ComponentType.valueOf(collaborator.getType())));

                    relationships.add(new Dependency(new Component(boundedContext.getName(), ComponentType.valueOf(CollaboratorType.BOUNDED_CONTEXT)),
                                new Component(collaborator.getName(), ComponentType.valueOf(collaborator.getType()))));
                }
            }
            for (Communication communication : boundedContext.getOutboundCommunication()) {
                for (Collaborator collaborator : communication.getCollaborators()) {
                    if (!(collaborator.getName() == null || collaborator.getName().isBlank())) {
                        components.add(new Component(collaborator.getName(), ComponentType.valueOf(collaborator.getType())));
                    }
                    relationships.add(new Dependency(new Component(collaborator.getName(), ComponentType.valueOf(collaborator.getType())),
                            new Component(boundedContext.getName(), ComponentType.valueOf(CollaboratorType.BOUNDED_CONTEXT))));

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
        for (Dependency relationship : relationships) {
            builder.append(format(relationship.componentA().name()) + " --> " + format(relationship.componentB().name()) + "\n");
        }
        builder.append("\n\n");
        builder.append("@enduml");
        return builder.toString();
    }

    Iterable<? extends BoundedContextAware> obtainBoundedContexts(Set<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            return service.obtain(ids);
        }
        return BoundedContextAwareService.EMPTY_LIST;
    }

    private String format(String text) {
        return text.replaceAll(" ", "_").replaceAll("-", "_");
    }
}
