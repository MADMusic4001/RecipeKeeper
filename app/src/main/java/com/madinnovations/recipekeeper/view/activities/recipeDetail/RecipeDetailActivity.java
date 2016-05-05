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
package com.madinnovations.recipekeeper.view.activities.recipeDetail;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.view.RecipeKeeperApp;
import com.madinnovations.recipekeeper.view.di.components.ActivityComponent;
import com.madinnovations.recipekeeper.view.di.modules.ActivityModule;

/**
 * Activity to create and manage the Recipe details UI
 */
public class RecipeDetailActivity extends Activity {
	private RecipeDetailFragment detailFragment;
	private ActivityComponent activityComponent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activityComponent = ((RecipeKeeperApp) getApplication()).getApplicationComponent()
				.newActivityComponent(new ActivityModule(this));
		activityComponent.injectInto(this);
		setContentView(R.layout.recipe_detail);

		detailFragment = (RecipeDetailFragment)getFragmentManager().findFragmentById(R.id.recipe_detail_framgent);
		Log.e("RecipeDetailActivity", "detailFragment = " + detailFragment);
	}

	// Getters and setters
	public ActivityComponent getActivityComponent() {
		return activityComponent;
	}
}
