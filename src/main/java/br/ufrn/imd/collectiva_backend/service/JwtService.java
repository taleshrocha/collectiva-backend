package br.ufrn.imd.collectiva_backend.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.ufrn.imd.collectiva_backend.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Util class for JWT.
 *
 * @author Joabson Arley do Nascimento.
 * @since 1.0.0
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token.expiration}")
    private long expirationAccessToken;

    @Value("${jwt.refresh-token.expiration}")
    private long expirationRefreshToken;

    /**
     * Generates a JWT access token for a given username.
     *
     * @param principal the username for which to generate a token
     * @return a JWT access token for the given username
     */
    public String generateAccessToken(Object principal) {
        UserInfo user = (UserInfo) principal;
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("email", user.getEmail());
        claims.put("id", user.getId());

        return generateToken(claims, user, expirationAccessToken);
    }

    /**
     * Generates a JWT refresh token for a given username.
     *
     * @param principal the username for which to generate a token
     * @return a JWT refresh token for the given username
     */
    public String generateRefreshToken(Object principal) {
        UserInfo user = (UserInfo) principal;
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("email", user.getEmail());

        return generateToken(claims, user, expirationRefreshToken);
    }

    /**
     * Generates a JWT token for a given username and claims.
     * The token is signed with a key derived from a secret and set to expire after
     * a certain period.
     *
     * @param claims     a map of claims to include in the token
     * @param user       the user for which to generate a token
     * @param expiration the added time to expire the token
     * @return a JWT token for the given username and claims
     */
    public String generateToken(Map<String, Object> claims, UserInfo user, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getPerson().getName())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Retrieves the email from a given JWT token.
     *
     * @param token the JWT token from which to retrieve the email
     * @return the email retrieved from the token
     */
    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    /**
     * Checks if a given JWT token is valid.
     * A token is considered valid if it contains a username and is not expired.
     *
     * @param token    the JWT token to check
     * @param userInfo the user info to check
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserInfo userInfo) {
        String username = getEmail(token);
        return username != null && (username.equals(userInfo.getEmail())) && !isTokenExpired(token);
    }

    /**
     * Checks if a given JWT token is expired.
     *
     * @param token the JWT token to check
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        Date expirationDate = getClaims(token).getExpiration();
        return expirationDate.before(new Date());
    }

    /**
     * Retrieves the claims from a given JWT token.
     *
     * @param token the JWT token from which to retrieve the claims
     * @return the claims retrieved from the token
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Generates a signing key from a secret.
     * The secret is base64-encoded and used to create a SecretKeySpec.
     *
     * @return the generated signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Base64.getEncoder().encode(this.secret.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    public long getExpirationAccessToken() {
        return expirationAccessToken;
    }
}
