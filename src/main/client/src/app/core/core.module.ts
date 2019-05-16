import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoreRoutingModule } from './core-routing.module';

import { HomeComponent } from './home/home.component';
import { GitComponent } from './version/git/git.component';
import { ProfileComponent } from './user/profile/profile.component';
import { PasswordComponent } from './user/password/password.component';
import { LoginComponent } from './auth/login/login.component';
import { MaterialModule } from '../material.module';

@NgModule({
  declarations: [ LoginComponent, HomeComponent, GitComponent, ProfileComponent, PasswordComponent ],
  imports: [
    CommonModule,
    CoreRoutingModule,
    MaterialModule
  ]
})

export class CoreModule { }

