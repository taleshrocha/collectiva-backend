package br.ufrn.imd.collectiva_backend.model;

import java.util.Objects;

import jakarta.persistence.*;

import org.hibernate.annotations.SQLRestriction;

import br.ufrn.imd.collectiva_backend.model.builders.FileBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@SQLRestriction("active = true")
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FILE_ENTITY")
    @SequenceGenerator(name = "SEQ_FILE_ENTITY", sequenceName = "seq_file_entity", allocationSize = 1)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String path;

    @NotNull
    private Long size;

    @NotBlank
    private String mimeType;

    public static FileBuilder builder() {
        return new FileBuilder();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        File file = (File) o;
        return Objects.equals(name, file.name) && Objects.equals(description, file.description)
                && Objects.equals(path, file.path) && Objects.equals(size, file.size)
                && Objects.equals(mimeType, file.mimeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, path, size, mimeType);
    }

}
