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
package com.madinnovations.recipekeeper.model.dao;

import com.madinnovations.recipekeeper.model.entities.Recipe;

import java.util.Set;

/**
 * Contains operations to read/write/delete {@link Recipe} instances from persistent storage.
 */
public interface RecipeDao {
	/**
	 * Saves a new or existing Recipe to persistent storage.
	 *
	 * @param recipe  the Recipe instance to save
	 * @return  true if the Recipe was saved successfully, otherwise false.
	 */
	public boolean save(Recipe recipe);

	/**
	 * Deletes a Recipe instance from persistent storage.
	 *
	 * @param recipe  the Recipe instance to delete
	 * @return true if the Recipe was successfully deleted, otherwise false.
	 */
	public boolean delete(Recipe recipe);

	/**
	 * Reads 0 or more Recipe instances from persistent storage.
	 *
	 * @param filter  a Recipe instance containing values to filter the search by
	 * @return  a Set of Recipe instances (empty if none were found)
	 */
	public Set<Recipe> read(Recipe filter);

	/**
	 * Reads a single Recipe instance from persistent storage.
	 *
	 * @param id  the unique id of the Recipe to read
	 * @return  the Recipe with matching id or null if not found.
	 */
	public Recipe read(int id);
}
