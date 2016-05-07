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

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.events.LoadUnitsOfMeasureEvent;
import com.madinnovations.recipekeeper.controller.events.UnitOfMeasureSelectedEvent;
import com.madinnovations.recipekeeper.controller.events.UnitsOfMeasureLoadedEvent;
import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;
import com.madinnovations.recipekeeper.view.adapters.UnitOfMeasureListAdapter;
import com.madinnovations.recipekeeper.view.di.modules.FragmentModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Manages the interaction with the Units of Measure list user interface.
 */
public class UnitsOfMeasureListFragment extends Fragment {
	@Inject
	protected EventBus eventBus = null;
	@Inject
	protected UnitOfMeasureListAdapter adapter;
	private ListView listView;

	@Override
	public void onResume() {
		super.onResume();
		if(eventBus != null && !eventBus.isRegistered(this)) {
			eventBus.register(this);
		}
	}

	@Override
	public void onPause() {
		if(eventBus != null) {
			eventBus.unregister(this);
		}
		super.onPause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.units_of_measure_menu, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((UnitsOfMeasureActivity)getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this))
				.injectInto(this);
		if(!eventBus.isRegistered(this)) {
			eventBus.register(this);
		}
		View layout = inflater.inflate(R.layout.unit_of_measure_detail_fragment, container, false);
		listView = (ListView)layout.findViewById(R.id.uomList);
		initListView();
		eventBus.post(new LoadUnitsOfMeasureEvent(null));
		return layout;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onUnitsOfMeasureLoadedEvent(UnitsOfMeasureLoadedEvent event) {
		String toastString;

		adapter.clear();
		if(event.isSuccessful()) {
			toastString = getString(R.string.toast_units_of_measure_loaded);
			Log.d("RecipesListFragment", event.getUnitsOfMeasureSet().size() + " recipes loaded.");
			adapter.addAll(event.getUnitsOfMeasureSet());
		} else {
			toastString = getString(R.string.toast_units_of_measure_loaded_error);
		}
		adapter.notifyDataSetChanged();
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
	}

	private void initListView() {
		View headerView;

		headerView = getActivity().getLayoutInflater().inflate(
				R.layout.recipes_list_header, listView, false);
		listView.addHeaderView(headerView);

		// Create and set onClick methods for sorting the {@link World} list.
		headerView.findViewById(R.id.nameHeader).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				controller.sortRecipesByName();
			}
		});
		headerView.findViewById(R.id.categoriesHeader).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				controller.sortRecipesByCategories();
			}
		});
		headerView.findViewById(R.id.createdHeader).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				controller.sortRecipesByCreated();
			}
		});
		View updatedView = headerView.findViewById(R.id.updatedHeader);
		if(updatedView != null) {
			updatedView.setOnClickListener(new View.OnClickListener
					() {
				@Override
				public void onClick(View v) {
//					controller.sortRecipesByUpdated();
				}
			});
		}
		listView.setAdapter(adapter);

		// Clicking a row in the listView will send the user to the recipes details activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				UnitOfMeasure item = (UnitOfMeasure) listView.getItemAtPosition(position);
				if (item != null) {
					eventBus.post(new UnitOfMeasureSelectedEvent(item));
				}
			}
		});
		registerForContextMenu(listView);
	}
}
