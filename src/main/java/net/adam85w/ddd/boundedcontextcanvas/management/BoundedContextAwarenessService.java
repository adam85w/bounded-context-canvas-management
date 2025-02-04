package net.adam85w.ddd.boundedcontextcanvas.management;

public interface BoundedContextAwarenessService {

    BoundedContextAwareness obtain(long id);

    Iterable<? extends BoundedContextAwareness> obtainAll();
}
