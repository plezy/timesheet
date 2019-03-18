import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const coreRoutes: Routes = [];

@NgModule({
  imports: [RouterModule.forChild(coreRoutes)],
  exports: [RouterModule]
})

export class CoreRoutingModule { }
