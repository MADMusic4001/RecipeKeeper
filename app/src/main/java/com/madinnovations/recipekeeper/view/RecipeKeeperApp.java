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
package com.madinnovations.recipekeeper.view;

import android.util.Log;

import com.activeandroid.app.Application;
import com.madinnovations.recipekeeper.controller.eventhandlers.RecipeEventHandler;
import com.madinnovations.recipekeeper.view.di.components.ApplicationComponent;
import com.madinnovations.recipekeeper.view.di.components.DaggerApplicationComponent;
import com.madinnovations.recipekeeper.view.di.modules.ApplicationModule;
import com.madinnovations.recipekeeper.view.di.modules.EventHandlerModule;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Main class for maintaining application state.
 *
 * Created 4/23/2016.
 */
public class RecipeKeeperApp extends Application {
	private ApplicationComponent applicationComponent;
	@Inject
	RecipeEventHandler recipeEventHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		this.initializeInjector();
	}

	private void initializeInjector() {
		this.applicationComponent = DaggerApplicationComponent.builder()
				.eventHandlerModule(new EventHandlerModule())
				.applicationModule(new ApplicationModule(this))
				.build();
		Log.d("RecipeKeeperApp", "RecipeEventHandler = " + recipeEventHandler);
		Log.d("RecipeKeeperApp", "ApplicationComponent successfully created.");
	}

	public ApplicationComponent getApplicationComponent() {
		return this.applicationComponent;
	}
}
