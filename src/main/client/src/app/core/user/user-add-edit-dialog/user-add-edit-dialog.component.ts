import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { User } from 'src/app/common/model/user';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';

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
  private editMode = true;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<UserAddEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: UserAddEditDialogData) {
      this.title = data.title;
      if (data.user) {
        this.user = data.user;
      } else {
        this.user = new User();
        this.editMode = false;
      }

  }

  ngOnInit() {
    if (this.editMode) {
      // Edit entity
      this.form = this.fb.group({
        username: [this.user.username, [Validators.required, Validators.minLength(5), Validators.maxLength(10)]],
        password: ['', []],
        confirmPwd: ['', []],
        firstname: [this.user.firstName, [Validators.required]],
        lastname: [this.user.lastName, [Validators.required]],
        phone: [ this.user.phone, []],
        mobile: [ this.user.mobile, []],
        email: [ this.user.email, [Validators.required, Validators.email]],
      });
      this.form.controls.username.disable();
    } else {
        // Adding new entity
        this.form = this.fb.group({
          username: [this.user.username, [Validators.required, Validators.minLength(5), Validators.maxLength(10)]],
          password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(16)]],
          confirmPwd: ['', [Validators.required]],
          firstname: [this.user.firstName, [Validators.required]],
          lastname: [this.user.lastName, [Validators.required]],
          phone: [ this.user.phone, []],
          mobile: [ this.user.mobile, []],
          email: [ this.user.email, [Validators.required, Validators.email]],
        }, { validator: this.checkPasswords });
    }
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
