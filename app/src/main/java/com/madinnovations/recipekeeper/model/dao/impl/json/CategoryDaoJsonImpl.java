package com.madinnovations.recipekeeper.model.dao.impl.json;

import com.google.gson.Gson;
import com.madinnovations.recipekeeper.model.dao.CategoryDao;
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.util.Set;

import javax.inject.Singleton;

/**
 * Implementation of the {@link CategoryDao} for maintaining a {@link Category} in a json file.
 */
@Singleton
public class CategoryDaoJsonImpl implements BaseDaoJson, CategoryDao {
    private static final String DIR_NAME = "categories";
    private RecipeKeeperFileHelper fileHelper;

    /**
     * Creates a CategoryDaoJsonImpl instance with the given {@link RecipeKeeperFileHelper} dependency instance.
     *
     * @param fileHelper  the RecipeKeeperFileHelper instance to use
     */
    public CategoryDaoJsonImpl(RecipeKeeperFileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    @Override
    public boolean save(Category category) {
        if(category.getId() == DataConstants.UNINITIALIZED) {
            category.setId(fileHelper.getNextAvailableId(DIR_NAME));
        }
        Gson gson = new Gson();
        String json = gson.toJson(category, Category.class);
        return fileHelper.writeFile(false, DIR_NAME + SEPARATOR + String.format("%019d", category.getId()) + SUFFIX, json.getBytes());
    }

    @Override
    public boolean delete(Category category) {
        return false;
    }

    @Override
    public Set<Category> read(Category filter) {
        return null;
    }

    @Override
    public Category read(long id) {
        return null;
    }
}
