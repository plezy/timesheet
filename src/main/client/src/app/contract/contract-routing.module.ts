import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { ContractListComponent } from './contract-list/contract-list.component';
import { ContratAddEditTmDetailsComponent } from './contrat-add-edit-tm-details/contrat-add-edit-tm-details.component';

const contractRoutes: Routes = [
    { path: '', redirectTo: '/cntrct/home', pathMatch: 'full' },
    { path: 'home', component: ContractListComponent },
    { path: 'tm-details', component: ContratAddEditTmDetailsComponent },
];

@NgModule({
    imports: [RouterModule.forChild(contractRoutes)],
    exports: [RouterModule]
  })

export class ContractRoutingModule { }
