package com.feedme.api.recipe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Visibility {
    @JsonProperty("public")
    PUBLIC,

    @JsonProperty("private")
    PRIVATE
}

