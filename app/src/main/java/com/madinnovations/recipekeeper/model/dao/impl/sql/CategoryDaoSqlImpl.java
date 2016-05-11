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
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

/**
 * Implementation of the {@link CategoryDao} for maintaining a {@link Category} in a SQLite database.
 */
@Singleton
public class CategoryDaoSqlImpl implements BaseDaoSql, CategoryDao {
	public static abstract class CategoryContract implements BaseColumns {
		public static final String TABLE_NAME = "categories";
		public static final String NAME_COLUMN_NAME = "name";
		public static final String DESCRIPTION_COLUMN_NAME = "description";
	}
	public static final String CREATE_TABLE_CATEGORIES =
			"CREATE TABLE " + CategoryContract.TABLE_NAME + " (" +
					CategoryContract._ID + " INTEGER NOT NULL PRIMARY KEY, " +
					CategoryContract.NAME_COLUMN_NAME + " TEXT NOT NULL, " +
					CategoryContract.DESCRIPTION_COLUMN_NAME + " TEXT, " +
					"CONSTRAINT unique_category_name UNIQUE (" + CategoryContract.NAME_COLUMN_NAME + "));";
	private static final String [] CATEGORY_COLUMNS = {
			CategoryContract._ID,
			CategoryContract.NAME_COLUMN_NAME,
			CategoryContract.DESCRIPTION_COLUMN_NAME};
	private static final int      ID_INDEX          	= 0;
	private static final int      NAME_INDEX   			= 1;
	private static final int      DESCRIPTION_INDEX  	= 2;
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
		try {
			if (category.getId() == DataConstants.UNINITIALIZED) {
				category.setId(sqlHelper.getWritableDatabase().insert(CategoryContract.TABLE_NAME, null, values));
				result = (category.getId() != DataConstants.UNINITIALIZED);
			}
			else {
				values.put("_id", category.getId());
				int count = sqlHelper.getWritableDatabase().update(CategoryContract.TABLE_NAME, values,
																   WHERE + SPACE + CategoryContract._ID + EQUALS + PLACEHOLDER,
																   new String[]{Long.valueOf(category.getId()).toString()});
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
	public boolean delete(Category filter) {
		Log.d("CategoryDaoSqlImpl", "Deleting Category records using filter " + filter);
		boolean result = false;
		List<String> whereArgsList = new ArrayList<>();
		String whereClause = buildWhereArgs(filter, whereArgsList);
		String[] whereArgs = new String[whereArgsList.size()];

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			result = (sqlHelper.getWritableDatabase().delete(CategoryContract.TABLE_NAME, whereClause,
					whereArgsList.toArray(whereArgs)) >= 0);
			sqlHelper.getWritableDatabase().setTransactionSuccessful();
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.d("CategoryDaoSqlImpl", "Delete " + (result ? "succeeded" : "failed"));
		return result;
	}

	@Override
	public Set<Category> read(Category filter) {
		Log.e("CategoryDaoSqlImpl", "Reading UnitOfMeasure records filtered by " + filter);
		Set<Category> result = new HashSet<>();
		List<String> whereArgsList = new ArrayList<>();
		String whereClause = buildWhereArgs(filter, whereArgsList);
		String[] whereArgs = new String[whereArgsList.size()];

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			Cursor cursor = sqlHelper.getWritableDatabase().query(CategoryContract.TABLE_NAME, CATEGORY_COLUMNS,
					whereClause, whereArgsList.toArray(whereArgs), null, null,
					null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				Category category = createCategoryInstance(cursor);
				result.add(category);
				cursor.moveToNext();
			}
			cursor.close();
			sqlHelper.getWritableDatabase().setTransactionSuccessful();
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.e("CategoryDaoSqlImpl", "Loaded " + result.size() + " Category records.");
		return result;
	}

	@Override
	public Category read(long id) {
		Log.d("CategoryDaoSqlImpl", "Reading Category for id = " + id);
		Category result = null;
		String whereClause = CategoryContract._ID + EQUALS + PLACEHOLDER;
		String[] whereArgs = new String[1];
		whereArgs[0] = Long.valueOf(id).toString();

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			Cursor cursor = sqlHelper.getWritableDatabase().query(true, CategoryContract.TABLE_NAME, CATEGORY_COLUMNS,
					whereClause, whereArgs, null, null, null, null);
			if(cursor != null && !cursor.isClosed()) {
				if (cursor.moveToFirst()) {
					result = createCategoryInstance(cursor);
				}
				cursor.close();
				sqlHelper.getWritableDatabase().setTransactionSuccessful();
			}
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.d("CategoryDaoSqlImpl", "Loaded " + result);
		return result;
	}

	private String buildWhereArgs(Category filter, Collection<String> args) {
		boolean isFirst = true;
		String whereClause = "";
		if(filter != null) {
			if(filter.getId() != DataConstants.UNINITIALIZED) {
				whereClause = addFilter(whereClause, CategoryContract._ID, Long.valueOf(filter.getId()).toString(),
						true, args);
			} else {
				if (filter.getName() != null && !filter.getName().isEmpty()) {
					whereClause = addFilter(whereClause, CategoryContract.NAME_COLUMN_NAME, filter.getName(),
							true, args);
					isFirst = false;
				}
				if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
					whereClause = addFilter(whereClause, CategoryContract.DESCRIPTION_COLUMN_NAME, filter.getDescription(),
							isFirst, args);
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

	private Category createCategoryInstance(Cursor cursor) {
		Category category = new Category();
		category.setId(cursor.getLong(ID_INDEX));
		category.setName(cursor.getString(NAME_INDEX));
		category.setDescription(cursor.getString(DESCRIPTION_INDEX));

		return category;
	}
}
