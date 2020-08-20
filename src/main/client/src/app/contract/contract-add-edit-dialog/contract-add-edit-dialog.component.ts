import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatOptionSelectionChange } from '@angular/material';
import { Contract } from 'src/app/common/model/contract';
import { ContractType } from 'src/app/common/model/contractType';
import { ContractService } from 'src/app/common/services/contract.service';
import { CustomerDto } from 'src/app/common/model/customer';
import { debounceTime, tap, switchMap, finalize } from 'rxjs/operators';
import { CustomerService } from 'src/app/common/services/customer.service';

export interface ContractAddEditDialogData {
  title: string;
  contract: Contract;
}

const validatorsName = [Validators.required, Validators.minLength(6), Validators.maxLength(64)];

@Component({
  selector: 'app-contract-add-edit-dialog',
  templateUrl: './contract-add-edit-dialog.component.html',
  styles: [ `
  `]
})
export class ContractAddEditDialogComponent implements OnInit {

  private title: string;
  private contract: Contract;
  public editMode = true;
  private form: FormGroup;

  /** Customer autocomplete */
  filteredCustomerList: any;
  selectedCustomer: CustomerDto;
  isLoading = false;

  private types: ContractType[];
  /*
   = [
    {id: "PROJECT", description: "Projet"},
    {id: "TIMES_MEANS", description: "Times and Means"},
  ];
  */
  constructor(
    private contractService: ContractService,
    private customerService: CustomerService,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ContractAddEditDialogComponent>,

    @Inject(MAT_DIALOG_DATA) public data: ContractAddEditDialogData) {
      this.title = data.title;
      if (data.contract) {
        this.contract = data.contract;
      } else {
        this.contract = new Contract();
        this.editMode = false;
      }
    }

  ngOnInit() {
    this.contractService.getContractTypes().subscribe(
      allTypes => {
        // console.log(allRoles);
        this.types = allTypes;
    });

    console.log(this.contract);
    this.form = this.fb.group({
        name: [this.contract.name, validatorsName],
        description: [this.contract.description, []],
        contractType: [{value: this.contract.contractType, disabled: this.editMode}, []],
        customer: [{value: this.contract.customer, disabled: this.editMode}, []],
        orderNumber: [this.contract.orderNumber, []],
        orderDate: [this.contract.orderDate, []],
        plannedStart: [this.contract.plannedStart, []],
        plannedEnd: [this.contract.plannedEnd, []],
    });

    // Init Customer Autocomplete control
    this.form.get('customer').valueChanges
    .pipe(
      debounceTime(500),
      tap(() => {
        console.log('Value change pipe\'s');
        this.filteredCustomerList = [];
        this.isLoading = true;
      }),
      switchMap(value => this.customerService.autocompleteCustomer(value)
        .pipe(
          finalize(() => {
            this.isLoading = false;
          }),
        )
      )
    )
    .subscribe(data => {
      console.log(data);
      if (data instanceof Array) {
        // this.errorMsg = '';
        this.filteredCustomerList = data;
      } else {
        // this.errorMsg = 'Error getting list';
        this.filteredCustomerList = [];
      }
      console.log(this.filteredCustomerList);
    });
  }

  cancel(): void {
    this.dialogRef.close();
  }

  save() {
    if (this.form.valid) {
      this.contract.name = this.form.controls.name.value;
      this.contract.description = this.form.controls.description.value;
      this.contract.orderNumber = this.form.controls.orderNumber.value;
      this.contract.orderDate = this.form.controls.orderDate.value;
      this.contract.plannedStart = this.form.controls.plannedStart.value;
      this.contract.plannedEnd = this.form.controls.plannedEnd.value;
      if (!this.editMode) {
        this.contract.contractType = this.form.controls.contractType.value;
        this.contract.customer = this.form.controls.customer.value;
      }
      this.dialogRef.close( { contract: this.contract } );
    }
  }

  /** Customer autocomplete */
  displayCustomerFn(customer?: CustomerDto): string | undefined {
    console.log('customer = ' + (customer ? customer.id : 'none selected'));
    return customer ? customer.name : undefined;
  }

  customerSelected(event: MatOptionSelectionChange) {
    console.log('option selected ! :');
    console.log( event.source.value);
    this.selectedCustomer = event.source.value as CustomerDto;
  }
}
