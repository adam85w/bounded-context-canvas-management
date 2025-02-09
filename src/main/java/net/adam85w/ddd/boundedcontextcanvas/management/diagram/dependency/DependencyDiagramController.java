package net.adam85w.ddd.boundedcontextcanvas.management.diagram.dependency;

import net.adam85w.ddd.boundedcontextcanvas.management.diagram.DiagramEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/diagram/dependency")
class DependencyDiagramController {

    private final DependencyDiagramGenerator generator;

    private final DiagramEncoder diagramEncoder;

    DependencyDiagramController(DependencyDiagramGenerator generator, DiagramEncoder diagramEncoder) {
        this.generator = generator;
        this.diagramEncoder = diagramEncoder;
    }

    @GetMapping
    String generate(Model model) throws IOException {
        var diagramSource = generator.generate();
        model.addAttribute("source", diagramSource);
        model.addAttribute("encoded", diagramEncoder.encode(diagramSource));
        return "diagram/dependency";
    }
}
