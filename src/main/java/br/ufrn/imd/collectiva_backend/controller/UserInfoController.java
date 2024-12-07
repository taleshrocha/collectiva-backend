package br.ufrn.imd.collectiva_backend.controller;

import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.collectiva_backend.dto.ApiResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.EntityDTO;
import br.ufrn.imd.collectiva_backend.dto.UserInfoDTO;
import br.ufrn.imd.collectiva_backend.model.UserInfo;
import br.ufrn.imd.collectiva_backend.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/user-info")
@Validated
public class UserInfoController extends GenericController<UserInfo, UserInfoDTO, UserInfoService> {

    protected UserInfoController(UserInfoService userInfoService) {
        super(userInfoService);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO<Page<UserInfoDTO>>> getAllByQuery(@ParameterObject Pageable pageable,
                                                                           @RequestParam(required = false) String query) {
        Page<UserInfoDTO> pageList = service.search(query, pageable);

        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Sucesso na listagem",
                pageList,
                null));
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<ApiResponseDTO<UserInfoDTO>> getByEmail(@PathVariable String email) {
        Optional<UserInfoDTO> userInfoDTO = service.getByEmail(email);

        return userInfoDTO
                .map(infoDTO -> ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(
                        true,
                        "Success",
                        infoDTO,
                        null)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>(
                        false,
                        "Failed: User not found",
                        null,
                        null)));
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponseDTO<Boolean>> checkEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(
                true,
                "Success: Request has been finished.",
                service.existByEmail(email),
                null));
    }

    @GetMapping("/by-cpf/{cpf}")
    public ResponseEntity<ApiResponseDTO<UserInfoDTO>> getByCpf(@PathVariable String cpf) {
        Optional<UserInfoDTO> userInfoDTO = service.getByCpf(cpf);

        return userInfoDTO
                .map(infoDTO -> ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(
                        true,
                        "Success",
                        infoDTO,
                        null)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>(
                        false,
                        "Failed: User not found",
                        null,
                        null)));
    }

    @GetMapping("/check-cpf/{cpf}")
    public ResponseEntity<ApiResponseDTO<Boolean>> checkCpf(@PathVariable String cpf) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(
                true,
                "Success: Request has been finished.",
                service.existByCpf(cpf),
                null));
    }
}
