package ru.yandex.potapov.schedule.manager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import ru.yandex.potapov.schedule.server.HttpTaskServer;
import ru.yandex.potapov.schedule.server.KVServer;
import ru.yandex.potapov.schedule.task.Epic;
import ru.yandex.potapov.schedule.task.Status;
import ru.yandex.potapov.schedule.task.Subtask;
import ru.yandex.potapov.schedule.task.Task;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager{
    private final String url;
    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String url) {
        this.url = url;
        gson = new Gson();
        client = new KVTaskClient(url);
    }

    public HttpTaskManager loadFromServer() {
        HttpTaskManager taskManager = new HttpTaskManager(url);
        List<String> keys = new ArrayList<>();
        keys.add("tasks");
        keys.add("epics");
        keys.add("subtasks");
        keys.add("history");
        for (String key : keys) {
            String content = client.load(key);
            switch (key) {
                case "tasks": {
                    TypeToken<List<Task>> type = new TypeToken<>() {
                    };
                    List<Task> task = gson.fromJson(content, type);
                    for (Task t : task) {
                        taskManager.addAnyTask(t);
                    }
                    break;
                }
                case "epics": {
                    TypeToken<List<Epic>> type = new TypeToken<>() {
                    };
                    List<Epic> epic = gson.fromJson(content, type);
                    for (Epic e : epic) {
                        taskManager.addAnyTask(e);
                    }
                    break;
                }
                case "subtasks": {
                    TypeToken<List<Subtask>> type = new TypeToken<>() {
                    };
                    List<Subtask> subtask = gson.fromJson(content, type);
                    for (Subtask s : subtask) {
                        taskManager.addAnyTask(s);
                    }
                    break;
                }
                case "history": {
                    break;
                }
            }
        }
        return taskManager;
    }

    @Override
    public void save() {
        client.put("tasks", gson.toJson(getTasks()));
        client.put("epics", gson.toJson(getEpics()));
        client.put("subtasks", gson.toJson(getSubtasks()));
        client.put("history", gson.toJson(getHistory()));
    }
}
