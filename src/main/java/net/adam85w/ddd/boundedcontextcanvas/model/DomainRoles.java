package net.adam85w.ddd.boundedcontextcanvas.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import net.adam85w.ddd.boundedcontextcanvas.model.domainroles.RoleType;

import java.util.List;

public class DomainRoles {

    @Size(min = 1, max = 3)
    @Schema(name = "roleTypes", type="array", description = """
            A model is likely to be described by a number of different traits. Review the list below and choose which one applies to the context you are working on or think of your own traits
            (It is possible to choose up to three roles, but it is recommended to choose only one for clarity):
            - Specification/Draft Model: Produces a document describing a job/request that needs to be performed. Example: Advertising Campaign Builder
            - Execution Model: Performs or tracks a job. Example: Advertising Campaign Engine
            - Analysis/Audit Model: Monitors the execution. Example: Advertising Campaign Analyser
            - Approver: Receives requests and determines if they should progress to the next step of the process. Example: Fraud Check
            - Enforcer: Ensures that other contexts carry out certain operations. Example: GDPR Context (ensures other contexts delete all of a user’s data)
            - Octopus Enforcer: Ensures that multiple/all contexts in the system all comply with a standard rule. Example: GDPR Context (as above)
            - Interchanger: Translates between multiple ubiquitous languages.
            - Gateway: Sits at the edge of a system and manages inbound and/or outbound communication. Example: IoT Message Gateway
            - Gateway Interchange: The combination of a gateway and an interchange.
            - Dogfood Context: Simulates the customer experience of using the core bounded contexts. Example: Whitelabel music store
            - Bubble Context: Sits in-front of legacy contexts providing a new, cleaner model while legacy contexts are being replaced.
            - Autonomous Bubble: Bubble context which has its own data store and synchronises data asynchronously with the legacy contexts.
            - Brain Context (likely anti-pattern): Contains a large number of important rules and many other contexts depend on it. Example: rules engine containing all the domain rules
            - Funnel Context: Receives documents from multiple upstream contexts and passes them to a single downstream context in a standard format (after applying its own rules).
            - Engagement Context: Provides key features which attract users to keep using the product. Example: Free Financial Advice Context
            - Other
            """, example = """
            [ "EXECUTION_MODEL" ]
            """)
    private final List<RoleType> roleTypes;

    public DomainRoles(@JsonProperty("roleTypes") List<RoleType> roleTypes) {
        this.roleTypes = roleTypes;
    }

    public List<RoleType> getRoleTypes() {
        return roleTypes;
    }
}
