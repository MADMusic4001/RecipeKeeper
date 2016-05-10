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
package com.madinnovations.recipekeeper.view.activities.unitsOfMeasure;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.eventhandlers.UnitOfMeasureEventHandler;
import com.madinnovations.recipekeeper.controller.events.unitofmeasure.UnitOfMeasureSelectedEvent;
import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;
import com.madinnovations.recipekeeper.view.RecipeKeeperApp;
import com.madinnovations.recipekeeper.view.activities.category.CategoriesActivity;
import com.madinnovations.recipekeeper.view.activities.recipesList.RecipesListActivity;
import com.madinnovations.recipekeeper.view.di.components.ActivityComponent;
import com.madinnovations.recipekeeper.view.di.modules.ActivityModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * Activity to create and manage the Unit of Measure UI
 */
public class UnitsOfMeasureActivity extends Activity {
	@Inject
	protected EventBus eventBus;
	@Inject
	protected UnitOfMeasureEventHandler unitOfMeasureEventHandler;
	private ActivityComponent    activityComponent;
	private UnitsOfMeasureListFragment listFragment;
	private UnitOfMeasureDetailFragment detailFragment;
	private boolean dualPane;

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
		setContentView(R.layout.units_of_measure);

		listFragment = (UnitsOfMeasureListFragment) getFragmentManager().findFragmentById(R.id.units_of_measure_list_fragment);
		detailFragment = (UnitOfMeasureDetailFragment) getFragmentManager().findFragmentById(R.id.unit_of_measure_detail_fragment);
		dualPane = (detailFragment != null);
		if(!dualPane) {
			listFragment = new UnitsOfMeasureListFragment();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.unitOfMeasureFragmentContainer, listFragment);
			transaction.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.units_of_measure_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = false;

		int id = item.getItemId();
		if(id == android.R.id.home) {
			Log.e("UnitsOfMeasureActivity", "home selected");
			if(!dualPane && detailFragment != null && detailFragment.isVisible()) {
				getFragmentManager().popBackStack();
				result = true;
			}
		}
		if(id == R.id.actionNewUnitOfMeasure){
			eventBus.post(new UnitOfMeasureSelectedEvent(new UnitOfMeasure()));
			result = true;
		}
		if(id == R.id.actionManageRecipes){
			Intent intent = new Intent(getApplicationContext(), RecipesListActivity.class);
			startActivity(intent);
		}
		if(id == R.id.actionManageCategories){
			Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
			startActivity(intent);
		}
		return result || super.onOptionsItemSelected(item);
	}

	@Subscribe
	public void onUnitOfMeasureSelectedEvent(UnitOfMeasureSelectedEvent event) {
		if (!dualPane) {
			if (detailFragment == null) {
				detailFragment = new UnitOfMeasureDetailFragment();
			}
			getFragmentManager().beginTransaction()
					.hide(listFragment)
					.add(R.id.unitOfMeasureFragmentContainer, detailFragment)
					.addToBackStack("listFragment")
					.commit();
		}
		detailFragment.setUnitOfMeasure(event.getUnitOfMeasure());
	}

	// Getters and setters
	public ActivityComponent getActivityComponent() {
		return activityComponent;
	}
}
