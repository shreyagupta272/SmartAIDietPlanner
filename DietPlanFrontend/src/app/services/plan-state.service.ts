import { Injectable } from '@angular/core';
import { DayPlan } from '../models/plan.model';

@Injectable({
    providedIn: 'root'
})
export class PlanStateService {
    private dayPlans: DayPlan[] = [];
    private coachNotes: string = '';

    setDayPlans(plans: DayPlan[]) {
        this.dayPlans = plans;
    }

    getDayPlans(): DayPlan[] {
        return this.dayPlans;
    }

    hasPlan(): boolean {
        return this.dayPlans && this.dayPlans.length > 0;
    }

    setCoachNotes(notes: string) {
        this.coachNotes = notes || '';
    }

    getCoachNotes(): string {
        return this.coachNotes;
    }

    clear() {
        this.dayPlans = [];
        this.coachNotes = '';
    }
}
