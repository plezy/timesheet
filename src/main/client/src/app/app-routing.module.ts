import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
  { path: '', redirectTo: '/core/home', pathMatch: 'full' },
  { path: 'core', loadChildren: () => import('./core/core.module').then(m => m.CoreModule)},
  { path: 'cust', loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule)},
  { path: 'cntrct', loadChildren: () => import('./contract/contract.module').then(m => m.ContractModule)},
  { path: '**', redirectTo: '/core/home' }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes,
        //{ enableTracing: true } // <-- debugging purposes only
      )],
  exports: [RouterModule]
})

export class AppRoutingModule { }
