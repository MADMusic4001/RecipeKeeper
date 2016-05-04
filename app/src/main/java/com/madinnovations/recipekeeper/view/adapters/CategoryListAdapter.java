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
import android.widget.TextView;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.view.di.PerActivity;

import javax.inject.Inject;

/**
 * Adapts Category data to a {@link android.widget.ListView}
 */
@PerActivity
public class CategoryListAdapter extends ArrayAdapter<Category> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.category_list_row;
	private LayoutInflater layoutInflater;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	/**
	 * Creates a new CategoryListAdapter instance
	 *
	 * @param context  the view {@code Context} to which the adapter will be attached
	 * @param layoutInflater  a {@link LayoutInflater} instance to use to inflate the footer and row layouts from xml
	 */
	@Inject
	public CategoryListAdapter(Context context, LayoutInflater layoutInflater) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = layoutInflater;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.categoy_name_text));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(getContext(), colors[position % colors.length]));
		final Category category = getItem(position);
		holder.nameView.setText(category.getName());
		return rowView;
	}

	private class ViewHolder {
		private TextView nameView;

		ViewHolder(TextView nameView) {
			this.nameView = nameView;
		}
	}
}
