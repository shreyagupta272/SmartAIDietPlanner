package com.example.DietPlanBackend.rag;

import lombok.Data;

@Data
public class RagChunk {
    private String id;         // e.g. "fat_loss-1"
    private String source;     // e.g. "fat_loss"
    private String text;       // actual guideline text
    private double[] embedding; // vector representation
}
