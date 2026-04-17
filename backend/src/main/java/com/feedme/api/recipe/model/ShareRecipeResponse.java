package com.feedme.api.recipe.model;

import java.util.UUID;

public record ShareRecipeResponse(
        UUID recipeId,
        String publicUrl
) {
}
