package net.adam85w.ddd.boundedcontextcanvas.management;

@FunctionalInterface
public interface CanvasOperationNotifiable {

    void notify(CanvasOperation operation);
}
