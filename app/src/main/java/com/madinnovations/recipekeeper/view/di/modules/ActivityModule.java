/**
 * Copyright (C) 2015 MadInnovations
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

import com.madinnovations.recipekeeper.view.activities.category.CategoriesActivity;
import com.madinnovations.recipekeeper.view.activities.recipeDetail.RecipeDetailActivity;
import com.madinnovations.recipekeeper.view.activities.recipesList.RecipesListActivity;
import com.madinnovations.recipekeeper.view.activities.unitsOfMeasure.UnitsOfMeasureActivity;
import com.madinnovations.recipekeeper.view.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module to provide Activity instances
 */
@PerActivity
@Module
public class ActivityModule {
	RecipesListActivity  recipesListActivity;
	RecipeDetailActivity recipeDetailActivity;
	UnitsOfMeasureActivity unitsOfMeasureActivity;
	CategoriesActivity categoriesActivity;

	public ActivityModule(RecipesListActivity recipesListActivity) {
		this.recipesListActivity = recipesListActivity;
	}
	public ActivityModule(RecipeDetailActivity recipeDetailActivity) {
		this.recipeDetailActivity = recipeDetailActivity;
	}
	public ActivityModule(UnitsOfMeasureActivity unitsOfMeasureActivity) {
		this.unitsOfMeasureActivity = unitsOfMeasureActivity;
	}
	public ActivityModule(CategoriesActivity categoriesActivity) {
		this.categoriesActivity = categoriesActivity;
	}

	@Provides @PerActivity
	RecipesListActivity recipesListActivity() {
		return this.recipesListActivity;
	}
	@Provides @PerActivity
	RecipeDetailActivity recipeDetailActivity() {
		return this.recipeDetailActivity;
	}
	@Provides @PerActivity
	UnitsOfMeasureActivity unitsOfMeasureActivity() {
		return this.unitsOfMeasureActivity;
	}
	@Provides @PerActivity
	CategoriesActivity categoriesActivity() {
		return this.categoriesActivity;
	}
}
