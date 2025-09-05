package org.example.tttn.dto;

import lombok.Data;

@Data
public class PredictionRequest {
    private boolean suspicious;
    private double confidence_score;
}
