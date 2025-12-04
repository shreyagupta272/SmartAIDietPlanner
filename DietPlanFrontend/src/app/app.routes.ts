import { Routes } from '@angular/router';
import { ClientSetupComponent } from './pages/client-setup/client-setup.component';
import { PlanBuilderComponent } from './pages/plan-builder/plan-builder.component';
import { PlanPreviewComponent } from './pages/plan-preview/plan-preview.component';

export const routes: Routes = [
    { path: '', pathMatch: 'full', redirectTo: 'client' },
    { path: 'client', component: ClientSetupComponent },
    { path: 'plan', component: PlanBuilderComponent },
    { path: 'preview', component: PlanPreviewComponent },
    { path: '**', redirectTo: 'client' },
];
