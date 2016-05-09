package com.madinnovations.recipekeeper.controller.events.category;

import com.madinnovations.recipekeeper.model.entities.Category;

import java.util.Set;

/**
 * Event indicating Category instances have been loaded from persistent storage.
 */
public class CategoriesLoadedEvent {
    private Set<Category> categorySet;
    private boolean successful;

    /**
     * Creates a new CategoriesLoadedEvent
     *
     * @param categorySet  a Set<Category> instance
     * @param successful  true if the Category set was loaded successfully
     */
    public CategoriesLoadedEvent(Set<Category> categorySet, boolean successful) {
        this.categorySet = categorySet;
        this.successful = successful;
    }

    // Getters
    public Set<Category> getCategorySet() {
        return categorySet;
    }
    public boolean isSuccessful() {
        return successful;
    }
}
