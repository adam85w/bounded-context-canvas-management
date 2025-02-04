package net.adam85w.ddd.boundedcontextcanvas.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Collections;
import java.util.List;

public class BoundedContext {

    @NotBlank
    @Size(min = 3, max = 30)
    @Schema(name = "name", description = "The name a the bounded context", example = "Scoring")
    private final String name;

    @NotBlank
    @Size(max = 500)
    @Schema(name = "purpose", description = """
                                            What benefits does this context provide
                                            and how does it provide them?
                                            Describe the purpose from a business
                                            perspective.""", example = """
                                            Provide a fully automated, highly trustable and reliable assessment
                                            of the approvability of a credit application for retail mortgage loans.""")
    private final String purpose;

    @Valid
    @NotNull
    @Schema(name = "strategicClassification")
    private final StrategicClassification strategicClassification;

    @Valid
    @NotNull
    @Schema(name ="domainRoles", description = """
            How can you characterise the behaviour of this bounded context?
            Does it receive high volumes of data and crunch them into insights
            - an analysis context? Or does it enforce a workflow 
            - an execution context? Identifying the different roles a context 
            plays can help to avoid coupling responsibilities.""")
    private final DomainRoles domainRoles;

    @Valid
    @Schema(name = "inboundCommunication", description = "Inbound communication represents collaborations that are initiated by other collaborators.",
        type = "array", example = """
            [
                {
                  "collaborators": [
                    {
                      "name": "Application Entry",
                      "type": "BOUNDED_CONTEXT"
                    }
                  ],
                  "messages": [
                    {
                      "name": "Application submitted",
                      "type": "COMMAND"
                    }
                  ]
                },
                {
                  "collaborators": [
                    {
                      "name": "Document Check",
                      "type": "BOUNDED_CONTEXT"
                    }
                  ],
                  "messages": [
                    {
                      "name": "Application marked as checked",
                      "type": "COMMAND"
                    }
                  ]
                },
                {
                  "collaborators": [
                    {
                      "name": "Real Estate Rating",
                      "type": "BOUNDED_CONTEXT"
                    }
                  ],
                  "messages": [
                    {
                      "name": "Real Estate ratios calculated",
                      "type": "COMMAND"
                    }
                  ]
                }
            ]
            """)
    @JsonSetter(contentNulls = Nulls.SKIP)
    private final List<Communication> inboundCommunication;

    @Valid
    @Schema(name ="outboundCommunication", description = """
            Outbound communication represents collaborations that are initiated by this context to interact with other collaborators.
            The same message types and notations apply as inbound communication.""",
            type = "array", example = """
            [
                {
                  "messages": [
                    {
                      "name": "Pre Scoring Green",
                      "type": "COMMAND"
                    }
                  ],
                  "collaborators": [
                    {
                      "name": "Document Check",
                      "type": "BOUNDED_CONTEXT"
                    }
                  ]
                },
                {
                  "messages": [
                    {
                      "name": "Pre Scoring Red",
                      "type": "COMMAND"
                    }
                  ],
                  "collaborators": [
                    {
                      "name": "Credit Decision",
                      "type": "BOUNDED_CONTEXT"
                    }
                  ]
                },
                {
                  "messages": [
                    {
                      "name": "Main Scoring Green",
                      "type": "COMMAND"
                    },
                    {
                      "name": "Main Scoring Red",
                      "type": "COMMAND"
                    }
                  ],
                  "collaborators": [
                    {
                      "name": "Credit Decision",
                      "type": "BOUNDED_CONTEXT"
                    }
                  ]
                },
                {
                  "messages": [
                    {
                      "name": "Retrieve Account Balance",
                      "type": "QUERY"
                    }
                  ],
                  "collaborators": [
                    {
                      "name": "Core Banking System",
                      "type": "EXTERNAL_SYSTEM"
                    }
                  ]
                },
                {
                  "messages": [
                    {
                      "name": "Determine creditworthiness od applicant",
                      "type": "EVENT"
                    }
                  ],
                  "collaborators": [
                    {
                      "name": "Credit Agency",
                      "type": "EXTERNAL_SYSTEM"
                    }
                  ]
                }
            ]
            """)
    @JsonSetter(contentNulls = Nulls.SKIP)
    private final List<Communication> outboundCommunication;

