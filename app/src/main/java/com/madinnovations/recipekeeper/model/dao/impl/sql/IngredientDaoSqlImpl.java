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

import com.madinnovations.recipekeeper.model.dao.IngredientDao;
import com.madinnovations.recipekeeper.model.entities.Ingredient;
import com.madinnovations.recipekeeper.model.entities.Recipe;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
			CREATE_TABLE + IngredientContract.TABLE_NAME + " (" +
				IngredientContract._ID + INTEGER + NOT_NULL + PRIMARY_KEY + COMMA +
				IngredientContract.NAME_COLUMN_NAME + TEXT + NOT_NULL + COMMA +
				IngredientContract.VALUE_COLUMN_NAME + TEXT + NOT_NULL + COMMA +
				IngredientContract.UOM_ID_COLUMN_NAME + INTEGER + NOT_NULL + COMMA +
				IngredientContract.RECIPE_ID_COLUMN_NAME + INTEGER + NOT_NULL + COMMA +
				CONSTRAINT + "fk_ingredient_to_uom" + FOREIGN_KEY + "(" + IngredientContract.UOM_ID_COLUMN_NAME + ") " +
					REFERENCES + UnitOfMeasureDaoSqlImpl.UnitOfMeasureContract.TABLE_NAME +
					" (" +UnitOfMeasureDaoSqlImpl.UnitOfMeasureContract._ID + ")" + ON + DELETE + RESTRICT + COMMA +
				CONSTRAINT + "fk_ingredient_to_recipe" + FOREIGN_KEY + "(" + IngredientContract.RECIPE_ID_COLUMN_NAME + ") " +
					REFERENCES + RecipeDaoSqlImpl.RecipeContract.TABLE_NAME +
					" (" + RecipeDaoSqlImpl.RecipeContract._ID + ")" + ON + DELETE + CASCADE + ");";
	private static final String [] INGREDIENT_COLUMNS = {
			IngredientContract._ID,
			IngredientContract.NAME_COLUMN_NAME,
			IngredientContract.VALUE_COLUMN_NAME,
			IngredientContract.UOM_ID_COLUMN_NAME,
			IngredientContract.RECIPE_ID_COLUMN_NAME};
	private static final int      ID_INDEX          = 0;
	private static final int      NAME_INDEX        = 1;
	private static final int      VALUE_INDEX  		= 2;
	private static final int      UOM_ID_INDEX 		= 3;
	private static final int      RECIPE_ID_INDEX   = 4;
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

		whereClause = buildWhereArgs(ingredient, whereArgsList);
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
		Log.d("IngredientDaoSqlImpl", "Reading ingredients filtered by " + filter);
		Set<Ingredient> result = new HashSet<>();
		List<String> whereArgsList = new ArrayList<>();
		String whereClause = buildWhereArgs(filter, whereArgsList);
		String[] whereArgs = new String[whereArgsList.size()];

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			Cursor cursor = sqlHelper.getWritableDatabase().query(IngredientContract.TABLE_NAME, INGREDIENT_COLUMNS, whereClause,
					whereArgsList.toArray(whereArgs), null, null, null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				Ingredient anIngredient = createIngredientInstance(cursor);
				result.add(anIngredient);
				cursor.moveToNext();
			}
			cursor.close();
			sqlHelper.getWritableDatabase().setTransactionSuccessful();
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.d("IngredientDaoSqlImpl", "Loaded " + result);
		return result;
	}

	@Override
	public Ingredient read(long id) {
		return null;
	}

	private String buildWhereArgs(Ingredient filter, Collection<String> args) {
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

	private Ingredient createIngredientInstance(Cursor cursor) {
		Ingredient anIngredient = new Ingredient();
		anIngredient.setId(cursor.getLong(ID_INDEX));
		anIngredient.setName(cursor.getString(NAME_INDEX));
		anIngredient.setValue(new BigDecimal(cursor.getString(VALUE_INDEX)));

		return anIngredient;
	}
}
