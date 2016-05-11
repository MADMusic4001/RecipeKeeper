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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.model.entities.Category;
import com.madinnovations.recipekeeper.model.utils.DataConstants;
import com.madinnovations.recipekeeper.view.di.PerActivity;

import javax.inject.Inject;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 5/10/2016.
 */
@PerActivity
public class CategorySpinnerAdapter extends ArrayAdapter<Category> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.category_spinner_row;
	LayoutInflater layoutInflater;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	@Inject
	public CategorySpinnerAdapter(Context context, LayoutInflater layoutInflater) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = layoutInflater;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.category_name_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		final Category category = getItem(position);
//		if(category.getId() == DataConstants.UNINITIALIZED) {
////			holder.nameView.setHeight(0);
//			holder.nameView.setVisibility(View.GONE);
//		}
//		else {
//			holder.nameView.setHeight(0);
			holder.nameView.setVisibility(View.VISIBLE);
			rowView.setBackgroundColor(ContextCompat.getColor(getContext(), colors[position % colors.length]));
			holder.nameView.setText(category.getName());
//		}

		parent.setVerticalScrollBarEnabled(false);

		return rowView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.category_name_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		final Category category = getItem(position);
		if(category.getId() == DataConstants.UNINITIALIZED) {
//			holder.nameView.setHeight(0);
			holder.nameView.setVisibility(View.GONE);
		}
		else {
			rowView.setBackgroundColor(ContextCompat.getColor(getContext(), colors[position % colors.length]));
			holder.nameView.setVisibility(View.VISIBLE);
			holder.nameView.setText(category.getName());
		}

		parent.setVerticalScrollBarEnabled(false);

		return rowView;
	}

	private static class ViewHolder {
		TextView nameView;

		public ViewHolder(TextView nameView) {
			this.nameView = nameView;
		}
	}
}
