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
package com.madinnovations.recipekeeper.view.activities.recipeDetail;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.events.category.CategoriesLoadedEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategoryPersistenceEvent;
import com.madinnovations.recipekeeper.controller.events.recipe.RecipePersistenceEvent;
import com.madinnovations.recipekeeper.controller.events.recipe.RecipeSavedEvent;
import com.madinnovations.recipekeeper.controller.events.recipe.RecipeSelectedEvent;
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.model.entities.Recipe;
import com.madinnovations.recipekeeper.model.utils.DataConstants;
import com.madinnovations.recipekeeper.model.utils.DateUtils;
import com.madinnovations.recipekeeper.model.utils.StringUtils;
import com.madinnovations.recipekeeper.view.activities.recipesList.RecipesListActivity;
import com.madinnovations.recipekeeper.view.adapters.CategoryListAdapter;
import com.madinnovations.recipekeeper.view.adapters.CategorySpinnerAdapter;
import com.madinnovations.recipekeeper.view.adapters.IngredientListAdapter;
import com.madinnovations.recipekeeper.view.adapters.UnitOfMeasureSpinnerAdapter;
import com.madinnovations.recipekeeper.view.di.modules.FragmentModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Manages the interaction with the Recipe details user interface.
 */
public class RecipeDetailFragment extends Fragment {
	@Inject
	protected CategorySpinnerAdapter categorySpinnerAdapter;
	@Inject
	protected IngredientListAdapter ingredientListAdapter;
	@Inject
	protected UnitOfMeasureSpinnerAdapter uomSpinnerAdapter;
	@Inject
	protected EventBus              eventBus;
	private   Recipe              recipe = new Recipe();
	private   EditText            nameEdit;
	private   EditText            descriptionEdit;
	private   EditText            directionsEdit;
	private   EditText            notesEdit;
	private   EditText            sourceEdit;
	private   LinearLayout        addCategoryLayout;
	private   LinearLayout        categoriesLayout;
	private   LinearLayout        ingredientsLayout;
	private   TextView            createdView;
	private   TextView            updatedView;
	private   Button              saveButton;

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

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(getActivity() instanceof  RecipesListActivity) {
			((RecipesListActivity) getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this))
					.injectInto(this);
		} else {
			((RecipeDetailActivity) getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this))
					.injectInto(this);
		}
		eventBus.register(this);
		View layout = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
		nameEdit = (EditText)layout.findViewById(R.id.recipe_name_edit);
		descriptionEdit = (EditText)layout.findViewById(R.id.recipe_description_edit);
		directionsEdit = (EditText)layout.findViewById(R.id.recipe_directions_edit);
		notesEdit = (EditText)layout.findViewById(R.id.recipe_notes_edit);
		sourceEdit = (EditText)layout.findViewById(R.id.recipe_source_edit);
		createdView = (TextView) layout.findViewById(R.id.recipe_created_text);
		updatedView = (TextView) layout.findViewById(R.id.recipe_updated_text);
		addCategoryLayout = (LinearLayout)layout.findViewById(R.id.add_category_layout);
		categoriesLayout = (LinearLayout)layout.findViewById(R.id.categories_layout);

		saveButton = (Button)layout.findViewById(R.id.recipe_save_button);

		initSaveButton();
		initCategoryLayout();
		initIngredientsLayout();
		eventBus.post(new CategoryPersistenceEvent(CategoryPersistenceEvent.Action.READ_BY_FILTER, null));
		return layout;
	}

	/**
	 * Handles a RecipeSelectedEvent by copying the Recipe data to the UI.
	 *
	 * @param event  a RecipeSelectedEvent instance
     */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRecipeSelectedEvent(RecipeSelectedEvent event) {
		this.recipe = event.getRecipe();
		copyRecipeToView();
	}

	/**
	 * Handles a RecipeSavedEvent by displaying a success/failure message to the user with a toast and copy the saved recipe to the UI
	 * fields if the save was successful.
	 *
	 * @param event  a RecipeSavedEvent
     */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRecipeSavedEvent(RecipeSavedEvent event) {
		String toastString;

		if(event.isSuccess()) {
			this.recipe = event.getRecipe();
			copyRecipeToView();
			toastString = getString(R.string.toast_recipe_saved);
		} else {
			toastString = getString((R.string.toast_recipe_saved_error));
		}
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_LONG).show();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onCategoriesLoaded(CategoriesLoadedEvent event) {
		categorySpinnerAdapter.clear();
		if(event.isSuccessful() || event.getCategorySet() == null || event.getCategorySet().size() == 0) {
			categorySpinnerAdapter.add(new Category("Select Category"));
			categorySpinnerAdapter.addAll(event.getCategorySet());
			categorySpinnerAdapter.notifyDataSetChanged();
			Log.e("RecipeDetailFragment", "adapter dataset size = " + categorySpinnerAdapter.getCount());
		}
		else {
			categorySpinnerAdapter.notifyDataSetInvalidated();
		}
	}

	private void initSaveButton() {
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean changed = false;
				boolean errors = false;
				Recipe newRecipe = new Recipe();

				newRecipe.setId(recipe.getId());
				newRecipe.setName(recipe.getName());
				newRecipe.setDescription(recipe.getDescription());
				newRecipe.setDirections(recipe.getDirections());
				newRecipe.setNotes(recipe.getNotes());
				newRecipe.setSource(recipe.getSource());
				if(recipe.getCreated() == null) {
					newRecipe.setCreated(Calendar.getInstance());
					newRecipe.setUpdated(newRecipe.getCreated());
				} else {
					newRecipe.setCreated(recipe.getCreated());
					newRecipe.setUpdated(Calendar.getInstance());
				}

				String value = nameEdit.getText().toString();
				if(value.isEmpty()) {
					nameEdit.setError(getString(R.string.error_required));
					errors = true;
				} else if(!value.equals(recipe.getName())) {
					newRecipe.setName(value);
					changed = true;
				}
				value = descriptionEdit.getText().toString();
				if(!StringUtils.equals(value, recipe.getDescription())) {
					newRecipe.setDescription(value);
					changed = true;
				}
				value = directionsEdit.getText().toString();
				if(!StringUtils.equals(value, recipe.getDirections())) {
					newRecipe.setDirections(value);
					changed = true;
				}
				value = notesEdit.getText().toString();
				if(!StringUtils.equals(value, recipe.getNotes())) {
					newRecipe.setNotes(value);
					changed = true;
				}
				value = sourceEdit.getText().toString();
				if(!StringUtils.equals(value, recipe.getSource())) {
					newRecipe.setSource(value);
					changed = true;
				}

				if(changed && !errors) {
					eventBus.post(new RecipePersistenceEvent(RecipePersistenceEvent.Action.SAVE, newRecipe));
				}
			}
		});
	}

	private void initCategoryLayout() {
		final Spinner categorySpinner = (Spinner)addCategoryLayout.findViewById(R.id.add_category_spinner);
		ImageButton addCategoryButton = (ImageButton)addCategoryLayout.findViewById(R.id.add_category_button);
		addCategoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Category category = (Category)categorySpinner.getSelectedItem();
				if(category.getId() != DataConstants.UNINITIALIZED) {
					recipe.getCategories().add(category);
					TextView textView = (TextView)getActivity().getLayoutInflater().inflate(R.layout.category_list_view,
							categoriesLayout, false);
					textView.setText(category.getName());
					textView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// TODO: implement removing the category from the Recipe and adding it back to the spinner
						}
					});
					categoriesLayout.addView(textView);
					categorySpinnerAdapter.remove(category);
					if(categorySpinnerAdapter.getCount() == 0) {
						categorySpinnerAdapter.notifyDataSetInvalidated();
					}
					else {
						categorySpinnerAdapter.notifyDataSetChanged();
					}
				}
			}
		});
		categorySpinner.setAdapter(categorySpinnerAdapter);
		categorySpinnerAdapter.setDropDownViewResource(R.layout.category_spinner_row);

		// Create and set onClick methods for sorting the {@link World} list.
