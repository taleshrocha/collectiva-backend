package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.UserInfoDTO;
import br.ufrn.imd.collectiva_backend.mappers.DTOMapper;
import br.ufrn.imd.collectiva_backend.mappers.UserInfoMapper;
import br.ufrn.imd.collectiva_backend.model.UserInfo;
import br.ufrn.imd.collectiva_backend.repository.GenericRepository;
import br.ufrn.imd.collectiva_backend.repository.UserInfoRepository;
import br.ufrn.imd.collectiva_backend.utils.exception.BusinessException;
import br.ufrn.imd.collectiva_backend.utils.validators.CpfValidator;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserInfoService implements GenericService<UserInfo, UserInfoDTO>, UserDetailsService {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper mapper;

    public UserInfoService(UserInfoRepository userInfoRepository, UserInfoMapper mapper) {
        this.userInfoRepository = userInfoRepository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<UserInfo> getRepository() {
        return this.userInfoRepository;
    }

    @Override
    public DTOMapper<UserInfo, UserInfoDTO> getDtoMapper() {
        return this.mapper;
    }

    @Override
    public UserInfo loadUserByUsername(String email) throws UsernameNotFoundException {
        return userInfoRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }

    public boolean existByEmail(String email) {
        return userInfoRepository.existsByEmail(email);
    }

    public Optional<UserInfoDTO> getByEmail(String email) {
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail(email);

        return userInfo.map(mapper::toDTO);
    }

    public boolean existByCpf(String cpf) {
        return userInfoRepository.existsByPersonCpf(cpf);
    }

    public Optional<UserInfoDTO> getByCpf(String cpf) {
        Optional<UserInfo> userInfo = userInfoRepository.findByPersonCpf(cpf);

        return userInfo.map(mapper::toDTO);
    }

    public Page<UserInfoDTO> search(@Nullable String query, Pageable pageable) {
        return query == null
                ? userInfoRepository.findAll(pageable).map(mapper::toDTO)
                : userInfoRepository.searchByParams(query, pageable).map(mapper::toDTO);
    }

    @Override
    public void validateBeforeSave(UserInfo entity) {
        GenericService.super.validateBeforeSave(entity);

        if (!CpfValidator.validateCPF(entity.getPerson().getCpf())) {
            throw new BusinessException("The user's CPF is invalid.", HttpStatus.BAD_REQUEST);
        } else if (userInfoRepository.existsByPersonCpf(entity.getPerson().getCpf())) {
            throw new BusinessException("A user is already using this cpf.", HttpStatus.BAD_REQUEST);
        } else if (userInfoRepository.existsByEmail(entity.getEmail())) {
            throw new BusinessException("A user is already using this email.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validateBeforeUpdate(UserInfo entity) {
        GenericService.super.validateBeforeUpdate(entity);

        Optional<UserInfo> anotherPersonEmail = userInfoRepository.findByEmail(entity.getEmail());
        Optional<UserInfo> anotherPersonCpf = userInfoRepository.findByPersonCpf(entity.getPerson().getCpf());

        if (!CpfValidator.validateCPF(entity.getPerson().getCpf())) {
            throw new BusinessException("The user's CPF is invalid.", HttpStatus.BAD_REQUEST);
        } else if (anotherPersonCpf.isPresent() && !anotherPersonCpf.get().getId().equals(entity.getId())) {
            throw new BusinessException("A user is already using this cpf.", HttpStatus.BAD_REQUEST);
        } else if (anotherPersonEmail.isPresent() && !anotherPersonEmail.get().getId().equals(entity.getId())) {
            throw new BusinessException("A user is already using this email.", HttpStatus.BAD_REQUEST);
        }
    }
}
