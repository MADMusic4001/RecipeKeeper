package com.madinnovations.recipekeeper.controller.events.category;

import com.madinnovations.recipekeeper.model.entities.Category;

/**
 * Event representing a request to perform an action in persistent storage for a Category.
 */
public class CategoryPersistenceEvent {
    public enum Action {
        SAVE,
        READ_BY_ID,
        READ_BY_FILTER,
        DELETE
    }
    private Action action;
    private Category category;

    /**
     * Creates a CategoryPersistenceEvent instance with the given action and Category.
     *
     * @param action  the action to take
     * @param category  a Category to take action on or use as a filter
     */
    public CategoryPersistenceEvent(
            Action action, Category category) {
        this.action = action;
        this.category = category;
    }

    // Getters
    public Action getAction() {
        return action;
    }
    public Category getCategory() {
        return category;
    }
}
