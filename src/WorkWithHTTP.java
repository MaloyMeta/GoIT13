import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WorkWithHTTP {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void createNewClient() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String json = """
        {
            "name": "Bombo Clat",
            "username": "BOMBOCLATT",
            "email": "BOMBOCLATT@gmail.com",
            "address": {
                "street": "Jamaica",
                "suite": "coconut",
                "city": "MeClat",
                "zipcode": "59905"
            }
        }
        """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response code " + response.statusCode());
        System.out.println("Response body " + response.body());
    }

    public static void updateClient() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String json = """
        {
            "id": 1,
            "name": "Bombo Clat",
            "username": "BOMBOCLATT",
            "email": "BOMBOCLATT@gmail.com",
            "address": {
                "street": "Jamaica",
                "suite": "coconut",
                "city": "MeClat",
                "zipcode": "59905"
            }
        }
        """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response code " + response.statusCode());
        System.out.println("Response body " + response.body());
    }

    public static void deleteClient() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response code " + response.statusCode());
    }

    public static void getAllUsers() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response code " + response.statusCode());
        System.out.println("Response body " + response.body());
    }

    public static void getUserById(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = BASE_URL + "/users/" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response code " + response.statusCode());
        System.out.println("Response body " + response.body());
    }

    public static void getUserByUsername(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = BASE_URL + "/users?username=" + username;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response code " + response.statusCode());
        System.out.println("Response body " + response.body());
    }

    // EX2

    public static int getLastPostId(int userId) throws IOException {
        String response = fetchData(BASE_URL + "/users/" + userId + "/posts");
        int maxId = -1;
        for (String post : response.split("},")) {
            if (post.contains("\"id\":")) {
                int id = Integer.parseInt(post.split("\"id\":")[1].split(",")[0].trim());
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId;
    }

    public static void writeCommentsToFile(int userId, int postId, String comments) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String formattedJson = gson.toJson(gson.fromJson(comments, Object.class));

        String fileName = "user-" + userId + "-post-" + postId + "-comments.json";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(formattedJson);
        }
        System.out.println("Файл " + fileName + " створено.");
    }

    public static String fetchData(String urlString) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    // EX3

    public static void printOpenTodos(int userId) throws IOException {
        String response = fetchData(BASE_URL + "/users/" + userId + "/todos");

        JsonArray todosArray = JsonParser.parseString(response).getAsJsonArray();
        System.out.println("Відкриті задачі користувача " + userId + ":");

        for (JsonElement todoElement : todosArray) {
            boolean completed = todoElement.getAsJsonObject().get("completed").getAsBoolean();
            if (!completed) {
                String title = todoElement.getAsJsonObject().get("title").getAsString();
                System.out.println("- " + title);
            }
        }
    }
}
