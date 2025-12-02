import { Injectable } from '@angular/core';

export interface ClientDetails {
  name: string;
  age?: number | null;
  gender?: string;
  goal?: string;
  activityLevel?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClientStateService {
  private client: ClientDetails | null = null;

  setClientDetails(details: ClientDetails) {
    this.client = details;
  }

  getClientDetails(): ClientDetails | null {
    return this.client;
  }
}
