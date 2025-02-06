package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    ModelAndView discover(ModelAndView modelAndView) throws IOException {
        modelAndView.addObject("circularDependencies", discoverer.discover());
        modelAndView.addObject("measurements", measurementService.retrieve());
        modelAndView.setViewName("fitness_functions/circular_dependencies");
        return modelAndView;
    }
}
