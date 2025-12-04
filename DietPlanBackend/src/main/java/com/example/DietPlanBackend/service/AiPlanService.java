package com.example.DietPlanBackend.service;

import com.example.DietPlanBackend.api.GeneratePlanRequest;
import com.example.DietPlanBackend.models.DayPlan;
import com.example.DietPlanBackend.api.OpenAiChatRequest;
import com.example.DietPlanBackend.api.OpenAiChatResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiPlanService {

    private final WebClient groqWebClient; // injected from GroqConfig
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generate plan using Groq (OpenAI-compatible / Chat Completions).
     * Returns a List<DayPlan>.
     */
    public List<DayPlan> generatePlan(GeneratePlanRequest request) {
        // Feature flag: if you want to fallback to mock when no key / failure
        boolean useRealAi = true;

        if (!useRealAi) {
            return mockPlan(request); // implement a mock fallback if needed
        }

        try {
            String userPrompt = buildUserPrompt(request);

            OpenAiChatRequest openAiRequest = new OpenAiChatRequest(
                    "openai/gpt-oss-120b", // choose model available on Groq; update if necessary
                    List.of(
                            new OpenAiChatRequest.Message(
                                    "system",
                                    "You are a nutrition assistant for fitness trainers. " +
                                            "You generate structured diet plans as JSON for Indian-style meals. " +
                                            "Respond ONLY with valid JSON, no markdown, no explanations."
                            ),
                            new OpenAiChatRequest.Message("user", userPrompt)
                    ),
                    0.2
            );

            OpenAiChatResponse response = groqWebClient.post()
                    .uri("/chat/completions")
                    .bodyValue(openAiRequest)
                    .retrieve()
                    .bodyToMono(OpenAiChatResponse.class)
                    .retryWhen(
                            Retry.backoff(3, Duration.ofSeconds(1))
                                    .filter(throwable -> throwable instanceof WebClientResponseException
                                            && ((WebClientResponseException) throwable).getStatusCode().value() == 429)
                    )
                    .block();

            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                throw new RuntimeException("Empty response from Groq");
            }

            String content = response.getChoices().get(0).getMessage().getContent();

            // Parse JSON into GeneratePlanResponse-like structure
            // We expect the model to return {"dayPlans": [...DayPlan objects...]}
            var node = objectMapper.readTree(content);
            var dayPlansNode = node.get("dayPlans");
            if (dayPlansNode == null || !dayPlansNode.isArray()) {
                throw new RuntimeException("Groq response missing 'dayPlans' array. Response: " + content);
            }

            // map directly to List<DayPlan>
            List<DayPlan> dayPlans = objectMapper.readerForListOf(DayPlan.class).readValue(dayPlansNode);
            return dayPlans;

        } catch (WebClientResponseException e) {
            // Log the raw body for debugging (quota vs rate limit)
            System.err.println("Groq API error status: " + e.getStatusCode());
            System.err.println("Groq API body: " + e.getResponseBodyAsString());
            throw new RuntimeException("Groq API error: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate plan from Groq", e);
        }
    }

    private String buildUserPrompt(GeneratePlanRequest req) {
        var client = req.getClient();
        int days = req.getDays() != null ? req.getDays() : 1;
        String pref = req.getDietaryPreference();

        StringBuilder sb = new StringBuilder();
        sb.append("Generate a structured diet plan for the following client.\n\n");
        sb.append("Client:\n");
        sb.append("- Name: ").append(client.getName()).append("\n");
        sb.append("- Age: ").append(client.getAge()).append("\n");
        sb.append("- Gender: ").append(client.getGender()).append("\n");
        sb.append("- Goal: ").append(client.getGoal()).append("\n");
        sb.append("- Activity level: ").append(client.getActivityLevel()).append("\n");
        if (pref != null && !pref.isBlank()) {
            sb.append("- Dietary preference: ").append(pref).append("\n");
        }
        sb.append("\n");
        sb.append("Generate a plan for ").append(days).append(" day(s).\n");
        sb.append("Each day should include meals like Breakfast, Lunch, Snack(s), Dinner.\n");
        sb.append("Use simple, practical Indian meals where possible.\n\n");

        sb.append("Return JSON in this exact format:\n");
        sb.append("{\n");
        sb.append("  \"dayPlans\": [\n");
        sb.append("    {\n");
        sb.append("      \"id\": 1,\n");
        sb.append("      \"name\": \"Day 1 · Monday\",\n");
        sb.append("      \"subtitle\": \"Short description like 'Balanced veg · ~1600 kcal'\",\n");
        sb.append("      \"meals\": [\n");
        sb.append("        {\n");
        sb.append("          \"type\": \"Breakfast\",\n");
        sb.append("          \"time\": \"8:00 AM\",\n");
        sb.append("          \"items\": \"Line 1 of food\\nLine 2 of food\"\n");
        sb.append("        }\n");
        sb.append("      ]\n");
        sb.append("    }\n");
        sb.append("  ]\n");
        sb.append("}\n");
        sb.append("\n");
        sb.append("Do NOT include any text outside the JSON. Do NOT use markdown. Only output raw JSON.");

        return sb.toString();
    }

    private List<DayPlan> mockPlan(GeneratePlanRequest request) {
        // return the same mock as before if you want a fallback
        // ... implement or call previous mock method
        throw new UnsupportedOperationException("Mock not implemented");
    }
}
