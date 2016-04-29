/**
 * Copyright (C) 2016 MadMusic4001
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.recipekeeper.model.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Represents the relationship between a category and a recipe.
 */
@Table(name = "recipe_category")
public class RecipeCategory extends Model {
	@Column(name = "recipe_id")
	private Recipe recipe;
	@Column(name = "category_id")
	private Category category;

	/**
	 * Creates a RecipeCategory instance.
	 */
	public RecipeCategory() {
	}

	/**
	 * Gets a list of Recipe instances.
	 *
	 * @return a list of Recipe instances.
	 */
	public List<Recipe> recipes() {
		return getMany(Recipe.class, "RecipeCategory");
	}

	/**
	 * Gets a list of Category instances.
	 *
	 * @return a list of Category instances.
	 */
	public List<Category> categories() {
		return getMany(Category.class, "RecipeCategory");
	}

	// Getters and setters
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
}
