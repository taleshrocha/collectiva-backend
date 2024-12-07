package br.ufrn.imd.collectiva_backend.model.builders;

import br.ufrn.imd.collectiva_backend.model.Person;
import br.ufrn.imd.collectiva_backend.model.UserInfo;

public class UserInfoBuilder {
    private Long id;
    private String email;
    private String password;
    private String alternativeEmail;
    private Person person;

    public UserInfoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserInfoBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserInfoBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserInfoBuilder alternativeEmail(String alternativeEmail) {
        this.alternativeEmail = alternativeEmail;
        return this;
    }

    public UserInfoBuilder person(Person person) {
        this.person = person;
        return this;
    }

    public UserInfo build() {
        var user = new UserInfo();
        user.setId(this.id);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setAlternativeEmail(this.alternativeEmail);
        user.setPerson(this.person);
        return user;
    }
}
