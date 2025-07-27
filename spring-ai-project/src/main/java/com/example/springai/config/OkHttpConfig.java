package com.example.springai.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        // Create a logging interceptor for debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // Set the log level (BASIC, HEADERS, BODY, NONE)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        
        // Configure the OkHttpClient with timeouts and connection pool
        return new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(30))
                .writeTimeout(Duration.ofSeconds(30))
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                .addInterceptor(loggingInterceptor)
                .build();
    }
}