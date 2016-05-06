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
import android.os.Bundle;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.events.UnitOfMeasureSelectedEvent;
import com.madinnovations.recipekeeper.view.RecipeKeeperApp;
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
	}

	@Subscribe
	public void onUnitOfMeasureSelectedEvent(UnitOfMeasureSelectedEvent event) {
		if (!dualPane) {
			if (detailFragment == null) {
				detailFragment = new UnitOfMeasureDetailFragment();
			}
			getFragmentManager().beginTransaction()
					.add(R.id.unitOfMeasureFragmentContainer, detailFragment)
					.addToBackStack(null)
					.commit();
			listFragment = null;
		}
		detailFragment.setUnitOfMeasure(event.getUnitOfMeasure());

	}

	// Getters and setters
	public ActivityComponent getActivityComponent() {
		return activityComponent;
	}
}
