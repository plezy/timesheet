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

  public form: FormGroup;

  private id: number;
  public username: string;
  public firstName: string;
  public lastName: string;

  constructor(private fb: FormBuilder, private userService: UserService, private authService: AuthService, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['/core/login']);
      return;
    }

    this.form = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(16)]],
      confirm: ['', [Validators.required]],
    }, { validator: this.checkPasswords });

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

  save() {
    const pass = this.form.controls.password.value;
    const confirm = this.form.controls.confirm.value;

    if (pass === confirm) {
      this.userService.updateUserPassword(this.id, pass).subscribe(
        res => {
          if (res) {
            console.log('Password updated');
          }
        },
        err => console.log(err)
      );
    }
    this.router.navigate(['/core/profile']);
  }
}
