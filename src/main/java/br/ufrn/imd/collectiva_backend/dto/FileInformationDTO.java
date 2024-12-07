package br.ufrn.imd.collectiva_backend.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;

public class FileInformationDTO implements Serializable {
    private long id;
    private @NotBlank String name;
    private String description;

    public FileInformationDTO(long id, @NotBlank String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
