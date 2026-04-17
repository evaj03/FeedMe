package com.feedme.api.recipe.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public record RecipeRequest(
        @NotBlank(message = "title is required")
        @Size(max = 200, message = "title must not exceed 200 characters")
        String title,
        @NotEmpty(message = "ingredients are required")
        List<@NotBlank(message = "ingredient value must not be blank") String> ingredients,
        @NotBlank(message = "steps are required")
        String steps,
        Map<String, Object> nutritionInfo,
        List<@NotBlank(message = "tag value must not be blank") String> tags,
        Visibility visibility
) {
}
