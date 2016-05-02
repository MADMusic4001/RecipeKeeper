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

import com.madinnovations.recipekeeper.model.entities.Category;

import java.util.Set;

/**
 * Contains operations to read/write/delete {@link Category} instances from persistent storage.
 */
public interface CategoryDao {
	/**
	 * Saves a new or existing Category to persistent storage.
	 *
	 * @param category  the Category instance to save
	 * @return  true if the Category was saved successfully, otherwise false.
	 */
	public boolean save(Category category);

	/**
	 * Deletes a UnitOfMeasure instance from persistent storage.
	 *
	 * @param category  the Category instance to delete
	 * @return true if the Category was successfully deleted, otherwise false.
	 */
	public boolean delete(Category category);

	/**
	 * Reads 0 or more Category instances from persistent storage.
	 *
	 * @param filter  a Category instance containing values to filter the search by
	 * @return  a Set of Category instances (empty if none were found)
	 */
	public Set<Category> read(Category filter);

	/**
	 * Reads a single Category instance from persistent storage.
	 *
	 * @param id  the unique id of the Category to read
	 * @return  the Category with matching id or null if not found.
	 */
	public Category read(int id);
}
