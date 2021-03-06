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

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.events.recipe.RecipePersistenceEvent;
import com.madinnovations.recipekeeper.controller.events.recipe.RecipeSavedEvent;
import com.madinnovations.recipekeeper.controller.events.recipe.RecipeSelectedEvent;
import com.madinnovations.recipekeeper.controller.events.recipe.RecipesLoadedEvent;
import com.madinnovations.recipekeeper.model.entities.Recipe;
import com.madinnovations.recipekeeper.view.activities.category.CategoriesActivity;
import com.madinnovations.recipekeeper.view.activities.unitsOfMeasure.UnitsOfMeasureActivity;
import com.madinnovations.recipekeeper.view.adapters.RecipeListAdapter;
import com.madinnovations.recipekeeper.view.di.modules.FragmentModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Manages the interaction with the Recipes List user interface.
 */
public class RecipesListFragment extends Fragment {
	@Inject
	protected RecipeListAdapter adapter;
	@Inject
	protected EventBus eventBus = null;
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

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((RecipesListActivity)getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this)).injectInto(this);
		if(!eventBus.isRegistered(this)) {
			eventBus.register(this);
		}
		View layout = inflater.inflate(R.layout.recipe_list_fragment, container, false);
		listView = (ListView)layout.findViewById(R.id.rlf_list_view);
		initListView();
		eventBus.post(new RecipePersistenceEvent(RecipePersistenceEvent.Action.READ_BY_FILTER, null));
		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.recipes_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = false;

		int id = item.getItemId();
		if(id == R.id.actionNewRecipe){
			Log.d("RecipeListFragment", "Creating new recipe");
			eventBus.post(new RecipeSelectedEvent(new Recipe(getString(R.string.default_recipe_name))));
			result = true;
		}
		if(id == R.id.actionManageCategories){
			Intent intent = new Intent(getActivity().getApplicationContext(), CategoriesActivity.class);
			startActivity(intent);
		}
		if(id == R.id.actionManageUnitsOfMeasure){
			Intent intent = new Intent(getActivity().getApplicationContext(), UnitsOfMeasureActivity.class);
			startActivity(intent);
		}
		return result || super.onOptionsItemSelected(item);
	}

	/**
	 * Handles a RecipesLoadedEvent by displaying a success/failure message to the user with a toast and copying the collection of Recipe
	 * instances to the UI list view if the load was successful.
	 *
	 * @param event  a RecipesLoadedEvent instance
     */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRecipesLoadedEvent(RecipesLoadedEvent event) {
		String toastString;
		adapter.clear();
		if(event.isSuccess()) {
			toastString = getString(R.string.toast_recipes_loaded);
			adapter.addAll(event.getRecipes());
		} else {
			toastString = getString(R.string.toast_recipes_loaded_error);
		}
		adapter.notifyDataSetChanged();
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Handles a RecipesSavedEvent by adding the Recipe to the UI list view if the save was successful.
	 *
	 * @param event  a RecipesSavedEvent instance
     */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRecipeSavedEvent(RecipeSavedEvent event) {
		if(event.isSuccess()) {
			adapter.add(event.getRecipe());
			adapter.notifyDataSetChanged();
		}
	}

	// <editor-fold desc="Private plumbing methods">

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
				Recipe theRecipe = (Recipe) listView.getItemAtPosition(position);
				if (theRecipe != null) {
					eventBus.post(new RecipeSelectedEvent(theRecipe));
				}
			}
		});
		registerForContextMenu(listView);
	}
	// </editor-fold>
}
