package com.madinnovations.recipekeeper.model.dao.impl.json;

import com.google.gson.Gson;
import com.madinnovations.recipekeeper.model.dao.IngredientDao;
import com.madinnovations.recipekeeper.model.entities.Ingredient;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.util.Set;

import javax.inject.Singleton;

/**
 * Implementation of the {@link IngredientDao} for maintaining a {@link Ingredient} in a json file.
 */
@Singleton
public class IngredientDaoJsonImpl implements BaseDaoJson, IngredientDao {
    private static final String DIR_NAME = "ingredients";
    private RecipeKeeperFileHelper fileHelper;

    /**
     * Creates a IngredientDaoJsonImpl instance with the given {@link RecipeKeeperFileHelper} dependency instance.
     *
     * @param fileHelper  the RecipeKeeperFileHelper instance to use
     */
    public IngredientDaoJsonImpl(RecipeKeeperFileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    @Override
    public boolean save(Ingredient ingredient) {
        if(ingredient.getId() == DataConstants.UNINITIALIZED) {
            ingredient.setId(fileHelper.getNextAvailableId(DIR_NAME));
        }
        Gson gson = new Gson();
        String json = gson.toJson(ingredient, Ingredient.class);
        return fileHelper.writeFile(false, String.format("%019d", ingredient.getId()) + SUFFIX, json.getBytes());
    }

    @Override
    public boolean delete(Ingredient ingredient) {
        return false;
    }

    @Override
    public Set<Ingredient> read(Ingredient filter) {
        return null;
    }

    @Override
    public Ingredient read(long id) {
        return null;
    }
}
