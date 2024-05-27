import { Component } from '@angular/core';
import {InputGroupModule} from "primeng/inputgroup";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {DropdownModule} from "primeng/dropdown";
import {InputTextModule} from "primeng/inputtext";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CalendarModule} from "primeng/calendar";

@Component({
  selector: 'app-expenses-form',
  standalone: true,
  imports: [
    InputGroupModule,
    InputGroupAddonModule,
    DropdownModule,
    InputTextModule,
    FormsModule,
    CalendarModule,
    ReactiveFormsModule
  ],
  templateUrl: './expenses-form.component.html',
  styleUrl: './expenses-form.component.css'
})
export class ExpensesFormComponent {


}
