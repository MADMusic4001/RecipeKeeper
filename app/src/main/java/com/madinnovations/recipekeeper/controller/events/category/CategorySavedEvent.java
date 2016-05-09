package com.madinnovations.recipekeeper.controller.events.category;

import com.madinnovations.recipekeeper.model.entities.Category;

/**
 * Created by madanle on 5/9/16.
 */
public class CategorySavedEvent {
    private boolean success;
    private Category category;

    /**
     * Creates a new CategorySavedEvent instance for the given Category
     *
     * @param category  the Category instance that was saved
     * @param success  true if the Category was successfully saved, otherwise false
     */
    public CategorySavedEvent(Category category, boolean success) {
        this.category = category;
        this.success = success;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }
    public Category getCategory() {
        return category;
    }
}
