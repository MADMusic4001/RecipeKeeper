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

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import javax.inject.Singleton;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/28/2016.
 */
@Singleton
public class RecipeKeeperSqlHelper extends SQLiteOpenHelper {
	private final Context context;

	/**
	 * Creates an instance of RecipeKeeperSqlHelper with the given context, name, factory, version, and errorHandler.
	 *
	 * @param context  the android {@link Context} to use
	 * @param name  the name of the database
	 * @param factory  a {@link SQLiteDatabase.CursorFactory} to use
	 * @param version  the version of the database
	 * @param errorHandler  a {@link DatabaseErrorHandler} instance to use
	 */
	public RecipeKeeperSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
								 DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(this.getClass().getName(), "Creating db...");
		try {
			db.beginTransaction();
			db.execSQL(RecipeDaoSqlImpl.CREATE_TABLE_RECIPES);
			db.execSQL(UnitOfMeasureDaoSqlImpl.CREATE_TABLE_UNITS_OF_MEASURE);
			db.execSQL(CategoryDaoSqlImpl.CREATE_TABLE_CATEGORIES);
			db.execSQL(IngredientDaoSqlImpl.CREATE_TABLE_INGREDIENTS);
			db.execSQL(RecipeDaoSqlImpl.CREATE_TABLE_RECIPE_CATEGORIES);
			db.setTransactionSuccessful();
			db.endTransaction();
			Log.d(this.getClass().getName(), "Db creation complete. Db located at " + db.getPath());
		}
		catch(SQLException ex) {
			Log.e(this.getClass().getName(), "Error creating db.", ex);
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
