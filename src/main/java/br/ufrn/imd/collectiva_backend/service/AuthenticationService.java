package br.ufrn.imd.collectiva_backend.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.ufrn.imd.collectiva_backend.dto.ApiResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.AuthenticationDTO;
import br.ufrn.imd.collectiva_backend.dto.LoginResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.RefreshTokenDTO;
import br.ufrn.imd.collectiva_backend.dto.UserInfoDTO;
import br.ufrn.imd.collectiva_backend.mappers.UserInfoMapper;
import br.ufrn.imd.collectiva_backend.model.RefreshToken;
import br.ufrn.imd.collectiva_backend.model.UserInfo;
import br.ufrn.imd.collectiva_backend.repository.RefreshTokenRepository;
import br.ufrn.imd.collectiva_backend.repository.UserInfoRepository;
import br.ufrn.imd.collectiva_backend.utils.exception.BusinessException;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserInfoService userInfoService;
    private final AuthenticationManager authenticationManager;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper userInfoMapper;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationService(JwtService jwtService, UserInfoService userInfoService,
            AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
            UserInfoRepository userInfoRepository, UserInfoMapper userInfoMapper,
            RefreshTokenRepository refreshTokenRepository) {
        this.jwtService = jwtService;
        this.userInfoService = userInfoService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userInfoMapper = userInfoMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> authenticate(AuthenticationDTO authenticationDTO) {
        UserDetails savedUser;
        try {
            savedUser = userInfoService.loadUserByUsername(authenticationDTO.email());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null, null));
        }

        if (savedUser != null && !savedUser.isAccountNonLocked()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDTO<>(false, "Acesso do usuário está bloqueado", null, null));
        }

        Authentication authenticate = new UsernamePasswordAuthenticationToken(authenticationDTO.email(),
                authenticationDTO.password());
        Authentication authentication = authenticationManager.authenticate(authenticate);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserInfo user = (UserInfo) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        if (refreshTokenRepository.existsByUser(user)) {
            refreshTokenRepository.deleteAllByUser(user.getId());
        }

        refreshTokenRepository.save(new RefreshToken(newRefreshToken, user));

        LoginResponseDTO loginResponse = new LoginResponseDTO(accessToken, newRefreshToken);

        return ResponseEntity.ok().body(new ApiResponseDTO<>(true, "Usuário logado com sucesso!", loginResponse, null));
    }

    public UserInfoDTO signup(AuthenticationDTO authenticationDTO) {
        if (userInfoRepository.existsByEmail(authenticationDTO.email())) {
            throw new BusinessException(" Email já cadastrado",
                    HttpStatus.CONFLICT);
        }

        UserInfo userInfoSaved = UserInfo.builder()
                .email(authenticationDTO.email())
                .password(passwordEncoder.encode(authenticationDTO.password()))
                .build();

        return userInfoMapper.toDTO(userInfoRepository.save(userInfoSaved));
    }

    @Transactional
    public LoginResponseDTO refreshAccessToken(RefreshTokenDTO refreshTokenDTO) {
        try {
            String refreshToken = refreshTokenDTO.refreshToken();
            String email = jwtService.getEmail(refreshToken);
            UserInfo user = userInfoService.loadUserByUsername(email);
            Optional<RefreshToken> savedRefreshToken = refreshTokenRepository.findByToken(refreshToken);

            if (!jwtService.isTokenValid(refreshToken, user) || savedRefreshToken.isEmpty()) {
                throw new BusinessException("Refresh token inválido ou expirado!", HttpStatus.FORBIDDEN);
            }

            refreshTokenRepository.delete(savedRefreshToken.get());
            String newAccessToken = jwtService.generateAccessToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            refreshTokenRepository.save(new RefreshToken(newRefreshToken, user));

            return new LoginResponseDTO(newAccessToken, newRefreshToken);
        } catch (Exception e) {
            throw new BusinessException("Erro ao gerar novo token de acesso: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
