package com.example.DietPlanBackend.api;

import com.example.DietPlanBackend.models.DayPlan;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class GeneratePlanResponse {
    private List<DayPlan> dayPlans;

    public List<DayPlan> getDayPlans() {
        return dayPlans;
    }

    public void setDayPlans(List<DayPlan> dayPlans) {
        this.dayPlans = dayPlans;
    }
}
