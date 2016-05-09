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
package com.madinnovations.recipekeeper.controller.events;

import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;

/**
 * Event signifying the result of a request to delete a UnitOfMeasure instance from persistent storage.
 */
public class UnitOfMeasureDeletedEvent {
	private boolean successful;
	private UnitOfMeasure unitOfMeasure;

	/**
	 * Creates a UnitOfMeasureDeletedEvent instance with the given status and the filter for the instances that were to be
	 * deleted.
	 *
	 * @param successful  true if the delete was successful, otherwise false
	 * @param unitOfMeasure  the filter for the instances to be deleted
	 */
	public UnitOfMeasureDeletedEvent(boolean successful, UnitOfMeasure unitOfMeasure) {
		this.successful = successful;
		this.unitOfMeasure = unitOfMeasure;
	}

	// Getters
	public boolean isSuccessful() {
		return successful;
	}
	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}
}
