import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CustomerListComponent } from './customer-list/customer-list.component';

const customerRoutes: Routes = [
    { path: '', redirectTo: '/cust/home', pathMatch: 'full' },
    { path: 'home', component: CustomerListComponent },
];

@NgModule({
    imports: [RouterModule.forChild(customerRoutes)],
    exports: [RouterModule]
  })

export class CustomerRoutingModule { }
