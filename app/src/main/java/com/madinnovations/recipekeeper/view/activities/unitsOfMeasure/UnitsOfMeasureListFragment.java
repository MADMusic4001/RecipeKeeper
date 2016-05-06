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
package com.madinnovations.recipekeeper.view.activities.unitsOfMeasure;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.events.LoadUnitsOfMeasureEvent;
import com.madinnovations.recipekeeper.controller.events.UnitOfMeasureSavedEvent;
import com.madinnovations.recipekeeper.view.adapters.UnitOfMeasureListAdapter;
import com.madinnovations.recipekeeper.view.di.modules.FragmentModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 5/5/2016.
 */
public class UnitsOfMeasureListFragment extends Fragment {
	@Inject
	protected EventBus eventBus = null;
	@Inject
	protected UnitOfMeasureListAdapter adapter;
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

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((UnitsOfMeasureActivity)getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this))
				.injectInto(this);
		if(!eventBus.isRegistered(this)) {
			eventBus.register(this);
		}
		View layout = inflater.inflate(R.layout.unit_of_measure_detail_fragment, container, false);
		listView = (ListView)layout.findViewById(R.id.rlf_list_view);
		initListView();
		eventBus.post(new LoadUnitsOfMeasureEvent(null));
		return layout;
	}

	private void initListView() {

	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onUnitOfMeasureSave(UnitOfMeasureSavedEvent event) {

	}
}
