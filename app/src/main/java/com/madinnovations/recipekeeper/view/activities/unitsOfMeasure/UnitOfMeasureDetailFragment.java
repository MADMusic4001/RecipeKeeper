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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.controller.events.unitofmeasure.UnitOfMeasurePersistenceEvent;
import com.madinnovations.recipekeeper.controller.events.unitofmeasure.UnitOfMeasureSavedEvent;
import com.madinnovations.recipekeeper.controller.events.unitofmeasure.UnitOfMeasureSelectedEvent;
import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;
import com.madinnovations.recipekeeper.model.utils.StringUtils;
import com.madinnovations.recipekeeper.view.di.modules.FragmentModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Manages the interaction with the Unit of Measure details user interface.
 */
public class UnitOfMeasureDetailFragment extends Fragment{
	@Inject
	protected EventBus eventBus = null;
	private UnitOfMeasure unitOfMeasure;
	private EditText singularNameEdit;
	private EditText pluralNameEdit;
	private EditText notesEdit;
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
		if(getActivity() instanceof  UnitsOfMeasureActivity) {
			((UnitsOfMeasureActivity) getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this))
					.injectInto(this);
		} else {
			((UnitsOfMeasureActivity) getActivity()).getActivityComponent().newFragmentComponent(new FragmentModule(this))
					.injectInto(this);
		}
		eventBus.register(this);
		View layout = inflater.inflate(R.layout.unit_of_measure_detail_fragment, container, false);
		singularNameEdit = (EditText)layout.findViewById(R.id.uom_singular_name_edit);
		pluralNameEdit = (EditText)layout.findViewById(R.id.uom_plural_name_edit);
		notesEdit = (EditText)layout.findViewById(R.id.uom_notes_edit);
		saveButton = (Button)layout.findViewById(R.id.uom_save_button);
		if(unitOfMeasure == null) {
			unitOfMeasure = new UnitOfMeasure();
		}
		initSaveButton();
		return layout;
	}

	/**
	 * Responds to UnitOfMeasureSelectedEvent by copying the select UnitOfMeasure instance to the UI fields.
	 *
	 * @param event  a UnitOfMeasureSelectedEvent instance
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onUnitOfMeasureSelected(UnitOfMeasureSelectedEvent event) {
		unitOfMeasure = event.getUnitOfMeasure();
		copyUnitOfMeasureToView();
	}

	/**
	 * Responds to UnitOfMeasureSavedEvent by displaying a toast informing the user of the result and, if the save was successful, it also
	 * copies the save UnitOfMeasure instance to the UI fields.
	 *
	 * @param event  a UnitOfMeasureSavedEvent instance
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onUnitOfMeasureSaved(UnitOfMeasureSavedEvent event) {
		String toastString;

		if(event.isSuccess()) {
			unitOfMeasure = event.getUnitOfMeasure();
			copyUnitOfMeasureToView();
			toastString = getString(R.string.toast_uom_saved);
		} else {
			toastString = getString(R.string.toast_uom_saved_error);
		}
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
	}

	private void initSaveButton() {
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean changed = false;
				boolean errors = false;
				UnitOfMeasure newUom = new UnitOfMeasure();

				newUom.setId(unitOfMeasure.getId());
				newUom.setSingularName(unitOfMeasure.getSingularName());
				newUom.setPluralName(unitOfMeasure.getPluralName());
				newUom.setNotes(unitOfMeasure.getNotes());

				String value = singularNameEdit.getText().toString();
				if(value.isEmpty()) {
					singularNameEdit.setError(getString(R.string.error_required));
					errors = true;
				} else if(!value.equals(unitOfMeasure.getSingularName())) {
					newUom.setSingularName(value);
					changed = true;
				}

				value = pluralNameEdit.getText().toString();
				if(value == null || value.isEmpty()) {
					pluralNameEdit.setError(getString(R.string.error_required));
					errors = true;
				} else if(!value.equals(unitOfMeasure.getPluralName())) {
					newUom.setPluralName(value);
					changed = true;
				}

				value = notesEdit.getText().toString();
				if(!StringUtils.equals(value, unitOfMeasure.getNotes())) {
					newUom.setNotes(value);
					changed = true;
				}

				if(changed && !errors) {
					eventBus.post(new UnitOfMeasurePersistenceEvent(UnitOfMeasurePersistenceEvent.Action.SAVE, newUom));
				}
			}
		});
	}

	private void copyUnitOfMeasureToView() {
		if(unitOfMeasure == null) {
			this.singularNameEdit.setText(null);
			this.pluralNameEdit.setText(null);
			this.notesEdit.setText(null);
		} else {
			this.singularNameEdit.setText(unitOfMeasure.getSingularName());
			this.pluralNameEdit.setText(unitOfMeasure.getPluralName());
			this.notesEdit.setText(unitOfMeasure.getNotes());
		}
	}

	// Getters and setters
	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}
	public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
		copyUnitOfMeasureToView();
	}
}

