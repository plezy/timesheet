import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './core/home/home.component';
import { LoginComponent } from './core/auth/login/login.component';
import { GitComponent } from './core/version/git/git.component';


const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'users', component: HomeComponent },
  { path: 'sources', component: GitComponent },
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
