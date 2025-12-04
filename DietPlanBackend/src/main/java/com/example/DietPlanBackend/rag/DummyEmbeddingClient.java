package com.example.DietPlanBackend.rag;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DummyEmbeddingClient implements EmbeddingClient {

    private static final int DIM = 64; // pretend 64-dim vectors
    private final Random random = new Random();

    @Override
    public double[] embed(String text) {
        double[] v = new double[DIM];
        for (int i = 0; i < DIM; i++) {
            v[i] = random.nextDouble();
        }
        return v;
    }
}
