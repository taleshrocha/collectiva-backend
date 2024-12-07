package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.UserInfoDTO;
import br.ufrn.imd.collectiva_backend.mappers.UserInfoMapper;
import br.ufrn.imd.collectiva_backend.model.Person;
import br.ufrn.imd.collectiva_backend.model.UserInfo;
import br.ufrn.imd.collectiva_backend.repository.UserInfoRepository;
import br.ufrn.imd.collectiva_backend.utils.UserInfoTestDataGenerator;
import br.ufrn.imd.collectiva_backend.utils.exception.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserInfoServiceTest {

    @SpyBean
    UserInfoService userInfoService;

    @MockBean
    UserInfoMapper userInfoMapper;

    @MockBean
    UserInfoRepository userInfoRepository;

    @Test
    public void shouldCreate() {
        UserInfoDTO UserInfoDTO = UserInfoTestDataGenerator.createUserInfoDTO();
        UserInfo userInfo = UserInfoTestDataGenerator.createUserInfo();

        when(userInfoMapper.toEntity(any(UserInfoDTO.class))).thenReturn(userInfo);
        when(userInfoMapper.toDTO(any(UserInfo.class))).thenReturn(UserInfoDTO);
        when(userInfoRepository.save(any(UserInfo.class))).thenReturn(userInfo);
        doNothing().when(userInfoService).validateBeforeSave(any(UserInfo.class));

        UserInfoDTO result = userInfoService.create(UserInfoDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(UserInfoDTO.id(), result.id());
    }

    @Test
    public void shouldThrowErrorWhenProvidedInvalidCPFInValidation() {
        Person person = Person.builder()
                .id(1L)
                .cpf("INVALID CPF")
                .name("person")
                .phone("84972132453")
                .build();

        UserInfo userInfo = UserInfo.builder()
                .id(1L)
                .email("userinfo@email.com")
                .password("password")
                .person(person)
                .build();

        Assertions.assertThrowsExactly(BusinessException.class, () -> {
            userInfoService.validateBeforeSave(userInfo);
        });

        Assertions.assertThrowsExactly(BusinessException.class, () -> {
            userInfoService.validateBeforeUpdate(userInfo);
        });
    }

    @Test
    public void shouldCallValidationInCreation() {
        UserInfoDTO userInfoDTO = UserInfoTestDataGenerator.createUserInfoDTO();
        UserInfo userInfo = UserInfoTestDataGenerator.createUserInfo();

        when(userInfoMapper.toEntity(any(UserInfoDTO.class))).thenReturn(userInfo);
        when(userInfoMapper.toDTO(any(UserInfo.class))).thenReturn(userInfoDTO);
        when(userInfoRepository.save(any(UserInfo.class))).thenReturn(userInfo);

        userInfoService.create(userInfoDTO);
        verify(userInfoService, times(1)).validateBeforeSave(any(UserInfo.class));
    }

    @Test
    public void shouldFindEntityWithTheProvidedID() {
        UserInfo userInfo = UserInfoTestDataGenerator.createUserInfo();
        UserInfoDTO userInfoDTO = UserInfoTestDataGenerator.createUserInfoDTO();

        when(userInfoRepository.findById(userInfo.getId())).thenReturn(Optional.of(userInfo));
        when(userInfoMapper.toDTO(any(UserInfo.class))).thenReturn(userInfoDTO);

        Assertions.assertDoesNotThrow(() -> {
            userInfoService.findById(userInfo.getId());
        });

        UserInfoDTO result = userInfoService.findById(userInfo.getId());

        Assertions.assertEquals(result.id(), userInfo.getId());
    }
}
