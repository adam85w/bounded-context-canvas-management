package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAware;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwareService;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.Collaborator;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.Message;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.MessageType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
class CommunicationCounter {

    private final BoundedContextAwareService service;

    private final ObjectMapper mapper;

    CommunicationCounter(BoundedContextAwareService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    HashMap<String, Long> count() throws JsonProcessingException {
        var communications = obtainCommunications();
        HashMap<String, Long> summaries = new HashMap<>();
        for (String communicationType : Arrays.stream(MessageType.values()).map(type -> type.getName().toLowerCase()).toList()) {
            var amount = communications.stream().filter(communication -> communicationType.equals(communication.communicationType())).count();
            summaries.put(communicationType, amount);
        }
        return summaries;
    }

    private Set<Communication> obtainCommunications() throws JsonProcessingException {
        Set<Communication> communications = new HashSet<>();
        for (BoundedContextAware boundedContextAware : service.obtainAll()) {
            BoundedContext boundedContext = mapper.readValue(boundedContextAware.retrieveContext(), BoundedContext.class);
            for (net.adam85w.ddd.boundedcontextcanvas.model.Communication communication : boundedContext.getInboundCommunication()) {
                for (Collaborator collaborator : communication.getCollaborators()) {
                    for (Message message : communication.getMessages()) {
                        communications.add(new Communication(collaborator.getName(), message.getType().getName().toLowerCase(), boundedContext.getName()));
                    }
                }
            }
            for (net.adam85w.ddd.boundedcontextcanvas.model.Communication communication : boundedContext.getOutboundCommunication()) {
                for (Collaborator collaborator : communication.getCollaborators()) {
                    for (Message message : communication.getMessages()) {
                        communications.add(new Communication(boundedContext.getName(), message.getType().getName().toLowerCase(), collaborator.getName()));
                    }
                }
            }
        }
        return communications;
    }
}
