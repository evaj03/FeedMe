package com.feedme.api.recipe.service;

import java.util.UUID;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException(UUID id) {
        super("Recipe not found for id: " + id);
    }
}

