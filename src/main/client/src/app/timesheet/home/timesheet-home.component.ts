import { Component, OnInit } from '@angular/core';
import {timer} from "rxjs";
import {CalendarDate} from "../../common/components/big-calendar/big-calendar.component";
import {isDefined} from "@angular/compiler/src/util";
import {BigCalendarService} from "../../common/components/big-calendar/big-calendar.service";

@Component({
  selector: 'timesheet-home',
  templateUrl: './timesheet-home.component.html',
  styleUrls: ['./timesheet-home.component.css']
})
export class TimeSheetHomeComponent implements OnInit {

  cnt = 0;
  selectedDate: CalendarDate;

  constructor(private calendarService: BigCalendarService) {
  }

  ngOnInit(): void {

    const source = timer(4000,4000);
    const runningTimer = source.subscribe(val => {
      this.cnt ++;

      if (isDefined(this.selectedDate)) {
        console.log("Timer test : " + this.cnt);
        this.calendarService.setLine2Text({momentDate: this.selectedDate.momentDate.clone(), text: this.cnt.toString()});
        let num = Math.floor(Math.random() * 100) + 1;
        this.calendarService.setLine1Text({momentDate: this.selectedDate.momentDate.clone(), text: num.toString()});
      }
    });
  }

  calendarDateClicked(event) {
    this.selectedDate = event;
  }

  onMonthChanged() {
    console.log("Month changed");
    delete this.selectedDate;
  }

}
