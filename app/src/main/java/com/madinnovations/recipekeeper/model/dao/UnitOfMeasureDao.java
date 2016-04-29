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

import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;

import java.util.Set;

/**
 * Contains operations to read/write/delete {@link UnitOfMeasure} instances from persistent storage.
 */
public interface UnitOfMeasureDao {
	/**
	 * Saves a new or existing UnitOfMeasure to persistent storage.
	 *
	 * @param uom  the UnitOfMeasure instance to save
	 * @return  true if the UnitOfMeasure was saved successfully, otherwise false.
	 */
	public boolean save(UnitOfMeasure uom);

	/**
	 * Deletes a UnitOfMeasure instance from persistent storage.
	 *
	 * @param uom  the UnitOfMeasure instance to delete
	 * @return true if the UnitOfMeasure was successfully deleted, otherwise false.
	 */
	public boolean delete(UnitOfMeasure uom);

	/**
	 * Reads 0 or more UnitOfMeasure instances from persistent storage.
	 *
	 * @param filter  a UnitOfMeasure instance containing values to filter the search by
	 * @return  a Set of UnitOfMeasure instances (empty if none were found)
	 */
	public Set<UnitOfMeasure> read(UnitOfMeasure filter);

	/**
	 * Reads a single UnitOfMeasure instance from persistent storage.
	 *
	 * @param id  the unique id of the UnitOfMeasure to read
	 * @return  the UnitOfMeasure with matching id or null if not found.
	 */
	public UnitOfMeasure read(int id);
}
