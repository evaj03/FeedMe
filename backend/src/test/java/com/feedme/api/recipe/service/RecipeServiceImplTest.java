package com.feedme.api.recipe.service;

import com.feedme.api.recipe.model.Recipe;
import com.feedme.api.recipe.model.RecipeRequest;
import com.feedme.api.recipe.model.RecipeResponse;
import com.feedme.api.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    void createRecipeUsesDefaultVisibilityWhenMissing() {
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecipeRequest request = new RecipeRequest(
                "Tomato Pasta",
                List.of("Tomatoes", "Pasta"),
                "Boil pasta and mix with sauce",
                Map.of("calories", 450),
                List.of("vegetarian"),
                null
        );

        RecipeResponse response = recipeService.createRecipe(request);

        assertEquals("public", response.visibility());
        assertEquals("Tomato Pasta", response.title());
    }

    @Test
    void updateRecipeThrowsWhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        RecipeRequest request = new RecipeRequest(
                "Tomato Pasta",
                List.of("Tomatoes", "Pasta"),
                "Boil pasta and mix with sauce",
                Map.of(),
                List.of(),
                "public"
        );

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(id, request));
    }

    @Test
    void deleteRecipeThrowsWhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(recipeRepository.deleteById(id)).thenReturn(false);

        assertThrows(RecipeNotFoundException.class, () -> recipeService.deleteRecipe(id));
    }

    @Test
    void shareRecipeReturnsPublicLink() {
        UUID id = UUID.randomUUID();
        when(recipeRepository.findById(id)).thenReturn(Optional.of(sampleRecipe(id)));

        String publicUrl = recipeService.shareRecipe(id).publicUrl();

        assertEquals("https://share.feedme.local/recipes/" + id, publicUrl);
    }

    @Test
    void getAllRecipesSortsByCreatedAt() {
        UUID olderId = UUID.randomUUID();
        UUID newerId = UUID.randomUUID();

        Recipe older = new Recipe(
                olderId,
                "Older",
                List.of("A"),
                "Steps",
                Map.of(),
                List.of(),
                "public",
                Instant.parse("2026-04-16T00:00:00Z"),
                Instant.parse("2026-04-16T00:00:00Z")
        );

        Recipe newer = new Recipe(
                newerId,
                "Newer",
                List.of("B"),
                "Steps",
                Map.of(),
                List.of(),
                "public",
                Instant.parse("2026-04-17T00:00:00Z"),
                Instant.parse("2026-04-17T00:00:00Z")
        );

        when(recipeRepository.findAll()).thenReturn(List.of(newer, older));

        List<RecipeResponse> response = recipeService.getAllRecipes();

        assertEquals(olderId, response.get(0).id());
        assertEquals(newerId, response.get(1).id());
    }

    @Test
    void updateRecipePersistsChanges() {
        UUID id = UUID.randomUUID();
        Recipe existing = sampleRecipe(id);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecipeRequest request = new RecipeRequest(
                "Updated Pasta",
                List.of("Tomatoes", "Pasta", "Basil"),
                "Cook and garnish",
                Map.of("calories", 500),
                List.of("vegetarian"),
                "private"
        );

        recipeService.updateRecipe(id, request);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(captor.capture());
        assertEquals("Updated Pasta", captor.getValue().title());
        assertEquals("private", captor.getValue().visibility());
    }

    private Recipe sampleRecipe(UUID id) {
        return new Recipe(
                id,
                "Tomato Pasta",
                List.of("Tomatoes", "Pasta"),
                "Boil pasta and mix with sauce",
                Map.of("calories", 450),
                List.of("vegetarian"),
                "public",
                Instant.parse("2026-04-17T00:00:00Z"),
                Instant.parse("2026-04-17T00:00:00Z")
        );
    }
}

