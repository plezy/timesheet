import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
  { path: '', redirectTo: '/core/home', pathMatch: 'full' },
  { path: 'core', loadChildren: './core/core.module#CoreModule'},
  { path: '**', redirectTo: '/core/home' }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes,
         { enableTracing: true } // <-- debugging purposes only
      )],
  exports: [RouterModule]
})

export class AppRoutingModule { }
