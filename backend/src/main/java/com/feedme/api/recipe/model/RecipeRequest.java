package com.feedme.api.recipe.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Map;

public record RecipeRequest(
        @NotBlank(message = "title is required")
        String title,
        @NotEmpty(message = "ingredients are required")
        List<@NotBlank(message = "ingredient value must not be blank") String> ingredients,
        @NotBlank(message = "steps are required")
        String steps,
        Map<String, Object> nutritionInfo,
        List<String> tags,
        @Pattern(regexp = "public|private", message = "visibility must be public or private")
        String visibility
) {
}

