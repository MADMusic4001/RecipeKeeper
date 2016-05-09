package com.madinnovations.recipekeeper.controller.events.category;

import com.madinnovations.recipekeeper.model.entities.Category;

/**
 * Event indicating a {@link Category} was selected from the list of recipes.
 */
public class CategorySelectedEvent {
    private Category category;

    /**
     * Creates a new CategorySelectedEvent with the given Category
     *
     * @param category  the selected Category instance
     */
    public CategorySelectedEvent(Category category) {
        this.category = category;
    }

    // Getter
    public Category getCategory() {
        return category;
    }
}
