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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;
import com.madinnovations.recipekeeper.view.di.PerActivity;

import javax.inject.Inject;

/**
 * Adapts {@link UnitOfMeasure} data to a {@link android.widget.Spinner}
 */
@PerActivity
public class UnitOfMeasureSpinnerAdapter extends ArrayAdapter<UnitOfMeasure> {
	private   LayoutInflater       layoutInflater;

	/**
	 * Creates a new UnitOfMeasureSpinnerAdapter instance.
	 *
	 * @param context  the view {@code Context} to which this UnitOfMeasureSpinnerAdapter will be attached
	 * @param layoutInflater  a {@link LayoutInflater} instance to use to inflate the header and row layouts from xml
	 */
	@Inject
	public UnitOfMeasureSpinnerAdapter(Context context, LayoutInflater layoutInflater) {
		super(context, android.R.layout.simple_spinner_item);
		this.layoutInflater = layoutInflater;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView;
		ViewHolder holder;

		if (convertView == null) {
			itemView = layoutInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
			holder = new ViewHolder((TextView)itemView.findViewById(android.R.id.text1));
			itemView.setTag(holder);
		} else {
			itemView = convertView;
			holder = (ViewHolder)convertView.getTag();
		}

		holder.nameText.setText(getItem(position).toString());
		return itemView;
	}

	private static class ViewHolder {
		private TextView  nameText;

		public ViewHolder(TextView nameText) {
			this.nameText = nameText;
		}
	}
}
