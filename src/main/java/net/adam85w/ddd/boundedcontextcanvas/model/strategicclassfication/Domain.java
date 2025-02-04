package net.adam85w.ddd.boundedcontextcanvas.model.strategicclassfication;

public enum Domain {
    CORE("Core"),
    SUPPORTING("Supporting"),
    GENERIC("Generic"),
    OTHER("Other");

    private final String name;

    Domain(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
