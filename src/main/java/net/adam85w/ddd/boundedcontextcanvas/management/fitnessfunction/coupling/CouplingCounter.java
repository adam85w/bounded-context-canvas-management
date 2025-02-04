package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.coupling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwareness;
import net.adam85w.ddd.boundedcontextcanvas.management.BoundedContextAwarenessService;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import net.adam85w.ddd.boundedcontextcanvas.model.Communication;
import org.springframework.stereotype.Component;

@Component
public class  CouplingCounter {

    private final BoundedContextAwarenessService service;

    private final ObjectMapper mapper;

    CouplingCounter(BoundedContextAwarenessService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    CouplingSummary count() throws JsonProcessingException {
        int afferentCouplingsCounter = 0;
        int efferentCouplingsCounter = 0;
        int componentsCounter = 0;
        for (BoundedContextAwareness boundedContextAwareness : service.obtainAll()) {
            BoundedContext boundedContext = mapper.readValue(boundedContextAwareness.retrieveContext(), BoundedContext.class);
            componentsCounter++;
            for (Communication communication : boundedContext.getInboundCommunication()) {
                afferentCouplingsCounter += communication.getCollaborators().size()*communication.getMessages().size();
            }
            for (Communication communication : boundedContext.getOutboundCommunication()) {
                efferentCouplingsCounter += communication.getCollaborators().size()*communication.getMessages().size();
            }

        }
        return new CouplingSummary(componentsCounter, afferentCouplingsCounter, efferentCouplingsCounter);
    }
}
