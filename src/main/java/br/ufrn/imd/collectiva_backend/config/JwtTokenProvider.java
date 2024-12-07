package br.ufrn.imd.collectiva_backend.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import br.ufrn.imd.collectiva_backend.config.auth.CustomAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Provides JWT token utilities.
 *
 * @since 1.0.0
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.token.prefix}")
    private String TOKEN_PREFIX;

    @Value("${jwt.header.string}")
    private String HEADER_STRING;

    @Value("${jwt.secret}")
    private String SECRET;

    /**
     * Get authentication from JWT token
     *
     * @param request http request
     * @return authentication
     */
    public Optional<Authentication> getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        String path = request.getRequestURL().toString();

        if (token != null && token.startsWith(TOKEN_PREFIX) && !path.contains("/v1/auth/")) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            Integer claimsUserInfoId = (Integer) claims.get("id");
            String username = claims.getSubject();

            if (claimsUserInfoId != null && username != null) {

                Long userInfoId = (long) claimsUserInfoId;

                CustomAuthenticationToken authenticate = new CustomAuthenticationToken(username, userInfoId);

                authenticate.setDetails(new WebAuthenticationDetails(request));

                return Optional.of(authenticate);
            }
        }
        return Optional.empty();
    }

    /**
     * Generates a signing key from a secret.
     * The secret is base64-encoded and used to create a SecretKeySpec.
     *
     * @return the generated signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Base64.getEncoder().encode(this.SECRET.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }
}