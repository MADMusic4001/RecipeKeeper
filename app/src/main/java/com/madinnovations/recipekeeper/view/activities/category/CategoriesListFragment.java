package com.madinnovations.recipekeeper.view.activities.category;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.events.category.CategoriesLoadedEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategoryDeletedEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategoryPersistenceEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategorySavedEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategorySelectedEvent;
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.view.adapters.CategoryListAdapter;
import com.madinnovations.recipekeeper.view.di.modules.FragmentModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Manages the interaction with the Categories list user interface.
 */
public class CategoriesListFragment extends Fragment {
    @Inject
    protected EventBus eventBus = null;
    @Inject
    protected CategoryListAdapter adapter;
    private ListView listView;

    @Override
    public void onResume() {
        super.onResume();
        if(eventBus != null && !eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Override
    public void onPause() {
        if(eventBus != null) {
            eventBus.unregister(this);
        }
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.category_list_context_menu, contextMenu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Category category;

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.category_context_edit:
                category = (Category) listView.getItemAtPosition(info.position);
                if(category != null) {
                    eventBus.post(new CategorySelectedEvent(category));
                    return true;
                }
                else {
                    return false;
                }
            case R.id.category_context_delete:
                category = (Category) listView.getItemAtPosition(info.position);
                if(category != null) {
                    eventBus.post(new CategoryPersistenceEvent(CategoryPersistenceEvent.Action.DELETE, category));
                    return true;
                }
                else {
                    return false;
                }
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((CategoriesActivity)getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this)).injectInto(this);
        if(!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        View layout = inflater.inflate(R.layout.categories_list_fragment, container, false);
        listView = (ListView)layout.findViewById(R.id.categoryList);
        initListView();
        eventBus.post(new CategoryPersistenceEvent(CategoryPersistenceEvent.Action.READ_BY_FILTER, null));
        return layout;
    }

    /**
     * Sets the list of Category instances in the display list.
     *
     * @param event  a CategoriesLoadedEvent instance
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoriesLoaded(CategoriesLoadedEvent event) {
        String toastString;

        adapter.clear();
        if(event.isSuccessful()) {
            toastString = getString(R.string.toast_categories_loaded);
            adapter.addAll(event.getCategorySet());
        } else {
            toastString = getString(R.string.toast_categories_loaded_error);
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
    }

    /**
     * Removes a Category from the UI list view when it is deleted from persistent storage.
     *
     * @param event  a CategoryDeletedEvent instance
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryDeleted(CategoryDeletedEvent event) {
        String toastString;

        if(event.isSuccessful()) {
            toastString = getString(R.string.toast_category_deleted);
            adapter.remove(event.getCategory());
            adapter.notifyDataSetChanged();
        } else {
            toastString = getString(R.string.toast_category_deleted_error);
        }
        Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
    }

    /**
     * Adds newly saved Category instances to the display list
     *
     * @param event  a CategorySavedEvent instance
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySaved(CategorySavedEvent event) {
        if(event.isSuccess()) {
            adapter.add(event.getCategory());
            adapter.notifyDataSetChanged();
        }
    }

    private void initListView() {
        View headerView;

        headerView = getActivity().getLayoutInflater().inflate(
                R.layout.categories_list_header, listView, false);
        listView.addHeaderView(headerView);

        // Create and set onClick methods for sorting the {@link World} list.
        headerView.findViewById(R.id.nameHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				controller.sortRecipesByName();
            }
        });
        headerView.findViewById(R.id.descriptionHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				controller.sortRecipesByCategories();
            }
        });
        listView.setAdapter(adapter);

        // Clicking a row in the listView will send the user to the recipes details activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category item = (Category) listView.getItemAtPosition(position);
                if (item != null) {
                    eventBus.post(new CategorySelectedEvent(item));
                }
            }
        });
        registerForContextMenu(listView);
    }
}
