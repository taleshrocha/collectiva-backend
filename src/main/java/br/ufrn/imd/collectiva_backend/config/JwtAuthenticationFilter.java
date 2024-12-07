package br.ufrn.imd.collectiva_backend.config;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.ufrn.imd.collectiva_backend.dto.ApiResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.ErrorDTO;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Jwt authentication filter.
 *
 * @author Joabson Arley do Nascimento.
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Jwt token provider.
     */
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * This method is responsible for filtering each HTTP request and performing JWT
     * authentication.
     *
     * @param request     HttpServletRequest object
     * @param response    HttpServletResponse object
     * @param filterChain FilterChain object
     * @throws IOException if an input or output exception occurred
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws IOException {
        try {
            Optional<Authentication> authentication = jwtTokenProvider.getAuthentication(request);
            authentication.ifPresent(value -> SecurityContextHolder.getContext().setAuthentication(value));

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

            ErrorDTO err = new ErrorDTO(
                    ZonedDateTime.now(),
                    HttpStatus.UNAUTHORIZED.value(),
                    "Invalid Token.",
                    "",
                    request.getRequestURI());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDTO<>(
                                    false,
                                    "JWT error.",
                                    null,
                                    err))));

        } catch (Exception e) {
            String errorMessage = "Internal server error.";

            ErrorDTO err = new ErrorDTO(
                    ZonedDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    errorMessage,
                    "",
                    request.getRequestURI());

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<>(
                            false,
                            errorMessage,
                            null,
                            err)).toString());

        }
    }
}
