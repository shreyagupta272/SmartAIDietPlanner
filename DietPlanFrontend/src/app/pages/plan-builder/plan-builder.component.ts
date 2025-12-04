import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { ClientStateService, ClientDetails } from '../../services/client-state.service';
import { PlanStateService } from '../../services/plan-state.service';
import { DayPlan } from '../../models/plan.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import html2pdf from 'html2pdf.js';
import { AiPlanService } from '../../services/ai-plan.service';

@Component({
  selector: 'app-plan-builder',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './plan-builder.component.html',
  styleUrls: ['./plan-builder.component.css']
})
export class PlanBuilderComponent implements OnInit {
  @ViewChild('planContent') planContent!: ElementRef;

  client: ClientDetails | null = null;
  dayPlans: DayPlan[] = [];
  private nextDayId = 2;

  constructor(
    private clientState: ClientStateService,
    private planState: PlanStateService,
    private aiPlanService: AiPlanService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.client = this.clientState.getClientDetails();

    if (!this.client) {
      this.router.navigate(['/client']);
      return;
    }

    if (this.planState.hasPlan()) {
      this.dayPlans = this.planState.getDayPlans();
      this.nextDayId = this.dayPlans.length + 1;
      return;
    }

    this.dayPlans = [
      {
        id: 1,
        name: 'Day 1 · Monday',
        subtitle: 'Balanced veg · ~1600 kcal',
        meals: [
          {
            type: 'Breakfast',
            time: '8:00 AM',
            items: 'Oats with skim milk & 1 banana\nGreen tea (no sugar)',
          },
          {
            type: 'Mid-morning Snack',
            time: '11:00 AM',
            items: 'Handful of mixed nuts\n1 fruit (apple/pear)',
          },
          {
            type: 'Lunch',
            time: '1:30 PM',
            items: '1 cup dal or chole\n2 phulkas\nLarge bowl salad',
          },
          {
            type: 'Evening Snack',
            time: '5:00 PM',
            items: 'Buttermilk / green tea\nRoasted chana',
          },
          {
            type: 'Dinner',
            time: '8:30 PM',
            items: 'Paneer bhurji / tofu stir-fry\n1–2 phulkas or small rice portion\nStir-fried veggies',
          },
        ],
      },
    ];
  }

  addDay() {
    const id = this.nextDayId++;
    this.dayPlans.push({
      id,
      name: `Day ${id}`,
      subtitle: 'Customize meals for this day',
      meals: [
        { type: 'Breakfast', time: '', items: '' },
        { type: 'Lunch', time: '', items: '' },
        { type: 'Snack', time: '', items: '' },
        { type: 'Dinner', time: '', items: '' },
      ],
    });
  }

  trackByDayId(index: number, day: DayPlan) {
    return day.id;
  }

  splitLines(text: string): string[] {
    return text
      ? text.split('\n').filter(line => line.trim().length > 0)
      : [];
  }

  generateWithAi() {
    if (!this.client) return;

    const req = {
      client: this.client,
      days: this.dayPlans.length || 1,
      dietaryPreference: '' // can add UI later
    };

    this.aiPlanService.generatePlan(req).subscribe({
      next: (res) => {
        this.dayPlans = res.dayPlans;
        // also update PlanState so if you later navigate this is kept
        this.planState.setDayPlans(this.dayPlans);
      },
      error: (err) => {
        console.error('AI plan generation failed', err);
        alert('Something went wrong while generating plan from AI.');
      }
    });

  }

  goToPreview() {
    if (!this.client) return;
    this.planState.setDayPlans(this.dayPlans); // make sure latest changes are stored
    this.router.navigate(['/preview']);
  }
}


