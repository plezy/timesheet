import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TimesheetRoutingModule } from './timesheet-routing.module';
import { TimeSheetHomeComponent } from './home/timesheet-home.component';
import {BigCalendarComponent} from "../common/components/big-calendar/big-calendar.component";
import {MaterialModule} from "../material.module";


@NgModule({
  declarations: [
    TimeSheetHomeComponent,
    BigCalendarComponent
  ],
    imports: [
        CommonModule,
        MaterialModule,
        TimesheetRoutingModule
    ]
})
export class TimesheetModule { }
