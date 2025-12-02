import { Routes } from '@angular/router';
import { ClientSetupComponent } from './pages/client-setup/client-setup.component';
import { PlanBuilderComponent } from './pages/plan-builder/plan-builder.component';

export const routes: Routes = [
    { path: '', pathMatch: 'full', redirectTo: 'client' },
    { path: 'client', component: ClientSetupComponent },
    { path: 'plan', component: PlanBuilderComponent },
    { path: '**', redirectTo: 'client' },
];
