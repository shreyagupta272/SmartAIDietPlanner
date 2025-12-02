import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanBuilderComponent } from './plan-builder.component';

describe('PlanBuilderComponent', () => {
  let component: PlanBuilderComponent;
  let fixture: ComponentFixture<PlanBuilderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlanBuilderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanBuilderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
