package net.adam85w.ddd.boundedcontextcanvas.model.strategicclassfication;

public enum BusinessModel {

    REVENUE("Revenue"),
    ENGAGEMENT("Engagement"),
    COMPLIANCE("Compliance"),
    COST_REDUCTION("Cost reduction"),
    OTHER("Other");

    private final String name;

    BusinessModel(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
