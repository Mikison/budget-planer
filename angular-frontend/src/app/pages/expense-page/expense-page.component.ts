import { Component } from '@angular/core';
import {ExpensesFormComponent} from "./components/expenses-form/expenses-form.component";
import {ExpenseStatisticsComponent} from "./components/expense-statistics/expense-statistics.component";
import {PercentageOverviewComponent} from "./components/percentage-overview/percentage-overview.component";
import {ExpensesTableComponent} from "./components/expenses-table/expenses-table.component";

@Component({
  selector: 'app-expense-page',
  standalone: true,
  imports: [
    ExpensesFormComponent,
    ExpenseStatisticsComponent,
    PercentageOverviewComponent,
    ExpensesTableComponent
  ],
  templateUrl: './expense-page.component.html',
  styleUrl: './expense-page.component.css'
})
export class ExpensePageComponent {

}
