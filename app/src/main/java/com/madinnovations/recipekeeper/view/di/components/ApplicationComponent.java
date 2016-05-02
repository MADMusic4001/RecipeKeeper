/**
 * Copyright (C) 2014 MadInnovations
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

package com.madinnovations.recipekeeper.view.di.components;

import com.madinnovations.recipekeeper.view.di.modules.ActivityModule;
import com.madinnovations.recipekeeper.view.di.modules.ApplicationModule;
import com.madinnovations.recipekeeper.view.di.modules.SqlDaoModule;
import com.madinnovations.recipekeeper.view.di.modules.EventHandlerModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 *
 */
@Singleton
@Component(modules = {ApplicationModule.class, EventHandlerModule.class, SqlDaoModule.class})
public interface ApplicationComponent {
	ActivityComponent newActivityComponent(ActivityModule activityModule);
}
