package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.communication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/fitness-functions/communications")
class CommunicationController {

    private final CommunicationMeasurementService measurementService;

    CommunicationController(CommunicationMeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    ModelAndView count(ModelAndView modelAndView) {
        modelAndView.addObject("measurements", measurementService.retrieve());
        modelAndView.setViewName("fitness_functions/communications");
        return modelAndView;
    }
}
