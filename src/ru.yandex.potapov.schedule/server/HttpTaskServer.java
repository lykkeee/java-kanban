package ru.yandex.potapov.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.potapov.schedule.manager.Managers;
import ru.yandex.potapov.schedule.manager.TaskManager;
import ru.yandex.potapov.schedule.task.Epic;
import ru.yandex.potapov.schedule.task.Subtask;
import ru.yandex.potapov.schedule.task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final Gson gson;
    private final TaskManager taskManager;
    private final HttpServer server;

    public HttpTaskServer() throws IOException, InterruptedException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
        taskManager = Managers.getDefault();
        gson = new Gson();
    }

     /*Не могу понять почему не вызываются таски
     Причем если создавать их здесь, то будет все нормально, но даже с подсказками я не смог отследить где ошибка
     В остальном вроде все работает */

    private void handleTasks(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case ("POST"):
                    if (Pattern.matches("^/tasks/task$", path)) {
                        Task task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Task.class);
                        if (taskManager.getTasks().contains(task)) {
                            taskManager.updateTask(task);
                            System.out.println("Updated task id: " + task.getId());
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            taskManager.addNewTask(task);
                            System.out.println("Added new task id: " + task.getId());
                            exchange.sendResponseHeaders(200, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subtask$", path)) {
                        Subtask subtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Subtask.class);
                        if (taskManager.getSubtasks().contains(subtask)) {
                            taskManager.updateSubtask(subtask);
                            System.out.println("Updated subtask id: " + subtask.getId());
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            taskManager.addNewSubtask(subtask);
                            System.out.println("Added new subtask id: " + subtask.getId());
                            exchange.sendResponseHeaders(200, 0);
                        }
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        Epic epic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Epic.class);
                        if (taskManager.getEpics().contains(epic)) {
                            taskManager.updateEpic(epic);
                            System.out.println("Updated epic id: " + epic.getId());
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            taskManager.addNewEpic(epic);
                            System.out.println("Added new epic id: " + epic.getId());
                            exchange.sendResponseHeaders(200, 0);
                        }
                    } else {
                        System.out.println("Wrong URI");
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    }
                    break;
                case ("GET"):
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(exchange, response);
                        return;
                    } else if (Pattern.matches("^/tasks/task$", path)) {
                        String response = gson.toJson(taskManager.getTasks());
                        sendText(exchange, response);
                        return;
                    } else if (Pattern.matches("^/tasks/task/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getTask(id));
                            sendText(exchange, response);
                            break;
                        } else {
                            System.out.println("Given incorrect id: " + pathId);
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subtask+$", path)) {
                        String response = gson.toJson(taskManager.getSubtasks());
                        sendText(exchange, response);
                        return;
                    } else if (Pattern.matches("^/tasks/epic+$", path)) {
                        String response = gson.toJson(taskManager.getEpics());
                        sendText(exchange, response);
                        return;
                    } else if (Pattern.matches("^/tasks/subtask/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getSubtask(id));
                            sendText(exchange, response);
                            break;
                        } else {
                            System.out.println("Given incorrect id: " + pathId);
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/epic/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getEpic(id));
                            sendText(exchange, response);
                            break;
                        } else {
                            System.out.println("Given incorrect id: " + pathId);
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subtask/epic/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getEpic(id).getSubtasks());
                            sendText(exchange, response);
                            break;
                        } else {
                            System.out.println("Given incorrect id: " + pathId);
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/history$", path)) {
                        String response = gson.toJson(taskManager.getHistory());
                        sendText(exchange, response);
                        return;
                    } else {
                        System.out.println("Wrong URI");
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    }
                    break;
                case ("DELETE"):
                    if (Pattern.matches("^/tasks/task$", path)) {
                        taskManager.deleteTasks();
                        System.out.println("Deleted all tasks");
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    } else if (Pattern.matches("^/tasks/task/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteTask(id);
                            System.out.println("Deleted task id = " + pathId);
                            exchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            System.out.println("Given incorrect id: " + pathId);
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subtask+$", path)) {
                        taskManager.deleteSubtasks();
                        System.out.println("Deleted all subtasks");
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    } else if (Pattern.matches("^/tasks/epic+$", path)) {
                        taskManager.deleteEpics();
                        System.out.println("Deleted all epics");
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    } else if (Pattern.matches("^/tasks/subtask/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteSubtask(id);
                            System.out.println("Deleted subtask id = " + pathId);
                            exchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            System.out.println("Given incorrect id: " + pathId);
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/epic/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteEpic(id);
                            System.out.println("Deleted epic id = " + pathId);
                            exchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            System.out.println("Given incorrect id: " + pathId);
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        System.out.println("Wrong URI");
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    }
                    break;
                default:
                    System.out.println("Invalid method: " + requestMethod);
                    exchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен");
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}