import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserAddEditDialogData } from '../user-add-edit-dialog/user-add-edit-dialog.component';
import { User } from 'src/app/common/model/user';

@Component({
  selector: 'app-user-set-password-dialog',
  templateUrl: './user-set-password-dialog.component.html',
  styles: [ `
    .fill-space {
      flex: 1 1 auto;
    }
  `]
})
export class UserSetPasswordDialogComponent implements OnInit {

  private form: FormGroup;
  private user: User;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<UserSetPasswordDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: UserAddEditDialogData) {

      this.user = data.user;
  }

  ngOnInit() {
    this.form = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(16)]],
      confirmPwd: ['', [Validators.required]],
    }, { validator: this.checkPasswords });
  }

  checkPasswords(group: FormGroup) {
    // console.log('Checking passwords ...');
    const pass = group.controls.password.value;
    const confirm = group.controls.confirmPwd.value;

    group.controls.confirmPwd.setErrors( pass === confirm ? null : { notSame: true } );
  }

  cancel(): void {
    this.dialogRef.close();
  }

  save() {
    if (this.form.valid) {
      this.dialogRef.close( { pwd: this.form.controls.password.value } );
    }
  }

}
