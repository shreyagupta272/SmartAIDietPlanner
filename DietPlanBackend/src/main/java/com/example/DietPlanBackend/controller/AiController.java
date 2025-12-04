package com.example.DietPlanBackend.controller;

import com.example.DietPlanBackend.api.GeneratePlanRequest;
import com.example.DietPlanBackend.api.GeneratePlanResponse;
import com.example.DietPlanBackend.models.DayPlan;
import com.example.DietPlanBackend.service.AiPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:4200") // adjust for prod
@RequiredArgsConstructor
public class AiController {

    private final AiPlanService aiPlanService;

    @PostMapping("/generate-plan")
    public GeneratePlanResponse generatePlan(@RequestBody GeneratePlanRequest request) {
        List<DayPlan> plans = aiPlanService.generatePlan(request);
        GeneratePlanResponse response = new GeneratePlanResponse();
        response.setDayPlans(plans);
        return response;
    }

}
