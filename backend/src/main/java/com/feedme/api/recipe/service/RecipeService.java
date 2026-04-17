package com.feedme.api.recipe.service;

import com.feedme.api.recipe.model.RecipeRequest;
import com.feedme.api.recipe.model.RecipeResponse;
import com.feedme.api.recipe.model.ShareRecipeResponse;

import java.util.List;
import java.util.UUID;

public interface RecipeService {
    List<RecipeResponse> getAllRecipes();

    RecipeResponse createRecipe(RecipeRequest request);

    RecipeResponse updateRecipe(UUID id, RecipeRequest request);

    void deleteRecipe(UUID id);

    ShareRecipeResponse shareRecipe(UUID id);
}

