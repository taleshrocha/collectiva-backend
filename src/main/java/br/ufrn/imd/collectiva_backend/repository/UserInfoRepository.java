package br.ufrn.imd.collectiva_backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import br.ufrn.imd.collectiva_backend.model.UserInfo;

public interface UserInfoRepository extends GenericRepository<UserInfo> {

    boolean existsByEmail(String email);

    Optional<UserInfo> findByEmail(String email);

    boolean existsByPersonCpf(String cpf);

    Optional<UserInfo> findByPersonCpf(String cpf);

    @Query("""
            select u from UserInfo u
            where upper(u.person.name) like upper(concat('%', ?1, '%')) or upper(u.email) like upper(concat('%', ?1, '%'))
            or upper(u.person.cpf) like upper(concat('%', ?1, '%')) or upper(u.person.phone) like upper(concat('%', ?1, '%'))""")
    Page<UserInfo> searchByParams(String query, Pageable pageable);

}