    @NotEmpty
    @Schema(name ="ubiquitousLanguage", description = "What are the key domain terms that exist within this context, and what do they mean?")
    private final List<UbiquitousLanguage> ubiquitousLanguage;

    @NotEmpty
    @Schema(name ="businessDecisions", description = "What are the key business rules and policies within this context?",
            type="array", example = """
                ["KO Criteria",
                 "Point Based Rules",
                 "Scoring Result: 1 KO Criteria red >= 120 point and no KO: green"]""")
    private final List<String> businessDecisions;

    @NotEmpty
    @Schema(name ="assumptions", description = """
            You will never make design decisions having a full knowledge about everything in your domain.
            Most design happens based on assumptions and it is highly recommended to make them explicit.
            This can be done in this section of the Bounded Context Design Canvas.""",
            type="array", example = """
                    ["Pre- and Main Scoring will run on the same rules for the forseeable future",
                     "We will only work with one credit agency",
                     "There will be a high stability in the area of real estate ratios"]
                """)
    private final List<String> assumptions;

    @NotEmpty
    @Schema(name ="verificationMetrics", description = """
            Domain Driven Design is about an iterative approach towards modelling and design based on continuous learning.
            Metrics can help you gathering valuable input for those learnings (think about build-measure-learn).
            Think about metrics that you and your team can define in order to gather learnings if the chosen boundaries of your bounded context are a good fit or not.""",
            type="array", example = """
            ["95% of changes will affect both pre- and main scoring and 90% only on rule cluster at a time",
             "75% of changes to the application form will have no impact on Scoring"]""")
    private final List<String> verificationMetrics;

    @Schema(name = "openQuestions", description = """
            If you have questions that no one in the room can answer while running a workshop you can enter them into this section of the canvas.
            This way you can make sure that no open questions get lost but you can also get a visual indicator how certain the team is regarding the design of a given bounded context." +
            Many questions are a good indicator towards a high degree of uncertainty.""",
            type="array", example = """
            ["Should there be a score color yellow for errors?"]""")
    private final List<String> openQuestions;

    public BoundedContext(String name,
                          String purpose,
                          StrategicClassification strategicClassification,
                          DomainRoles domainRoles,
                          List<Communication> inboundCommunication,
                          List<Communication> outboundCommunication,
                          List<UbiquitousLanguage> ubiquitousLanguage,
                          List<String> businessDecisions,
                          List<String> assumptions,
                          List<String> verificationMetrics,
                          List<String> openQuestions) {
        this.name = name;
        this.purpose = purpose;
        this.strategicClassification = strategicClassification;
        this.domainRoles = domainRoles;
        this.inboundCommunication = inboundCommunication == null ? Collections.emptyList() : inboundCommunication;
        this.outboundCommunication = outboundCommunication == null ? Collections.emptyList() : outboundCommunication;
        this.ubiquitousLanguage = ubiquitousLanguage;
        this.businessDecisions = businessDecisions;
        this.assumptions = assumptions;
        this.verificationMetrics = verificationMetrics;
        this.openQuestions = openQuestions == null ? Collections.emptyList() : openQuestions;
    }


    public String getName() {
        return name;
    }

    public String getPurpose() {
        return purpose;
    }

    public StrategicClassification getStrategicClassification() {
        return strategicClassification;
    }

    public DomainRoles getDomainRoles() { return domainRoles; }

    public List<Communication> getInboundCommunication() {
        return inboundCommunication;
    }

    public List<Communication> getOutboundCommunication() {
        return outboundCommunication;
    }

    public List<UbiquitousLanguage> getUbiquitousLanguage() {
        return ubiquitousLanguage;
    }

    public List<String> getBusinessDecisions() {
        return businessDecisions;
    }

    public List<String> getAssumptions() {
        return assumptions;
    }

    public List<String> getVerificationMetrics() {
        return verificationMetrics;
    }

    public List<String> getOpenQuestions() {
        return openQuestions;
    }
}
