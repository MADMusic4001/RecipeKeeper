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
package com.madinnovations.recipekeeper.controller.eventhandlers;

import android.util.Log;

import com.madinnovations.recipekeeper.controller.events.RecipeSavedEvent;
import com.madinnovations.recipekeeper.controller.events.SaveRecipeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class to handle Recipe related events
 */
@Singleton
public class RecipeEventHandler {
	private EventBus eventBus;

	/**
	 * Creates a new RecioeEventHandler instance configured with the given {@link EventBus} instance
	 *
	 * @param eventBus  an EventBus instance
	 */
	public RecipeEventHandler(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Subscribe
	public void onSaveRecipeEvent(SaveRecipeEvent event) {
		Log.d("RecipeEventHandler", "SaveRecipeEvent received.");
		event.getRecipe().save();
		eventBus.post(new RecipeSavedEvent(event.getRecipe(), event.getRecipe().getId() != null));
	}

	@Subscribe
	public void onEvent(Object event) {
		Log.d("RecipeEventHandler", event + " received,");
	}

//	@Subscribe
//	public void onDeadEvent(DeadEvent event) {
//		Log.d("RecipeEventHandler", "DeadEvent received,");
//	}
}
