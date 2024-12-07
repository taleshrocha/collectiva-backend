package br.ufrn.imd.collectiva_backend.controller;

import br.ufrn.imd.collectiva_backend.dto.OrganizationDTO;
import br.ufrn.imd.collectiva_backend.model.Organization;
import br.ufrn.imd.collectiva_backend.service.OrganizationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/organization")
@Validated
public class OrganizationController extends GenericController<Organization, OrganizationDTO, OrganizationService> {

    protected OrganizationController(OrganizationService service) {
        super(service);
    }
}
