package net.adam85w.ddd.boundedcontextcanvas.model.domainroles;

public enum RoleType {

    SPECIFICATION("Specification/Draft Model"),
    EXECUTION_MODEL("Execution Model"),
    ANALYSIS("Analysis/Audit Model"),
    APPROVER("Approver"),
    ENFORCER("Enforcer"),
    OCTOPUS_ENFORCER("Octopus Enforcer"),
    INTERCHANGER("Interchanger"),
    GATEWAY("Gateway"),
    GATEWAY_INTERCHANGE("Gateway Interchange"),
    DOGFOOD_CONTEXT("Dogfood Context"),
    BUBBLE_CONTEXT("Bubble Context"),
    AUTONOMOUS_BUBBLE("Autonomous Bubble"),
    BRAIN_CONTEXT("Brain Context (likely anti-pattern)"),
    FUNNEL_CONTEXT("Funnel Context"),
    ENGAGEMENT_CONTEXT("Engagement Context"),
    OTHER("Other");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
