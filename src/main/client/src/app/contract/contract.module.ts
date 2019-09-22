import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContractRoutingModule } from './contract-routing.module';
import { ContractListComponent } from './contract-list/contract-list.component';
import { MaterialModule } from '../material.module';
import { ContractAddEditDialogComponent } from './contract-add-edit-dialog/contract-add-edit-dialog.component';

@NgModule({
  declarations: [ ContractListComponent, ContractAddEditDialogComponent ],
  imports: [
    CommonModule,
    ContractRoutingModule,
    MaterialModule
  ],
  entryComponents: [ ContractAddEditDialogComponent ]
})
export class ContractModule { }
