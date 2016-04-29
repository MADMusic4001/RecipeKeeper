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
package com.madinnovations.recipekeeper.view.activities.recipesList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.eventhandlers.RecipeEventHandler;
import com.madinnovations.recipekeeper.model.entities.Recipe;
import com.madinnovations.recipekeeper.model.utils.IntentConstants;
import com.madinnovations.recipekeeper.view.RecipeKeeperApp;
import com.madinnovations.recipekeeper.view.activities.recipeDetail.RecipeDetailActivity;
import com.madinnovations.recipekeeper.view.activities.recipeDetail.RecipeDetailFragment;
import com.madinnovations.recipekeeper.view.di.components.ActivityComponent;
import com.madinnovations.recipekeeper.view.di.modules.ActivityModule;

import org.greenrobot.eventbus.EventBus;

import java.util.Collection;
import java.util.Comparator;

import javax.inject.Inject;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/23/2016.
 */
public class RecipesListActivity extends Activity implements
		RecipesListFragment.OnRecipesListEventsListener {
	@Inject
	protected RecipeEventHandler recipeEventHandler;
	@Inject
	protected EventBus eventBus;
	private ActivityComponent    activityComponent;
	private RecipeDetailFragment detailFragment;
	private boolean              isDualPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activityComponent = ((RecipeKeeperApp) getApplication()).getApplicationComponent()
				.newActivityComponent(new ActivityModule(this));
		activityComponent.injectInto(this);
//		eventBus.register(recipeEventHandler);
		setContentView(R.layout.recipes_list);

		detailFragment = (RecipeDetailFragment)getFragmentManager().findFragmentById(R.id.recipe_detail_framgent);
	}

	@Override
	public void onRecipeSelected(Recipe recipe) {
		if(detailFragment == null) {
			detailFragment.setRecipe(recipe);
		}
		else {
			Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
			intent.putExtra(IntentConstants.RECIPE_DETAIL_INTENT_RECIPE_ID, recipe.getId());
			startActivity(intent);
		}
	}

	// Getters and setters
	public ActivityComponent getActivityComponent() {
		return activityComponent;
	}
}
