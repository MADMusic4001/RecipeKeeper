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
package com.madinnovations.recipekeeper.view.di.components;

import com.madinnovations.recipekeeper.view.activities.recipeDetail.RecipeDetailActivity;
import com.madinnovations.recipekeeper.view.activities.recipesList.RecipesListActivity;
import com.madinnovations.recipekeeper.view.activities.unitsOfMeasure.UnitsOfMeasureActivity;
import com.madinnovations.recipekeeper.view.di.PerActivity;
import com.madinnovations.recipekeeper.view.di.modules.ActivityModule;
import com.madinnovations.recipekeeper.view.di.modules.FragmentModule;

import dagger.Subcomponent;

/**
 * Dagger component for providing Activity instances
 *
 * @author Mark
 *         Created 7/4/2015.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
	FragmentComponent newFragmentComponent(FragmentModule fragmentModule);

	void injectInto(RecipesListActivity activity);
	void injectInto(RecipeDetailActivity activity);
	void injectInto(UnitsOfMeasureActivity activity);
}
