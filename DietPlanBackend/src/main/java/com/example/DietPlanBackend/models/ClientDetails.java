package com.example.DietPlanBackend.models;

import lombok.Data;

@Data
public class ClientDetails {
    private String name;
    private Integer age;
    private String gender;
    private String goal;
    private String activityLevel;
}
