package com.madinnovations.recipekeeper.controller.events.unitofmeasure;

import com.madinnovations.recipekeeper.model.entities.UnitOfMeasure;

import java.util.Set;

/**
 * Event indicating UnitOfMeasure instances have been loaded from persistent storage.
 */
public class UnitsOfMeasureLoadedEvent {
    private Set<UnitOfMeasure> unitsOfMeasureSet;
    private boolean successful;

    /**
     * Creates a new UnitsOfMeasureLoadedEvent
     *
     * @param unitsOfMeasureSet  a Set<UnitsOfMeasure> instance
     * @param successful  true if the UnitOfMeasure set was loaded successfully
     */
    public UnitsOfMeasureLoadedEvent(Set<UnitOfMeasure> unitsOfMeasureSet, boolean successful) {
        this.unitsOfMeasureSet = unitsOfMeasureSet;
        this.successful = successful;
    }

    // Getters
    public Set<UnitOfMeasure> getUnitsOfMeasureSet() {
        return unitsOfMeasureSet;
    }
    public boolean isSuccessful() {
        return successful;
    }
}
