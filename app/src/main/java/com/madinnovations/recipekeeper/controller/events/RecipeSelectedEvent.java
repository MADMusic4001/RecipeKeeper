package com.madinnovations.recipekeeper.controller.events;

import com.madinnovations.recipekeeper.model.entities.Recipe;

/**
 * Event indicating a Recipe was selected from the list of recipes.
 */
public class RecipeSelectedEvent {
    private Recipe recipe;

    /**
     * Creates a new RecipeSelectedEvent instance with the given Recipe
     *
     * @param recipe  the selected Recipe instance
     */
    public RecipeSelectedEvent(Recipe recipe) {
        this.recipe = recipe;
    }

    // Getter
    public Recipe getRecipe() {
        return recipe;
    }
}
