package com.example.DietPlanBackend.rag;

import com.example.DietPlanBackend.api.CoachNotesRequest;
import com.example.DietPlanBackend.models.DayPlan;
import com.example.DietPlanBackend.api.OpenAiChatRequest;
import com.example.DietPlanBackend.api.OpenAiChatResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagService {

    private final ResourcePatternResolver resourceResolver;
    private final EmbeddingClient embeddingClient;
    private final WebClient groqWebClient; // reuse your existing WebClient bean for Groq/OpenAI

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<RagChunk> chunks = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            // Load all files under classpath:guidelines/*.txt
            Resource[] resources = resourceResolver.getResources("classpath:guidelines/*.txt");
            int idCounter = 1;

            for (Resource res : resources) {
                String filename = Objects.requireNonNull(res.getFilename());
                String source = filename.replace(".txt", "");

                String text = new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

                // Simple chunking: split on blank lines
                String[] rawChunks = text.split("\\n\\s*\\n");

                for (String raw : rawChunks) {
                    String trimmed = raw.trim();
                    if (trimmed.isEmpty()) continue;

                    RagChunk chunk = new RagChunk();
                    chunk.setId(source + "-" + (idCounter++));
                    chunk.setSource(source);
                    chunk.setText(trimmed);
                    chunk.setEmbedding(embeddingClient.embed(trimmed)); // embedding

                    chunks.add(chunk);
                }
            }

            System.out.println("Loaded RAG chunks: " + chunks.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize RAG chunks", e);
        }
    }

    public String generateCoachNotes(CoachNotesRequest request) {
        if (chunks.isEmpty()) {
            return "No guidelines available yet.";
        }

        // 1. Build query from client + goal
        String query = buildQuery(request);

        // 2. Embed query
        double[] queryEmbedding = embeddingClient.embed(query);

        // 3. Retrieve top-K chunks
        List<RagChunk> top = topKSimilar(queryEmbedding, 5);

        // 4. Build prompt
        String prompt = buildPrompt(request, top);

        // 5. Call LLM chat completion (Groq/OpenAI) to get final notes
        return callLlm(prompt);
    }

    private String buildQuery(CoachNotesRequest req) {
        var c = req.getClient();
        StringBuilder sb = new StringBuilder();
        sb.append("Generate coach notes for a client.\n");
        sb.append("Goal: ").append(c.getGoal()).append("\n");
        sb.append("Activity level: ").append(c.getActivityLevel()).append("\n");
        return sb.toString();
    }

    private List<RagChunk> topKSimilar(double[] queryEmbedding, int k) {
        return chunks.stream()
                .map(chunk -> new AbstractMap.SimpleEntry<>(chunk, VectorUtils.cosineSimilarity(queryEmbedding, chunk.getEmbedding())))
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private String buildPrompt(CoachNotesRequest req, List<RagChunk> contextChunks) {
        var c = req.getClient();
        List<DayPlan> dayPlans = req.getDayPlans();

        StringBuilder sb = new StringBuilder();

        sb.append("You are an expert fitness and nutrition coach.\n");
        sb.append("Use ONLY the provided guidelines context to create friendly, practical coach notes.\n");
        sb.append("Do not invent medical facts. Keep the tone supportive and simple.\n\n");

        sb.append("Client:\n");
        sb.append("- Name: ").append(c.getName()).append("\n");
        sb.append("- Age: ").append(c.getAge()).append("\n");
        sb.append("- Gender: ").append(c.getGender()).append("\n");
        sb.append("- Goal: ").append(c.getGoal()).append("\n");
        sb.append("- Activity level: ").append(c.getActivityLevel()).append("\n\n");

        sb.append("Guidelines context (from trainer's knowledge base):\n");
        for (RagChunk chunk : contextChunks) {
            sb.append("[Source: ").append(chunk.getSource()).append("]\n");
            sb.append(chunk.getText()).append("\n\n");
        }

        sb.append("Diet plan summary (high-level):\n");
        for (DayPlan dp : dayPlans) {
            sb.append(dp.getName()).append(": ");
            sb.append(dp.getSubtitle()).append("\n");
        }
        sb.append("\n");

        sb.append("Task:\n");
        sb.append("Write 5–8 bullet points of coach notes for this client, ");
        sb.append("covering hydration, activity, general eating habits, and mindset.\n");
        sb.append("Mention 1 point on how the diet plan is helping with the client conditions");
        sb.append("Output plain text bullets, one per line, no numbering like '1.' just use '- '.\n");

        return sb.toString();
    }

    private String callLlm(String userPrompt) {
        try {
            OpenAiChatRequest openAiRequest = new OpenAiChatRequest(
                    "openai/gpt-oss-120b", // e.g. groq/llama3-8b etc.
                    List.of(
                            new OpenAiChatRequest.Message(
                                    "system",
                                    "You are a helpful fitness coach assistant. Respond with plain text bullet points."
                            ),
                            new OpenAiChatRequest.Message("user", userPrompt)
                    ),
                    0.3
            );

            OpenAiChatResponse response = groqWebClient.post()
                    .uri("/chat/completions")
                    .bodyValue(openAiRequest)
                    .retrieve()
                    .bodyToMono(OpenAiChatResponse.class)
                    .block();

            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                throw new RuntimeException("Empty response from LLM");
            }

            return response.getChoices().get(0).getMessage().getContent().trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to call LLM for coach notes", e);
        }
    }
}
