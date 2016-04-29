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

import com.madinnovations.recipekeeper.model.dao.UnitOfMeasureDao;
import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;

import java.util.Set;

import javax.inject.Singleton;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/28/2016.
 */
@Singleton
public class UnitOfMeasureDaoSqlImpl implements BaseDaoSqlImpl, UnitOfMeasureDao {
	private static abstract class UnitOfMeasureContract implements BaseColumns {
		public static final String TABLE_NAME = "units_of_measure";
		public static final String SINGULAR_NAME_COLUMN_NAME = "singular_name";
		public static final String PLURAL_NAME_COLUMN_NAME = "plural_name";
		public static final String NOTES_COLUMN_NAME = "notes_name";
	}
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
		if(uom.getId() == UNINITIALIZED) {
			uom.setId(sqlHelper.getWritableDatabase().insert(UnitOfMeasureContract.TABLE_NAME, null, values));
			result = (uom.getId() != UNINITIALIZED);
		}
		else {
			values.put(UnitOfMeasureContract._ID, uom.getId());
			int count = sqlHelper.getWritableDatabase().update(UnitOfMeasureContract.TABLE_NAME,values,
							WHERE + SPACE + UnitOfMeasureContract._ID + EQUALS + PLACEHOLDER,
							new String[]{Long.valueOf(uom.getId()).toString()});
			result = (count == 1);
		}
		return result;
	}

	@Override
	public boolean delete(UnitOfMeasure uom) {
		return false;
	}

	@Override
	public Set<UnitOfMeasure> read(UnitOfMeasure filter) {
		return null;
	}

	@Override
	public UnitOfMeasure read(int id) {
		return null;
	}
}
