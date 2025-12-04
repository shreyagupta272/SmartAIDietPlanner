package com.example.DietPlanBackend.api;

import com.example.DietPlanBackend.models.ClientDetails;
import com.example.DietPlanBackend.models.DayPlan;
import lombok.Data;

import java.util.List;

@Data
public class CoachNotesRequest {
    private ClientDetails client;
    private List<DayPlan> dayPlans;
}