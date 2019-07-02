import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Customer } from 'src/app/common/model/customer';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

export interface CustomerAddEditDialogData {
  title: string;
  customer: Customer;
}

@Component({
  selector: 'app-cust-add-edit-dialog',
  templateUrl: './cust-add-edit-dialog.component.html',
  styles: [ `
    .fill-space {
      flex: 1 1 auto;
    }
  `]
})
export class CustomerAddEditDialogComponent implements OnInit {

  private form: FormGroup;
  private title: string;
  private customer: Customer;
  private editMode = true;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<CustomerAddEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CustomerAddEditDialogData) {
      this.title = data.title;
      if (data.customer) {
        this.customer = data.customer;
      } else {
        this.customer = new Customer();
        this.editMode = false;
      }

    }

  ngOnInit() {
    this.form = this.fb.group({
      name: [this.customer.name, [Validators.required, Validators.minLength(5), Validators.maxLength(10)]],
    });
  }

  cancel(): void {
    this.dialogRef.close();
  }

  save() {
    if (this.form.valid) {
      this.customer.name = this.form.controls.name.value;
      this.dialogRef.close( { user: this.customer } );
    }
  }
}
