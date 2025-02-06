package net.adam85w.ddd.boundedcontextcanvas.management.diagram;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/diagram/component-flow")
class DiagramController {

    private final DiagramGenerator generator;

    DiagramController(DiagramGenerator generator) {
        this.generator = generator;
    }

    @GetMapping
    ModelAndView generate(ModelAndView modelAndView) throws IOException {
        var diagramSource = generator.generate();
        modelAndView.addObject("source", diagramSource);
        modelAndView.addObject("encoded", DiagramEncoder.encode(diagramSource));
        modelAndView.setViewName("diagram/component_flow");
        return modelAndView;
    }
}
