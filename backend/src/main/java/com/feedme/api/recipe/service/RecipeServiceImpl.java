package com.feedme.api.recipe.service;

import com.feedme.api.recipe.model.Recipe;
import com.feedme.api.recipe.model.RecipeRequest;
import com.feedme.api.recipe.model.RecipeResponse;
import com.feedme.api.recipe.model.ShareRecipeResponse;
import com.feedme.api.recipe.model.Visibility;
import com.feedme.api.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final String shareBaseUrl;

    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            @Value("${feedme.share.base-url:https://share.feedme.local}") String shareBaseUrl
    ) {
        this.recipeRepository = recipeRepository;
        this.shareBaseUrl = shareBaseUrl;
    }

    @Override
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .sorted(Comparator.comparing(Recipe::createdAt))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RecipeResponse getRecipeById(UUID id) {
        return recipeRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @Override
    public RecipeResponse createRecipe(RecipeRequest request) {
        Instant now = Instant.now();
        return toResponse(recipeRepository.save(buildRecipe(UUID.randomUUID(), request, now, now)));
    }

    @Override
    public RecipeResponse updateRecipe(UUID id, RecipeRequest request) {
        Recipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        return toResponse(recipeRepository.save(buildRecipe(existing.id(), request, existing.createdAt(), Instant.now())));
    }

    @Override
    public void deleteRecipe(UUID id) {
        if (!recipeRepository.deleteById(id)) {
            throw new RecipeNotFoundException(id);
        }
    }

    @Override
    public ShareRecipeResponse shareRecipe(UUID id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        return new ShareRecipeResponse(recipe.id(), shareBaseUrl + "/recipes/" + recipe.id());
    }

    private Recipe buildRecipe(UUID id, RecipeRequest request, Instant createdAt, Instant updatedAt) {
        return new Recipe(
                id,
                request.title().trim(),
                List.copyOf(request.ingredients()),
                request.steps().trim(),
                request.nutritionInfo() == null ? Map.of() : Map.copyOf(request.nutritionInfo()),
                request.tags() == null ? List.of() : List.copyOf(request.tags()),
                request.visibility() == null ? Visibility.PUBLIC : request.visibility(),
                createdAt,
                updatedAt
        );
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
}
