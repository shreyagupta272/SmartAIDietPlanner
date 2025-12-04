import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClientDetails } from './client-state.service';
import { DayPlan } from '../models/plan.model';

export interface CoachNotesRequest {
    client: ClientDetails;
    dayPlans: DayPlan[];
}

export interface CoachNotesResponse {
    coachNotes: string;
}

@Injectable({
    providedIn: 'root'
})
export class RagCoachNotesService {
    // adjust base URL if needed
    private baseUrl = 'http://localhost:8080/api/rag';

    constructor(private http: HttpClient) { }

    generateCoachNotes(req: CoachNotesRequest): Observable<CoachNotesResponse> {
        return this.http.post<CoachNotesResponse>(`${this.baseUrl}/coach-notes`, req);
    }
}
