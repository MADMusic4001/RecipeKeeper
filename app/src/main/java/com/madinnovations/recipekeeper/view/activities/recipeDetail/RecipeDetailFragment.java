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

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.events.RecipeSavedEvent;
import com.madinnovations.recipekeeper.model.entities.Recipe;
import com.madinnovations.recipekeeper.view.activities.recipesList.RecipesListActivity;
import com.madinnovations.recipekeeper.view.di.modules.FragmentModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/23/2016.
 */
public class RecipeDetailFragment extends Fragment {
	private Recipe recipe;
	@Inject
	protected EventBus eventBus;

	@Override
	public void onStop() {
		eventBus.unregister(this);
		super.onStop();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(getActivity() instanceof  RecipesListActivity) {
			((RecipesListActivity) getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this))
					.injectInto(this);
		} else {
			((RecipeDetailActivity) getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this))
					.injectInto(this);
		}
		eventBus.register(this);
		View layout = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
		return layout;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void OnRecipeSavedEvent(RecipeSavedEvent event) {
		String toastString;
		if(event.isSuccess()) {
			toastString = getString(R.string.toast_recipe_saved);
		}
		else {
			toastString = getString(R.string.toast_recipe_saved_error);
		}
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
	}

	// Getters and setters
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
}
