import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/common/services/user.service';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { fbind } from 'q';

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.css']
})
export class PasswordComponent implements OnInit {

  private form: FormGroup;
  private showPassword = false;

  private id: number;
  private username: string;
  private firstName: string;
  private lastName: string;

  constructor(private fb: FormBuilder, private userService: UserService, private authService: AuthService, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['login']);
      return;
    }

    this.form = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(16)]],
      confirm: ['', [Validators.required]],
    }, { validator: this.checkPasswords });

    console.log(this.form);

    this.userService.getMe().subscribe(
      me => {
        // console.log('Got user Id : ' + me.id);
        this.id = me.id;
        this.username = me.username;
        this.firstName = me.firstName;
        this.lastName = me.lastName;
      }
    );
  }

  checkPasswords(group: FormGroup) {
    // console.log('Checking passwords ...');
    const pass = group.controls.password.value;
    const confirm = group.controls.confirm.value;

    group.controls.confirm.setErrors( pass === confirm ? null : { notSame: true } );
  }

  toggleShowPassword() {
    console.log('toggleShowPassword');
    this.showPassword = ! this.showPassword;
    if (this.showPassword) {
      console.log('unset validators');
      this.form.controls.confirm.setValidators(null);
      this.form.setValidators(null);
    } else {
      console.log('setting back validators');
      this.form.controls.confirm.setValidators([ Validators.required ]);
      // this.form.setValidators( this.checkPasswords ) ;
    }
  }

  save() {
    const pass = this.form.controls.password.value;
    const confirm = this.form.controls.confirm.value;

    if (pass === confirm) {
      console.log('TODO SAVE');
    }
  }
}
