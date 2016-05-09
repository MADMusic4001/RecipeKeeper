/**
 * Copyright (C) 2016 MadInnovations
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
package com.madinnovations.recipekeeper.controller.events.recipe;

import com.madinnovations.recipekeeper.model.entities.Recipe;

import java.util.Set;

/**
 * Event indicating Recipes have been loaded from persistent storage.
 */
public class RecipesLoadedEvent {
	private boolean success;
	private Set<Recipe> recipes;

	/**
	 * Creates a new RecipesLoadedEvent with the given recipes and status.
	 *
	 * @param recipes  a set of Recipe instances
	 * @param success  true if the Recipe instances were successfully loaded from persistent storage.
	 */
	public RecipesLoadedEvent(Set<Recipe> recipes, boolean success) {
		this.recipes = recipes;
		this.success = success;
	}

	// Getters
	public boolean isSuccess() {
		return success;
	}
	public Set<Recipe> getRecipes() {
		return recipes;
	}
}
