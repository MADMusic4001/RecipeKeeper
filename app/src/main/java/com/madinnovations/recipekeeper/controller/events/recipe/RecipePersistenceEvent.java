package com.madinnovations.recipekeeper.controller.events.recipe;

import com.madinnovations.recipekeeper.model.entities.Recipe;

/**
 * Event representing a request to perform an action in persistent storage for a Recipe.
 */
public class RecipePersistenceEvent {
    public enum Action {
        SAVE,
        READ_BY_ID,
        READ_BY_FILTER,
        DELETE
    }
    private Action action;
    private Recipe recipe;

    /**
     * Creates a RecipePersistenceEvent instance with the given Action and Recipe.
     *
     * @param action  the action to take
     * @param recipe  the Recipe instance to take action on or use as a filter
     */
    public RecipePersistenceEvent(Action action, Recipe recipe) {
        this.action = action;
        this.recipe = recipe;
    }

    // Getters
    public Action getAction() {
        return action;
    }
    public Recipe getRecipe() {
        return recipe;
    }
}
