package ru.yandex.potapov.schedule.manager;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    protected InMemoryTaskManagerTest() throws IOException, InterruptedException {
    }

    @Override
    protected TaskManager createNewManager() {
        return new InMemoryTaskManager();
    }
}
