import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/common/services/user.service';
import { User } from 'src/app/common/model/user';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private form: FormGroup;
  private id: number;
  private username: string;
  private roles: string[];

  constructor(private fb: FormBuilder, private userService: UserService, private authService: AuthService, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['login']);
      return;
    }

    this.form = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', []],
      mobile: ['', []],
    });

    this.userService.getMe().subscribe(
      me => {
        // console.log('Got user Id : ' + me.id);
        this.id = me.id;
        this.username = me.username;
        this.form.controls.firstName.setValue(me.firstName);
        this.form.controls.lastName.setValue(me.lastName);
        this.form.controls.email.setValue(me.email);
        this.form.controls.phone.setValue(me.phone);
        this.form.controls.mobile.setValue(me.mobile);
        this.roles = me.roles;
    });

  }

  get formsControl() {
    return this.form.controls;
  }

  save() {
    const user = new User();
    user.id = this.id;
    user.username = this.username;
    user.firstName = this.form.controls.firstName.value;
    user.lastName = this.form.controls.lastName.value;
    user.email = this.form.controls.email.value;
    user.phone = this.form.controls.phone.value;
    user.mobile = this.form.controls.mobile.value;

    this.userService.saveUser(user).subscribe(
      res => {

        console.log('Done');
      },
      err => console.log(err)
    );
  }

}
