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
package com.madinnovations.recipekeeper.controller.events.unitofmeasure;

import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;

/**
 * Event representing a request to perform an action in persistent storage for a UnitOfMeasure.
 */
public class UnitOfMeasurePersistenceEvent {
	public enum Action {
		SAVE,
		READ_BY_ID,
		READ_BY_FILTER,
		DELETE
	}
	private Action action;
	private UnitOfMeasure unitOfMeasure;

	/**
	 * Creates a UnitOfMeasurePersistenceEvent instance with the given action and UnitOfMeasure.
	 *
	 * @param action  the action to take
	 * @param unitOfMeasure  a UnitOfMeasure to take action on or use as a filter
     */
	public UnitOfMeasurePersistenceEvent(
			Action action, UnitOfMeasure unitOfMeasure) {
		this.action = action;
		this.unitOfMeasure = unitOfMeasure;
	}

	// Getters
	public Action getAction() {
		return action;
	}
	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}
}
