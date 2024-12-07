package br.ufrn.imd.collectiva_backend.dto;

public record PersonDTO(
        Long id,
        String name,
        String cpf,
        String phone,
        String cellphone,
        String cep,
        String address,
        String neighborhood,
        String uf,
        String city,
        String number,
        String complement) implements EntityDTO {
    @Override
    public EntityDTO toResponse() {
        return this;
    }
}
