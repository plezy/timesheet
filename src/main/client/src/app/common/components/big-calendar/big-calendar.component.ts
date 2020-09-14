import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import * as moment from "moment";

const monthNames: string[] = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"];

export interface CalendarDate {
  momentDate: moment.Moment;
  selected?: boolean;
  isToday?: boolean;
  isCurrentMonth?: boolean;
}

@Component({
  selector: 'big-calendar',
  templateUrl: './big-calendar.component.html',
  styleUrls: ['./big-calendar.component.scss']
})

export class BigCalendarComponent implements OnInit {

  dayNames: string[] = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"];

  monthName: string;
  month: number;
  year: number;

  currentDate: moment.Moment;
  today = moment();

  weeks: CalendarDate[][] = [];

  @Input('autoSwitchMonth')
  autoSwitchMonth: boolean = false;
  @Input('selectOnlyInMonth')
  selectOnlyInMonth: boolean = false;

  @Output()
  onItemClicked: EventEmitter<CalendarDate> = new EventEmitter<CalendarDate>();
  @Output()
  onMonthChanged: EventEmitter<void> = new EventEmitter<void>();

  constructor() { }

  ngOnInit() {
    this.setMoment(this.today);
  }

  setMoment(date: moment.Moment) {
    this.currentDate = date;
    this.updateDate();
  }

  setDate(date: Date) {
    this.currentDate = moment(date);
    this.updateDate();
  }

  private updateDate() {
    this.month = this.currentDate.month();
    this.monthName = monthNames[this.month];
    this.year = this.currentDate.year();
    const dates = this.fillCalendarArray(this.currentDate);
    // removes week beyond the current month
    if (!dates[35].isCurrentMonth) {
      dates.splice(35);
    }
    // rearrange calendar dates in weeks
    this.weeks = [];
    while (dates.length > 0) {
      this.weeks.push(dates.splice(0, 7));
    }
    //console.log(this.weeks);
    this.onMonthChanged.emit();
  }

  isToday(date: moment.Moment): boolean {
    return this.today.isSame(moment(date), 'day');
  }

  fillCalendarArray(currentMoment: moment.Moment): CalendarDate[] {
    // first day of month (from monday 0, to sunday 6)
    const firstOfMonth = (moment(currentMoment).startOf('month').day() + 6) % 7;
    console.log('firstOfMonth : ' + firstOfMonth);
    // compute date of first day on the grid
    const firstDayOfGrid = moment(currentMoment).startOf('month').subtract(firstOfMonth, 'd');
    console.log('firstDayOfGrid : ' + firstDayOfGrid);
    const start = firstDayOfGrid.date();
    console.log('start of calendar : ' + start);

    return Array.from(Array(42).keys()).map((i): CalendarDate => {
      const day = firstDayOfGrid.clone().add(i, 'd');
      //console.log(day);
      return {
        momentDate: day,
        selected: false,
        isToday: this.isToday(day),
        isCurrentMonth: day.month() == this.month
      }
    });
  }

  incrementMonth() {
    this.currentDate.add(1, 'M');
    this.updateDate();
  }

  decrementMonth() {
    this.currentDate.add(-1, 'M');
    this.updateDate();
  }

  itemClicked(item: CalendarDate) {
    console.log("Item clicked");
    if (!this.selectOnlyInMonth || item.isCurrentMonth) {
      this.onItemClicked.emit(item);
      if ((!item.isCurrentMonth) && this.autoSwitchMonth) {
        //console.log("Autoswitching month");
        this.setMoment(item.momentDate);
      }
    }
  }
}
