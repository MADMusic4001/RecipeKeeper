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
package com.madinnovations.recipekeeper.view.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.model.entities.Ingredient;
import com.madinnovations.recipekeeper.model.utils.NumberUtils;
import com.madinnovations.recipekeeper.view.di.PerActivity;

import javax.inject.Inject;

/**
 * Adapts {@link Ingredient} data to a {@link android.widget.ListView}
 */
@PerActivity
public class IngredientListAdapter extends ArrayAdapter<Ingredient> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.ingredient_list_row;
	private LayoutInflater layoutInflater;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	/**
	 * Creates a new IngredientListAdapter instance.
	 *
	 * @param context  the view {@code Context} to which this IngredientListAdapter will be attached
	 * @param layoutInflater  a {@link LayoutInflater} instance to use to inflate the header and row layouts from xml
	 */
	@Inject
	public IngredientListAdapter(Context context, LayoutInflater layoutInflater) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = layoutInflater;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((EditText) rowView.findViewById(R.id.ingredient_value_edit),
									(Spinner) rowView.findViewById(R.id.ingredient_uom_spinner),
									(EditText) rowView.findViewById(R.id.ingredient_name_edit));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(getContext(), colors[position % colors.length]));
		final Ingredient ingredient = getItem(position);
		holder.valueEdit.setText(NumberUtils.toDisplayString(getContext().getResources().getConfiguration().locale,
															 ingredient.getValue()));
		holder.nameEdit.setText(ingredient.getName());
		holder.nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					String enteredName = ((EditText)v).getText().toString();
					Ingredient ingredient = getItem(position);
					if(!ingredient.getName().equals(enteredName)) {
						ingredient.setName(enteredName);
					}
				}
				else {
					((EditText)v).selectAll();
				}
			}
		});
		return rowView;
	}

	private static class ViewHolder {
		private EditText valueEdit;
		private Spinner  uomSpinner;
		private EditText nameEdit;

		ViewHolder(EditText valueEdit, Spinner uomSpinner, EditText nameEdit) {
			this.valueEdit = valueEdit;
			this.uomSpinner = uomSpinner;
			this.nameEdit = nameEdit;
		}
	}
}
