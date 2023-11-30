package com.cognixus.todo.enums;

public enum Role {
    ALL("ALL"),
    LIBRARIAN("LIBRARIAN"),
    MEMBER("MEMBER"),
    ;

    private final String description;

    Role(String description) {
        this.description = description;
    }

}
