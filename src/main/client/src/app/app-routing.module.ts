import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './core/home/home.component';
import { LoginComponent } from './core/auth/login/login.component';
import { GitComponent } from './core/version/git/git.component';
import { ProfileComponent } from './core/user/profile/profile.component';
import { PasswordComponent } from './core/user/password/password.component';
import { JavaComponent } from './core/version/java/java.component';
import { EnvComponent } from './core/version/env/env.component';
import { BuildComponent } from './core/version/build/build.component';


const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'users', component: HomeComponent },
  { path: 'build', component: BuildComponent },
  { path: 'javaprops', component: JavaComponent },
  { path: 'environment', component: EnvComponent },
  { path: 'git', component: GitComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'passwd', component: PasswordComponent },
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes,
        // { enableTracing: true } // <-- debugging purposes only
      )],
  exports: [RouterModule]
})

export class AppRoutingModule { }
