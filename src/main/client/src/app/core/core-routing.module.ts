import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './auth/login/login.component';
import { UserListComponent } from './user/user-list/user-list.component';
import { ProfileComponent } from './user/profile/profile.component';
import { PasswordComponent } from './user/password/password.component';
import { HomeComponent } from './home/home.component';
import { BuildComponent } from './version/build/build.component';
import { JavaComponent } from './version/java/java.component';
import { EnvComponent } from './version/env/env.component';
import { GitComponent } from './version/git/git.component';

const coreRoutes: Routes = [
  { path: '', redirectTo: '/core/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'users', component: UserListComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'passwd', component: PasswordComponent },

  { path: 'build', component: BuildComponent },
  { path: 'javaprops', component: JavaComponent },
  { path: 'environment', component: EnvComponent },
  { path: 'git', component: GitComponent },
];

@NgModule({
  imports: [RouterModule.forChild(coreRoutes)],
  exports: [RouterModule]
})

export class CoreRoutingModule { }
