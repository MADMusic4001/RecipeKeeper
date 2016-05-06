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
 * Event indicating a {@link UnitOfMeasure} was selected from the list of recipes.
 */
public class UnitOfMeasureSelectedEvent {
	private UnitOfMeasure unitOfMeasure;

	/**
	 * Creates a new UnitOfMeasureSelectedEvent with the given UnitOfMeasure
	 *
	 * @param unitOfMeasure  the selected UnitOfMeasure instance
	 */
	public UnitOfMeasureSelectedEvent(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	// Getter
	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}
}
