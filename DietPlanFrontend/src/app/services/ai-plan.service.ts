// src/app/services/ai-plan.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClientDetails } from './client-state.service';
import { DayPlan } from '../models/plan.model';

export interface GeneratePlanRequest {
    client: ClientDetails;
    days: number;
    dietaryPreference?: string;
}

export interface GeneratePlanResponse {
    dayPlans: DayPlan[];
}

@Injectable({
    providedIn: 'root'
})
export class AiPlanService {
    private baseUrl = 'http://localhost:8080/api/ai';

    constructor(private http: HttpClient) { }

    generatePlan(req: GeneratePlanRequest): Observable<GeneratePlanResponse> {
        return this.http.post<GeneratePlanResponse>(`${this.baseUrl}/generate-plan`, req);
    }
}
