package br.ufrn.imd.collectiva_backend.controller;

import br.ufrn.imd.collectiva_backend.dto.ResourceDTO;
import br.ufrn.imd.collectiva_backend.model.Resource;
import br.ufrn.imd.collectiva_backend.service.ResourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/resource")
@Validated
public class ResourceController extends GenericController<Resource, ResourceDTO, ResourceService> {

    protected ResourceController(ResourceService service) {
        super(service);
    }
}
