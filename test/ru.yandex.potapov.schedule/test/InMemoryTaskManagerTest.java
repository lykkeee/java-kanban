package ru.yandex.potapov.schedule.test;

import ru.yandex.potapov.schedule.manager.InMemoryTaskManager;
import ru.yandex.potapov.schedule.manager.Managers;
import ru.yandex.potapov.schedule.manager.TaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
   @Override
    protected TaskManager createNewManager() {
        return Managers.getInMemoryTaskManager();
    }
}
