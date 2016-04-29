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
package com.madinnovations.recipekeeper.model.dao;

import com.madinnovations.recipekeeper.model.entities.Ingredient;

import java.util.Set;

/**
 * Contains operations to read/write/delete {@link Ingredient} instances from persistent storage.
 */
public interface IngredientDao {
	/**
	 * Saves a new or existing Ingredient to persistent storage.
	 *
	 * @param ingredient  the Ingredient instance to save
	 * @return  true if the Ingredient was saved successfully, otherwise false.
	 */
	public boolean save(Ingredient ingredient);

	/**
	 * Deletes a Ingredient instance from persistent storage.
	 *
	 * @param ingredient  the Ingredient instance to delete
	 * @return true if the Ingredient was successfully deleted, otherwise false.
	 */
	public boolean delete(Ingredient ingredient);

	/**
	 * Reads 0 or more Ingredient instances from persistent storage.
	 *
	 * @param filter  a Ingredient instance containing values to filter the search by
	 * @return  a Set of Ingredient instances (empty if none were found)
	 */
	public Set<Ingredient> read(Ingredient filter);

	/**
	 * Reads a single Ingredient instance from persistent storage.
	 *
	 * @param id  the unique id of the Ingredient to read
	 * @return  the Ingredient with matching id or null if not found.
	 */
	public Ingredient read(int id);
}
