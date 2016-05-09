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
package com.madinnovations.recipekeeper.model.dao.impl.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.madinnovations.recipekeeper.model.dao.CategoryDao;
import com.madinnovations.recipekeeper.model.dao.IngredientDao;
import com.madinnovations.recipekeeper.model.dao.RecipeDao;
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.model.entities.Ingredient;
import com.madinnovations.recipekeeper.model.entities.Recipe;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the {@link RecipeDao} for maintaining a {@link Recipe} in a SQLite database.
 */
public class RecipeDaoSqlImpl implements BaseDaoSql, RecipeDao {
	public static abstract class RecipeContract implements BaseColumns {
		public static final String TABLE_NAME = "recipes";
		public static final String NAME_COLUMN_NAME = "name";
		public static final String DESCRIPTION_COLUMN_NAME = "description";
		public static final String DIRECTIONS_COLUMN_NAME = "directions";
		public static final String NOTES_COLUMN_NAME = "notes";
		public static final String SOURCE_COLUMN_NAME = "source";
		public static final String CREATED_COLUMN_NAME = "created";
		public static final String UPDATED_COLUMN_NAME = "updated";
	}
	public static final String CREATE_TABLE_RECIPES =
		"CREATE TABLE " + RecipeContract.TABLE_NAME + " (" +
			RecipeContract._ID + " INTEGER NOT NULL PRIMARY KEY, " +
			RecipeContract.NAME_COLUMN_NAME + " TEXT NOT NULL, " +
			RecipeContract.DESCRIPTION_COLUMN_NAME + " TEXT, " +
			RecipeContract.DIRECTIONS_COLUMN_NAME + " TEXT, " +
			RecipeContract.NOTES_COLUMN_NAME + " TEXT, " +
			RecipeContract.SOURCE_COLUMN_NAME + " TEXT, " +
			RecipeContract.CREATED_COLUMN_NAME + " LONG NOT NULL, " +
			RecipeContract.UPDATED_COLUMN_NAME + " LONG NOT NULL);";
	public static abstract class RecipeCategoryContract implements BaseColumns {
		public static final String TABLE_NAME = "recipe_categories";
		public static final String RECIPE_ID_COLUMN_NAME = "recipe_id";
		public static final String CATEGORY_ID_COLUMN_NAME = "category_id";
	}
	private static final String [] RECIPE_COLUMNS = {
			RecipeContract._ID,
			RecipeContract.NAME_COLUMN_NAME,
			RecipeContract.DIRECTIONS_COLUMN_NAME,
			RecipeContract.DESCRIPTION_COLUMN_NAME,
			RecipeContract.NOTES_COLUMN_NAME,
			RecipeContract.SOURCE_COLUMN_NAME,
			RecipeContract.CREATED_COLUMN_NAME,
			RecipeContract.UPDATED_COLUMN_NAME};
	private static final int      ID_INDEX          = 0;
	private static final int      NAME_INDEX        = 1;
	private static final int      DIRECTIONS_INDEX  = 2;
	private static final int      DESCRIPTION_INDEX = 3;
	private static final int      NOTES_INDEX       = 4;
	private static final int      SOURCE_INDEX      = 5;
	private static final int      CREATED_INDEX     = 6;
	private static final int      UPDATED_INDEX     = 7;
	public static final String CREATE_TABLE_RECIPE_CATEGORIES =
		"CREATE TABLE " + RecipeCategoryContract.TABLE_NAME + " (" +
			RecipeCategoryContract._ID + " INTEGER NOT NULL, " +
			RecipeCategoryContract.CATEGORY_ID_COLUMN_NAME + " INTEGER NOT NULL, " +
			RecipeCategoryContract.RECIPE_ID_COLUMN_NAME + " INTEGER NOT NULL, " +
			"PRIMARY KEY (" + RecipeCategoryContract.RECIPE_ID_COLUMN_NAME + ", " +
				RecipeCategoryContract.CATEGORY_ID_COLUMN_NAME + "), " +
			"CONSTRAINT fk_recipe_category_to_recipe FOREIGN KEY (" + RecipeCategoryContract.RECIPE_ID_COLUMN_NAME + ") " +
				"REFERENCES " + RecipeContract.TABLE_NAME + " (" + RecipeContract._ID + ") ON DELETE CASCADE, " +
			"CONSTRAINT fk_recipe_category_to_category FOREIGN KEY (" + RecipeCategoryContract.CATEGORY_ID_COLUMN_NAME + ") " +
				"REFERENCES " + CategoryDaoSqlImpl.CategoryContract.TABLE_NAME +
				" (" + CategoryDaoSqlImpl.CategoryContract._ID + ") ON DELETE CASCADE);";
	public static final String[] RECIPE_CATEGORY_COLUMNS = {
			RecipeCategoryContract._ID,
			RecipeCategoryContract.RECIPE_ID_COLUMN_NAME,
			RecipeCategoryContract.CATEGORY_ID_COLUMN_NAME};
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
		Log.d("RecipeDaoSqlImpl", "Saving recipe" + recipe);
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
			if (recipe.getId() == DataConstants.UNINITIALIZED) {
				recipe.setId(sqlHelper.getWritableDatabase().insert(RecipeContract.TABLE_NAME, null, values));
				Log.e("RecipeDaoSqlImpl", "New recipe id = " + recipe.getId());
				result = (recipe.getId() != DataConstants.UNINITIALIZED);
			}
			else {
				values.put("_id", recipe.getId());
				int count = sqlHelper.getWritableDatabase().update(RecipeContract.TABLE_NAME, values,
																   RecipeContract._ID + EQUALS + PLACEHOLDER,
																   new String[]{Long.valueOf(recipe.getId()).toString()});
				result = (count == 1);
			}
			if (result) {
				result = saveIngredients(recipe);
				if(result) {
					result = saveRecipeCategories(recipe);
				}
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
		boolean result;
		// Delete existing ingredients for this recipe from the database
		Ingredient ingredientFilter = new Ingredient();
		ingredientFilter.setParent(recipe);
		result = ingredientDao.delete(ingredientFilter);
		// Re-save all the ingredients in the Recipe object
		if(result) {
			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.setId(DataConstants.UNINITIALIZED);
				ingredient.setParent(recipe);
				result = ingredientDao.save(ingredient);
				if(!result) {
					break;
				}
			}
		}
		return result;
	}

	private boolean saveRecipeCategories(Recipe recipe) {
		boolean result;
		result = (sqlHelper.getWritableDatabase().delete(RecipeCategoryContract.TABLE_NAME,
			RecipeCategoryContract.RECIPE_ID_COLUMN_NAME + EQUALS + PLACEHOLDER,
			new String[] {Long.valueOf(recipe.getId()).toString()}) >= 0);
		for(Category category : recipe.getCategories()) {
			result = saveRecipeCategory(recipe, category);
			if(!result) {
				break;
			}
		}
		return result;
	}

	private boolean saveRecipeCategory(Recipe recipe, Category category) {
		Log.d("RecipeDaoSqlImpl", "Saving recipe category");
		boolean result;
		ContentValues values = new ContentValues();
		values.put(RecipeCategoryContract.RECIPE_ID_COLUMN_NAME, recipe.getId());
		values.put(RecipeCategoryContract.CATEGORY_ID_COLUMN_NAME, category.getId());

		result = (sqlHelper.getWritableDatabase().insert(RecipeCategoryContract.TABLE_NAME, null, values) != -1);
		return result;
	}
	@Override
	public boolean delete(Recipe filter) {
		Log.d("RecipeDaoSqlImpl", "Deleting recipes using filter " + filter);
		boolean result = false;
		List<String> whereArgsList = new ArrayList<>();
		String whereClause = buildWhereArgs(filter, whereArgsList);
		String[] whereArgs = new String[whereArgsList.size()];

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			result = (sqlHelper.getWritableDatabase().delete(RecipeContract.TABLE_NAME, whereClause, whereArgsList.toArray(whereArgs)) >= 0);
			sqlHelper.getWritableDatabase().setTransactionSuccessful();
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.d("RecipeDaoSqlImpl", "Delete " + (result ? "succeeded" : "failed"));
		return result;
	}

	@Override
	public Set<Recipe> read(Recipe filter) {
		Log.e("RecipeDaoSqlImpl", "Reading recipes filtered by " + filter);
		Set<Recipe> result = new HashSet<>();
		List<String> whereArgsList = new ArrayList<>();
		String whereClause = buildWhereArgs(filter, whereArgsList);
		String[] whereArgs = new String[whereArgsList.size()];

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			Cursor cursor = sqlHelper.getWritableDatabase().query(RecipeContract.TABLE_NAME, RECIPE_COLUMNS, whereClause,
																  whereArgsList.toArray(whereArgs), null, null, null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				Recipe aRecipe = createRecipeInstance(cursor);
				result.add(aRecipe);
				cursor.moveToNext();
			}
			cursor.close();
			sqlHelper.getWritableDatabase().setTransactionSuccessful();
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.e("RecipeDaoSqlImpl", "Loaded " + result.size() + " recipes.");
		return result;
	}

	@Override
	public Recipe read(long id) {
		Log.d("RecipeDaoSqlImpl", "Reading recipe for id = " + id);
		Recipe result = null;
		String whereClause = RecipeContract._ID + EQUALS + PLACEHOLDER;
		String[] whereArgs = new String[1];
		whereArgs[0] = Long.valueOf(id).toString();

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			Cursor cursor = sqlHelper.getWritableDatabase().query(true, RecipeContract.TABLE_NAME, RECIPE_COLUMNS, whereClause,
					whereArgs, null, null, null, null);
			if(cursor.moveToFirst()) {
				result = createRecipeInstance(cursor);
			}
			cursor.close();
			sqlHelper.getWritableDatabase().setTransactionSuccessful();
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.d("RecipeDaoSqlImpl", "Loaded " + result);
		return result;
	}

	private String buildWhereArgs(Recipe filter, Collection<String> args) {
		boolean isFirst = true;
		String whereClause = "";
		if(filter != null) {
			if(filter.getId() != DataConstants.UNINITIALIZED) {
				whereClause = addFilter(whereClause, RecipeContract._ID, Long.valueOf(filter.getId()).toString(), true, args);
			} else {
				if (filter.getName() != null && !filter.getName().isEmpty()) {
					whereClause = addFilter(whereClause, RecipeContract.NAME_COLUMN_NAME, filter.getName(), true, args);
					isFirst = false;
				}
				if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
					whereClause = addFilter(whereClause, RecipeContract.DESCRIPTION_COLUMN_NAME, filter.getDescription(), isFirst,
							args);
					isFirst = false;
				}
				if (filter.getDirections() != null && !filter.getDirections().isEmpty()) {
					whereClause = addFilter(whereClause, RecipeContract.DIRECTIONS_COLUMN_NAME, filter.getDirections(), isFirst,
							args);
					isFirst = false;
				}
				if (filter.getNotes() != null && !filter.getNotes().isEmpty()) {
					whereClause = addFilter(whereClause, RecipeContract.NOTES_COLUMN_NAME, filter.getNotes(), isFirst,
							args);
					isFirst = false;
				}
				if (filter.getSource() != null && !filter.getSource().isEmpty()) {
					whereClause = addFilter(whereClause, RecipeContract.SOURCE_COLUMN_NAME, filter.getSource(), isFirst,
							args);
					isFirst = false;
				}
				if (filter.getCreated() != null) {
					whereClause = addFilter(whereClause, RecipeContract.CREATED_COLUMN_NAME,
							Long.valueOf(filter.getCreated().getTimeInMillis()).toString(), isFirst, args);
					isFirst = false;
				}
				if (filter.getUpdated() != null) {
					whereClause = addFilter(whereClause, RecipeContract.UPDATED_COLUMN_NAME,
							Long.valueOf(filter.getUpdated().getTimeInMillis()).toString(), isFirst, args);
				}
			}
		}
		return whereClause;
	}

	private String addFilter(String whereClause, String fieldName, String value, boolean isFirst, Collection<String> args) {
		if(!isFirst) {
			whereClause = whereClause + AND + SPACE;
		}
		whereClause = whereClause + fieldName + EQUALS + PLACEHOLDER;
		args.add(value);
		return whereClause;
	}

	private Recipe createRecipeInstance(Cursor cursor) {
		Calendar calendar;

		Recipe aRecipe = new Recipe();
		aRecipe.setId(cursor.getLong(ID_INDEX));
		aRecipe.setName(cursor.getString(NAME_INDEX));
		aRecipe.setDirections(cursor.getString(DIRECTIONS_INDEX));
		aRecipe.setDescription(cursor.getString(DESCRIPTION_INDEX));
		aRecipe.setNotes(cursor.getString(NOTES_INDEX));
		aRecipe.setSource(cursor.getString(SOURCE_INDEX));
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(cursor.getLong(CREATED_INDEX));
		aRecipe.setCreated(calendar);
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(cursor.getLong(UPDATED_INDEX));
		aRecipe.setUpdated(calendar);

		return aRecipe;
	}
}
