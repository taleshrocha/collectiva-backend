package br.ufrn.imd.collectiva_backend.mappers;

import org.springframework.stereotype.Component;

import br.ufrn.imd.collectiva_backend.dto.PersonDTO;
import br.ufrn.imd.collectiva_backend.model.Person;

@Component
public class PersonMapper implements DTOMapper<Person, PersonDTO> {
    @Override
    public PersonDTO toDTO(Person entity) {
        return new PersonDTO(
                entity.getId(),
                entity.getName(),
                entity.getCpf(),
                entity.getPhone(),
                entity.getCellphone(),
                entity.getCep(),
                entity.getAddress(),
                entity.getNeighborhood(),
                entity.getUf(),
                entity.getCity(),
                entity.getNumber(),
                entity.getComplement());
    }

    @Override
    public Person toEntity(PersonDTO personDTO) {
        return Person.builder()
                .id(personDTO.id())
                .name(personDTO.name())
                .cpf(personDTO.cpf())
                .phone(personDTO.phone())
                .cellphone(personDTO.cellphone())
                .cep(personDTO.cep())
                .address(personDTO.address())
                .neighborhood(personDTO.neighborhood())
                .number(personDTO.number())
                .uf(personDTO.uf())
                .city(personDTO.city())
                .complement(personDTO.complement())
                .build();
    }
}
