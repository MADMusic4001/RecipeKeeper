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
import com.madinnovations.recipekeeper.model.entities.Category;

import java.util.Set;

import javax.inject.Singleton;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/28/2016.
 */
@Singleton
public class CategoryDaoSqlImpl implements BaseDaoSqlImpl, CategoryDao {
	private static abstract class CategoryContract implements BaseColumns {
		public static final String TABLE_NAME = "categories";
		public static final String NAME_COLUMN_NAME = "name";
		public static final String DESCRIPTION_COLUMN_NAME = "description";
	}
	private RecipeKeeperSqlHelper sqlHelper;

	/**
	 * Creates a CategoryDaoSqlImpl instance with the given {@link RecipeKeeperSqlHelper}
	 *
	 * @param sqlHelper  the RecipeKeeperSqlHelper to use
	 */
	public CategoryDaoSqlImpl(RecipeKeeperSqlHelper sqlHelper) {
		this.sqlHelper = sqlHelper;
	}

	@Override
	public boolean save(Category category) {
		boolean result;
		ContentValues values = new ContentValues();
		values.put(CategoryContract.NAME_COLUMN_NAME, category.getName());
		values.put(CategoryContract.DESCRIPTION_COLUMN_NAME, category.getDescription());

		sqlHelper.getWritableDatabase().beginTransaction();
		if(category.getId() == UNINITIALIZED) {
			category.setId(sqlHelper.getWritableDatabase().insert(CategoryContract.TABLE_NAME, null, values));
			result = (category.getId() != UNINITIALIZED);
		}
		else {
			values.put("_id", category.getId());
			int count = sqlHelper.getWritableDatabase().update(CategoryContract.TABLE_NAME, values,
							WHERE + SPACE + CategoryContract._ID + EQUALS + PLACEHOLDER,
							new String[]{Long.valueOf(category.getId()).toString()});
			result = (count == 1);
		}
		return result;
	}

	@Override
	public boolean delete(Category uom) {
		return false;
	}

	@Override
	public Set<Category> read(Category filter) {
		return null;
	}

	@Override
	public Category read(int id) {
		return null;
	}
}
