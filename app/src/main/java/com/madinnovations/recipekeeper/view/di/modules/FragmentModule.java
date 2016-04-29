/**
 * Copyright (C) 2015 MadMusic4001
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

import com.madinnovations.recipekeeper.view.activities.recipeDetail.RecipeDetailFragment;
import com.madinnovations.recipekeeper.view.activities.recipesList.RecipesListFragment;
import com.madinnovations.recipekeeper.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module for creating Fragment instances
 *
 * Created 4/23/2016.
 */
@Module
public class FragmentModule {
	RecipesListFragment  recipesListFragment;
	RecipeDetailFragment recipeDetailFragment;

	public FragmentModule(RecipesListFragment recipesListFragment) {
		this.recipesListFragment = recipesListFragment;
	}

	public FragmentModule(RecipeDetailFragment recipeDetailFragment) {
		this.recipeDetailFragment = recipeDetailFragment;
	}

	@Provides @PerFragment
	RecipesListFragment recipesListFragment() {
		return this.recipesListFragment;
	}

	@Provides @PerFragment
	RecipeDetailFragment recipeDetailFragment() {
		return this.recipeDetailFragment;
	}
}
