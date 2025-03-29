package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/fitness-functions/circular-dependencies")
class CircularDependencyController {

    private final CircularDependencyDiscoverer discoverer;

    private final CircularDependencyMeasurementService measurementService;

    CircularDependencyController(CircularDependencyDiscoverer discoverer, CircularDependencyMeasurementService measurementService) {
        this.discoverer = discoverer;
        this.measurementService = measurementService;
    }

    @GetMapping
    String discover(Model model, @RequestParam(name = "pageNo", defaultValue = "0") int pageNo) throws IOException {
        model.addAttribute("circularDependencies", discoverer.discover());
        model.addAttribute("page", measurementService.retrieve(pageNo));
        return "fitness_functions/circular_dependencies";
    }
}
