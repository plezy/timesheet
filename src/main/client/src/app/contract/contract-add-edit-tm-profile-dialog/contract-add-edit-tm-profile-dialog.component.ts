import { Component, OnInit, Inject } from '@angular/core';
import { ContractProfile } from 'src/app/common/model/contractProfile';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface ContractAddEditTmProfileDialogData {
  title: string;
  profile: ContractProfile;
}

const validatorsName = [Validators.required, Validators.minLength(2), Validators.maxLength(64)];
const validatorsDesc = [Validators.required, Validators.minLength(6), Validators.maxLength(1024)];

@Component({
  selector: 'app-contract-add-edit-tm-profile-dialog',
  templateUrl: './contract-add-edit-tm-profile-dialog.component.html',
  styles: [ `
  `]
})
export class ContractAddEditTmProfileDialogComponent implements OnInit {

  public title: string;
  private profile: ContractProfile;
  public editMode = true;
  public form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ContractAddEditTmProfileDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ContractAddEditTmProfileDialogData) {
      this.title = data.title;
      if (data.profile) {
        this.profile = data.profile;
      } else {
        this.profile = new ContractProfile();
        this.profile.minimumDailyInvoiced = 8;
        this.profile.maximumDailyInvoiced = 8;
        this.profile.multipleUnitInvoiced = 0;
        this.editMode = false;
      }
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: [this.profile.name, validatorsName],
      description: [this.profile.description, validatorsDesc],
      hourlyRate: [this.profile.hourlyRate, Validators.required],
      minimumDailyInvoiced: [this.profile.minimumDailyInvoiced, Validators.required],
      maximumDailyInvoiced: [this.profile.maximumDailyInvoiced, Validators.required],
      multipleUnitInvoiced: [this.profile.multipleUnitInvoiced, Validators.required],
    });
  }

  cancel(): void {
    this.dialogRef.close();
  }

  ok() {
    if (this.form.valid) {
      this.profile.name = this.form.controls.name.value;
      this.profile.description = this.form.controls.description.value;
      this.profile.hourlyRate = this.form.controls.hourlyRate.value;
      this.profile.minimumDailyInvoiced = this.form.controls.minimumDailyInvoiced.value;
      this.profile.maximumDailyInvoiced = this.form.controls.maximumDailyInvoiced.value;
      this.profile.multipleUnitInvoiced = this.form.controls.multipleUnitInvoiced.value;
      this.dialogRef.close( { profile: this.profile } );
    }

  }

}
