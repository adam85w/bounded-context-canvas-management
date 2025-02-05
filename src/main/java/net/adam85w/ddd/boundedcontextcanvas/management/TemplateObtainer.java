package net.adam85w.ddd.boundedcontextcanvas.management;

import java.util.Map;
import java.util.Set;

@FunctionalInterface
public interface TemplateObtainer {

    Map<String, Set<String>> obtain();
}
