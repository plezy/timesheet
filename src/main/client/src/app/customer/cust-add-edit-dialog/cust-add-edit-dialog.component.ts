import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Customer } from 'src/app/common/model/customer';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Address } from 'src/app/common/model/address';

export interface CustomerAddEditDialogData {
  title: string;
  customer: Customer;
}

const validatorsName = [Validators.required, Validators.minLength(4), Validators.maxLength(64)];
const validatorsAddressL1 = [Validators.required, Validators.maxLength(128)];
const validatorsLen128 = [Validators.maxLength(128)];
const validatorsLen80 = [Validators.maxLength(80)];
const validatorsLen8 = [Validators.maxLength(8)];
const validatorsCity = [Validators.required, Validators.maxLength(80)];

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
        this.customer.useBillingAddress = true;
        this.editMode = false;
      }

    }

  ngOnInit() {
    if (!this.customer.address) {
      this.customer.address = new Address();
    }
    if (!this.customer.billingAddress) {
      this.customer.billingAddress = new Address();
    }
    this.form = this.fb.group({
      name: [this.customer.name, validatorsName],
      addressLine1: [this.customer.address.addressLine1, validatorsAddressL1],
      addressLine2: [this.customer.address.addressLine2, validatorsLen128],
      addressPostCode: [this.customer.address.postCode, validatorsLen8],
      addressCity: [this.customer.address.city, validatorsCity],
      addressArea: [this.customer.address.area, validatorsLen80],
      addressCountry: [this.customer.address.country, validatorsLen80],
      useBillingAddress: [this.customer.useBillingAddress, []],
      socAddressLine1: [this.customer.billingAddress.addressLine1, validatorsAddressL1],
      socAddressLine2: [this.customer.billingAddress.addressLine2, validatorsLen128],
      socAddressPostCode: [this.customer.billingAddress.postCode, validatorsLen8],
      socAddressCity: [this.customer.billingAddress.city, validatorsCity],
      socAddressArea: [this.customer.billingAddress.area, validatorsLen80],
      socAddressCountry: [this.customer.billingAddress.country, validatorsLen80],
    });

    if (!this.customer.useBillingAddress) {
      this.removeBillingValidations();
    }
  }

  removeBillingValidations(): void {
    // console.log('removeBillingValidations');
    this.form.controls.socAddressLine1.disable();
    this.form.controls.socAddressLine2.disable();
    this.form.controls.socAddressPostCode.disable();
    this.form.controls.socAddressCity.disable();
    this.form.controls.socAddressArea.disable();
    this.form.controls.socAddressCountry.disable();

  }

  setBillingValidations(): void {
    // console.log('setBillingValidations');
    this.form.controls.socAddressLine1.enable();
    this.form.controls.socAddressLine2.enable();
    this.form.controls.socAddressPostCode.enable();
    this.form.controls.socAddressCity.enable();
    this.form.controls.socAddressArea.enable();
    this.form.controls.socAddressCountry.enable();

  }

  billingAddressChanged() {
    console.log('billingAddressChanged');
    if (this.form.controls.useBillingAddress.value) {
      this.setBillingValidations();
    } else {
      this.removeBillingValidations();
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }

  save() {
    if (this.form.valid) {
      this.customer.name = this.form.controls.name.value;
      this.customer.address.addressLine1 = this.form.controls.addressLine1.value;
      this.customer.address.addressLine2 = this.form.controls.addressLine2.value;
      this.customer.address.postCode = this.form.controls.addressPostCode.value;
      this.customer.address.city = this.form.controls.addressCity.value;
      this.customer.address.area = this.form.controls.addressArea.value;
      this.customer.address.country = this.form.controls.addressCountry.value;
      this.customer.useBillingAddress = this.form.controls.useBillingAddress.value;
      if (this.customer.useBillingAddress) {
        this.customer.billingAddress.addressLine1 = this.form.controls.socAddressLine1.value;
        this.customer.billingAddress.addressLine2 = this.form.controls.socAddressLine2.value;
        this.customer.billingAddress.postCode = this.form.controls.socAddressPostCode.value;
        this.customer.billingAddress.city = this.form.controls.socAddressCity.value;
        this.customer.billingAddress.area = this.form.controls.socAddressArea.value;
        this.customer.billingAddress.country = this.form.controls.socAddressCountry.value;
      } else {
        this.customer.billingAddress = null;
      }
      
      this.dialogRef.close( { customer: this.customer } );
    }
  }


}
