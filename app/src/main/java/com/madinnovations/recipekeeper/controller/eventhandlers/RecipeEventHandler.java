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

import com.madinnovations.recipekeeper.controller.events.recipe.RecipePersistenceEvent;
import com.madinnovations.recipekeeper.controller.events.recipe.RecipeSavedEvent;
import com.madinnovations.recipekeeper.controller.events.recipe.RecipesLoadedEvent;
import com.madinnovations.recipekeeper.model.dao.RecipeDao;
import com.madinnovations.recipekeeper.model.entities.Recipe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Set;

import javax.inject.Singleton;

/**
 * Class to handle Recipe related events
 */
@Singleton
public class RecipeEventHandler {
	private EventBus eventBus;
	private RecipeDao recipeDao;

	/**
	 * Creates a new RecipeEventHandler instance configured with the given {@link EventBus} and {@link RecipeDao} instances.
	 *
	 * @param eventBus  an EventBus instance
	 * @param recipeDao  a RecipeDao instance
	 */
	public RecipeEventHandler(EventBus eventBus, RecipeDao recipeDao) {
		this.eventBus = eventBus;
		this.recipeDao = recipeDao;
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onRecipePersistenceEvent(RecipePersistenceEvent event) {
		switch (event.getAction()) {
			case SAVE:
				boolean result = recipeDao.save(event.getRecipe());
				eventBus.post(new RecipeSavedEvent(event.getRecipe(), result));
				break;
			case READ_BY_FILTER:
				Set<Recipe> results = recipeDao.read(event.getRecipe());
				eventBus.post(new RecipesLoadedEvent(results, results != null));
		}
	}
}
