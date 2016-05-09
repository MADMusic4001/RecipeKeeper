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

import com.madinnovations.recipekeeper.model.dao.UnitOfMeasureDao;
import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

/**
 * Implementation of the {@link UnitOfMeasureDao} for maintaining a {@link UnitOfMeasure} in a SQLite database.
 */
@Singleton
public class UnitOfMeasureDaoSqlImpl implements BaseDaoSql, UnitOfMeasureDao {
	public static abstract class UnitOfMeasureContract implements BaseColumns {
		public static final String TABLE_NAME = "units_of_measure";
		public static final String SINGULAR_NAME_COLUMN_NAME = "singular_name";
		public static final String PLURAL_NAME_COLUMN_NAME = "plural_name";
		public static final String NOTES_COLUMN_NAME = "notes_name";
	}
	public static final String CREATE_TABLE_UNITS_OF_MEASURE =
			"CREATE TABLE " + UnitOfMeasureContract.TABLE_NAME + " (" +
					UnitOfMeasureContract._ID + " INTEGER NOT NULL PRIMARY KEY, " +
					UnitOfMeasureContract.SINGULAR_NAME_COLUMN_NAME + " TEXT NOT NULL, " +
					UnitOfMeasureContract.PLURAL_NAME_COLUMN_NAME + " TEXT NOT NULL, " +
					UnitOfMeasureContract.NOTES_COLUMN_NAME + " TEXT, " +
					"CONSTRAINT unique_singular_name UNIQUE (" + UnitOfMeasureContract.SINGULAR_NAME_COLUMN_NAME + "));";
	private static final String [] UNIT_OF_MEASURE_COLUMNS = {
			UnitOfMeasureContract._ID,
			UnitOfMeasureContract.SINGULAR_NAME_COLUMN_NAME,
			UnitOfMeasureContract.PLURAL_NAME_COLUMN_NAME,
			UnitOfMeasureContract.NOTES_COLUMN_NAME};
	private static final int      ID_INDEX          	= 0;
	private static final int      SINGULAR_NAME_INDEX   = 1;
	private static final int      PLURAL_NAME_INDEX  	= 2;
	private static final int      NOTES_INDEX 			= 3;
	private RecipeKeeperSqlHelper sqlHelper;

	/**
	 * Creates a UnitOfMeasureDaoSqlImpl instance with the given {@link RecipeKeeperSqlHelper}
	 *
	 * @param sqlHelper  the RecipeKeeperSqlHelper to use
	 */
	public UnitOfMeasureDaoSqlImpl(RecipeKeeperSqlHelper sqlHelper) {
		this.sqlHelper = sqlHelper;
	}

	@Override
	public boolean save(UnitOfMeasure uom) {
		boolean result;
		ContentValues values = new ContentValues();
		values.put(UnitOfMeasureContract.SINGULAR_NAME_COLUMN_NAME, uom.getSingularName());
		values.put(UnitOfMeasureContract.PLURAL_NAME_COLUMN_NAME, uom.getPluralName());
		values.put(UnitOfMeasureContract.NOTES_COLUMN_NAME, uom.getNotes());

		sqlHelper.getWritableDatabase().beginTransaction();
		if(uom.getId() == DataConstants.UNINITIALIZED) {
			uom.setId(sqlHelper.getWritableDatabase().insert(UnitOfMeasureContract.TABLE_NAME, null, values));
			result = (uom.getId() != DataConstants.UNINITIALIZED);
		}
		else {
			values.put(UnitOfMeasureContract._ID, uom.getId());
			int count = sqlHelper.getWritableDatabase().update(UnitOfMeasureContract.TABLE_NAME,values,
							UnitOfMeasureContract._ID + EQUALS + PLACEHOLDER,
							new String[]{Long.valueOf(uom.getId()).toString()});
			result = (count == 1);
		}
		return result;
	}

	@Override
	public boolean delete(UnitOfMeasure filter) {
		Log.d("UnitOfMeasureDaoSqlImpl", "Deleting UnitOfMeasure records using filter " + filter);
		boolean result = false;
		List<String> whereArgsList = new ArrayList<>();
		String whereClause = buildWhereArgs(filter, whereArgsList);
		String[] whereArgs = new String[whereArgsList.size()];

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			result = (sqlHelper.getWritableDatabase().delete(UnitOfMeasureContract.TABLE_NAME, whereClause,
															 whereArgsList.toArray(whereArgs)) >= 0);
			sqlHelper.getWritableDatabase().setTransactionSuccessful();
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.d("UnitOfMeasureDaoSqlImpl", "Delete " + (result ? "succeeded" : "failed"));
		return result;
	}

