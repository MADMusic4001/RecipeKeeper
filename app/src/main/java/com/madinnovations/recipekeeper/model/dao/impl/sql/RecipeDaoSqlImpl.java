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
package com.madinnovations.recipekeeper.model.dao.impl.sql;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.madinnovations.recipekeeper.model.dao.CategoryDao;
import com.madinnovations.recipekeeper.model.dao.IngredientDao;
import com.madinnovations.recipekeeper.model.dao.RecipeDao;
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.model.entities.Ingredient;
import com.madinnovations.recipekeeper.model.entities.Recipe;

import java.util.Set;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/28/2016.
 */
public class RecipeDaoSqlImpl implements BaseDaoSqlImpl, RecipeDao {
	private static abstract class RecipeContract implements BaseColumns {
		public static final String TABLE_NAME = "recipes";
		public static final String NAME_COLUMN_NAME = "name";
		public static final String DESCRIPTION_COLUMN_NAME = "description";
		public static final String DIRECTIONS_COLUMN_NAME = "directions";
		public static final String NOTES_COLUMN_NAME = "notes";
		public static final String SOURCE_COLUMN_NAME = "source";
		public static final String CREATED_COLUMN_NAME = "created";
		public static final String UPDATED_COLUMN_NAME = "updated";
	}
	private static abstract class RecipeCategoryContract implements BaseColumns {
		public static final String TABLE_NAME = "recipe_categories";
		public static final String RECIPE_ID_COLUMN_NAME = "recipe_id";
		public static final String CATEGORY_ID_COLUMN_NAME = "category_id";
	}
	private RecipeKeeperSqlHelper sqlHelper;
	private CategoryDao categoryDao;
	private IngredientDao ingredientDao;

	/**
	 * Creates a RecipeDaoSqlImpl instance with the given {@link RecipeKeeperSqlHelper}, {@link CategoryDao} and {@link IngredientDao}
	 *
	 * @param sqlHelper  the RecipeKeeperSqlHelper to use
	 * @param categoryDao  a CategoryDao instance
	 * @param ingredientDao   an IngredientDao instance
	 */
	public RecipeDaoSqlImpl(RecipeKeeperSqlHelper sqlHelper, CategoryDao categoryDao,
							IngredientDao ingredientDao) {
		this.sqlHelper = sqlHelper;
		this.categoryDao = categoryDao;
		this.ingredientDao = ingredientDao;
	}

	@Override
	public boolean save(Recipe recipe) {
		boolean result;
		ContentValues values = new ContentValues();
		values.put(RecipeContract.NAME_COLUMN_NAME, recipe.getName());
		values.put(RecipeContract.DESCRIPTION_COLUMN_NAME, recipe.getDescription());
		values.put(RecipeContract.DIRECTIONS_COLUMN_NAME, recipe.getDirections());
		values.put(RecipeContract.NOTES_COLUMN_NAME, recipe.getNotes());
		values.put(RecipeContract.SOURCE_COLUMN_NAME, recipe.getSource());
		values.put(RecipeContract.DESCRIPTION_COLUMN_NAME, recipe.getDescription());
		values.put(RecipeContract.CREATED_COLUMN_NAME, recipe.getCreated().getTimeInMillis());
		values.put(RecipeContract.UPDATED_COLUMN_NAME, recipe.getUpdated().getTimeInMillis());

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			if (recipe.getId() == UNINITIALIZED) {
				recipe.setId(sqlHelper.getWritableDatabase().insert(RecipeContract.TABLE_NAME, null, values));
				result = (recipe.getId() != UNINITIALIZED);
			}
			else {
				values.put("_id", recipe.getId());
				int count = sqlHelper.getWritableDatabase().update(RecipeContract.TABLE_NAME, values,
																   WHERE + SPACE + RecipeContract._ID + EQUALS + PLACEHOLDER,
																   new String[]{Long.valueOf(recipe.getId()).toString()});
				result = (count == 1);
			}
			if (result) {
			}
			if(result) {
				sqlHelper.getWritableDatabase().setTransactionSuccessful();
			}
		} finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		return result;
	}

	private boolean saveIngredients(Recipe recipe) {
		boolean result = false;
		// Delete existing ingredients for this recipe from the database
		Ingredient ingredientFilter = new Ingredient();
		ingredientFilter.setParent(recipe);
		result = ingredientDao.delete(ingredientFilter);
		// Re-save all the ingredients in the Recipe object
		if(result) {
			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.setId(BaseDaoSqlImpl.UNINITIALIZED);
				ingredient.setParent(recipe);
				result &= ingredientDao.save(ingredient);
				if(!result) {
					break;
				}
			}
		}
		return result;
	}

	private boolean saveRecipeCategories(Recipe recipe) {
		boolean result = false;
		sqlHelper.getWritableDatabase().delete(RecipeCategoryContract.TABLE_NAME,
			WHERE + SPACE + RecipeCategoryContract.RECIPE_ID_COLUMN_NAME + EQUALS + PLACEHOLDER,
			new String[] {Long.valueOf(recipe.getId()).toString()});
		for(Category category : recipe.getCategories()) {
			result &= saveRecipeCategory(recipe, category);
			if(!result) {
				break;
			}
		}
		return result;
	}

	private boolean saveRecipeCategory(Recipe recipe, Category category) {
		boolean result = false;
		ContentValues values = new ContentValues();
		values.put(RecipeCategoryContract.RECIPE_ID_COLUMN_NAME, recipe.getId());
		values.put(RecipeCategoryContract.CATEGORY_ID_COLUMN_NAME, category.getId());

		sqlHelper.getWritableDatabase().insert(RecipeCategoryContract.TABLE_NAME, null, values);
		return result;
	}
	@Override
	public boolean delete(Recipe recipe) {
		return false;
	}

	@Override
	public Set<Recipe> read(Recipe filter) {
		return null;
	}

	@Override
	public Recipe read(int id) {
		return null;
	}
}
