import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { ContractListComponent } from './contract-list/contract-list.component';

const contractRoutes: Routes = [
    { path: '', redirectTo: '/cntrct/home', pathMatch: 'full' },
    { path: 'home', component: ContractListComponent },
];

@NgModule({
    imports: [RouterModule.forChild(contractRoutes)],
    exports: [RouterModule]
  })

export class ContractRoutingModule { }
