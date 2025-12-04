import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientStateService, ClientDetails } from '../../services/client-state.service';
import { PlanStateService } from '../../services/plan-state.service';
import { DayPlan } from '../../models/plan.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AiPlanService } from '../../services/ai-plan.service';
import { RagCoachNotesService } from '../../services/rag-coach-notes.service';

@Component({
  selector: 'app-plan-builder',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './plan-builder.component.html',
  styleUrls: ['./plan-builder.component.css']
})
export class PlanBuilderComponent implements OnInit {

  client: ClientDetails | null = null;
  dayPlans: DayPlan[] = [];
  coachNotes: string = '';
  private nextDayId = 2;
  isGeneratingNotes = false;

  constructor(
    private clientState: ClientStateService,
    private planState: PlanStateService,
    private aiPlanService: AiPlanService,
    private ragCoachNotesService: RagCoachNotesService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.client = this.clientState.getClientDetails();

    if (!this.client) {
      this.router.navigate(['/client']);
      return;
    }

    // restore existing state if any
    this.coachNotes = this.planState.getCoachNotes();

    if (this.planState.hasPlan()) {
      this.dayPlans = this.planState.getDayPlans();
      this.nextDayId = this.dayPlans.length + 1;
      return;
    }

    // default day 1
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

  generateWithAi() {
    if (!this.client) return;

    const req = {
      client: this.client,
      days: this.dayPlans.length || 1,
      dietaryPreference: ''
    };

    this.aiPlanService.generatePlan(req).subscribe({
      next: (res) => {
        this.dayPlans = res.dayPlans;
        this.planState.setDayPlans(this.dayPlans);
      },
      error: (err) => {
        console.error('AI plan generation failed', err);
        alert('Something went wrong while generating plan from AI.');
      }
    });
  }

  generateCoachNotesWithRag() {
    if (!this.client || !this.dayPlans.length) return;

    this.isGeneratingNotes = true;

    const req = {
      client: this.client,
      dayPlans: this.dayPlans
    };

    this.ragCoachNotesService.generateCoachNotes(req).subscribe({
      next: (res) => {
        this.coachNotes = res.coachNotes || '';
        this.planState.setCoachNotes(this.coachNotes);
        this.isGeneratingNotes = false;
      },
      error: (err) => {
        console.error('RAG coach notes failed', err);
        alert('Could not generate coach notes right now.');
        this.isGeneratingNotes = false;
      }
    });
  }

  onCoachNotesChange(value: string) {
    this.coachNotes = value;
    this.planState.setCoachNotes(this.coachNotes);
  }

  goToPreview() {
    if (!this.client) return;
    this.planState.setDayPlans(this.dayPlans);
    this.planState.setCoachNotes(this.coachNotes);
    this.router.navigate(['/preview']);
  }
}
