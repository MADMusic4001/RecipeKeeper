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
package com.madinnovations.recipekeeper.model.dao.impl.sql;

/**
 * Interface declaring commonly used SQL string constants
 */
public interface BaseDaoSql {
	public static final String CREATE_TABLE = "CREATE TABLE ";
	public static final String TEXT = " TEXT ";
	public static final String INTEGER = " INTEGER ";
	public static final String REAL = " REAL ";
	public static final String NOT_NULL = " NOT NULL ";
	public static final String PRIMARY_KEY = " PRIMARY KEY ";
	public static final String FOREIGN_KEY = " FOREIGN KEY ";
	public static final String COMMA = ",";
	public static final String SPACE = " ";
	public static final String CONSTRAINT = "CONSTRAINT ";
	public static final String REFERENCES = "REFERENCES ";
	public static final String SELECT = "SELECT";
	public static final String INSERT = "INSERT";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";
	public static final String FROM = "FROM";
	public static final String WHERE = "WHERE";
	public static final String ON = " ON ";
	public static final String RESTRICT = " RESTRICT ";
	public static final String CASCADE = " CASCADE ";
	public static final String EQUALS = "=";
	public static final String NOT = "!";
	public static final String AND = "and";
	public static final String PLACEHOLDER = "?";
}