//		headerView.findViewById(R.id.nameHeader).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				controller.sortRecipesByName();
//			}
//		});
//		headerView.findViewById(R.id.categoriesHeader).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
//		headerView.findViewById(R.id.createdHeader).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
//		View updatedView = headerView.findViewById(R.id.updatedHeader);
//		if(updatedView != null) {
//			updatedView.setOnClickListener(new View.OnClickListener
//					() {
//				@Override
//				public void onClick(View v) {
////					controller.sortRecipesByUpdated();
//				}
//			});
//		}
//		categoriesList.setAdapter(categoryListAdapter);

		// Clicking a row in the listView will send the user to the recipes details activity
//		categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Recipe theRecipe = (Recipe) listView.getItemAtPosition(position);
//				if (theRecipe != null) {
//					editRecipe(theRecipe);
//				}
//			}
//		});
//		registerForContextMenu(categoriesList);
	}

	private void initIngredientsLayout() {
		List<String> list = new ArrayList<String>();
		ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(getActivity(),
																			   android.R.layout.simple_spinner_item) {

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				View v = null;

				if(position == 0) {
					TextView textView = new TextView(getContext());
					textView.setHeight(0);
					textView.setVisibility(View.GONE);
					v = textView;
				}
				else {
					v = super.getDropDownView(position, null, parent);
				}

				parent.setVerticalScrollBarEnabled(false);
				return v;
			}
		};

		categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Create and set onClick methods for sorting the {@link World} list.
