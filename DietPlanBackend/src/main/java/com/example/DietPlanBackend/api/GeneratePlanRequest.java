package com.example.DietPlanBackend.api;

import com.example.DietPlanBackend.models.ClientDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratePlanRequest {
    private ClientDetails client;
    private Integer days;
    private String dietaryPreference;

    public void setClient(ClientDetails client) {
        this.client = client;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public void setDietaryPreference(String dietaryPreference) {
        this.dietaryPreference = dietaryPreference;
    }

    public ClientDetails getClient() {
        return client;
    }

    public Integer getDays() {
        return days;
    }

    public String getDietaryPreference() {
        return dietaryPreference;
    }
}
