package br.ufrn.imd.collectiva_backend.controller;

import br.ufrn.imd.collectiva_backend.dto.ApiResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.AuthenticationDTO;
import br.ufrn.imd.collectiva_backend.dto.LoginResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.RefreshTokenDTO;
import br.ufrn.imd.collectiva_backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    @Operation(tags = "Client", summary = "Authenticate the user")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> authenticate(@RequestBody @Valid AuthenticationDTO data) {
        return authenticationService.authenticate(data);
    }

    @PostMapping("/refresh-token")
    @Operation(tags = "Client", summary = "Generate a new access token from refresh token")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> refreshToken(
            @RequestBody @Valid RefreshTokenDTO refreshTokenDTO) {
        LoginResponseDTO response = authenticationService.refreshAccessToken(refreshTokenDTO);

        return ResponseEntity.ok()
                .body(new ApiResponseDTO<>(true, "Refresh Token atualizado com sucesso!", response, null));
    }
}
