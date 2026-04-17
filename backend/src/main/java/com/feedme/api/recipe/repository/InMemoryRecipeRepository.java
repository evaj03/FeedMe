package com.feedme.api.recipe.repository;

import com.feedme.api.recipe.model.Recipe;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRecipeRepository implements RecipeRepository {

    private final ConcurrentHashMap<UUID, Recipe> storage = new ConcurrentHashMap<>();

    @Override
    public List<Recipe> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Recipe> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Recipe save(Recipe recipe) {
        storage.put(recipe.id(), recipe);
        return recipe;
    }

    @Override
    public boolean deleteById(UUID id) {
        return storage.remove(id) != null;
    }
}

