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

import com.madinnovations.recipekeeper.model.dao.IngredientDao;
import com.madinnovations.recipekeeper.model.entities.Ingredient;

import java.util.Set;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/28/2016.
 */
public class IngredientDaoSqlImp implements BaseDaoSqlImpl, IngredientDao {
	private static abstract class IngredientContract implements BaseColumns {
		public static final String TABLE_NAME = "ingredients";
		public static final String NAME_COLUMN_NAME = "name";
		public static final String VALUE_COLUMN_NAME = "value";
		public static final String UOM_ID_COLUMN_NAME = "uom_id";
		public static final String RECIPE_ID_COLUMN_NAME = "recipe_id";
	}
	private RecipeKeeperSqlHelper sqlHelper;

	/**
	 * Creates a IngredientDaoSqlImp instance with the given {@link RecipeKeeperSqlHelper}
	 *
	 * @param sqlHelper  the RecipeKeeperSqlHelper to use
	 */
	public IngredientDaoSqlImp(RecipeKeeperSqlHelper sqlHelper) {
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

		sqlHelper.getWritableDatabase().beginTransaction();
		if(ingredient.getId() == UNINITIALIZED) {
			ingredient.setId(sqlHelper.getWritableDatabase().insert(IngredientContract.TABLE_NAME, null, values));
			result = (ingredient.getId() != UNINITIALIZED);
		}
		else {
			values.put(IngredientContract._ID, ingredient.getId());
			int count = sqlHelper.getWritableDatabase().update(IngredientContract.TABLE_NAME, values,
							WHERE + SPACE + IngredientContract._ID + EQUALS + PLACEHOLDER,
							new String[]{Long.valueOf(ingredient.getId()).toString()});
			result = (count == 1);
		}
		return result;
	}

	@Override
	public boolean delete(Ingredient ingredient) {
		return false;
	}

	@Override
	public Set<Ingredient> read(Ingredient filter) {
		return null;
	}

	@Override
	public Ingredient read(int id) {
		return null;
	}
}
