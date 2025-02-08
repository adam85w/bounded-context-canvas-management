package net.adam85w.ddd.boundedcontextcanvas.management.diagram.dependency;

import net.adam85w.ddd.boundedcontextcanvas.management.diagram.DiagramEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    ModelAndView generate(ModelAndView modelAndView) throws IOException {
        var diagramSource = generator.generate();
        modelAndView.addObject("source", diagramSource);
        modelAndView.addObject("encoded", diagramEncoder.encode(diagramSource));
        modelAndView.setViewName("diagram/dependency");
        return modelAndView;
    }
}
