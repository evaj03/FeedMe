package com.feedme.api.recipe.repository;

import com.feedme.api.recipe.model.Recipe;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository {
    List<Recipe> findAll();

    Optional<Recipe> findById(UUID id);

    Recipe save(Recipe recipe);

    boolean deleteById(UUID id);
}

