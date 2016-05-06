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
import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;
import com.madinnovations.recipekeeper.view.di.PerActivity;

import javax.inject.Inject;

/**
 * Adapts {@link UnitOfMeasure} data to a {@link android.widget.ListView}.
 */
@PerActivity
public class UnitOfMeasureListAdapter extends ArrayAdapter<UnitOfMeasure> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.recipe_list_row;
	private LayoutInflater layoutInflater;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	/**
	 * Creates a new UnitOfMeasureListAdapter instance
	 *
	 * @param context  the view {@code Context} to which the categoryListAdapter will be attached
	 * @param layoutInflater  a {@link LayoutInflater} instance to use to inflate the header and row layouts from xml
	 */
	@Inject
	public UnitOfMeasureListAdapter(Context context, LayoutInflater layoutInflater) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = layoutInflater;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if(convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView)rowView.findViewById(R.id.singularNameView),
									(TextView)rowView.findViewById(R.id.pluralNameView));
			rowView.setTag(holder);
		} else {
			rowView = convertView;
			holder = (ViewHolder)rowView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(getContext(), colors[position % colors.length]));
		final UnitOfMeasure unitOfMeasure = getItem(position);
		holder.singularNameView.setText(unitOfMeasure.getSingularName());
		holder.pluralNameView.setText(unitOfMeasure.getPluralName());
		return rowView;
	}

	private static class ViewHolder {
		private TextView singularNameView;
		private TextView pluralNameView;

		public ViewHolder(TextView singularNameView, TextView pluralNameView) {
			this.singularNameView = singularNameView;
			this.pluralNameView = pluralNameView;
		}
	}
}
