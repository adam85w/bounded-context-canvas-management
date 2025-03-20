package net.adam85w.ddd.boundedcontextcanvas.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
class CanvasService implements BoundedContextAwareService {

    private final ObjectMapper mapper;

    private final CanvasRepository repository;

    private final CanvasOperationRepository canvasOperationRepository;

    private final Set<ValidationRule> validationRulesForCreate;

    private final Set<ValidationRule> validationRulesForUpdate;

    CanvasService(CanvasRepository repository, CanvasOperationRepository canvasOperationRepository, ObjectMapper mapper, Set<ValidationRule> validationRules) {
        this.repository = repository;
        this.canvasOperationRepository = canvasOperationRepository;
        this.mapper = mapper;
        this.validationRulesForCreate = validationRules.stream().filter(validationRule -> validationRule.getTypes().contains(ValidationRuleType.CREATE)).collect(Collectors.toSet());
        this.validationRulesForUpdate = validationRules.stream().filter(validationRule -> validationRule.getTypes().contains(ValidationRuleType.UPDATE)).collect(Collectors.toSet());
    }

    @Override
    public BoundedContextAware obtain(long id) {
        return repository.findById(id).orElseThrow(() -> new CanvasNotFoundException("The canvas doesn't exist or was removed."));
    }

    @Override
    public Iterable<? extends BoundedContextAware> obtain(Set<Long> ids) {
        return repository.findByIdInOrderByUpdatedAtDesc(ids);
    }

    @Override
    public Iterable<? extends BoundedContextAware> obtainAll() {
        return repository.findAllByOrderByUpdatedAtDesc();
    }

    Iterable<? extends BoundedContextAware> obtain(String searchPhrase) {
        return repository.findAllByNameContainingOrderByUpdatedAtDesc(searchPhrase);
    }

    Canvas save(long id, int version, BoundedContext boundedContext) throws JsonProcessingException {
        if (id == 0) {
            return insert(boundedContext);
        }
        var entity = repository.findById(id).orElseThrow(() -> new CanvasNotFoundException("The canvas doesn't exist or was removed."));
        if (entity.getVersion() != version) {
            throw new CanvasOperationConflictException("The canvas was modified by a different user! Please refresh and check for changes.");
        }
        return update(entity, boundedContext);
    }

    Canvas load(BoundedContext boundedContext) throws JsonProcessingException {
        Optional<Canvas> entity = repository.findByName(boundedContext.getName());
        if (entity.isEmpty()) {
            return insert(boundedContext);
        }
        return update(entity.get(), boundedContext);
    }

    void delete(long id, int version) {
        Canvas entity = repository.findById(id).orElseThrow(() -> new CanvasNotFoundException("The canvas doesn't exist or was removed."));
        if (entity.getVersion() != version) {
            throw new CanvasOperationConflictException("TThe canvas was modified by a different user! Please refresh and check for changes.");
        }
        repository.delete(entity);
        canvasOperationRepository.save(new CanvasOperation(entity.getName(), OperationType.REMOVE, LocalDateTime.now()));
    }

    private Canvas insert(BoundedContext boundedContext) throws JsonProcessingException {
        validationRulesForCreate.stream()
                .filter(validationRule -> validationRule.test(boundedContext))
                .findFirst()
                .ifPresent(validationRule -> { throw new ValidationException(validationRule.getMessage()); });
        canvasOperationRepository.save(new CanvasOperation(boundedContext.getName(), OperationType.ADD, LocalDateTime.now()));
        return repository.save(new Canvas(boundedContext.getName(), mapper.writeValueAsString(boundedContext), LocalDateTime.now()));
    }

    private Canvas update(Canvas entity, BoundedContext boundedContext) throws JsonProcessingException {
        validationRulesForUpdate.stream()
                .filter(validationRule -> validationRule.test(boundedContext))
                .findFirst()
                .ifPresent(validationRule -> { throw new ValidationException(validationRule.getMessage()); });
        entity.update(mapper.writeValueAsString(boundedContext), LocalDateTime.now());
        canvasOperationRepository.save(new CanvasOperation(entity.getName(), OperationType.EDIT, LocalDateTime.now()));
        return repository.save(entity);
    }
}
