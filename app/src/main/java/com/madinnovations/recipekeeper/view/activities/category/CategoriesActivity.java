package com.madinnovations.recipekeeper.view.activities.category;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.eventhandlers.CategoryEventHandler;
import com.madinnovations.recipekeeper.controller.events.category.CategorySelectedEvent;
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.view.RecipeKeeperApp;
import com.madinnovations.recipekeeper.view.activities.recipesList.RecipesListActivity;
import com.madinnovations.recipekeeper.view.activities.unitsOfMeasure.UnitsOfMeasureActivity;
import com.madinnovations.recipekeeper.view.di.components.ActivityComponent;
import com.madinnovations.recipekeeper.view.di.modules.ActivityModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * Activity to create and manage the Category UI
 */
public class CategoriesActivity extends Activity {
    @Inject
    protected EventBus eventBus;
    @Inject
    protected CategoryEventHandler categoryEventHandler;
    private ActivityComponent activityComponent;
    private CategoriesListFragment listFragment;
    private CategoryDetailFragment detailFragment;
    private boolean dualPane;

    @Override
    protected void onResume() {
        super.onResume();
        if(eventBus != null && !eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Override
    protected void onPause() {
        if(eventBus != null) {
            eventBus.unregister(this);
        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent = ((RecipeKeeperApp) getApplication()).getApplicationComponent()
                .newActivityComponent(new ActivityModule(this));
        activityComponent.injectInto(this);
        if(!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        setContentView(R.layout.categories);

        listFragment = (CategoriesListFragment) getFragmentManager().findFragmentById(R.id.categories_list_fragment);
        detailFragment = (CategoryDetailFragment) getFragmentManager().findFragmentById(R.id.category_detail_fragment);
        dualPane = (detailFragment != null);
        if(!dualPane) {
            listFragment = new CategoriesListFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.categoriesFragmentContainer, listFragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;

        int id = item.getItemId();
        if(id == android.R.id.home) {
            Log.e("CategoriesActivity", "home selected");
            if(!dualPane && detailFragment != null && detailFragment.isVisible()) {
                getFragmentManager().popBackStack();
                result = true;
            }
        }
        if(id == R.id.actionNewCategory){
            eventBus.post(new CategorySelectedEvent(new Category()));
            result = true;
        }
        if(id == R.id.actionManageRecipes){
            Intent intent = new Intent(getApplicationContext(), RecipesListActivity.class);
            startActivity(intent);
        }
        if(id == R.id.actionManageUnitsOfMeasure){
            Intent intent = new Intent(getApplicationContext(), UnitsOfMeasureActivity.class);
            startActivity(intent);
        }
        return result || super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onCategorySelectedEvent(CategorySelectedEvent event) {
        if (!dualPane) {
            if (detailFragment == null) {
                detailFragment = new CategoryDetailFragment();
            }
            getFragmentManager().beginTransaction()
                    .hide(listFragment)
                    .add(R.id.categoriesFragmentContainer, detailFragment)
                    .addToBackStack("listFragment")
                    .commit();
        }
        detailFragment.setCategory(event.getCategory());
    }

    // Getters and setters
    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
