package com.example.myapplication.api;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    public interface Callback<T> {
        void onSuccess(T data);
        void onError(Exception e);
    }

    public static <T> void request(String urlString, String method, Type typeofT,
                                   String body, Callback<T> callback){
        executor.execute(() -> {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(BASE_URL + urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setRequestProperty("Content-Type", "application/json");
                    if (body != null){
                        connection.setDoOutput(true);
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(body.getBytes(StandardCharsets.UTF_8));
                        outputStream.close();
                    }
                    int returnCode = connection.getResponseCode();
                    InputStream is = (returnCode >= 200 && returnCode <= 300) ?
                            connection.getInputStream() : connection.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder result = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null){
                        result.append(line);
                    }
                    reader.close();
                    if(returnCode >=200 & returnCode <= 300){
                        T data = new Gson().fromJson(result.toString(), typeofT);
                        callback.onSuccess(data);
                    } else callback.onError(new Exception("HTTP Error: " + returnCode));

                } catch (Exception e) {
                    callback.onError(e);
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
        });
    }
}
