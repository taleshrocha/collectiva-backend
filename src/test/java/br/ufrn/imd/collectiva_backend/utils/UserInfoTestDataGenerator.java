package br.ufrn.imd.collectiva_backend.utils;

import br.ufrn.imd.collectiva_backend.dto.PersonDTO;
import br.ufrn.imd.collectiva_backend.dto.UserInfoDTO;
import br.ufrn.imd.collectiva_backend.model.Person;
import br.ufrn.imd.collectiva_backend.model.UserInfo;

public class UserInfoTestDataGenerator {

    public static UserInfo createUserInfo() {
        Person person = Person.builder()
                .id(1L)
                .name("person")
                .phone("84982282342")
                .cpf("99914126030")
                .cellphone("84982282342")
                .cep("48170971")
                .address("Rua Oscar Santana, s/n")
                .neighborhood("Barra")
                .uf("BA")
                .city("Água Fria")
                .number("100")
                .complement("Random")
                .build();

        return UserInfo.builder()
                .id(1L)
                .email("user@email.com")
                .password("password")
                .alternativeEmail("user2@email.com")
                .person(person)
                .build();
    }

    public static UserInfoDTO createUserInfoDTO() {
        PersonDTO personDTO = new PersonDTO(1L, "person", "99914126030", "84982282342",
                                    "84982282342", "48170971", "Rua Oscar Santana, s/n",
                                    "Barra", "BA", "Água Fria", "100", "Random");

        return new UserInfoDTO(1L, "email@email.com", "password", "user2@email.com", personDTO);
    }
}
