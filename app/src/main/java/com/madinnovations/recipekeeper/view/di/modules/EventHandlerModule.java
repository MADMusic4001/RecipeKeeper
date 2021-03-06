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
package com.madinnovations.recipekeeper.view.di.modules;

import com.madinnovations.recipekeeper.controller.eventhandlers.CategoryEventHandler;
import com.madinnovations.recipekeeper.controller.eventhandlers.RecipeEventHandler;
import com.madinnovations.recipekeeper.controller.eventhandlers.UnitOfMeasureEventHandler;
import com.madinnovations.recipekeeper.model.dao.CategoryDao;
import com.madinnovations.recipekeeper.model.dao.RecipeDao;
import com.madinnovations.recipekeeper.model.dao.UnitOfMeasureDao;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/27/2016.
 */
@Module(includes = ApplicationModule.class)
public class EventHandlerModule {
	@Provides @Singleton
	RecipeEventHandler provideRecipeEventHandler(EventBus eventBus, RecipeDao recipeDao) {
		RecipeEventHandler handler = new RecipeEventHandler(eventBus, recipeDao);
		eventBus.register(handler);
		return handler;
	}

	@Provides @Singleton
	UnitOfMeasureEventHandler provideUnitOfMeasureEventHandler(EventBus eventBus, UnitOfMeasureDao unitOfMeasureDao) {
		UnitOfMeasureEventHandler handler = new UnitOfMeasureEventHandler(eventBus, unitOfMeasureDao);
		eventBus.register(handler);
		return handler;
	}

	@Provides @Singleton
	CategoryEventHandler provideCategoryEventHandler(EventBus eventBus, CategoryDao categoryDao) {
		CategoryEventHandler handler = new CategoryEventHandler(eventBus, categoryDao);
		eventBus.register(handler);
		return handler;
	}
}
