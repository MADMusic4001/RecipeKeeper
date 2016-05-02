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
import android.provider.BaseColumns;
import android.util.Log;

import com.madinnovations.recipekeeper.model.dao.IngredientDao;
import com.madinnovations.recipekeeper.model.entities.Ingredient;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Implementation of the {@link IngredientDao} for maintaining a {@link Ingredient} in a SQLite database.
 */
public class IngredientDaoSqlImpl implements BaseDaoSql, IngredientDao {
	public static abstract class IngredientContract implements BaseColumns {
		public static final String TABLE_NAME = "ingredients";
		public static final String NAME_COLUMN_NAME = "name";
		public static final String VALUE_COLUMN_NAME = "value";
		public static final String UOM_ID_COLUMN_NAME = "uom_id";
		public static final String RECIPE_ID_COLUMN_NAME = "recipe_id";
	}
	public static final String CREATE_TABLE_INGREDIENTS =
			"CREATE TABLE " + IngredientContract.TABLE_NAME + " (" +
				IngredientContract._ID + " INTEGER NOT NULL PRIMARY KEY, " +
				IngredientContract.NAME_COLUMN_NAME + " TEXT NOT NULL, " +
				IngredientContract.VALUE_COLUMN_NAME + " REAL NOT NULL, " +
				IngredientContract.UOM_ID_COLUMN_NAME + " INTEGER NOT NULL, " +
				IngredientContract.RECIPE_ID_COLUMN_NAME + " INTEGER NOT NULL, " +
				"CONSTRAINT fk_ingredient_to_uom FOREIGN KEY (" + IngredientContract.UOM_ID_COLUMN_NAME + ") " +
					"REFERENCES " + UnitOfMeasureDaoSqlImpl.UnitOfMeasureContract.TABLE_NAME +
					" (" +UnitOfMeasureDaoSqlImpl.UnitOfMeasureContract._ID + ") ON DELETE RESTRICT, " +
				"CONSTRAINT fk_ingredient_to_recipe FOREIGN KEY (" + IngredientContract.RECIPE_ID_COLUMN_NAME + ") " +
					"REFERENCES " + RecipeDaoSqlImpl.RecipeContract.TABLE_NAME +
					" (" + RecipeDaoSqlImpl.RecipeContract._ID + ") ON DELETE CASCADE);";
	private RecipeKeeperSqlHelper sqlHelper;

	/**
	 * Creates a IngredientDaoSqlImp instance with the given {@link RecipeKeeperSqlHelper}
	 *
	 * @param sqlHelper  the RecipeKeeperSqlHelper to use
	 */
	public IngredientDaoSqlImpl(RecipeKeeperSqlHelper sqlHelper) {
		this.sqlHelper = sqlHelper;
	}

	@Override
	public boolean save(Ingredient ingredient) {
		boolean result;
		ContentValues values = new ContentValues();
		values.put(IngredientContract.NAME_COLUMN_NAME, ingredient.getName());
		values.put(IngredientContract.VALUE_COLUMN_NAME, ingredient.getValue().toPlainString());
		values.put(IngredientContract.UOM_ID_COLUMN_NAME, ingredient.getUnit().getId());
		values.put(IngredientContract.RECIPE_ID_COLUMN_NAME, ingredient.getParent().getId());

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			if (ingredient.getId() == DataConstants.UNINITIALIZED) {
				ingredient.setId(sqlHelper.getWritableDatabase().insert(IngredientContract.TABLE_NAME, null, values));
				result = (ingredient.getId() != DataConstants.UNINITIALIZED);
			}
			else {
				values.put(IngredientContract._ID, ingredient.getId());
				int count = sqlHelper.getWritableDatabase().update(IngredientContract.TABLE_NAME, values,
																   WHERE + SPACE + IngredientContract._ID + EQUALS + PLACEHOLDER,
																   new String[]{Long.valueOf(ingredient.getId()).toString()});
				result = (count == 1);
			}
			if(result) {
				sqlHelper.getWritableDatabase().setTransactionSuccessful();
			}
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		return result;
	}

	@Override
	public boolean delete(Ingredient ingredient) {
		boolean result = false;
		String whereClause = null;
		ArrayList<String> whereArgsList = new ArrayList<>();
		String[] whereArgs;

		whereClause = setWhereData(ingredient, whereArgsList);
		whereArgs = new String[whereArgsList.size()];

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			result = (sqlHelper.getWritableDatabase().delete(IngredientContract.TABLE_NAME, whereClause,
												   whereArgsList.toArray(whereArgs)) >= 0);
			if(result) {
				sqlHelper.getWritableDatabase().setTransactionSuccessful();
			}
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		return result;
	}

	@Override
	public Set<Ingredient> read(Ingredient filter) {
		return null;
	}

	@Override
	public Ingredient read(int id) {
		return null;
	}

	private String setWhereData(Ingredient filter, Collection<String> args) {
		boolean isFirst = true;
		String whereClause = "";

		if(filter != null) {
			if(filter.getId() != DataConstants.UNINITIALIZED) {
				whereClause = addFilter(whereClause, IngredientContract._ID,
										Long.valueOf(filter.getId()).toString(), isFirst, args);
				isFirst = false;
			}
			if(filter.getName() != null && !filter.getName().isEmpty()) {
				whereClause = addFilter(whereClause, IngredientContract.NAME_COLUMN_NAME, filter.getName(), isFirst, args);
				isFirst = false;
			}
			if(filter.getValue() != null) {
				whereClause = addFilter(whereClause, IngredientContract.VALUE_COLUMN_NAME, filter.getValue().toPlainString(),
										isFirst, args);
				isFirst = false;
			}
			if(filter.getUnit() != null && filter.getUnit().getId() != DataConstants.UNINITIALIZED) {
				whereClause = addFilter(whereClause, IngredientContract.UOM_ID_COLUMN_NAME,
										Long.valueOf(filter.getUnit().getId()).toString(), isFirst, args);
				isFirst = false;
			}
			if(filter.getParent() != null && filter.getParent().getId() != DataConstants.UNINITIALIZED) {
				whereClause = addFilter(whereClause, IngredientContract.RECIPE_ID_COLUMN_NAME,
										Long.valueOf(filter.getParent().getId()).toString(), isFirst, args);
				isFirst = false;
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
}
