package br.ufrn.imd.collectiva_backend.controller;

import br.ufrn.imd.collectiva_backend.dto.ResourceHistoryDTO;
import br.ufrn.imd.collectiva_backend.model.ResourceHistory;
import br.ufrn.imd.collectiva_backend.service.ResourceHistoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/resource-history")
@Validated
public class ResourceHistoryController extends GenericController<ResourceHistory, ResourceHistoryDTO, ResourceHistoryService> {

    protected ResourceHistoryController(ResourceHistoryService service) {
        super(service);
    }
}
