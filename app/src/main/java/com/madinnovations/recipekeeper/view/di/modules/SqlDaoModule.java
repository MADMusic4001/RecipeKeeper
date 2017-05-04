package com.madinnovations.recipekeeper.view.di.modules;

import android.content.Context;

import com.madinnovations.recipekeeper.model.dao.CategoryDao;
import com.madinnovations.recipekeeper.model.dao.IngredientDao;
import com.madinnovations.recipekeeper.model.dao.RecipeDao;
import com.madinnovations.recipekeeper.model.dao.UnitOfMeasureDao;
import com.madinnovations.recipekeeper.model.dao.impl.sql.CategoryDaoSqlImpl;
import com.madinnovations.recipekeeper.model.dao.impl.sql.IngredientDaoSqlImpl;
import com.madinnovations.recipekeeper.model.dao.impl.sql.RecipeDaoSqlImpl;
import com.madinnovations.recipekeeper.model.dao.impl.sql.RecipeKeeperSqlHelper;
import com.madinnovations.recipekeeper.model.dao.impl.sql.UnitOfMeasureDaoSqlImpl;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Class used by Dagger dependency injection framework to provide instances of DAO classes to inject into classes that depend on
 * them.
 */
@Module(includes = ApplicationModule.class)
public class SqlDaoModule {
    /**
     * Creates a {@link RecipeKeeperSqlHelper} instance.
     *
     * @param context  the android context needed by RecipeKeeperSqlHelper
     * @return  a new instance of RecipeKeeperSqlHelper
     */
    @Provides @Singleton
    public RecipeKeeperSqlHelper provideRecipeKeeperSqlHelper(Context context) {
        return new RecipeKeeperSqlHelper(context, DataConstants.DB_NAME, null, DataConstants.DB_VERSION, null);
    }

    /**
     * Creates a {@link UnitOfMeasureDao} instance.
     *
     * @param sqlHelper  the RecipeKeeperSqlHelper instance needed by UnitOfMeasuerDaoSqlImpl
     * @return  a new instance of UnitOfMeasureDaoSqlImpl
     */
    @Provides @Singleton
    public UnitOfMeasureDao provideUnitOfMeasureDao(RecipeKeeperSqlHelper sqlHelper) {
        return new UnitOfMeasureDaoSqlImpl(sqlHelper);
    }

    /**
     * Creates a {@link CategoryDao} instance.
     *
     * @param sqlHelper  the RecipeKeeperSqlHelper instance needed by CategoryDaoSqlImpl
     * @return  a new instance of CategoryDaoSqlImpl
     */
    @Provides @Singleton
    public CategoryDao provideCategoryDao(RecipeKeeperSqlHelper sqlHelper) {
        return new CategoryDaoSqlImpl(sqlHelper);
    }

    /**
     * Creates a {@link IngredientDao} instance.
     *
     * @param sqlHelper  the RecipeKeeperSqlHelper instance needed by IngredientDaoSqlImp
     * @return  a new instance of IngredientDaoSqlImp
     */
    @Provides @Singleton
    public IngredientDao provideIngredientDao(RecipeKeeperSqlHelper sqlHelper, RecipeDao recipeDao,
                                              UnitOfMeasureDao unitOfMeasureDao) {
        return new IngredientDaoSqlImpl(sqlHelper, recipeDao, unitOfMeasureDao);
    }

    /**
     * Creates a {@link RecipeDao} instance.
     *
     * @param sqlHelper  the RecipeKeeperSqlHelper instance needed by RecipeDaoSqlImpl
     * @param categoryDao  the CategoryDao instance needed by RecipeDaoSqlImpl
     * @param ingredientDao  the IngredientDao instance needed by RecipeDaoSqlImpl
     * @return  a new instance of RecipeDaoSqlImpl
     */
    @Provides @Singleton
    public RecipeDao provideRecipeDao(RecipeKeeperSqlHelper sqlHelper, CategoryDao categoryDao, IngredientDao ingredientDao) {
        return new RecipeDaoSqlImpl(sqlHelper, categoryDao, ingredientDao);
    }
}
