/**
 * Copyright (C) 2016 MadMusic4001
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
import android.widget.TextView;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.model.entities.Recipe;
import com.madinnovations.recipekeeper.model.utils.DateUtils;
import com.madinnovations.recipekeeper.view.di.PerActivity;

import javax.inject.Inject;

/**
 * This class adapts Recipe data to a user interface list view.
 */
@PerActivity
public class RecipeListAdapter extends ArrayAdapter<Recipe> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.recipe_list_row;

	private   LayoutInflater       layoutInflater;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	/**
	 * Creates a new RecipeListAdapter instance.
	 *
	 * @param context the view {@code Context} the adapter will be attached to.
	 */
	@Inject
	public RecipeListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.nameView),
									(TextView) rowView.findViewById(R.id.categoriesView),
									(TextView) rowView.findViewById(R.id.createdView),
									(TextView) rowView.findViewById(R.id.updatedView));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(getContext(), colors[position % colors.length]));
		final Recipe recipe = getItem(position);
		holder.nameView.setText(recipe.getName());
		holder.categoriesView.setText(recipe.getCategoriesAsText());
		holder.createdView.setText(DateUtils.getFormattedDateOrTime(getContext(), recipe.getCreated().getTimeInMillis()));
		if (holder.updatedView != null) {
			holder.updatedView.setText(DateUtils.getFormattedDateOrTime(getContext(), recipe.getUpdated().getTimeInMillis()));
		}
		holder.nameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					String enteredName = ((EditText)v).getText().toString();
					Recipe recipe = getItem(position);
					if(!recipe.getName().equals(enteredName)) {
						recipe.setName(enteredName);
					}
				}
				else {
					((EditText)v).selectAll();
				}
			}
		});
		return rowView;
	}

	private class ViewHolder {
		private TextView nameView;
		private TextView categoriesView;
		private TextView createdView;
		private TextView updatedView;

		ViewHolder(TextView nameView, TextView categoriesView, TextView createdView, TextView updatedView) {
			this.nameView = nameView;
			this.categoriesView = categoriesView;
			this.createdView = createdView;
			this.updatedView = updatedView;
		}
	}
}
