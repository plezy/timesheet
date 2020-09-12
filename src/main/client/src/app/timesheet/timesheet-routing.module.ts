import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {TimeSheetHomeComponent} from "./home/timesheet-home.component";

const routes: Routes = [
  { path: '', redirectTo: '/timesheet/home', pathMatch: 'full' },
  { path: 'home', component: TimeSheetHomeComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TimesheetRoutingModule { }
