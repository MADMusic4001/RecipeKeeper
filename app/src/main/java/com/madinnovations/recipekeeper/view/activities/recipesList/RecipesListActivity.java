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
package com.madinnovations.recipekeeper.view.activities.recipesList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.eventhandlers.RecipeEventHandler;
import com.madinnovations.recipekeeper.controller.events.RecipeSelectedEvent;
import com.madinnovations.recipekeeper.model.utils.IntentConstants;
import com.madinnovations.recipekeeper.view.RecipeKeeperApp;
import com.madinnovations.recipekeeper.view.activities.recipeDetail.RecipeDetailActivity;
import com.madinnovations.recipekeeper.view.activities.recipeDetail.RecipeDetailFragment;
import com.madinnovations.recipekeeper.view.di.components.ActivityComponent;
import com.madinnovations.recipekeeper.view.di.modules.ActivityModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Activity to create and manage the Recipe list UI
 */
public class RecipesListActivity extends Activity {
	@Inject
	protected RecipeEventHandler recipeEventHandler;
	@Inject
	protected EventBus eventBus;
	private ActivityComponent    activityComponent;
	private RecipeDetailFragment detailFragment;

	@Override
	protected void onResume() {
		super.onResume();
		if(eventBus != null && !eventBus.isRegistered(this)) {
			eventBus.register(this);
		}
	}

	@Override
	protected void onPause() {
		if(eventBus != null) {
			eventBus.unregister(this);
		}
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activityComponent = ((RecipeKeeperApp) getApplication()).getApplicationComponent()
				.newActivityComponent(new ActivityModule(this));
		activityComponent.injectInto(this);
		if(!eventBus.isRegistered(this)) {
			eventBus.register(this);
		}
		setContentView(R.layout.recipes_list);

		detailFragment = (RecipeDetailFragment)getFragmentManager().findFragmentById(R.id.recipe_detail_framgent);
		Log.e("RecipeListActivity", "detailFragment = " + detailFragment);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRecipeSelected(RecipeSelectedEvent event) {
		Log.e("RecipeListActivity", "detailFragment = " + detailFragment);
		if(detailFragment != null) {
			Log.e("RecipeListActivity", "isVisible = " + detailFragment.isVisible());
			detailFragment.setRecipe(event.getRecipe());
		}
		else {
			Log.e("RecipeListActivity", "Starting detail activity");
			Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
			intent.putExtra(IntentConstants.RECIPE_DETAIL_INTENT_RECIPE_ID, event.getRecipe().getId());
			startActivity(intent);
		}
	}

	// Getters and setters
	public ActivityComponent getActivityComponent() {
		return activityComponent;
	}
}
