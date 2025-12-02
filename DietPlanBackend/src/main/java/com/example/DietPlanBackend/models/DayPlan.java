package com.example.DietPlanBackend.models;

import lombok.Data;

import java.util.List;

@Data
public class DayPlan {
    private Integer id;
    private String name;      // e.g. "Day 1 · Monday"
    private String subtitle;  // e.g. "Balanced veg · ~1600 kcal"
    private List<Meal> meals;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}