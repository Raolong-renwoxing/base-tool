package com.example.springai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ApiClientService {

    private static final Logger logger = LoggerFactory.getLogger(ApiClientService.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public ApiClientService(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Perform a synchronous GET request
     * 
     * @param url The URL to call
     * @return Response body as string
     * @throws IOException If the request fails
     */
    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
                
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Response body is null");
            }
            
            return responseBody.string();
        }
    }
    
    /**
     * Perform an asynchronous GET request
     * 
     * @param url The URL to call
     * @return CompletableFuture with response body as string
     */
    public CompletableFuture<String> getAsync(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
                
        CompletableFuture<String> future = new CompletableFuture<>();
        
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (response) {
                    if (!response.isSuccessful()) {
                        future.completeExceptionally(new IOException("Unexpected response code: " + response));
                        return;
                    }
                    
                    ResponseBody responseBody = response.body();
                    if (responseBody == null) {
                        future.completeExceptionally(new IOException("Response body is null"));
                        return;
                    }
                    
                    future.complete(responseBody.string());
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        });
        
        return future;
    }
    
    /**
     * Perform a synchronous POST request with JSON body
     * 
     * @param url The URL to call
     * @param requestBody The request body as an object (will be serialized to JSON)
     * @return Response body as string
     * @throws IOException If the request fails
     */
    public String post(String url, Object requestBody) throws IOException {
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        RequestBody body = RequestBody.create(jsonBody, JSON);
        
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
                
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Response body is null");
            }
            
            return responseBody.string();
        }
    }
    
    /**
     * Perform an asynchronous POST request with JSON body
     * 
     * @param url The URL to call
     * @param requestBody The request body as an object (will be serialized to JSON)
     * @return CompletableFuture with response body as string
     */
    public CompletableFuture<String> postAsync(String url, Object requestBody) {
        CompletableFuture<String> future = new CompletableFuture<>();
        
        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            RequestBody body = RequestBody.create(jsonBody, JSON);
            
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
                    
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    future.completeExceptionally(e);
                }
                
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try (response) {
                        if (!response.isSuccessful()) {
                            future.completeExceptionally(new IOException("Unexpected response code: " + response));
                            return;
                        }
                        
                        ResponseBody responseBody = response.body();
                        if (responseBody == null) {
                            future.completeExceptionally(new IOException("Response body is null"));
                            return;
                        }
                        
                        future.complete(responseBody.string());
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                }
            });
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        
        return future;
    }
    
    /**
     * Parse JSON response to a Map
     * 
     * @param jsonResponse JSON response as string
     * @return Map representation of the JSON
     * @throws IOException If parsing fails
     */
    public Map<String, Object> parseJsonToMap(String jsonResponse) throws IOException {
        return objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});
    }
    
    /**
     * Parse JSON response to a specific type
     * 
     * @param jsonResponse JSON response as string
     * @param valueType The class of the type to parse to
     * @param <T> The type to parse to
     * @return Object of type T
     * @throws IOException If parsing fails
     */
    public <T> T parseJson(String jsonResponse, Class<T> valueType) throws IOException {
        return objectMapper.readValue(jsonResponse, valueType);
    }
}