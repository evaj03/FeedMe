package com.feedme.api.recipe.api;

import com.feedme.api.common.GlobalExceptionHandler;
import com.feedme.api.recipe.model.RecipeRequest;
import com.feedme.api.recipe.model.RecipeResponse;
import com.feedme.api.recipe.model.ShareRecipeResponse;
import com.feedme.api.recipe.model.Visibility;
import com.feedme.api.recipe.service.RecipeNotFoundException;
import com.feedme.api.recipe.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecipeController.class)
@Import(GlobalExceptionHandler.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    void listRecipesReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(recipeService.getAllRecipes()).thenReturn(List.of(sampleResponse(id)));

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].title").value("Tomato Pasta"));
    }

    @Test
    void getRecipeByIdReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(recipeService.getRecipeById(id)).thenReturn(sampleResponse(id));

        mockMvc.perform(get("/api/recipes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Tomato Pasta"));
    }

    @Test
    void getRecipeByIdNotFoundReturns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(recipeService.getRecipeById(id)).thenThrow(new RecipeNotFoundException(id));

        mockMvc.perform(get("/api/recipes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void createRecipeReturnsCreatedWithLocationHeader() throws Exception {
        UUID id = UUID.randomUUID();
        when(recipeService.createRecipe(any(RecipeRequest.class))).thenReturn(sampleResponse(id));

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/recipes/" + id))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.visibility").value("public"));
    }

    @Test
    void updateRecipeReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(recipeService.updateRecipe(eq(id), any(RecipeRequest.class))).thenReturn(sampleResponse(id));

        mockMvc.perform(put("/api/recipes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void deleteRecipeReturnsNoContentAndCallsService() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/recipes/{id}", id))
                .andExpect(status().isNoContent());

        verify(recipeService).deleteRecipe(id);
    }

    @Test
    void shareRecipeReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(recipeService.shareRecipe(id))
                .thenReturn(new ShareRecipeResponse(id, "https://share.feedme.local/recipes/" + id));

        mockMvc.perform(post("/api/recipes/{id}/share", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(id.toString()))
                .andExpect(jsonPath("$.publicUrl").value("https://share.feedme.local/recipes/" + id));
    }

    @Test
    void createRecipeWithInvalidPayloadReturnsBadRequest() throws Exception {
        RecipeRequest invalid = new RecipeRequest("", List.of(), "", null, null, Visibility.PUBLIC);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void updateRecipeNotFoundReturnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new RecipeNotFoundException(id))
                .when(recipeService).updateRecipe(eq(id), any(RecipeRequest.class));

        mockMvc.perform(put("/api/recipes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    private RecipeRequest sampleRequest() {
        return new RecipeRequest(
                "Tomato Pasta",
                List.of("Tomatoes", "Pasta"),
                "Boil pasta and mix with sauce",
                Map.of("calories", 450),
                List.of("vegetarian"),
                Visibility.PUBLIC
        );
    }

    private RecipeResponse sampleResponse(UUID id) {
        Instant now = Instant.parse("2026-04-17T00:00:00Z");
        return new RecipeResponse(
                id, "Tomato Pasta", List.of("Tomatoes", "Pasta"),
                "Boil pasta and mix with sauce", Map.of("calories", 450),
                List.of("vegetarian"), Visibility.PUBLIC, now, now
        );
    }
}
