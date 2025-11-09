package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAware;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwareService;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import net.adam85w.ddd.boundedcontextcanvas.model.Communication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
@ConditionalOnProperty(prefix = "application.fitness-function", value = "enabled", havingValue = "true")
class CircularDependencyDiscoverer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircularDependencyDiscoverer.class);

    private final BoundedContextAwareService service;

    private final ObjectMapper mapper;

    private final boolean debug;

    private final int limit;

    CircularDependencyDiscoverer(BoundedContextAwareService service, ObjectMapper mapper,
                                 @Value("${application.fitness-function.circular-dependencies.limit:10}") int limit,
                                 @Value("${application.fitness-function.circular-dependencies.debug:false}") boolean debug) {
        this.service = service;
        this.mapper = mapper;
        this.limit = limit;
        this.debug = debug;
    }

    Set<List<Relation>> discover(LocalDateTime changeAt) throws IOException {
        Set<Relation> relations = obtainSimpleRelations(changeAt);
        Set<List<Relation>> chains = createAllChains(relations);
        if (debug) {
            printChains(chains);
        }
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
                chains.addAll(appendChain(chain, relations, 0));
            }
        }
        return chains;
    }

    private Set<List<Relation>> appendChain(List<Relation> chain, Set<Relation> relations, int deep) {
        Set<List<Relation>> chains = new HashSet<>();
        if (deep < limit) {
            for (Relation relation : relations) {
                if (chain.getLast().componentB().equalsIgnoreCase(relation.componentA())) {
                    chain.add(relation);
                    if (chain.getFirst().equals(relation) || chain.get(chain.size()-3).equals(relation)) {
                        chains.add(new LinkedList<>(chain));
                        chain.removeLast();
                        continue;
                    }
                    chains.addAll(appendChain(chain, relations, deep + 1));
                    chain.removeLast();
                }
            }
        }
        return chains;
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

    private void printChains(Set<List<Relation>> chains) {
        for (List<Relation> chain : chains) {
            StringBuilder chainString = new StringBuilder();
            for (Relation relation : chain) {
                chainString.append("[" + relation.componentA() + " -> " + relation.componentB() + "] -> ");
            }
            LOGGER.info(chainString.replace(chainString.length()-" -> ".length(), chainString.length(), "").toString());
        }
    }
}