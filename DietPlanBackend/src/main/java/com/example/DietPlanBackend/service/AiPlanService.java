package com.example.DietPlanBackend.service;

import com.example.DietPlanBackend.api.GeneratePlanRequest;
import com.example.DietPlanBackend.models.DayPlan;
import com.example.DietPlanBackend.models.Meal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiPlanService {

    public List<DayPlan> generatePlan(GeneratePlanRequest request) {
        // TODO: Replace this mock with actual AI call & JSON parsing

        int days = request.getDays() != null ? request.getDays() : 1;

        List<DayPlan> result = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            DayPlan day = new DayPlan();
            day.setId(i);
            day.setName("Day " + i);
            day.setSubtitle("AI-generated meal plan");

            List<Meal> meals = new ArrayList<>();

            Meal breakfast = new Meal();
            breakfast.setType("Breakfast");
            breakfast.setTime("8:00 AM");
            breakfast.setItems("Oats with milk\n1 banana\nGreen tea (no sugar)");
            meals.add(breakfast);

            Meal lunch = new Meal();
            lunch.setType("Lunch");
            lunch.setTime("1:00 PM");
            lunch.setItems("1 cup dal\n2 rotis\nSalad");
            meals.add(lunch);

            Meal snack = new Meal();
            snack.setType("Snack");
            snack.setTime("5:00 PM");
            snack.setItems("Handful of nuts\nButtermilk");
            meals.add(snack);

            Meal dinner = new Meal();
            dinner.setType("Dinner");
            dinner.setTime("8:30 PM");
            dinner.setItems("Paneer bhurji\n2 rotis\nVeggies");
            meals.add(dinner);

            day.setMeals(meals);
            result.add(day);
        }

        return result;
    }
}
