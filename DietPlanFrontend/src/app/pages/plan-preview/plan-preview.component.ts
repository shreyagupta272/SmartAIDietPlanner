import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ClientStateService, ClientDetails } from '../../services/client-state.service';
import { PlanStateService } from '../../services/plan-state.service';
import { DayPlan } from '../../models/plan.model';
import html2pdf from 'html2pdf.js';

@Component({
  selector: 'app-plan-preview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './plan-preview.component.html',
  styleUrls: ['./plan-preview.component.css']
})
export class PlanPreviewComponent implements OnInit {
  @ViewChild('pdfArea') pdfArea!: ElementRef;

  client: ClientDetails | null = null;
  dayPlans: DayPlan[] = [];
  coachNotes: string = ''; // if you want, we can also store this in PlanStateService later

  constructor(
    private clientState: ClientStateService,
    private planState: PlanStateService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.client = this.clientState.getClientDetails();
    this.dayPlans = this.planState.getDayPlans();

    if (!this.client || !this.dayPlans || this.dayPlans.length === 0) {
      this.router.navigate(['/plan']);
      return;
    }
  }

  backToEdit() {
    this.router.navigate(['/plan']);
  }

  downloadPdf() {
    if (!this.pdfArea) return;

    const element = this.pdfArea.nativeElement;

    const opt: any = {
      margin: 5,
      filename: `diet-plan-${this.client?.name || 'client'}.pdf`,
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
    };

    html2pdf().from(element).set(opt).save();
  }

  splitLines(text: string | null | undefined): string[] {
    if (!text) return [];
    return text
      .split('\n')
      .map(line => line.trim())
      .filter(line => line.length > 0);
  }

  getMealIcon(type: string | undefined): string {
    if (!type) return '🍽️';
    const t = type.toLowerCase();
    if (t.includes('breakfast')) return '🥣';
    if (t.includes('lunch')) return '🍱';
    if (t.includes('snack')) return '🥜';
    if (t.includes('dinner')) return '🍽️';
    return '🥄';
  }
}
