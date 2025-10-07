package net.adam85w.ddd.boundedcontextcanvas.management;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface BoundedContextAwareService {

    List<? extends BoundedContextAware> EMPTY_LIST = Collections.emptyList();

    BoundedContextAware obtain(long id);

    Iterable<? extends BoundedContextAware> obtain(Set<Long> ids);

    Iterable<? extends BoundedContextAware> obtain(LocalDateTime updatedAt);

    Iterable<? extends BoundedContextAware> obtainAll();
}
