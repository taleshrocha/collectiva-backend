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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/event")
@Validated
public class EventController extends GenericController<Event, EventDTO, EventService> {

    protected EventController(EventService service) {
        super(service);
    }

    //@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
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
                "Sucesso na listagem",
                pageList,
                null));
    }
}
