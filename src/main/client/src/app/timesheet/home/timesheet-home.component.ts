import { Component, OnInit } from '@angular/core';
import {CalendarDate} from "../../common/components/big-calendar/big-calendar.component";
import {BigCalendarService} from "../../common/components/big-calendar/big-calendar.service";

@Component({
  selector: 'timesheet-home',
  templateUrl: './timesheet-home.component.html',
  styleUrls: ['./timesheet-home.component.css']
})
export class TimeSheetHomeComponent implements OnInit {

  selectedDate: CalendarDate;

  constructor(private calendarService: BigCalendarService) {
  }

  ngOnInit(): void {

  }

  calendarDateClicked(event) {
    this.selectedDate = event;
  }

  onMonthChanged() {
    console.log("Month changed");
    delete this.selectedDate;
  }

}
