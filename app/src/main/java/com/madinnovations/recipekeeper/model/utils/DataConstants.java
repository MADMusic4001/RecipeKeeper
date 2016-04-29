/**
 * Copyright (C) 2014 MadMusic4001
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.madinnovations.recipekeeper.model.utils;

import java.io.File;

import javax.inject.Singleton;

/**
 * Utility class to maintain constants used in multiple places in the app
 */
@Singleton
public final class DataConstants {
	public static final long   APP_VERSION_ID       = 20160423001L;
	public static final String PROJECTS_DIR         = File.separator + "projects";
	public static final int    DB_VERSION           = 1;
	public static final String DB_NAME              = "recipe_keeper_db";
	public static final String PACKAGE_NAME         = "com.madinnovations.recipekeeper";
	public static final String RECIPE_FILE_EXTENSION = ".rcp";
	public static final String ALL_FILES_REGEX      = ".*";
	public static final String SELECTED_RECIPE_NAME  = PACKAGE_NAME + ".selected_recipe_name";

	public static final int ID_WHEN_RESOURCE_NOT_FOUND        = -1;
}
