import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContractRoutingModule } from './contract-routing.module';
import { MaterialModule } from '../material.module';
import { ContractListComponent } from './contract-list/contract-list.component';
import { ContractAddEditDialogComponent } from './contract-add-edit-dialog/contract-add-edit-dialog.component';
import { ContractListTmDetailsComponent } from './contract-list-tm-details/contract-list-tm-details.component';
import { ContractAddEditTmProfileDialogComponent } from './contract-add-edit-tm-profile-dialog/contract-add-edit-tm-profile-dialog.component';
import { MatChipsModule } from "@angular/material/chips";

@NgModule({
  declarations: [
    ContractListComponent,
    ContractAddEditDialogComponent,
    ContractListTmDetailsComponent,
    ContractAddEditTmProfileDialogComponent
  ],
  imports: [
    CommonModule,
    ContractRoutingModule,
    MaterialModule,
    MatChipsModule
  ],
  entryComponents: [ ContractAddEditDialogComponent, ContractAddEditTmProfileDialogComponent ]
})
export class ContractModule { }
