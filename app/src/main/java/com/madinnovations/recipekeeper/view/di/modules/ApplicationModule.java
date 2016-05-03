/**
 * Copyright (C) 2015 MadInnovations
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

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;

import com.madinnovations.recipekeeper.view.RecipeKeeperApp;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency injection module for application level instances.
 */
@Module
public class ApplicationModule {
	static final String PREFS_DEFAULT = "rkapp";
	private final RecipeKeeperApp application;

	@Inject
	public ApplicationModule(RecipeKeeperApp application) {
		this.application = application;
	}

	/**
	 * The {@link Context} for the application.
	 *
	 * @return a {@link Context} instance.
	 */
	@Provides @Singleton
	Context provideApplicationContext() {
		return this.application;
	}

	/**
	 * The {@link Application} instance.
	 *
	 * @param app the {@link RecipeKeeperApp}.
	 * @return the {@link Application} instance
	 */
	@Provides @Singleton
	Application provideApplication(RecipeKeeperApp app) {
		return app;
	}

	/**
	 * The {@link RecipeKeeperApp}.
	 *
	 * @return the {@link RecipeKeeperApp} instance.
	 */
	@Provides @Singleton
	RecipeKeeperApp provideRecipeKeeperApp() {
		return application;
	}

	/**
	 * The {@link SharedPreferences} for the application.
	 *
	 * @param app the {@link Application} whose {@link SharedPreferences} is being requested.
	 * @return the {@link SharedPreferences} instance.
	 */
	@Provides @Singleton
	SharedPreferences provideSharedPrefs(Application app) {
		return app.getSharedPreferences(PREFS_DEFAULT, Context.MODE_PRIVATE);
	}

	@Provides @Singleton
	EventBus provideEventBus() {
		return new EventBus();
	}

	@Provides @Singleton
	LayoutInflater provideLayoutInflater(Context context) {
		return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
}
