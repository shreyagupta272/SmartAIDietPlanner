package com.example.DietPlanBackend.rag;

public interface EmbeddingClient {

    /**
     * Returns an embedding vector for the given text.
     * This can call Groq, OpenAI, or any embedding provider.
     */
    double[] embed(String text);
}