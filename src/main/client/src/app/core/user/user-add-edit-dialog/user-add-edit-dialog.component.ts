import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { User } from 'src/app/common/model/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

export interface UserAddEditDialogData {
  title: string;
  user: User;
}
@Component({
  selector: 'app-user-add-edit-dialog',
  templateUrl: './user-add-edit-dialog.component.html',
  styles: [ `
    .fill-space {
      flex: 1 1 auto;
    }
  `]
})
export class UserAddEditDialogComponent implements OnInit {

  private form: FormGroup;
  private title: string;
  private user: User;
  private edit = true;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<UserAddEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: UserAddEditDialogData) {
      this.title = data.title;
      this.user = data.user;
  }

  ngOnInit() {
    if (! this.user) {
      // for adding entity
      this.user = new User();
      this.edit = false;
    }
    this.form = this.fb.group({
      username: [this.user.username, [Validators.required]],
      password: ['', [Validators.required]],
      confirmPwd: ['', [Validators.required]],
      firstname: [this.user.firstName, [Validators.required]],
      lastname: [this.user.lastName, [Validators.required]],
      phone: [ this.user.phone, []],
      mobile: [ this.user.mobile, []],
      email: [ this.user.email, []],
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
      this.user.username = this.form.controls.username.value;
      this.user.firstName = this.form.controls.firstname.value;
      this.user.lastName = this.form.controls.lastname.value;
      this.user.phone = this.form.controls.phone.value;
      this.user.mobile = this.form.controls.mobile.value;
      this.user.email = this.form.controls.email.value;
      this.dialogRef.close( {user: this.user, pwd: this.form.controls.password.value} );
    }
  }
}
