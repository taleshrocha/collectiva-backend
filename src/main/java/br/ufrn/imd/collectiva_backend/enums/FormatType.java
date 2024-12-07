package br.ufrn.imd.collectiva_backend.enums;

public enum FormatType {
    PRESENTIAL("Presencial"),
    HYBRID("Híbrido"),
    REMOTE("Remoto");

    private final String name;

    FormatType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
