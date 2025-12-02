package com.example.DietPlanBackend.models;

import lombok.Data;

@Data
public class Meal {
    private String type;     // Breakfast / Lunch / ...
    private String time;     // "8:00 AM"
    private String items;    // Each line = one item

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
