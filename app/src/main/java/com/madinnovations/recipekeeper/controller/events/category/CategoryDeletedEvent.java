package com.madinnovations.recipekeeper.controller.events.category;

import com.madinnovations.recipekeeper.model.entities.Category;

/**
 * Event signifying the result of a request to delete a Category instance from persistent storage.
 */
public class CategoryDeletedEvent {
    private boolean successful;
    private Category category;

    /**
     * Creates a CategoryDeletedEvent instance with the given status and the filter for the instances that were to be
     * deleted.
     *
     * @param successful  true if the delete was successful, otherwise false
     * @param category  the filter for the instances to be deleted
     */
    public CategoryDeletedEvent(boolean successful, Category category) {
        this.successful = successful;
        this.category = category;
    }

    // Getters
    public boolean isSuccessful() {
        return successful;
    }
    public Category getCategory() {
        return category;
    }
}
