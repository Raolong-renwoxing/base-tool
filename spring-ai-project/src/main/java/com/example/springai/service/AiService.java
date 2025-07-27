package com.example.springai.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiService {

    private final ChatClient chatClient;

    @Autowired
    public AiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String generateResponse(String input) {
        String template = """
                You are a helpful assistant.
                User: {input}
                Assistant:
                """;
        
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("input", input));
        
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
}