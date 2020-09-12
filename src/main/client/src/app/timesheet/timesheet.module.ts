import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TimesheetRoutingModule } from './timesheet-routing.module';
import { TimeSheetHomeComponent } from './home/timesheet-home.component';


@NgModule({
  declarations: [TimeSheetHomeComponent],
  imports: [
    CommonModule,
    TimesheetRoutingModule
  ]
})
export class TimesheetModule { }
