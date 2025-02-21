import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.*;
import java.net.URI;
import com.google.gson.*;

public class Main{

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) throws IOException,InterruptedException {
        WorkWithHTTP http = new WorkWithHTTP();
        //Ex 1
        System.out.println("---------------------------------------- Завдання 1 ----------------------------------------");
        http.createNewClient();
        System.out.println("---------------------------------------- Завдання 1.2 ----------------------------------------");
        http.updateClient();
        System.out.println("---------------------------------------- Завдання 1.3 ----------------------------------------");
        http.deleteClient();
        System.out.println("---------------------------------------- Завдання 1.4 ----------------------------------------");
        http.getAllUsers();
        System.out.println("---------------------------------------- Завдання 1.5 ----------------------------------------");
        http.getUserById(5);
        System.out.println("---------------------------------------- Завдання 1.6 ----------------------------------------");
        http.getUserByUsername("Bret");

       //Ex 2
        System.out.println("---------------------------------------- Завдання 2 ----------------------------------------");
        int userId = 1;
        int lastPostId = http.getLastPostId(userId);
        if (lastPostId != -1) {
            String comments = http.fetchData(BASE_URL + "/posts/" + lastPostId + "/comments");
            http.writeCommentsToFile(userId, lastPostId, comments);
        } else {
            System.out.println("Користувач не має постів.");
        }

        //Ex 3
        System.out.println("---------------------------------------- Завдання 3 ----------------------------------------");
        http.printOpenTodos(1);

    }


}



