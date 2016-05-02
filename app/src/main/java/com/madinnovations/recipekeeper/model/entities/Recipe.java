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

import android.support.annotation.NonNull;

import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Class representing a cooking Recipe
 */
public class Recipe {
	private long id = DataConstants.UNINITIALIZED;
	private String           name;
	private String           description;
	private String			 directions;
	private Set<Category>    categories = new HashSet<>();
	private Set<Ingredient> ingredients = new HashSet<>();
	private String   notes;
	private String   source;
	private Calendar created;
	private Calendar updated;

	/**
	 * Creates a new Recipe instance
	 */
	public Recipe() {
		super();
		this.created = Calendar.getInstance();
		this.updated = this.created;
	}

	/**
	 * Creates a new Recipe instance with the name initialized with the given argument
	 *
	 * @param name  the value to use to initialize the name member variable
	 */
	public Recipe(@NonNull String name) {
		this.name = name;
		this.created = Calendar.getInstance();
		this.updated = this.created;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		Recipe recipe = (Recipe) o;

		return id == recipe.id;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	@Override
	public String toString() {
		return "Recipe{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", directions='" + directions + '\'' +
				", categories=" + categories +
				", ingredients=" + ingredients +
				", notes='" + notes + '\'' +
				", source='" + source + '\'' +
				", created=" + created +
				", updated=" + updated +
				'}';
	}

	/**
	 * Returns the categories for this recipe as a comma separated list of category names
	 *
	 * @return the list of category names
	 */
	public String getCategoriesAsText() {
		int i = 0;
		StringBuilder builder = new StringBuilder();
		for(Category category : getCategories()) {
			builder.append(category.getName());
			i++;
			if(getCategories().size() != i) {
				builder.append(", ");
			}
		}
		return builder.toString();
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDirections() {
		return directions;
	}
	public void setDirections(String directions) {
		this.directions = directions;
	}
	public Set<Category> getCategories() {
		return categories;
	}
	public Set<Ingredient> getIngredients() {
		return ingredients;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Calendar getCreated() {
		return created;
	}
	public void setCreated(Calendar created) {
		this.created = created;
	}
	public Calendar getUpdated() {
		return updated;
	}
	public void setUpdated(Calendar updated) {
		this.updated = updated;
	}
}
