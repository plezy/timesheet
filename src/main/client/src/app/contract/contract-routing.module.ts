import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { ContractListComponent } from './contract-list/contract-list.component';
import { ContractListTmDetailsComponent } from './contract-list-tm-details/contract-list-tm-details.component';

const contractRoutes: Routes = [
    { path: '', redirectTo: '/cntrct/home', pathMatch: 'full' },
    { path: 'home', component: ContractListComponent },
    { path: 'tm-details', component: ContractListTmDetailsComponent },
];

@NgModule({
    imports: [RouterModule.forChild(contractRoutes)],
    exports: [RouterModule]
  })

export class ContractRoutingModule { }
