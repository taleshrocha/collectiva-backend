package br.ufrn.imd.collectiva_backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import br.ufrn.imd.collectiva_backend.model.Person;

public interface PersonRepository extends GenericRepository<Person> {

    boolean existsByCpf(String cpf);

    Optional<Person> findByCpf(String cpf);

    @Query("""
            select p from Person p
            where upper(p.name) like upper(concat('%', ?1, '%')) or upper(p.cpf) like upper(concat('%', ?1, '%'))
             or upper(p.phone) like upper(concat('%', ?1, '%'))""")
    Page<Person> searchByParams(String query, Pageable pageable);
}
