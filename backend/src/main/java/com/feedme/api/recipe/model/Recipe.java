package com.feedme.api.recipe.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record Recipe(
        UUID id,
        String title,
        List<String> ingredients,
        String steps,
        Map<String, Object> nutritionInfo,
        List<String> tags,
        String visibility,
        Instant createdAt,
        Instant updatedAt
) {
}

