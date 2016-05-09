package com.madinnovations.recipekeeper.view.activities.category;

import android.app.Fragment;

import com.madinnovations.recipekeeper.model.entities.Category;

/**
 * Created by madanle on 5/9/16.
 */
public class CategoryDetailFragment extends Fragment {
    private Category category;

    // Getters and setters
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}
