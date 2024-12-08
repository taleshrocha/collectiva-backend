package br.ufrn.imd.collectiva_backend.controller;

import br.ufrn.imd.collectiva_backend.dto.ApiResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.EventDTO;
import br.ufrn.imd.collectiva_backend.model.Event;
import br.ufrn.imd.collectiva_backend.service.EventService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/event")
@Validated
public class EventController extends GenericController<Event, EventDTO, EventService> {

    protected EventController(EventService service) {
        super(service);
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponseDTO<Page<EventDTO>>> filter(
            @RequestParam(required = false) String queryEvent,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String description,
            @ParameterObject Pageable pageable
    ) {

        Sort originalSort = pageable.getSort();
        Sort ignoreCaseSort = Sort.by(
                originalSort.stream()
                        .map(order -> {
                            if ("id".equals(order.getProperty())) {
                                return order;
                            } else {
                                return order.isAscending()
                                        ? Sort.Order.asc(order.getProperty()).ignoreCase()
                                        : Sort.Order.desc(order.getProperty()).ignoreCase();
                            }
                        })
                        .toList()
        );

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ignoreCaseSort);

        Page<EventDTO> pageList = service.filter(queryEvent, name, location, category,
                startDate, endDate, description, pageable);

        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Eventos filtrados com sucesso.",
                pageList,
                null));
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<ApiResponseDTO<EventDTO>> finish(@PathVariable Long id) {

        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Evento finalizado com sucesso.",
                service.finishById(id),
                null));
    }
}
