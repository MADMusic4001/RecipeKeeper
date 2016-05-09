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
package com.madinnovations.recipekeeper.controller.eventhandlers;

import com.madinnovations.recipekeeper.controller.events.unitofmeasure.UnitOfMeasureDeletedEvent;
import com.madinnovations.recipekeeper.controller.events.unitofmeasure.UnitOfMeasurePersistenceEvent;
import com.madinnovations.recipekeeper.controller.events.unitofmeasure.UnitOfMeasureSavedEvent;
import com.madinnovations.recipekeeper.controller.events.unitofmeasure.UnitOfMeasureSelectedEvent;
import com.madinnovations.recipekeeper.controller.events.unitofmeasure.UnitsOfMeasureLoadedEvent;
import com.madinnovations.recipekeeper.model.dao.UnitOfMeasureDao;
import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Set;

import javax.inject.Singleton;

/**
 * Class to handle UnitOfMeasure related events
 */
@Singleton
public class UnitOfMeasureEventHandler {
	private EventBus eventBus;
	private UnitOfMeasureDao unitOfMeasureDao;

	/**
	 * Creates a UnitOfMeasureEventHandler instance with the given {@link EventBus} and {@link UnitOfMeasureDao}.
	 *
	 * @param eventBus  an EventBus instance
	 * @param unitOfMeasureDao  a UnitOfMeasureDao instance
	 */
	public UnitOfMeasureEventHandler(EventBus eventBus, UnitOfMeasureDao unitOfMeasureDao) {
		this.eventBus = eventBus;
		this.unitOfMeasureDao = unitOfMeasureDao;
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onUnitOfMeasurePersistenceEvent(UnitOfMeasurePersistenceEvent event) {
		switch(event.getAction()) {
			case SAVE:
				boolean successful = unitOfMeasureDao.save(event.getUnitOfMeasure());
				eventBus.post(new UnitOfMeasureSavedEvent(event.getUnitOfMeasure(), successful));
				break;
			case READ_BY_FILTER:
				Set<UnitOfMeasure> unitOfMeasureSet = unitOfMeasureDao.read(event.getUnitOfMeasure());
				eventBus.post(new UnitsOfMeasureLoadedEvent(unitOfMeasureSet, unitOfMeasureSet != null));
				break;
			case READ_BY_ID:
				UnitOfMeasure unitOfMeasure = unitOfMeasureDao.read(event.getUnitOfMeasure().getId());
				eventBus.post(new UnitOfMeasureSelectedEvent(unitOfMeasure));
				break;
			case DELETE:
				boolean success = unitOfMeasureDao.delete(event.getUnitOfMeasure());
				eventBus.post(new UnitOfMeasureDeletedEvent(success, event.getUnitOfMeasure()));
				break;
		}
	}
}
