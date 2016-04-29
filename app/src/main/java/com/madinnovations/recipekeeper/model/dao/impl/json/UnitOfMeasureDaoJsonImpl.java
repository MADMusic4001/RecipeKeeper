package com.madinnovations.recipekeeper.model.dao.impl.json;

import com.google.gson.Gson;
import com.madinnovations.recipekeeper.model.dao.UnitOfMeasureDao;
import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;
import com.madinnovations.recipekeeper.model.utils.DataConstants;

import java.util.Set;

import javax.inject.Singleton;

/**
 * Implementation of the {@link UnitOfMeasureDao} for maintaining a {@link UnitOfMeasure} in a json file.
 */
@Singleton
public class UnitOfMeasureDaoJsonImpl implements BaseDaoJson, UnitOfMeasureDao {
    private static final String DIR_NAME = "unitsOfMeasure";
    private RecipeKeeperFileHelper fileHelper;

    /**
     * Creates a UnitOfMeasureDaoJsonImpl instance with the given {@link RecipeKeeperFileHelper} dependency instance.
     *
     * @param fileHelper  the RecipeKeeperFileHelper instance to use
     */
    public UnitOfMeasureDaoJsonImpl(RecipeKeeperFileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    @Override
    public boolean save(UnitOfMeasure uom) {
        if(uom.getId() == DataConstants.UNINITIALIZED) {
            uom.setId(fileHelper.getNextAvailableId(DIR_NAME));
        }
        Gson gson = new Gson();
        String json = gson.toJson(uom, UnitOfMeasure.class);
        return fileHelper.writeFile(false, String.format("%019d", uom.getId()) + SUFFIX, json.getBytes());
    }

    @Override
    public boolean delete(UnitOfMeasure uom) {
        return false;
    }

    @Override
    public Set<UnitOfMeasure> read(UnitOfMeasure filter) {
        return null;
    }

    @Override
    public UnitOfMeasure read(int id) {
        return null;
    }
}
