
export interface Meal {
    type: string;      // Breakfast / Lunch / ...
    time: string;      // "8:00 AM"
    items: string;     // Multiline text like "Oats with milk\nGreen tea"
}

export interface DayPlan {
    id: number;
    name: string;      // "Day 1 · Monday"
    subtitle: string;  // "Balanced veg · ~1600 kcal"
    meals: Meal[];
}