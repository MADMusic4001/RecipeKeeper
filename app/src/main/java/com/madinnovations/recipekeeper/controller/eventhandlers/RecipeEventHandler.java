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
package com.madinnovations.recipekeeper.controller.eventhandlers;

import android.util.Log;

import com.madinnovations.recipekeeper.controller.events.LoadRecipesEvent;
import com.madinnovations.recipekeeper.controller.events.RecipeSavedEvent;
import com.madinnovations.recipekeeper.controller.events.RecipesLoadedEvent;
import com.madinnovations.recipekeeper.controller.events.SaveRecipeEvent;
import com.madinnovations.recipekeeper.model.dao.RecipeDao;
import com.madinnovations.recipekeeper.model.entities.Recipe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class to handle Recipe related events
 */
@Singleton
public class RecipeEventHandler {
	private EventBus eventBus;
	private RecipeDao recipeDao;

	/**
	 * Creates a new RecioeEventHandler instance configured with the given {@link EventBus} instance
	 *
	 * @param eventBus  an EventBus instance
	 */
	public RecipeEventHandler(EventBus eventBus, RecipeDao recipeDao) {
		this.eventBus = eventBus;
		this.recipeDao = recipeDao;
	}

	/**
	 * Processes a request to save a Recipe to persistent storage.
	 *
	 * @param event  a SaveRecipeEvent containing the Recipe instance to save
	 */
	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onSaveRecipeEvent(SaveRecipeEvent event) {
		Log.d("RecipeEventHandler", "SaveRecipeEvent received. " + event);
		boolean result = recipeDao.save(event.getRecipe());
		eventBus.post(new RecipeSavedEvent(event.getRecipe(), result));
	}

	/**
	 * Processes a request to load Recipe instances from persistent storage.
	 *
	 * @param event  a LoadRecipesEvent instance containing a Recipe instance to use as a filter.
	 */
	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onLoadRecipesEvent(LoadRecipesEvent event) {
		Log.d("RecipeEventHandler", "LoadRecipesEvent received. " + event);
		Set<Recipe> results = recipeDao.read(event.getFilter());
		Log.d("RecipeEventHandler", "LoadRecipesEvent results = " + results);
		eventBus.post(new RecipesLoadedEvent(results, results != null));
	}
}
