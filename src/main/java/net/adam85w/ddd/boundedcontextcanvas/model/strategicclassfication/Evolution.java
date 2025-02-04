package net.adam85w.ddd.boundedcontextcanvas.model.strategicclassfication;

public enum Evolution {
    GENESIS("Genesis"),
    CUSTOM_BUILT("Custom built"),
    PRODUCT("Product"),
    COMMODITY("Commodity"),
    OTHER("Other");

    private final String name;

    Evolution(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}