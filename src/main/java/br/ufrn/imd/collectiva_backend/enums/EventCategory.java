package br.ufrn.imd.collectiva_backend.enums;

public enum EventCategory {
    SHOW("Show"),
    LECTURE("Palestra"),
    PARTY("Festa");

    private final String name;

    EventCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
