import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerListComponent } from './customer-list/customer-list.component';
import { CustomerRoutingModule } from './customer-routing.module';
import { MaterialModule } from '../material.module';
import { CustomerAddEditDialogComponent } from './cust-add-edit-dialog/cust-add-edit-dialog.component';

@NgModule({
  declarations: [CustomerListComponent, CustomerAddEditDialogComponent],
  imports: [
    CommonModule,
    CustomerRoutingModule,
    MaterialModule
  ],
  entryComponents: [ CustomerAddEditDialogComponent ]
})
export class CustomerModule { }
