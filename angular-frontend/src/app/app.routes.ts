import { Routes } from '@angular/router';

export const routes: Routes = [
  {path: "expenses", loadComponent: () => import('./pages/expense-page/expense-page.component').then(m => m.ExpensePageComponent)},
];
