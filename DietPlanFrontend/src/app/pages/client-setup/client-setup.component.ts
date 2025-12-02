import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ClientStateService, ClientDetails } from '../../services/client-state.service';
import { NgForm, FormsModule } from '@angular/forms';

@Component({
  selector: 'app-client-setup',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './client-setup.component.html',
  styleUrls: ['./client-setup.component.css']
})
export class ClientSetupComponent {
  client: ClientDetails = {
    name: '',
    age: null,
    gender: '',
    goal: '',
    activityLevel: ''
  };

  constructor(
    private clientState: ClientStateService,
    private router: Router
  ) { }

  onSubmit(form: NgForm) {
    if (form.invalid) {
      return;
    }

    this.clientState.setClientDetails(this.client);
    this.router.navigate(['/plan']);
  }
}
