package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.UserInfoDTO;
import br.ufrn.imd.collectiva_backend.model.UserInfo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserInfoMapper implements DTOMapper<UserInfo, UserInfoDTO> {

    private final PersonMapper personMapper;

    public UserInfoMapper(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }


    @Override
    public UserInfoDTO toDTO(UserInfo entity) {

        return new UserInfoDTO(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getAlternativeEmail(),
                personMapper.toDTO(entity.getPerson())
        );
    }

    @Override
    public UserInfo toEntity(UserInfoDTO userInfoDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return UserInfo.builder()
                .id(userInfoDTO.id())
                .email(userInfoDTO.email())
                .password(passwordEncoder.encode(userInfoDTO.password()))
                .alternativeEmail(userInfoDTO.alternativeEmail())
                .person(personMapper.toEntity(userInfoDTO.person()))
                .build();
    }
}
