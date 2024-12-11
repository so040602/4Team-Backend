package com.recipe.entity;

import lombok.Getter;

@Getter
public enum RegistrationState {
    TEMP("임시저장"),
    PUBLISHED("발행됨");

    private final String description;

    RegistrationState(String description) {
        this.description = description;
    }
}
