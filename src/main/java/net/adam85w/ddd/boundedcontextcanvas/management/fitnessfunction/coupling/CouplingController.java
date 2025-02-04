package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.coupling;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/fitness-functions/couplings")
class CouplingController {

    private final CouplingMeasurementService measurementService;

    CouplingController(CouplingMeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    ModelAndView count(ModelAndView modelAndView) {
        modelAndView.addObject("measurements", measurementService.retrieve());
        modelAndView.setViewName("fitness_functions/couplings");
        return modelAndView;
    }
}
