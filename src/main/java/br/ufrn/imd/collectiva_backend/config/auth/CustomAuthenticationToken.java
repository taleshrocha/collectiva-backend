package br.ufrn.imd.collectiva_backend.config.auth;

import javax.security.auth.Subject;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private final String principal;
    private final Long userInfoId;

    public CustomAuthenticationToken(String principal, Long userInfoId) {
        super(null);
        this.principal = principal;
        this.userInfoId = userInfoId;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
