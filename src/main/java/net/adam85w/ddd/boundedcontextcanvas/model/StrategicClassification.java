package net.adam85w.ddd.boundedcontextcanvas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import net.adam85w.ddd.boundedcontextcanvas.model.strategicclassfication.BusinessModel;
import net.adam85w.ddd.boundedcontextcanvas.model.strategicclassfication.Domain;
import net.adam85w.ddd.boundedcontextcanvas.model.strategicclassfication.Evolution;

public class StrategicClassification {

    @NotNull
    @Schema(name = "domain", description = """
            How important is this context to the success of your organisation?:
            - core domain: a key strategic initiative
            - supporting domain: necessary but not a differentiator
            - generic: a common capability found in many domains
            """, example = "CORE")
    private final Domain domain;

    @NotNull
    @Schema(name = "businessModel", description = """
            What role does the context play in your business model:
            - revenue generator: people pay directly for this
            - engagement creator: users like it but they don't pay for it
            - compliance enforcer: protects your business reputation and existence
            """, example = "COMPLIANCE")
    private final BusinessModel businessModel;

    @NotNull
    @Schema(name = "evolution", description = """
            How evolved is the concept (see Wardley Maps):
            - genesis: new unexplored domain
            - custom built: companies are building their own versions
            - product: off-the-shelf versions exist with differentiation
            - commodity: highly-standardised versions exist
            """, example = "CUSTOM_BUILT")
    private final Evolution evolution;

    public StrategicClassification(Domain domain, BusinessModel businessModel, Evolution evolution) {
        this.domain = domain;
        this.businessModel = businessModel;
        this.evolution = evolution;
    }

    public Domain getDomain() {
        return domain;
    }

    public BusinessModel getBusinessModel() {
        return businessModel;
    }

    public Evolution getEvolution() {
        return evolution;
    }
}
