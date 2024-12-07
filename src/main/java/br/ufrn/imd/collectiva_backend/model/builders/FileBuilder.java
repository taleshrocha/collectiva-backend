package br.ufrn.imd.collectiva_backend.model.builders;

import br.ufrn.imd.collectiva_backend.model.File;

public class FileBuilder {
    private Long id;
    private String name;
    private String description;
    private String path;
    private Long size;
    private String mimeType;

    public FileBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public FileBuilder name(String name) {
        this.name = name;
        return this;
    }

    public FileBuilder description(String description) {
        this.description = description;
        return this;
    }

    public FileBuilder path(String path) {
        this.path = path;
        return this;
    }

    public FileBuilder size(Long size) {
        this.size = size;
        return this;
    }

    public FileBuilder mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public File build() {
        File file = new File();
        file.setId(this.id);
        file.setName(this.name);
        file.setDescription(this.description);
        file.setPath(this.path);
        file.setSize(this.size);
        file.setMimeType(this.mimeType);
        return file;
    }
}
