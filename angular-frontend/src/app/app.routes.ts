import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: 'register',
    loadComponent: () => import('./pages/register-page/register-page.component').then(m => m.RegisterPageComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login-page/login-page.component').then(m => m.LoginPageComponent)
  },
  {
    path: 'budget',
    loadComponent: () => import('./pages/budget-category/budget-category.component').then(m => m.BudgetCategoryComponent)
  }

];
