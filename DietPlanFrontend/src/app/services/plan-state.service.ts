import { Injectable } from '@angular/core';
import { DayPlan } from '../models/plan.model';

@Injectable({
    providedIn: 'root'
})
export class PlanStateService {
    private dayPlans: DayPlan[] = [];

    setDayPlans(plans: DayPlan[]) {
        // store a copy so we don't accidentally mutate from outside
        this.dayPlans = plans.map(p => ({
            ...p,
            meals: p.meals.map(m => ({ ...m }))
        }));
    }

    getDayPlans(): DayPlan[] {
        return this.dayPlans;
    }

    hasPlan(): boolean {
        return this.dayPlans.length > 0;
    }
}
