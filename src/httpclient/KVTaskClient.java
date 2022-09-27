package httpclient;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String url;
    private final String apiToken;

    public KVTaskClient() {
        url = "http://localhost:8080/";
        apiToken = register(url);
    }

    private String register(String url){ // Авторизация APITOKEN
       try {
           HttpClient httpClient = HttpClient.newHttpClient();
           HttpRequest build = HttpRequest.newBuilder()
                   .uri(URI.create(url + "register"))
                   .GET()
                   .build();
           HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
           return send.body();

       }catch (Exception e){
           throw new RuntimeException();
       }
    }

    public void save(String key, String value){ // Сохранение на KVServer
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();
            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public String load(String key) { // Загрузка данных с KVServer
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
            return send.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}