	@Override
	public Set<UnitOfMeasure> read(UnitOfMeasure filter) {
		Log.e("UnitOfMeasureDaoSqlImpl", "Reading UnitOfMeasure records filtered by " + filter);
		Set<UnitOfMeasure> result = new HashSet<>();
		List<String> whereArgsList = new ArrayList<>();
		String whereClause = buildWhereArgs(filter, whereArgsList);
		String[] whereArgs = new String[whereArgsList.size()];

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			Cursor cursor = sqlHelper.getWritableDatabase().query(UnitOfMeasureContract.TABLE_NAME, UNIT_OF_MEASURE_COLUMNS,
																  whereClause, whereArgsList.toArray(whereArgs), null, null,
																  null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				UnitOfMeasure unitOfMeasure = createUnitOfMeasureInstance(cursor);
				result.add(unitOfMeasure);
				cursor.moveToNext();
			}
			cursor.close();
			sqlHelper.getWritableDatabase().setTransactionSuccessful();
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.e("UnitOfMeasureDaoSqlImpl", "Loaded " + result.size() + " UnitOfMeasure records.");
		return result;
	}

	@Override
	public UnitOfMeasure read(long id) {
		Log.d("UnitOfMeasureDaoSqlImpl", "Reading UnitOfMeasure for id = " + id);
		UnitOfMeasure result = null;
		String whereClause = UnitOfMeasureContract._ID + EQUALS + PLACEHOLDER;
		String[] whereArgs = new String[1];
		whereArgs[0] = Long.valueOf(id).toString();

		sqlHelper.getWritableDatabase().beginTransactionNonExclusive();
		try {
			Cursor cursor = sqlHelper.getWritableDatabase().query(true, UnitOfMeasureContract.TABLE_NAME, UNIT_OF_MEASURE_COLUMNS,
																  whereClause, whereArgs, null, null, null, null);
			if(cursor.moveToFirst()) {
				result = createUnitOfMeasureInstance(cursor);
			}
			cursor.close();
			sqlHelper.getWritableDatabase().setTransactionSuccessful();
		}
		finally {
			sqlHelper.getWritableDatabase().endTransaction();
		}
		Log.d("UnitOfMeasureDaoSqlImpl", "Loaded " + result);
		return result;
	}

	private String buildWhereArgs(UnitOfMeasure filter, Collection<String> args) {
		boolean isFirst = true;
		String whereClause = "";
		if(filter != null) {
			if(filter.getId() != DataConstants.UNINITIALIZED) {
				whereClause = addFilter(whereClause, UnitOfMeasureContract._ID, Long.valueOf(filter.getId()).toString(),
										isFirst, args);
				isFirst = false;
			}
			if(filter.getSingularName() != null && !filter.getSingularName().isEmpty()) {
				whereClause = addFilter(whereClause, UnitOfMeasureContract.SINGULAR_NAME_COLUMN_NAME, filter.getSingularName(),
										isFirst, args);
				isFirst = false;
			}
			if(filter.getPluralName() != null && !filter.getPluralName().isEmpty()) {
				whereClause = addFilter(whereClause, UnitOfMeasureContract.PLURAL_NAME_COLUMN_NAME, filter.getPluralName(),
										isFirst, args);
				isFirst = false;
			}
			if(filter.getNotes() != null && !filter.getNotes().isEmpty()) {
				whereClause = addFilter(whereClause, UnitOfMeasureContract.NOTES_COLUMN_NAME, filter.getNotes(), isFirst, args);
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

	private UnitOfMeasure createUnitOfMeasureInstance(Cursor cursor) {
		UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		unitOfMeasure.setId(cursor.getLong(ID_INDEX));
		unitOfMeasure.setSingularName(cursor.getString(SINGULAR_NAME_INDEX));
		unitOfMeasure.setPluralName(cursor.getString(PLURAL_NAME_INDEX));
		unitOfMeasure.setNotes(cursor.getString(NOTES_INDEX));

		return unitOfMeasure;
	}
}
