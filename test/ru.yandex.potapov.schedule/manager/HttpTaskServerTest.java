package ru.yandex.potapov.schedule.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.potapov.schedule.server.HttpTaskServer;
import ru.yandex.potapov.schedule.server.KVServer;
import ru.yandex.potapov.schedule.task.Epic;
import ru.yandex.potapov.schedule.task.Status;
import ru.yandex.potapov.schedule.task.Subtask;
import ru.yandex.potapov.schedule.task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private static KVServer kvServer;
    private static HttpTaskServer server;
    private final Gson gson = new Gson();

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        server = new HttpTaskServer();
        server.start();
    }

    @AfterAll
    static void afterAll() {
        server.stop();
        kvServer.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0, 15, LocalDateTime.of(2015, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        TypeToken<List<Task>> type = new TypeToken<>() {
        };
        List<Task> task = gson.fromJson(response.body(), type);
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(task, "Tasks didn't get");
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Incorrect number of tasks");
        assertEquals(taskManager.getPrioritizedTasks().get(0), task, "Tasks are not equal");
    }

    @Test
    void getTasksTask() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0, 15, LocalDateTime.of(2015, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        TypeToken<List<Task>> type = new TypeToken<>() {
        };
        List<Task> task = gson.fromJson(response.body(), type);
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(task, "Tasks didn't get");
        assertEquals(1, taskManager.getTasks().size(), "Incorrect number of tasks");
        assertEquals(taskManager.getTasks().get(0), task, "Tasks are not equal");
    }

    @Test
    void getTasksTaskId() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0, 15, LocalDateTime.of(2015, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/id=0");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        TypeToken<List<Task>> type = new TypeToken<>() {
        };
        List<Task> task = gson.fromJson(response.body(), type);
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(task, "Tasks didn't get");
        assertEquals(1, taskManager.getTasks().size(), "Incorrect number of tasks");
        assertEquals(taskManager.getTasks().get(0), task, "Tasks are not equal");
    }

    @Test
    void getTasksEpic() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        TypeToken<List<Epic>> type = new TypeToken<>() {
        };
        List<Epic> epic = gson.fromJson(response.body(), type);
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(epic, "Tasks didn't get");
        assertEquals(1, taskManager.getEpics().size(), "Incorrect number of tasks");
        assertEquals(taskManager.getEpics().get(0), epic, "Tasks are not equal");
    }

    @Test
    void getTasksEpicId() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/id=0");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        TypeToken<List<Epic>> type = new TypeToken<>() {
        };
        List<Epic> epic = gson.fromJson(response.body(), type);
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(epic, "Tasks didn't get");
        assertEquals(1, taskManager.getEpics().size(), "Incorrect number of tasks");
        assertEquals(taskManager.getEpics().get(0), epic, "Tasks are not equal");
    }

    @Test
    void getTasksSubtask() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        taskManager.addNewSubtask(new Subtask("1 подзадача", "Описание 1 подзадачи", Status.NEW, 0, 0, 6, LocalDateTime.of(2013, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        TypeToken<List<Subtask>> type = new TypeToken<>() {
        };
        List<Subtask> subtask = gson.fromJson(response.body(), type);
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(subtask, "Tasks didn't get");
        assertEquals(1, taskManager.getSubtasks().size(), "Incorrect number of tasks");
        assertEquals(taskManager.getTasks().get(0), subtask, "Tasks are not equal");
    }

    @Test
    void getTasksSubtaskId() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        taskManager.addNewSubtask(new Subtask("1 подзадача", "Описание 1 подзадачи", Status.NEW, 0, 0, 6, LocalDateTime.of(2013, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        TypeToken<List<Subtask>> type = new TypeToken<>() {
        };
        List<Subtask> subtask = gson.fromJson(response.body(), type);
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(subtask, "Tasks didn't get");
        assertEquals(1, taskManager.getSubtasks().size(), "Incorrect number of tasks");
        assertEquals(taskManager.getTasks().get(0), subtask, "Tasks are not equal");
    }

    @Test
    void postTasksTask() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        String json = gson.toJson(new Task("1 задача", "Описание 1", Status.NEW, 0, 15, LocalDateTime.of(2015, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json)).uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(taskManager.getTasks(), "Tasks didn't get");
        assertEquals(1, taskManager.getTasks().size(), "Incorrect number of tasks");
    }

    @Test
    void postTasksEpic() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        String json = gson.toJson(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json)).uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(taskManager.getEpics(), "Tasks didn't get");
        assertEquals(1, taskManager.getEpics().size(), "Incorrect number of tasks");
    }

    @Test
    void postTasksSubtask() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        String json = gson.toJson(new Subtask("1 подзадача", "Описание 1 подзадачи", Status.NEW, 0, 0, 6, LocalDateTime.of(2013, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json)).uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertNotNull(taskManager.getSubtasks(), "Tasks didn't get");
        assertEquals(1, taskManager.getSubtasks().size(), "Incorrect number of tasks");
    }

    @Test
    void deleteTasksTask() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0, 15, LocalDateTime.of(2015, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertEquals(0, taskManager.getTasks().size(), "Incorrect number of tasks");
    }

    @Test
    void deleteTasksTaskId() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0, 15, LocalDateTime.of(2015, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/id=0");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertEquals(0, taskManager.getTasks().size(), "Incorrect number of tasks");
    }

    @Test
    void deleteTasksEpic() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertEquals(0, taskManager.getEpics().size(), "Incorrect number of tasks");
    }

    @Test
    void deleteTasksEpicId() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/id=0");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertEquals(0, taskManager.getEpics().size(), "Incorrect number of tasks");
    }

    @Test
    void deleteTasksSubtask() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        taskManager.addNewSubtask(new Subtask("1 подзадача", "Описание 1 подзадачи", Status.NEW, 0, 0, 6, LocalDateTime.of(2013, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertEquals(0, taskManager.getSubtasks().size(), "Incorrect number of tasks");
    }

    @Test
    void deleteTasksSubtaskId() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        taskManager.addNewSubtask(new Subtask("1 подзадача", "Описание 1 подзадачи", Status.NEW, 0, 0, 6, LocalDateTime.of(2013, 1, 1, 1, 1)));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(200, response.statusCode(), "Status code isn't 200");
        assertEquals(0, taskManager.getSubtasks().size(), "Incorrect number of tasks");
    }

}
