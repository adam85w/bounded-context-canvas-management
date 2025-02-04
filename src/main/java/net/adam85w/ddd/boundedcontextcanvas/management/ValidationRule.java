package net.adam85w.ddd.boundedcontextcanvas.management;

import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;

import java.util.Set;
import java.util.function.Predicate;

interface ValidationRule extends Predicate<BoundedContext> {

    String getMessage();

    Set<ValidationRuleType> getTypes();
}


