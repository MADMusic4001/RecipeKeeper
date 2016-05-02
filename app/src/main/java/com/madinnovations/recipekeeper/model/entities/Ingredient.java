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
package com.madinnovations.recipekeeper.model.entities;

import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.math.BigDecimal;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/17/2016.
 */
public class Ingredient {
	private long id = DataConstants.UNINITIALIZED;
	private String name;
	private BigDecimal value;
	private UnitOfMeasure unit;
	private Recipe  parent;

	/**
	 * Creates a new Ingredient instance
	 */
	public Ingredient() {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Ingredient that = (Ingredient) o;

		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	@Override
	public String toString() {
		return "Ingredient{" +
				"id=" + id +
				", name='" + name + '\'' +
				", value=" + value +
				", unit=" + unit +
				'}';
	}

	// Getters and setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public UnitOfMeasure getUnit() {
		return unit;
	}
	public void setUnit(UnitOfMeasure unit) {
		this.unit = unit;
	}
	public Recipe getParent() {
		return parent;
	}
	public void setParent(Recipe parent) {
		this.parent = parent;
	}
}
