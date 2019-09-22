import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Contract } from 'src/app/common/model/contract';
import { ContractType } from 'src/app/common/model/contractType';
import { ContractService } from 'src/app/common/services/contract.service';

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
  private editMode = true;
  private form: FormGroup;

  private types: ContractType[];
  /*
   = [
    {id: "PROJECT", description: "Projet"},
    {id: "TIMES_MEANS", description: "Times and Means"},
  ];
  */
  constructor(
    private contractService: ContractService,
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

    this.form = this.fb.group({
        name: [this.contract.name, validatorsName],
        description: [this.contract.description, []],
        orderNumber: [this.contract.orderNumber, []],
        orderDate: [this.contract.orderDate, []],
        plannedStart: [this.contract.plannedStart, []],
        plannedEnd: [this.contract.plannedEnd, []],
        contractType: [this.contract.contractType, []],
    });
  }

  cancel(): void {
    this.dialogRef.close();
  }

  save() {
    this.dialogRef.close( { contract: this.contract } );
  }
}
