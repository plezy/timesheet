import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TimesheetRoutingModule } from './timesheet-routing.module';
import { TimeSheetHomeComponent } from './home/timesheet-home.component';
import {BigCalendarComponent} from "../common/components/big-calendar/big-calendar.component";
import {MaterialModule} from "../material.module";
import { TimeTrackAddEditDialogComponent } from './time-track-add-edit-dialog/time-track-add-edit-dialog.component';


@NgModule({
  declarations: [
    TimeSheetHomeComponent,
    BigCalendarComponent,
    TimeTrackAddEditDialogComponent
  ],
    imports: [
        CommonModule,
        MaterialModule,
        TimesheetRoutingModule
    ],
  entryComponents: [ TimeTrackAddEditDialogComponent ]
})
export class TimesheetModule { }
