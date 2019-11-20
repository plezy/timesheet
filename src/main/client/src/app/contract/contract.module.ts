import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContractRoutingModule } from './contract-routing.module';
import { MaterialModule } from '../material.module';
import { ContractListComponent } from './contract-list/contract-list.component';
import { ContractAddEditDialogComponent } from './contract-add-edit-dialog/contract-add-edit-dialog.component';
import { ContratAddEditTmDetailsComponent } from './contrat-add-edit-tm-details/contrat-add-edit-tm-details.component';

@NgModule({
  declarations: [ ContractListComponent, ContractAddEditDialogComponent, ContratAddEditTmDetailsComponent ],
  imports: [
    CommonModule,
    ContractRoutingModule,
    MaterialModule
  ],
  entryComponents: [ ContractAddEditDialogComponent ]
})
export class ContractModule { }