//		headerView.findViewById(R.id.nameHeader).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				controller.sortRecipesByName();
//			}
//		});
//		headerView.findViewById(R.id.categoriesHeader).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
//		headerView.findViewById(R.id.createdHeader).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
//		View updatedView = headerView.findViewById(R.id.updatedHeader);
//		if(updatedView != null) {
//			updatedView.setOnClickListener(new View.OnClickListener
//					() {
//				@Override
//				public void onClick(View v) {
////					controller.sortRecipesByUpdated();
//				}
//			});
//		}
//		categoriesList.setAdapter(categoryListAdapter);

		// Clicking a row in the listView will send the user to the recipes details activity
//		categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Recipe theRecipe = (Recipe) listView.getItemAtPosition(position);
//				if (theRecipe != null) {
//					editRecipe(theRecipe);
//				}
//			}
//		});
//		registerForContextMenu(categoriesList);
	}

	private void copyRecipeToView() {
		if(this.recipe == null) {
			this.nameEdit.setText(null);
			this.descriptionEdit.setText(null);
			this.directionsEdit.setText(null);
			this.notesEdit.setText(null);
			this.sourceEdit.setText(null);
			this.createdView.setText(null);
			this.updatedView.setText(null);
		} else {
			this.nameEdit.setText(this.recipe.getName());
			this.descriptionEdit.setText(this.recipe.getDescription());
			this.directionsEdit.setText(this.recipe.getDirections());
			this.notesEdit.setText(this.recipe.getNotes());
			this.sourceEdit.setText(this.recipe.getSource());
			this.createdView.setText(String.format(getString(R.string.label_created_on),
						DateUtils.getFormattedDateOrTime(getActivity(), this.recipe.getCreated().getTimeInMillis())));
			this.updatedView.setText(String.format(getString(R.string.label_last_updated),
						DateUtils.getFormattedDateOrTime(getActivity(), this.recipe.getUpdated().getTimeInMillis())));
		}
//		categoryListAdapter.notifyDataSetChanged();
	}

	// Getters and setters
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
}
