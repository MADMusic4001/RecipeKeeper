package com.madinnovations.recipekeeper.view.activities.category;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.events.category.CategoryPersistenceEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategorySavedEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategorySelectedEvent;
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.view.activities.unitsOfMeasure.UnitsOfMeasureActivity;
import com.madinnovations.recipekeeper.view.di.modules.FragmentModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Manages the interaction with the Category details user interface.
 */
public class CategoryDetailFragment extends Fragment {
    @Inject
    protected EventBus eventBus = null;
    private Category category;
    private EditText nameEdit;
    private EditText descriptionEdit;
    private Button saveButton;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((CategoriesActivity) getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this)).injectInto(this);
        eventBus.register(this);
        View layout = inflater.inflate(R.layout.category_detail_fragment, container, false);
        nameEdit = (EditText)layout.findViewById(R.id.category_name_edit);
        descriptionEdit = (EditText)layout.findViewById(R.id.category_description_edit);
        saveButton = (Button)layout.findViewById(R.id.category_save_button);
        if(category == null) {
            category = new Category();
        }
        initSaveButton();
        copyCategoryToView();
        return layout;
    }

    /**
     * Responds to CategorySelectedEvent by copying the selected Category instance to the UI fields.
     *
     * @param event  a CategorySelectedEvent instance
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySelected(CategorySelectedEvent event) {
        category = event.getCategory();
        copyCategoryToView();
    }

    /**
     * Responds to CategorySavedEvent by displaying a toast informing the user of the result and, if the save was successful, it also
     * copies the save Category instance to the UI fields.
     *
     * @param event  a CategorySavedEvent instance
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySaved(CategorySavedEvent event) {
        String toastString;

        if(event.isSuccess()) {
            category = event.getCategory();
            copyCategoryToView();
            toastString = getString(R.string.toast_category_saved);
        } else {
            toastString = getString(R.string.toast_category_saved_error);
        }
        Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
    }

    private void initSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean changed = false;
                boolean errors = false;
                Category newCategory = new Category();

                newCategory.setId(category.getId());
                newCategory.setName(category.getName());
                newCategory.setDescription(category.getDescription());

                String value = nameEdit.getText().toString();
                if(value.isEmpty()) {
                    nameEdit.setError(getString(R.string.error_required));
                    errors = true;
                } else if(!value.equals(category.getName())) {
                    newCategory.setName(value);
                    changed = true;
                }

                value = descriptionEdit.getText().toString();
                if(!value.equals(category.getDescription())) {
                    newCategory.setDescription(value);
                    changed = true;
                }

                if(changed && !errors) {
                    eventBus.post(new CategoryPersistenceEvent(CategoryPersistenceEvent.Action.SAVE, newCategory));
                }
            }
        });
    }

    private void copyCategoryToView() {
        if(category == null) {
            this.nameEdit.setText(null);
            this.descriptionEdit.setText(null);
        } else {
            this.nameEdit.setText(category.getName());
            this.descriptionEdit.setText(category.getDescription());
        }
    }

    // Getters and setters
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}
