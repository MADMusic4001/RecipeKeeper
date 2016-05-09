package com.madinnovations.recipekeeper.controller.eventhandlers;

import com.madinnovations.recipekeeper.controller.events.category.CategoriesLoadedEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategoryDeletedEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategoryPersistenceEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategorySavedEvent;
import com.madinnovations.recipekeeper.controller.events.category.CategorySelectedEvent;
import com.madinnovations.recipekeeper.model.dao.CategoryDao;
import com.madinnovations.recipekeeper.model.entities.Category;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Set;

import javax.inject.Singleton;

/**
 * Class to handle Category related events
 */
@Singleton
public class CategoryEventHandler {
    private EventBus eventBus;
    private CategoryDao categoryDao;

    /**
     * Creates a CategoryEventHandler instance with the given {@link EventBus} and {@link CategoryDao}.
     *
     * @param eventBus  an EventBus instance
     * @param categoryDao  a CategoryDao instance
     */
    public CategoryEventHandler(EventBus eventBus, CategoryDao categoryDao) {
        this.eventBus = eventBus;
        this.categoryDao = categoryDao;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCategoryPersistenceEvent(CategoryPersistenceEvent event) {
        switch(event.getAction()) {
            case SAVE:
                boolean successful = categoryDao.save(event.getCategory());
                eventBus.post(new CategorySavedEvent(event.getCategory(), successful));
                break;
            case READ_BY_FILTER:
                Set<Category> categorySet = categoryDao.read(event.getCategory());
                eventBus.post(new CategoriesLoadedEvent(categorySet, categorySet != null));
                break;
            case READ_BY_ID:
                Category category = categoryDao.read(event.getCategory().getId());
                eventBus.post(new CategorySelectedEvent(category));
                break;
            case DELETE:
                boolean success = categoryDao.delete(event.getCategory());
                eventBus.post(new CategoryDeletedEvent(success, event.getCategory()));
                break;
        }
    }
}
