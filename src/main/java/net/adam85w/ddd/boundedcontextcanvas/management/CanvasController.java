package net.adam85w.ddd.boundedcontextcanvas.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Map;

@Controller
class CanvasController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CanvasController.class);

    private static final BoundedContext BOUNDED_CONTEXT_EMPTY = new BoundedContext(null, null, null, null, new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());

    private final BoundedContext boundedContextExample;

    private final CanvasService service;

    private final TemplateObtainer templateObtainer;

    private final ObjectMapper mapper;

    CanvasController(CanvasService service, TemplateObtainer templateObtainer, ObjectMapper mapper, BoundedContext boundedContextExample) {
        this.service = service;
        this.templateObtainer = templateObtainer;
        this.mapper = mapper;
        this.boundedContextExample = boundedContextExample;
    }

    @GetMapping({"/list", "/"})
    String list(Model model,
                @RequestParam(name="searchPhrase", required = false) String searchPhrase,
                @RequestParam(name="action", defaultValue = "EMPTY") Action action,
                @RequestParam(name="name", required = false) String canvasName) {
        if (Action.EMPTY != action) {
            model.addAttribute("action", action);
            model.addAttribute("canvasName", new String(Base64.getDecoder().decode(canvasName)));
        }
        if (searchPhrase != null && !searchPhrase.isBlank()) {
            model.addAttribute("canvases", service.obtain(searchPhrase));
            model.addAttribute("searchPhrase", searchPhrase);
        } else {
            model.addAttribute("canvases", service.obtainAll());
            model.addAttribute("searchPhrase", null);
        }
        model.addAttribute("templates", templateObtainer.obtain());
        return "canvas/list";
    }

    @GetMapping("/add")
    String add(Model model,@RequestParam(name="example", defaultValue = "false") boolean example) {
        model.addAttribute("id", 0);
        model.addAttribute("title", "Adding a new Bounded Context Canvas");
        if (example) {
            model.addAttribute("boundedContext", boundedContextExample);
        } else {
            model.addAttribute("boundedContext", BOUNDED_CONTEXT_EMPTY);
        }
        model.addAttribute("templates", templateObtainer.obtain());
        return "canvas/form";
    }

    @GetMapping("/edit")
    String edit(Model model, @RequestParam(name = "id") long id) throws JsonProcessingException {
        Canvas canvas = (Canvas) service.obtain(id);
        model.addAttribute("id", canvas.getId());
        model.addAttribute("version", canvas.getVersion());
        model.addAttribute("name", canvas.getName());
        model.addAttribute("boundedContext", mapper.readValue(canvas.retrieveContext(), BoundedContext.class));
        model.addAttribute("templates", templateObtainer.obtain());
        return "canvas/form";
    }

    @GetMapping("/clone")
    String clone(Model model, @RequestParam(name = "id") long id) throws JsonProcessingException {
        Canvas canvas = (Canvas) service.obtain(id);
        model.addAttribute("id", 0);
        model.addAttribute("name", canvas.getName());
        model.addAttribute("boundedContext", mapper.readValue(canvas.retrieveContext(), BoundedContext.class));
        model.addAttribute("templates", templateObtainer.obtain());
        return "canvas/form";
    }

    @GetMapping("/export")
    ResponseEntity<String> export(Model model, @RequestParam(name = "id") long id) {
        Canvas canvas = (Canvas) service.obtain(id);
        model.addAttribute("id", 0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(canvas.getName().replace(" ", "_" ) + ".json")
                .build());
        return new ResponseEntity<>(canvas.retrieveContext(), headers, HttpStatus.OK);
    }

    @GetMapping("/load")
    String load(Model model) {
        return "canvas/load";
    }

    @PostMapping("/load")
    void load(@RequestParam(name = "file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var canvas = service.load(mapper.readValue(file.getInputStream().readAllBytes(), BoundedContext.class));
        response.sendRedirect(String.format("%s/list?action=%s&name=%s", request.getContextPath(), Action.LOAD, Base64.getEncoder().encodeToString(canvas.getName().getBytes(StandardCharsets.UTF_8))));
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<String, String>> save(@RequestBody BoundedContext boundedContext, @RequestParam(name = "id", defaultValue = "0") long id, @RequestParam(name="version", defaultValue = "0") int version, HttpServletRequest request) throws IOException {
        Canvas canvas = service.save(id, version, boundedContext);
        var action = id == 0 ? Action.ADD: Action.EDIT;
        return ResponseEntity.ok(Map.of("url", String.format("%s/list?action=%s&name=%s", request.getContextPath(), action, Base64.getEncoder().encodeToString(canvas.getName().getBytes(StandardCharsets.UTF_8)))));
    }

    @GetMapping(value = {"/remove", "/delete"})
    void delete(@RequestParam("id") long id, @RequestParam("version") int version, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Canvas canvas = (Canvas) service.obtain(id);
        service.delete(id, version);
        response.sendRedirect(String.format("%s/list?action=%s&name=%s", request.getContextPath(), Action.DELETE, Base64.getEncoder().encodeToString(canvas.getName().getBytes(StandardCharsets.UTF_8))));
    }

    @ExceptionHandler(ValidationException.class)
    ErrorResponse handleValidationException(ValidationException e) {
        LOGGER.warn(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(400), e.getMessage()).build();
    }

    @ExceptionHandler(CanvasNotFoundException.class)
    ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        LOGGER.warn(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(404), e.getMessage()).build();
    }

    @ExceptionHandler(CanvasOperationConflictException.class)
    ErrorResponse handleOptimisticLockException(OptimisticLockException e) {
        LOGGER.warn(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(409), e.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    ErrorResponse handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(500), "Internal server error").build();
    }

    enum Action {
        ADD("added"), LOAD("loaded"), EDIT("edited"), DELETE("deleted"), EMPTY(null);

        final String name;

        Action(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
