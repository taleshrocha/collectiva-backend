package br.ufrn.imd.collectiva_backend.model.builders;

import br.ufrn.imd.collectiva_backend.model.Person;

public class PersonBuilder {
    private Long id;
    private String name;
    private String phone;
    private String cpf;
    private String cellphone;
    private String cep;
    private String address;
    private String neighborhood;
    private String uf;
    private String city;
    private String number;
    private String complement;

    public PersonBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PersonBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public PersonBuilder cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public PersonBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder cellphone(String cellphone) {
        this.cellphone = cellphone;
        return this;
    }

    public PersonBuilder cep(String cep) {
        this.cep = cep;
        return this;
    }

    public PersonBuilder address(String address) {
        this.address = address;
        return this;
    }

    public PersonBuilder neighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
        return this;
    }

    public PersonBuilder uf(String uf) {
        this.uf = uf;
        return this;
    }

    public PersonBuilder city(String city) {
        this.city = city;
        return this;
    }

    public PersonBuilder number(String number) {
        this.number = number;
        return this;
    }

    public PersonBuilder complement(String complement) {
        this.complement = complement;
        return this;
    }

    public Person build() {
        var person = new Person();
        person.setId(this.id);
        person.setName(this.name);
        person.setPhone(this.phone);
        person.setCpf(this.cpf);
        person.setCellphone(this.cellphone);
        person.setCep(this.cep);
        person.setAddress(this.address);
        person.setNeighborhood(this.neighborhood);
        person.setNumber(this.number);
        person.setUf(this.uf);
        person.setCity(this.city);
        person.setComplement(this.complement);
        return person;
    }
}
