package com.feedme.api.recipe.service;

import com.feedme.api.recipe.model.Recipe;
import com.feedme.api.recipe.model.RecipeRequest;
import com.feedme.api.recipe.model.RecipeResponse;
import com.feedme.api.recipe.model.ShareRecipeResponse;
import com.feedme.api.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RecipeServiceImpl implements RecipeService {

    private static final String DEFAULT_VISIBILITY = "public";

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .sorted(Comparator.comparing(Recipe::createdAt))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RecipeResponse createRecipe(RecipeRequest request) {
        Instant now = Instant.now();
        Recipe recipe = new Recipe(
                UUID.randomUUID(),
                request.title().trim(),
                List.copyOf(request.ingredients()),
                request.steps().trim(),
                request.nutritionInfo() == null ? Map.of() : Map.copyOf(request.nutritionInfo()),
                request.tags() == null ? List.of() : List.copyOf(request.tags()),
                normalizeVisibility(request.visibility()),
                now,
                now
        );

        Recipe saved = recipeRepository.save(recipe);
        return toResponse(saved);
    }

    @Override
    public RecipeResponse updateRecipe(UUID id, RecipeRequest request) {
        Recipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));

        Recipe updated = new Recipe(
                existing.id(),
                request.title().trim(),
                List.copyOf(request.ingredients()),
                request.steps().trim(),
                request.nutritionInfo() == null ? Map.of() : Map.copyOf(request.nutritionInfo()),
                request.tags() == null ? List.of() : List.copyOf(request.tags()),
                normalizeVisibility(request.visibility()),
                existing.createdAt(),
                Instant.now()
        );

        Recipe saved = recipeRepository.save(updated);
        return toResponse(saved);
    }

    @Override
    public void deleteRecipe(UUID id) {
        boolean removed = recipeRepository.deleteById(id);
        if (!removed) {
            throw new RecipeNotFoundException(id);
        }
    }

    @Override
    public ShareRecipeResponse shareRecipe(UUID id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));

        return new ShareRecipeResponse(recipe.id(), "https://share.feedme.local/recipes/" + recipe.id());
    }

    private RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.id(),
                recipe.title(),
                recipe.ingredients(),
                recipe.steps(),
                recipe.nutritionInfo(),
                recipe.tags(),
                recipe.visibility(),
                recipe.createdAt(),
                recipe.updatedAt()
        );
    }

    private String normalizeVisibility(String visibility) {
        if (visibility == null || visibility.isBlank()) {
            return DEFAULT_VISIBILITY;
        }
        return visibility;
    }
}

