package br.ufrn.imd.collectiva_backend.config.envers;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "revinfo", schema = "audit")
@RevisionEntity(CustomRevisionListener.class)
public class CustomRevisionEntity extends DefaultRevisionEntity {

    private Long userInfoId;

    private String ipAddress;

    public Long getUserInfoId() {
        return userInfoId;
    }

    public CustomRevisionEntity setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}