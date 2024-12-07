package br.ufrn.imd.collectiva_backend.config.envers;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import br.ufrn.imd.collectiva_backend.config.auth.CustomAuthenticationToken;

public class CustomRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity revision = (CustomRevisionEntity) revisionEntity;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication instanceof CustomAuthenticationToken) {
                CustomAuthenticationToken customToken = (CustomAuthenticationToken) authentication;
                WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) customToken.getDetails();

                revision.setUserInfoId(customToken.getUserInfoId());
                revision.setIpAddress(webAuthenticationDetails.getRemoteAddress());
            } else {
                revision.setUserInfoId(null);
                revision.setIpAddress("unknown");
            }
        } else {
            revision.setUserInfoId(null);
            revision.setIpAddress("unknown");
        }
    }
}