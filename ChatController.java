package com.example.pdfchatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.ai.embeddings.EmbeddingsModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.chat.client.ChatClient;
import com.example.pdfchatbot.util.PdfUtils;

@RestController
public class ChatController {

    @Autowired
    private EmbeddingsModel embeddingsModel;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ChatClient chatClient;

    @PostMapping(path = "/ask", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ChatResponse ask(@RequestParam("file") MultipartFile file,
                            @RequestParam("question") String question) throws Exception {
        String text = PdfUtils.extractText(file.getInputStream());
        List<String> chunks = PdfUtils.chunkText(text, 1000, 150);
        vectorStore.upsert(chunks, embeddingsModel.embedAll(chunks));
        String response = chatClient.chatWithContext(
            question, vectorStore.similaritySearch(embeddingsModel.embed(question))
        );
        return new ChatResponse(response);
    }

    public static class ChatResponse {
        private String answer;
        public ChatResponse(String answer) { this.answer = answer; }
        public String getAnswer() { return answer; }
    }
}
