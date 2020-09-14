import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TimesheetRoutingModule } from './timesheet-routing.module';
import { TimeSheetHomeComponent } from './home/timesheet-home.component';
import {BigCalendarComponent} from "../common/components/big-calendar/big-calendar.component";


@NgModule({
  declarations: [
    TimeSheetHomeComponent,
    BigCalendarComponent
  ],
  imports: [
    CommonModule,
    TimesheetRoutingModule
  ]
})
export class TimesheetModule { }
