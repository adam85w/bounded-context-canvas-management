package net.adam85w.ddd.boundedcontextcanvas.management;

public interface BoundedContextAwareService {

    BoundedContextAware obtain(long id);

    Iterable<? extends BoundedContextAware> obtainAll();
}
