package com.madinnovations.recipekeeper.model.dao.impl.json;

import com.google.gson.Gson;
import com.madinnovations.recipekeeper.model.dao.CategoryDao;
import com.madinnovations.recipekeeper.model.dao.IngredientDao;
import com.madinnovations.recipekeeper.model.dao.RecipeDao;
import com.madinnovations.recipekeeper.model.entities.Recipe;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.util.Set;

/**
 * Implementation of the {@link RecipeDao} for maintaining a {@link Recipe} in a json file.
 */
public class RecipeDaoJsonImpl implements BaseDaoJson, RecipeDao {
    private static final String DIR_NAME = "recipes";
    private RecipeKeeperFileHelper fileHelper;
    private CategoryDao categoryDao;
    private IngredientDao ingredientDao;

    /**
     * Creates a RecipeDaoJsonImpl instance with the given {@link RecipeKeeperFileHelper} dependency instance.
     *
     * @param fileHelper  the RecipeKeeperFileHelper instance to use
	 * @param categoryDao  a CategoryDao instance
	 * @param ingredientDao   an IngredientDao instance
     */
    public RecipeDaoJsonImpl(RecipeKeeperFileHelper fileHelper, CategoryDao categoryDao,
                             IngredientDao ingredientDao) {
        this.fileHelper = fileHelper;
        this.categoryDao = categoryDao;
        this.ingredientDao = ingredientDao;
    }

    @Override
    public boolean save(Recipe recipe) {
        if(recipe.getId() == DataConstants.UNINITIALIZED) {
            recipe.setId(fileHelper.getNextAvailableId(DIR_NAME));
        }
        Gson gson = new Gson();
        String json = gson.toJson(recipe, Recipe.class);
        return fileHelper.writeFile(false, String.format("%019d", recipe.getId()) + SUFFIX, json.getBytes());
    }

    @Override
    public boolean delete(Recipe recipe) {
        return false;
    }

    @Override
    public Set<Recipe> read(Recipe filter) {
        return null;
    }

    @Override
    public Recipe read(int id) {
        return null;
    }
}
