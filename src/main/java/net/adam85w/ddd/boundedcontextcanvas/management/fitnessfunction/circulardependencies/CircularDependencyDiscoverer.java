package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwareness;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwarenessService;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import net.adam85w.ddd.boundedcontextcanvas.model.Communication;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.Collaborator;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class CircularDependencyDiscoverer {

    private final BoundedContextAwarenessService service;

    private final ObjectMapper mapper;

    CircularDependencyDiscoverer(BoundedContextAwarenessService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    Set<CircularDependency> discover() throws IOException {
        Set<Relation> relations = obtainRelations();
        Set<CircularDependency> circularDependencies = new HashSet<>();
        for (Relation relationA : relations) {
            for (Relation relationB : relations) {
                if (relationA.equals(relationB)) {
                    continue;
                }
                if (relationA.componentA().equals(relationB.componentB())) {
                    if (circularDependencies.contains(new CircularDependency(relationB, relationA)))  {
                        continue;
                    }
                    circularDependencies.add(new CircularDependency(relationA, relationB));
                }
            }
        }
        return circularDependencies;
    }

    private Set<Relation> obtainRelations() throws JsonProcessingException {
        Set<Relation> relations = new HashSet<>();
        Iterable<? extends BoundedContextAwareness> awarenesses = service.obtainAll();
        for (BoundedContextAwareness awarenessA : awarenesses) {
            BoundedContext boundedContextA = mapper.readValue(awarenessA.retrieveContext(), BoundedContext.class);
            for (BoundedContextAwareness awarenessB : awarenesses) {
                if (awarenessA.retrieveContext().equals(awarenessB.retrieveContext())) {
                    continue;
                }
                BoundedContext boundedContextB = mapper.readValue(awarenessB.retrieveContext(), BoundedContext.class);
                for (Communication inbound : boundedContextB.getInboundCommunication()) {
                    for (Collaborator collaborator : inbound.getCollaborators()) {
                        if (collaborator.getName().equals(boundedContextA.getName())) {
                            relations.add(new Relation(boundedContextA.getName(), obtainRelationTypes(inbound.getMessages()), boundedContextB.getName()));
                        }
                    }
                }
                for (Communication outbound : boundedContextB.getOutboundCommunication()) {
                    for (Collaborator collaborator : outbound.getCollaborators()) {
                        if (collaborator.getName().equals(boundedContextA.getName())) {
                            relations.add(new Relation(boundedContextB.getName(), obtainRelationTypes(outbound.getMessages()), boundedContextA.getName()));
                        }
                    }
                }
            }
        }
        return relations;
    }

    private String obtainRelationTypes(List<Message> messages) {
        return String.join(", ", messages.stream()
                .map(message -> message.getType().getName().toLowerCase())
                .collect(Collectors.toSet()));
    }
}
