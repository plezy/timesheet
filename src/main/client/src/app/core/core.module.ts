import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoreRoutingModule } from './core-routing.module';

import { HomeComponent } from './home/home.component';
import { GitComponent } from './version/git/git.component';
import { ProfileComponent } from './user/profile/profile.component';
import { PasswordComponent } from './user/password/password.component';
import { LoginComponent } from './auth/login/login.component';
import { MaterialModule } from '../material.module';
import { EnvComponent } from './version/env/env.component';
import { JavaComponent } from './version/java/java.component';
import { BuildComponent } from './version/build/build.component';

@NgModule({
  declarations: [
    LoginComponent,
    HomeComponent,
    GitComponent,
    ProfileComponent,
    PasswordComponent,
    EnvComponent,
    JavaComponent,
    BuildComponent
  ],
  imports: [
    CommonModule,
    CoreRoutingModule,
    MaterialModule
  ]
})

export class CoreModule { }

