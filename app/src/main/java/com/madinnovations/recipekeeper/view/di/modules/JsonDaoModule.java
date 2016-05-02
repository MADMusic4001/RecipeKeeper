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
package com.madinnovations.recipekeeper.view.di.modules;

import android.content.Context;

import com.madinnovations.recipekeeper.model.dao.CategoryDao;
import com.madinnovations.recipekeeper.model.dao.IngredientDao;
import com.madinnovations.recipekeeper.model.dao.RecipeDao;
import com.madinnovations.recipekeeper.model.dao.UnitOfMeasureDao;
import com.madinnovations.recipekeeper.model.dao.impl.json.CategoryDaoJsonImpl;
import com.madinnovations.recipekeeper.model.dao.impl.json.IngredientDaoJsonImpl;
import com.madinnovations.recipekeeper.model.dao.impl.json.RecipeDaoJsonImpl;
import com.madinnovations.recipekeeper.model.dao.impl.json.RecipeKeeperFileHelper;
import com.madinnovations.recipekeeper.model.dao.impl.json.UnitOfMeasureDaoJsonImpl;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Class used by Dagger dependency injection framework to provide instances of DAO classes to inject into classes that depend on
 * them.
 */
@Module(includes = ApplicationModule.class)
public class JsonDaoModule {
	/**
	 * Creates a {@link RecipeKeeperFileHelper} instance.
	 *
	 * @param context  the android context needed by RecipeKeeperFileHelper
	 * @return  a new instance of RecipeKeeperFileHelper
	 */
	@Provides @Singleton
	public RecipeKeeperFileHelper provideRecipeKeeperFileHelper(Context context) {
		return new RecipeKeeperFileHelper(context, DataConstants.PROJECTS_DIR);
	}

	/**
	 * Creates a {@link UnitOfMeasureDao} instance.
	 *
	 * @param fileHelper  the RecipeKeeperFileHelper instance needed by UnitOfMeasuerDaoJsonImpl
	 * @return  a new instance of UnitOfMeasureDaoJsonImpl
	 */
	@Provides @Singleton
	public UnitOfMeasureDao provideUnitOfMeasureDao(RecipeKeeperFileHelper fileHelper) {
		return new UnitOfMeasureDaoJsonImpl(fileHelper);
	}

	/**
	 * Creates a {@link CategoryDao} instance.
	 *
	 * @param fileHelper  the RecipeKeeperFileHelper instance needed by CategoryDaoJsonImpl
	 * @return  a new instance of CategoryDaoJsonImpl
	 */
	@Provides @Singleton
	public CategoryDao provideCategoryDao(RecipeKeeperFileHelper fileHelper) {
		return new CategoryDaoJsonImpl(fileHelper);
	}

	/**
	 * Creates a {@link IngredientDao} instance.
	 *
	 * @param fileHelper  the RecipeKeeperFileHelper instance needed by IngredientDaoSqlImp
	 * @return  a new instance of IngredientDaoSqlImp
	 */
	@Provides @Singleton
	public IngredientDao provideIngredientDao(RecipeKeeperFileHelper fileHelper) {
		return new IngredientDaoJsonImpl(fileHelper);
	}

	/**
	 * Creates a {@link RecipeDao} instance.
	 *
	 * @param fileHelper  the RecipeKeeperFileHelper instance needed by RecipeDaoJsonImpl
	 * @param categoryDao  the CategoryDao instance needed by RecipeDaoJsonImpl
	 * @param ingredientDao  the IngredientDao instance needed by RecipeDaoJsonImpl
	 * @return  a new instance of RecipeDaoJsonImpl
	 */
	@Provides @Singleton
	public RecipeDao provideRecipeDao(RecipeKeeperFileHelper fileHelper, CategoryDao categoryDao,
									  IngredientDao ingredientDao) {
		return new RecipeDaoJsonImpl(fileHelper, categoryDao, ingredientDao);
	}
}
