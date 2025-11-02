package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAware;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwareService;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import net.adam85w.ddd.boundedcontextcanvas.model.Communication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
@ConditionalOnProperty(prefix = "application.fitness-function", value = "enabled", havingValue = "true")
class CircularDependencyDiscoverer {

    private final BoundedContextAwareService service;

    private final ObjectMapper mapper;

    CircularDependencyDiscoverer(BoundedContextAwareService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    Set<List<Relation>> discover(LocalDateTime changeAt) throws IOException {
        Set<Relation> relations = obtainSimpleRelations(changeAt);
        Set<List<Relation>> chains = createAllChains(relations);
        return removeDuplicates(findAllCircularDependency(chains));
    }

    private Set<Relation> obtainSimpleRelations(LocalDateTime changeAt) throws JsonProcessingException {
        Set<Relation> relations = new HashSet<>();
        for (BoundedContextAware awareness : service.obtain(changeAt)) {
            BoundedContext boundedContext = mapper.readValue(awareness.retrieveContext(), BoundedContext.class);
            for (Communication communication : boundedContext.getInboundCommunication()) {
                communication.getCollaborators().forEach(collaborator -> relations.add(new Relation(collaborator.getName(), boundedContext.getName())));
            }
            for (Communication communication : boundedContext.getOutboundCommunication()) {
                communication.getCollaborators().forEach(collaborator -> relations.add(new Relation(boundedContext.getName(), collaborator.getName())));
            }
        }
        return relations;
    }

    private Set<List<Relation>> createAllChains(Set<Relation> relations) {
        Set<List<Relation>> chains = new HashSet<>();
        for (Relation relation : relations) {
            chains.addAll(createChains(relation, relations));
        }
        return chains;
    }

    private Set<List<Relation>> createChains(Relation relation, Set<Relation> relations) {
        Set<List<Relation>> chains = new HashSet<>();
        for (Relation possibleRelation : relations) {
            if (relation.componentB().equalsIgnoreCase(possibleRelation.componentA())) {
                LinkedList<Relation> chain = new LinkedList<>();
                chain.add(relation);
                chain.add(possibleRelation);
                chains.add(chain);
                appendChain(chain, relations);
            }
        }
        return chains;
    }

    private List<Relation> appendChain(List<Relation> chain, Set<Relation> relations) {
        for (Relation relation : relations) {
            if (chain.getLast().componentB().equalsIgnoreCase(relation.componentA())) {
                chain.add(relation);
                if (chain.getFirst().equals(relation)) {
                    return chain;
                }
                return appendChain(chain, relations);
            }
        }
        return chain;
    }

    private Set<List<Relation>> findAllCircularDependency(Set<List<Relation>> chains) {
        Set<List<Relation>> circularDependencies = new HashSet<>();
        for (List<Relation> chain : chains) {
            if (chain.getFirst().equals(chain.getLast())) {
                circularDependencies.add(chain);
            }
        }
        return circularDependencies;
    }

    private Set<List<Relation>> removeDuplicates(Set<List<Relation>> chains) {
        Set<List<Relation>> distinctChains = new HashSet<>();
        for (List<Relation> chain : chains) {
            var distinct = true;
            for (List<Relation> added : distinctChains) {
                if (new HashSet<>(chain).containsAll(added)) {
                    distinct = false;
                    break;
                }
            }
            if (distinct) {
                distinctChains.add(chain);
            }
        }
        return distinctChains;
    }

}