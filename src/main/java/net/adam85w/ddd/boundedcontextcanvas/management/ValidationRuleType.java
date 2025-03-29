package net.adam85w.ddd.boundedcontextcanvas.management;

import java.util.Set;

public enum ValidationRuleType {

    CREATE, UPDATE;

    static final Set<ValidationRuleType> ALL = Set.of(CREATE, UPDATE) ;
}
